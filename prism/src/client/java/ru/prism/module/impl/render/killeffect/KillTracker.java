package ru.prism.module.impl.render.killeffect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public final class KillTracker {

    private static final int MAX_MEMORIES = 12;

    private final long memoryMs;
    private final Consumer<PendingKill> onKill;
    private final List<PendingKill> memories = new ArrayList<>();

    public KillTracker(long memoryMs, Consumer<PendingKill> onKill) {
        this.memoryMs = memoryMs;
        this.onKill = onKill;
    }

    public void remember(LivingEntity entity) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (entity == null || entity == mc.player || entity.age < 2) return;

        for (PendingKill pending : memories) {
            if (pending.matches(entity)) {
                pending.refresh(memoryMs);
                return;
            }
        }

        if (memories.size() >= MAX_MEMORIES) {
            memories.remove(0);
        }
        memories.add(new PendingKill(entity, memoryMs));
    }

    public void tick() {
        Iterator<PendingKill> iterator = memories.iterator();
        while (iterator.hasNext()) {
            PendingKill pending = iterator.next();

            if (!pending.isActive()) {
                iterator.remove();
                continue;
            }

            if (!pending.triggered && pending.shouldTrigger()) {
                onKill.accept(pending);
                iterator.remove();
                continue;
            }

            pending.update();
        }
    }

    public boolean triggerNow(LivingEntity entity) {
        if (entity == null) return false;

        Iterator<PendingKill> iterator = memories.iterator();
        while (iterator.hasNext()) {
            PendingKill pending = iterator.next();
            if (pending.matches(entity)) {
                if (!pending.triggered) {
                    onKill.accept(pending);
                }
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public void clear() {
        memories.clear();
    }
}
