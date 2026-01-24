package com.minh.rule;

import com.minh.business.StringOptimizeImlp;
import com.minh.business.StringSpecImpl;
import com.minh.business.StringUtil;

import java.util.Set;

public class TextRule {
    private static final StringUtil STRING_UTIL = new StringOptimizeImlp();

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

    public boolean inside(String input) {
        return input != null && STRING_UTIL.contains(input, target);
    }


    public String apply(String claim, Set<String> emotions, Set<String> attitudes) {
        if (!target.equals(replacement) && STRING_UTIL.contains(claim, target)) {
            STRING_UTIL.replace(claim,target,replacement);
        }

        emotions.add(emotion);
        attitudes.add(attitude);
        return claim;
    }
}
