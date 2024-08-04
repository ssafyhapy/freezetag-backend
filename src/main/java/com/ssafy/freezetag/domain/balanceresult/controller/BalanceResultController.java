package com.ssafy.freezetag.domain.balanceresult.controller;

import com.ssafy.freezetag.domain.balanceresult.entity.BalanceQuestionRedis;
import com.ssafy.freezetag.domain.balanceresult.entity.BalanceResultRedis;
import com.ssafy.freezetag.domain.balanceresult.service.BalanceResultService;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceQuestionRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceQuestionSaveRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceResultSaveRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.response.BalanceQuestionResponseDto;
import com.ssafy.freezetag.global.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ssafy.freezetag.domain.common.CommonResponse.success;

@RequiredArgsConstructor
@RequestMapping("/api/result/balance")
@RestController
public class BalanceResultController implements BalanceResultControllerSwagger{

    private final BalanceResultService balanceResultService;

    @GetMapping("/question")
    public ResponseEntity<?> getBalanceQuestion(@RequestBody BalanceQuestionRequestDto balanceQuestionRequestDto) {
        BalanceQuestionResponseDto question = balanceResultService.getQuestion(balanceQuestionRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(success(question));
    }

    @PostMapping("/question")
    public ResponseEntity<?> saveBalanceQuestion(@RequestBody BalanceQuestionSaveRequestDto balanceQuestionSaveRequestDto){
        BalanceQuestionRedis savedBalanceQuestion = balanceResultService.saveBalanceQuestion(balanceQuestionSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(success(savedBalanceQuestion));
    }

    // 타이머 종료시 사용자들의 선택 저장하기
    @PostMapping("/selection")
    public ResponseEntity<?> saveBalanceResult(@Login Long memberId, @RequestBody BalanceResultSaveRequestDto balanceResultSaveRequestDto){
        BalanceResultRedis savedBalanceResult = balanceResultService.saveBalanceResult(memberId, balanceResultSaveRequestDto);
        
        // TODO : 어떤 값들을 반환해줄지 생각해보기
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(success(savedBalanceResult));
    }

    @DeleteMapping("/question/{roomId}")
    public ResponseEntity<?> deleteBalanceQuestion(@PathVariable Long roomId){
        balanceResultService.deleteBalanceQuestion(roomId);

        return ResponseEntity.noContent().build();
    }

}
