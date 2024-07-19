package com.ssafy.freezetag.domain.member.entity;

import lombok.Getter;

@Getter
public enum Age {
    ONE(1),
    TEN(10),
    TWENTY(20),
    THIRTY(30),
    FORTY(40),
    FIFTY(50),
    SIXTY(60),
    SEVENTY(70),
    EIGHTY(80),
    NINETY(90);

    private final int value;

    Age(int value) {
        this.value = value;
    }

}

