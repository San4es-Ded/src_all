package rockstar.modules.combat;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.*;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pyrock.events.network.SendPacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationUtils;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;
import rockstar.client.notification.NotificationType;

@ModuleInfo(name="Auto Anchor", category=ModuleCategory.COMBAT, internalMethod09633="modules.descriptions.auto_anchor")
public class AutoAnchorModule
extends Module {
    private BooleanSetting internalField0650;
    private SliderSetting internalField0383;
    private SliderSetting internalField0382;
    private static final int internalField0227 = 4;
    private static final long internalField0229 = 4000L;
    private final Map<BlockPos, InternalType0251> internalField0543 = new ConcurrentHashMap<BlockPos, InternalType0251>();
    private final Stopwatch internalField0519 = new Stopwatch();
    private final Stopwatch internalField0518 = new Stopwatch();
    private final Stopwatch internalField1189 = new Stopwatch();
    private int internalField0228 = Integer.MIN_VALUE;
    private final EventListener<SendPacketEvent> internalField0157 = sendPacketEvent -> {
        if (!this.isEnabled()) {
            return;
        }
        if (sendPacketEvent.isCancelled()) {
            return;
        }
        Packet<?> packet = sendPacketEvent.getPacket();
        if (!(packet instanceof UpdateSelectedSlotC2SPacket)) {
            return;
        }
        UpdateSelectedSlotC2SPacket updateSelectedSlotC2SPacket = (UpdateSelectedSlotC2SPacket)packet;
        this.internalField0228 = updateSelectedSlotC2SPacket.getSelectedSlot();
    };
    private final EventListener<ClientPlayerTickEvent> internalField0158 = clientPlayerTickEvent -> {
        if (AutoAnchorModule.internalField0149.player == null || AutoAnchorModule.internalField0149.world == null || AutoAnchorModule.internalField0149.interactionManager == null || internalField0149.getNetworkHandler() == null) {
            return;
        }
        if (AutoAnchorModule.internalField0149.world.getEnvironmentAttributes().getAttributeValue(
            net.minecraft.world.attribute.EnvironmentAttributes.RESPAWN_ANCHOR_WORKS_GAMEPLAY,
            AutoAnchorModule.internalField0149.player.getBlockPos()
        )) {
            return;
        }
        this.internalMethod09386();
        BlockPos blockPos = this.internalMethod06824();
        if (blockPos != null) {
            this.internalMethod03004(blockPos);
            return;
        }
        if (this.internalField0650.internalMethod04496()) {
            this.internalMethod09576();
        }
    };

    public AutoAnchorModule() {
        this.internalMethod09385();
    }

    private void internalMethod09385() {
        this.internalField0650 = new BooleanSetting(this, "modules.settings.auto_anchor.place").internalMethod04836(true);
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.auto_anchor.min_damage", "modules.settings.auto_anchor.min_damage.desc").internalMethod05900(1.0f).internalMethod02732(20.0f).internalMethod08673(0.5f).internalMethod08074(4.0f);
        this.internalField0382 = new SliderSetting((SettingOwner)this, "modules.settings.auto_anchor.max_self_damage", "modules.settings.auto_anchor.max_self_damage.desc").internalMethod05900(0.0f).internalMethod02732(36.0f).internalMethod08673(0.5f).internalMethod08074(12.0f);
    }

    @Override
    public void onEnable() {
        this.internalField0228 = AutoAnchorModule.internalField0149.player != null ? AutoAnchorModule.internalField0149.player.getInventory().getSelectedSlot() : Integer.MIN_VALUE;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.internalField0543.clear();
        this.internalField0228 = Integer.MIN_VALUE;
    }

    private void internalMethod09386() {
        long l = System.currentTimeMillis();
        Iterator<Map.Entry<BlockPos, InternalType0251>> iterator = this.internalField0543.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, InternalType0251> entry = iterator.next();
            if (l - entry.getValue().internalField0229 > 4000L) {
                iterator.remove();
                continue;
            }
            if (AutoAnchorModule.internalField0149.world.getBlockState(entry.getKey()).getBlock() instanceof RespawnAnchorBlock || l - entry.getValue().internalField0229 <= 500L) continue;
            iterator.remove();
        }
    }

    private void internalMethod03004(BlockPos blockPos2) {
        BlockState blockState = AutoAnchorModule.internalField0149.world.getBlockState(blockPos2);
        if (!(blockState.getBlock() instanceof RespawnAnchorBlock)) {
            return;
        }
        InternalType0251 nestedValue2031 = this.internalField0543.computeIfAbsent(blockPos2.toImmutable(), blockPos -> new InternalType0251());
        int n = Math.max((Integer)blockState.get((Property)RespawnAnchorBlock.CHARGES), nestedValue2031.internalField0227);
        boolean bl = n >= 4;
        int n2 = this.internalMethod01306(Items.GLOWSTONE);
        boolean bl2 = AutoAnchorModule.internalField0149.player.getMainHandStack().isOf(Items.GLOWSTONE);
        boolean bl3 = AutoAnchorModule.internalField0149.player.getOffHandStack().isOf(Items.GLOWSTONE);
        if (!(bl || bl2 || bl3 || n2 != -1)) {
            this.internalMethod04351("auto_anchor.no_glowstone", Items.GLOWSTONE.getName().getString());
            return;
        }
        if (!this.internalField0519.internalMethod02365(25L)) {
            return;
        }
        Vec3d vec3d = new Vec3d((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 1.0, (double)blockPos2.getZ() + 0.5);
        if (AutoAnchorModule.internalField0149.player.getEyePos().distanceTo(vec3d) > 4.5) {
            return;
        }
        if (!this.internalMethod08842(vec3d)) {
            return;
        }
        this.internalMethod06633(vec3d);
        Hand hand = Hand.MAIN_HAND;
        int n3 = AutoAnchorModule.internalField0149.player.getInventory().getSelectedSlot();
        boolean bl4 = false;
        if (bl2) {
            hand = Hand.MAIN_HAND;
        } else if (bl3) {
            hand = Hand.OFF_HAND;
        } else if (n2 != -1 && !bl) {
            AutoAnchorModule.internalField0149.player.getInventory().setSelectedSlot(n2);
            this.internalMethod08157(n2);
            bl4 = true;
        }
        BlockHitResult blockHitResult = new BlockHitResult(vec3d, Direction.UP, blockPos2, false);
        AutoAnchorModule.internalField0149.interactionManager.interactBlock(AutoAnchorModule.internalField0149.player, hand, blockHitResult);
        AutoAnchorModule.internalField0149.player.swingHand(hand);
        if (!bl) {
            ++nestedValue2031.internalField0227;
        }
        nestedValue2031.internalField0229 = System.currentTimeMillis();
        this.internalField0519.internalMethod00701();
        if (bl4) {
            AutoAnchorModule.internalField0149.player.getInventory().setSelectedSlot(n3);
            this.internalMethod08157(n3);
        }
    }

    @Nullable
    private BlockPos internalMethod06824() {
        BlockPos blockPos = AutoAnchorModule.internalField0149.player.getBlockPos();
        int n = (int)Math.ceil(4.5);
        float f = -1.0f;
        double d = Double.MAX_VALUE;
        BlockPos blockPos2 = null;
        for (int i = -n; i <= n; ++i) {
            for (int j = -n; j <= n; ++j) {
                for (int k = -n; k <= n; ++k) {
                    Vec3d vec3d;
                    BlockPos blockPos3 = blockPos.add(i, j, k);
                    if (!(AutoAnchorModule.internalField0149.world.getBlockState(blockPos3).getBlock() instanceof RespawnAnchorBlock)) continue;
                    Vec3d vec3d2 = new Vec3d((double)blockPos3.getX() + 0.5, (double)blockPos3.getY() + 1.0, (double)blockPos3.getZ() + 0.5);
                    if (AutoAnchorModule.internalField0149.player.getEyePos().distanceTo(vec3d2) > 4.5 || !this.internalMethod08842(vec3d2) || !this.internalMethod03005(blockPos3) || this.internalMethod04424(blockPos3) || !this.internalMethod05502(vec3d = blockPos3.toCenterPos())) continue;
                    float f2 = this.internalMethod06632(vec3d);
                    if (!this.internalMethod06634(vec3d)) continue;
                    double d2 = AutoAnchorModule.internalField0149.player.squaredDistanceTo(vec3d);
                    if (!(f2 > f) && (f2 != f || !(d2 < d))) continue;
                    f = f2;
                    d = d2;
                    blockPos2 = blockPos3.toImmutable();
                }
            }
        }
        return blockPos2;
    }

    private void internalMethod09576() {
        Hand hand;
        boolean bl;
        int n = this.internalMethod01306(Items.RESPAWN_ANCHOR);
        boolean bl2 = bl = AutoAnchorModule.internalField0149.player.getMainHandStack().isOf(Items.RESPAWN_ANCHOR) || AutoAnchorModule.internalField0149.player.getOffHandStack().isOf(Items.RESPAWN_ANCHOR);
        if (n == -1 && !bl) {
            this.internalMethod04351("auto_anchor.no_anchor", Items.RESPAWN_ANCHOR.getName().getString());
            return;
        }
        if (this.internalMethod02600(Items.GLOWSTONE) < 4) {
            this.internalMethod04351("auto_anchor.no_glowstone", Items.GLOWSTONE.getName().getString());
            return;
        }
        BlockPos blockPos = this.internalMethod02429();
        if (blockPos == null) {
            return;
        }
        if (!this.internalField0518.internalMethod02365(1L)) {
            return;
        }
        Vec3d vec3d = new Vec3d((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.0, (double)blockPos.getZ() + 0.5);
        this.internalMethod06633(vec3d);
        int n2 = AutoAnchorModule.internalField0149.player.getInventory().getSelectedSlot();
        boolean bl3 = false;
        Hand hand2 = hand = AutoAnchorModule.internalField0149.player.getOffHandStack().isOf(Items.RESPAWN_ANCHOR) ? Hand.OFF_HAND : Hand.MAIN_HAND;
        if (hand == Hand.MAIN_HAND && !AutoAnchorModule.internalField0149.player.getMainHandStack().isOf(Items.RESPAWN_ANCHOR)) {
            AutoAnchorModule.internalField0149.player.getInventory().setSelectedSlot(n);
            this.internalMethod08157(n);
            bl3 = true;
        }
        BlockHitResult blockHitResult = new BlockHitResult(vec3d, Direction.UP, blockPos, false);
        AutoAnchorModule.internalField0149.interactionManager.interactBlock(AutoAnchorModule.internalField0149.player, hand, blockHitResult);
        AutoAnchorModule.internalField0149.player.swingHand(hand);
        this.internalField0543.put(blockPos.up().toImmutable(), new InternalType0251());
        this.internalField0518.internalMethod00701();
        if (bl3) {
            AutoAnchorModule.internalField0149.player.getInventory().setSelectedSlot(n2);
            this.internalMethod08157(n2);
        }
    }

    @Nullable
    private BlockPos internalMethod02429() {
        BlockPos blockPos = AutoAnchorModule.internalField0149.player.getBlockPos();
        int n = (int)Math.ceil(4.5);
        BlockState blockState = Blocks.RESPAWN_ANCHOR.getDefaultState();
        float f = -1.0f;
        double d = Double.MAX_VALUE;
        BlockPos blockPos2 = null;
        for (int i = -n; i <= n; ++i) {
            for (int j = -4; j <= 2; ++j) {
                for (int k = -n; k <= n; ++k) {
                    Vec3d vec3d;
                    BlockPos blockPos3 = blockPos.add(i, j, k);
                    BlockPos blockPos4 = blockPos3.up();
                    if (!AutoAnchorModule.internalField0149.world.getBlockState(blockPos3).isSolidBlock((BlockView)AutoAnchorModule.internalField0149.world, blockPos3) || !AutoAnchorModule.internalField0149.world.getBlockState(blockPos4).isReplaceable() || !AutoAnchorModule.internalField0149.world.canPlace(blockState, blockPos4, ShapeContext.absent())) continue;
                    Vec3d vec3d2 = new Vec3d((double)blockPos3.getX() + 0.5, (double)blockPos3.getY() + 1.0, (double)blockPos3.getZ() + 0.5);
                    if (AutoAnchorModule.internalField0149.player.getEyePos().distanceTo(vec3d2) > 4.5 || !this.internalMethod08842(vec3d2) || !this.internalMethod03005(blockPos4) || this.internalMethod04424(blockPos4) || !this.internalMethod05502(vec3d = blockPos4.toCenterPos()) || !this.internalMethod06634(vec3d)) continue;
                    float f2 = this.internalMethod06632(vec3d);
                    double d2 = AutoAnchorModule.internalField0149.player.squaredDistanceTo(vec3d);
                    if (!(f2 > f) && (f2 != f || !(d2 < d))) continue;
                    f = f2;
                    d = d2;
                    blockPos2 = blockPos3.toImmutable();
                }
            }
        }
        return blockPos2;
    }

    private boolean internalMethod06634(Vec3d vec3d) {
        float f = this.internalField0383.internalMethod08576();
        LivingEntity livingEntity = RockstarClient.getInstance().internalMethod04463().internalMethod01783();
        if (livingEntity != null && livingEntity.isAlive() && livingEntity != AutoAnchorModule.internalField0149.player) {
            if (livingEntity instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity)livingEntity;
                if (RockstarClient.getInstance().internalMethod03375().internalMethod00380(playerEntity.getName().getString())) {
                    return this.internalMethod06632(vec3d) >= f;
                }
            }
            return this.internalMethod07396(vec3d, livingEntity) >= f;
        }
        return this.internalMethod06632(vec3d) >= f;
    }

    private boolean internalMethod05502(Vec3d vec3d) {
        float f = this.internalMethod07396(vec3d, (LivingEntity)AutoAnchorModule.internalField0149.player);
        if (f > this.internalField0382.internalMethod08576()) {
            return false;
        }
        return f < AutoAnchorModule.internalField0149.player.getHealth() + AutoAnchorModule.internalField0149.player.getAbsorptionAmount();
    }

    private float internalMethod06632(Vec3d vec3d) {
        float f = 0.0f;
        for (PlayerEntity playerEntity : AutoAnchorModule.internalField0149.world.getPlayers()) {
            if (playerEntity == AutoAnchorModule.internalField0149.player || !playerEntity.isAlive() || RockstarClient.getInstance().internalMethod03375().internalMethod00380(playerEntity.getName().getString())) continue;
            f = Math.max(f, this.internalMethod07396(vec3d, (LivingEntity)playerEntity));
        }
        return f;
    }

    private float internalMethod07396(Vec3d vec3d, LivingEntity livingEntity) {
        Vec3d vec3d2 = livingEntity.getBoundingBox().getCenter();
        double d = vec3d2.distanceTo(vec3d);
        if (d > 5.0) {
            return 0.0f;
        }
        double d2 = this.internalMethod04664(vec3d, vec3d2) ? 1.0 : 0.35;
        double d3 = (1.0 - d / 5.0) * d2;
        float f = (float)((d3 * d3 + d3) / 2.0 * 7.0 * 10.0 + 1.0);
        Difficulty difficulty = AutoAnchorModule.internalField0149.world.getDifficulty();
        f *= (switch (difficulty) {
            case Difficulty.PEACEFUL -> 0.0f;
            case Difficulty.EASY -> 0.5f;
            case Difficulty.HARD -> 1.5f;
            default -> 1.0f;
        });
        float f2 = livingEntity.getArmor();
        return Math.max(0.0f, f *= 1.0f - Math.min(f2 / (f2 + 20.0f), 0.8f));
    }

    private boolean internalMethod03005(BlockPos blockPos) {
        return (double)blockPos.getY() + 0.5 - AutoAnchorModule.internalField0149.player.getY() >= 0.5;
    }

    private boolean internalMethod04424(BlockPos blockPos) {
        Vec3d vec3d = Vec3d.ofCenter((Vec3i)blockPos);
        Box box = new Box(vec3d, vec3d).expand(5.0);
        for (PlayerEntity playerEntity2 : AutoAnchorModule.internalField0149.world.getEntitiesByClass(PlayerEntity.class, box, playerEntity -> playerEntity != AutoAnchorModule.internalField0149.player)) {
            if (!playerEntity2.isAlive() || !RockstarClient.getInstance().internalMethod03375().internalMethod00380(playerEntity2.getName().getString())) continue;
            return true;
        }
        return false;
    }

    private boolean internalMethod04664(Vec3d vec3d, Vec3d vec3d2) {
        return AutoAnchorModule.internalField0149.world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)AutoAnchorModule.internalField0149.player)).getType() == HitResult.Type.MISS;
    }

    private boolean internalMethod08842(Vec3d vec3d) {
        BlockHitResult blockHitResult = AutoAnchorModule.internalField0149.world.raycast(new RaycastContext(AutoAnchorModule.internalField0149.player.getEyePos(), vec3d, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)AutoAnchorModule.internalField0149.player));
        return blockHitResult.getType() == HitResult.Type.MISS;
    }

    private int internalMethod01306(Item item) {
        for (int i = 0; i < 9; ++i) {
            if (!AutoAnchorModule.internalField0149.player.getInventory().getStack(i).isOf(item)) continue;
            return i;
        }
        return -1;
    }

    private int internalMethod02600(Item item) {
        int n = 0;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = AutoAnchorModule.internalField0149.player.getInventory().getStack(i);
            if (!itemStack.isOf(item)) continue;
            n += itemStack.getCount();
        }
        ItemStack itemStack = AutoAnchorModule.internalField0149.player.getOffHandStack();
        if (itemStack.isOf(item)) {
            n += itemStack.getCount();
        }
        return n;
    }

    private void internalMethod04351(String string, String string2) {
        if (!this.internalField1189.internalMethod02365(5000L)) {
            return;
        }
        this.internalField1189.internalMethod00701();
        RockstarClient.getInstance().internalMethod02503().internalMethod00599(NotificationType.internalField0705, LanguageManager.internalMethod07214(string), LanguageManager.internalMethod00160("auto_anchor.need_item", string2));
    }

    private void internalMethod08157(int n) {
        if (internalField0149.getNetworkHandler() == null || n < 0 || n > 8) {
            return;
        }
        if (n == this.internalField0228) {
            return;
        }
        internalField0149.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(n));
    }

    private void internalMethod06633(Vec3d vec3d) {
        Rotation typedValue266 = RotationUtils.internalMethod05580(vec3d);
        RockstarClient.getInstance().internalMethod02368().internalMethod00418(typedValue266, RotationBehavior.internalField1003, 180.0f, 180.0f, 180.0f, RotationPriority.internalField1009);
    }

    static final class InternalType0251 {
        int internalField0227;
        long internalField0229 = System.currentTimeMillis();

        InternalType0251() {
        }
    }
}
