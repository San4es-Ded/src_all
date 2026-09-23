package wtf.wyvern.utility.game.combat;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.explosion.Explosion;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.wyvern.utility.predict.PredictUtils;
import wtf.astroguard.J2C.FastNative;

/**
 * Перенесено из ru.clouse.infrastructure.player.combat.ExplosionUtility.
 * Расчёт урона от взрыва кристалла (в т.ч. предикт позиции цели и ghost-блоков
 * обсидиана). MC 1.21.4 API совпадает с оригиналом — заменён только фреймворк
 * (IMinecraft из wyvern и предикт через {@link PredictUtils}).
 */
public final class ExplosionUtility implements IMinecraft {
    private static final ClientExplosion CLIENT_EXPLOSION = new ClientExplosion();

    private ExplosionUtility() {
    }

    @FastNative
    public static float getAutoCrystalDamage(Vec3d crystalPos, PlayerEntity target, int predictTicks, boolean optimized, boolean ignoreTerrain, boolean assumeBestArmor) {
        if (predictTicks == 0) {
            return getExplosionDamage(crystalPos, target, optimized, ignoreTerrain, assumeBestArmor);
        }
        return getExplosionDamageWPredict(crystalPos, target, predictBox(target, predictTicks), optimized, ignoreTerrain, assumeBestArmor);
    }

    @FastNative
    public static float getExplosionDamagePredict(Vec3d explosionPos, PlayerEntity target, int predictTicks, boolean optimized, boolean ignoreTerrain, boolean assumeBestArmor) {
        if (predictTicks <= 0) {
            return getExplosionDamage(explosionPos, target, optimized, ignoreTerrain, assumeBestArmor);
        }
        Box predictedBox = predictBox(target, predictTicks);
        if (predictedBox == null) {
            return getExplosionDamage(explosionPos, target, optimized, ignoreTerrain, assumeBestArmor);
        }
        return getExplosionDamageWPredict(explosionPos, target, predictedBox, optimized, ignoreTerrain, assumeBestArmor);
    }

    @FastNative
    public static float getDamageOfGhostBlockPredict(Vec3d explosionPos, PlayerEntity target, BlockPos bp, int predictTicks, boolean ignoreTerrain, boolean assumeBestArmor) {
        if (predictTicks <= 0) {
            return getDamageOfGhostBlock(explosionPos, target, bp, ignoreTerrain, assumeBestArmor);
        }
        Box predictedBox = predictBox(target, predictTicks);
        if (predictedBox == null) {
            return getDamageOfGhostBlock(explosionPos, target, bp, ignoreTerrain, assumeBestArmor);
        }
        return getDamageOfGhostBlockWPredict(explosionPos, target, bp, predictedBox, ignoreTerrain, assumeBestArmor);
    }

    @FastNative
    public static float getSelfExplosionDamage(Vec3d explosionPos, int predictTicks, boolean optimized, boolean ignoreTerrain, boolean assumeBestArmor) {
        return getAutoCrystalDamage(explosionPos, mc.player, predictTicks, optimized, ignoreTerrain, assumeBestArmor);
    }

    @FastNative
    public static float getExplosionDamage(Vec3d explosionPos, PlayerEntity target, boolean optimized, boolean ignoreTerrain, boolean assumeBestArmor) {
        if (mc.world == null || mc.player == null || target == null || mc.world.getDifficulty() == Difficulty.PEACEFUL) {
            return 0f;
        }

        DamageSource damageSource = Explosion.createDamageSource(mc.world, mc.player);
        CLIENT_EXPLOSION.update(explosionPos, mc.player, damageSource);

        if (!new Box(
                MathHelper.floor(explosionPos.x - 11),
                MathHelper.floor(explosionPos.y - 11),
                MathHelper.floor(explosionPos.z - 11),
                MathHelper.floor(explosionPos.x + 13),
                MathHelper.floor(explosionPos.y + 13),
                MathHelper.floor(explosionPos.z + 13)
        ).intersects(target.getBoundingBox())) {
            return 0f;
        }

        if (target.isImmuneToExplosion(CLIENT_EXPLOSION) || target.isInvulnerable()) {
            return 0f;
        }

        double distExposure = target.squaredDistanceTo(explosionPos) / 144.0;
        if (distExposure > 1.0) return 0f;

        double exposure = getExposure(explosionPos, target.getBoundingBox(), optimized, ignoreTerrain);
        return applyExplosionDamage(target, distExposure, exposure, damageSource, assumeBestArmor);
    }

