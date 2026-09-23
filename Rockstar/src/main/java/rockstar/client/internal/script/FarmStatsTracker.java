package rockstar.client.internal.script;






import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import pyrock.events.game.BlockBreakEvent;
import pyrock.events.network.ReceivePacketEvent;
import rockstar.client.internal.inventory.AutoFarmModeState;
import rockstar.client.internal.core.FarmStatsMetric;
import rockstar.client.internal.core.AutoFarmUnit;
import rockstar.client.event.EventListener;
import rockstar.client.internal.game.ScoreboardBalanceTracker;
import rockstar.client.MinecraftClientAccess;

public class FarmStatsTracker
implements MinecraftClientAccess {
    public static final int internalField0227 = 36;
    private static final String internalField0248 = "\u0423 \u0412\u0430\u0441 \u043a\u0443\u043f\u0438\u043b\u0438";
    private static final Pattern internalField0293 = Pattern.compile("(\\d[\\d.,\\u00A0]*\\d|\\d)\\s*(kkk|kk|k|\u043a\u043a\u043a|\u043a\u043a|\u043a|m|\u043c)?", 66);
    private final ScoreboardBalanceTracker internalField0801 = new ScoreboardBalanceTracker();
    private Supplier<AutoFarmModeState> internalField0017;
    private final long[] internalField0620 = new long[36];
    private final long[] internalField0619 = new long[36];
    private boolean internalField0277;
    private long internalField1059;
    private long internalField1058;
    private long internalField1060;
    private long internalField1057;
    private long internalField1473;
    private long internalField1477 = -1L;
    private long internalField1471 = -1L;
    private long internalField1472;
    private int internalField0228;
    private int internalField1053;
    private long internalField1474;
    private final EventListener<BlockBreakEvent> internalField0157 = blockBreakEvent -> {
        if (!this.internalField0277) {
            return;
        }
        AutoFarmModeState typedValue313 = this.internalField0017.get();
        if (typedValue313 == null || typedValue313.internalMethod02317() != AutoFarmUnit.internalField0853) {
            return;
        }
        this.internalMethod08501();
    };
    private final EventListener<ReceivePacketEvent> internalField0158 = receivePacketEvent -> {
        Object object;
        if (!this.internalField0277 || !((object = receivePacketEvent.getPacket()) instanceof GameMessageS2CPacket)) {
            return;
        }
        GameMessageS2CPacket gameMessageS2CPacket = (GameMessageS2CPacket)object;
        object = gameMessageS2CPacket.content().getString();
        if (!((String)object).contains(internalField0248)) {
            return;
        }
        long l = FarmStatsTracker.internalMethod07195((String)object);
        if (l > 0L) {
            this.internalField1473 += l;
        }
    };

    public FarmStatsTracker(Supplier<AutoFarmModeState> supplier) {
        this.internalField0017 = supplier;
    }

    public void internalMethod01589() {
        this.internalField0277 = true;
        this.internalField1059 = System.currentTimeMillis();
        this.internalField1058 = 0L;
        this.internalField1060 = 0L;
        this.internalField1057 = 0L;
        this.internalField1473 = 0L;
        this.internalField1477 = -1L;
        this.internalField1471 = -1L;
        this.internalField1472 = 0L;
        this.internalField0801.internalMethod07056();
        Arrays.fill(this.internalField0620, 0L);
        Arrays.fill(this.internalField0619, 0L);
        this.internalField0228 = 0;
        this.internalField1053 = 1;
        this.internalField1474 = this.internalField1059;
    }

    public void internalMethod01595() {
        this.internalField0277 = false;
        this.internalField1058 = System.currentTimeMillis();
    }

    public void internalMethod08497() {
        if (!this.internalField0277) {
            return;
        }
        this.internalMethod08509();
        this.internalMethod08511();
    }

    public void internalMethod08501() {
        this.internalMethod04318(1);
    }

    public void internalMethod04318(int n) {
        if (!this.internalField0277 || n <= 0) {
            return;
        }
        this.internalField1060 += (long)n;
        int n2 = this.internalField0228;
        this.internalField0620[n2] = this.internalField0620[n2] + (long)n;
    }

    public long internalMethod01588() {
        if (this.internalField1059 == 0L) {
            return 0L;
        }
        return (this.internalField0277 ? System.currentTimeMillis() : this.internalField1058) - this.internalField1059;
    }

    public double internalMethod01585() {
        return this.internalMethod04317(this.internalField1060);
    }

    public double internalMethod01592() {
        return this.internalMethod04317(this.internalField1057);
    }

    private double internalMethod04317(double d) {
        long l = this.internalMethod01588();
        if (l < 1000L) {
            return 0.0;
        }
        return d / ((double)l / 3600000.0);
    }

    public long[] internalMethod02394(FarmStatsMetric typedValue315) {
        long[] lArray = typedValue315 == FarmStatsMetric.internalField0851 ? this.internalField0619 : this.internalField0620;
        int n = Math.max(0, Math.min(this.internalField1053, 36) - 1);
        long[] lArray2 = new long[n];
        for (int i = 0; i < n; ++i) {
            int n2 = Math.floorMod(this.internalField0228 - n + i, 36);
            lArray2[i] = lArray[n2];
        }
        return lArray2;
    }

    public static float internalMethod01586() {
        return 5.0f;
    }

    private void internalMethod08509() {
        long l = System.currentTimeMillis();
        while (l - this.internalField1474 >= 5000L) {
            this.internalField1474 += 5000L;
            this.internalField0228 = (this.internalField0228 + 1) % 36;
            this.internalField0620[this.internalField0228] = 0L;
            this.internalField0619[this.internalField0228] = 0L;
            if (this.internalField1053 >= 36) continue;
            ++this.internalField1053;
        }
    }

    private void internalMethod08511() {
        long l = System.currentTimeMillis();
        if (l - this.internalField1472 < 1000L) {
            return;
        }
        this.internalField1472 = l;
        long l2 = this.internalField0801.internalMethod07055();
        if (l2 < 0L) {
            return;
        }
        if (this.internalField1477 < 0L) {
            this.internalField1477 = l2;
            this.internalField1471 = l2;
            return;
        }
        long l3 = l2 - this.internalField1471;
        this.internalField1471 = l2;
        this.internalField1057 = l2 - this.internalField1477;
        if (l3 != 0L) {
            int n = this.internalField0228;
            this.internalField0619[n] = this.internalField0619[n] + l3;
        }
    }

    private static long internalMethod07195(String string) {
        Matcher matcher = internalField0293.matcher(string);
        long l = -1L;
        while (matcher.find()) {
            long l2;
            String string2 = matcher.group(1).replaceAll("[^\\d]", "");
            if (string2.isEmpty()) continue;
            try {
                l2 = Long.parseLong(string2);
            }
            catch (NumberFormatException numberFormatException) {
                continue;
            }
            String string3 = matcher.group(2);
            if (string3 != null) {
                l2 *= (switch (string3.toLowerCase(Locale.ROOT)) {
                    case "k", "\u043a" -> 1000L;
                    case "kk", "\u043a\u043a", "m", "\u043c" -> 1000000L;
                    case "kkk", "\u043a\u043a\u043a" -> 1000000000L;
                    default -> 1L;
                });
            }
            l = Math.max(l, l2);
        }
        return l;
    }

    public static String internalMethod03244(long l) {
        String string = l < 0L ? "-" : "+";
        return string + FarmStatsTracker.internalMethod03854(Math.abs(l));
    }

    public static String internalMethod03854(double d) {
        double d2 = Math.abs(d);
        if (d2 >= 1.0E9) {
            return FarmStatsTracker.internalMethod06426(d / 1.0E9) + "kkk";
        }
        if (d2 >= 1000000.0) {
            return FarmStatsTracker.internalMethod06426(d / 1000000.0) + "kk";
        }
        if (d2 >= 1000.0) {
            return FarmStatsTracker.internalMethod06426(d / 1000.0) + "k";
        }
        return String.valueOf(Math.round(d));
    }

    private static String internalMethod06426(double d) {
        String string = String.format(Locale.ROOT, "%.1f", d);
        return string.endsWith(".0") ? string.substring(0, string.length() - 2) : string;
    }

    public static String internalMethod01822(long l) {
        long l2 = Math.max(0L, l) / 1000L;
        long l3 = l2 / 3600L;
        long l4 = l2 % 3600L / 60L;
        long l5 = l2 % 60L;
        return l3 > 0L ? String.format(Locale.ROOT, "%d:%02d:%02d", l3, l4, l5) : String.format(Locale.ROOT, "%d:%02d", l4, l5);
    }

    @Generated
    public ScoreboardBalanceTracker internalMethod05647() {
        return this.internalField0801;
    }

    @Generated
    public Supplier<AutoFarmModeState> internalMethod01840() {
        return this.internalField0017;
    }

    @Generated
    public long[] internalMethod04316() {
        return this.internalField0620;
    }

    @Generated
    public long[] internalMethod04371() {
        return this.internalField0619;
    }

    @Generated
    public boolean internalMethod01590() {
        return this.internalField0277;
    }

    @Generated
    public long internalMethod01594() {
        return this.internalField1059;
    }

    @Generated
    public long internalMethod08496() {
        return this.internalField1058;
    }

    @Generated
    public long internalMethod08500() {
        return this.internalField1060;
    }

    @Generated
    public long internalMethod08508() {
        return this.internalField1057;
    }

    @Generated
    public long internalMethod08510() {
        return this.internalField1473;
    }

    @Generated
    public long internalMethod09248() {
        return this.internalField1477;
    }

    @Generated
    public long internalMethod09249() {
        return this.internalField1471;
    }

    @Generated
    public long internalMethod09258() {
        return this.internalField1472;
    }

    @Generated
    public int internalMethod01587() {
        return this.internalField0228;
    }

    @Generated
    public int internalMethod01593() {
        return this.internalField1053;
    }

    @Generated
    public long internalMethod09259() {
        return this.internalField1474;
    }

    @Generated
    public EventListener<BlockBreakEvent> internalMethod00592() {
        return this.internalField0157;
    }

    @Generated
    public EventListener<ReceivePacketEvent> internalMethod01930() {
        return this.internalField0158;
    }
}

