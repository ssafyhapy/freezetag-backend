package com.ssafy.freezetag.domain.oxresult.entity;

public enum OXMotion {
    O(0), X(1), N(2);

    private int value;
    OXMotion(final int value) {
        this.value = value;
    }
}
