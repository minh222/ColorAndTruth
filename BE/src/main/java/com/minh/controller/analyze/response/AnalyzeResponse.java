package com.minh.controller.analyze.response;

import java.util.Collections;
import java.util.List;

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

    public String getOriginal() {
        return original;
    }



    public List<String> getEmotion() {
        if (emotion.isEmpty()) {
            return Collections.singletonList(" - ");
        }
        return emotion;
    }

    public String getAttitude() {
        if (attitude.isEmpty()) {
            return " - ";
        }
        return attitude;
    }

    public String getClaim() {
        return claim;
    }


}
