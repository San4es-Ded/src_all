package haron.player;

import haron.client.MinecraftClientAccess;
import haron.player.PvpStatus;
import java.util.regex.Pattern;

public final class lrsc12
implements MinecraftClientAccess {
    private static final Pattern a = Pattern.compile("(?i)(?:pvp|пвп)");
    private static final Pattern b = Pattern.compile("(\\d+)");

    private lrsc12() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static PvpStatus a() {
        return new PvpStatus(true, 0);
    }
}

