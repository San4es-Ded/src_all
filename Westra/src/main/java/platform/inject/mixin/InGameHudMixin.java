package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.CrosshairEvent;
import aethereal.event.DrawEvent;
import aethereal.event.RemovalsEvent;
import aethereal.event.ScoreboardEvent;
import aethereal.render.Animations;
import aethereal.render.ColorUtil;
import java.util.function.Function;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1921;
import net.minecraft.class_2561;
import net.minecraft.class_266;
import net.minecraft.class_2960;
import net.minecraft.class_327;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_355;
import net.minecraft.class_5348;
import net.minecraft.class_8646;
import net.minecraft.class_9278;
import net.minecraft.class_9334;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import platform.inject.accessors.ItemCooldownEntryAccessor;
import platform.inject.accessors.ItemCooldownManagerAccessor;

@Mixin({class_329.class})
public class InGameHudMixin {
   @Shadow
   @Final
   private class_355 field_2015;

   @Inject(
      method = {"method_1762"},
      at = {@At("RETURN")}
   )
   private void onRenderHotbarItem(class_332 context, int x, int y, class_9779 counter, class_1657 player, class_1799 stack, int seed, CallbackInfo ci) {
      class_9278 charged = (class_9278)stack.method_57824(class_9334.field_49649);
      if (stack.method_7909() == class_1802.field_8399 && charged != null && !charged.method_57442()) {
         context.method_51448().method_22903();
         context.method_51448().method_46416(x + 2.0F, y + 2.0F, 250.0F);
         context.method_51448().method_22905(0.75F, 0.75F, 1.0F);
         context.method_51427((class_1799)charged.method_57437().getFirst(), 0, 0);
         context.method_51448().method_22909();
      }

      if (player == Interface.aM_.field_1724 && !stack.method_7960()) {
         ItemCooldownManagerAccessor cooldowns = (ItemCooldownManagerAccessor)player.method_7357();
         Object entry = cooldowns.getEntries().get(player.method_7357().method_62836(stack));
         if (entry != null && ((ItemCooldownEntryAccessor)entry).getEndTick() - cooldowns.getTick() > 0) {
            int remaining = ((ItemCooldownEntryAccessor)entry).getEndTick() - cooldowns.getTick();
            int seconds = (int)Math.ceil(remaining / 20.0F);
            context.method_51452();
            context.method_51448().method_22903();
            context.method_51448().method_46416(x, y, 300.0F);
            context.method_51433(Interface.aM_.field_1772, seconds > 99 ? "99+" : String.valueOf(seconds), 0, 0, this.cooldownColor(remaining), true);
            context.method_51448().method_22909();
         }
      }
   }

   @Unique
   private int cooldownColor(int remaining) {
      int seconds = (int)Math.ceil(remaining / 20.0F);
      switch (seconds) {
         case 0:
         case 1:
         case 2:
         case 3:
            return ColorUtil.a(80, 220, 100, 255);
         case 4:
         case 5:
         case 6:
            return ColorUtil.a(255, 200, 60, 255);
         default:
            return ColorUtil.a(255, 70, 70, 255);
      }
   }

   @Inject(
      method = {"method_1753"},
      at = {@At("HEAD")}
   )
   public void headRender(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      Westra.h().d().i().e().a(context.method_51448());
      EventManager.a((IEvent)(new DrawEvent(context, tickCounter.method_60637(false), DrawEvent.a.D2D)));
   }

   @Inject(
      method = {"method_1753"},
      at = {@At("TAIL")}
   )
   private void render(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      Animations animations = Westra.h().d().t().Q();
      if (animations.m() && animations.q().a("TAB").c() && animations.r().c() > 0.0F && !Interface.aM_.field_1690.field_1907.method_1434()) {
         this.field_2015
            .method_1919(
               context,
               Interface.aM_.method_22683().method_4486(),
               Interface.aM_.field_1687.method_8428(),
               Interface.aM_.field_1687.method_8428().method_1189(class_8646.field_45156)
            );
      }
   }

   @Inject(
      method = {"method_55805"},
      at = {@At("HEAD")}
   )
   private void headRenderMainHud(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      Animations animations = Westra.h().d().t().Q();
      if (animations.m() && animations.q().a("Поднятие хотбара").c()) {
         context.method_51448().method_22903();
         context.method_51448().method_46416(0.0F, -16.0F * animations.s().c(), 0.0F);
      }
   }

   @Inject(
      method = {"method_55805"},
      at = {@At("RETURN")}
   )
   private void renderMainHud(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      Animations animations = Westra.h().d().t().Q();
      if (animations.m() && animations.q().a("Поднятие хотбара").c()) {
         context.method_51448().method_22909();
      }
   }

