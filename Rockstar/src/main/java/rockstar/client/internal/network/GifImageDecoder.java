package rockstar.client.internal.network;


import rockstar.client.*;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import lombok.Generated;

public class GifImageDecoder {
    public static final int internalField0227 = 0;
    public static final int internalField0228 = 1;
    public static final int internalField1053 = 2;
    public BufferedInputStream internalField0189;
    public int internalField1055;
    public int internalField1056;
    public int internalField1054;
    public boolean internalField0277;
    public int internalField1464;
    public int internalField1470 = 1;
    public int[] internalField0618;
    public int[] internalField0617;
    public int[] internalField1241;
    public int internalField1465;
    public int internalField1463;
    public int internalField1466;
    public int internalField1467;
    public boolean internalField0276;
    public boolean internalField1099;
    public int internalField1469;
    public int internalField1468;
    public int internalField1740;
    public int internalField1741;
    public int internalField1736;
    public Rectangle internalField0303;
    public BufferedImage internalField0297;
    public BufferedImage internalField0298;
    public byte[] internalField0609 = new byte[256];
    public int internalField1735 = 0;
    public int internalField1748 = 0;
    public int internalField1733 = 0;
    public boolean internalField1100 = false;
    public int internalField1738 = 0;
    public int internalField1739;
    public static final int internalField1742 = 4096;
    public short[] internalField0627;
    public byte[] internalField0610;
    public byte[] internalField1234;
    public byte[] internalField1235;
    public ArrayList<InternalType0110> internalField0085;
    public int internalField1743;

    public int internalMethod00920(int n) {
        this.internalField1738 = -1;
        if (n >= 0 && n < this.internalField1743) {
            this.internalField1738 = this.internalField0085.get((int)n).internalField0227;
        }
        return this.internalField1738;
    }

    public BufferedImage internalMethod03380() {
        return this.internalMethod07338(0);
    }

    public void internalMethod04031() {
        int n = 0;
        int[] nArray = ((DataBufferInt)this.internalField0297.getRaster().getDataBuffer()).getData();
        if (this.internalField1733 > 0) {
            if (this.internalField1733 == 3) {
                n = this.internalField1743 - 2;
                this.internalField0298 = n > 0 ? this.internalMethod07338(n - 1) : null;
            }
            if (this.internalField0298 != null) {
                int[] nArray2 = ((DataBufferInt)this.internalField0298.getRaster().getDataBuffer()).getData();
                System.arraycopy(nArray2, 0, nArray, 0, this.internalField1056 * this.internalField1054);
                if (this.internalField1733 == 2) {
                    Graphics2D graphics2D = this.internalField0297.createGraphics();
                    Color color = this.internalField1100 ? new Color(0, 0, 0, 0) : new Color(this.internalField1466);
                    graphics2D.setColor(color);
                    graphics2D.setComposite(AlphaComposite.Src);
                    graphics2D.fill(this.internalField0303);
                    graphics2D.dispose();
                }
            }
        }
        n = 1;
        int n2 = 8;
        int n3 = 0;
        for (int i = 0; i < this.internalField1736; ++i) {
            int n4 = i;
            if (this.internalField1099) {
                if (n3 >= this.internalField1736) {
                    switch (++n) {
                        case 2: {
                            n3 = 4;
                            break;
                        }
                        case 3: {
                            n3 = 2;
                            n2 = 4;
                            break;
                        }
                        case 4: {
                            n3 = 1;
                            n2 = 2;
                        }
                    }
                }
                n4 = n3;
                n3 += n2;
            }
            if ((n4 += this.internalField1740) >= this.internalField1054) continue;
            int n5 = n4 * this.internalField1056;
            int n6 = n5 + this.internalField1468;
            int n7 = n6 + this.internalField1741;
            if (n5 + this.internalField1056 < n7) {
                n7 = n5 + this.internalField1056;
            }
            int n8 = i * this.internalField1741;
            while (n6 < n7) {
                int n9;
                int n10;
                if ((n10 = this.internalField1241[n9 = this.internalField1235[n8++] & 0xFF]) != 0) {
                    nArray[n6] = n10;
                }
                ++n6;
            }
        }
    }

