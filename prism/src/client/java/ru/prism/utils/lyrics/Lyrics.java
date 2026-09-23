package ru.prism.utils.lyrics;

import java.util.Collections;
import java.util.List;

public class Lyrics {

    public static final Lyrics EMPTY = new Lyrics(Collections.emptyList(), false);

    private final List<LyricLine> lines;
    private final boolean synced;

    public Lyrics(List<LyricLine> lines, boolean synced) {
        this.lines = lines == null ? Collections.emptyList() : lines;
        this.synced = synced;
    }

    public List<LyricLine> getLines() {
        return lines;
    }

    public boolean isSynced() {
        return synced;
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public int indexAt(long time) {
        int low = 0;
        int high = lines.size() - 1;
        int result = -1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            LyricLine line = lines.get(mid);
            if (line.contains(time)) {
                return mid;
            }
            if (time < line.getStartMillis()) {
                high = mid - 1;
            } else {
                result = mid;
                low = mid + 1;
            }
        }
        return result;
    }

    public LyricLine lineAt(long time) {
        int index = indexAt(time);
        return index < 0 ? null : lines.get(index);
    }

    public LyricLine lineAfter(long time) {
        for (LyricLine line : lines) {
            if (line.getStartMillis() > time) {
                return line;
            }
        }
        return null;
    }

    public LyricWord wordAt(long time) {
        LyricLine line = lineAt(time);
        return line == null ? null : line.wordAt(time);
    }
}
