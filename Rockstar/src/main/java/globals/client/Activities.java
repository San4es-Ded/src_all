package globals.client;


import rockstar.client.i18n.*;
import rockstar.client.i18n.LanguageManager;

public final class Activities {
    public static final String MAIN_MENU = "main_menu";
    public static final String SINGLEPLAYER = "singleplayer";
    public static final String MULTIPLAYER = "multiplayer";
    public static final String OFFLINE = "offline";
    public static final String WEBSITE = "website";
    private Activities() {
    }

    public static String multiplayer(String string) {
        return string == null || string.isBlank() ? MULTIPLAYER : "multiplayer|" + string;
    }

    public static boolean offline(String string) {
        return Activities.is(string, OFFLINE, "rocknet.status.offline.id");
    }

    public static boolean mainMenu(String string) {
        return Activities.is(string, MAIN_MENU, "rocknet.status.main_menu.id");
    }

    public static boolean website(String string) {
        return Activities.is(string, WEBSITE, "rocknet.status.website.id");
    }

    public static boolean singleplayer(String string) {
        return Activities.is(string, SINGLEPLAYER, "rocknet.activity.singleplayer");
    }

    public static boolean onServer(String string) {
        return !Activities.offline(string) && !Activities.mainMenu(string) && !Activities.website(string) && !Activities.singleplayer(string);
    }

    public static String translate(String string) {
        if (string == null || string.isBlank()) {
            return "";
        }
        int n = string.indexOf(124);
        String string2 = n < 0 ? string : string.substring(0, n);
        String string3 = n < 0 ? "" : string.substring(n + 1);
        return switch (string2) {
            case MAIN_MENU -> LanguageManager.internalMethod07214("rocknet.activity.main_menu");
            case SINGLEPLAYER -> LanguageManager.internalMethod07214("rocknet.activity.singleplayer");
            case MULTIPLAYER -> {
                if (string3.isBlank()) {
                    yield LanguageManager.internalMethod07214("rocknet.activity.online");
                }
                yield LanguageManager.internalMethod00160("rocknet.activity.multiplayer", string3);
            }
            case OFFLINE -> LanguageManager.internalMethod07214("rocknet.status.offline");
            case WEBSITE -> LanguageManager.internalMethod07214("rocknet.status.website");
            default -> Activities.legacy(string);
        };
    }

    private static String legacy(String string) {
        if (string.equals(LanguageManager.internalMethod07214("rocknet.status.offline.id"))) {
            return LanguageManager.internalMethod07214("rocknet.status.offline");
        }
        if (string.equals(LanguageManager.internalMethod07214("rocknet.status.main_menu.id"))) {
            return LanguageManager.internalMethod07214("rocknet.status.main_menu");
        }
        if (string.equals(LanguageManager.internalMethod07214("rocknet.status.website.id"))) {
            return LanguageManager.internalMethod07214("rocknet.status.website");
        }
        return string;
    }

    private static boolean is(String string, String string2, String string3) {
        return string != null && (string.equals(string2) || string.equals(LanguageManager.internalMethod07214(string3)));
    }
}

