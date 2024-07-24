package com.ssafy.freezetag.domain.result.controller;

import com.ssafy.freezetag.domain.result.entity.redis.IntroRedis;
import com.ssafy.freezetag.domain.result.service.IntroResultService;
import com.ssafy.freezetag.domain.result.service.request.IntroAllRequestDto;
import com.ssafy.freezetag.domain.result.service.request.IntroModifyRequestDto;
import com.ssafy.freezetag.domain.result.service.request.IntroSaveRequestDto;
import com.ssafy.freezetag.domain.result.service.response.IntroSaveResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/result")
@RequiredArgsConstructor
public class ResultController {

    private final IntroResultService introResultService;

    @PostMapping("/intro")
    public ResponseEntity<?> saveIntro(@RequestBody IntroSaveRequestDto introSaveRequestDto){
        IntroRedis saveIntroRedis = introResultService.save(introSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Result<>(true, new IntroSaveResponseDto(saveIntroRedis.getId(),
                                                                        saveIntroRedis.getRoomId(),
                                                                        saveIntroRedis.getMemberRoomId(),
                                                                        saveIntroRedis.getContent())));
    }

    @PostMapping("/intro/modify")
    public ResponseEntity<?> modifyIntro(@RequestBody IntroModifyRequestDto introModifyRequestDto){
        IntroRedis modifiedIntroRedis = introResultService.modify(introModifyRequestDto);
        return ResponseEntity.ok()
                .body(new Result<>(true, modifiedIntroRedis));
    }

    @GetMapping("/intro")
    public ResponseEntity<?> getIntros(@RequestBody IntroAllRequestDto introAllRequestDto){
        List<IntroRedis> introRedisList = introResultService.findAllByRoomId(introAllRequestDto);

        return ResponseEntity.ok()
                .body(new Result<>(true, introRedisList));
    }

    @DeleteMapping("/intro")
    public ResponseEntity<?> deleteIntros(@RequestBody IntroAllRequestDto introAllRequestDto){
        introResultService.deleteAll(introAllRequestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private boolean success;
        private T data;
    }
}
