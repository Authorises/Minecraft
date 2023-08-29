package dev.authorises.cavelet.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoreChunk {
    private static final Pattern pattern = Pattern.compile("(>[A-Za-z0-9!#]+<)");

    private static String getNextPrefix(String line, String defaultPrefix){
        Matcher m = pattern.matcher(new StringBuilder(line).reverse());
        if(m.find()) {
            return "<!italic>"+new StringBuilder(m.group(1)).reverse();
        }else{
            return defaultPrefix;
        }
    }

    public static List<String> chunk(String s, int limit, String newLineColor) {
        List<String> parts = new ArrayList<String>();
        String nextPrefix = newLineColor;
        while(s.length() > limit) {
            int splitAt = limit-1;
            for(;splitAt>0 && !Character.isWhitespace(s.charAt(splitAt)); splitAt--);
            if(splitAt == 0)
                return parts; // can't be split
            String line = s.substring(0, splitAt);
            parts.add(ColorUtils.format(nextPrefix+line));
            nextPrefix = getNextPrefix(line, newLineColor);
            s = s.substring(splitAt+1);
        }
        parts.add(ColorUtils.format(newLineColor+s));
        return parts;
    }
}
