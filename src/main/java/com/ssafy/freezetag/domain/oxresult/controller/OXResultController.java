package com.ssafy.freezetag.domain.oxresult.controller;

import com.ssafy.freezetag.domain.oxresult.service.OXResultService;
import com.ssafy.freezetag.domain.oxresult.service.request.OXModifyRequestDto;
import com.ssafy.freezetag.domain.oxresult.service.request.OXSaveRequestDto;
import com.ssafy.freezetag.domain.oxresult.service.response.OXResponseDto;
import com.ssafy.freezetag.domain.oxresult.service.response.OXsResponseDto;
import com.ssafy.freezetag.global.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ssafy.freezetag.domain.common.CommonResponse.success;

@RestController
@RequestMapping("/api/result/ox")
@RequiredArgsConstructor
public class OXResultController implements OXResultControllerSwagger{

    private final OXResultService oxResultService;

    @PostMapping()
    public ResponseEntity<?> saveOX(@Login Long memberId, @RequestBody List<OXSaveRequestDto> oxSaveRequestDtoList){
        List<OXResponseDto> oxResponseDtoList = oxResultService.save(memberId, oxSaveRequestDtoList);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(success(oxResponseDtoList));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getOX(@Login Long memberId, @PathVariable Long roomId){
        List<OXResponseDto> oxResponseDtoList = oxResultService.getOX(memberId, roomId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(success(oxResponseDtoList));
    }


    @GetMapping("/{roomId}/all")
    public ResponseEntity<?> getOXs(@PathVariable Long roomId){
        List<OXsResponseDto> oxsResponseDtoList = oxResultService.getOXs(roomId);
        return ResponseEntity.ok()
                .body(success(oxsResponseDtoList));
    }

    @PatchMapping("/{roomId}/modify")
    public ResponseEntity<?> modifyOX(@Login Long memberId, @PathVariable Long roomId, @RequestBody List<OXModifyRequestDto> oxModifyRequestDtoList){
        List<OXResponseDto> oxResponseDtoList = oxResultService.modify(memberId, roomId, oxModifyRequestDtoList);
        return ResponseEntity.ok()
                .body(success(oxResponseDtoList));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteOXs(@PathVariable Long roomId){
        oxResultService.deleteAll(roomId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

}
