package wtf.wyvern.client.modules.impl.misc;

import wtf.wyvern.core.eventbus.EventTarget;
import java.util.List;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import wtf.wyvern.core.events.impl.input.EventKey;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.core.events.impl.other.EventTickMovement;
import wtf.wyvern.core.events.impl.player.EventMove;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BindSetting;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.utility.game.player.PlayerInventoryComponent;
import wtf.wyvern.utility.game.player.PlayerInventoryUtil;
import wtf.wyvern.utility.game.player.PlayerIntersectionUtil;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "ElytraHelper",
        category = Category.MISC,
        description = "Помогает летать на элитрах"
)
public final class ElytraHelper extends Module {
   public static final ElytraHelper INSTANCE = new ElytraHelper();
   private static final String SWAP_MODE_NORMAL = "Обычный";
   private static final String SWAP_MODE_LONY_HW = "Lony/HW";
   private static final int SWAP_STAGE_IDLE = 0;
   private static final int SWAP_STAGE_WAITING_TO_SWAP = 1;
   private static final int SWAP_SLOW_TICKS = 3;
   private static final double SWAP_MOVEMENT_MULTIPLIER = 0.55D;

   private final BindSetting elytraSetting = new BindSetting("Кнопка свапа");
   private final BindSetting fireworkSetting = new BindSetting("Кнопка фейерверка");
   private final ModeSetting swapMode = new ModeSetting("Режим свапа", SWAP_MODE_NORMAL, SWAP_MODE_LONY_HW);
   private final ModeSetting fireworkMode = new ModeSetting("Мод запуска фейерверка", "Рейдж", "Легит");
   private final BooleanSetting legitFireworkLaunch =
           new BooleanSetting("Легитный пуск фейерверка", false);
   private boolean swap;
   private boolean useFirework;
   private int fireworkTicks;
   private int swapStage;
   private int swapSlowTicks;

   @FastNative
   @EventTarget
   public void onKey(EventKey e) {
      if (e.isKeyDown(this.elytraSetting.getKeyCode()) && this.swapStage == SWAP_STAGE_IDLE) {
         this.swap = true;
      } else if (e.isKeyDown(this.fireworkSetting.getKeyCode())) {
         this.useFirework = true;
         this.fireworkTicks = 4;
      }

   }

   @FastNative
   @EventTarget
   private void onTick(EventTick e) {
      if (this.swapSlowTicks > 0) {
         this.swapSlowTicks--;
      }

      if (this.swapStage != SWAP_STAGE_IDLE) {
         handleLonySwapStage();
         return;
      }

      if (!this.swap) return;
      this.swap = false;
      if (!canSwapElytra() || chestPlate() == null) return;

      if (this.swapMode.is(SWAP_MODE_LONY_HW)) {
         slowMovementForSwap();
         this.swapSlowTicks = SWAP_SLOW_TICKS;
         this.swapStage = SWAP_STAGE_WAITING_TO_SWAP;
         return;
      }

      swapElytra();
   }

   @FastNative
   private void handleLonySwapStage() {
      if (this.swapStage == SWAP_STAGE_WAITING_TO_SWAP) {
         if (!canSwapElytra() || !swapElytra()) {
            finishLonySwap();
            return;
         }
      }
      finishLonySwap();
   }

   @FastNative
   private boolean canSwapElytra() {
      return mc.player != null && mc.interactionManager != null && mc.currentScreen == null
         && mc.player.currentScreenHandler.syncId == 0
         && mc.player.currentScreenHandler.getCursorStack().isEmpty();
   }

   private boolean swapElytra() {
      Slot source = chestPlate();
      if (source == null || source.id == 6) return false;

      int syncId = mc.player.currentScreenHandler.syncId;
      Runnable swapAction = () -> {
         if (source.id >= 36 && source.id <= 44) {
            mc.interactionManager.clickSlot(syncId, 6, source.id - 36,
                    SlotActionType.SWAP, mc.player);
            return;
         }

         // Use SWAP transactions instead of cursor PICKUP transactions.
         // The latter can leave a non-empty cursor when Grim delays one of the
         // three packets, which is observed by the server as a dropped chest
         // item. Three symmetric SWAPs are revision-safe and cursor-free.
         int hotbarSlot = mc.player.getInventory().selectedSlot;
         mc.interactionManager.clickSlot(syncId, source.id, hotbarSlot,
                 SlotActionType.SWAP, mc.player);
         mc.interactionManager.clickSlot(syncId, 6, hotbarSlot,
                 SlotActionType.SWAP, mc.player);
         mc.interactionManager.clickSlot(syncId, source.id, hotbarSlot,
                 SlotActionType.SWAP, mc.player);
      };

      if (this.swapMode.is(SWAP_MODE_LONY_HW)) {
         // Grim validates inventory clicks against the latest input state.
         // Keep the whole cursor-free transaction between the temporary
         // neutral-input packet and the restored real input packet.
         PlayerInventoryUtil.swapWithBypassGrim(swapAction);
      } else {
         swapAction.run();
      }
      return true;
   }

