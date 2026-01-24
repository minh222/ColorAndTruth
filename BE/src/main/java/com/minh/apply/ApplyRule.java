package com.minh.apply;


import static com.minh.rule.Rule.textRules;

import com.minh.business.StringSpecImpl;
import com.minh.business.StringUtil;
import com.minh.rule.TextRule;

import java.util.*;

public class ApplyRule {
    private static final StringUtil STRING_UTIL = new StringSpecImpl();

    public static Output apply(String original) {
        original = STRING_UTIL.normalize(original);
        String claim = original;

        Set<String> emotions = new HashSet<>();
        Set<String> attitudes = new HashSet<>();

        for (TextRule textRule : textRules) {
            if (textRule.inside(original)) {
                claim = textRule.apply(claim, emotions, attitudes);
            }
        }

        return new Output(
                original,
                claim,
                new ArrayList<>(emotions),
                String.join(", ", attitudes)
        );
    }
}
