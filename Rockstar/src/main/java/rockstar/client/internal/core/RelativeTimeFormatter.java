package rockstar.client.internal.core;



import rockstar.client.i18n.*;
import rockstar.client.*;
import java.util.regex.Pattern;
import lombok.Generated;
import rockstar.client.i18n.LanguageManager;

public final class RelativeTimeFormatter {
    private static final Pattern internalField0293 = Pattern.compile("\\|");

    public static String internalMethod06419(long l) {
        long l2 = Math.max(0L, (System.currentTimeMillis() - l) / 1000L);
        if (l2 < 60L) {
            return LanguageManager.internalMethod07214("rocknet.time.just_now");
        }
        long l3 = l2 / 60L;
        if (l3 < 60L) {
            return RelativeTimeFormatter.internalMethod00901("rocknet.time.minutes", l3);
        }
        long l4 = l3 / 60L;
        if (l4 < 24L) {
            return RelativeTimeFormatter.internalMethod00901("rocknet.time.hours", l4);
        }
        long l5 = l4 / 24L;
        if (l5 < 7L) {
            return RelativeTimeFormatter.internalMethod00901("rocknet.time.days", l5);
        }
        if (l5 < 30L) {
            return RelativeTimeFormatter.internalMethod00901("rocknet.time.weeks", l5 / 7L);
        }
        if (l5 < 365L) {
            return RelativeTimeFormatter.internalMethod00901("rocknet.time.months", l5 / 30L);
        }
        return RelativeTimeFormatter.internalMethod00901("rocknet.time.years", l5 / 365L);
    }

    private static String internalMethod00901(String string, long l) {
        return LanguageManager.internalMethod00160("rocknet.time.ago", RelativeTimeFormatter.internalMethod06680(LanguageManager.internalMethod07214(string), l));
    }

    private static String internalMethod06680(String string, long l) {
        String[] stringArray = internalField0293.split(string);
        return String.format(stringArray[RelativeTimeFormatter.internalMethod00452(stringArray.length, l)], l);
    }

    private static int internalMethod00452(int n, long l) {
        if (n < 3) {
            return l == 1L ? 0 : n - 1;
        }
        long l2 = l % 10L;
        long l3 = l % 100L;
        if (l2 == 1L && l3 != 11L) {
            return 0;
        }
        if (l2 >= 2L && l2 <= 4L && (l3 < 10L || l3 >= 20L)) {
            return 1;
        }
        return 2;
    }

    @Generated
    private RelativeTimeFormatter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

