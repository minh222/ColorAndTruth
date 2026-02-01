package com.minh.apply.rule;

import java.util.Arrays;
import java.util.List;

public class Rule {
    public static List<TextRule> textRules = Arrays.asList(

            // replace toxic
            new TextRule("con mẹ chúng mày", "", "con mẹ chúng mày", "chửi"),
            new TextRule("thằng", "bạn", "thằng", "ko tôn trọng cá nhân"),
            new TextRule("loại", "bạn", "loại", "hạ thấp giá trị con người"),
            new TextRule("loại này", "bạn", "loại này", "hạ thấp giá trị con người"),
            new TextRule("thể loại", "bạn", "thể loại", "hạ thấp giá trị con người"),
            new TextRule("ngu", "không thông minh", "ngu", "đánh giá tiêu cực năng lực"),
            new TextRule("liều quá", "mạo hiểm", "liều quá", "lo lắng về sự mạo hiểm"),
            new TextRule("rước hoạ vào thân", "xui xẻo", "rước hoạ vào thân", "khẳng định vận xui"),
            new TextRule("con này", "người này", "người này", "ko tôn trọng cá nhân"),
            new TextRule("dốt", "thiếu hiểu biết", "dốt", "đánh giá tiêu cực năng lực"),
            new TextRule("óc chó", "thiếu hiểu biết", "óc chó", "lăng mạ thô tục"),
            new TextRule("não tàn", "thiếu hiểu biết", "não tàn", "lăng mạ thô tục"),
            new TextRule("biết cái gì", "", "biết cái gì", "phủ nhận năng lực"),
            new TextRule("hiểu gì mà nói", "", "hiểu gì mà nói", "phủ nhận năng lực"),
            new TextRule("nói ngu", "", "nói ngu", "đánh giá tiêu cực"),
            new TextRule("suy nghĩ kiểu gì", "", "suy nghĩ kiểu gì", "chỉ trích tư duy"),
            new TextRule("hay quá ha", "", "hay quá ha", "châm biếm"),
            new TextRule("giỏi quá", "", "giỏi quá", "châm biếm"),
            new TextRule("đỉnh cao trí tuệ", "", "đỉnh cao trí tuệ", "châm biếm"),
            new TextRule("xuất sắc ghê", "", "xuất sắc ghê", "châm biếm"),
            new TextRule("vô học", "", "vô học", "đánh giá đạo đức"),
            new TextRule("mất dạy", "", "mất dạy", "đánh giá đạo đức"),
            new TextRule("sống lỗi", "", "sống lỗi", "chỉ trích đạo đức"),
            new TextRule("tao", "tôi", "tao", "none"),
            new TextRule("mày", "bạn", "mày", "none"),
            new TextRule("lũ", "những người", "lũ", "hạ thấp giá trị con người"),
            new TextRule("con", "bạn gái", "con", "hạ thấp giá trị con người"),
            new TextRule("nhảm vãi", "thiếu giá trị", "nhảm vãi", "đánh giá nội dung rất tiêu cực"),
            new TextRule("thần kinh", "", "thần kinh", "không bình thường"),
            new TextRule("đéo", "không", "đéo", "đéo"),
            new TextRule("chó", "", "chó", "khinh người"),
            new TextRule("cc", "", "cc", "chửi"),


            // emotion
            new TextRule("buồn quá", "", "buồn quá", "biểu lộ cảm xúc buồn"),
            new TextRule("chán thật", "", "chán thật", "biểu lộ cảm xúc chán nản"),
            new TextRule("chán ghê", "", "chán ghê", "biểu lộ cảm xúc chán nản"),
            new TextRule("mệt mỏi", "", "mệt mỏi", "biểu lộ cảm xúc mệt mỏi"),
            new TextRule("chán đời", "", "chán đời", "bi quan"),
            new TextRule("tức quá", "", "tức quá", "biểu lộ tức giận"),
            new TextRule("bực mình", "", "bực mình", "khó chịu"),
            new TextRule("cay thật", "", "cay thật", "tức giận ngầm"),
            new TextRule("điên thật", "", "điên thật", "mất kiểm soát cảm xúc"),
            new TextRule("thất vọng", "", "thất vọng", "kỳ vọng không đạt"),
            new TextRule("vãi thật", "", "vãi thật", "phản ứng tiêu cực"),
            new TextRule("chịu luôn", "", "chịu luôn", "bỏ cuộc tâm lý"),
            new TextRule("bó tay", "", "bó tay", "không còn giải pháp"),
            new TextRule("lo quá", "", "lo quá", "bất an"),
            new TextRule("sợ thật", "", "sợ thật", "lo sợ"),
            new TextRule("toang rồi", "", "toang rồi", "lo lắng cực độ"),
            new TextRule("vui quá", "", "vui quá", "cảm xúc tích cực"),
            new TextRule("mừng ghê", "", "mừng ghê", "hài lòng"),
            new TextRule("ok rồi", "", "ok rồi", "giảm căng thẳng"),
            new TextRule("haha", "", "haha", "cười"),
            new TextRule("ha ha", "", "ha ha", "cười"),
            new TextRule("thôi kệ", "", "thôi kệ", "chấp nhận tiêu cực"),
            new TextRule("tuỳ", "", "tuỳ", "không quan tâm"),
            new TextRule("quan tâm làm gì", "", "quan tâm làm gì", "mất động lực"),
            new TextRule("ta nói nó chán", "", "ta nói nó chán", "mất động lực"),
            new TextRule("tuyệt vọng", "", "tuyệt vọng", "mất niềm tin"),
            new TextRule("stress", "", "stress", "áp lực"),
            new TextRule("buồn", "", "buồn", "hong vui"),
            new TextRule("mệt mỏi", "", "mệt mỏi", "sức khỏe ko ổn"),
            new TextRule("chán", "", "chán", "mất hứng thú"),
            new TextRule("hihi", "", "hihi", "cười"),


            // khen
            new TextRule("cuti quá", "đáng yêu quá", "cuti quá", "cuti quá"),
            new TextRule("nhaaaaaaaa", "", "nhaaaaaaaa", "nhaaaaaaaa (cute voice) "),
            new TextRule("dthw", "dễ thương", "dthw", "dthw (khen) "),
            new TextRule("yêu", "yêu", "yêu", "yêu (khen) "),
            new TextRule("iu", "iu", "iu", "iu (khen) "),

            //oke
            new TextRule("oke luôn", "được", "oke luôn", "rất được"),
            new TextRule("ok luôn", "được", "ok luôn", "rất được"),
            new TextRule("oke", "được", "oke", "được"),
            new TextRule("ok", "được", "ok", "được"),


            // remove
            new TextRule("con ơi", "", "con ơi", "cảm xúc mạnh"),
            new TextRule("đó con", "", "đó con", "cảm xúc mạnh"),
            new TextRule("con ơi", "", "con ơi", "cảm xúc mạnh"),
            new TextRule("trời ơi", "", "trời ơi", "cảm xúc mạnh"),
            new TextRule("ôi trời", "", "ôi trời", "cảm xúc mạnh"),
            new TextRule("vãi", "", "vãi", "cảm xúc mạnh"),
            new TextRule("vl", "", "vl", "cảm xúc mạnh"),
            new TextRule("ta", "", "ta", "nghi vấn"),
            new TextRule("ghê", "", "ghê", "nhấn mạnh cảm xúc"),

            ///  dấu câu
            new TextRule("???", "", "???", "nghi vấn mạnh"),
            new TextRule("??",  "", "??", "nghi vấn"),
            new TextRule("?",   "", "?", "nghi vấn"),
            new TextRule("!!!", "", "!!!", "cảm thán mạnh"),
            new TextRule("!!",  "", "!!", "cảm thán"),
            new TextRule("!",   "", "!", "cảm thán"),
            new TextRule("?!",  "", "?!", "châm biếm"),
            new TextRule("!?",  "", "!?", "châm biếm"),
            new TextRule("...", "", "...", "chán nản"),

            // icon
            new TextRule(":)))", "", ":)))", "cười")
            );


}
