package wtf.wyvern.core.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.math.BlockPos;
import wtf.wyvern.core.command.api.CommandAbstract;
import wtf.wyvern.core.command.impl.args.CoordinateArgumentType;
import wtf.wyvern.core.command.impl.args.PlayerArgumentType;
import wtf.wyvern.core.eventbus.EventManager;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.utility.game.other.MessageUtil;
import wtf.wyvern.utility.interfaces.IMinecraft;

public class TPCommand extends CommandAbstract {

    private static DashTask activeDash;

    public TPCommand() {
        super("tp");
    }

    @Override
    public void execute(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(arg("player", PlayerArgumentType.create()).executes(context -> {
            if (mc.player == null || mc.world == null) return 0;

            String targetName = context.getArgument("player", String.class);
            AbstractClientPlayerEntity target = findPlayer(targetName);

            if (target == null) {
                MessageUtil.displayError("Игрок " + targetName + " не найден");
                return 0;
            }

            double distance = mc.player.distanceTo(target);
            double targetX = target.getX();
            double targetZ = target.getZ();
            double targetY = findSolidBlockY(target);

            if (targetY == Double.MIN_VALUE) {
                MessageUtil.displayError("Не удалось найти твердый блок рядом с игроком " + targetName);
                return 0;
            }

            int packetsCount = Math.max((int) (distance / 1000), 3);

            for (int i = 0; i < packetsCount; i++) {
                mc.player.networkHandler.sendPacket(new OnGroundOnly(mc.player.isOnGround(), mc.player.horizontalCollision));
            }

            mc.player.networkHandler.sendPacket(new PositionAndOnGround(targetX, targetY, targetZ, false, mc.player.horizontalCollision));
            mc.player.setPosition(targetX, targetY, targetZ);

            MessageUtil.displayInfo(String.format("Телепортация к %s выполнена. Координаты: %.1f %.1f %.1f", targetName, targetX, targetY, targetZ));
            return 1;
        }));

        var zArg = arg("Z", CoordinateArgumentType.create()).executes(context -> {
            if (mc.player == null || mc.world == null) return 0;

            double x = context.getArgument("X", Double.class);
            double y = context.getArgument("Y", Double.class);
            double z = context.getArgument("Z", Double.class);

            startDash(x, y, z);
            return 1;
        });
        var yArg = arg("Y", CoordinateArgumentType.create()).then(zArg);
        var xArg = arg("X", CoordinateArgumentType.create()).then(yArg);
        builder.then(xArg);
    }

    private void startDash(double x, double y, double z) {
        if (activeDash != null) {
            activeDash.stop();
        }
        activeDash = new DashTask(x, y, z);
        activeDash.start();
        MessageUtil.displayInfo(String.format("Телепортация к %.1f %.1f %.1f началась", x, y, z));
    }

    private static final class DashTask implements IMinecraft {
        private static final double STEP = 8.0;

        private final double targetX;
        private final double targetY;
        private final double targetZ;

        private DashTask(double targetX, double targetY, double targetZ) {
            this.targetX = targetX;
            this.targetY = targetY;
            this.targetZ = targetZ;
        }

        void start() {
            EventManager.register(this);
        }

        void stop() {
            EventManager.unregister(this);
            if (activeDash == this) {
                activeDash = null;
            }
        }

        @EventTarget
        public void onUpdate(EventUpdate event) {
            if (mc.player == null || mc.world == null) {
                stop();
                return;
            }

            double dx = targetX - mc.player.getX();
            double dy = targetY - mc.player.getY();
            double dz = targetZ - mc.player.getZ();
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

            double nextX;
            double nextY;
            double nextZ;
            boolean last = distance <= STEP;

            if (last) {
                nextX = targetX;
                nextY = targetY;
                nextZ = targetZ;
            } else {
                double factor = STEP / distance;
                nextX = mc.player.getX() + dx * factor;
                nextY = mc.player.getY() + dy * factor;
                nextZ = mc.player.getZ() + dz * factor;
            }

            mc.player.networkHandler.sendPacket(new PositionAndOnGround(nextX, nextY, nextZ, false, mc.player.horizontalCollision));
            mc.player.setPosition(nextX, nextY, nextZ);

            if (last) {
                MessageUtil.displayInfo(String.format("Телепортация завершена: %.1f %.1f %.1f", targetX, targetY, targetZ));
                stop();
            }
        }
    }

    private double findSolidBlockY(AbstractClientPlayerEntity target) {
        BlockPos targetPos = target.getBlockPos();

        for (int y = targetPos.getY() - 1; y >= mc.world.getBottomY(); y--) {
            BlockPos pos = new BlockPos(targetPos.getX(), y, targetPos.getZ());
            if (!mc.world.getBlockState(pos).isAir()) {
                return y + 0.25;
            }
        }

        for (int y = targetPos.getY() + 1; y < 320; y++) {
            BlockPos pos = new BlockPos(targetPos.getX(), y, targetPos.getZ());
            if (!mc.world.getBlockState(pos).isAir()) {
                return y + 0.25;
            }
        }

        return Double.MIN_VALUE;
    }

    private AbstractClientPlayerEntity findPlayer(String name) {
        if (mc.world.getPlayers() == null) return null;

        for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
            if (player != null && player.getGameProfile() != null
                    && player.getGameProfile().getName().equalsIgnoreCase(name)
                    && player != mc.player) {
                return player;
            }
        }
        return null;
    }
}
