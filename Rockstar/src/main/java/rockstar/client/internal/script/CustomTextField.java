package rockstar.client.internal.script;










import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.MathHelper;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.core.TextFieldKeyActions;
import rockstar.client.internal.script.EmojiTextNode;
import rockstar.client.internal.core.FuzzySearchUtils;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.ui.ImeCompositionHelper;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.MathUtils;
import rockstar.client.ui.LegacyUiElement;
import rockstar.client.render.HudRenderUtils;
import rockstar.client.render.ScissorStack;
import rockstar.client.internal.render.UiVertexBatch;
import rockstar.client.internal.render.PlainGlyphBatch;
import rockstar.client.internal.core.ClientSoundEvents;
import rockstar.client.util.Stopwatch;

public class CustomTextField
extends LegacyUiElement
implements MinecraftClientAccess {
    public static CustomTextField internalField0936;
    private static final Pattern internalField0293;
    private final HashMap<Character, Float> internalField0329 = new HashMap();
    private final List<InternalType0163> internalField0416 = new ArrayList<InternalType0163>();
    private final SizedFont internalField0447;
    private float[] internalField0615 = new float[0];
    private float[] internalField0616 = new float[]{0.0f};
    private boolean internalField0277 = true;
    private String internalField0248 = "";
    private final AnimatedValue internalField0808 = new AnimatedValue(300L, 0.0f, Easing.internalField1626);
    private boolean internalField0276;
    private final AnimatedValue internalField0809 = new AnimatedValue(300L, 0.0f, Easing.internalField0812);
    private int internalField0227;
    private InternalType0162 internalField0487;
    private int internalField0228 = -1;
    private long internalField0229 = 0L;
    private int internalField1053 = 0;
    private final Stopwatch internalField0519 = new Stopwatch();
    private String internalField0247 = "";
    private String internalField1077 = "";
    private Map<String, TextFieldKeyActions> internalField0543 = new HashMap<String, TextFieldKeyActions>();
    private String internalField1076 = "";
    private float internalField1049;
    private final Stopwatch internalField0518 = new Stopwatch();
    private float internalField1046 = 1.0f;
    private ColorRGBA internalField0777 = ColorRGBA.WHITE;
    private boolean internalField1099;
    private int internalField1055;
    private boolean internalField1100;

    public void internalMethod00484(String string) {
        if (string == null) {
            string = "";
        }
        if (this.internalField1099) {
            string = this.internalMethod07267(string);
        }
        this.internalField0416.clear();
        this.internalField0248 = "";
        this.internalField0227 = 0;
        Matcher matcher = internalField0293.matcher(string);
        while (matcher.find() && (this.internalField1055 <= 0 || this.internalField0416.size() < this.internalField1055)) {
            InternalType0163 nestedValue2022 = new InternalType0163(matcher.group());
            nestedValue2022.internalField0808.internalMethod07060(1.0f);
            this.internalField0416.add(nestedValue2022);
        }
        this.internalField0277 = true;
        this.internalMethod09127();
        this.internalField0227 = this.internalField0416.size();
        this.internalField0487 = null;
        this.internalField1049 = 0.0f;
        this.internalField0519.internalMethod00701();
    }

    public final void internalMethod00344() {
        if (this.internalField0416.isEmpty()) {
            this.internalField0487 = null;
            this.internalField0227 = 0;
            return;
        }
        this.internalField0487 = new InternalType0162(true, 0, this.internalField0416.size());
        this.internalField0227 = this.internalField0416.size();
    }

    private String internalMethod07267(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = false;
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (Character.isDigit(c)) {
                stringBuilder.append(c);
                continue;
            }
            if (!(c != '.' && c != ',' || bl)) {
                stringBuilder.append('.');
                bl = true;
                continue;
            }
            if (c != '-' || !stringBuilder.isEmpty()) continue;
            stringBuilder.append('-');
        }
        if (stringBuilder.length() == 1 && stringBuilder.charAt(0) == '-') {
            return "";
        }
        return stringBuilder.toString();
    }

    @Override
    public void internalMethod05619(UiRenderContext iII) {
        float f;
        float f2;
        if (this.internalField0276) {
            ImeCompositionHelper.internalMethod04762();
        }
        float f3 = 0.0f;
        float f4 = this.internalField1047 / 2.0f - this.internalField0447.internalMethod04890() / 2.0f;
        float f5 = this.internalField0447.internalMethod04890() / 8.0f;
        if (this.internalField0416.removeIf(nestedValue2022 -> nestedValue2022.internalField0808.internalMethod02881() == 0.0f && nestedValue2022.internalField0277)) {
            this.internalField0277 = true;
        }
        this.internalField0227 = Math.min(this.internalField0227, this.internalField0416.size());
        this.internalMethod09485();
        this.internalField0808.internalMethod07062(this.internalField0276);
        if (this.internalField0487 != null) {
            this.internalField0487.internalField0227 = MathHelper.clamp((int)this.internalField0487.internalField0227, (int)0, (int)this.internalField0416.size());
            this.internalField0487.internalField0228 = MathHelper.clamp((int)this.internalField0487.internalField0228, (int)0, (int)this.internalField0416.size());
            if (this.internalField0487.internalMethod02579() == this.internalField0487.internalMethod02584()) {
                this.internalField0487 = null;
            }
        }
        if (this.internalField0228 != -1) {
            this.internalField0519.internalMethod00701();
            int n = -1;
            f2 = 0.0f;
            for (int i = 0; i < this.internalField0416.size(); ++i) {
                f = this.internalField0615[i];
                if ((float)iII.internalMethod05259() < this.internalField0205 + this.internalField1049 + f2 + f + f / 2.0f) {
                    n = i;
                    break;
                }
                f2 += f;
            }
            if (n == -1) {
                n = this.internalField0416.size();
            }
            if (n != this.internalField0228) {
                this.internalField0487 = new InternalType0162(n > this.internalField0228, Math.min(this.internalField0228, n), Math.max(this.internalField0228, n));
                this.internalField0227 = n;
            } else {
                if (this.internalField0487 != null) {
                    this.internalField0227 = this.internalField0487.internalMethod02579();
                }
                this.internalField0487 = null;
            }
        }
        if (this.internalMethod03399(iII) && this.internalField1046 > 0.0f) {
            CursorManager.internalMethod06882(CursorType.internalField1206);
        }
        this.internalMethod09127();
        float f6 = this.internalField0616[this.internalField0227];
        f2 = this.internalField0487 == null ? 0.0f : this.internalField0616[this.internalField0487.internalMethod02579()];
        float f7 = this.internalField0487 == null ? 0.0f : this.internalField0616[this.internalField0487.internalMethod02584()];
        ScissorStack.internalMethod06303(iII.getMatrices(), this.internalField0205, this.internalField0206, this.internalField1048, this.internalField1047);
        f = RenderSystem.getShaderColor()[3];
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)(f * this.internalField1046));
        iII.drawRect(this.internalField0205 + this.internalField1049 + f4 + f2, this.internalField0206 + f4 - 1.0f, f7 - f2, this.internalField0447.internalMethod04890() + 2.0f, ThemeColors.internalField1310.withAlpha(114.75f * this.internalField0808.internalMethod02881()));
        PlainGlyphBatch typedValue252 = new PlainGlyphBatch(VertexFormats.POSITION_TEXTURE_COLOR, this.internalField0447.internalMethod01335());
        if (this.internalField0416.isEmpty()) {
            iII.drawText(this.internalField0447, this.internalField0247, this.internalField0205 + f3 + f4, this.internalField0206 + f4 - 2.0f * this.internalField0808.internalMethod02881(), this.internalField0777.mulAlpha(0.75f * (1.0f - this.internalField0808.internalMethod02881())));
        }
        if (!this.internalField1076.isEmpty() && this.internalField1076.toLowerCase().startsWith(this.internalField0248.toLowerCase()) && !this.internalField0248.isEmpty()) {
            iII.drawText(this.internalField0447, this.internalField0248 + this.internalField1076.substring(this.internalField0248.length()), this.internalField0205 + f3 + f4, this.internalField0206 + f4, this.internalField0777.mulAlpha(0.5882353f * this.internalField0808.internalMethod02881()));
        }
        for (int i = 0; i < this.internalField0416.size(); ++i) {
            InternalType0163 nestedValue2023 = this.internalField0416.get(i);
            String string = nestedValue2023.internalField0248;
            if (this.internalField1100) {
                string = "*";
            }
            nestedValue2023.internalField0808.internalMethod07061(200L);
            nestedValue2023.internalField0808.internalMethod07062(!nestedValue2023.internalField0277);
            EmojiTextNode.internalMethod06314(iII, this.internalField0447, string, this.internalField0205 + f3 + f4 + this.internalField1049, this.internalField0206 + f4 + 2.0f - 2.0f * nestedValue2023.internalField0808.internalMethod02881(), this.internalField0447.internalMethod04890(), this.internalField0777.mulAlpha(nestedValue2023.internalField0808.internalMethod02881()));
            f3 += this.internalField0615[i] * nestedValue2023.internalField0808.internalMethod02881();
        }
        ((UiVertexBatch)typedValue252).internalMethod09053();
        f6 += (float)(this.internalField0227 == this.internalField0416.size() ? 1 : 0);
        if (this.internalField0518.internalMethod02365(10L)) {
            if (!this.internalField0416.isEmpty() && f6 + f4 + this.internalField1049 > this.internalField1048 - 5.0f) {
                this.internalField1049 -= this.internalField0615[0];
                this.internalField0518.internalMethod00701();
            } else if (!this.internalField0416.isEmpty() && f6 + f4 + this.internalField1049 < 5.0f) {
                this.internalField1049 += this.internalField0615[0];
                this.internalField0518.internalMethod00701();
            }
            if (EmojiTextNode.internalMethod06011(this.internalField0447, this.internalField0248) < this.internalField1048 - 10.0f) {
                this.internalField1049 = 0.0f;
            }
        }
        this.internalField0809.internalMethod06645(Easing.internalField0811);
        this.internalField0809.internalMethod07059(f6);
        HudRenderUtils.internalMethod02865(iII.getMatrices(), this.internalField0205 + f4 + this.internalField1049 + this.internalField0809.internalMethod02881() + f5 / 2.0f, this.internalField0206 + f4 - 1.0f, Math.clamp(f6 - this.internalField0809.internalMethod02881(), -20.0f, 20.0f));
        iII.drawRect(this.internalField0205 + f4 + this.internalField0809.internalMethod02881() + this.internalField1049, this.internalField0206 + f4 - 1.0f, f5, this.internalField0447.internalMethod04890() + 2.0f, this.internalField0777.mulAlpha((float)((double)(0.78431374f * this.internalField0808.internalMethod02881()) * (!this.internalField0519.internalMethod02365(300L) ? 3.0 : MathUtils.internalMethod04857((double)System.currentTimeMillis() / 200.0) + 2.0) / 3.0)));
        HudRenderUtils.internalMethod00012(iII.getMatrices());
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f);
        ScissorStack.internalMethod07643();
    }

    @Override
    public void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
        if (this.internalMethod04931(d, d2)) {
            if (typedParameter1015 == MouseButton.internalField0102) {
                long l = System.currentTimeMillis();
                this.internalField1053 = l - this.internalField0229 < 500L ? ++this.internalField1053 : 1;
                this.internalField0229 = l;
                this.internalMethod07508(true);
                float f = 0.0f;
                int n = this.internalField0416.size();
                this.internalMethod09485();
                for (int i = 0; i < this.internalField0416.size(); ++i) {
                    float f2 = this.internalField0615[i];
                    if (d < (double)(this.internalField0205 + this.internalField1049 + f + f2 + f2 / 2.0f)) {
                        n = i;
                        break;
                    }
                    f += f2;
                }
                this.internalField0227 = n;
                if (this.internalField1053 == 2) {
                    this.internalMethod09140();
                    this.internalField0228 = -1;
                } else {
                    this.internalField0487 = null;
                    this.internalField0228 = this.internalField0227;
                }
            }
        } else {
            this.internalMethod07508(false);
        }
    }

    @Override
    public void internalMethod02863(double d, double d2, MouseButton typedParameter1015) {
        this.internalField0228 = -1;
    }

    public void internalMethod07508(boolean bl) {
        if (bl && internalField0936 != null && internalField0936 != this) {
            internalField0936.internalMethod07508(false);
        }
        this.internalField0276 = bl;
        if (bl) {
            internalField0936 = this;
        }
    }

    @Override
    public void internalMethod05727(int n, int n2, int n3) {
        if (!this.internalField0276) {
            return;
        }
        if ((n == 259 || n == 261) && this.internalField0487 != null) {
            this.internalMethod09142();
            ClientSoundEvents.internalField1428.internalMethod03132(0.3f, 1.2f);
            this.internalMethod07562(0);
        } else if (n == 259 && this.internalField0227 > 0) {
            int n4 = rockstar.client.compat.InputCompat.hasControlDown() ? Math.max(1, this.internalMethod07507(false)) : 1;
            for (int i = 0; i < n4; ++i) {
                this.internalMethod07562(-1);
                InternalType0163 nestedValue2022 = null;
                for (int j = this.internalField0227; j >= 0; --j) {
                    InternalType0163 nestedValue2023 = this.internalField0416.get(j);
                    if (nestedValue2023.internalField0277) continue;
                    nestedValue2022 = nestedValue2023;
                    break;
                }
                if (nestedValue2022 == null) continue;
                nestedValue2022.internalField0277 = true;
            }
            ClientSoundEvents.internalField1428.internalMethod03132(0.3f, 1.2f);
        } else if (n == 261 && this.internalField0227 < this.internalField0416.size()) {
            int n5 = rockstar.client.compat.InputCompat.hasControlDown() ? Math.max(1, this.internalMethod07507(true)) : 1;
            block2: for (int i = 0; i < n5 && this.internalField0227 < this.internalField0416.size(); ++i) {
                for (int j = this.internalField0227; j < this.internalField0416.size(); ++j) {
                    InternalType0163 nestedValue2022 = this.internalField0416.get(j);
                    if (nestedValue2022.internalField0277) continue;
                    nestedValue2022.internalField0277 = true;
                    continue block2;
                }
            }
            this.internalMethod07562(0);
            ClientSoundEvents.internalField1428.internalMethod03132(0.3f, 1.2f);
        } else if (n == 263) {
            int n6;
            ClientSoundEvents.internalField1428.internalMethod03132(0.3f, 1.3f);
            int n7 = n6 = rockstar.client.compat.InputCompat.hasControlDown() ? Math.max(1, this.internalMethod07507(false)) : 1;
            if (rockstar.client.compat.InputCompat.hasShiftDown()) {
                this.internalMethod08303(-n6);
            } else if (this.internalField0487 != null) {
                this.internalField0227 = this.internalField0487.internalMethod02579();
                this.internalField0487 = null;
                return;
            }
            this.internalMethod07562(-n6);
        } else if (n == 262) {
            int n8;
            ClientSoundEvents.internalField1428.internalMethod03132(0.3f, 1.3f);
            int n9 = n8 = rockstar.client.compat.InputCompat.hasControlDown() ? Math.max(1, this.internalMethod07507(true)) : 1;
            if (rockstar.client.compat.InputCompat.hasShiftDown()) {
                this.internalMethod08303(n8);
            } else if (this.internalField0487 != null) {
                this.internalField0227 = this.internalField0487.internalMethod02584();
                this.internalField0487 = null;
                return;
            }
            this.internalMethod07562(n8);
        } else if (rockstar.client.compat.InputCompat.isSelectAll(n)) {
            this.internalField0487 = new InternalType0162(true, 0, this.internalField0416.size());
        } else if (rockstar.client.compat.InputCompat.isCopy(n)) {
            if (this.internalField0487 != null) {
                CustomTextField.internalField0149.keyboard.setClipboard(this.internalMethod08080());
                return;
            }
            CustomTextField.internalField0149.keyboard.setClipboard(this.internalField0248);
        } else if (rockstar.client.compat.InputCompat.isCut(n)) {
            if (this.internalField0487 != null) {
                CustomTextField.internalField0149.keyboard.setClipboard(this.internalMethod08080());
                this.internalMethod09142();
                this.internalMethod07562(0);
                return;
            }
            CustomTextField.internalField0149.keyboard.setClipboard(this.internalField0248);
            for (InternalType0163 nestedValue2022 : this.internalField0416) {
                nestedValue2022.internalField0277 = true;
            }
            this.internalField0248 = "";
        } else if (rockstar.client.compat.InputCompat.isPaste(n)) {
            this.internalMethod07070(CustomTextField.internalField0149.keyboard.getClipboard());
        } else if (n == 258 || n == 257) {
            this.internalMethod09127();
            TextFieldKeyActions typedValue193 = this.internalField0543.get(this.internalField1076);
            if (typedValue193 != null) {
                this.internalMethod09126();
                if (n == 257) {
                    typedValue193.internalMethod05575().run();
                } else {
                    typedValue193.internalMethod00875().run();
                }
                this.internalMethod07508(false);
                return;
            }
            if (n == 257) {
                this.internalMethod07508(false);
            }
        } else if (n == 259 && this.internalField0416.isEmpty()) {
            this.internalMethod07508(false);
        }
        this.internalMethod09127();
    }

    @Override
    public boolean internalMethod05413(char c, int n) {
        if (!this.internalField0276) {
            return false;
        }
        if (this.internalField1099) {
            if (!Character.isDigit(c) && c != '.' && c != '-' && c != ',') {
                return false;
            }
            if (c == '-' && this.internalField0227 != 0) {
                return false;
            }
            if ((c == '.' || c == ',') && (this.internalField0248.contains(".") || this.internalField0248.contains(","))) {
                return false;
            }
            if (c == ',') {
                c = (char)46;
            }
        }
        if (c == ' ') {
            ClientSoundEvents.internalField1428.internalMethod03132(0.3f, 0.8f);
        } else {
            if (!this.internalField0329.containsKey(Character.valueOf(c))) {
                this.internalField0329.put(Character.valueOf(c), Float.valueOf(MathUtils.internalMethod05368(0.8, 1.2)));
            }
            ClientSoundEvents.internalField1428.internalMethod03132(0.3f, this.internalField0329.get(Character.valueOf(c)).floatValue());
        }
        this.internalMethod07505(c);
        this.internalMethod09127();
        return true;
    }

    private void internalMethod09127() {
        String string = this.internalField0248;
        StringBuilder stringBuilder = new StringBuilder();
        for (InternalType0163 nestedValue2022 : this.internalField0416) {
            if (nestedValue2022.internalField0277) continue;
            stringBuilder.append(nestedValue2022.internalField0248);
        }
        this.internalField0248 = stringBuilder.toString();
        if (!string.equals(this.internalField0248)) {
            this.internalField1076 = "";
            String string2 = FuzzySearchUtils.internalMethod00096(this.internalField0248);
            int n = Integer.MAX_VALUE;
            for (String string3 : this.internalField0543.keySet()) {
                int n2 = FuzzySearchUtils.internalMethod05951(FuzzySearchUtils.internalMethod00096(string3), string2);
                if (n2 >= n && (n2 != n || string3.compareToIgnoreCase(this.internalField1076) >= 0)) continue;
                this.internalField1076 = string3;
                n = n2;
            }
        }
    }

    public int internalMethod07507(boolean bl) {
        int n = 0;
        if (bl) {
            for (int i = this.internalField0227; i < this.internalField0416.size(); ++i) {
                InternalType0163 nestedValue2022 = this.internalField0416.get(i);
                if (!nestedValue2022.internalField0277 && !nestedValue2022.internalField0248.equals(" ")) {
                    ++n;
                    continue;
                }
                break;
            }
        } else {
            for (int i = this.internalField0227 - 1; i >= 0; --i) {
                InternalType0163 nestedValue2022 = this.internalField0416.get(i);
                if (!nestedValue2022.internalField0277 && !nestedValue2022.internalField0248.equals(" ")) {
                    ++n;
                    continue;
                }
                break;
            }
        }
        return n;
    }

    public void internalMethod07070(String string) {
        Matcher matcher = internalField0293.matcher(string == null ? "" : string);
        while (matcher.find()) {
            this.internalMethod07891(matcher.group());
        }
    }

    public void internalMethod07505(char c) {
        this.internalMethod07891(String.valueOf(c));
    }

    private void internalMethod07891(String string) {
        this.internalMethod09142();
        if (this.internalField0227 > 0 && this.internalField0227 <= this.internalField0416.size()) {
            InternalType0163 nestedValue2022 = this.internalField0416.get(this.internalField0227 - 1);
            if (!nestedValue2022.internalField0277 && this.internalMethod01138(nestedValue2022.internalField0248, string)) {
                nestedValue2022.internalField0248 = nestedValue2022.internalField0248 + string;
                this.internalField0277 = true;
                return;
            }
        }
        if (this.internalField1055 > 0 && this.internalField0416.size() >= this.internalField1055) {
            return;
        }
        this.internalField0416.add(Math.clamp((long)this.internalField0227, 0, this.internalField0416.size()), new InternalType0163(string));
        this.internalField0277 = true;
        this.internalMethod07562(1);
        internalField0936 = this;
    }

    private boolean internalMethod01138(String string, String string2) {
        if (string.endsWith("\u200d")) {
            return true;
        }
        if (string.length() == 1 && Character.isHighSurrogate(string.charAt(0)) && Character.isLowSurrogate(string2.charAt(0))) {
            return true;
        }
        int n = string2.codePointAt(0);
        if (n == 8205 || n == 8419 || n == 65038 || n == 65039 || n >= 127995 && n <= 127999) {
            return true;
        }
        int n2 = string.codePointCount(0, string.length());
        int n3 = string.codePointAt(0);
        return n2 == 1 && n3 >= 127462 && n3 <= 127487 && n >= 127462 && n <= 127487;
    }

    private void internalMethod07562(int n) {
        this.internalField0227 = MathHelper.clamp((int)(this.internalField0227 + n), (int)0, (int)this.internalField0416.size());
        this.internalField0519.internalMethod00701();
    }

    public void internalMethod09126() {
        this.internalField0416.clear();
        this.internalField0277 = true;
        this.internalField0248 = "";
        this.internalField1076 = "";
    }

    private void internalMethod09140() {
        InternalType0163 nestedValue2022;
        if (this.internalField0416.isEmpty()) {
            return;
        }
        int n = this.internalField0227;
        int n2 = this.internalField0227;
        int n3 = this.internalField0227 - 1;
        while (n3 >= 0) {
            nestedValue2022 = this.internalField0416.get(n3);
            if (nestedValue2022.internalField0277 || nestedValue2022.internalField0248.equals(" ") || !nestedValue2022.internalField0248.codePoints().allMatch(Character::isLetterOrDigit)) break;
            n = n3--;
        }
        for (n3 = this.internalField0227; n3 < this.internalField0416.size(); ++n3) {
            nestedValue2022 = this.internalField0416.get(n3);
            if (nestedValue2022.internalField0277 || nestedValue2022.internalField0248.equals(" ") || !nestedValue2022.internalField0248.codePoints().allMatch(Character::isLetterOrDigit)) break;
            n2 = n3 + 1;
        }
        if (n != n2) {
            this.internalField0487 = new InternalType0162(true, n, n2);
            this.internalField0227 = n2;
        }
    }

    private void internalMethod09142() {
        if (this.internalField0487 == null) {
            return;
        }
        for (InternalType0163 nestedValue2022 : this.internalMethod03780()) {
            nestedValue2022.internalField0277 = true;
        }
        this.internalField0227 = this.internalField0487.internalMethod02579();
        this.internalField0487 = null;
    }

    private List<InternalType0163> internalMethod03780() {
        ArrayList<InternalType0163> arrayList = new ArrayList<InternalType0163>();
        for (int i = this.internalField0487.internalMethod02579(); i < this.internalField0487.internalMethod02584(); ++i) {
            arrayList.add(this.internalField0416.get(i));
        }
        return arrayList;
    }

    private String internalMethod08080() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = this.internalField0487.internalMethod02579(); i < this.internalField0487.internalMethod02584(); ++i) {
            stringBuilder.append(this.internalField0416.get((int)i).internalField0248);
        }
        return stringBuilder.toString();
    }

    private void internalMethod08303(int n) {
        if (this.internalField0487 == null) {
            this.internalField0487 = new InternalType0162(n > 0, this.internalField0227, this.internalField0227);
        }
        if (!this.internalField0487.internalField0277) {
            this.internalField0487.internalField0227 = MathHelper.clamp((int)(this.internalField0487.internalMethod02579() + n), (int)0, (int)this.internalField0416.size());
        } else {
            this.internalField0487.internalField0228 = MathHelper.clamp((int)(this.internalField0487.internalMethod02584() + n), (int)0, (int)this.internalField0416.size());
        }
    }

    public void internalMethod07506(int n) {
        this.internalField1055 = n;
        if (n > 0 && this.internalField0416.size() > n) {
            while (this.internalField0416.size() > n) {
                this.internalField0416.remove(this.internalField0416.size() - 1);
            }
            this.internalField0277 = true;
            this.internalField0227 = Math.min(this.internalField0227, this.internalField0416.size());
            this.internalMethod09127();
        }
    }

    public CustomTextField internalMethod01541() {
        this.internalField1100 = true;
        this.internalField0277 = true;
        return this;
    }

    private void internalMethod09485() {
        if (!this.internalField0277) {
            return;
        }
        int n = this.internalField0416.size();
        if (this.internalField0615.length < n) {
            this.internalField0615 = new float[n];
        }
        if (this.internalField0616.length <= n) {
            this.internalField0616 = new float[n + 1];
        }
        this.internalField0616[0] = 0.0f;
        for (int i = 0; i < n; ++i) {
            String string = this.internalField1100 ? "*" : this.internalField0416.get((int)i).internalField0248;
            this.internalField0615[i] = EmojiTextNode.internalMethod06011(this.internalField0447, string);
            this.internalField0616[i + 1] = this.internalField0616[i] + this.internalField0615[i];
        }
        this.internalField0277 = false;
    }

    @Generated
    public CustomTextField(SizedFont typedValue020) {
        this.internalField0447 = typedValue020;
    }

    @Generated
    public String internalMethod06202() {
        return this.internalField0248;
    }

    @Generated
    public boolean internalMethod00342() {
        return this.internalField0276;
    }

    @Generated
    public String internalMethod02722() {
        return this.internalField0247;
    }

    @Generated
    public String internalMethod08376() {
        return this.internalField1077;
    }

    @Generated
    public void internalMethod09000(String string) {
        this.internalField0247 = string;
    }

    @Generated
    public void internalMethod08723(String string) {
        this.internalField1077 = string;
    }

    @Generated
    public void internalMethod03471(Map<String, TextFieldKeyActions> map) {
        this.internalField0543 = map;
    }

    @Generated
    public String internalMethod07660() {
        return this.internalField1076;
    }

    @Generated
    public void internalMethod08627(float f) {
        this.internalField1046 = f;
    }

    @Generated
    public ColorRGBA internalMethod04350() {
        return this.internalField0777;
    }

    @Generated
    public void internalMethod00143(ColorRGBA colorRGBA) {
        this.internalField0777 = colorRGBA;
    }

    @Generated
    public boolean internalMethod00345() {
        return this.internalField1099;
    }

    @Generated
    public void internalMethod07563(boolean bl) {
        this.internalField1099 = bl;
    }

    @Generated
    public int internalMethod00341() {
        return this.internalField1055;
    }

    static {
        internalField0293 = Pattern.compile("\\X");
    }

    static class InternalType0163 {
        final AnimatedValue internalField0808 = new AnimatedValue(300L, 0.0f, Easing.internalField1626);
        boolean internalField0277;
        String internalField0248;

        InternalType0163(String string) {
            this.internalField0248 = string;
        }
    }

    static class InternalType0162 {
        final boolean internalField0277;
        int internalField0227;
        int internalField0228;

        int internalMethod02579() {
            return Math.min(this.internalField0228, this.internalField0227);
        }

        int internalMethod02584() {
            return Math.max(this.internalField0228, this.internalField0227);
        }

        @Generated
        public InternalType0162(boolean bl, int n, int n2) {
            this.internalField0277 = bl;
            this.internalField0227 = n;
            this.internalField0228 = n2;
        }
    }
}
