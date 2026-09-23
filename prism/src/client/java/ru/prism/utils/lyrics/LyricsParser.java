package ru.prism.utils.lyrics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LyricsParser {

    private static final Pattern LINE_TIME = Pattern.compile("\\[(\\d+):(\\d{1,2})(?:[.:](\\d{1,3}))?]");
    private static final Pattern WORD_TIME = Pattern.compile("<(\\d+):(\\d{1,2})(?:[.:](\\d{1,3}))?>");
    private static final Pattern OFFSET = Pattern.compile("\\[offset:\\s*([+-]?\\d+)]");
    private static final Pattern TAG = Pattern.compile("\\[[a-zA-Z#]+:.*]");

    private static final long TAIL_MILLIS = 5000L;
    private static final long MILLIS_PER_SYLLABLE = 300L;
    private static final long MIN_SUNG_MILLIS = 1500L;
    private static final long FULL_SPAN_MILLIS = 7000L;
    private static final String VOWELS = "аеёиоуыэюяіїєaeiouy";

    private LyricsParser() {
    }

    public static Lyrics parse(String content) {
        if (content == null || content.isBlank()) {
            return Lyrics.EMPTY;
        }
        long offset = readOffset(content);
        List<LyricLine> raw = new ArrayList<>();
        boolean synced = false;
        StringBuilder plain = new StringBuilder();

        for (String line : content.split("\\R")) {
            Matcher matcher = LINE_TIME.matcher(line);
            List<Long> times = new ArrayList<>();
            int bodyStart = 0;
            while (matcher.find()) {
                times.add(timestamp(matcher, 1));
                bodyStart = matcher.end();
            }
            String body = line.substring(Math.min(bodyStart, line.length())).trim();
            if (times.isEmpty()) {
                String stripped = TAG.matcher(line).replaceAll("").trim();
                if (!stripped.isEmpty()) {
                    if (plain.length() > 0) {
                        plain.append(' ');
                    }
                    plain.append(stripped);
                }
                continue;
            }
            synced = true;
            for (long time : times) {
                raw.add(new LyricLine(time + offset, time + offset, body, null));
            }
        }

        if (!synced) {
            String text = plain.toString().trim();
            if (text.isEmpty()) {
                return Lyrics.EMPTY;
            }
            return new Lyrics(List.of(new LyricLine(0L, 0L, text, List.of())), false);
        }

        raw.sort(Comparator.comparingLong(LyricLine::getStartMillis));
        List<LyricLine> lines = new ArrayList<>(raw.size());
        for (int i = 0; i < raw.size(); i++) {
            LyricLine line = raw.get(i);
            long end = i + 1 < raw.size() ? raw.get(i + 1).getStartMillis() : line.getStartMillis() + TAIL_MILLIS;
            if (end <= line.getStartMillis()) {
                end = line.getStartMillis() + TAIL_MILLIS;
            }
            lines.add(build(line.getStartMillis(), end, line.getText()));
        }
        return new Lyrics(lines, true);
    }

    private static LyricLine build(long start, long end, String body) {
        if (WORD_TIME.matcher(body).find()) {
            return enhanced(body, start, end);
        }
        return new LyricLine(start, end, body, distribute(body, start, end));
    }

    private static LyricLine enhanced(String body, long lineStart, long lineEnd) {
        Matcher matcher = WORD_TIME.matcher(body);
        List<Long> starts = new ArrayList<>();
        List<String> chunks = new ArrayList<>();
        int cursor = 0;
        while (matcher.find()) {
            if (cursor < matcher.start()) {
                chunks.add(body.substring(cursor, matcher.start()));
                starts.add(-1L);
            }
            starts.add(timestamp(matcher, 1));
            cursor = matcher.end();
        }
        if (cursor < body.length()) {
            chunks.add(body.substring(cursor));
            starts.add(-1L);
        }

        StringBuilder text = new StringBuilder();
        List<LyricWord> words = new ArrayList<>();
        for (int i = 0; i < chunks.size(); i++) {
            String chunk = chunks.get(i);
            int begin = text.length();
            text.append(chunk);
            long wordStart = starts.get(i) >= 0 ? starts.get(i) : lineStart;
            long wordEnd = lineEnd;
            for (int j = i + 1; j < starts.size(); j++) {
                if (starts.get(j) >= 0) {
                    wordEnd = starts.get(j);
                    break;
                }
            }
            if (!chunk.isBlank()) {
                words.add(new LyricWord(wordStart, Math.max(wordStart + 1L, wordEnd), chunk, begin, text.length()));
            }
        }
        return new LyricLine(lineStart, lineEnd, text.toString(), words);
    }

    private static List<LyricWord> distribute(String body, long start, long end) {
        List<LyricWord> words = new ArrayList<>();
        int total = Math.max(1, syllables(body, 0, body.length()));
        long span = Math.max(MIN_SUNG_MILLIS, Math.min(FULL_SPAN_MILLIS, Math.max(1L, end - start)));
        long cursor = start;
        int index = 0;
        while (index < body.length()) {
            while (index < body.length() && Character.isWhitespace(body.charAt(index))) {
                index++;
            }
            if (index >= body.length()) {
                break;
            }
            int begin = index;
            while (index < body.length() && !Character.isWhitespace(body.charAt(index))) {
                index++;
            }
            String word = body.substring(begin, index);
            int syl = Math.max(1, syllables(word, 0, word.length()));
            long duration = Math.max(MILLIS_PER_SYLLABLE * syl, span * syl / total);
            long wordEnd = Math.min(end, cursor + duration);
            if (wordEnd <= cursor) {
                wordEnd = cursor + 1L;
            }
            words.add(new LyricWord(cursor, wordEnd, word, begin, index));
            cursor = wordEnd;
        }
        return words;
    }

    private static int syllables(String text, int begin, int end) {
        int count = 0;
        for (int i = Math.max(0, begin); i < Math.min(end, text.length()); i++) {
            char lower = Character.toLowerCase(text.charAt(i));
            if (VOWELS.indexOf(lower) >= 0) {
                count++;
            }
        }
        return count;
    }

    private static long readOffset(String content) {
        Matcher matcher = OFFSET.matcher(content);
        if (matcher.find()) {
            try {
                return Long.parseLong(matcher.group(1));
            } catch (NumberFormatException ignored) {
            }
        }
        return 0L;
    }

    private static long timestamp(Matcher matcher, int group) {
        long minutes = Long.parseLong(matcher.group(group));
        long seconds = Long.parseLong(matcher.group(group + 1));
        String fraction = matcher.group(group + 2);
        long millis = 0L;
        if (fraction != null) {
            millis = switch (fraction.length()) {
                case 1 -> Long.parseLong(fraction) * 100L;
                case 2 -> Long.parseLong(fraction) * 10L;
                default -> Long.parseLong(fraction.substring(0, 3));
            };
        }
        return minutes * 60_000L + seconds * 1_000L + millis;
    }
}
