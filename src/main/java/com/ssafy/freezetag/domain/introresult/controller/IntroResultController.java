package com.ssafy.freezetag.domain.introresult.controller;

import com.ssafy.freezetag.domain.introresult.service.IntroResultService;
import com.ssafy.freezetag.domain.introresult.service.request.IntroModifyRequestDto;
import com.ssafy.freezetag.domain.introresult.service.request.IntroSaveRequestDto;
import com.ssafy.freezetag.domain.introresult.service.response.IntroResponseDto;
import com.ssafy.freezetag.domain.introresult.service.response.IntroSaveResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/result/intro")
@RequiredArgsConstructor
public class IntroResultController implements IntroResultControllerSwagger{

    private final IntroResultService introResultService;

    @PostMapping()
    public ResponseEntity<?> saveIntro(@RequestBody IntroSaveRequestDto introSaveRequestDto) {
        IntroSaveResponseDto introSaveResponseDto = introResultService.save(introSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Result<>(true, introSaveResponseDto));
    }

    @PostMapping("/modify")
    public ResponseEntity<?> modifyIntro(@RequestBody IntroModifyRequestDto introModifyRequestDto) {
        IntroResponseDto introResponseDto = introResultService.modify(introModifyRequestDto);

        return ResponseEntity.ok()
                .body(new Result<>(true,introResponseDto));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getIntros(@PathVariable Long roomId) {
        List<IntroResponseDto> introResponseDtoList = introResultService.findAllByRoomId(roomId);

        return ResponseEntity.ok()
                .body(new Result<>(true, introResponseDtoList));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteIntros(@PathVariable Long roomId) {
        introResultService.deleteAll(roomId);
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
