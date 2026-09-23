package rockstar.client.internal.game;


import rockstar.client.*;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import rockstar.client.MinecraftClientAccess;

public class ScoreboardBalanceTracker
implements MinecraftClientAccess {
    private static final Pattern internalField0293 = Pattern.compile("(\\d[\\d.,\\u00A0]*\\d|\\d)\\s*(kkkk|kkk|kk|k|\u043a\u043a\u043a|\u043a\u043a|\u043a|m|\u043c)?", 66);
    private long internalField0229 = -1L;

    public long internalMethod07055() {
        long l = this.internalMethod07058();
        if (l >= 0L) {
            this.internalField0229 = l;
        }
        return this.internalField0229;
    }

    public boolean internalMethod05965(long l) {
        long l2 = this.internalMethod07055();
        return l2 < 0L || l2 >= l;
    }

    public void internalMethod07056() {
        this.internalField0229 = -1L;
    }

    private long internalMethod07058() {
        if (ScoreboardBalanceTracker.internalField0149.world == null) {
            return -1L;
        }
        Scoreboard scoreboard = ScoreboardBalanceTracker.internalField0149.world.getScoreboard();
        ScoreboardObjective scoreboardObjective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
        if (scoreboardObjective == null) {
            return -1L;
        }
        long l = -1L;
        long l2 = -1L;
        for (ScoreboardEntry scoreboardEntry : scoreboard.getScoreboardEntries(scoreboardObjective)) {
            long l3;
            String string = scoreboardEntry.owner();
            Object object = scoreboardEntry.display() == null ? string : scoreboardEntry.display().getString();
            Team team = scoreboard.getScoreHolderTeam(string);
            if (team != null) {
                object = team.getPrefix().getString() + (String)object + team.getSuffix().getString();
            }
            if ((l3 = this.internalMethod04069((String)object)) < 0L) continue;
            l2 = Math.max(l2, l3);
            String string2 = ((String)object).toLowerCase(Locale.ROOT);
            if (!string2.contains("\u0431\u0430\u043b\u0430\u043d\u0441") && !string2.contains("\u043c\u043e\u043d\u0435\u0442") && !string2.contains("\u0434\u0435\u043d\u044c\u0433") && !string2.contains("balance") && !string2.contains("money") && !string2.contains("\u043a\u043e\u0438\u043d") && !string2.contains("$") && !string2.contains("\u26c1") && !string2.contains("bank")) continue;
            l = Math.max(l, l3);
        }
        return l >= 0L ? l : l2;
    }

    private long internalMethod04069(String string) {
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
                    case "kkkk" -> 1000000000000L;
                    default -> 1L;
                });
            }
            l = Math.max(l, l2);
        }
        return l;
    }
}

