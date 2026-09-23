package wtf.wyvern.client.modules.impl.player;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.utility.game.other.NetworkUtils;
import wtf.wyvern.utility.game.player.PlayerInventoryUtil;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "AutoTool",
   category = Category.PLAYER,
   description = "При копании берет лучший предмет"
)
public final class AutoTool extends Module {
   public static final AutoTool INSTANCE = new AutoTool();
   private final ModeSetting mode = new ModeSetting("Режим", new String[]{"Обычный", "Скрытный", "HW/Lony", "GrimAC"});

   // Сколько тиков ждать после окончания ломания, прежде чем вернуть предмет.
   // Защищает от преждевременного restore в микропаузах между блоками серии.
   private static final int RESTORE_DELAY_TICKS = 3;

   // --- Свап инструмента из инвентаря (оба режима) ---
   // Инструмент свапается из своего исходного экран-слота в активный слот хотбара.
   // Для возврата достаточно повторить тот же свап — он симметричен.
   private boolean swapped;               // активен ли свап инструмента
   private boolean swappedStealth;        // был ли свап скрытным (с обходом grim)
   private int toolSourceScreenSlot = -1; // экран-слот, откуда пришёл инструмент
   private int swappedHotbarSlot = -1;    // слот хотбара, куда положили инструмент (= выбранный в момент свапа)

   // --- Обычный режим + инструмент в хотбаре (прямая смена selectedSlot) ---
   private int originalHotbarSlot = -1;

   private int notBreakingTicks = 0;

   // GrimAC changes the local/server held item immediately before START and
   // restores the exact previous item immediately after STOP/ABORT.
   private boolean grimSpoofing;
   private int grimOriginalSlot = -1;
   private int grimServerSlot = -1;
   private int grimSourceScreenSlot = -1;
   private int grimSwapHotbarSlot = -1;
   private boolean grimInventorySwap;
   private int grimIdleTicks;
   private boolean grimRestoring;

   private AutoTool() {
   }

   @FastNative
   @Override
   public void onDisable() {
      restoreGrimSlot();
      restore();
      super.onDisable();
   }

   @FastNative
   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (mc.player == null || mc.world == null || mc.interactionManager == null || mc.player.isCreative()) {
         restoreGrimSlot();
         restore();
         return;
      }

      if (isGrimMode()) {
         // A mode can be changed while the module is enabled.
         if (swapped || originalHotbarSlot != -1) {
            restore();
         }

         if (grimSpoofing) {
            if (mc.interactionManager.isBreakingBlock()) {
               grimIdleTicks = 0;
            } else if (++grimIdleTicks >= RESTORE_DELAY_TICKS) {
               restoreGrimSlot();
            }
         }
         return;
      }

      if (grimSpoofing) {
         restoreGrimSlot();
      }

