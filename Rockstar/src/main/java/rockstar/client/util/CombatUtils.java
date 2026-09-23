package rockstar.client.util;







import rockstar.client.server.*;
import rockstar.client.rotation.*;
import rockstar.client.notification.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.internal.game.EntityPositionTracker;
import rockstar.client.util.GameUtils;
import rockstar.client.internal.rotation.PlayerFallSimulator;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.EnchantmentUtils;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.inventory.HotbarSlot;
import rockstar.client.internal.core.BackTrackDataAccess;
import rockstar.modules.combat.AuraModule;
import rockstar.modules.combat.BackTrackModule;
import rockstar.modules.combat.CriticalsModule;
import rockstar.client.notification.NotificationType;

public final class CombatUtils
implements MinecraftClientAccess {
    public static HotbarSlot internalMethod06028() {
        SlotCollection<HotbarSlot> typedValue228 = InventorySlots.internalMethod02872();
        RegistryKey<Enchantment> preferredEnchantment = CombatUtils.internalField0149.player.fallDistance > 2.0f
            ? Enchantments.WIND_BURST
            : Enchantments.BREACH;
        HotbarSlot typedValue231 = typedValue228.internalMethod03297(stack -> CombatUtils.internalMethod06207(preferredEnchantment, stack));
        if (typedValue231 == null) {
            typedValue231 = typedValue228.internalMethod02510(Items.MACE);
        }
        return typedValue231;
    }

    public static Vec3d internalMethod01237(Entity entity, boolean bl) {
        Vec3d vec3d;
        BackTrackModule internalValue0005 = RockstarClient.getInstance().getModuleManager().getModule(BackTrackModule.class);
        if (internalValue0005.isEnabled() && entity instanceof BackTrackDataAccess && (vec3d = internalValue0005.internalMethod03167(entity)) != null) {
            return vec3d;
        }
        vec3d = EntityPositionTracker.internalMethod01255(entity);
        Vec3d vec3d2 = entity.getEntityPos();
        return CombatUtils.internalField0149.player.getEyePos().distanceTo(vec3d) < CombatUtils.internalField0149.player.getEyePos().distanceTo(vec3d2) && bl ? vec3d : vec3d2;
    }

    public static Box internalMethod04321(Entity entity, boolean bl) {
        return entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ()).offset(CombatUtils.internalMethod01237(entity, bl));
    }

    public static double internalMethod00827(Entity entity, boolean bl) {
        return entity.getEyeY() - entity.getY() + CombatUtils.internalMethod01237((Entity)entity, (boolean)bl).y;
    }

    public static float internalMethod03105(LivingEntity livingEntity) {
        AuraModule internalValue0004 = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
        return ServerUtils.internalMethod08700() || ServerUtils.internalMethod01786(KnownServer.internalField1567) || ServerUtils.internalMethod06501("cakeworld") || ServerUtils.internalMethod01786(KnownServer.internalField0579) || ServerUtils.internalMethod01786(KnownServer.internalField0578) ? internalValue0004.internalMethod05571() : 0.0f;
    }

    public static boolean internalMethod04254() {
        AuraModule internalValue0004 = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
        if (GameUtils.internalMethod02397(0.0, 2.0, 0.0) != Blocks.AIR && GameUtils.internalMethod02397(0.0, -1.0, 0.0) != Blocks.AIR && ServerUtils.internalMethod01786(KnownServer.internalField0579) && internalValue0004.internalMethod08808() % 5 == 0) {
            return false;
        }
        return CombatUtils.internalField0149.player.fallDistance > 1.2f || CombatUtils.internalField0149.player.fallDistance < 0.76f;
    }

    public static boolean internalMethod06040(LivingEntity livingEntity, boolean bl) {
        if (CombatUtils.internalField0149.world == null || CombatUtils.internalField0149.player == null) {
            return false;
        }
        Block block = CombatUtils.internalField0149.world.getBlockState(CombatUtils.internalField0149.player.getBlockPos().up(2)).getBlock();
        AuraModule internalValue0004 = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
        CriticalsModule typedValue118 = RockstarClient.getInstance().getModuleManager().getModule(CriticalsModule.class);
        double d = (double)((int)CombatUtils.internalField0149.player.getY()) - CombatUtils.internalField0149.player.getY();
        boolean bl2 = d == -0.01250004768371582;
        boolean bl3 = d == -0.1875;
        return CombatUtils.internalField0149.player.isClimbing() || CombatUtils.internalField0149.player.isTouchingWater() && GameUtils.internalMethod02397(0.0, 1.0, 0.0) == Blocks.WATER && CombatUtils.internalField0149.player.fallDistance <= 0.0f || CombatUtils.internalField0149.player.isSwimming() || typedValue118.internalMethod09264() || CombatUtils.internalField0149.player.isInLava() || internalValue0004.internalMethod06308().isSelected() || CombatUtils.internalField0149.player.getAbilities().flying || CombatUtils.internalField0149.player.hasStatusEffect(StatusEffects.BLINDNESS) || CombatUtils.internalField0149.player.hasStatusEffect(StatusEffects.LEVITATION) || CombatUtils.internalField0149.player.hasStatusEffect(StatusEffects.SLOW_FALLING) || CombatUtils.internalField0149.player.hasVehicle() || CombatUtils.internalField0149.player.fallDistance > CombatUtils.internalMethod03105(livingEntity) && !CombatUtils.internalField0149.player.isOnGround() && CombatUtils.internalMethod04254() || typedValue118.internalMethod09260() || GameUtils.internalMethod02397(0.0, 2.0, 0.0) != Blocks.AIR && GameUtils.internalMethod02397(0.0, -1.0, 0.0) != Blocks.AIR && ServerUtils.internalMethod01786(KnownServer.internalField0579) && CombatUtils.internalField0149.player.isOnGround() && internalValue0004.internalMethod08808() % 5 == 0;
    }

    public static boolean internalMethod03106(LivingEntity livingEntity) {
        if (CombatUtils.internalField0149.world == null || CombatUtils.internalField0149.player == null) {
            return false;
        }
        AuraModule internalValue0004 = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
        CriticalsModule typedValue118 = RockstarClient.getInstance().getModuleManager().getModule(CriticalsModule.class);
        double d = (double)((int)CombatUtils.internalField0149.player.getY()) - CombatUtils.internalField0149.player.getY();
        return CombatUtils.internalField0149.player.isClimbing() || CombatUtils.internalField0149.player.isTouchingWater() && GameUtils.internalMethod02397(0.0, 1.0, 0.0) == Blocks.WATER && CombatUtils.internalField0149.player.fallDistance <= 0.0f || CombatUtils.internalField0149.player.isSwimming() || typedValue118.internalMethod09264() || CombatUtils.internalField0149.player.isInLava() || internalValue0004.internalMethod06308().isSelected() || CombatUtils.internalField0149.player.getAbilities().flying || CombatUtils.internalField0149.player.hasStatusEffect(StatusEffects.BLINDNESS) || CombatUtils.internalField0149.player.hasStatusEffect(StatusEffects.LEVITATION) || CombatUtils.internalField0149.player.hasStatusEffect(StatusEffects.SLOW_FALLING) || CombatUtils.internalField0149.player.hasVehicle() || PlayerFallSimulator.internalMethod02421(CombatUtils.internalField0149.player).internalMethod04604(CombatUtils.internalMethod03105(livingEntity), 2) && !CombatUtils.internalField0149.player.isOnGround() || typedValue118.internalMethod09260();
    }

    public static boolean internalMethod04183(LivingEntity livingEntity) {
        if (!(livingEntity instanceof PlayerEntity)) {
            return false;
        }
        PlayerEntity playerEntity = (PlayerEntity)livingEntity;
        if (!playerEntity.isUsingItem()) {
            return false;
        }
        return playerEntity.getActiveItem().getItem() instanceof ShieldItem;
    }

    public static boolean internalMethod07758(LivingEntity livingEntity) {
        if (CombatUtils.internalField0149.player == null || CombatUtils.internalField0149.player.isDead()) {
            return false;
        }
        if (livingEntity.isDead()) {
            return false;
        }
        HotbarSlot typedValue231 = InventorySlots.internalMethod02872().internalMethod03297(itemStack -> itemStack.getItem() instanceof AxeItem);
        if (typedValue231 == null) {
            return false;
        }
        Vec3d vec3d = livingEntity.getRotationVector(0.0f, livingEntity.getYaw());
        Vec3d vec3d2 = new Vec3d(CombatUtils.internalField0149.player.getX() - livingEntity.getX(), 0.0, CombatUtils.internalField0149.player.getZ() - livingEntity.getZ());
        double d = vec3d2.length();
        if (d < 0.01) {
            return true;
        }
        return vec3d2.dotProduct(vec3d) > 0.0;
    }

    public static boolean internalMethod07972(LivingEntity livingEntity) {
        boolean bl;
        if (CombatUtils.internalField0149.player == null || CombatUtils.internalField0149.interactionManager == null) {
            return false;
        }
        if (!CombatUtils.internalMethod04183(livingEntity)) {
            return false;
        }
        HotbarSlot typedValue231 = InventorySlots.internalMethod02872().internalMethod03297(itemStack -> itemStack.getItem() instanceof AxeItem);
        if (typedValue231 == null) {
            return false;
        }
        int n = CombatUtils.internalField0149.player.getInventory().getSelectedSlot();
        boolean bl2 = bl = typedValue231.internalMethod08745() != n;
        if (bl) {
            CombatUtils.internalField0149.player.networkHandler.sendPacket((Packet)new UpdateSelectedSlotC2SPacket(typedValue231.internalMethod08745()));
        }
        CombatUtils.internalField0149.interactionManager.attackEntity((PlayerEntity)CombatUtils.internalField0149.player, (Entity)livingEntity);
        CombatUtils.internalField0149.player.swingHand(Hand.MAIN_HAND);
        if (bl) {
            CombatUtils.internalField0149.player.networkHandler.sendPacket((Packet)new UpdateSelectedSlotC2SPacket(n));
        }
        RockstarClient.getInstance().internalMethod02503().internalMethod00599(NotificationType.internalField0704, LanguageManager.internalMethod07214("shieldbreaker.title"), LanguageManager.internalMethod07214("shieldbreaker.desc"));
        return true;
    }

    public static boolean internalMethod09150(LivingEntity livingEntity) {
        Vec3d vec3d = livingEntity.getEntityPos();
        Box box = livingEntity.getBoundingBox();
        float f = 0.05f;
        return !CombatUtils.internalMethod01397(box.minX - (double)f, vec3d.y, box.minZ - (double)f) || !CombatUtils.internalMethod01397(box.maxX + (double)f, vec3d.y, box.minZ - (double)f) || !CombatUtils.internalMethod01397(box.minX - (double)f, vec3d.y, box.maxZ + (double)f) || !CombatUtils.internalMethod01397(box.maxX + (double)f, vec3d.y, box.maxZ + (double)f);
    }

    private static boolean internalMethod01397(double d, double d2, double d3) {
        return CombatUtils.internalField0149.world.getBlockState(new BlockPos((int)d, (int)d2, (int)d3)).getBlock() == Blocks.AIR;
    }

    @Generated
    private CombatUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static boolean internalMethod06207(RegistryKey registryKey, ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof MaceItem)) {
            return false;
        }
        return EnchantmentUtils.internalMethod03526(itemStack, (RegistryKey<Enchantment>)registryKey) > 0;
    }
}
