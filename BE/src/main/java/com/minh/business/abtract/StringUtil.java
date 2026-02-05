package com.minh.business.abtract;

public interface StringUtil {
    boolean contains(String s, String target);

    String replace(String s, String target, String replacement);

    String normalize(String s);
}

