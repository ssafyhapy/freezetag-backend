package com.ssafy.freezetag.domain.oxresult.controller;

import com.ssafy.freezetag.domain.oxresult.service.OXResultService;
import com.ssafy.freezetag.domain.oxresult.service.request.OXModifyRequestDto;
import com.ssafy.freezetag.domain.oxresult.service.request.OXSaveRequestDto;
import com.ssafy.freezetag.domain.oxresult.service.response.OXResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/result/ox")
@RequiredArgsConstructor
public class OXResultController implements OXResultControllerSwagger{

    private final OXResultService oxResultService;

    @PostMapping()
    public ResponseEntity<?> saveOX(@RequestBody List<OXSaveRequestDto> oxSaveRequestDtoList){
        List<OXResponseDto> oxResponseDtoList = oxResultService.save(oxSaveRequestDtoList);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Result<>(true, oxResponseDtoList));
    }

    @PatchMapping("/modify")
    public ResponseEntity<?> modifyOX(@RequestBody List<OXModifyRequestDto> oxModifyRequestDtoList){
        List<OXResponseDto> oxResponseDtoList = oxResultService.modify(oxModifyRequestDtoList);
        return ResponseEntity.ok()
                .body(new Result<>(true, oxResponseDtoList));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getOXs(@PathVariable Long roomId){
        List<OXResponseDto> oxResponseDtoList = oxResultService.findAllByRoomId(roomId);
        return ResponseEntity.ok()
                .body(new Result<>(true, oxResponseDtoList));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteOXs(@PathVariable Long roomId){
        oxResultService.deleteAll(roomId);
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
