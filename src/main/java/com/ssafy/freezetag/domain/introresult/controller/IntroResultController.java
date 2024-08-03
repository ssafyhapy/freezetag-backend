package com.ssafy.freezetag.domain.introresult.controller;

import com.ssafy.freezetag.domain.introresult.service.IntroResultService;
import com.ssafy.freezetag.domain.introresult.service.request.IntroModifyRequestDto;
import com.ssafy.freezetag.domain.introresult.service.request.IntroSaveRequestDto;
import com.ssafy.freezetag.domain.introresult.service.response.IntroResponseDto;
import com.ssafy.freezetag.domain.introresult.service.response.IntroSaveResponseDto;
import com.ssafy.freezetag.global.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ssafy.freezetag.domain.common.CommonResponse.success;

@Slf4j
@RestController
@RequestMapping("/api/result/intro")
@RequiredArgsConstructor
public class IntroResultController implements IntroResultControllerSwagger {

    private final IntroResultService introResultService;

    @PostMapping()
    public ResponseEntity<?> saveIntro(@Login Long memberId, @RequestBody IntroSaveRequestDto introSaveRequestDto) {
        IntroSaveResponseDto introSaveResponseDto = introResultService.save(memberId, introSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(success(introSaveResponseDto));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getIntro(@Login Long memberId, @PathVariable Long roomId){
        IntroResponseDto introResponseDto = introResultService.getMyIntro(memberId, roomId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(success(introResponseDto));
    }

    @GetMapping("/{roomId}/all")
    public ResponseEntity<?> getIntros(@PathVariable Long roomId) {
        List<IntroResponseDto> introResponseDtoList = introResultService.findAllByRoomId(roomId);

        return ResponseEntity.ok()
                .body(success(introResponseDtoList));
    }

    @PostMapping("/{roomId}/modify")
    public ResponseEntity<?> modifyIntro(@Login Long memberId, @PathVariable Long roomId, @RequestBody IntroModifyRequestDto introModifyRequestDto) {
        IntroResponseDto introResponseDto = introResultService.modify(memberId, roomId, introModifyRequestDto);

        return ResponseEntity.ok()
                .body(success(introResponseDto));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteIntros(@PathVariable Long roomId) {
        introResultService.deleteAll(roomId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