      if (mc.interactionManager.isBreakingBlock()) {
         notBreakingTicks = 0;
         Block block = getTargetBlock();
         if (block == null) {
            restore();
            return;
         }

         // Если ломаем паутину и есть "Супер ножницы" — приоритет им
         if (block == Blocks.COBWEB) {
            int superScissorsSlot = findSuperScissorsSlot();
            if (superScissorsSlot != -1) {
               equipSlot(superScissorsSlot);
               return;
            }
         }

         if (isPacketMode()) {
            handleStealth(block);
         } else {
            handleNormal(block);
         }
      } else {
         // Дебаунс: ждём несколько тиков, прежде чем вернуть предмет.
         // Это предотвращает дёрганье свапов при ломании серии блоков,
         // когда между блоками бывают тики с isBreakingBlock() == false.
         notBreakingTicks++;
         if (notBreakingTicks >= RESTORE_DELAY_TICKS && (swapped || originalHotbarSlot != -1)) {
            restore();
         }
      }
   }

   /**
    * GrimAC mode handles the exact outgoing dig packet instead of waiting for
    * EventUpdate. This prevents the first START packet from being sent with the
    * old item and guarantees STOP/ABORT is sent before the old slot is restored.
    */
   @FastNative
   @EventTarget
   public void onPacket(EventPacket event) {
      if (!isGrimMode() || !event.isSent() || mc.player == null || mc.world == null
              || mc.player.isCreative()) {
         return;
      }

       if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket && grimSpoofing && !grimRestoring) {
         // Scrolling while mining must not replace the spoofed tool on the
         // server. Remember the new real slot and send it during restore.
         grimOriginalSlot = mc.player.getInventory().selectedSlot;
         mc.player.getInventory().selectedSlot = grimServerSlot;
         event.setCancelled(true);
         return;
      }

      if (!(event.getPacket() instanceof PlayerActionC2SPacket packet)) {
         return;
      }

      PlayerActionC2SPacket.Action action = packet.getAction();
      if (action == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
         equipGrimTool(packet.getPos());
      } else if ((action == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK
              || action == PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK) && grimSpoofing) {
         // A held attack key produces STOP/START pairs between adjacent blocks.
         // Keep the first tool equipped; EventUpdate restores it only after the
         // player has actually stopped breaking for several ticks.
         grimIdleTicks = 0;
      }
   }

   private void equipGrimTool(BlockPos pos) {
      if (grimSpoofing) {
         grimIdleTicks = 0;
         return;
      }

      Block block = mc.world.getBlockState(pos).getBlock();
      int selected = mc.player.getInventory().selectedSlot;
      int best = findBestToolInRange(block, 0, 35);

      if (best == -1) {
         return;
      }

      if (best == -1 || best == selected) {
         return;
      }

      grimOriginalSlot = selected;
      if (best > 8) {
         grimSourceScreenSlot = toScreenSlot(best);
         grimSwapHotbarSlot = selected;
         PlayerInventoryUtil.swapWithBypassGrim(() ->
                 mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId,
                         grimSourceScreenSlot, grimSwapHotbarSlot,
                         SlotActionType.SWAP, mc.player));
         grimInventorySwap = true;
         best = selected;
      }

      if (best != selected) {
         NetworkUtils.sendSilentPacket(new UpdateSelectedSlotC2SPacket(best));
         mc.player.getInventory().selectedSlot = best;
      }

      grimSpoofing = true;
      grimServerSlot = best;
      grimIdleTicks = 0;
   }

   private void restoreGrimSlot() {
      if (!grimSpoofing || grimRestoring) {
         return;
      }

      grimRestoring = true;
      try {
         if (mc.player != null && grimOriginalSlot >= 0 && grimOriginalSlot <= 8) {
            if (grimInventorySwap && grimSourceScreenSlot >= 0 && grimSwapHotbarSlot >= 0
                    && mc.interactionManager != null) {
               PlayerInventoryUtil.swapWithBypassGrim(() ->
                       mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId,
                               grimSourceScreenSlot, grimSwapHotbarSlot,
                               SlotActionType.SWAP, mc.player));
            }

            // Let the real restore packet through instead of treating it as
            // a slot scroll while the temporary tool is active.
            grimSpoofing = false;
            mc.player.getInventory().selectedSlot = grimOriginalSlot;
            if (mc.getNetworkHandler() != null && grimOriginalSlot != grimServerSlot) {
               NetworkUtils.sendSilentPacket(new UpdateSelectedSlotC2SPacket(grimOriginalSlot));
            }
         }
      } finally {
         grimSpoofing = false;
         grimOriginalSlot = -1;
         grimServerSlot = -1;
         grimSourceScreenSlot = -1;
         grimSwapHotbarSlot = -1;
         grimInventorySwap = false;
         grimIdleTicks = 0;
         grimRestoring = false;
      }
   }

   // ===================== СКРЫТНЫЙ РЕЖИМ =====================
   @FastNative
   private void handleStealth(Block block) {
      int sel = mc.player.getInventory().selectedSlot;

      // Ищем лучший инструмент по всему инвентарю в ТЕКУЩЕМ раскладе.
      int best = findBestToolInRange(block, 0, 35);

      // Нет инструмента лучше голой руки — снимать свап, если висит.
      if (best == -1) {
         restore();
         return;
      }

      // Лучший инструмент уже в выбранном слоте. Если это наш активный свап —
      // оставляем как есть; в противном случае менять ничего не нужно.
      if (best == sel) {
         return;
      }

      // Нужно принести 'best' в 'sel'. Сначала возвращаем любой висящий свап,
      // чтобы получить чистый расклад — иначе позиции предметов после возврата
      // не совпадут с только что вычисленным 'best'.
      if (swapped) {
         restore();
      }

      // После возврата позиции могли измениться — пересчитываем.
      best = findBestToolInRange(block, 0, 35);
      if (best == -1 || best == sel) {
         return;
      }

      int sourceScreenSlot = toScreenSlot(best);
      swap(sourceScreenSlot, sel, true);
      toolSourceScreenSlot = sourceScreenSlot;
      swappedHotbarSlot = sel;
      swappedStealth = true;
      swapped = true;
   }

   // ===================== ОБЫЧНЫЙ РЕЖИМ =====================
   // Ищет лучший инструмент по всему инвентарю.
   //  - инструмент в хотбаре (0-8): просто переключаем selectedSlot;
   //  - инструмент в инвентаре (9-35): свапаем его в текущий слот хотбара
   //    обычным SWAP-кликом (без обхода grim) и возвращаем после копания.
   @FastNative
   private void handleNormal(Block block) {
      int sel = mc.player.getInventory().selectedSlot;
      int best = findBestToolInRange(block, 0, 35);

      if (best == -1) {
         // Нет подходящего инструмента — возвращаем всё как было.
         restore();
         return;
      }

      // Инструмент в хотбаре — обходимся сменой слота (без свапа).
      if (best <= 8) {
         // Снимаем возможный инвентарь-свап, чтобы не висел.
         if (swapped) {
            restore();
         }
         if (originalHotbarSlot == -1) {
            originalHotbarSlot = sel;
         }
         if (sel != best) {
            mc.player.getInventory().selectedSlot = best;
            syncSelectedSlot();
         }
         return;
      }

      // Инструмент в инвентаре — нужен свап в текущий слот хотбара.
      if (originalHotbarSlot == -1) {
         originalHotbarSlot = sel;
      }

      // Лучший инструмент уже лежит в выбранном слоте (наш активный свап) — ок.
      if (swapped && best == sel) {
         return;
      }

      // Сначала возвращаем любой висящий свап — иначе позиции предметов
      // после возврата не совпадут с только что вычисленным 'best'.
      if (swapped) {
         restore();
         best = findBestToolInRange(block, 0, 35);
         sel = mc.player.getInventory().selectedSlot;
         if (best == -1) {
            return;
         }
      }

      // Перенесли инструмент в инвентаре в 'sel' — запоминаем как хотбар-свап.
      if (best != sel) {
         int sourceScreenSlot = toScreenSlot(best);
         swap(sourceScreenSlot, sel, false);
         toolSourceScreenSlot = sourceScreenSlot;
         swappedHotbarSlot = sel;
         swappedStealth = false;
         swapped = true;
      }
   }

   // ===================== СВАП / ВОЗВРАТ =====================
   /** Симметричный свап экран-слота со слотом хотбара через SWAP-клик. */
   private void swap(int screenSlot, int hotbarSlot, boolean stealth) {
      int syncId = mc.player.playerScreenHandler.syncId;
      if (stealth) {
         PlayerInventoryUtil.swapWithBypassGrim(() ->
                 mc.interactionManager.clickSlot(syncId, screenSlot, hotbarSlot, SlotActionType.SWAP, mc.player));
      } else {
         mc.interactionManager.clickSlot(syncId, screenSlot, hotbarSlot, SlotActionType.SWAP, mc.player);
      }
   }

   @FastNative
   private void restore() {
      if (mc.player == null || mc.interactionManager == null) {
         resetState();
         return;
      }

      // Активный свап инструмента: повторяем тот же свап — инструмент уходит обратно.
      if (swapped && toolSourceScreenSlot != -1 && swappedHotbarSlot != -1) {
         swap(toolSourceScreenSlot, swappedHotbarSlot, swappedStealth);
      }
      // Смена слота без свапа: возвращаем выбранный слот.
      else if (!swapped && originalHotbarSlot != -1
              && mc.player.getInventory().selectedSlot != originalHotbarSlot) {
         mc.player.getInventory().selectedSlot = originalHotbarSlot;
         syncSelectedSlot();
      }

      resetState();
   }

   @FastNative
   private void resetState() {
      swapped = false;
      swappedStealth = false;
      toolSourceScreenSlot = -1;
      swappedHotbarSlot = -1;
      originalHotbarSlot = -1;
      notBreakingTicks = 0;
   }

   @FastNative
   private void syncSelectedSlot() {
      if (mc.player != null && mc.getNetworkHandler() != null) {
         mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
      }
   }

   /** Inventory-индекс -> screen-слот playerScreenHandler (хотбар 0-8 -> 36-44, инвентарь 9-35 как есть). */
   @FastNative
   private int toScreenSlot(int inventoryIndex) {
      return inventoryIndex < 9 ? 36 + inventoryIndex : inventoryIndex;
   }

   @FastNative
   private Block getTargetBlock() {
      HitResult hit = mc.crosshairTarget;
      if (hit instanceof BlockHitResult blockHit) {
         return mc.world.getBlockState(blockHit.getBlockPos()).getBlock();
      }
      return null;
   }

   /** Ищет "Супер ножницы" по хотбару (0-8) и инвентарю (9-35). */
   @FastNative
   private int findSuperScissorsSlot() {
      for (int slot = 0; slot < 36; slot++) {
         ItemStack stack = mc.player.getInventory().getStack(slot);
         if (!stack.isEmpty() && stack.contains(DataComponentTypes.CUSTOM_NAME)) {
            String name = stack.getName().getString().toLowerCase();
            if (name.contains("супер ножницы") || name.contains("super scissors")) {
               return slot;
            }
         }
      }
      return -1;
   }

   /**
    * Экипирует предмет из указанного инвентарь-слота:
    *  - хотбар (0-8): смена selectedSlot;
    *  - инвентарь (9-35): свап в текущий слот хотбара (обычный или скрытный режим).
    */
   @FastNative
   private void equipSlot(int slot) {
      int sel = mc.player.getInventory().selectedSlot;

      if (slot <= 8) {
         // Инструмент в хотбаре — просто переключаем слот
         if (swapped) {
            restore();
         }
         if (originalHotbarSlot == -1) {
            originalHotbarSlot = sel;
         }
         if (sel != slot) {
            mc.player.getInventory().selectedSlot = slot;
            syncSelectedSlot();
         }
         return;
      }

      // Инструмент в инвентаре — свап в текущий слот хотбара
      if (originalHotbarSlot == -1) {
         originalHotbarSlot = sel;
      }

      // Уже активный свап этого инструмента — ок
      if (swapped && toolSourceScreenSlot == toScreenSlot(slot) && swappedHotbarSlot == sel) {
         return;
      }

      // Возвращаем висящий свап и пересчитываем
      if (swapped) {
         restore();
      }

      int sourceScreenSlot = toScreenSlot(slot);
      boolean stealth = isPacketMode();
      swap(sourceScreenSlot, sel, stealth);
      toolSourceScreenSlot = sourceScreenSlot;
      swappedHotbarSlot = sel;
      swappedStealth = stealth;
      swapped = true;
   }

   @FastNative
   private int findBestToolInRange(Block block, int start, int end) {
      int bestSlot = -1;
      float bestSpeed = 1.1F;

      for (int i = start; i <= end; i++) {
         float speed = getSpeed(i, block);
         if (speed > bestSpeed) {
            bestSpeed = speed;
            bestSlot = i;
         }
      }
      return bestSlot;
   }

   @FastNative
   private float getSpeed(int slot, Block block) {
      ItemStack stack = mc.player.getInventory().getStack(slot);
      return stack.getMiningSpeedMultiplier(block.getDefaultState());
   }

   @FastNative
   private boolean isPacketMode() {
      return mode.is("Скрытный") || mode.is("HW/Lony");
   }

   @FastNative
   private boolean isGrimMode() {
      return mode.is("GrimAC");
   }
}