   @FastNative
   private void slowMovementForSwap() {
      PlayerInventoryComponent.canMove = true;
   }

   @FastNative
   @EventTarget
   private void onSwapMove(EventMove event) {
      if (this.swapSlowTicks > 0 && mc.player != null) {
         Vec3d movement = event.getMovePos();
         event.setMovePos(new Vec3d(
                 movement.x * SWAP_MOVEMENT_MULTIPLIER,
                 movement.y,
                 movement.z * SWAP_MOVEMENT_MULTIPLIER));
      }
   }

   @FastNative
   private void finishLonySwap() {
      this.swapStage = SWAP_STAGE_IDLE;
      PlayerInventoryComponent.canMove = true;
      PlayerInventoryComponent.updateMoveKeys();
   }

   @FastNative
   @EventTarget

   private void onTickMovement(EventTickMovement e) {
      if (mc.player == null) {
         return;
      }
      if (this.fireworkTicks > 0) {
         --this.fireworkTicks;
      }
      if (!this.useFirework || !mc.player.isGliding()) {
         if (this.fireworkTicks <= 0) {
            this.useFirework = false;
         }
         return;
      }
      this.useFirework = false;
      this.fireworkTicks = 0;

      if (this.legitFireworkLaunch.isEnabled()) {
         useFireworkFromOffhand();
         return;
      }

      // КЛЮЧЕВОЕ: если игрок что-то использует (ест зелёное/золотое яблоко на ПКМ) — НЕ трогаем
      // главную руку и НЕ меняем выбранный слот. Иначе UpdateSelectedSlot/interactItem дёрнут
      // clearActiveItem() и еда сбросится. Стреляем феером прямым interact-пакетом из offhand.
      if (mc.player.isUsingItem()) {
         useFireworkReliable();
         return;
      }

      // Обычное поведение, когда ничего не используем — без изменений.
      if (useDirectFirework()) {
         return;
      }

      if (this.fireworkMode.is("Рейдж")) {
         PlayerInventoryUtil.swapAndUseHvH(Items.FIREWORK_ROCKET);
      } else {
         PlayerInventoryUtil.swapAndUseLegit(Items.FIREWORK_ROCKET);
      }
   }

   /**
    * Временно переносит ракету в левую руку, запускает её этой рукой и
    * возвращает предмет, который находился в offhand, в исходный слот.
    */
   private boolean useFireworkFromOffhand() {
      if (mc.player.getOffHandStack().isOf(Items.FIREWORK_ROCKET)) {
         sendFireworkInteract(Hand.OFF_HAND);
         return true;
      }

      // Активный предмет в левой руке нельзя безопасно заменить: это прервёт его использование.
      if (mc.player.isUsingItem() && mc.player.getActiveHand() == Hand.OFF_HAND) {
         return false;
      }

      Slot slot = PlayerInventoryUtil.getSlot(s ->
              s.id >= 9 && s.id <= 44 && s.getStack().isOf(Items.FIREWORK_ROCKET));
      if (slot == null) {
         return false;
      }

      PlayerInventoryUtil.swapHand(slot, Hand.OFF_HAND, false);
      try {
         sendFireworkInteract(Hand.OFF_HAND);
      } finally {
         PlayerInventoryUtil.swapHand(slot, Hand.OFF_HAND, false);
         PlayerInventoryUtil.closeScreen(true);
      }
      return true;
   }

