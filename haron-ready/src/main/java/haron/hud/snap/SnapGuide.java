package haron.hud.snap;

import haron.hud.snap.SnapAnchor;
import haron.hud.snap.GuideOrientation;

public class SnapGuide {
    private final GuideOrientation orientation;
    private final SnapAnchor anchor;
    private final float position;
    private final float start;
    private final float end;

    public GuideOrientation orientation() {
        return this.orientation;
    }

    public SnapGuide(GuideOrientation cw6y412, SnapAnchor b95rqx2, float f, float f2, float f3) {
        this.orientation = cw6y412;
        this.anchor = b95rqx2;
        this.position = f;
        this.start = f2;
        this.end = f3;
    }

    public float end() {
        return this.end;
    }

    public float e() {
        return this.end();
    }

    public float position() {
        return this.position;
    }

    public SnapAnchor b() {
        return this.anchor();
    }

    public float c() {
        return this.position();
    }

    public float d() {
        int n = 82;
        return this.start();
    }

    public GuideOrientation a() {
        return this.orientation();
    }

    public float start() {
        return this.start;
    }

    public SnapAnchor anchor() {
        return this.anchor;
    }
}