    @FastNative
    public static float getExplosionDamageWPredict(Vec3d explosionPos, PlayerEntity target, Box predict, boolean optimized, boolean ignoreTerrain, boolean assumeBestArmor) {
        if (mc.world == null || mc.player == null || target == null || predict == null || mc.world.getDifficulty() == Difficulty.PEACEFUL) {
            return 0f;
        }

        DamageSource damageSource = Explosion.createDamageSource(mc.world, mc.player);
        CLIENT_EXPLOSION.update(explosionPos, mc.player, damageSource);

        if (!new Box(
                MathHelper.floor(explosionPos.x - 11d),
                MathHelper.floor(explosionPos.y - 11d),
                MathHelper.floor(explosionPos.z - 11d),
                MathHelper.floor(explosionPos.x + 13d),
                MathHelper.floor(explosionPos.y + 13d),
                MathHelper.floor(explosionPos.z + 13d)
        ).intersects(predict)) {
            return 0f;
        }

        if (target.isImmuneToExplosion(CLIENT_EXPLOSION) || target.isInvulnerable()) {
            return 0f;
        }

        double distExposure = predict.getCenter().add(0, -0.9, 0).squaredDistanceTo(explosionPos) / 144.0;
        if (distExposure > 1.0) return 0f;

        double exposure = getExposure(explosionPos, predict, optimized, ignoreTerrain);
        return applyExplosionDamage(target, distExposure, exposure, damageSource, assumeBestArmor);
    }

    public static BlockHitResult rayCastBlock(RaycastContext context, BlockPos block) {
        return BlockView.raycast(context.getStart(), context.getEnd(), context, (raycastContext, blockPos) -> {
            BlockState blockState = blockPos.equals(block) ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState();

            Vec3d vec3d = raycastContext.getStart();
            Vec3d vec3d2 = raycastContext.getEnd();
            VoxelShape voxelShape = raycastContext.getBlockShape(blockState, mc.world, blockPos);
            BlockHitResult blockHitResult = mc.world.raycastBlock(vec3d, vec3d2, blockPos, voxelShape, blockState);
            VoxelShape voxelShape2 = VoxelShapes.empty();
            BlockHitResult blockHitResult2 = voxelShape2.raycast(vec3d, vec3d2, blockPos);

            double d = blockHitResult == null ? Double.MAX_VALUE : raycastContext.getStart().squaredDistanceTo(blockHitResult.getPos());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : raycastContext.getStart().squaredDistanceTo(blockHitResult2.getPos());

            return d <= e ? blockHitResult : blockHitResult2;
        }, (raycastContext) -> {
            Vec3d vec3d = raycastContext.getStart().subtract(raycastContext.getEnd());
            return BlockHitResult.createMissed(raycastContext.getEnd(), Direction.getFacing(vec3d.x, vec3d.y, vec3d.z), BlockPos.ofFloored(raycastContext.getEnd()));
        });
    }

