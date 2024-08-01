package com.ssafy.freezetag.global.util;

import java.util.Random;

public class CodeGenerator {

    // 랜덤 객체 생성
    private static final Random random = new Random();

    // "123-456" 형식의 코드 생성
    public static String generateCode() {
        int firstPart = generateThreeDigitNumber();
        int secondPart = generateThreeDigitNumber();

        return String.format("%03d-%03d", firstPart, secondPart);
    }

    // 세 자리 숫자 생성
    private static int generateThreeDigitNumber() {
        return random.nextInt(1000);  // 0부터 999까지의 숫자 생성
    }
}
