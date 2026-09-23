package wtf.wyvern.client.modules.impl.misc;

import wtf.wyvern.core.eventbus.EventTarget;
import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.MultiBooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.utility.math.Timer;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "AutoDupe",
        category = Category.MISC,
        description = "Берёт киты, складывает нужное в сундук и выбрасывает остальное"
)
@FastNative
public final class AutoDupe extends Module {
    public static final AutoDupe INSTANCE = new AutoDupe();

    private static final long KIT_COOLDOWN = 3_600_000L; // 1 час
    private static final long REQUEST_INTERVAL = 15_000L; // 15 секунд
    private static final double REACH_SQ = 4.5D * 4.5D;
    private static final float THROW_PITCH = -30.0F; // смотрим вверх, чтобы предметы улетали дальше

    private static final List<Kit> KITS = List.of(
            new Kit("winner", KIT_COOLDOWN),
            new Kit("delta", KIT_COOLDOWN),
            new Kit("sponsor", KIT_COOLDOWN),
            new Kit("universal", KIT_COOLDOWN),
            new Kit("snowman", KIT_COOLDOWN),
            new Kit("cobra", KIT_COOLDOWN),
            new Kit("slime", KIT_COOLDOWN)
    );

    private final MultiBooleanSetting kits = new MultiBooleanSetting("Киты",
            MultiBooleanSetting.Value.of("Кит winner", false),
            MultiBooleanSetting.Value.of("Кит delta", false),
            MultiBooleanSetting.Value.of("Кит sponsor", false),
            MultiBooleanSetting.Value.of("Кит universal", false),
            MultiBooleanSetting.Value.of("Кит snowman", false),
            MultiBooleanSetting.Value.of("Кит cobra", false),
            MultiBooleanSetting.Value.of("Кит slime", false)
    );

    private final MultiBooleanSetting items = new MultiBooleanSetting("Предметы",
            MultiBooleanSetting.Value.of("Зелье Викинга", true),
            MultiBooleanSetting.Value.of("Зачарованное золотое яблоко", true),
            MultiBooleanSetting.Value.of("Ливалка", true),
            MultiBooleanSetting.Value.of("Клирик", true)
    );

    private final SliderSetting scanRadius = new SliderSetting("Радиус поиска сундука", 4.0F, 2.0F, 6.0F, 1.0F);

    private final Timer actionTimer = new Timer();
    private final Timer requestTimer = new Timer();
    private final Map<String, Long> kitCooldowns = new ConcurrentHashMap<>();

    private Phase phase = Phase.IDLE;
    private String lastRequestedKit;
    private boolean aimed;
    private float savedYaw;
    private float savedPitch;

    private AutoDupe() {
    }

    @EventTarget
    private void onTick(EventTick event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null || mc.getNetworkHandler() == null) {
            return;
        }

