package com.minh.business;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// spec - ưu tiên readable
public class StringSpecImpl implements StringUtil {

    @Override
    public boolean contains(String s, String target) {
        return s.matches(".*\\b" + Pattern.quote(target) + "\\b.*");
    }

    @Override
    public String replace(String s, String target, String replacement) {
        return s.replaceAll(
                "\\b" + Pattern.quote(target) + "\\b",
                Matcher.quoteReplacement(replacement)
        );
    }

    @Override
    public String normalize(String s) {
        return s.replaceAll("\\s+", " ").trim();
    }
}
