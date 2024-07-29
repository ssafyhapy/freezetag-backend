package com.ssafy.freezetag.domain.common.constant;

public class OpenAi {
    public static final String OPEN_AI_MODEL = "gpt-4o";
    public static final Float OPEN_AI_TEMPERATURE = 0.8F;
    public static final String DEFAULT_PROMPT = "1. 당신은 밸런스 게임 주제를 받을거야" +
            "2. 우리의 웹서비스는 처음 만나는 사람들이 밸런스게임을 하며 서로 알아가고 친해지는 서비스야 " +
            "3. 주어지는 주제(purpose)는 그룹이 모이는 목적이 될거야 " +
            "4. 주어지는 예시 그룹 목적과 예시 선택지를 참고해서 1개 선택지를 만들어줘 " +
            "5. 예시 그룹 목적: 우리는 개발자 신입으로 회사에 첫 출근하는 주니어 개발자들이야 " +
            "6. 예시 선택지: 말 안통하는 외국인 기획자와 협업 vs 말 안통하는 외국인 개발 팀장 " +
            "7. JSON 형식은 다음과 같아 optionFirst:산 optionSecond:바다 " +
            "8. JSON 앞에 ```json\\n 을 붙이지마" +
            "9. 너는 한번 제공한 선택지를 다시 제공해서는 안돼";
}
