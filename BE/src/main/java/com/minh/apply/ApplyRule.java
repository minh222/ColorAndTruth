package com.minh.apply;


import static com.minh.config.Config.STRING_UTIL;
import static com.minh.rule.Rule.textRules;

import com.minh.rule.TextRule;

import java.util.*;


public class ApplyRule {

    public static Output apply(String original) {
        Set<String> emotions = new HashSet<>();
        Set<String> attitudes = new HashSet<>();
        String claim = STRING_UTIL.normalize(original);

        for (TextRule textRule : textRules) {
            if (textRule.inside(original)) {
                claim = textRule.apply(claim, emotions, attitudes);
            }
        }

        return new Output(
                original,
                new ArrayList<>(emotions),
                String.join(", ", attitudes),
                STRING_UTIL.normalize(claim)
        );
    }

    public static Output exact(String original) {
        Set<String> emotions = new HashSet<>();
        Set<String> attitudes = new HashSet<>();
        String claim = STRING_UTIL.normalize(original);


        return new Output(
                original,
                new ArrayList<>(emotions),
                String.join(", ", attitudes),
                STRING_UTIL.normalize(claim)
        );
    }


}
