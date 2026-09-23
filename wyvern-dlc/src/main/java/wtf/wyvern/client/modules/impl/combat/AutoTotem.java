package wtf.wyvern.client.modules.impl.combat;

import wtf.wyvern.core.eventbus.EventTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Box;
import wtf.wyvern.core.events.impl.player.EventMoveInput;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.MultiBooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.client.modules.impl.movement.AutoSprint;
import wtf.wyvern.utility.game.player.PlayerInventoryUtil;
import wtf.wyvern.utility.game.player.SimulatedPlayer;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "AutoTotem",
        category = Category.COMBAT,
        description = "Автоматически берёт тотем в опасности"
)
public class AutoTotem extends Module {

    public static final AutoTotem INSTANCE = new AutoTotem();

    // --- режим ---
    private final ModeSetting mode = new ModeSetting("Режим", "Sloth", "BravoHVH");

    // ========================================================================
    //  SLOTH SETTINGS (видимы только в режиме Sloth)
    // ========================================================================

    private final MultiBooleanSetting triggers = new MultiBooleanSetting("Брать от",
            new MultiBooleanSetting.Value("Кристалл рядом", true),
            new MultiBooleanSetting.Value("Кристалл в руках", true),
            new MultiBooleanSetting.Value("Обсидиан в руках", true),
            new MultiBooleanSetting.Value("Якорь в руках", false),
            new MultiBooleanSetting.Value("Падения", true)) {
        {
            setVisible(() -> mode.is("Sloth"));
        }
    };

    private final SliderSetting hp = new SliderSetting("Здоровье на земле", 6, 1, 20, 0.5f,
            () -> mode.is("Sloth"));
    private final SliderSetting hpOnElytra = new SliderSetting("Здоровье на элитрах", 10, 1, 20, 0.5f,
            () -> mode.is("Sloth"));
    private final SliderSetting crystalRadius = new SliderSetting("Радиус от кристаллка", 6, 1, 12, 0.5f,
            () -> mode.is("Sloth") && isCrystalRadiusVisible());
    private final SliderSetting fallHeight = new SliderSetting("Колличество блоков", 10, 3, 50, 1f,
            () -> mode.is("Sloth") && triggers.isEnable("Падения"));
    private final BooleanSetting returnTotem = new BooleanSetting("Возвращать тотем", true,
            () -> mode.is("Sloth"));
    private final SliderSetting returnDelay = new SliderSetting("Задержка возврата", 20, 5, 100, 5f,
            () -> mode.is("Sloth") && returnTotem.isEnabled());
    private final BooleanSetting bypassgrim = new BooleanSetting("Обходить Grim", true,
            () -> mode.is("Sloth"));

    private final ModeSetting swapVersion = new ModeSetting("Версия свапа", () -> mode.is("Sloth"), "1.21.4", "1.16.5");

    // ========================================================================
    //  BRAVOHVH SETTINGS (видимы только в режиме BravoHVH)
    // ========================================================================

    private final BooleanSetting elytra = new BooleanSetting("Здоровье на элитрах", true,
            () -> mode.is("BravoHVH"));
    private final SliderSetting bravoHealth = new SliderSetting("Здоровье на земле", 4.0F, 0.0F, 20.0F, 0.5F,
            () -> mode.is("BravoHVH"));
    private final BooleanSetting fall = new BooleanSetting("Срабатывание на падение", true,
            () -> mode.is("BravoHVH"));
    private final SliderSetting fallDistance = new SliderSetting("Количество блоков", 20.0F, 10.0F, 50.0F, 0.1F,
            () -> mode.is("BravoHVH") && fall.isEnabled());
    private final SliderSetting elytraHealth = new SliderSetting("Здоровье на элитрах", 10.0F, 0.0F, 20.0F, 0.5F,
            () -> mode.is("BravoHVH") && elytra.isEnabled());
    private final BooleanSetting crystals = new BooleanSetting("Срабатывать на кристаллы", true,
            () -> mode.is("BravoHVH"));
    private final SliderSetting healthWithBall = new SliderSetting("Здоровье с шаром", 10.0F, 0.0F, 20.0F, 0.5F,
            () -> mode.is("BravoHVH") && crystals.isEnabled());
    private final BooleanSetting predict = new BooleanSetting("Предикт урона", true,
            () -> mode.is("BravoHVH"));
    private final SliderSetting safetyMargin = new SliderSetting("Запас здоровья", 2.0F, 0.0F, 10.0F, 0.5F,
            () -> mode.is("BravoHVH") && predict.isEnabled());
    private final BooleanSetting pingCompensation = new BooleanSetting("Компенсация пинга", true,
            () -> mode.is("BravoHVH"));
    private final BooleanSetting keepEnchanted = new BooleanSetting("Сохранять зачарованный", true,
            () -> mode.is("BravoHVH"));
    private final SliderSetting healthThreshold = new SliderSetting("Порог здоровья", 6.0F, 0.5F, 20.0F, 0.5F,
            () -> mode.is("BravoHVH") && keepEnchanted.isEnabled());

