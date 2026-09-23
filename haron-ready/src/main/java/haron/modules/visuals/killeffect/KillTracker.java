package haron.modules.visuals.killeffect;

import haron.modules.visuals.killeffect.PendingKill;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public final class KillTracker {
    private static final int MAX_MEMORIES = 12;
    private final long maxMemoryMs;
    private final Consumer<LivingEntity> onTrigger;
    private final List<PendingKill> memories = new ArrayList<PendingKill>();

    public void tick() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        Iterator<PendingKill> iterator = this.memories.iterator();
        while (iterator.hasNext()) {
            PendingKill e1wzbg2 = iterator.next();
            if (!e1wzbg2.isActive()) {
                iterator.remove();
                continue;
            }
            if (!e1wzbg2.triggered && e1wzbg2.shouldTrigger(minecraftClient)) {
                this.onTrigger.accept(e1wzbg2.entity);
                e1wzbg2.triggered = true;
            }
            if (!e1wzbg2.triggered && e1wzbg2.isActive()) continue;
            iterator.remove();
        }
    }

    public void forget(int n) {
        this.memories.removeIf(e1wzbg2 -> {
            return e1wzbg2.entity.getId() == n;
        });
    }

    public void remember(LivingEntity livingEntity, boolean bl) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (livingEntity == minecraftClient.player || livingEntity.age < 2) {
            return;
        }
        for (PendingKill e1wzbg2 : this.memories) {
            if (!e1wzbg2.matches(livingEntity)) continue;
            e1wzbg2.refresh(this.maxMemoryMs);
            return;
        }
        if (!bl && !(livingEntity instanceof PlayerEntity)) {
            return;
        }
        if (this.memories.size() >= 12) {
            this.memories.remove(0);
        }
        this.memories.add(new PendingKill(livingEntity, this.maxMemoryMs));
    }

    public KillTracker(long l, Consumer<LivingEntity> consumer) {
        this.maxMemoryMs = l;
        this.onTrigger = consumer;
    }

    public void clear() {
        this.memories.clear();
    }
}

