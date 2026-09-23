package haron.events;

import java.util.UUID;

public class PlayerLeftEvent {
    private final String playerName;
    private final UUID playerUuid;

    public UUID playerUuid() {
        return this.playerUuid;
    }

    public String playerName() {
        return this.playerName;
    }

    public PlayerLeftEvent(String string, UUID uUID) {
        this.playerName = string;
        this.playerUuid = uUID;
    }

    public UUID b() {
        return this.playerUuid;
    }

    public String a() {
        int n = 52;
        return this.playerName;
    }
}