   @Inject(
      method = {"method_1759"},
      at = {@At("HEAD")}
   )
   private void headRenderHotbar(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      Westra.h().d().t().ck().a(context);
   }

   @Redirect(
      method = {"method_1759"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_332;method_52706(Ljava/util/function/Function;Lnet/minecraft/class_2960;IIII)V"
      )
   )
   private void redirectHotbarTexture(class_332 context, Function<class_2960, class_1921> layer, class_2960 texture, int x, int y, int width, int height) {
      if (!Westra.h().d().t().ck().m()) {
         context.method_52706(layer, texture, x, y, width, height);
      }
   }

   @ModifyArg(
      method = {"method_1759"},
      index = 2,
      at = @At(
         value = "INVOKE",
         ordinal = 1,
         target = "Lnet/minecraft/class_332;method_52706(Ljava/util/function/Function;Lnet/minecraft/class_2960;IIII)V"
      )
   )
   private int hotbarSelectionSlot(int x) {
      Animations animations = Westra.h().d().t().Q();
      return animations.m() && animations.q().a("Слот хотбара").c() && Interface.aM_.field_1724 != null
         ? Math.round(x - Interface.aM_.field_1724.method_31548().field_7545 * 20 + animations.v() * 20.0F)
         : x;
   }

   @Inject(
      method = {"method_56136"},
      at = {@At("HEAD")}
   )
   private void headRenderExperienceLevel(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      Animations animations = Westra.h().d().t().Q();
      if (animations.m() && animations.q().a("Поднятие хотбара").c()) {
         context.method_51448().method_22903();
         context.method_51448().method_46416(0.0F, -16.0F * animations.s().c(), 0.0F);
      }
   }

   @Inject(
      method = {"method_56136"},
      at = {@At("RETURN")}
   )
   private void renderExperienceLevel(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      Animations animations = Westra.h().d().t().Q();
      if (animations.m() && animations.q().a("Поднятие хотбара").c()) {
         context.method_51448().method_22909();
      }
   }

   @Inject(
      method = {"method_1757(Lnet/minecraft/class_332;Lnet/minecraft/class_266;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderScoreboardSidebar(class_332 context, class_266 objective, CallbackInfo ci) {
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.SCOREBOARD);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }

   @Redirect(
      method = {"method_1757(Lnet/minecraft/class_332;Lnet/minecraft/class_266;)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_266;method_1114()Lnet/minecraft/class_2561;"
      )
   )
   private class_2561 scoreboardTitle(class_266 objective) {
      ScoreboardEvent event = new ScoreboardEvent(objective.method_1114());
      EventManager.a((IEvent)event);
      return event.b();
   }

   @Redirect(
      method = {"method_1757(Lnet/minecraft/class_332;Lnet/minecraft/class_266;)V"},
      at = @At(
         value = "INVOKE",
         ordinal = 1,
         target = "Lnet/minecraft/class_332;method_51439(Lnet/minecraft/class_327;Lnet/minecraft/class_2561;IIIZ)I"
      )
   )
   private int scoreboardLine(class_332 context, class_327 textRenderer, class_2561 text, int x, int y, int color, boolean shadow) {
      ScoreboardEvent event = new ScoreboardEvent(text);
      EventManager.a((IEvent)event);
      return context.method_51439(textRenderer, event.b(), x, y, color, shadow);
   }

   @Redirect(
      method = {"method_1757(Lnet/minecraft/class_332;Lnet/minecraft/class_266;)V"},
      at = @At(
         value = "INVOKE",
         ordinal = 1,
         target = "Lnet/minecraft/class_327;method_27525(Lnet/minecraft/class_5348;)I"
      )
   )
   private int scoreboardWidth(class_327 textRenderer, class_5348 line) {
      ScoreboardEvent event = new ScoreboardEvent((class_2561)line);
      EventManager.a((IEvent)event);
      return textRenderer.method_27525(event.b());
   }

   @Inject(
      method = {"method_1746"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderPortalOverlay(class_332 context, float nauseaStrength, CallbackInfo ci) {
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.PORTAL);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }

   @ModifyArgs(
      method = {"method_55798"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_329;method_31977(Lnet/minecraft/class_332;Lnet/minecraft/class_2960;F)V",
         ordinal = 0
      )
   )
   private void onRenderPumpkinOverlay(Args args) {
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.PUMPKIN);
      EventManager.a((IEvent)event);
      if (event.a()) {
         args.set(2, 0.0F);
      }
   }

   @Inject(
      method = {"method_1736"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderCrosshair(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      CrosshairEvent event = new CrosshairEvent(context, tickCounter.method_60637(false));
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"method_1765"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderStatusEffectOverlay(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      ci.cancel();
   }
}
