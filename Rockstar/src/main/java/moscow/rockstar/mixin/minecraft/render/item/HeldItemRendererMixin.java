package moscow.rockstar.mixin.minecraft.render.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pyrock.events.render.HandRenderEvent;
import rockstar.client.RockstarClient;
import rockstar.client.esp.EspManager;
import rockstar.client.esp.FillEspFeature;
import rockstar.client.esp.FlameEspFeature;
import rockstar.client.esp.GlowEspFeature;
import rockstar.client.esp.ItemTargetType;
import rockstar.client.internal.game.DynamicLightEngine;
import rockstar.client.internal.render.EspGlowCaptureRenderer;
import rockstar.client.module.ModuleManager;
import rockstar.modules.visual.SwingAnimationModule;
import rockstar.modules.visual.ViewModelModule;

/** First-person hand hooks adapted to 1.21.11's extracted render-command queue. */
@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
   @Shadow private ItemStack mainHand;
   @Shadow private ItemStack offHand;
   @Shadow private float equipProgressMainHand;
   @Shadow private float lastEquipProgressMainHand;
   @Shadow private float equipProgressOffHand;
   @Shadow private float lastEquipProgressOffHand;
   @Shadow @Final private ItemModelManager itemModelManager;

   @Unique private static boolean rockstar$handRenderHijacked;
   @Unique private static boolean rockstar$firstPersonItemRendered;
   @Unique private static boolean rockstar$tookOverHandRender;
   @Unique private static boolean rockstar$decoratingHeldItem;
   @Unique private boolean rockstar$eventMatrixPushed;

   @Shadow
   protected abstract void renderFirstPersonItem(
      AbstractClientPlayerEntity player,
      float tickProgress,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      OrderedRenderCommandQueue queue,
      int light
   );

   @ModifyVariable(
      method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V",
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0
   )
   private int rockstar$applyDynamicLight(int light) {
      MinecraftClient client = MinecraftClient.getInstance();
      return client.player == null ? light : DynamicLightEngine.internalMethod07614(BlockPos.ofFloored(client.player.getEyePos()), light);
   }

   @Inject(
      method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void rockstar$onRenderFirstPersonItem(
      AbstractClientPlayerEntity player,
      float tickProgress,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      OrderedRenderCommandQueue queue,
      int light,
      CallbackInfo ci
   ) {
      rockstar$firstPersonItemRendered = true;
      Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
      boolean rightArm = arm == Arm.RIGHT;
      EspGlowCaptureRenderer.internalField1099 = !rightArm;

      matrices.push();
      this.rockstar$eventMatrixPushed = true;
      HandRenderEvent event = new HandRenderEvent(arm, swingProgress, item, equipProgress, matrices);
      RockstarClient.getInstance().internalMethod03317().internalMethod06883(event);
      if (!event.isCancelled()) {
         return;
      }

      float x = -0.4F * MathHelper.sin(MathHelper.sqrt(0.0F) * (float)Math.PI);
      float y = 0.2F * MathHelper.sin(MathHelper.sqrt(0.0F) * (float)(Math.PI * 2));
      float z = -0.2F * MathHelper.sin(0.0F);
      int direction = rightArm ? 1 : -1;
      matrices.translate(direction * x, y, z);
      matrices.translate(direction * 0.56F, -0.52F, -0.72F);
      if (!item.isEmpty()) {
         int dynamicLight = DynamicLightEngine.internalMethod07614(BlockPos.ofFloored(player.getEyePos()), light);
         ItemDisplayContext context = rightArm ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
         ((HeldItemRenderer)(Object)this).renderItem(player, item, context, matrices, queue, dynamicLight);
      }

      this.rockstar$popEventMatrix(matrices);
      ci.cancel();
   }

   @Inject(
      method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V",
      at = @At("RETURN")
   )
   private void rockstar$onRenderFirstPersonItemEnd(
      AbstractClientPlayerEntity player,
      float tickProgress,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      OrderedRenderCommandQueue queue,
      int light,
      CallbackInfo ci
   ) {
      this.rockstar$popEventMatrix(matrices);
   }

   @Inject(
      method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void rockstar$decorateHeldItem(
      LivingEntity entity,
      ItemStack stack,
      ItemDisplayContext context,
      MatrixStack matrices,
      OrderedRenderCommandQueue queue,
      int light,
      CallbackInfo ci
   ) {
      if (rockstar$decoratingHeldItem || !EspGlowCaptureRenderer.internalField0276 || !rockstar$isHeldInFirstPerson(entity, context)) {
         return;
      }

      boolean leftHanded = context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
      if (!ViewModelModule.internalField0277) {
         ViewModelModule viewModel = rockstar$viewModel();
         if (viewModel != null) {
            viewModel.internalMethod04205(
               entity,
               stack,
               context,
               leftHanded,
               matrices,
               entity.getEntityWorld(),
               light,
               OverlayTexture.DEFAULT_UV,
               entity.getId() + context.ordinal()
            );
         }
      }

      EspGlowCaptureRenderer.internalMethod00596(stack, context, leftHanded, matrices);
      EspManager espManager = EspManager.internalMethod06726();
      if (espManager == null) {
         return;
      }
      GlowEspFeature glow = espManager.internalMethod05464(GlowEspFeature.class);
      FlameEspFeature flame = espManager.internalMethod05464(FlameEspFeature.class);
      FillEspFeature fill = espManager.internalMethod05464(FillEspFeature.class);
      boolean renderGlow = glow != null && glow.internalMethod02927(ItemTargetType.internalField0012);
      boolean renderFlame = flame != null && flame.internalMethod02927(ItemTargetType.internalField0012);
      boolean renderFill = fill != null && fill.internalMethod02927(ItemTargetType.internalField0012);
      boolean hideVanilla = fill != null && fill.internalMethod04999();

      if (entity instanceof AbstractClientPlayerEntity player && (renderGlow || renderFlame || renderFill)) {
         rockstar$decoratingHeldItem = true;
         try {
            HeldItemRenderer renderer = (HeldItemRenderer)(Object)this;
            if (renderGlow) glow.internalMethod07050(renderer, player, stack, context, leftHanded, matrices, light);
            if (renderFlame) flame.internalMethod00220(renderer, player, stack, context, leftHanded, matrices, light);
            if (renderFill) fill.internalMethod01398(renderer, player, stack, context, leftHanded, matrices, light);
         } finally {
            rockstar$decoratingHeldItem = false;
         }
      }

      if (hideVanilla) {
         ci.cancel();
      }
   }

   @Inject(
      method = "renderArmHoldingItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IFFLnet/minecraft/util/Arm;)V",
      at = @At("RETURN")
   )
   private void rockstar$captureArmMask(
      MatrixStack matrices,
      OrderedRenderCommandQueue queue,
      int light,
      float equipProgress,
      float swingProgress,
      Arm arm,
      CallbackInfo ci
   ) {
      if (EspGlowCaptureRenderer.internalField0276 && !rockstar$decoratingHeldItem && !EspGlowCaptureRenderer.internalField0277 && !ViewModelModule.internalField0277) {
         EspGlowCaptureRenderer.internalMethod01707(arm, matrices);
         ViewModelModule viewModel = rockstar$viewModel();
         if (viewModel != null) {
            viewModel.internalMethod04693(arm, matrices);
         }
      }
   }

   @Inject(
      method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void rockstar$takeOverHandRender(
      float tickProgress,
      MatrixStack matrices,
      OrderedRenderCommandQueue queue,
      ClientPlayerEntity player,
      int light,
      CallbackInfo ci
   ) {
      rockstar$firstPersonItemRendered = false;
      rockstar$tookOverHandRender = false;
      EspGlowCaptureRenderer.internalMethod05095();
      if (!rockstar$handRenderHijacked || !this.rockstar$needsHandRenderEvent()) {
         return;
      }

      rockstar$tookOverHandRender = true;
      float swing = player.getHandSwingProgress(tickProgress);
      Hand preferredHand = player.preferredHand == null ? Hand.MAIN_HAND : player.preferredHand;
      float pitch = player.getLerpedPitch(tickProgress);
      float lastPitch = MathHelper.lerp(tickProgress, player.lastRenderPitch, player.renderPitch);
      float lastYaw = MathHelper.lerp(tickProgress, player.lastRenderYaw, player.renderYaw);
      float yawDelta = (player.getYaw(tickProgress) - lastYaw) % 360.0F;
      matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((player.getPitch(tickProgress) - lastPitch) * 0.1F));
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yawDelta * 0.1F));

      boolean renderMain = true;
      boolean renderOff = true;
      ItemStack main = player.getMainHandStack();
      ItemStack off = player.getOffHandStack();
      if (main.isOf(Items.BOW) || off.isOf(Items.BOW) || main.isOf(Items.CROSSBOW) || off.isOf(Items.CROSSBOW)) {
         if (player.isUsingItem()) {
            ItemStack active = player.getActiveItem();
            Hand activeHand = player.getActiveHand();
            if (active.isOf(Items.BOW) || active.isOf(Items.CROSSBOW)) {
               renderMain = activeHand == Hand.MAIN_HAND;
               renderOff = !renderMain;
            } else if (activeHand == Hand.MAIN_HAND && rockstar$isChargedCrossbow(off)) {
               renderOff = false;
            }
         } else if (rockstar$isChargedCrossbow(main)) {
            renderOff = false;
         }
      }

      if (renderMain) {
         float handSwing = preferredHand == Hand.MAIN_HAND ? swing : 0.0F;
         float equip = this.itemModelManager.getSwapAnimationScale(this.mainHand)
            * (1.0F - MathHelper.lerp(tickProgress, this.lastEquipProgressMainHand, this.equipProgressMainHand));
         this.renderFirstPersonItem(player, tickProgress, pitch, Hand.MAIN_HAND, handSwing, this.mainHand, equip, matrices, queue, light);
      }
      if (renderOff) {
         float handSwing = preferredHand == Hand.OFF_HAND ? swing : 0.0F;
         float equip = this.itemModelManager.getSwapAnimationScale(this.offHand)
            * (1.0F - MathHelper.lerp(tickProgress, this.lastEquipProgressOffHand, this.equipProgressOffHand));
         this.renderFirstPersonItem(player, tickProgress, pitch, Hand.OFF_HAND, handSwing, this.offHand, equip, matrices, queue, light);
      }

      MinecraftClient client = MinecraftClient.getInstance();
      client.gameRenderer.getEntityRenderDispatcher().render();
      client.getBufferBuilders().getEntityVertexConsumers().draw();
      EspGlowCaptureRenderer.internalMethod08038();
      EspGlowCaptureRenderer.internalMethod05102();
      ci.cancel();
   }

   @Inject(
      method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V",
      at = @At("RETURN")
   )
   private void rockstar$detectHijackedHandRender(
      float tickProgress,
      MatrixStack matrices,
      OrderedRenderCommandQueue queue,
      ClientPlayerEntity player,
      int light,
      CallbackInfo ci
   ) {
      EspGlowCaptureRenderer.internalMethod08038();
      EspGlowCaptureRenderer.internalMethod05102();
      if (!rockstar$tookOverHandRender) {
         rockstar$handRenderHijacked = !rockstar$firstPersonItemRendered;
      }
   }

   @Unique
   private void rockstar$popEventMatrix(MatrixStack matrices) {
      if (this.rockstar$eventMatrixPushed) {
         matrices.pop();
         this.rockstar$eventMatrixPushed = false;
      }
   }

   @Unique
   private static ViewModelModule rockstar$viewModel() {
      ModuleManager manager = RockstarClient.getInstance().getModuleManager();
      return manager == null ? null : manager.getModule(ViewModelModule.class);
   }

   @Unique
   private static boolean rockstar$isChargedCrossbow(ItemStack stack) {
      return stack.isOf(Items.CROSSBOW) && CrossbowItem.isCharged(stack);
   }

   @Unique
   private static boolean rockstar$isHeldInFirstPerson(LivingEntity entity, ItemDisplayContext context) {
      return entity instanceof ClientPlayerEntity
         && (context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
   }

   @Unique
   private boolean rockstar$needsHandRenderEvent() {
      ModuleManager manager = RockstarClient.getInstance().getModuleManager();
      if (manager == null) {
         return false;
      }
      ViewModelModule viewModel = manager.getModule(ViewModelModule.class);
      if (viewModel != null && viewModel.isEnabled()) {
         return true;
      }
      SwingAnimationModule swing = manager.getModule(SwingAnimationModule.class);
      return swing != null && swing.isEnabled() && swing.internalMethod02588(this.mainHand);
   }
}
