package com.minh.apply;

import com.minh.StringUlity.MutableString;
import com.minh.rule.TextRule;

import javax.naming.Context;
import java.util.*;

import static com.minh.rule.Rule.rules;

public class ApplyRule {
    public static Output apply(String claim) {
        Set<String> emotions = new HashSet<>();
        Set<String> attitudes = new HashSet<>();

        for (TextRule rule : rules) {
            if (rule.match(claim)) {
                claim = rule.apply(claim, emotions, attitudes);
            }
        }

        return new Output(
                claim,
                normalize(claim),
                new ArrayList<>(emotions),
                String.join(", ", attitudes)
        );
    }

    private static String normalize(String s) {
        return s.replaceAll("\\s+", " ").trim();
    }
}
