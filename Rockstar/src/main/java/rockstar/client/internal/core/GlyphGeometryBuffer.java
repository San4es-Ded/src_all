package rockstar.client.internal.core;



import rockstar.client.render.*;
import rockstar.client.*;
import rockstar.client.internal.core.GlyphSpanIndex;
import rockstar.client.render.GlyphMesh;

public final class GlyphGeometryBuffer {
    public static final int internalField0227 = 4096;
    public static final int internalField0228 = 12;
    public static final int internalField1053 = 1024;
    private float[] internalField0615 = new float[262144];
    private int[] internalField0618 = new int[131072];
    private float[] internalField0616 = new float[131072];
    private int internalField1056 = 16;
    private int internalField1054 = 16;
    private int internalField1464 = 16;
    private int internalField1470;
    private int internalField1465;
    private int internalField1463;
    private int internalField1466;
    private int internalField1467;
    private int internalField1469;
    private int internalField1468;
    private int internalField1740;
    private boolean internalField0277;
    private boolean internalField0276;
    private boolean internalField1099;

    public int internalMethod02929() {
        return this.internalField1467;
    }

    public float[] internalMethod00637() {
        return this.internalField0615;
    }

    public int[] internalMethod00638() {
        return this.internalField0618;
    }

    public float[] internalMethod00698() {
        return this.internalField0616;
    }

    public int internalMethod02932() {
        return this.internalField1056;
    }

    public int internalMethod07828() {
        return this.internalField1054;
    }

    public int internalMethod07831() {
        return this.internalField1464;
    }

    public int internalMethod07842() {
        return this.internalField1469;
    }

    public int internalMethod07843() {
        return this.internalField1468;
    }

    public int internalMethod09207() {
        return this.internalField1740;
    }

    public boolean internalMethod02931() {
        return this.internalField0277;
    }

    public boolean internalMethod02933() {
        return this.internalField0276;
    }

    public boolean internalMethod07829() {
        return this.internalField1099;
    }

    public void internalMethod02930() {
        this.internalField1740 = 0;
        this.internalField1468 = 0;
        this.internalField1469 = 0;
        this.internalField1099 = false;
        this.internalField0276 = false;
        this.internalField0277 = false;
    }

    public int internalMethod01811(GlyphSpanIndex typedValue024) {
        int n;
        int n2;
        int n3;
        GlyphMesh typedValue025 = typedValue024.internalField0653;
        int n4 = typedValue025.internalField0227;
        int[] nArray = new int[n4];
        int[] nArray2 = new int[n4];
        for (n3 = 0; n3 < n4; ++n3) {
            if (this.internalField1470 + 2 > 4096) {
                this.internalField1470 = 0;
                ++this.internalField1465;
            }
            this.internalMethod00639(this.internalField1465 + 1);
            nArray[n3] = this.internalField1470;
            nArray2[n3] = this.internalField1465;
            n2 = (this.internalField1465 * 4096 + this.internalField1470) * 4;
            n = n3 * 6;
            this.internalField0615[n2] = typedValue025.internalField0615[n];
            this.internalField0615[n2 + 1] = typedValue025.internalField0615[n + 1];
            this.internalField0615[n2 + 2] = typedValue025.internalField0615[n + 2];
            this.internalField0615[n2 + 3] = typedValue025.internalField0615[n + 3];
            this.internalField0615[n2 + 4] = typedValue025.internalField0615[n + 4];
            this.internalField0615[n2 + 5] = typedValue025.internalField0615[n + 5];
            this.internalField0615[n2 + 6] = 0.0f;
            this.internalField0615[n2 + 7] = 0.0f;
            this.internalField1469 = Math.max(this.internalField1469, this.internalField1465 + 1);
            this.internalField1470 += 2;
        }
        n3 = typedValue024.internalField0228 + typedValue024.internalField0227;
        if (this.internalField1463 + n3 > 4096) {
            this.internalField1463 = 0;
            ++this.internalField1466;
        }
        n2 = typedValue024.internalMethod06181();
        this.internalMethod00699(this.internalField1466 + (this.internalField1463 + n2) / 4096 + 1);
        n = this.internalField1463;
        int n5 = this.internalField1466;
        int n6 = n3;
        int n7 = 0;
        for (int[] nArray3 : typedValue024.internalField0040) {
            n6 = this.internalMethod03515(n, n5, n7++, nArray3, n6, nArray, nArray2);
        }
        for (int[] nArray3 : typedValue024.internalField0041) {
            n6 = this.internalMethod03515(n, n5, n7++, nArray3, n6, nArray, nArray2);
        }
        int n8 = n + n6;
        this.internalField1463 = n8 & 0xFFF;
        this.internalField1466 = n5 + (n8 >> 12);
        this.internalField1468 = Math.max(this.internalField1468, this.internalField1466 + 1);
        int n9 = this.internalField1467++;
        this.internalMethod07830(n9 / 1024 + 1);
        int n10 = n9 * 8;
        this.internalField0616[n10] = typedValue024.internalField0205;
        this.internalField0616[n10 + 1] = typedValue024.internalField0206;
        this.internalField0616[n10 + 2] = typedValue024.internalField1048;
        this.internalField0616[n10 + 3] = typedValue024.internalField1047;
        this.internalField0616[n10 + 4] = n;
        this.internalField0616[n10 + 5] = n5;
        this.internalField0616[n10 + 6] = typedValue024.internalField0227 - 1;
        this.internalField0616[n10 + 7] = typedValue024.internalField0228 - 1;
        this.internalField1740 = Math.max(this.internalField1740, n9 / 1024 + 1);
        return n9;
    }

