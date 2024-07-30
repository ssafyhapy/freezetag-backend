package com.ssafy.freezetag.domain.result.service;

import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.result.entity.BalanceQuestion;
import com.ssafy.freezetag.domain.result.entity.BalanceResult;
import com.ssafy.freezetag.domain.result.entity.redis.BalanceQuestionRedis;
import com.ssafy.freezetag.domain.result.entity.redis.BalanceResultRedis;
import com.ssafy.freezetag.domain.result.repository.BalanceQuestionRedisRepository;
import com.ssafy.freezetag.domain.result.repository.BalanceQuestionRepository;
import com.ssafy.freezetag.domain.result.repository.BalanceResultRedisRepository;
import com.ssafy.freezetag.domain.result.repository.BalanceResultRepository;
import com.ssafy.freezetag.domain.result.service.request.BalanceQuestionRequestDto;
import com.ssafy.freezetag.domain.result.service.request.BalanceQuestionSaveRequestDto;
import com.ssafy.freezetag.domain.result.service.request.BalanceResultSaveRequestDto;
import com.ssafy.freezetag.domain.result.service.response.BalanceQuestionResponseDto;
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
        return getOption(chatResponse);
    }

    private BalanceQuestionResponseDto getOption(ChatResponse chatResponse) {
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
    public BalanceResultRedis saveBalanceResult(BalanceResultSaveRequestDto balanceResultSaveRequestDto){
        BalanceResultRedis balanceResultRedis = new BalanceResultRedis(balanceResultSaveRequestDto.getBalanceQuestionId(),
                balanceResultSaveRequestDto.getMemberId(),
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

        balanceQuestionRepository.saveAll(balanceQuestionList);

        // TODO : Member 구현 완료되면 1L 대신 request에서 memberId 가져오기
        Member findMember = memberRepository.findById(1L).orElseThrow(() -> new RuntimeException("임시 에러 메세지 : 해당 Id에 일치하는 멤버가 없습니다."));
        deleteAndSaveBalanceResult(balanceQuestionRedisList, balanceQuestionList, findMember);

        balanceQuestionRedisRepository.deleteAll(balanceQuestionRedisList);
    }

    private void deleteAndSaveBalanceResult(List<BalanceQuestionRedis> balanceQuestionRedisList, List<BalanceQuestion> balanceQuestionList, Member findMember) {
        for (BalanceQuestionRedis balanceQuestionRedis : balanceQuestionRedisList) {
            List<BalanceResultRedis> balanceResultRedisList = balanceResultRedisRepository.findAllByBalanceQuestionId(balanceQuestionRedis.getId());

            List<BalanceResult> balanceResultList = balanceResultRedisList.stream()
                    .map(balanceResultRedis -> {
                        BalanceQuestion balanceQuestion = balanceQuestionList.stream()
                                .filter(bq -> bq.getBalanceQuestionOptionFirst().equals(balanceQuestionRedis.getOptionFirst()) && bq.getBalanceQuestionOptionSecond().equals(balanceQuestionRedis.getOptionSecond()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("매칭되는 질문을 찾을 수 없습니다."));
                        return new BalanceResult(balanceQuestion, findMember, balanceResultRedis.getBalanceResultSelectedOption());
                    })
                    .toList();

            balanceResultRepository.saveAll(balanceResultList);
            balanceResultRedisRepository.deleteAll(balanceResultRedisList);
        }
    }

}
