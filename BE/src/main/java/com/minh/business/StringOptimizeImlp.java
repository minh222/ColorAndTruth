package com.minh.business;

import com.minh.config.RegexCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// optimized - đối chiếu spec để trả nợ kĩ thuật
@Service
public class StringOptimizeImlp implements StringUtil {
    @Autowired RegexCache regexCache;

    @Override
    public boolean contains(String s, String target) { // chốt spec sẽ update
        return regexCache.get(target).matcher(s).find();
    }

    @Override
    public String replace(String s, String target, String replacement) {
        return s.replaceAll(
                "\\b" + Pattern.quote(target) + "\\b",
                Matcher.quoteReplacement(replacement)
        );
    }

    @Override
    public String normalize(String s) { // fast + readablity ko cần optimize
        return s.replaceAll("\\s+", " ").trim().toLowerCase();
    }
}
