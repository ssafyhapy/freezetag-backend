package com.ssafy.freezetag.domain.result.controller;

import com.ssafy.freezetag.domain.result.entity.redis.OXRedis;
import com.ssafy.freezetag.domain.result.service.OXResultService;
import com.ssafy.freezetag.domain.result.service.request.OXModifyRequestDto;
import com.ssafy.freezetag.domain.result.service.request.OXSaveRequestDto;
import com.ssafy.freezetag.domain.result.service.request.RoomIdRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/result/ox")
@RequiredArgsConstructor
public class OXResultController {

    private final OXResultService oxResultService;

    @PostMapping()
    public ResponseEntity<?> saveOX(@RequestBody List<OXSaveRequestDto> oxSaveRequestDtoList){
        List<OXRedis> oxRedisList = oxResultService.save(oxSaveRequestDtoList);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Result<>(true, oxRedisList));
    }

    @PatchMapping("/modify")
    public ResponseEntity<?> modifyOX(@RequestBody List<OXModifyRequestDto> oxModifyRequestDtoList){
        List<OXRedis> modifiedOXRedisList = oxResultService.modify(oxModifyRequestDtoList);
        return ResponseEntity.ok()
                .body(new Result<>(true, modifiedOXRedisList));
    }

    @GetMapping()
    public ResponseEntity<?> getOXs(@RequestBody RoomIdRequestDto roomIdRequestDto){
        List<OXRedis> oxRedisList = oxResultService.findAllByRoomId(roomIdRequestDto);
        return ResponseEntity.ok()
                .body(new Result<>(true, oxRedisList));
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteOXs(@RequestBody RoomIdRequestDto roomIdRequestDto){
        oxResultService.deleteAll(roomIdRequestDto);
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
