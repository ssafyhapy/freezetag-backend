package com.ssafy.freezetag.domain.result.entity;

public enum SelectedOption {
    FIRST(0), SECOND(1);

    private int value;
    SelectedOption(final int value) {
        this.value = value;
    }
}
