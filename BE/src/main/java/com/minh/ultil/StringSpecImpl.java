package com.minh.ultil;

// spec
public class StringSpecImpl implements StringUtil {

    @Override
    public boolean contains(String s, String target) {
        String[] words = s.split("\\s+");
        for (String word : words) {
            if (word.equals(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String replace(String s, String target,String replacement) {
        String[] words = s.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(target)) {
                words[i] = replacement;
            }
        }
        return String.join(" ", words);
    }
}
