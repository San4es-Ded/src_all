package haron.player;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;

public class PlayerNameCache
implements ThreadFactory {
    private final Set<String> names = ConcurrentHashMap.newKeySet();

    public long e() {
        return 0L;
    }

    public void b(Collection<String> collection) {
        this.names.clear();
        if (collection != null) {
            this.names.addAll(collection);
        }
    }

    public void b() {
    }

    public void c() {
    }

    public Set<String> f() {
        return Collections.unmodifiableSet(this.names);
    }

    public int d() {
        return 0;
    }

    public void a(long l, boolean bl) {
    }

    public void a() {
    }

    public void a(Collection<PlayerListEntry> collection) {
    }

    public void a(int n, long l) {
    }

    public void a(PlayerListS2CPacket playerListS2CPacket) {
    }

    public void a(boolean bl, boolean bl2) {
    }

    public void g() {
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, "pulse-player-list");
        thread.setDaemon(true);
        return thread;
    }
}