    @FastNative
    public static float getDamageOfGhostBlock(Vec3d explosionPos, PlayerEntity target, BlockPos bp, boolean ignoreTerrain, boolean assumeBestArmor) {
        if (mc.world == null || mc.player == null || target == null || bp == null || mc.world.getDifficulty() == Difficulty.PEACEFUL) {
            return 0f;
        }

        DamageSource damageSource = Explosion.createDamageSource(mc.world, mc.player);
        CLIENT_EXPLOSION.update(explosionPos, mc.player, damageSource);

        double maxDist = 12;
        if (!new Box(
                MathHelper.floor(explosionPos.x - maxDist - 1.0),
                MathHelper.floor(explosionPos.y - maxDist - 1.0),
                MathHelper.floor(explosionPos.z - maxDist - 1.0),
                MathHelper.floor(explosionPos.x + maxDist + 1.0),
                MathHelper.floor(explosionPos.y + maxDist + 1.0),
                MathHelper.floor(explosionPos.z + maxDist + 1.0)
        ).intersects(target.getBoundingBox())) {
            return 0f;
        }

        if (target.isImmuneToExplosion(CLIENT_EXPLOSION) || target.isInvulnerable()) {
            return 0f;
        }

        double distExposure = target.squaredDistanceTo(explosionPos) / 144.0;
        if (distExposure > 1.0) return 0f;

        double exposure = getExposureGhost(explosionPos, target, bp, ignoreTerrain);
        return applyExplosionDamage(target, distExposure, exposure, damageSource, assumeBestArmor);
    }

    @FastNative
    public static float getDamageOfGhostBlockWPredict(Vec3d explosionPos, PlayerEntity target, BlockPos bp, Box predictedBox, boolean ignoreTerrain, boolean assumeBestArmor) {
        if (mc.world == null || mc.player == null || target == null || bp == null || predictedBox == null || mc.world.getDifficulty() == Difficulty.PEACEFUL) {
            return 0f;
        }

        DamageSource damageSource = Explosion.createDamageSource(mc.world, mc.player);
        CLIENT_EXPLOSION.update(explosionPos, mc.player, damageSource);

        double maxDist = 12;
        if (!new Box(
                MathHelper.floor(explosionPos.x - maxDist - 1.0),
                MathHelper.floor(explosionPos.y - maxDist - 1.0),
                MathHelper.floor(explosionPos.z - maxDist - 1.0),
                MathHelper.floor(explosionPos.x + maxDist + 1.0),
                MathHelper.floor(explosionPos.y + maxDist + 1.0),
                MathHelper.floor(explosionPos.z + maxDist + 1.0)
        ).intersects(predictedBox)) {
            return 0f;
        }

        if (target.isImmuneToExplosion(CLIENT_EXPLOSION) || target.isInvulnerable()) {
            return 0f;
        }

        double distExposure = predictedBox.getCenter().add(0, -0.9, 0).squaredDistanceTo(explosionPos) / 144.0;
        if (distExposure > 1.0) return 0f;

        double exposure = getExposureGhostPredict(explosionPos, predictedBox, bp, ignoreTerrain);
        return applyExplosionDamage(target, distExposure, exposure, damageSource, assumeBestArmor);
    }

    @FastNative
    private static float applyExplosionDamage(PlayerEntity target, double distExposure, double exposure, DamageSource damageSource, boolean assumeBestArmor) {
        double finalExposure = (1.0 - distExposure) * exposure;

        float toDamage = (float) Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * 12.0 + 1.0);

        if (mc.world.getDifficulty() == Difficulty.EASY) {
            toDamage = Math.min(toDamage / 2f + 1f, toDamage);
        } else if (mc.world.getDifficulty() == Difficulty.HARD) {
            toDamage = toDamage * 3f / 2f;
        }

        toDamage = DamageUtil.getDamageLeft(
                target,
                toDamage,
                damageSource,
                target.getArmor(),
                (float) target.getAttributeInstance(EntityAttributes.ARMOR_TOUGHNESS).getValue()
        );

        if (target.hasStatusEffect(StatusEffects.RESISTANCE)) {
            int resistance = 25 - (target.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1) * 5;
            float resistancePart = toDamage * resistance;
            toDamage = Math.max(resistancePart / 25f, 0f);
        }

        if (toDamage <= 0f) {
            return 0f;
        }

        float protAmount = assumeBestArmor ? 32f : getProtectionAmount(target.getArmorItems());
        if (protAmount > 0f) {
            toDamage = DamageUtil.getInflictedDamage(toDamage, protAmount);
        }