    public BufferedImage internalMethod07338(int n) {
        BufferedImage bufferedImage = null;
        if (n >= 0 && n < this.internalField1743) {
            bufferedImage = this.internalField0085.get((int)n).internalField0297;
        }
        return bufferedImage;
    }

    public Dimension internalMethod03361() {
        return new Dimension(this.internalField1056, this.internalField1054);
    }

    public int internalMethod01604(BufferedInputStream bufferedInputStream) {
        this.internalMethod07886();
        if (bufferedInputStream != null) {
            this.internalField0189 = bufferedInputStream;
            this.internalMethod07898();
            if (!this.internalMethod04032()) {
                this.internalMethod07888();
                if (this.internalField1743 < 0) {
                    this.internalField1055 = 1;
                }
            }
        } else {
            this.internalField1055 = 2;
        }
        try {
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return this.internalField1055;
    }

    public int internalMethod06978(InputStream inputStream) {
        this.internalMethod07886();
        if (inputStream != null) {
            if (!(inputStream instanceof BufferedInputStream)) {
                inputStream = new BufferedInputStream(inputStream);
            }
            this.internalField0189 = (BufferedInputStream)inputStream;
            this.internalMethod07898();
            if (!this.internalMethod04032()) {
                this.internalMethod07888();
                if (this.internalField1743 < 0) {
                    this.internalField1055 = 1;
                }
            }
        } else {
            this.internalField1055 = 2;
        }
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return this.internalField1055;
    }

    public int internalMethod01367(String string) {
        this.internalField1055 = 0;
        try {
            string = string.trim().toLowerCase();
            if (string.contains("file:") || string.indexOf(":/") > 0) {
                URL uRL = new URL(string);
                this.internalField0189 = new BufferedInputStream(uRL.openStream());
            } else {
                this.internalField0189 = new BufferedInputStream(new FileInputStream(string));
            }
            this.internalField1055 = this.internalMethod01604(this.internalField0189);
        }
        catch (IOException iOException) {
            this.internalField1055 = 2;
        }
        return this.internalField1055;
    }

    public void internalMethod04037() {
        int n;
        int n2 = -1;
        int n3 = this.internalField1741 * this.internalField1736;
        if (this.internalField1235 == null || this.internalField1235.length < n3) {
            this.internalField1235 = new byte[n3];
        }
        if (this.internalField0627 == null) {
            this.internalField0627 = new short[4096];
        }
        if (this.internalField0610 == null) {
            this.internalField0610 = new byte[4096];
        }
        if (this.internalField1234 == null) {
            this.internalField1234 = new byte[4097];
        }
        int n4 = this.internalMethod04030();
        int n5 = 1 << n4;
        int n6 = n5 + 1;
        int n7 = n5 + 2;
        int n8 = n2;
        int n9 = n4 + 1;
        int n10 = (1 << n9) - 1;
        for (n = 0; n < n5; ++n) {
            this.internalField0627[n] = 0;
            this.internalField0610[n] = (byte)n;
        }
        int n11 = 0;
        int n12 = 0;
        int n13 = 0;
        int n14 = 0;
        int n15 = 0;
        int n16 = 0;
        int n17 = 0;
        int n18 = 0;
        while (n18 < n3) {
            if (n13 == 0) {
                if (n16 < n9) {
                    if (n15 == 0) {
                        n15 = this.internalMethod04036();
                        if (n15 <= 0) break;
                        n11 = 0;
                    }
                    n17 += (this.internalField0609[n11] & 0xFF) << n16;
                    n16 += 8;
                    ++n11;
                    --n15;
                    continue;
                }
                n = n17 & n10;
                n17 >>= n9;
                n16 -= n9;
                if (n > n7 || n == n6) break;
                if (n == n5) {
                    n9 = n4 + 1;
                    n10 = (1 << n9) - 1;
                    n7 = n5 + 2;
                    n8 = n2;
                    continue;
                }
                if (n8 == n2) {
                    this.internalField1234[n13++] = this.internalField0610[n];
                    n8 = n;
                    n14 = n;
                    continue;
                }
                int n19 = n;
                if (n == n7) {
                    this.internalField1234[n13++] = (byte)n14;
                    n = n8;
                }
                while (n > n5) {
                    this.internalField1234[n13++] = this.internalField0610[n];
                    n = this.internalField0627[n];
                }
                n14 = this.internalField0610[n] & 0xFF;
                if (n7 >= 4096) {
                    this.internalField1234[n13++] = (byte)n14;
                    continue;
                }
                this.internalField1234[n13++] = (byte)n14;
                this.internalField0627[n7] = (short)n8;
                this.internalField0610[n7] = (byte)n14;
                if ((++n7 & n10) == 0 && n7 < 4096) {
                    ++n9;
                    n10 += n7;
                }
                n8 = n19;
            }
            this.internalField1235[n12++] = this.internalField1234[--n13];
            ++n18;
        }
        for (n18 = n12; n18 < n3; ++n18) {
            this.internalField1235[n18] = 0;
        }
    }

    public boolean internalMethod04032() {
        return this.internalField1055 != 0;
    }

    public void internalMethod07886() {
        this.internalField1055 = 0;
        this.internalField1743 = 0;
        this.internalField0085 = new ArrayList();
        this.internalField0618 = null;
        this.internalField0617 = null;
    }

    public int internalMethod04030() {
        int n = 0;
        try {
            n = this.internalField0189.read();
        }
        catch (IOException iOException) {
            this.internalField1055 = 1;
        }
        return n;
    }

    public int internalMethod04036() {
        int n = 0;
        this.internalField1735 = this.internalMethod04030();
        if (this.internalField1735 > 0) {
            try {
                int n2;
                for (n = 0; n < this.internalField1735 && (n2 = this.internalField0189.read(this.internalField0609, n, this.internalField1735 - n)) != -1; n += n2) {
                }
            }
            catch (IOException iOException) {
                // empty catch block
            }
            if (n < this.internalField1735) {
                this.internalField1055 = 1;
            }
        }
        return n;
    }

    public int[] internalMethod01172(int n) {
        int n2 = 3 * n;
        int[] nArray = null;
        byte[] byArray = new byte[n2];
        int n3 = 0;
        try {
            n3 = this.internalField0189.read(byArray);
        }
        catch (IOException iOException) {
            // empty catch block
        }
        if (n3 < n2) {
            this.internalField1055 = 1;
        } else {
            nArray = new int[256];
            int n4 = 0;
            int n5 = 0;
            while (n4 < n) {
                int n6 = byArray[n5++] & 0xFF;
                int n7 = byArray[n5++] & 0xFF;
                int n8 = byArray[n5++] & 0xFF;
                nArray[n4++] = 0xFF000000 | n6 << 16 | n7 << 8 | n8;
            }
        }
        return nArray;
    }

    public void internalMethod07888() {
        boolean bl = false;
        block10: while (!bl && !this.internalMethod04032()) {
            int n = this.internalMethod04030();
            switch (n) {
                case 44: {
                    this.internalMethod09272();
                    continue block10;
                }
                case 33: {
                    n = this.internalMethod04030();
                    switch (n) {
                        case 249: {
                            this.internalMethod07897();
                            continue block10;
                        }
                        case 255: {
                            this.internalMethod04036();
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < 11; ++i) {
                                stringBuilder.append((char)this.internalField0609[i]);
                            }
                            if (stringBuilder.toString().equals("NETSCAPE2.0")) {
                                this.internalMethod09278();
                                continue block10;
                            }
                            this.internalMethod09457();
                            continue block10;
                        }
                    }
                    this.internalMethod09457();
                    continue block10;
                }
                case 59: {
                    bl = true;
                    continue block10;
                }
                case 0: {
                    continue block10;
                }
            }
            this.internalField1055 = 1;
        }
    }

