package com.minh.apply;

import static com.minh.rule.Rule.textRules;

import com.minh.rule.TextRule;

import java.util.*;

public class ApplyRule {
    public static Output apply(String original) {
        Set<String> emotions = new HashSet<>();
        Set<String> attitudes = new HashSet<>();
        String claim = original;

        for (TextRule textRule : textRules) {
            if (textRule.inside(original)) {
                claim = textRule.apply(claim, emotions, attitudes);
            }
        }

        return new Output(
                original,
                normalize(claim),
                new ArrayList<>(emotions),
                String.join(", ", attitudes)
        );
    }

    private static String normalize(String s) {
        return s.replaceAll("\\s+", " ").trim();
    }
}
