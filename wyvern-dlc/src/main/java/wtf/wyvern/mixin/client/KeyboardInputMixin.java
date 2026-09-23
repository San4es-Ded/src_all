package wtf.wyvern.mixin.client;

import wtf.wyvern.core.eventbus.EventManager;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.PlayerInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.MinecraftClient;
import wtf.wyvern.render.display.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.wyvern.core.events.impl.player.EventMoveInput;
import wtf.wyvern.client.modules.impl.movement.InventoryMove;

@Mixin({KeyboardInput.class})
public abstract class KeyboardInputMixin extends Input {
   @Shadow
   @Final
   private GameOptions settings;

   @Unique
   private float abobaGetMovementMultiplier(boolean positive, boolean negative) {
      if (positive == negative) {
         return 0.0F;
      } else {
         return positive ? 1.0F : -1.0F;
      }
   }

   @Unique
   private boolean wyvern$isPhysicallyPressed(MinecraftClient client, KeyBinding keyBinding) {
      int code = InputUtil.fromTranslationKey(keyBinding.getBoundKeyTranslationKey()).getCode();
      return code >= 0 && InputUtil.isKeyPressed(client.getWindow().getHandle(), code);
   }

   @Inject(
      method = {"tick"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/input/KeyboardInput;playerInput:Lnet/minecraft/util/PlayerInput;",
   ordinal = 0,
   shift = Shift.AFTER
)},
      cancellable = true
   )
   public void injectInputEvent(CallbackInfo ci) {
      MinecraftClient client = MinecraftClient.getInstance();

      if (InventoryMove.INSTANCE.isEnabled() && InventoryMove.INSTANCE.shouldHandleInput()) {
          boolean suspended = InventoryMove.INSTANCE.isInputSuspended();
          // Screens clear KeyBinding#pressed before KeyboardInput.tick on newer
          // clients. Read the physical keys here so every InventoryMove mode receives
          // current input in the same tick instead of a permanently false state.
          boolean f = !suspended && wyvern$isPhysicallyPressed(client, client.options.forwardKey);
          boolean b = !suspended && wyvern$isPhysicallyPressed(client, client.options.backKey);
          boolean l = !suspended && wyvern$isPhysicallyPressed(client, client.options.leftKey);
          boolean r = !suspended && wyvern$isPhysicallyPressed(client, client.options.rightKey);
          this.movementForward = abobaGetMovementMultiplier(f, b);
          this.movementSideways = abobaGetMovementMultiplier(l, r);
          boolean jumping = !suspended && wyvern$isPhysicallyPressed(client, client.options.jumpKey);
          boolean sneaking = !suspended && wyvern$isPhysicallyPressed(client, client.options.sneakKey);
          boolean sprinting = !suspended && wyvern$isPhysicallyPressed(client, client.options.sprintKey);

          this.playerInput = new PlayerInput(f, b, l, r, jumping, sneaking, sprinting);
          ci.cancel();
          return;
      }

      EventMoveInput event = new EventMoveInput(this.playerInput, this.abobaGetMovementMultiplier(this.playerInput.forward(), this.playerInput.backward()), this.abobaGetMovementMultiplier(this.playerInput.left(), this.playerInput.right()));
      EventManager.call(event);
      if (!event.isCancelled()) {
         this.movementForward = event.getForward();
         this.movementSideways = event.getStrafe();
         this.playerInput = new PlayerInput(this.movementForward > 0.0F, this.movementForward < 0.0F, this.movementSideways > 0.0F, this.movementSideways < 0.0F, client.options.jumpKey.isPressed(), client.options.sneakKey.isPressed(), client.options.sprintKey.isPressed());
         ci.cancel();
      }
   }
}
