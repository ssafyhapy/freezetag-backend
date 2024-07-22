package com.ssafy.freezetag.domain.result.controller;

import com.ssafy.freezetag.domain.result.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/result")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    @PostMapping("/intro")
    public ResponseEntity<?> saveIntro(){
        resultService.save(1L, 1L, "나는 고민호입니다.");
        return ResponseEntity.ok()
                .build();
    }
}
