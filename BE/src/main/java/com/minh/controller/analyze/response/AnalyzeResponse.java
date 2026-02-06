package com.minh.controller.analyze.response;

import lombok.Getter;

import java.util.List;



@Getter
public class AnalyzeResponse {
    private final String original;
    private final String claim;
    private final List<String> emotion;
    private final String attitude;

    public AnalyzeResponse(String original, List<String> emotion, String attitude, String claim) {
        this.original = original;
        this.emotion = emotion;
        this.attitude = attitude;
        this.claim = claim;
    }
}