    // ========================================================================
    //  SLOTH STATE
    // ========================================================================

    private int bypassTicks;
    private int swapCooldown;
    private int savedTotemSlot = -1;
    private ItemStack originalOffhandItem = ItemStack.EMPTY;
    private boolean totemTakenByUs = false;
    private boolean returnMode = false;
    private boolean needFastSwap = false;
    private int safeTicks = 0;

    // ========================================================================
    //  BRAVOHVH STATE
    // ========================================================================

    private static final int SWAP_COOLDOWN = 2;
    private static final int POP_RESYNC_COOLDOWN = 2;
    private static final float RESTORE_MARGIN = 2.0F;
    private static final int BASE_LOOKAHEAD = 6;
    private static final int MAX_LOOKAHEAD = 20;
    private static final int PING_TICK_CAP = 6;
    private static final int MAX_SWAP_COOLDOWN = 6;

    private int bravoCooldown;
    private int pingTicks;
    private int cachedPlainSlot = -1;
    private ItemStack savedOffhand;

    private static final Predicate<Slot> IS_STORAGE_SLOT = s -> s.id >= 9 && s.id <= 44;

    private static final List<Predicate<ItemStack>> VALUABLE_RULES = new ArrayList<>(List.of(
            s -> s.getItem() == Items.TOTEM_OF_UNDYING && s.hasEnchantments(),
            s -> s.getItem() == Items.PLAYER_HEAD && s.hasEnchantments(),
            s -> s.get(DataComponentTypes.CUSTOM_NAME) != null
    ));

    @FastNative
    public static void registerValuable(Predicate<ItemStack> rule) {
        VALUABLE_RULES.add(rule);
    }

    // ========================================================================
    //  EVENT DISPATCH
    // ========================================================================

    @FastNative
    @EventTarget
    public void onInput(final EventMoveInput e) {
        if (!mode.is("Sloth")) return;
        if (bypassgrim.isEnabled() && bypassTicks > 0) {
            if (mc.player == null) return;
            mc.player.setSprinting(false);
            e.setForward(0);
            e.setStrafe(0);
        }
    }

    @FastNative
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (mc.player == null || mc.world == null) return;

