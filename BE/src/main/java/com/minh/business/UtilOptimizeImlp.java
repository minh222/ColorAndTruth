package com.minh.business;


import com.minh.business.abtract.Util;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.minh.config.Config.CACHE;


// optimized - đối chiếu spec để trả nợ kĩ thuật, dev song song vs spec ưu tiên fast
@Service
public class UtilOptimizeImlp implements Util {
    @Override
    public boolean contains(String s,  String target) { // chốt spec sẽ update
        Pattern pattern = CACHE.get(
                target,
                t -> Pattern.compile("(?<!\\p{L})" + Pattern.quote(t) + "(?!\\p{L})")
        );
        return pattern.matcher(s).find();
    }

    @Override
    public String replace( String s,  String target,  String replacement) {
        return s.replaceAll(
                "(?<!\\p{L})" + Pattern.quote(target) + "(?!\\p{L})",
                Matcher.quoteReplacement(replacement)
        );
    }

    @Override
    public String normalize( String s) { // fast + readablity ko cần optimize
        return s.replaceAll("\\s+", " ").trim().toLowerCase();
    }
}