        try {
            switch (phase) {
                case IDLE -> tickIdle();
                case AWAIT_ITEMS -> tickAwaitItems();
                case DEPOSIT -> tickDeposit();
                case THROW -> tickThrow();
            }
        } catch (Exception ignored) {
        }
    }

    private void tickIdle() {
        if (lastRequestedKit != null && !requestTimer.finished(REQUEST_INTERVAL)) {
            return;
        }

        String kit = getKitToRequest();
        if (kit == null) {
            return;
        }

        mc.getNetworkHandler().sendChatCommand("kit " + kit);
        kitCooldowns.put(kit, System.currentTimeMillis());
        lastRequestedKit = kit;
        requestTimer.reset();
        actionTimer.reset();
        phase = Phase.AWAIT_ITEMS;
    }

    private void tickAwaitItems() {
        if (hasTargetItems() && actionTimer.finished(1500L)) {
            actionTimer.reset();
            phase = Phase.DEPOSIT;
            return;
        }

        // Предметы так и не пришли — не зависаем навсегда.
        if (actionTimer.finished(8000L)) {
            actionTimer.reset();
            phase = isInventoryEmpty() ? Phase.IDLE : Phase.THROW;
        }
    }

    private void tickDeposit() {
        if (!hasTargetItems()) {
            actionTimer.reset();
            phase = Phase.THROW;
            return;
        }

        if (mc.currentScreen instanceof GenericContainerScreen) {
            int containerSlots = mc.player.currentScreenHandler.slots.size() - 36;
            for (int i = containerSlots; i < mc.player.currentScreenHandler.slots.size(); i++) {
                ItemStack stack = mc.player.currentScreenHandler.getSlot(i).getStack();
                if (isTargetItem(stack)) {
                    mc.interactionManager.clickSlot(
                            mc.player.currentScreenHandler.syncId,
                            i,
                            0,
                            SlotActionType.QUICK_MOVE,
                            mc.player
                    );
                }
            }

            mc.player.closeHandledScreen();
            actionTimer.reset();
            phase = Phase.THROW;
            return;
        }

        // Ищем двойной сундук. Если его нет — ждём (ничего не трогаем).
        BlockPos chest = findNearbyDoubleChest();
        if (chest == null) {
            return;
        }

        if (actionTimer.finished(500L)) {
            openChest(chest);
            actionTimer.reset();
        }
    }

    private void tickThrow() {
        // Кидаем всё, что осталось в инвентаре, разворачивая игрока вверх, чтобы предметы улетали далеко.
        if (mc.currentScreen instanceof GenericContainerScreen) {
            return;
        }

        // Сначала целимся вверх и ждём тик, чтобы серверу ушёл новый поворот до выброса.
        if (!aimed) {
            savedYaw = mc.player.getYaw();
            savedPitch = mc.player.getPitch();
            mc.player.setPitch(THROW_PITCH);
            aimed = true;
            actionTimer.reset();
            return;
        }
        if (!actionTimer.finished(150L)) {
            return;
        }

        // Слоты playerScreenHandler: 5-8 броня, 9-44 инвентарь+хотбар, 45 левая рука.
        int syncId = mc.player.playerScreenHandler.syncId;
        boolean threw = false;
        for (int slot = 5; slot <= 45; slot++) {
            ItemStack stack = mc.player.playerScreenHandler.getSlot(slot).getStack();
            if (!stack.isEmpty()) {
                mc.interactionManager.clickSlot(syncId, slot, 1, SlotActionType.THROW, mc.player);
                threw = true;
            }
        }

        if (!threw || isInventoryEmpty()) {
            // Возвращаем исходный поворот и завершаем цикл.
            mc.player.setYaw(savedYaw);
            mc.player.setPitch(savedPitch);
            aimed = false;
            actionTimer.reset();
            phase = Phase.IDLE;
        }
    }

    private BlockPos findNearbyDoubleChest() {
        int radius = (int) scanRadius.getCurrent();
        BlockPos origin = mc.player.getBlockPos();
        Vec3d eye = mc.player.getEyePos();

        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;

        for (BlockPos pos : BlockPos.iterate(
                origin.getX() - radius, origin.getY() - radius, origin.getZ() - radius,
                origin.getX() + radius, origin.getY() + radius, origin.getZ() + radius)) {
            var state = mc.world.getBlockState(pos);
            if (!state.isOf(Blocks.CHEST) || !state.contains(Properties.CHEST_TYPE)) {
                continue;
            }
            if (state.get(Properties.CHEST_TYPE) == ChestType.SINGLE) {
                continue;
            }

            double dist = eye.squaredDistanceTo(Vec3d.ofCenter(pos));
            if (dist <= REACH_SQ && dist < bestDist) {
                bestDist = dist;
                best = pos.toImmutable();
            }
        }

        return best;
    }

    private void openChest(BlockPos pos) {
        Vec3d hit = Vec3d.ofCenter(pos);
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND,
                new BlockHitResult(hit, Direction.UP, pos, false));
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    private String getKitToRequest() {
        long now = System.currentTimeMillis();
        for (Kit kit : KITS) {
            if (!kits.isEnable("Кит " + kit.name)) {
                continue;
            }

            long last = kitCooldowns.getOrDefault(kit.name, 0L);
            if (now - last >= kit.cooldown) {
                return kit.name;
            }
        }
        return null;
    }

    private boolean hasTargetItems() {
        for (ItemStack stack : mc.player.getInventory().main) {
            if (!stack.isEmpty() && isTargetItem(stack)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTargetItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        if (items.isEnable("Зачарованное золотое яблоко") && stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            return true;
        }

        String displayName = Objects.requireNonNull(stack.getName()).getString().toLowerCase(java.util.Locale.ROOT);
        if (items.isEnable("Зелье Викинга") && displayName.contains("викинг")) {
            return true;
        }
        if (items.isEnable("Ливалка") && displayName.contains("ливал")) {
            return true;
        }
        if (items.isEnable("Клирик") && stack.isOf(Items.TIPPED_ARROW) && displayName.contains("клирик")) {
            return true;
        }

        return false;
    }

    private boolean isInventoryEmpty() {
        for (ItemStack stack : mc.player.getInventory().main) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        actionTimer.reset();
        requestTimer.reset();
        phase = Phase.IDLE;
        lastRequestedKit = null;
        aimed = false;
        kitCooldowns.clear();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        phase = Phase.IDLE;
        lastRequestedKit = null;
        aimed = false;
    }

    private enum Phase {
        IDLE,
        AWAIT_ITEMS,
        DEPOSIT,
        THROW
    }

    private record Kit(String name, long cooldown) {
    }
}
