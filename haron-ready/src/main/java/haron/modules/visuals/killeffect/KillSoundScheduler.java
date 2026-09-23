package haron.modules.visuals.killeffect;

import haron.audio.SoundPlayer;
import haron.modules.visuals.killeffect.ScheduledKillSound;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.sound.SoundEvent;

public final class KillSoundScheduler {
    private final List<ScheduledKillSound> pendingSounds = new ArrayList<ScheduledKillSound>();

    public void tick() {
        long l = System.currentTimeMillis();
        Iterator<ScheduledKillSound> iterator = this.pendingSounds.iterator();
        while (iterator.hasNext()) {
            ScheduledKillSound epjnhz2 = iterator.next();
            if (l < epjnhz2.playAt()) continue;
            SoundPlayer.playRandomHit(epjnhz2.volume());
            iterator.remove();
        }
    }

    public void clear() {
        this.pendingSounds.clear();
    }

    public void schedule(SoundEvent soundEvent, float f, long l) {
        if (soundEvent == null) {
            return;
        }
        if (l <= 0L) {
            SoundPlayer.playRandomHit(f);
            return;
        }
        this.pendingSounds.add(new ScheduledKillSound(f, System.currentTimeMillis() + l));
    }
}
