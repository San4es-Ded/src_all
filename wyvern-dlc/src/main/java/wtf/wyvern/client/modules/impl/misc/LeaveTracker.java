package wtf.wyvern.client.modules.impl.misc;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventUpdate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@ModuleAnnotation(
        name = "LeaveTracker",
        category = Category.MISC,
        description = "Показывает координаты игроков, вышедших из прогрузки"
)
public final class LeaveTracker extends Module {
    public static final LeaveTracker INSTANCE = new LeaveTracker();

    private final Map<UUID, TrackedPlayer> trackedPlayers = new HashMap<>();
    private ClientWorld lastWorld;
    private boolean initialized;

    private LeaveTracker() {
    }

    @Override
    public void onDisable() {
        trackedPlayers.clear();
        lastWorld = null;
        initialized = false;
        super.onDisable();
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc.player == null || mc.world == null) return;

        if (mc.world != lastWorld) {
            lastWorld = mc.world;
            trackedPlayers.clear();
            initialized = false;
        }

        if (!initialized) {
            snapshotPlayers();
            initialized = true;
            return;
        }

        Set<UUID> seenPlayers = new HashSet<>();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || !player.isAlive()) continue;

            UUID uuid = player.getUuid();
            seenPlayers.add(uuid);
            trackedPlayers.put(uuid, new TrackedPlayer(
                    player.getName().getString(),
                    player.getBlockPos()
            ));
        }

        Iterator<Map.Entry<UUID, TrackedPlayer>> iterator =
                trackedPlayers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, TrackedPlayer> entry = iterator.next();
            if (seenPlayers.contains(entry.getKey())) continue;

            TrackedPlayer tracked = entry.getValue();
            BlockPos position = tracked.position();
            double distanceSquared = mc.player.squaredDistanceTo(
                    position.getX(), position.getY(), position.getZ()
            );
            if (distanceSquared < 65536.0) {
                Text message = Text.literal(tracked.name()).formatted(Formatting.GRAY)
                        .append(Text.literal(" ливнул на ").formatted(Formatting.WHITE))
                        .append(Text.literal(
                                position.getX() + " " + position.getY() + " " + position.getZ()
                        ).formatted(Formatting.GRAY));
                mc.player.sendMessage(message, false);
            }
            iterator.remove();
        }
    }

    private void snapshotPlayers() {
        trackedPlayers.clear();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || !player.isAlive()) continue;
            trackedPlayers.put(
                    player.getUuid(),
                    new TrackedPlayer(player.getName().getString(), player.getBlockPos())
            );
        }
    }

    private record TrackedPlayer(String name, BlockPos position) {
    }
}
