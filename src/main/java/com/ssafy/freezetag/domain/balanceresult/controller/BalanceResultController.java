package com.ssafy.freezetag.domain.balanceresult.controller;

import com.ssafy.freezetag.domain.balanceresult.entity.BalanceQuestionRedis;
import com.ssafy.freezetag.domain.balanceresult.entity.BalanceResultRedis;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceResultSaveRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.BalanceResultService;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceQuestionRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceQuestionSaveRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.response.BalanceQuestionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/result/balance")
@RestController
public class BalanceResultController implements BalanceResultControllerSwagger{

    private final BalanceResultService balanceResultService;

    @GetMapping("/question")
    public ResponseEntity<?> getBalanceQuestion(@RequestBody BalanceQuestionRequestDto balanceQuestionRequestDto) {
        BalanceQuestionResponseDto question = balanceResultService.getQuestion(balanceQuestionRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result<>(true, question));
    }

    @PostMapping("/question")
    public ResponseEntity<?> saveBalanceQuestion(@RequestBody BalanceQuestionSaveRequestDto balanceQuestionSaveRequestDto){
        BalanceQuestionRedis savedBalanceQuestion = balanceResultService.saveBalanceQuestion(balanceQuestionSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Result<>(true, savedBalanceQuestion));
    }

    // 타이머 종료시 사용자들의 선택 저장하기
    @PostMapping("/selection")
    public ResponseEntity<?> saveBalanceResult(@RequestBody BalanceResultSaveRequestDto balanceResultSaveRequestDto){
        BalanceResultRedis savedBalanceResult = balanceResultService.saveBalanceResult(balanceResultSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Result<>(true, savedBalanceResult));
    }

    @DeleteMapping("/question/{roomId}")
    public ResponseEntity<?> deleteBalanceQuestion(@PathVariable Long roomId){
        balanceResultService.deleteBalanceQuestion(roomId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private boolean success;
        private T data;
    }
}
