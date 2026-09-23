package haron.hud.snap;

import haron.hud.snap.SnapGuide;
import java.util.Collections;
import java.util.List;

public class SnapResult {
    private final float x;
    private final float y;
    private final List<SnapGuide> guides;
    private final boolean snappedX;
    private final boolean snappedY;

    public static SnapResult none(float f, float f2) {
        return new SnapResult(f, f2, Collections.emptyList(), false, false);
    }

    public List<SnapGuide> guides() {
        return this.guides;
    }

    public boolean snappedX() {
        return this.snappedX;
    }

    public boolean snappedY() {
        return this.snappedY;
    }

    public boolean snapped() {
        return this.snappedX || this.snappedY;
    }

    public SnapResult(float f, float f2, List<SnapGuide> list, boolean bl, boolean bl2) {
        this.x = f;
        this.y = f2;
        this.guides = list;
        this.snappedX = bl;
        this.snappedY = bl2;
    }

    public boolean e() {
        return this.snappedY();
    }

    public float b() {
        return this.y();
    }

    public float x() {
        return this.x;
    }

    public List<SnapGuide> c() {
        return this.guides();
    }

    public boolean f() {
        return this.snapped();
    }

    public boolean d() {
        int n = 183;
        return this.snappedX();
    }

    public static SnapResult a(float f, float f2) {
        return SnapResult.none(f, f2);
    }

    public float a() {
        return this.x();
    }

    public float y() {
        return this.y;
    }
}

