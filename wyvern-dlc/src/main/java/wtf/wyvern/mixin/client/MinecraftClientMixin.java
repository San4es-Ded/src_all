package wtf.wyvern.mixin.client;

import wtf.wyvern.core.eventbus.EventManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.events.impl.input.EventSetScreen;
import wtf.wyvern.core.events.impl.other.EventGameUpdate;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.client.modules.impl.player.FastUse;
import wtf.wyvern.client.gui.screens.MainMenuScreen;

@Mixin({MinecraftClient.class})
public abstract class MinecraftClientMixin {
   @Shadow
   private int itemUseCooldown;
   @Unique
   private long lastHookTime = Util.getMeasuringTimeNano();
   @Unique
   private int accumulatedCalls = 0;

   @Inject(
      method = {"<init>"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/MinecraftClient$1;<init>(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/RunArgs;)V"
)}
   )
   public void init(RunArgs args, CallbackInfo ci) {
      Wyvern.getInstance().init();
   }

   @ModifyVariable(
      method = {"setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private Screen mixin$modifySetScreenArg(Screen original) {
      if (original instanceof TitleScreen) {
         return new MainMenuScreen();
      }
      EventSetScreen event = new EventSetScreen(original);
      EventManager.call(event);
      return event.getScreen();
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   private void render(boolean tick, CallbackInfo ci) {
      wtf.wyvern.utility.math.MultipointUtils.onClientFrame();
      long now = Util.getMeasuringTimeNano();
      long delta = now - this.lastHookTime;
      this.accumulatedCalls += (int)(delta / 4166666L);
      this.lastHookTime += (long)this.accumulatedCalls * 4166666L;

      for(this.accumulatedCalls = Math.min(this.accumulatedCalls, 240); this.accumulatedCalls > 0; --this.accumulatedCalls) {
         EventManager.call(new EventGameUpdate());
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   public void tick(CallbackInfo ci) {
      EventTick event = new EventTick();
      EventManager.call(event);
      if (FastUse.INSTANCE.shouldRemoveBlockCooldown()) {
         this.itemUseCooldown = 0;
      }
   }

}
