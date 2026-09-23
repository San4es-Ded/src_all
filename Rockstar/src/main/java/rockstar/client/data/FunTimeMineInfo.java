package rockstar.client.data;


import rockstar.client.*;
public class FunTimeMineInfo {
    private String serverId;
    private String serverRuName;
    private String mineName;
    private String mineRarity;
    private String nextMineRarity;
    private long resetSecondsLeft;
    private long fetchTime;

    public String internalMethod06462() {
        return this.serverId;
    }

    public void internalMethod00454(String string) {
        this.serverId = string;
    }

    public String internalMethod02961() {
        return this.serverRuName;
    }

    public void internalMethod07021(String string) {
        this.serverRuName = string;
    }

    public String internalMethod08370() {
        return this.mineName;
    }

    public void internalMethod08754(String string) {
        this.mineName = string;
    }

    public String internalMethod09125() {
        return this.mineRarity;
    }

    public void internalMethod08439(String string) {
        this.mineRarity = string;
    }

    public String internalMethod08068() {
        return this.nextMineRarity;
    }

    public void internalMethod07665(String string) {
        this.nextMineRarity = string;
    }

    public long internalMethod05251() {
        return this.resetSecondsLeft;
    }

    public void internalMethod03774(long l) {
        this.resetSecondsLeft = l;
    }

    public long internalMethod05260() {
        return this.fetchTime;
    }

    public void internalMethod03825(long l) {
        this.fetchTime = l;
    }

    public long internalMethod08414() {
        long l = (System.currentTimeMillis() - this.fetchTime) / 1000L;
        return Math.max(0L, this.resetSecondsLeft - l);
    }

    public String toString() {
        return "FunTimeMine{serverId='" + this.serverId + "', serverRuName='" + this.serverRuName + "', mineName='" + this.mineName + "', mineRarity='" + this.mineRarity + "', nextMineRarity='" + this.nextMineRarity + "', secondsRemaining=" + this.internalMethod08414() + "}";
    }
}

