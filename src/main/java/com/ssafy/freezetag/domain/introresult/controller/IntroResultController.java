package com.ssafy.freezetag.domain.introresult.controller;

import com.ssafy.freezetag.domain.introresult.service.IntroResultService;
import com.ssafy.freezetag.domain.introresult.service.request.IntroModifyRequestDto;
import com.ssafy.freezetag.domain.introresult.service.request.IntroSaveRequestDto;
import com.ssafy.freezetag.domain.introresult.service.response.IntroResponseDto;
import com.ssafy.freezetag.domain.introresult.service.response.IntroSaveResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ssafy.freezetag.domain.common.CommonResponse.success;

@RestController
@RequestMapping("/api/result/intro")
@RequiredArgsConstructor
public class IntroResultController implements IntroResultControllerSwagger {

    private final IntroResultService introResultService;

    @PostMapping()
    public ResponseEntity<?> saveIntro(@RequestBody IntroSaveRequestDto introSaveRequestDto) {
        IntroSaveResponseDto introSaveResponseDto = introResultService.save(introSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(success(introSaveResponseDto));
    }

    @PostMapping("/modify")
    public ResponseEntity<?> modifyIntro(@RequestBody IntroModifyRequestDto introModifyRequestDto) {
        IntroResponseDto introResponseDto = introResultService.modify(introModifyRequestDto);

        return ResponseEntity.ok()
                .body(success(introResponseDto));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getIntros(@PathVariable Long roomId) {
        List<IntroResponseDto> introResponseDtoList = introResultService.findAllByRoomId(roomId);

        return ResponseEntity.ok()
                .body(success(introResponseDtoList));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteIntros(@PathVariable Long roomId) {
        introResultService.deleteAll(roomId);
        return ResponseEntity.noContent().build();
    }
}
