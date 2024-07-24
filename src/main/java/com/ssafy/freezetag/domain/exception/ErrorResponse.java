package com.ssafy.freezetag.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final boolean success;

    private final String errorMsg;
}
