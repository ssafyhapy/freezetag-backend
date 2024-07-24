package com.ssafy.freezetag.domain.result.controller;

import com.ssafy.freezetag.domain.result.entity.redis.IntroRedis;
import com.ssafy.freezetag.domain.result.service.IntroResultService;
import com.ssafy.freezetag.domain.result.service.request.RoomIdRequestDto;
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
        IntroRedis saveIntroRedis = introResultService.save(introSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Result<>(true, new IntroSaveResponseDto(saveIntroRedis.getId(),
                        saveIntroRedis.getRoomId(),
                        saveIntroRedis.getMemberRoomId(),
                        saveIntroRedis.getContent())));
    }

    @PostMapping("/modify")
    public ResponseEntity<?> modifyIntro(@RequestBody IntroModifyRequestDto introModifyRequestDto) {
        IntroRedis modifiedIntroRedis = introResultService.modify(introModifyRequestDto);
        return ResponseEntity.ok()
                .body(new Result<>(true, new IntroResponseDto(modifiedIntroRedis.getId(),
                        modifiedIntroRedis.getContent())));
    }

    @GetMapping()
    public ResponseEntity<?> getIntros(@RequestBody RoomIdRequestDto roomIdRequestDto) {
        List<IntroRedis> introRedisList = introResultService.findAllByRoomId(roomIdRequestDto);
        List<IntroResponseDto> introResponseDtoList = introRedisList.stream()
                .map(introRedis -> new IntroResponseDto(introRedis.getId(), introRedis.getContent())).toList();

        return ResponseEntity.ok()
                .body(new Result<>(true, introResponseDtoList));
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteIntros(@RequestBody RoomIdRequestDto roomIdRequestDto) {
        introResultService.deleteAll(roomIdRequestDto);
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
