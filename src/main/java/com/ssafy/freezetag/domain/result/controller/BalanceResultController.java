package com.ssafy.freezetag.domain.result.controller;

import com.ssafy.freezetag.domain.result.service.BalanceResultService;
import com.ssafy.freezetag.domain.result.service.request.BalanceQuestionRequestDto;
import com.ssafy.freezetag.domain.result.service.response.BalanceQuestionResponseDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/result/balance")
@RequiredArgsConstructor
public class BalanceResultController {

    private final BalanceResultService balanceResultService;

    @GetMapping("/question")
    public ResponseEntity<?> getBalanceQuestion(@RequestBody BalanceQuestionRequestDto balanceQuestionRequestDto) throws JSONException {
        List<BalanceQuestionResponseDto> question = balanceResultService.getQuestion(balanceQuestionRequestDto);
        return ResponseEntity.ok(question);
    }
}
