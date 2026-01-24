package com.minh.business;

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
                t -> Pattern.compile("\\b" + Pattern.quote(t) + "\\b")
        );
        return pattern.matcher(s).find();
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
