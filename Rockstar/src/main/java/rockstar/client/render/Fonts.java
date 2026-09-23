package rockstar.client.render;


import rockstar.client.*;
import lombok.Generated;
import rockstar.client.render.FontFamily;

public final class Fonts {
    public static final FontFamily internalField0450 = FontFamily.internalMethod02606("bold", "bold");
    public static final FontFamily internalField0449 = FontFamily.internalMethod02606("medium", "medium");
    public static final FontFamily internalField1154 = FontFamily.internalMethod02606("regular", "regular");
    public static final FontFamily internalField1157 = FontFamily.internalMethod02606("semibold", "semi_bold");
    public static final FontFamily internalField1155 = FontFamily.internalMethod02606("light", "light");
    public static final FontFamily internalField1156 = FontFamily.internalMethod02606("roundbold", "round");
    public static final FontFamily internalField1537 = FontFamily.internalMethod06934();
    private static final String internalField0248 = " !?.,:;-\u2013\u2014()[]{}<>/\\|+*=%#@&\"'`~^_$0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ\u0430\u0431\u0432\u0433\u0434\u0435\u0451\u0436\u0437\u0438\u0439\u043a\u043b\u043c\u043d\u043e\u043f\u0440\u0441\u0442\u0443\u0444\u0445\u0446\u0447\u0448\u0449\u044a\u044b\u044c\u044d\u044e\u044f\u0410\u0411\u0412\u0413\u0414\u0415\u0401\u0416\u0417\u0418\u0419\u041a\u041b\u041c\u041d\u041e\u041f\u0420\u0421\u0422\u0423\u0424\u0425\u0426\u0427\u0428\u0429\u042a\u042b\u042c\u042d\u042e\u042f";

    public static void internalMethod05856() {
        Thread thread = new Thread(() -> {
            for (FontFamily typedValue022 : new FontFamily[]{internalField1154, internalField0449, internalField1157, internalField0450}) {
                typedValue022.internalMethod07043(internalField0248);
            }
        }, "rockstar-font-warmup");
        thread.setDaemon(true);
        thread.setPriority(1);
        thread.start();
    }

    public static void internalMethod05887() {
        internalField0450.internalMethod01612();
        internalField0449.internalMethod01612();
        internalField1154.internalMethod01612();
        internalField1157.internalMethod01612();
        internalField1155.internalMethod01612();
        internalField1156.internalMethod01612();
        Fonts.internalMethod05856();
    }

    public static FontFamily internalMethod00574(String string) {
        return switch (string.toLowerCase()) {
            case "noto" -> internalField1537;
            case "bold" -> internalField0450;
            case "medium" -> internalField0449;
            case "light" -> internalField1155;
            case "semibold" -> internalField1157;
            case "roundbold" -> internalField1156;
            default -> internalField1154;
        };
    }

    @Generated
    private Fonts() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

