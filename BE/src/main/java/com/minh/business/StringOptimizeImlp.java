package com.minh.business;

import com.minh.abtract.String;
import com.minh.config.Config;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// optimized - đối chiếu spec để trả nợ kĩ thuật, dev song song vs spec ưu tiên fast
@Service
public class StringOptimizeImlp implements String {

    @Override
    public boolean contains(java.lang.String s, java.lang.String target) { // chốt spec sẽ update
        Pattern pattern = Config.CACHE.get(
                target,
                t -> Pattern.compile("(?<!\\p{L})" + Pattern.quote(t) + "(?!\\p{L})")
        );
        return pattern.matcher(s).find();
    }

    @Override
    public java.lang.String replace(java.lang.String s, java.lang.String target, java.lang.String replacement) {
        return s.replaceAll(
                "(?<!\\p{L})" + Pattern.quote(target) + "(?!\\p{L})",
                Matcher.quoteReplacement(replacement)
        );
    }

    @Override
    public java.lang.String normalize(java.lang.String s) { // fast + readablity ko cần optimize
        return s.replaceAll("\\s+", " ").trim().toLowerCase();
    }
}