        if (mode.is("Sloth")) {
            runSloth();
        } else {
            runBravoHVH();
        }
    }

    @FastNative
    @EventTarget
    public void onPacket(EventPacket event) {
        if (event.isSent()) return;
        if (!mode.is("BravoHVH")) return;

        if (event.getPacket() instanceof EntityStatusS2CPacket status
                && status.getStatus() == 35
                && status.getEntity(mc.world) == mc.player) {
            cachedPlainSlot = -1;
            int p = currentPingTicks();
            bravoCooldown = Math.min(MAX_SWAP_COOLDOWN, Math.max(POP_RESYNC_COOLDOWN, p * 2));
        }
    }

    // ========================================================================
    //  SLOTH MODE
    // ========================================================================

    @FastNative
    private void runSloth() {
        boolean isCrystalDanger = isCrystalDangerSloth();

        if (isCrystalDanger) {
            needFastSwap = true;
            safeTicks = 0;
        }

        if (swapCooldown > 0) {
            swapCooldown--;
        }

        if (bypassgrim.isEnabled() && bypassTicks > 0) {
            mc.player.setSprinting(false);
            bypassTicks--;

            if (bypassTicks <= 0) {
                if (returnMode) {
                    slothPerformReturn();
                } else {
                    slothPerformSwap();
                }
            }
            return;
        }

        boolean needTotem = slothShouldTakeTotem(isCrystalDanger);

        if (needTotem && !slothHasTotemInOffhand()) {
            int totemSlot = slothFindTotemSlot();
            if (totemSlot == -1) return;

            if (!needFastSwap && swapCooldown > 0) return;

            if (originalOffhandItem.isEmpty() && !totemTakenByUs) {
                originalOffhandItem = mc.player.getOffHandStack().copy();
            }

            savedTotemSlot = totemSlot;
            returnMode = false;
            safeTicks = 0;

            if (bypassgrim.isEnabled()) {
                bypassTicks = needFastSwap ? 1 : 2;
                swapCooldown = needFastSwap ? 0 : 2;
            } else {
                slothPerformSwap();
                swapCooldown = needFastSwap ? 0 : 2;
            }
        }

        boolean isSafe = !needTotem;

        if (isSafe) {
            safeTicks++;
        } else {
            safeTicks = 0;
        }

        if (returnTotem.isEnabled() && !needTotem && slothHasTotemInOffhand() && totemTakenByUs
                && safeTicks >= returnDelay.getCurrent()) {
            if (!needFastSwap && swapCooldown > 0) return;

            returnMode = true;

            if (bypassgrim.isEnabled()) {
                bypassTicks = needFastSwap ? 1 : 2;
                swapCooldown = needFastSwap ? 0 : 2;
            } else {
                slothPerformReturn();
                swapCooldown = needFastSwap ? 0 : 2;
            }
        }

        if (!isCrystalDanger) {
            needFastSwap = false;
        }
    }

    @FastNative
    private boolean isCrystalDangerSloth() {
        float radius = crystalRadius.getCurrent();
        double radiusSq = radius * radius;

        if (triggers.isEnable("Кристалл рядом")) {
            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof EndCrystalEntity) {
                    if (mc.player.squaredDistanceTo(entity) <= radiusSq) {
                        return true;
                    }
                }
            }
        }

        if (triggers.isEnable("Кристалл в руках")) {
            for (PlayerEntity player : mc.world.getPlayers()) {
                if (mc.player.squaredDistanceTo(player) <= radiusSq) {
                    if (player.getMainHandStack().isOf(Items.END_CRYSTAL)
                            || player.getOffHandStack().isOf(Items.END_CRYSTAL)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @FastNative
    private boolean slothShouldTakeTotem(boolean isCrystalDanger) {
        float currentHp = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        boolean isGliding = mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA) && mc.player.isGliding();

        float hpThreshold = isGliding ? hpOnElytra.getCurrent() : hp.getCurrent();

        if (currentHp <= hpThreshold) {
            return true;
        }

        if (isCrystalDanger) {
            return true;
        }

        float radius = crystalRadius.getCurrent();
        double radiusSq = radius * radius;

        if (triggers.isEnable("Обсидиан в руках")) {
            for (PlayerEntity player : mc.world.getPlayers()) {
                if (mc.player.squaredDistanceTo(player) <= radiusSq) {
                    if (player.getMainHandStack().isOf(Items.OBSIDIAN) || player.getOffHandStack().isOf(Items.OBSIDIAN)) {
                        return true;
                    }
                }
            }
        }

        if (triggers.isEnable("Якорь в руках")) {
            for (PlayerEntity player : mc.world.getPlayers()) {
                if (mc.player.squaredDistanceTo(player) <= radiusSq
                        && (player.getMainHandStack().isOf(Items.RESPAWN_ANCHOR)
                        || player.getOffHandStack().isOf(Items.RESPAWN_ANCHOR))) {
                    return true;
                }
            }
        }

        if (triggers.isEnable("Падения")) {
            if (mc.player.fallDistance >= fallHeight.getCurrent() && !isGliding) {
                return true;
            }
        }

        return false;
    }

    @FastNative
    private boolean slothHasTotemInOffhand() {
        return mc.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING);
    }

    @FastNative
    private int slothFindTotemSlot() {
        for (int i = 9; i < 45; i++) {
            ItemStack stack = mc.player.playerScreenHandler.getSlot(i).getStack();
            if (stack.isOf(Items.TOTEM_OF_UNDYING)) {
                return i;
            }
        }
        return -1;
    }

    @FastNative
    private void slothPerformSwap() {
        int totemSlot = slothFindTotemSlot();
        if (totemSlot == -1) return;

        savedTotemSlot = totemSlot;
        slothDoSwap(totemSlot);
        totemTakenByUs = true;
        mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
    }

    @FastNative
    private void slothPerformReturn() {
        if (!slothHasTotemInOffhand()) {
            totemTakenByUs = false;
            return;
        }

        if (!originalOffhandItem.isEmpty()) {
            int slotToReturn = findSlotForItem(originalOffhandItem);
            if (slotToReturn != -1) {
                slothDoSwap(slotToReturn);
            } else {
                if (savedTotemSlot == -1) {
                    savedTotemSlot = 9;
                }
                slothDoSwap(savedTotemSlot);
            }
        } else {
            if (savedTotemSlot == -1) {
                savedTotemSlot = 9;
            }
            slothDoSwap(savedTotemSlot);
        }

        totemTakenByUs = false;
        savedTotemSlot = -1;
        originalOffhandItem = ItemStack.EMPTY;
        mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
    }

    @FastNative
    private int findSlotForItem(ItemStack item) {
        if (item.isEmpty()) return -1;

        for (int i = 9; i < 45; i++) {
            ItemStack stack = mc.player.playerScreenHandler.getSlot(i).getStack();
            if (ItemStack.areItemsEqual(stack, item) && ItemStack.areEqual(stack, item)) {
                return i;
            }
        }
        return -1;
    }

    @FastNative
    private void slothDoSwap(int slot) {
        if (swapVersion.is("1.16.5")) {
            slothDoSwap1165(slot);
            return;
        }
        slothDoSwap1214(slot);
    }

    @FastNative
    private void slothDoSwap1214(int slot) {
        if (slot >= 36 && slot <= 44) {
            int hotbarSlot = slot - 36;
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, 45,
                    hotbarSlot, SlotActionType.SWAP, mc.player);
        } else {
            // A single server-side offhand swap is reliable on 1.21.4 and does
            // not depend on the currently opened screen's sync id.
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId,
                    slot, 40, SlotActionType.SWAP, mc.player);
        }
    }

    @FastNative
    private void slothDoSwap1165(int slot) {
        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId,
                slot, 40, SlotActionType.SWAP, mc.player);
    }

    @FastNative
    private boolean isCrystalRadiusVisible() {
        return triggers.isEnable("Кристалл рядом")
                || triggers.isEnable("Кристалл в руках")
                || triggers.isEnable("Обсидиан в руках")
                || triggers.isEnable("Якорь в руках");
    }

    // ========================================================================
    //  BRAVO HVH MODE
    // ========================================================================

    @FastNative
    private void runBravoHVH() {
        if (PlayerInventoryUtil.isServerScreen()) {
            cachedPlainSlot = -1;
            return;
        }

        pingTicks = currentPingTicks();

        if (bravoCooldown > 0) {
            bravoCooldown--;
            return;
        }

        final ItemStack offhand = mc.player.getOffHandStack();
        final boolean keep = keepEnchanted.isEnabled();
        final boolean danger = bravoShouldHaveTotem() || (keep && bravoEffectiveHealth() <= healthThreshold.getCurrent());

        if (danger) {
            bravoAcquireTotem(offhand, keep);
        } else {
            bravoRestoreSaved(offhand);
        }
    }

    @FastNative
    private void bravoAcquireTotem(ItemStack offhand, boolean keep) {
        boolean offIsTotem = offhand.getItem() == Items.TOTEM_OF_UNDYING;
        boolean offIsValuable = isValuable(offhand);

        if (keep) {
            if (offIsTotem && !offIsValuable) {
                return;
            }
            Slot plain = bravoFindTotemSlot(false);
            if (plain != null) {
                // An enchanted/custom-named totem must remain in the inventory;
                // saving it as the temporary offhand item would put it back in
                // the danger slot as soon as the health recovers.
                if (!offIsValuable) {
                    bravoRememberIfWorthIt(offhand);
                }
                bravoExecuteSwap(plain);
                return;
            }
            if (offIsTotem || offIsValuable) {
                return;
            }
            Slot any = bravoFindBestTotemSlot();
            if (any != null) {
                bravoExecuteSwap(any);
            }
        } else {
            if (offIsTotem) {
                return;
            }
            Slot any = bravoFindBestTotemSlot();
            if (any != null) {
                bravoRememberIfWorthIt(offhand);
                bravoExecuteSwap(any);
            }
        }
    }

    @FastNative
    private void bravoRestoreSaved(ItemStack offhand) {
        if (savedOffhand == null || savedOffhand.isEmpty()) {
            return;
        }
        if (bravoEffectiveHealth() < bravoTriggerCeiling() + RESTORE_MARGIN) {
            return;
        }
        if (ItemStack.areEqual(offhand, savedOffhand)) {
            savedOffhand = null;
            return;
        }
        Slot slot = bravoFindSavedSlot();
        if (slot != null) {
            bravoExecuteSwap(slot);
        }
        savedOffhand = null;
    }

    private Slot bravoFindTotemSlot(boolean allowValuable) {
        if (!allowValuable) {
            Slot cached = bravoValidateCachedPlain();
            if (cached != null) {
                return cached;
            }
        }
        Slot found = PlayerInventoryUtil.getSlot(s ->
                IS_STORAGE_SLOT.test(s)
                        && s.getStack().getItem() == Items.TOTEM_OF_UNDYING
                        && (allowValuable || !isValuable(s.getStack())));
        if (!allowValuable) {
            cachedPlainSlot = found != null ? found.id : -1;
        }
        return found;
    }

    @FastNative
    private Slot bravoValidateCachedPlain() {
        if (cachedPlainSlot < 0) {
            return null;
        }
        ScreenHandler handler = mc.player.currentScreenHandler;
        if (cachedPlainSlot >= handler.slots.size()) {
            cachedPlainSlot = -1;
            return null;
        }
        Slot s = handler.getSlot(cachedPlainSlot);
        if (s != null
                && s.id == cachedPlainSlot
                && IS_STORAGE_SLOT.test(s)
                && s.getStack().getItem() == Items.TOTEM_OF_UNDYING
                && !isValuable(s.getStack())) {
            return s;
        }
        cachedPlainSlot = -1;
        return null;
    }

    @FastNative
    private Slot bravoFindBestTotemSlot() {
        Slot plain = bravoFindTotemSlot(false);
        return plain != null ? plain : bravoFindTotemSlot(true);
    }

    private Slot bravoFindSavedSlot() {
        return PlayerInventoryUtil.getSlot(s ->
                IS_STORAGE_SLOT.test(s) && ItemStack.areEqual(s.getStack(), savedOffhand));
    }

    @FastNative
    private boolean bravoShouldHaveTotem() {
        if (mc.player.isInCreativeMode() || mc.player.isSpectator()) {
            return false;
        }
        float hp = bravoEffectiveHealth();

        if (hp <= bravoHealth.getCurrent()) {
            return true;
        }

        if (elytra.isEnabled() && bravoWearingElytra()) {
            if (hp <= elytraHealth.getCurrent()) {
                return true;
            }
            if (predict.isEnabled() && hp - predictImpactDamage() <= elytraHealth.getCurrent() + safetyMargin.getCurrent()) {
                return true;
            }
        }

        if (fall.isEnabled() && !mc.player.isGliding()) {
            if (mc.player.fallDistance >= fallDistance.getCurrent()) {
                return true;
            }
            if (predict.isEnabled() && hp - predictImpactDamage() <= bravoHealth.getCurrent() + safetyMargin.getCurrent()) {
                return true;
            }
        }

        return crystals.isEnabled() && isThreatenedByCrystal();
    }

    @FastNative
    private float predictImpactDamage() {
        int look = Math.min(MAX_LOOKAHEAD, BASE_LOOKAHEAD + pingTicks);
        SimulatedPlayer sim = SimulatedPlayer.fromClientPlayer(
                SimulatedPlayer.SimulatedPlayerInput.fromClientPlayer(mc.player.input.playerInput));

        float worst = 0.0F;
        for (int i = 0; i < look; i++) {
            double horizBefore = sim.velocity.horizontalLength();
            boolean wasFlying = sim.isFallFlying;
            boolean wasOnGround = sim.onGround;
            float fallBefore = sim.fallDistance;

            sim.tick();

            if (wasFlying && sim.horizontalCollision) {
                double horizAfter = sim.velocity.horizontalLength();
                worst = Math.max(worst, (float) ((horizBefore - horizAfter) * 10.0 - 3.0));
            }
            if (sim.onGround && !wasOnGround) {
                worst = Math.max(worst, bravoComputeFallDamage(fallBefore));
                break;
            }
        }
        return Math.max(0.0F, worst);
    }

    @FastNative
    private float bravoComputeFallDamage(float distance) {
        double safe = mc.player.getAttributeValue(EntityAttributes.SAFE_FALL_DISTANCE);
        double mult = mc.player.getAttributeValue(EntityAttributes.FALL_DAMAGE_MULTIPLIER);
        return (float) Math.max(0.0, Math.ceil((distance - safe) * mult));
    }

    @FastNative
    private boolean isThreatenedByCrystal() {
        ItemStack offhand = mc.player.getOffHandStack();
        boolean holdingBall = offhand.getItem() == Items.PLAYER_HEAD && offhand.hasEnchantments();

        Box near = mc.player.getBoundingBox().expand(6.0);
        List<Entity> nearby = mc.world.getOtherEntities(mc.player, near);
        if (nearby.isEmpty()) {
            return false;
        }

        int look = Math.min(MAX_LOOKAHEAD, 3 + pingTicks);
        double futureY = SimulatedPlayer.simulateLocalPlayer(look).pos.getY();

        for (Entity entity : nearby) {
            if (!(entity instanceof EndCrystalEntity crystal)) {
                continue;
            }
            if (mc.player.getEyePos().distanceTo(crystal.getBoundingBox().getCenter()) > 5.0) {
                continue;
            }
            boolean atOrAboveCrystal = mc.player.getY() >= crystal.getY() || futureY >= crystal.getY();
            if (!atOrAboveCrystal) {
                continue;
            }
            if (holdingBall && mc.player.getHealth() > healthWithBall.getCurrent()) {
                continue;
            }
            return true;
        }
        return false;
    }

    @FastNative
    private void bravoExecuteSwap(Slot slot) {
        boolean gliding = mc.player.isGliding();
        boolean wasSprinting = !gliding && bravoStopSprinting();
        PlayerInventoryUtil.swapHand(slot, Hand.OFF_HAND, false);
        PlayerInventoryUtil.closeScreen(true);
        if (wasSprinting) {
            bravoResumeSprinting();
        }
        cachedPlainSlot = -1;
        bravoCooldown = bravoSwapCooldown();
    }

    @FastNative
    private int bravoSwapCooldown() {
        int c = SWAP_COOLDOWN;
        if (pingCompensation.isEnabled()) {
            c = Math.max(c, pingTicks * 2 + 1);
        }
        return Math.min(c, MAX_SWAP_COOLDOWN);
    }

    @FastNative
    private int currentPingTicks() {
        return pingCompensation.isEnabled() ? pingToTicks() : 0;
    }

    @FastNative
    private int pingToTicks() {
        if (mc.getNetworkHandler() == null) {
            return 0;
        }
        PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
        int ms = entry != null ? Math.max(0, entry.getLatency()) : 0;
        return Math.min(PING_TICK_CAP, ms / 50);
    }

    @FastNative
    private void bravoRememberIfWorthIt(ItemStack offhand) {
        if (offhand.isEmpty()) {
            return;
        }
        if (offhand.getItem() == Items.TOTEM_OF_UNDYING && !isValuable(offhand)) {
            return;
        }
        if (savedOffhand == null) {
            savedOffhand = offhand.copy();
        }
    }

    @FastNative
    private boolean isValuable(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        for (Predicate<ItemStack> rule : VALUABLE_RULES) {
            if (rule.test(stack)) {
                return true;
            }
        }
        return false;
    }

    @FastNative
    private boolean bravoWearingElytra() {
        return mc.player.getInventory().getArmorStack(2).getItem() == Items.ELYTRA;
    }

    @FastNative
    private float bravoEffectiveHealth() {
        return mc.player.getHealth() + mc.player.getAbsorptionAmount();
    }

    @FastNative
    private float bravoTriggerCeiling() {
        float ceiling = bravoHealth.getCurrent();
        if (keepEnchanted.isEnabled()) {
            ceiling = Math.max(ceiling, healthThreshold.getCurrent());
        }
        if (elytra.isEnabled() && bravoWearingElytra()) {
            ceiling = Math.max(ceiling, elytraHealth.getCurrent());
        }
        return ceiling;
    }

    @FastNative
    private boolean bravoStopSprinting() {
        if (!mc.player.isSprinting()) {
            return false;
        }
        mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(new PlayerInput(false, false, false, false, false, false, false)));
        mc.player.setSprinting(false);
        mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, Mode.STOP_SPRINTING));
        if (!AutoSprint.INSTANCE.isEnabled()) {
            mc.options.sprintKey.setPressed(false);
        }
        return true;
    }

    @FastNative
    private void bravoResumeSprinting() {
        mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(mc.player.input.playerInput));
    }

    // ========================================================================
    //  DISABLE
    // ========================================================================

    @FastNative
    @Override
    public void onDisable() {
        bypassTicks = 0;
        swapCooldown = 0;
        savedTotemSlot = -1;
        originalOffhandItem = ItemStack.EMPTY;
        totemTakenByUs = false;
        returnMode = false;
        needFastSwap = false;
        safeTicks = 0;
        savedOffhand = null;
        bravoCooldown = 0;
        cachedPlainSlot = -1;
        super.onDisable();
    }
}