    public void internalMethod07897() {
        this.internalMethod04030();
        int n = this.internalMethod04030();
        this.internalField1748 = (n & 0x1C) >> 2;
        if (this.internalField1748 == 0) {
            this.internalField1748 = 1;
        }
        this.internalField1100 = (n & 1) != 0;
        this.internalField1738 = this.internalMethod07885() * 10;
        this.internalField1739 = this.internalMethod04030();
        this.internalMethod04030();
    }

    public void internalMethod07898() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; ++i) {
            stringBuilder.append((char)this.internalMethod04030());
        }
        if (!stringBuilder.toString().startsWith("GIF")) {
            this.internalField1055 = 1;
            return;
        }
        this.internalMethod09273();
        if (this.internalField0277 && !this.internalMethod04032()) {
            this.internalField0618 = this.internalMethod01172(this.internalField1464);
            this.internalField1463 = this.internalField0618[this.internalField1465];
        }
    }

    public void internalMethod09272() {
        this.internalField1468 = this.internalMethod07885();
        this.internalField1740 = this.internalMethod07885();
        this.internalField1741 = this.internalMethod07885();
        this.internalField1736 = this.internalMethod07885();
        int n = this.internalMethod04030();
        this.internalField0276 = (n & 0x80) != 0;
        this.internalField1099 = (n & 0x40) != 0;
        this.internalField1469 = 2 << (n & 7);
        if (this.internalField0276) {
            this.internalField0617 = this.internalMethod01172(this.internalField1469);
            this.internalField1241 = this.internalField0617;
        } else {
            this.internalField1241 = this.internalField0618;
            if (this.internalField1465 == this.internalField1739) {
                this.internalField1463 = 0;
            }
        }
        int n2 = 0;
        if (this.internalField1100) {
            n2 = this.internalField1241[this.internalField1739];
            this.internalField1241[this.internalField1739] = 0;
        }
        if (this.internalField1241 == null) {
            this.internalField1055 = 1;
        }
        if (this.internalMethod04032()) {
            return;
        }
        this.internalMethod04037();
        this.internalMethod09457();
        if (this.internalMethod04032()) {
            return;
        }
        ++this.internalField1743;
        this.internalField0297 = new BufferedImage(this.internalField1056, this.internalField1054, 3);
        this.internalMethod04031();
        this.internalField0085.add(new InternalType0110(this.internalField0297, this.internalField1738));
        if (this.internalField1100) {
            this.internalField1241[this.internalField1739] = n2;
        }
        this.internalMethod09279();
    }

    public void internalMethod09273() {
        this.internalField1056 = this.internalMethod07885();
        this.internalField1054 = this.internalMethod07885();
        int n = this.internalMethod04030();
        this.internalField0277 = (n & 0x80) != 0;
        this.internalField1464 = 2 << (n & 7);
        this.internalField1465 = this.internalMethod04030();
        this.internalField1467 = this.internalMethod04030();
    }

    public void internalMethod09278() {
        do {
            this.internalMethod04036();
            if (this.internalField0609[0] != 1) continue;
            int n = this.internalField0609[1] & 0xFF;
            int n2 = this.internalField0609[2] & 0xFF;
            this.internalField1470 = n2 << 8 | n;
        } while (this.internalField1735 > 0 && !this.internalMethod04032());
    }

    public int internalMethod07885() {
        return this.internalMethod04030() | this.internalMethod04030() << 8;
    }

    public void internalMethod09279() {
        this.internalField1733 = this.internalField1748;
        this.internalField0303 = new Rectangle(this.internalField1468, this.internalField1740, this.internalField1741, this.internalField1736);
        this.internalField0298 = this.internalField0297;
        this.internalField1466 = this.internalField1463;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        this.internalField0617 = null;
    }

    public void internalMethod09457() {
        do {
            this.internalMethod04036();
        } while (this.internalField1735 > 0 && !this.internalMethod04032());
    }

    @Generated
    public int internalMethod07887() {
        return this.internalField1743;
    }

    public static class InternalType0110 {
        public BufferedImage internalField0297;
        public int internalField0227;

        public InternalType0110(BufferedImage bufferedImage, int n) {
            this.internalField0297 = bufferedImage;
            this.internalField0227 = n;
        }
    }
}
