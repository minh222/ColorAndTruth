package com.minh.apply;

import com.minh.rule.TextRule;

import java.util.*;

import static com.minh.rule.Rule.rules;

public class ApplyRule {
    public Output apply(String input) {
        Set<String> emotions = new HashSet<>();
        Set<String> attitudes = new HashSet<>();

        for (TextRule rule : rules) {
            if (rule.match(input)) {
                rule.apply(input, emotions, attitudes);
            }
        }

        return new Output(
                input,
                normalize(input),
                new ArrayList<>(emotions),
                String.join(", ", attitudes)
        );
    }

    private String normalize(String s) {
        return s.replaceAll("\\s+", " ").trim();
    }
}
