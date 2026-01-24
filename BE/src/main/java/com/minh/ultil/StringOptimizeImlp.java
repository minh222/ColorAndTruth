package com.minh.ultil;

// optimized - đối chiếu v1 để trả nợ kĩ thuật
public class StringOptimizeImlp implements StringUtil {

    // 30% benkmark on v1
    @Override
    public boolean contains(String s, String target) {
        int n = s.length(); // bỏ aloc
        int i = 0;

        while (i < n) {
            while (i < n && Character.isWhitespace(s.charAt(i))) {  // dùng while skip giảm số lần ko thỏa so vs for if thường phải duyệt hết
                i++;
            }
            if (i >= n) break;

            int start = i;
            while (i < n && !Character.isWhitespace(s.charAt(i))) { // dùng while skip white space
                i++;
            }

            int len = i - start;
            if (len == target.length() && s.regionMatches(start, target, 0, len)) {
                return true;
            }
        }
        return false;
    }

    // 30% benkmark on v1
    @Override
    public String replace(String s,String target, String replacement) {
        int n = s.length();
        int i = 0;

        StringBuilder sb = new StringBuilder(n);
        boolean firstWord = true;

        while (i < n) {
            while (i < n && Character.isWhitespace(s.charAt(i))) {
                i++;
            }
            if (i >= n) break;

            if (!firstWord) sb.append(' ');
            firstWord = false;

            int start = i;
            while (i < n && !Character.isWhitespace(s.charAt(i))) {
                i++;
            }

            int len = i - start;
            if (len == target.length() && s.regionMatches(start, target, 0, len)) {
                sb.append(replacement);
            } else {
                sb.append(s, start, i);
            }
        }
        return sb.toString();
    }
}
