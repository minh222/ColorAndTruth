package com.minh.apply;

import java.util.List;

public class Output {

    private final String original;
    private final String claim;
    private final List<String> emotion;
    private final String attitude;

    public Output(String original, String claim, List<String> emotion, String attitude) {
        this.original = original;
        this.claim = claim;
        this.emotion = emotion;
        this.attitude = attitude;
    }

    public String getOriginal() {
        return original;
    }

    public String getClaim() {
        return claim;
    }

    public List<String> getEmotion() {
        return emotion;
    }

    public String getAttitude() {
        return attitude;
    }
}
