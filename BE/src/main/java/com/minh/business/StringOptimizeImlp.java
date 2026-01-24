package com.minh.business;

import com.minh.abtract.StringUtil;
import com.minh.config.Config;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// optimized - đối chiếu spec để trả nợ kĩ thuật
@Service
public class StringOptimizeImlp implements StringUtil {

    @Override
    public boolean contains(String s, String target) { // chốt spec sẽ update
        Pattern pattern = Config.CACHE.get(
                target,
                t -> Pattern.compile("(?<!\\p{L})" + Pattern.quote(t) + "(?!\\p{L})")
        );
        return pattern.matcher(s).find();
    }

    @Override
    public String replace(String s, String target, String replacement) {
        return s.replaceAll(
                "(?<!\\p{L})" + Pattern.quote(target) + "(?!\\p{L})",
                Matcher.quoteReplacement(replacement)
        );
    }

    @Override
    public String normalize(String s) { // fast + readablity ko cần optimize
        return s.replaceAll("\\s+", " ").trim().toLowerCase();
    }
}
