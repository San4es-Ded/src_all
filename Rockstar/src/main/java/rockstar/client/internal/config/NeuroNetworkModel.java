package rockstar.client.internal.config;


import rockstar.client.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public final class NeuroNetworkModel {
    public final int internalField0227;
    public final int internalField0228;
    public final int internalField1053;
    public final int internalField1055;
    public final int internalField1056;
    public final int internalField1054;
    public final int internalField1464;
    private final float[] internalField0615;
    private final float[] internalField0616;
    private final float[] internalField1238;
    private final float[] internalField1240;
    private final float[] internalField1239;
    private final float[] internalField1237;
    private final float[] internalField1574;
    private final float[] internalField1573;
    private final float[] internalField1577;
    private final float[] internalField1575;
    private final float[] internalField1576;
    private final float[] internalField1578;
    private final float[] internalField1579;
    private final float[] internalField1580;
    private final float[] internalField1805;
    private final float[] internalField1806;
    private final float[] internalField1804;
    private final float[] internalField1808;
    private final float[] internalField1807;

    private NeuroNetworkModel(int n, int n2, int n3, int n4, int n5, int n6, int n7, float[] fArray, float[] fArray2, float[] fArray3, float[] fArray4, float[] fArray5, float[] fArray6, float[] fArray7, float[] fArray8, float[] fArray9, float[] fArray10, float[] fArray11, float[] fArray12, float[] fArray13, float[] fArray14, float[] fArray15, float[] fArray16, float[] fArray17, float[] fArray18, float[] fArray19) {
        this.internalField0227 = n;
        this.internalField0228 = n2;
        this.internalField1053 = n3;
        this.internalField1055 = n4;
        this.internalField1056 = n5;
        this.internalField1054 = n6;
        this.internalField1464 = n7;
        this.internalField0615 = fArray;
        this.internalField0616 = fArray2;
        this.internalField1238 = fArray3;
        this.internalField1240 = fArray4;
        this.internalField1239 = fArray5;
        this.internalField1237 = fArray6;
        this.internalField1574 = fArray7;
        this.internalField1573 = fArray8;
        this.internalField1577 = fArray9;
        this.internalField1575 = fArray10;
        this.internalField1576 = fArray11;
        this.internalField1578 = fArray12;
        this.internalField1579 = fArray13;
        this.internalField1580 = fArray14;
        this.internalField1805 = fArray15;
        this.internalField1806 = fArray16;
        this.internalField1804 = fArray17;
        this.internalField1808 = fArray18;
        this.internalField1807 = fArray19;
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public static NeuroNetworkModel internalMethod06376(Path path) {
        if (path == null || !Files.isRegularFile(path, new LinkOption[0])) {
            return null;
        }
        try (InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);){
            int n;
            int n2;
            int n3;
            int n4;
            int n5;
            int n6;
            int n7;
            DataInputStream dataInputStream;
            block19: {
                block18: {
                    NeuroNetworkModel typedValue217;
                    dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
                    try {
                        if (dataInputStream.readInt() == 1347375950) break block18;
                        typedValue217 = null;
                    }
                    catch (Throwable throwable) {
                        try {
                            dataInputStream.close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                        throw throwable;
                    }
                    dataInputStream.close();
                    return typedValue217;
                }
                dataInputStream.readInt();
                n7 = dataInputStream.readInt();
                n6 = dataInputStream.readInt();
                n5 = dataInputStream.readInt();
                n4 = dataInputStream.readInt();
                n3 = dataInputStream.readInt();
                n2 = dataInputStream.readInt();
                n = dataInputStream.readInt();
                if (n7 > 0 && n6 > 0 && n5 > 0 && n4 > 0 && n3 > 0) break block19;
                NeuroNetworkModel typedValue218 = null;
                dataInputStream.close();
                return typedValue218;
            }
            float[] fArray = NeuroNetworkModel.internalMethod03424(dataInputStream, n7);
            float[] fArray2 = NeuroNetworkModel.internalMethod03424(dataInputStream, n7);
            float[] fArray3 = NeuroNetworkModel.internalMethod03424(dataInputStream, 3);
            float[] fArray4 = NeuroNetworkModel.internalMethod03424(dataInputStream, 3);
            float[] fArray5 = NeuroNetworkModel.internalMethod03424(dataInputStream, 3 * n6 * n7);
            float[] fArray6 = NeuroNetworkModel.internalMethod03424(dataInputStream, 3 * n6 * n6);
            float[] fArray7 = NeuroNetworkModel.internalMethod03424(dataInputStream, 3 * n6);
            float[] fArray8 = NeuroNetworkModel.internalMethod03424(dataInputStream, 3 * n6);
            float[] fArray9 = NeuroNetworkModel.internalMethod03424(dataInputStream, n5 * n6);
            float[] fArray10 = NeuroNetworkModel.internalMethod03424(dataInputStream, n5);
            float[] fArray11 = NeuroNetworkModel.internalMethod03424(dataInputStream, n3 * n4);
            int n8 = 3 + n4;
            float[] fArray12 = NeuroNetworkModel.internalMethod03424(dataInputStream, 3 * n5 * n8);
            float[] fArray13 = NeuroNetworkModel.internalMethod03424(dataInputStream, 3 * n5 * n5);
            float[] fArray14 = NeuroNetworkModel.internalMethod03424(dataInputStream, 3 * n5);
            float[] fArray15 = NeuroNetworkModel.internalMethod03424(dataInputStream, 3 * n5);
            float[] fArray16 = NeuroNetworkModel.internalMethod03424(dataInputStream, 3 * n5);
            float[] fArray17 = NeuroNetworkModel.internalMethod03424(dataInputStream, 3);
            float[] fArray18 = NeuroNetworkModel.internalMethod03424(dataInputStream, n3 * n6);
            float[] fArray19 = NeuroNetworkModel.internalMethod03424(dataInputStream, n3);
            NeuroNetworkModel typedValue219 = new NeuroNetworkModel(n7, n6, n5, n4, n3, n2, n, fArray, fArray2, fArray3, fArray4, fArray5, fArray6, fArray7, fArray8, fArray9, fArray10, fArray11, fArray12, fArray13, fArray14, fArray15, fArray16, fArray17, fArray18, fArray19);
            dataInputStream.close();
            return typedValue219;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    private static float[] internalMethod03424(DataInputStream dataInputStream, int n) throws IOException {
        float[] fArray = new float[n];
        for (int i = 0; i < n; ++i) {
            fArray[i] = dataInputStream.readFloat();
        }
        return fArray;
    }

    public InternalType0137 internalMethod04964(float[][] fArray, int n) {
        int n2;
        if (n <= 0) {
            n = Math.max(1, this.internalField1464);
        }
        float[] fArray2 = this.internalMethod06974(fArray);
        double[] dArray = NeuroNetworkModel.internalMethod07636(NeuroNetworkModel.internalMethod04369(this.internalField1808, this.internalField1807, fArray2, this.internalField1056, this.internalField0228));
        float[] fArray3 = NeuroNetworkModel.internalMethod04369(this.internalField1577, this.internalField1575, fArray2, this.internalField1053, this.internalField0228);
        for (n2 = 0; n2 < this.internalField1053; ++n2) {
            fArray3[n2] = NeuroNetworkModel.internalMethod00970(fArray3[n2]);
        }
        n2 = this.internalField1053;
        int n3 = this.internalField1055;
        int n4 = 3 + n3;
        float[][][] fArray4 = new float[this.internalField1056][n][3];
        float[] fArray5 = new float[n4];
        for (int i = 0; i < this.internalField1056; ++i) {
            int n5;
            float[] fArray6 = (float[])fArray3.clone();
            fArray5[0] = 0.0f;
            fArray5[1] = 0.0f;
            fArray5[2] = 0.0f;
            for (n5 = 0; n5 < n3; ++n5) {
                fArray5[3 + n5] = this.internalField1576[i * n3 + n5];
            }
            for (n5 = 0; n5 < n; ++n5) {
                fArray6 = NeuroNetworkModel.internalMethod03480(fArray6, fArray5, this.internalField1578, this.internalField1579, this.internalField1580, this.internalField1805, n2, n4);
                float[] fArray7 = NeuroNetworkModel.internalMethod04369(this.internalField1806, this.internalField1804, fArray6, 3, n2);
                for (int j = 0; j < 3; ++j) {
                    fArray4[i][n5][j] = fArray7[j] * this.internalField1240[j] + this.internalField1238[j];
                    fArray5[j] = fArray7[j];
                }
            }
        }
        return new InternalType0137(fArray4, dArray);
    }

    private float[] internalMethod06974(float[][] fArray) {
        int n = this.internalField0228;
        int n2 = this.internalField0227;
        float[] fArray2 = new float[n];
        float[] fArray3 = new float[n2];
        for (float[] fArray4 : fArray) {
            for (int i = 0; i < n2; ++i) {
                float f = this.internalField0616[i];
                fArray3[i] = (fArray4[i] - this.internalField0615[i]) / (f == 0.0f ? 1.0f : f);
            }
            fArray2 = NeuroNetworkModel.internalMethod03480(fArray2, fArray3, this.internalField1239, this.internalField1237, this.internalField1574, this.internalField1573, n, n2);
        }
        return fArray2;
    }

    private static float[] internalMethod03480(float[] fArray, float[] fArray2, float[] fArray3, float[] fArray4, float[] fArray5, float[] fArray6, int n, int n2) {
        float f;
        float[] fArray7 = new float[3 * n];
        float[] fArray8 = new float[3 * n];
        for (int i = 0; i < 3 * n; ++i) {
            float f2 = fArray5[i];
            int n3 = i * n2;
            for (int j = 0; j < n2; ++j) {
                f2 += fArray3[n3 + j] * fArray2[j];
            }
            fArray7[i] = f2;
            f = fArray6[i];
            int n4 = i * n;
            for (int j = 0; j < n; ++j) {
                f += fArray4[n4 + j] * fArray[j];
            }
            fArray8[i] = f;
        }
        float[] fArray9 = new float[n];
        for (int i = 0; i < n; ++i) {
            float f3 = NeuroNetworkModel.internalMethod00912(fArray7[i] + fArray8[i]);
            f = NeuroNetworkModel.internalMethod00912(fArray7[n + i] + fArray8[n + i]);
            float f4 = NeuroNetworkModel.internalMethod00970(fArray7[2 * n + i] + f3 * fArray8[2 * n + i]);
            fArray9[i] = (1.0f - f) * f4 + f * fArray[i];
        }
        return fArray9;
    }

    private static float[] internalMethod04369(float[] fArray, float[] fArray2, float[] fArray3, int n, int n2) {
        float[] fArray4 = new float[n];
        for (int i = 0; i < n; ++i) {
            float f = fArray2[i];
            int n3 = i * n2;
            for (int j = 0; j < n2; ++j) {
                f += fArray[n3 + j] * fArray3[j];
            }
            fArray4[i] = f;
        }
        return fArray4;
    }

    private static double[] internalMethod07636(float[] fArray) {
        int n;
        int n2 = fArray.length;
        double d = Double.NEGATIVE_INFINITY;
        for (float f : fArray) {
            if (!((double)f > d)) continue;
            d = f;
        }
        double d2 = 0.0;
        double[] dArray = new double[n2];
        for (n = 0; n < n2; ++n) {
            dArray[n] = Math.exp((double)fArray[n] - d);
            d2 += dArray[n];
        }
        n = 0;
        while (n < n2) {
            int n3 = n++;
            dArray[n3] = dArray[n3] / d2;
        }
        return dArray;
    }

    private static float internalMethod00912(float f) {
        return (float)(1.0 / (1.0 + Math.exp(-f)));
    }

    private static float internalMethod00970(float f) {
        return (float)Math.tanh(f);
    }

    public static final class InternalType0137 {
        public final float[][][] internalField0089;
        public final double[] internalField0612;

        InternalType0137(float[][][] fArray, double[] dArray) {
            this.internalField0089 = fArray;
            this.internalField0612 = dArray;
        }
    }
}