   /**
    * Запуск фейерверка БЕЗ прерывания текущего использования предмета (еды).
    *
    * Почему не сбрасывает еду:
    *  - не вызываем stopUsingItem()/clearActiveItem();
    *  - не меняем selectedSlot (именно его смена обнуляет активный предмет и на клиенте, и на сервере);
    *  - не зовём interactionManager.interactItem() (полный right-click со swing/синком слота);
    *  - используем ВТОРУЮ (свободную) руку: feer кладётся в offhand SWAP-ом (кнопка 40, активный
    *    слот не меняется), затем шлём один PlayerInteractItemC2SPacket(OFF_HAND) с корректным sequence.
    *  - на сервере FireworkRocketItem.use() в режиме полёта просто спавнит ракету и НЕ ставит активный
    *    предмет, так что еда главной руки на сервере не прерывается.
    */
   private void useFireworkWithoutBreakingUse() {
      // 1) feer уже в offhand — ничего не двигаем, просто interact на левую руку.
      if (mc.player.getOffHandStack().isOf(Items.FIREWORK_ROCKET)) {
         sendFireworkInteract(Hand.OFF_HAND);
         return;
      }

      // 2) едим главной рукой -> кладём feer в offhand SWAP-ом (selectedSlot не меняется, еда живёт),
      //    затем interact(OFF_HAND). Для еды из offhand этот путь пропускаем (offhand занят едой).
      if (mc.player.getActiveHand() == Hand.MAIN_HAND) {
         Slot slot = PlayerInventoryUtil.getSlot(s ->
            s.id >= 9 && s.id <= 44 && s.getStack().isOf(Items.FIREWORK_ROCKET));
         if (slot != null) {
            // Берём feer в offhand лишь на доли тика: SWAP -> interact -> SWAP обратно.
            // Пакеты идут по порядку (TCP), сервер успевает использовать ракету до возврата,
            // а offhand восстанавливается (пусто/тотем AutoTotem обратно). selectedSlot не меняется -> еда живёт.
            PlayerInventoryUtil.swapHand(slot, Hand.OFF_HAND, false); // source -> offhand
            sendFireworkInteract(Hand.OFF_HAND);                      // используем feer левой рукой
            PlayerInventoryUtil.swapHand(slot, Hand.OFF_HAND, false); // возвращаем всё на место
            PlayerInventoryUtil.closeScreen(true);
         }
         return;
      }

      // 3) едим offhand (редкий кейс) -> свободна главная рука; стреляем ей, только если feer уже в ней
      //    (смена слота ради feer сбросила бы offhand-еду, поэтому слот не трогаем).
      if (mc.player.getMainHandStack().isOf(Items.FIREWORK_ROCKET)) {
         sendFireworkInteract(Hand.MAIN_HAND);
      }
   }

   @FastNative
   private void useFireworkReliable() {
      if (useDirectFirework()) {
         return;
      }
      useFireworkWithoutBreakingUse();
   }

   @FastNative
   private boolean useDirectFirework() {
      if (mc.player.getOffHandStack().isOf(Items.FIREWORK_ROCKET)) {
         sendFireworkInteract(Hand.OFF_HAND);
         return true;
      }
      if (mc.player.getMainHandStack().isOf(Items.FIREWORK_ROCKET)) {
         sendFireworkInteract(Hand.MAIN_HAND);
         return true;
      }
      return false;
   }

   /** Минимальная корректная отправка use: только interact-пакет с управляемым sequence, без swing/синка слота. */
   private void sendFireworkInteract(Hand hand) {
      PlayerIntersectionUtil.sendSequencedPacket(sequence ->
         new PlayerInteractItemC2SPacket(hand, sequence, mc.player.getYaw(), mc.player.getPitch()));
   }

   @FastNative
   private Slot chestPlate() {
      return mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)
         ? PlayerInventoryUtil.getSlot(List.of(Items.NETHERITE_CHESTPLATE, Items.DIAMOND_CHESTPLATE,
            Items.CHAINMAIL_CHESTPLATE, Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.LEATHER_CHESTPLATE))
         : PlayerInventoryUtil.getSlot(Items.ELYTRA);
   }

   @FastNative
   @Override
   public void onDisable() {
      if (this.swapStage != SWAP_STAGE_IDLE) {
         finishLonySwap();
      }
      this.swap = false;
      this.useFirework = false;
      this.fireworkTicks = 0;
      this.swapStage = SWAP_STAGE_IDLE;
      this.swapSlowTicks = 0;
      PlayerInventoryComponent.canMove = true;
      super.onDisable();
   }
}
