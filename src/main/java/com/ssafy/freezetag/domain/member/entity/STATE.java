package com.ssafy.freezetag.domain.member.entity;

public enum STATE {
    INTRO("intro"), PHOTOFIRST("photofirst"), GUESSME("guessme"), BALANCE("balance"), WRAPUP("wrapup"), PHOTOLAST("photolast");

    private final String value;

    STATE(String value) {
        this.value = value;
    }
}