    private int internalMethod03515(int n, int n2, int n3, int[] nArray, int n4, int[] nArray2, int[] nArray3) {
        int n5 = n + n4 & 0xFFF;
        if (n5 + nArray.length > 4096) {
            n4 += 4096 - n5;
        }
        this.internalMethod06613(n, n2, n3, nArray.length, n4);
        for (int i = 0; i < nArray.length; ++i) {
            this.internalMethod06613(n, n2, n4 + i, nArray2[nArray[i]], nArray3[nArray[i]]);
        }
        return n4 + nArray.length;
    }

    private void internalMethod06613(int n, int n2, int n3, int n4, int n5) {
        int n6 = n + n3;
        int n7 = n2 + (n6 >> 12);
        this.internalMethod00699(n7 + 1);
        int n8 = (n7 * 4096 + (n6 &= 0xFFF)) * 2;
        this.internalField0618[n8] = n4;
        this.internalField0618[n8 + 1] = n5;
        this.internalField1468 = Math.max(this.internalField1468, n7 + 1);
    }

    private void internalMethod00639(int n) {
        int n2;
        if (n <= this.internalField1056) {
            return;
        }
        for (n2 = this.internalField1056; n2 < n; n2 *= 2) {
        }
        float[] fArray = new float[4096 * n2 * 4];
        System.arraycopy(this.internalField0615, 0, fArray, 0, this.internalField0615.length);
        this.internalField0615 = fArray;
        this.internalField1056 = n2;
        this.internalField0277 = true;
        this.internalField1469 = Math.max(this.internalField1469, this.internalField1465 + 1);
    }

    private void internalMethod00699(int n) {
        int n2;
        if (n <= this.internalField1054) {
            return;
        }
        for (n2 = this.internalField1054; n2 < n; n2 *= 2) {
        }
        int[] nArray = new int[4096 * n2 * 2];
        System.arraycopy(this.internalField0618, 0, nArray, 0, this.internalField0618.length);
        this.internalField0618 = nArray;
        this.internalField1054 = n2;
        this.internalField0276 = true;
        this.internalField1468 = Math.max(this.internalField1468, this.internalField1466 + 1);
    }

    private void internalMethod07830(int n) {
        int n2;
        if (n <= this.internalField1464) {
            return;
        }
        for (n2 = this.internalField1464; n2 < n; n2 *= 2) {
        }
        float[] fArray = new float[2048 * n2 * 4];
        System.arraycopy(this.internalField0616, 0, fArray, 0, this.internalField0616.length);
        this.internalField0616 = fArray;
        this.internalField1464 = n2;
        this.internalField1099 = true;
    }
}

