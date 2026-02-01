package com.minh.business;

import com.minh.business.abtract.String;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// spec - ưu tiên readable, dev song song vs optimize
public class StringSpecImpl implements String {

    @Override
    public boolean contains(java.lang.String s, java.lang.String target) {
        return s.matches(".*(?<!\\p{L})" + Pattern.quote(target) + "(?!\\p{L}).*");
    }

    @Override
    public java.lang.String replace(java.lang.String s, java.lang.String target, java.lang.String replacement) {
        return s.replaceAll(
                "(?<!\\p{L})" + Pattern.quote(target) + "(?!\\p{L})",
                Matcher.quoteReplacement(replacement)
        );

    }

    @Override
    public java.lang.String normalize(java.lang.String s) {
        return s.replaceAll("\\s+", " ").trim();
    }
}
