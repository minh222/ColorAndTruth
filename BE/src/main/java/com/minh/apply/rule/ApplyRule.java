package com.minh.apply.rule;


import com.minh.controller.analyze.response.AnalyzeResponse;

import static com.minh.config.Config.STRING_UTIL;
import static com.minh.apply.rule.Rule.textRules;

import java.util.*;


public class ApplyRule {

    public static AnalyzeResponse apply(String original) {
        Set<String> emotions = new HashSet<>();
        Set<String> attitudes = new HashSet<>();
        String claim = STRING_UTIL.normalize(original);

        for (TextRule textRule : textRules) {
            if (textRule.inside(original)) {
                claim = textRule.apply(claim, emotions, attitudes);
            }
        }

        return new AnalyzeResponse(
                original,
                new ArrayList<>(emotions),
                String.join(", ", attitudes),
                STRING_UTIL.normalize(claim)
        );
    }

    public static AnalyzeResponse exact(String original) {
        Set<String> emotions = new HashSet<>();
        Set<String> attitudes = new HashSet<>();
        String claim = STRING_UTIL.normalize(original);

        return new AnalyzeResponse(
                null,
                new ArrayList<>(emotions),
                String.join(", ", attitudes),
                STRING_UTIL.normalize(claim)
        );
    }


}
