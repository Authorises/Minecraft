package dev.authorises.lightweightlobby.util;

import java.util.*;

public class TextUtil {

    public static List<String> splitText(String text, String prefix, int maxLineLength) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder(prefix);
        int remainingLineLength = maxLineLength - prefix.length();
        for (String word : words) {
            if (word.length() > maxLineLength) {
                // If a word is longer than the maximum line length, split it at the maximum line length.
                int startIndex = 0;
                while (startIndex < word.length()) {
                    int endIndex = Math.min(startIndex + remainingLineLength, word.length());
                    String subword = word.substring(startIndex, endIndex);
                    lines.add(line.toString() + subword);
                    line = new StringBuilder(prefix);
                    remainingLineLength = maxLineLength - prefix.length();
                    startIndex += remainingLineLength;
                }
            } else if (line.length() + word.length() + 1 <= maxLineLength || line.length() < prefix.length() + 7) {
                // If the word can be added to the current line without exceeding the maximum line length or
                // the current line has less than 10 characters left, add the word to the current line.
                line.append(word).append(" ");
                remainingLineLength -= word.length() + 1;
            } else {
                // If the word cannot be added to the current line without exceeding the maximum line length
                // and the current line has at least 10 characters left, start a new line.
                lines.add(line.toString().trim());
                line = new StringBuilder(prefix + word + " ");
                remainingLineLength = maxLineLength - prefix.length() - word.length() - 1;
            }
        }
        lines.add(line.toString().trim());
        return lines;
    }

    public static String formatTime(long epochTime) {
        long seconds = epochTime / 1000;
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;

        if (minutes > 0) {
            return String.format("%dm%02ds", minutes, remainingSeconds);
        } else {
            return String.format("%.2fs", epochTime / 1000.0);
        }
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String formatAbbreviation(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatAbbreviation(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatAbbreviation(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

}
