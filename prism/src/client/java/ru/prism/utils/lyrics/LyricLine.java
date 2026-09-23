package ru.prism.utils.lyrics;

import java.util.Collections;
import java.util.List;

public class LyricLine {

    private final long startMillis;
    private final long endMillis;
    private final String text;
    private final List<LyricWord> words;

    public LyricLine(long startMillis, long endMillis, String text, List<LyricWord> words) {
        this.startMillis = startMillis;
        this.endMillis = endMillis;
        this.text = text;
        this.words = words == null ? Collections.emptyList() : words;
    }

    public long getStartMillis() {
        return startMillis;
    }

    public long getEndMillis() {
        return endMillis;
    }

    public String getText() {
        return text;
    }

    public List<LyricWord> getWords() {
        return words;
    }

    public long durationMillis() {
        return Math.max(0L, endMillis - startMillis);
    }

    public boolean contains(long time) {
        return time >= startMillis && time < endMillis;
    }

    public float progress(long time) {
        long span = Math.max(1L, durationMillis());
        return Math.max(0.0F, Math.min(1.0F, (time - startMillis) / (float) span));
    }

    public LyricWord wordAt(long time) {
        for (LyricWord word : words) {
            if (word.contains(time)) {
                return word;
            }
        }
        return null;
    }
}
