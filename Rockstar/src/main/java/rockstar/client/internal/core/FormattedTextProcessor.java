package rockstar.client.internal.core;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class FormattedTextProcessor {
    public static List<InternalType0470> internalMethod03885(Text text, int n) {
        ArrayList<InternalType0470> arrayList = new ArrayList<InternalType0470>();
        text.visit((style, string) -> {
            if (!string.isEmpty()) {
                int n2 = FormattedTextProcessor.internalMethod01365(style, n);
                boolean bl = style.isBold();
                boolean bl2 = style.isItalic();
                boolean bl3 = style.isUnderlined();
                boolean bl4 = style.isStrikethrough();
                arrayList.add(new InternalType0470(string, n2, bl, bl2, bl3, bl4));
            }
            return Optional.empty();
        }, Style.EMPTY);
        return arrayList;
    }

    private static int internalMethod01365(Style style, int n) {
        TextColor textColor = style.getColor();
        if (textColor != null) {
            return textColor.getRgb() | 0xFF000000;
        }
        return n;
    }

    public static class InternalType0470 {
        public final String internalField0248;
        public final int internalField0227;
        public final boolean internalField0277;
        public final boolean internalField0276;
        public final boolean internalField1099;
        public final boolean internalField1100;

        @Generated
        public InternalType0470(String string, int n, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
            this.internalField0248 = string;
            this.internalField0227 = n;
            this.internalField0277 = bl;
            this.internalField0276 = bl2;
            this.internalField1099 = bl3;
            this.internalField1100 = bl4;
        }

        @Generated
        public String internalMethod01791() {
            return this.internalField0248;
        }

        @Generated
        public int internalMethod05248() {
            return this.internalField0227;
        }

        @Generated
        public boolean internalMethod05249() {
            return this.internalField0277;
        }

        @Generated
        public boolean internalMethod05252() {
            return this.internalField0276;
        }

        @Generated
        public boolean internalMethod08403() {
            return this.internalField1099;
        }

        @Generated
        public boolean internalMethod08404() {
            return this.internalField1100;
        }

        @Generated
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof InternalType0470)) {
                return false;
            }
            InternalType0470 nestedValue0166 = (InternalType0470)object;
            if (!nestedValue0166.internalMethod05822(this)) {
                return false;
            }
            if (this.internalMethod05248() != nestedValue0166.internalMethod05248()) {
                return false;
            }
            if (this.internalMethod05249() != nestedValue0166.internalMethod05249()) {
                return false;
            }
            if (this.internalMethod05252() != nestedValue0166.internalMethod05252()) {
                return false;
            }
            if (this.internalMethod08403() != nestedValue0166.internalMethod08403()) {
                return false;
            }
            if (this.internalMethod08404() != nestedValue0166.internalMethod08404()) {
                return false;
            }
            String string = this.internalMethod01791();
            String string2 = nestedValue0166.internalMethod01791();
            return !(string == null ? string2 != null : !string.equals(string2));
        }

        @Generated
        protected boolean internalMethod05822(Object object) {
            return object instanceof InternalType0470;
        }

        @Generated
        public int hashCode() {
            int n = 59;
            int n2 = 1;
            n2 = n2 * 59 + this.internalMethod05248();
            n2 = n2 * 59 + (this.internalMethod05249() ? 79 : 97);
            n2 = n2 * 59 + (this.internalMethod05252() ? 79 : 97);
            n2 = n2 * 59 + (this.internalMethod08403() ? 79 : 97);
            n2 = n2 * 59 + (this.internalMethod08404() ? 79 : 97);
            String string = this.internalMethod01791();
            n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
            return n2;
        }

        @Generated
        public String toString() {
            return "FormattedTextProcessor.TextSegment(text=" + this.internalMethod01791() + ", color=" + this.internalMethod05248() + ", bold=" + this.internalMethod05249() + ", italic=" + this.internalMethod05252() + ", underlined=" + this.internalMethod08403() + ", strikethrough=" + this.internalMethod08404() + ")";
        }
    }
}

