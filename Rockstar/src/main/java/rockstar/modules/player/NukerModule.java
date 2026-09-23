package rockstar.modules.player;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.RaycastContext;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.server.ServerUtils;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;

@ModuleInfo(name="Nuker", category=ModuleCategory.PLAYER, internalMethod09633="modules.descriptions.nuker")
public class NukerModule
extends Module {
    private SliderSetting internalField0383;
    private BlockPos internalField0352 = null;
    private BlockPos internalField0351 = null;
    private final Stopwatch internalField0519 = new Stopwatch();
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if (NukerModule.internalField0149.player == null || NukerModule.internalField0149.world == null || NukerModule.internalField0149.interactionManager == null) {
            return;
        }
        NukerModule.internalField0149.options.attackKey.setPressed(false);
        this.internalField0352 = this.internalMethod04132();
        if (this.internalField0352 == null) {
            this.internalField0351 = null;
            return;
        }
        Rotation typedValue266 = this.internalMethod05833(this.internalField0352);
        RockstarClient.getInstance().internalMethod02368().internalMethod00418(typedValue266, RotationBehavior.internalField1003, 180.0f, 180.0f, 180.0f, RotationPriority.internalField0122);
        if (this.internalField0351 == null || !this.internalField0351.equals((Object)this.internalField0352)) {
            this.internalField0351 = this.internalField0352;
            this.internalField0519.internalMethod00701();
        }
        if (!this.internalField0519.internalMethod02365((long)this.internalField0383.internalMethod08576())) {
            return;
        }
        Rotation typedValue267 = RockstarClient.getInstance().internalMethod02368().internalMethod09074();
        if (typedValue267 != null && typedValue267.internalMethod00735(typedValue266) > 3.0f) {
            return;
        }
        Direction direction = this.internalMethod05181(this.internalField0352);
        NukerModule.internalField0149.interactionManager.updateBlockBreakingProgress(this.internalField0352, direction);
        NukerModule.internalField0149.player.swingHand(Hand.MAIN_HAND);
    };

    public NukerModule() {
        this.internalMethod09250();
    }

    private void internalMethod09250() {
        this.internalField0383 = new SliderSetting(this, "modules.settings.nuker.swap_delay").internalMethod08673(1.0f).internalMethod05900(0.0f).internalMethod02732(500.0f).internalMethod08074(5.0f);
    }

    private int internalMethod00600(BlockPos blockPos) {
        Block block = NukerModule.internalField0149.world.getBlockState(blockPos).getBlock();
        if (block == Blocks.ANCIENT_DEBRIS) {
            return 10;
        }
        if (block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE) {
            return 9;
        }
        if (block == Blocks.EMERALD_ORE || block == Blocks.DEEPSLATE_EMERALD_ORE) {
            return 8;
        }
        if (block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE || block == Blocks.NETHER_GOLD_ORE) {
            return 7;
        }
        if (block == Blocks.LAPIS_ORE || block == Blocks.DEEPSLATE_LAPIS_ORE) {
            return 6;
        }
        if (block == Blocks.REDSTONE_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE) {
            return 5;
        }
        if (block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE) {
            return 4;
        }
        if (block == Blocks.COPPER_ORE || block == Blocks.DEEPSLATE_COPPER_ORE) {
            return 3;
        }
        if (block == Blocks.COAL_ORE || block == Blocks.DEEPSLATE_COAL_ORE) {
            return 2;
        }
        if (block == Blocks.NETHER_QUARTZ_ORE) {
            return 1;
        }
        return 0;
    }

    private boolean internalMethod06403(Block block) {
        if (!ServerUtils.internalMethod08699()) {
            return true;
        }
        return block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE || block == Blocks.EMERALD_ORE || block == Blocks.DEEPSLATE_EMERALD_ORE || block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE || block == Blocks.NETHER_GOLD_ORE || block == Blocks.LAPIS_ORE || block == Blocks.DEEPSLATE_LAPIS_ORE || block == Blocks.REDSTONE_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE || block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE || block == Blocks.COPPER_ORE || block == Blocks.DEEPSLATE_COPPER_ORE || block == Blocks.COAL_ORE || block == Blocks.DEEPSLATE_COAL_ORE || block == Blocks.NETHER_QUARTZ_ORE || block == Blocks.ANCIENT_DEBRIS || block == Blocks.COBBLESTONE || block == Blocks.STONE || block == Blocks.GRANITE || block == Blocks.DIORITE || block == Blocks.ANDESITE || block == Blocks.DEEPSLATE || block == Blocks.COBBLED_DEEPSLATE;
    }

    private BlockPos internalMethod04132() {
        double d = NukerModule.internalField0149.player.getBlockInteractionRange();
        int n = (int)Math.ceil(d);
        Vec3d vec3d = NukerModule.internalField0149.player.getEyePos();
        BlockPos blockPos = NukerModule.internalField0149.player.getBlockPos();
        BlockPos blockPos2 = null;
        int n2 = -1;
        double d2 = Double.MAX_VALUE;
        for (int i = 0; i <= n; ++i) {
            for (int j = -n; j <= n; ++j) {
                for (int k = -n; k <= n; ++k) {
                    int n3;
                    double d3;
                    BlockPos blockPos3 = blockPos.add(j, i, k);
                    Block block = NukerModule.internalField0149.world.getBlockState(blockPos3).getBlock();
                    if (block == Blocks.AIR || block.getHardness() < 0.0f || !this.internalMethod06403(block) || (d3 = vec3d.squaredDistanceTo(Vec3d.ofCenter((Vec3i)blockPos3))) > d * d || !this.internalMethod07464(blockPos3, d) || (n3 = this.internalMethod00600(blockPos3)) <= n2 && (n3 != n2 || !(d3 < d2))) continue;
                    n2 = n3;
                    d2 = d3;
                    blockPos2 = blockPos3;
                }
            }
        }
        return blockPos2;
    }

    private boolean internalMethod07464(BlockPos blockPos, double d) {
        Vec3d vec3d = NukerModule.internalField0149.player.getEyePos();
        Vec3d vec3d2 = Vec3d.ofCenter((Vec3i)blockPos);
        double d2 = vec3d2.x - vec3d.x;
        double d3 = vec3d2.y - vec3d.y;
        double d4 = vec3d2.z - vec3d.z;
        double d5 = Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
        Vec3d vec3d3 = new Vec3d(d2 / d5, d3 / d5, d4 / d5);
        BlockHitResult blockHitResult = NukerModule.internalField0149.world.raycast(new RaycastContext(vec3d, vec3d.add(vec3d3.multiply(d + 0.5)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)NukerModule.internalField0149.player));
        if (blockHitResult == null || blockHitResult.getType() == HitResult.Type.MISS) {
            return false;
        }
        BlockPos blockPos2 = blockHitResult.getBlockPos();
        return blockPos2 != null && blockPos2.equals((Object)blockPos);
    }

    private Rotation internalMethod05833(BlockPos blockPos) {
        Vec3d vec3d = Vec3d.ofCenter((Vec3i)blockPos);
        Vec3d vec3d2 = NukerModule.internalField0149.player.getEyePos();
        double d = vec3d.x - vec3d2.x;
        double d2 = vec3d.y - vec3d2.y;
        double d3 = vec3d.z - vec3d2.z;
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)Math.toDegrees(Math.atan2(d3, d)) - 90.0f;
        float f2 = (float)(-Math.toDegrees(Math.atan2(d2, d4)));
        return new Rotation(f, f2);
    }

    private Direction internalMethod05181(BlockPos blockPos) {
        Vec3d vec3d = NukerModule.internalField0149.player.getEyePos();
        BlockHitResult blockHitResult = NukerModule.internalField0149.world.raycast(new RaycastContext(vec3d, Vec3d.ofCenter((Vec3i)blockPos), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)NukerModule.internalField0149.player));
        if (blockHitResult.getType() == HitResult.Type.BLOCK && blockHitResult.getBlockPos().equals((Object)blockPos)) {
            return blockHitResult.getSide();
        }
        Vec3d vec3d2 = Vec3d.ofCenter((Vec3i)blockPos);
        double d = vec3d.x - vec3d2.x;
        double d2 = vec3d.y - vec3d2.y;
        double d3 = vec3d.z - vec3d2.z;
        double d4 = Math.abs(d);
        double d5 = Math.abs(d2);
        double d6 = Math.abs(d3);
        if (d5 >= d4 && d5 >= d6) {
            return d2 >= 0.0 ? Direction.UP : Direction.DOWN;
        }
        if (d4 >= d6) {
            return d >= 0.0 ? Direction.EAST : Direction.WEST;
        }
        return d3 >= 0.0 ? Direction.SOUTH : Direction.NORTH;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        NukerModule.internalField0149.options.attackKey.setPressed(false);
        this.internalField0352 = null;
        this.internalField0351 = null;
    }
}
