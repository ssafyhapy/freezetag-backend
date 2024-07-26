package com.ssafy.freezetag.domain.result.service;

import com.ssafy.freezetag.domain.result.repository.BalanceQuestionRedisRepository;
import com.ssafy.freezetag.domain.result.service.request.BalanceQuestionRequestDto;
import com.ssafy.freezetag.domain.result.service.response.BalanceQuestionResponseDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceResultService {

    private final OpenAiChatModel openAiChatModel;
    private final BalanceQuestionRedisRepository balanceQuestionRedisRepository;

    public List<BalanceQuestionResponseDto> getQuestion(BalanceQuestionRequestDto balanceQuestionRequestDto) throws JSONException {

        String prompt = "1. 당신은 밸런스 게임 주제를 받을거야" +
                "2. 주제는 그룹이 모이는 목적이 될거야" +
                "3. 주어지는 예시 그룹 목적과 예시 선택지를 참고해서 7개 선택지를 만들어줘" +
                "4. 예시 그룹 목적: 우리는 개발자 신입으로 회사에 첫 출근하는 주니어 개발자들이야" +
                "5. 예시 선택지: 말 안통하는 외국인 기획자와 협업 vs 말 안통하는 외국인 개발 팀장" +
                "4. 너는 정해진 JSON 형식으로만 응답해야돼" +
                "5. JSON 형식은 다음과 같아 optionFirst:산 optionSecond:바다" +
                "6. 우리의 웹서비스는 처음 만나는 사람들이 밸런스게임을 하며 서로 알아가고 친해지는 서비스야"
                + balanceQuestionRequestDto.getPurpose();

        ChatResponse chatResponse = openAiChatModel.call(
                new Prompt(prompt,
                        OpenAiChatOptions.builder()
                                .withModel("gpt-4o")
                                .withTemperature(0.4F)
                                .build())
        );

        String jsonResponse = extractJsonResponse(chatResponse);

        //return chatResponse;
        return parseJsonResponse(jsonResponse);
    }

    private String extractJsonResponse(ChatResponse chatResponse) throws JSONException {
        // JSON 형식의 응답을 포함한 내용 추출
        String content = chatResponse.getResults().get(0).getOutput().getContent();
        // JSON 응답 내용에서 JSON 부분만 추출
        int startIdx = content.indexOf("[");
        int endIdx = content.lastIndexOf("]") + 1;
        if (startIdx >= 0 && endIdx > startIdx) {
            return content.substring(startIdx, endIdx);
        }
        throw new JSONException("Failed to extract JSON response.");
    }

    private List<BalanceQuestionResponseDto> parseJsonResponse(String jsonResponse) throws JSONException {
        List<BalanceQuestionResponseDto> responseList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonResponse);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String firstOption = jsonObject.getString("optionFirst");
            String secondOption = jsonObject.getString("optionSecond");

            BalanceQuestionResponseDto responseDto = new BalanceQuestionResponseDto(firstOption, secondOption);
            responseList.add(responseDto);
        }

        return responseList;
    }
}
