package ru.prism.utils.lyrics;

public class LyricWord {

    private final long startMillis;
    private final long endMillis;
    private final String text;
    private final int begin;
    private final int end;

    public LyricWord(long startMillis, long endMillis, String text, int begin, int end) {
        this.startMillis = startMillis;
        this.endMillis = endMillis;
        this.text = text;
        this.begin = begin;
        this.end = end;
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

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public boolean contains(long time) {
        return time >= startMillis && time < endMillis;
    }

    public float progress(long time) {
        long span = Math.max(1L, endMillis - startMillis);
        return Math.max(0.0F, Math.min(1.0F, (time - startMillis) / (float) span));
    }
}
