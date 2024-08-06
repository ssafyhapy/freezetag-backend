package com.ssafy.freezetag.domain.balanceresult.service;

import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.balanceresult.entity.BalanceQuestion;
import com.ssafy.freezetag.domain.balanceresult.entity.BalanceResult;
import com.ssafy.freezetag.domain.balanceresult.entity.BalanceQuestionRedis;
import com.ssafy.freezetag.domain.balanceresult.entity.BalanceResultRedis;
import com.ssafy.freezetag.domain.balanceresult.repository.BalanceQuestionRedisRepository;
import com.ssafy.freezetag.domain.balanceresult.repository.BalanceQuestionRepository;
import com.ssafy.freezetag.domain.balanceresult.repository.BalanceResultRedisRepository;
import com.ssafy.freezetag.domain.balanceresult.repository.BalanceResultRepository;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceQuestionRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceQuestionSaveRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceResultSaveRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.response.BalanceQuestionResponseDto;
import com.ssafy.freezetag.domain.room.entity.Room;
import com.ssafy.freezetag.domain.room.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssafy.freezetag.domain.common.constant.OpenAiConstant.*;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BalanceResultService {

    private final OpenAiChatModel openAiChatModel;
    private final BalanceQuestionRedisRepository balanceQuestionRedisRepository;
    private final BalanceQuestionRepository balanceQuestionRepository;
    private final BalanceResultRedisRepository balanceResultRedisRepository;
    private final BalanceResultRepository balanceResultRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    public BalanceQuestionResponseDto getQuestion(BalanceQuestionRequestDto balanceQuestionRequestDto) {

        String prompt = DEFAULT_PROMPT + balanceQuestionRequestDto.getPurpose();

        ChatResponse chatResponse = openAiChatModel.call(
                new Prompt(prompt,
                        OpenAiChatOptions.builder()
                                .withModel(OPEN_AI_MODEL)
                                .withTemperature(OPEN_AI_TEMPERATURE)
                                .build())
        );
        return parseChatResponse(chatResponse);
    }

    private BalanceQuestionResponseDto parseChatResponse(ChatResponse chatResponse) {
        String contentString = chatResponse.getResult()
                .getOutput()
                .getContent();

        JSONObject jsonObject = new JSONObject(contentString);

        String optionFirst = jsonObject.optString("optionFirst");
        String optionSecond = jsonObject.optString("optionSecond");

        return new BalanceQuestionResponseDto(optionFirst, optionSecond);
    }

    @Transactional
    public BalanceQuestionRedis saveBalanceQuestion(BalanceQuestionSaveRequestDto balanceQuestionSaveRequestDto){
        BalanceQuestionRedis balanceQuestionRedis = new BalanceQuestionRedis(balanceQuestionSaveRequestDto.getRoomId(),
                balanceQuestionSaveRequestDto.getOptionFirst(),
                balanceQuestionSaveRequestDto.getOptionSecond());

        return balanceQuestionRedisRepository.save(balanceQuestionRedis);
    }

    @Transactional
    public BalanceResultRedis saveBalanceResult(Long memberId, BalanceResultSaveRequestDto balanceResultSaveRequestDto){
        BalanceResultRedis balanceResultRedis = new BalanceResultRedis(balanceResultSaveRequestDto.getBalanceQuestionId(),
                memberId,
                balanceResultSaveRequestDto.getBalanceResultSelectedOption());

        return balanceResultRedisRepository.save(balanceResultRedis);
    }

    @Transactional
    public void deleteBalanceQuestion(Long roomId){
        List<BalanceQuestionRedis> balanceQuestionRedisList = balanceQuestionRedisRepository.findAllByRoomId(roomId);

        Room findRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("방을 찾을 수 없습니다."));

        List<BalanceQuestion> balanceQuestionList = balanceQuestionRedisList.stream()
                .map(balanceQuestionRedis -> {
                    return new BalanceQuestion(findRoom, balanceQuestionRedis.getOptionFirst(), balanceQuestionRedis.getOptionSecond());
                }).toList();

        // Redis 데이터 DBMS에 저장
        balanceQuestionRepository.saveAll(balanceQuestionList);

        // 질문(선택지)에 대한 선택들 DBMS에 저장하고 삭제하기
        deleteAndSaveBalanceResult(balanceQuestionRedisList, balanceQuestionList);

        balanceQuestionRedisRepository.deleteAll(balanceQuestionRedisList);
    }

    private void deleteAndSaveBalanceResult(List<BalanceQuestionRedis> balanceQuestionRedisList, List<BalanceQuestion> balanceQuestionList) {
        for (BalanceQuestionRedis balanceQuestionRedis : balanceQuestionRedisList) {
            List<BalanceResultRedis> balanceResultRedisList = balanceResultRedisRepository.findAllByBalanceQuestionId(balanceQuestionRedis.getId());

            List<BalanceResult> balanceResultList = balanceResultRedisList.stream()
                    .map(balanceResultRedis -> {
                        BalanceQuestion balanceQuestion = balanceQuestionList.stream()
                                .filter(bq -> bq.getBalanceQuestionOptionFirst().equals(balanceQuestionRedis.getOptionFirst()) && bq.getBalanceQuestionOptionSecond().equals(balanceQuestionRedis.getOptionSecond()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("매칭되는 질문을 찾을 수 없습니다."));
                        Member member = memberRepository.findById(balanceResultRedis.getMemberId()).orElseThrow(() -> new RuntimeException("해당 ID를 가진 회원을 찾을 수 없습니다."));
                        return new BalanceResult(balanceQuestion, member, balanceResultRedis.getBalanceResultSelectedOption());
                    })
                    .toList();

            balanceResultRepository.saveAll(balanceResultList);
            balanceResultRedisRepository.deleteAll(balanceResultRedisList);
        }
    }

    // 최종 레포트 조회
    public List<BalanceQuestion> getBalanceQuestion(Long roomId){
        return balanceQuestionRepository.findAllByRoomId(roomId);
    }

    public List<BalanceResult> getBalanceResult(Long balanceQuestionId){
        return balanceResultRepository.findAllByBalanceQuestionId(balanceQuestionId);
    }
}