        return Math.max(toDamage, 0f);
    }

    @FastNative
    private static float getExposureGhost(Vec3d source, Entity entity, BlockPos pos, boolean ignoreTerrain) {
        Box box = entity.getBoundingBox();
        return calcExposureGhost(source, box, pos, ignoreTerrain, entity);
    }

    @FastNative
    private static float getExposureGhostPredict(Vec3d source, Box box, BlockPos pos, boolean ignoreTerrain) {
        return calcExposureGhost(source, box, pos, ignoreTerrain, mc.player);
    }

    @FastNative
    private static float calcExposureGhost(Vec3d source, Box box, BlockPos pos, boolean ignoreTerrain, Entity entity) {
        double d = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
        double e = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
        double f = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);
        double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;

        if (d < 0.0 || e < 0.0 || f < 0.0) {
            return 0.0f;
        }

        int i = 0;
        int j = 0;

        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    double n = MathHelper.lerp(k, box.minX, box.maxX);
                    double o = MathHelper.lerp(l, box.minY, box.maxY);
                    double p = MathHelper.lerp(m, box.minZ, box.maxZ);
                    Vec3d vec3d = new Vec3d(n + g, o, p + h);
                    if (raycastGhost(new RaycastContext(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity), pos, ignoreTerrain).getType() == HitResult.Type.MISS) {
                        ++i;
                    }
                    ++j;
                }
            }
        }

        return (float) i / (float) j;
    }

    @FastNative
    public static float getExposure(Vec3d source, Box box, boolean optimized, boolean ignoreTerrain) {
        if (!optimized) return getExposure(source, box, ignoreTerrain);

        int miss = 0;
        int hit = 0;

        for (int k = 0; k <= 1; k += 1) {
            for (int l = 0; l <= 1; l += 1) {
                for (int m = 0; m <= 1; m += 1) {
                    double n = MathHelper.lerp(k, box.minX, box.maxX);
                    double o = MathHelper.lerp(l, box.minY, box.maxY);
                    double p = MathHelper.lerp(m, box.minZ, box.maxZ);
                    Vec3d vec3d = new Vec3d(n, o, p);
                    if (raycast(vec3d, source, ignoreTerrain) == HitResult.Type.MISS) {
                        ++miss;
                    }
                    ++hit;
                }
            }
        }
        return (float) miss / (float) hit;
    }

    @FastNative
    public static float getExposure(Vec3d source, Box box, boolean ignoreTerrain) {
        double d = 0.4545454446934474;
        double e = 0.21739130885479366;
        double f = 0.4545454446934474;

        int i = 0;
        int j = 0;

        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    double n = MathHelper.lerp(k, box.minX, box.maxX);
                    double o = MathHelper.lerp(l, box.minY, box.maxY);
                    double p = MathHelper.lerp(m, box.minZ, box.maxZ);
                    Vec3d vec3d = new Vec3d(n + 0.045454555306552624, o, p + 0.045454555306552624);
                    if (raycast(vec3d, source, ignoreTerrain) == HitResult.Type.MISS) {
                        ++i;
                    }
                    ++j;
                }
            }
        }

        return (float) i / (float) j;
    }

    private static BlockHitResult raycastGhost(RaycastContext context, BlockPos bPos, boolean ignoreTerrain) {
        return BlockView.raycast(context.getStart(), context.getEnd(), context, (innerContext, pos) -> {
            Vec3d vec3d = innerContext.getStart();
            Vec3d vec3d2 = innerContext.getEnd();

            BlockState blockState;
            if (pos.equals(bPos)) {
                blockState = Blocks.OBSIDIAN.getDefaultState();
            } else {
                blockState = mc.world.getBlockState(pos);
                if (ignoreTerrain && blockState.getBlock().getBlastResistance() < 600f) {
                    blockState = Blocks.AIR.getDefaultState();
                }
            }

            VoxelShape voxelShape = innerContext.getBlockShape(blockState, mc.world, pos);
            BlockHitResult blockHitResult = mc.world.raycastBlock(vec3d, vec3d2, pos, voxelShape, blockState);
            BlockHitResult blockHitResult2 = VoxelShapes.empty().raycast(vec3d, vec3d2, pos);
            double d = blockHitResult == null ? Double.MAX_VALUE : innerContext.getStart().squaredDistanceTo(blockHitResult.getPos());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : innerContext.getStart().squaredDistanceTo(blockHitResult2.getPos());
            return d <= e ? blockHitResult : blockHitResult2;
        }, innerContext -> {
            Vec3d vec3d = innerContext.getStart().subtract(innerContext.getEnd());
            return BlockHitResult.createMissed(innerContext.getEnd(), Direction.getFacing(vec3d.x, vec3d.y, vec3d.z), BlockPos.ofFloored(innerContext.getEnd()));
        });
    }

    public static HitResult.Type raycast(Vec3d start, Vec3d end, boolean ignoreTerrain) {
        return BlockView.raycast(start, end, null, (innerContext, blockPos) -> {
            BlockState blockState = mc.world.getBlockState(blockPos);
            if (blockState.getBlock().getBlastResistance() < 600 && ignoreTerrain) return null;
            BlockHitResult hitResult = blockState.getCollisionShape(mc.world, blockPos).raycast(start, end, blockPos);
            return hitResult == null ? null : hitResult.getType();
        }, (innerContext) -> HitResult.Type.MISS);
    }

    @FastNative
    public static int getProtectionAmount(Iterable<ItemStack> equipment) {
        int value = 0;
        for (ItemStack stack : equipment) {
            value += getProtectionAmount(stack);
        }
        return value;
    }

    @FastNative
    public static int getProtectionAmount(ItemStack stack) {
        if (mc.world == null || stack.isEmpty()) return 0;

        RegistryEntry<Enchantment> blastProtection = getEnchantmentEntry(Enchantments.BLAST_PROTECTION);
        RegistryEntry<Enchantment> protection = getEnchantmentEntry(Enchantments.PROTECTION);

        int modifierBlast = EnchantmentHelper.getLevel(blastProtection, stack);
        int modifier = EnchantmentHelper.getLevel(protection, stack);
        return modifierBlast * 2 + modifier;
    }

    @FastNative
    private static RegistryEntry<Enchantment> getEnchantmentEntry(RegistryKey<Enchantment> key) {
        return mc.world.getRegistryManager()
                .getOptional(RegistryKeys.ENCHANTMENT)
                .orElseThrow()
                .getEntry(key.getValue())
                .orElseThrow();
    }

    /**
     * Предсказывает позицию цели через wyvern {@link PredictUtils} и возвращает
     * её будущий хитбокс (bbox, смещённый на дельту предсказания).
     * Заменяет отсутствующий в wyvern clouse'овский PredictUtility.predictBox.
     */
    @FastNative
    private static Box predictBox(PlayerEntity target, int predictTicks) {
        if (target == null) return null;
        if (predictTicks <= 0) return target.getBoundingBox();

        Vec3d predicted = PredictUtils.getPredicted(target, predictTicks);
        Vec3d delta = predicted.subtract(target.getPos());
        Box box = target.getBoundingBox();
        return new Box(box.minX + delta.x, box.minY + delta.y, box.minZ + delta.z,
                box.maxX + delta.x, box.maxY + delta.y, box.maxZ + delta.z);
    }

    private static final class ClientExplosion implements Explosion {
        private Vec3d pos = Vec3d.ZERO;
        private Entity entity;
        private DamageSource damageSource;

        private void update(Vec3d pos, Entity entity, DamageSource damageSource) {
            this.pos = pos;
            this.entity = entity;
            this.damageSource = damageSource;
        }

        @Override
        public ServerWorld getWorld() {
            return null;
        }

        @Override
        public DestructionType getDestructionType() {
            return DestructionType.DESTROY;
        }

        @Override
        public LivingEntity getCausingEntity() {
            return entity instanceof LivingEntity living ? living : null;
        }

        @Override
        public Entity getEntity() {
            return entity;
        }

        @Override
        public float getPower() {
            return 6.0f;
        }

        @Override
        public Vec3d getPosition() {
            return pos;
        }

        @Override
        public boolean canTriggerBlocks() {
            return false;
        }

        @Override
        public boolean preservesDecorativeEntities() {
            return false;
        }
    }
}
