package com.minh.business;

import com.minh.business.abtract.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// spec - ưu tiên readable, dev song song vs optimize
public class StringSpecImpl implements StringUtil {

    @Override
    public boolean contains(String s, String target) {
        return s.matches(".*(?<!\\p{L})" + Pattern.quote(target) + "(?!\\p{L}).*");
    }

    @Override
    public java.lang.String replace(String s, String target, String replacement) {
        return s.replaceAll(
                "(?<!\\p{L})" + Pattern.quote(target) + "(?!\\p{L})",
                Matcher.quoteReplacement(replacement)
        );

    }

    @Override
    public String normalize(String s) {
        return s.replaceAll("\\s+", " ").trim();
    }
}
