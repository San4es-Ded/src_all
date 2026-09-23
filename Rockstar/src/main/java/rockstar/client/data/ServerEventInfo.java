package rockstar.client.data;



import rockstar.client.event.*;
import rockstar.client.*;
public class ServerEventInfo {
    private String server;
    private String id;
    private String eventName;
    private String eventType;
    private String status;
    private String phase;
    private String loot;
    private long startTime;
    private long endTime;

    public String internalMethod07317() {
        return this.server;
    }

    public void internalMethod02308(String string) {
        this.server = string;
    }

    public String internalMethod03888() {
        return this.id;
    }

    public void internalMethod00822(String string) {
        this.id = string;
    }

    public String internalMethod08737() {
        return this.eventName;
    }

    public void internalMethod08974(String string) {
        this.eventName = string;
    }

    public String internalMethod08061() {
        return this.eventType;
    }

    public void internalMethod08687(String string) {
        this.eventType = string;
    }

    public String internalMethod08422() {
        return this.status;
    }

    public void internalMethod07871(String string) {
        this.status = string;
    }

    public String internalMethod07762() {
        return this.phase;
    }

    public void internalMethod09104(String string) {
        this.phase = string;
    }

    public String internalMethod09305() {
        return this.loot;
    }

    public void internalMethod09595(String string) {
        this.loot = string;
    }

    public long internalMethod02476() {
        return this.startTime;
    }

    public void internalMethod00630(long l) {
        this.startTime = l;
    }

    public long internalMethod02478() {
        return this.endTime;
    }

    public void internalMethod00690(long l) {
        this.endTime = l;
    }
}

