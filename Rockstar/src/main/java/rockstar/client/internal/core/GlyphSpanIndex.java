package rockstar.client.internal.core;



import rockstar.client.render.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import rockstar.client.render.GlyphMesh;

public final class GlyphSpanIndex {
    public final GlyphMesh internalField0653;
    public final int internalField0227;
    public final int internalField0228;
    public final int[][] internalField0040;
    public final int[][] internalField0041;
    public final float internalField0205;
    public final float internalField0206;
    public final float internalField1048;
    public final float internalField1047;

    private GlyphSpanIndex(GlyphMesh typedValue025, int n, int n2, int[][] nArray, int[][] nArray2, float f, float f2, float f3, float f4) {
        this.internalField0653 = typedValue025;
        this.internalField0227 = n;
        this.internalField0228 = n2;
        this.internalField0040 = nArray;
        this.internalField0041 = nArray2;
        this.internalField0205 = f;
        this.internalField0206 = f2;
        this.internalField1048 = f3;
        this.internalField1047 = f4;
    }

    public int internalMethod06181() {
        int n = this.internalField0228 + this.internalField0227;
        for (int[] nArray : this.internalField0040) {
            n += nArray.length;
        }
        for (int[] nArray : this.internalField0041) {
            n += nArray.length;
        }
        return n;
    }

    public static GlyphSpanIndex internalMethod00809(GlyphMesh typedValue025) {
        int n = typedValue025.internalField0227;
        float f = Math.max(typedValue025.internalMethod01751(), 1.0E-6f);
        float f2 = Math.max(typedValue025.internalMethod01754(), 1.0E-6f);
        float f3 = Math.max(f, f2);
        int n2 = Math.max(1, Math.min(32, (n + 4 - 1) / 4));
        int n3 = Math.max(1, Math.min(32, Math.round((float)n2 * f / f3)));
        int n4 = Math.max(1, Math.min(32, Math.round((float)n2 * f2 / f3)));
        ArrayList<List<Integer>> arrayList = new ArrayList<List<Integer>>(n4);
        for (int i = 0; i < n4; ++i) {
            arrayList.add(new ArrayList());
        }
        ArrayList<List<Integer>> arrayList2 = new ArrayList<List<Integer>>(n3);
        for (int i = 0; i < n3; ++i) {
            arrayList2.add(new ArrayList());
        }
        float f4 = (float)n3 / f;
        float f5 = (float)n4 / f2;
        for (int i = 0; i < n; ++i) {
            int n5;
            int n6;
            int n7;
            int n8 = i * 6;
            float f6 = typedValue025.internalField0615[n8];
            float f7 = typedValue025.internalField0615[n8 + 1];
            float f8 = typedValue025.internalField0615[n8 + 2];
            float f9 = typedValue025.internalField0615[n8 + 3];
            float f10 = typedValue025.internalField0615[n8 + 4];
            float f11 = typedValue025.internalField0615[n8 + 5];
            float f12 = Math.min(f6, Math.min(f8, f10));
            float f13 = Math.max(f6, Math.max(f8, f10));
            float f14 = Math.min(f7, Math.min(f9, f11));
            float f15 = Math.max(f7, Math.max(f9, f11));
            if (f7 != f9 || f9 != f11) {
                n7 = GlyphSpanIndex.internalMethod00569((f14 - typedValue025.internalField1048) * f5, n4);
                n6 = GlyphSpanIndex.internalMethod00569((f15 - typedValue025.internalField1048) * f5, n4);
                for (n5 = n7; n5 <= n6; ++n5) {
                    ((List)arrayList.get(n5)).add(i);
                }
            }
            if (f6 == f8 && f8 == f10) continue;
            n7 = GlyphSpanIndex.internalMethod00569((f12 - typedValue025.internalField0206) * f4, n3);
            n6 = GlyphSpanIndex.internalMethod00569((f13 - typedValue025.internalField0206) * f4, n3);
            for (n5 = n7; n5 <= n6; ++n5) {
                ((List)arrayList2.get(n5)).add(i);
            }
        }
        return new GlyphSpanIndex(typedValue025, n3, n4, GlyphSpanIndex.internalMethod02330(arrayList, typedValue025, true), GlyphSpanIndex.internalMethod02330(arrayList2, typedValue025, false), f4, f5, -typedValue025.internalField0206 * f4, -typedValue025.internalField1048 * f5);
    }

    private static int internalMethod00569(float f, int n) {
        return Math.max(0, Math.min(n - 1, (int)f));
    }

    private static int[][] internalMethod02330(List<List<Integer>> list, GlyphMesh typedValue025, boolean bl) {
        int[][] nArrayArray = new int[list.size()][];
        for (int i = 0; i < list.size(); ++i) {
            List<Integer> list2 = list.get(i);
            list2.sort((n, n2) -> Float.compare(GlyphSpanIndex.internalMethod03803(typedValue025, n2, bl), GlyphSpanIndex.internalMethod03803(typedValue025, n, bl)));
            int[] nArray = new int[list2.size()];
            for (int j = 0; j < nArray.length; ++j) {
                nArray[j] = list2.get(j);
            }
            nArrayArray[i] = nArray;
        }
        return nArrayArray;
    }

    private static float internalMethod03803(GlyphMesh typedValue025, int n, boolean bl) {
        int n2 = n * 6 + (bl ? 0 : 1);
        return Math.max(typedValue025.internalField0615[n2], Math.max(typedValue025.internalField0615[n2 + 2], typedValue025.internalField0615[n2 + 4]));
    }
}

