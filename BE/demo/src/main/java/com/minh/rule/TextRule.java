package com.minh.rule;

import java.util.Set;

public class TextRule {

    private final String target;
    private final String replacement; // "" = remove
    private final String emotion;
    private final String attitude;

    public TextRule(String target,
                    String replacement,
                    String emotion,
                    String attitude) {
        this.target = target;
        this.replacement = replacement;
        this.emotion = emotion;
        this.attitude = attitude;
    }

    public boolean match(String input) {
        return input != null && input.contains(target);
    }

    public String apply(String claim, Set<String> emotions, Set<String> attitudes) {
        if (!target.equals(replacement) && claim.contains(target)) {
            claim = claim.replace(target, replacement);
        }

        emotions.add(emotion);
        attitudes.add(attitude);
        return claim;
    }
}
