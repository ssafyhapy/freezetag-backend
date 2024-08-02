package com.ssafy.freezetag.domain.member.entity;

public enum Visibility {
    PUBLIC(true),
    PRIVATE(false);

    private final boolean isVisible;

    Visibility(boolean isVisible) {
        this.isVisible = isVisible;
    }



}
