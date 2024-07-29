package com.ssafy.freezetag.domain.result.controller;

import com.ssafy.freezetag.domain.result.service.IntroResultService;
import com.ssafy.freezetag.domain.result.service.request.IntroModifyRequestDto;
import com.ssafy.freezetag.domain.result.service.request.IntroSaveRequestDto;
import com.ssafy.freezetag.domain.result.service.response.IntroResponseDto;
import com.ssafy.freezetag.domain.result.service.response.IntroSaveResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/result/intro")
@RequiredArgsConstructor
public class IntroResultController {

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
