package com.minh.apply.rule;

import java.util.Arrays;
import java.util.List;

public class Rule2 {
    public static List<TextRule> textRules = Arrays.asList(
            // replace toxic
            new TextRule("con mẹ chúng mày", "", "con mẹ chúng mày", "chửi"),
            // emotion
            new TextRule("buồn quá", "", "buồn quá", "biểu lộ cảm xúc buồn"),
            // khen
            new TextRule("cuti quá", "đáng yêu quá", "cuti quá", "cuti quá"),
            //oke
            new TextRule("oke luôn", "được", "oke luôn", "rất được"),
            // remove
            new TextRule("con ơi", "", "con ơi", "cảm xúc mạnh"),
            ///  dấu câu
            new TextRule("???", "", "???", "nghi vấn mạnh"),
            // icon
            new TextRule(":)))", "", ":)))", "cười"));
}
