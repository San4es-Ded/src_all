package haron.hud.snap;

import haron.core.BooleanCoercion;
import haron.hud.core.HudElement;
import haron.hud.snap.SnapAnchor;
import haron.hud.snap.SnapResult;
import haron.hud.snap.GuideOrientation;
import haron.hud.snap.SnapCandidate;
import haron.hud.snap.SnapGuide;
import java.util.ArrayList;
import java.util.List;

public class HudSnapEngine {
    private static final float c = 6.0f;
    private static final float d = 5.0f;
    private static final float e = 10.0f;
    public static int a;
    public static boolean b;

    private static SnapCandidate b(float f, float f2, float f3, float f4, float f5, float f6, List<HudElement> list, HudElement hylpge2, float f7, float f8) {
        float f9;
        SnapCandidate kyetud2 = null;
        if (Math.abs(f - 5.0f) < 6.0f) {
            kyetud2 = HudSnapEngine.a(null, Math.abs(f - 5.0f), 5.0f, new SnapGuide(GuideOrientation.HORIZONTAL, SnapAnchor.SCREEN_EDGE, 5.0f, Math.max(0.0f, f7 - 10.0f), Math.min(f5, f8 + 10.0f)));
        } else if (b) {
            // empty if block
        }
        if (Math.abs(f2 - (f6 - 5.0f)) < 6.0f) {
            kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f2 - (f6 - 5.0f)), f6 - 5.0f - f4, new SnapGuide(GuideOrientation.HORIZONTAL, SnapAnchor.SCREEN_EDGE, f6 - 5.0f, Math.max(0.0f, f7 - 10.0f), Math.min(f5, f8 + 10.0f)));
        }
        if (Math.abs(f3 - (f9 = f6 / 2.0f)) < 6.0f) {
            kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f3 - f9), f9 - f4 / 2.0f, new SnapGuide(GuideOrientation.HORIZONTAL, SnapAnchor.SCREEN_CENTER, f9, Math.max(0.0f, f7 - 10.0f), Math.min(f5, f8 + 10.0f)));
        }
        for (HudElement hylpge3 : list) {
            if (hylpge3 == hylpge2) continue;
            float f10 = hylpge3.m();
            float f11 = hylpge3.m() + hylpge3.o();
            float f12 = hylpge3.m() + hylpge3.o() / 2.0f;
            float f13 = hylpge3.l();
            float f14 = hylpge3.l() + hylpge3.n();
            if (Math.abs(f - f10) < 6.0f) {
                kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f - f10), f10, HudSnapEngine.b(f10, f7, f8, f13, f14, SnapAnchor.ELEMENT_EDGE));
            }
            if (Math.abs(f2 - f11) < 6.0f) {
                kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f2 - f11), f11 - f4, HudSnapEngine.b(f11, f7, f8, f13, f14, SnapAnchor.ELEMENT_EDGE));
            }
            if (Math.abs(f - f11) < 6.0f) {
                kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f - f11), f11, HudSnapEngine.b(f11, f7, f8, f13, f14, SnapAnchor.ELEMENT_EDGE));
            }
            if (Math.abs(f2 - f10) < 6.0f) {
                kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f2 - f10), f10 - f4, HudSnapEngine.b(f10, f7, f8, f13, f14, SnapAnchor.ELEMENT_EDGE));
            }
            if (!(Math.abs(f3 - f12) < 6.0f)) continue;
            kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f3 - f12), f12 - f4 / 2.0f, HudSnapEngine.b(f12, f7, f8, f13, f14, SnapAnchor.ELEMENT_CENTER));
        }
        return kyetud2;
    }

    private static SnapGuide b(float f, float f2, float f3, float f4, float f5, SnapAnchor b95rqx2) {
        float f6 = Math.min(f2, f4) - 10.0f;
        return new SnapGuide(GuideOrientation.HORIZONTAL, b95rqx2, f, Math.max(0.0f, f6), Math.max(f3, f5) + 10.0f);
    }

    private static SnapCandidate a(SnapCandidate kyetud2, float f, float f2, SnapGuide t0jzfe2) {
        return kyetud2 == null || f < kyetud2.a ? new SnapCandidate(f, f2, t0jzfe2) : kyetud2;
    }

    private static SnapGuide a(float f, float f2, float f3, float f4, float f5, SnapAnchor b95rqx2) {
        float f6 = Math.min(f2, f4) - 10.0f;
        return new SnapGuide(GuideOrientation.VERTICAL, b95rqx2, f, Math.max(0.0f, f6), Math.max(f3, f5) + 10.0f);
    }

    private static SnapCandidate a(float f, float f2, float f3, float f4, float f5, float f6, List<HudElement> list, HudElement hylpge2, float f7, float f8) {
        float f9;
        SnapCandidate kyetud2;
        SnapCandidate kyetud3 = kyetud2 = Math.abs(f - 5.0f) < 6.0f ? HudSnapEngine.a(null, Math.abs(f - 5.0f), 5.0f, new SnapGuide(GuideOrientation.VERTICAL, SnapAnchor.SCREEN_EDGE, 5.0f, Math.max(0.0f, f7 - 10.0f), Math.min(f6, f8 + 10.0f))) : null;
        if (Math.abs(f2 - (f5 - 5.0f)) < 6.0f) {
            kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f2 - (f5 - 5.0f)), f5 - 5.0f - f4, new SnapGuide(GuideOrientation.VERTICAL, SnapAnchor.SCREEN_EDGE, f5 - 5.0f, Math.max(0.0f, f7 - 10.0f), Math.min(f6, f8 + 10.0f)));
        }
        if (Math.abs(f3 - (f9 = f5 / 2.0f)) < 6.0f) {
            kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f3 - f9), f9 - f4 / 2.0f, new SnapGuide(GuideOrientation.VERTICAL, SnapAnchor.SCREEN_CENTER, f9, Math.max(0.0f, f7 - 10.0f), Math.min(f6, f8 + 10.0f)));
        }
        for (HudElement hylpge3 : list) {
            if (hylpge3 == hylpge2) continue;
            float f10 = hylpge3.l();
            float f11 = hylpge3.l() + hylpge3.n();
            float f12 = hylpge3.l() + hylpge3.n() / 2.0f;
            float f13 = hylpge3.m();
            float f14 = hylpge3.m() + hylpge3.o();
            if (Math.abs(f - f10) < 6.0f) {
                kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f - f10), f10, HudSnapEngine.a(f10, f7, f8, f13, f14, SnapAnchor.ELEMENT_EDGE));
            }
            if (Math.abs(f2 - f11) < 6.0f) {
                kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f2 - f11), f11 - f4, HudSnapEngine.a(f11, f7, f8, f13, f14, SnapAnchor.ELEMENT_EDGE));
            }
            if (Math.abs(f - f11) < 6.0f) {
                kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f - f11), f11, HudSnapEngine.a(f11, f7, f8, f13, f14, SnapAnchor.ELEMENT_EDGE));
            }
            if (Math.abs(f2 - f10) < 6.0f) {
                kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f2 - f10), f10 - f4, HudSnapEngine.a(f10, f7, f8, f13, f14, SnapAnchor.ELEMENT_EDGE));
            }
            if (!(Math.abs(f3 - f12) < 6.0f)) continue;
            kyetud2 = HudSnapEngine.a(kyetud2, Math.abs(f3 - f12), f12 - f4 / 2.0f, HudSnapEngine.a(f12, f7, f8, f13, f14, SnapAnchor.ELEMENT_CENTER));
        }
        return kyetud2;
    }

    public static SnapResult a(HudElement hylpge2, float f, float f2, float f3, float f4, List<HudElement> list, boolean bl) {
        SnapCandidate kyetud2;
        if (bl) {
            return SnapResult.a(Math.max(5.0f, Math.min(f, f3 - hylpge2.n() - 5.0f)), Math.max(5.0f, Math.min(f2, f4 - hylpge2.o() - 5.0f)));
        }
        float f5 = hylpge2.n();
        float f6 = hylpge2.o();
        ArrayList<SnapGuide> arrayList = new ArrayList<SnapGuide>();
        float f7 = f;
        float f8 = f2;
        int n = 0;
        boolean bl2 = false;
        float f9 = f + f5;
        float f10 = f + f5 / 2.0f;
        float f11 = f2 + f6;
        float f12 = f2 + f6 / 2.0f;
        SnapCandidate kyetud3 = HudSnapEngine.a(f, f9, f10, f5, f3, f4, list, hylpge2, f2, f11);
        if (kyetud3 != null) {
            f7 = kyetud3.b;
            arrayList.add(kyetud3.c);
            n = 1;
        }
        if ((kyetud2 = HudSnapEngine.b(f2, f11, f12, f6, f3, f4, list, hylpge2, f, f9)) != null) {
            f8 = kyetud2.b;
            arrayList.add(kyetud2.c);
        }
        return new SnapResult(Math.max(5.0f, Math.min(f7, f3 - f5 - 5.0f)), Math.max(5.0f, Math.min(f8, f4 - f6 - 5.0f)), arrayList, BooleanCoercion.from(n), BooleanCoercion.from(0));
    }
}

