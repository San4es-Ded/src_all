package ru.prism.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.prism.module.impl.render.HMI;
import ru.prism.module.impl.render.HmiTransforms;

/**
 * Миксин HMI: рисует 3D руку игрока со скином и рукавом, удерживающую предмет,
 * и применяет анимации взмахов, хвата, выпада и осмотра оружия.
 */
@Mixin(HeldItemRenderer.class)
public abstract class HmiHeldItemRendererMixin {

    @Shadow
    private EntityRenderManager entityRenderDispatcher;

    @Shadow
    public abstract void renderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode,
                                    MatrixStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, int light);

    @Unique private float prism$previousSwing;
    @Unique private boolean prism$leftSwing;

    @Inject(
            method = "renderFirstPersonItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void prism$renderFirstPersonItem(AbstractClientPlayerEntity player, float tickProgress, float pitch,
                                             Hand hand, float swingProgress, ItemStack item,
                                             float equipProgress, MatrixStack matrices,
                                             OrderedRenderCommandQueue orderedRenderCommandQueue, int light,
                                             CallbackInfo ci) {
        HMI hmi = HMI.get();
        if (hmi == null || !hmi.isEnabled() || item.isEmpty() || player.isUsingSpyglass()) {
            return;
        }

        UseAction activeUse = item.getUseAction();
        boolean eating = player.isUsingItem() && player.getActiveHand() == hand
                && (activeUse == UseAction.EAT || activeUse == UseAction.DRINK);

        if (player.isUsingItem() && player.getActiveHand() == hand && !eating) {
            return;
        }

        Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
        int side = arm == Arm.RIGHT ? 1 : -1;
        float smoothness = MathHelper.clamp(hmi.smoothness.getValue(), 0.35f, 2.5f);
        float progress = (float) Math.pow(MathHelper.clamp(swingProgress, 0.0f, 1.0f), smoothness);
        float swingRot = progress < 0.6f
                ? MathHelper.sin(MathHelper.clamp(progress, 0.0f, 0.12506f) * 12.56f)
                : MathHelper.sin(MathHelper.clamp(progress, 0.62532f, 0.75038f) * 12.56f);
        float swing = HmiTransforms.easeInOutBack(MathHelper.sin(progress * 3.14f));

        if (hand == Hand.MAIN_HAND && swingProgress > 0.0f
                && (prism$previousSwing <= 0.0f || swingProgress < prism$previousSwing - 0.01f)) {
            prism$leftSwing = !prism$leftSwing;
            hmi.trackMainHandSwing(swingProgress);
        }
        if (hand == Hand.MAIN_HAND) prism$previousSwing = swingProgress;

        boolean sword0 = item.isIn(ItemTags.SWORDS);
        boolean axe = item.isIn(ItemTags.AXES);
        boolean shovel = item.isIn(ItemTags.SHOVELS);
        boolean sword = sword0 || item.isOf(Items.MACE);
        boolean sharpSword = sword && hmi.hmiMode.is("Sharp");
        boolean pouchMode = hmi.hmiMode.is("Pouch");
        boolean pouchWeapon = sword || axe || shovel || item.isIn(ItemTags.PICKAXES);
        boolean pouchAlternate = hmi.alternate.getValue() && prism$leftSwing;
        float pouchStrength = hmi.strength.getValue();

        matrices.push();

        if (hand == Hand.MAIN_HAND) {
            hmi.applyWeaponInspectArm(matrices, side);
        }

        if (eating) {
            prism$applyEatAnimation(player, item, matrices, side, tickProgress);
        } else {
            if (pouchMode) {
                HmiTransforms.applyPouchHandSwing(matrices, side, progress,
                        pouchWeapon, pouchAlternate, pouchStrength, equipProgress);
            } else {
                prism$applyOriginalSwing(matrices, side, swingRot, swing,
                        sword, axe, shovel, sharpSword, pouchAlternate);
            }
        }

        matrices.translate(side, -equipProgress * 0.3f, 0.3f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45.0f * side));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-40.0f * side));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30.0f));
        matrices.scale(0.9f, 0.9f, 0.9f);
        prism$renderSkinArm(player, matrices, orderedRenderCommandQueue, light, side, arm);

        hmi.applyGrip(matrices, arm, item, hand);

        matrices.translate(-0.3f * side, 0.65f, -0.1f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-65.0f * side));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(10.0f));
        prism$applyOriginalItemPose(matrices, item, side,
                swing, swingRot, sword, sharpSword, pouchMode, shovel);

        if (hand == Hand.MAIN_HAND) {
            hmi.applyThrust(matrices, item, hand, arm, swingProgress);
            hmi.applyWeaponInspectItem(matrices, item, hand, side);
        }

        this.renderItem(player, item,
                arm == Arm.RIGHT ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                        : ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
                matrices, orderedRenderCommandQueue, light);
        matrices.pop();
        ci.cancel();
    }

    @Unique
    private void prism$applyEatAnimation(AbstractClientPlayerEntity player, ItemStack itemStack,
                                         MatrixStack poseStack, int side, float frameInterp) {
        float remaining = (float) player.getItemUseTimeLeft() - frameInterp + 1.0f;
        float total = Math.max(1.0f, (float) itemStack.getMaxUseTime(player));
        float used = remaining / total;
        if (used < 0.8f) {
            poseStack.translate(0.0f,
                    MathHelper.abs(MathHelper.cos(remaining / 4.0f * 3.1415927f) * 0.1f), 0.0f);
        }
        float bite = 1.0f - (float) Math.pow(used, 27.0);
        poseStack.translate(bite * 0.6f * side, bite * -0.5f, 0.0f);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(bite * 90.0f * side));
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(bite * 10.0f));
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(bite * 30.0f * side));
    }

    @Unique
    private void prism$renderSkinArm(AbstractClientPlayerEntity player, MatrixStack poseStack,
                                     OrderedRenderCommandQueue submitNodeCollector, int lightCoords,
                                     int side, Arm arm) {
        poseStack.translate(side * 0.64000005f, -0.6f, -0.71999997f);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(side * 45.0f));
        poseStack.translate(side * -1.0f, 3.6f, 3.5f);
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(side * 120.0f));
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(200.0f));
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(side * -135.0f));
        poseStack.translate(side * 5.6f, 0.0f, 0.0f);

        PlayerEntityRenderer<AbstractClientPlayerEntity> renderer = this.entityRenderDispatcher.getPlayerRenderer(player);
        Identifier skin = player.getSkin().body().texturePath();
        if (arm == Arm.RIGHT) {
            renderer.renderRightArm(poseStack, submitNodeCollector, lightCoords, skin,
                    player.isModelPartVisible(PlayerModelPart.RIGHT_SLEEVE));
        } else {
            renderer.renderLeftArm(poseStack, submitNodeCollector, lightCoords, skin,
                    player.isModelPartVisible(PlayerModelPart.LEFT_SLEEVE));
        }
    }

    @Unique
    private void prism$applyOriginalItemPose(MatrixStack poseStack, ItemStack itemStack,
                                             int side, float swing, float swingRot,
                                             boolean sword, boolean sharpSword, boolean pouchMode,
                                             boolean shovel) {
        UseAction use = itemStack.getUseAction();
        boolean block = use == UseAction.BLOCK;
        boolean spear = use == UseAction.SPEAR;
        boolean bow = use == UseAction.BOW;
        boolean handled = itemStack.isIn(ItemTags.SWORDS) || itemStack.isOf(Items.MACE)
                || itemStack.isIn(ItemTags.AXES)
                || itemStack.isIn(ItemTags.PICKAXES) || itemStack.isIn(ItemTags.SHOVELS)
                || itemStack.isIn(ItemTags.HOES) || itemStack.isOf(Items.SHEARS)
                || itemStack.isOf(Items.FISHING_ROD) || itemStack.isOf(Items.CARROT_ON_A_STICK)
                || itemStack.isOf(Items.WARPED_FUNGUS_ON_A_STICK)
                || block || spear || bow || use == UseAction.SPYGLASS;

        boolean placeable = itemStack.getItem() instanceof BlockItem
                && use != UseAction.EAT && use != UseAction.DRINK;

        if (placeable) {
            Block block0 = Block.getBlockFromItem(itemStack.getItem());
            BlockState state = block0.getDefaultState();
            boolean torch = itemStack.isIn(ItemTags.CANDLES) || block0 instanceof TorchBlock
                    || block0 instanceof WallTorchBlock;
            boolean hanging = itemStack.isOf(Items.LANTERN) || itemStack.isOf(Items.SOUL_LANTERN)
                    || itemStack.isIn(ItemTags.HANGING_SIGNS);
            boolean thin = state.isIn(BlockTags.DOORS) || state.isIn(BlockTags.TRAPDOORS)
                    || itemStack.isOf(Items.STRING) || itemStack.isOf(Items.REDSTONE)
                    || itemStack.isOf(Items.LEVER) || itemStack.isOf(Items.TRIPWIRE_HOOK)
                    || state.isIn(BlockTags.RAILS) || state.isIn(BlockTags.CLIMBABLE);

            if (torch || hanging || thin) {
                poseStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(5.0f * side));
                poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(15.0f));
                poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(75.0f * side));
                poseStack.translate(0.0f, -0.05f, -0.1f);
                poseStack.scale(0.7f, 0.7f, 0.7f);
                return;
            }

            poseStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(25.0f * side));
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(5.0f));
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(75.0f * side));
            poseStack.translate(0.2f * side, 0.2f, 0.05f);
            return;
        }

        if (!handled) {
            poseStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(5.0f * side));
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(15.0f));
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(75.0f * side));
            poseStack.translate(0.0f, -0.05f, -0.1f);
            poseStack.scale(0.7f, 0.7f, 0.7f);
            return;
        }

        if (block) {
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(160.0f * side));
            poseStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(-60.0f * side));
            poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-70.0f));
            poseStack.scale(0.75f, 0.75f, 0.75f);
            poseStack.translate(0.15f * side, 0.45f, -0.1f);
            poseStack.translate(0.17f * side, 0.0f, 0.3f);
            poseStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(-90.0f * side));
            return;
        }

        if (spear) {
            poseStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(75.0f * side));
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45.0f * side));
            poseStack.translate(-0.3f * side, 0.0f, 0.0f);
        } else {
            poseStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(75.0f * side));
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(70.0f));
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45.0f * side));
        }

        poseStack.scale(1.2f, 1.2f, 1.2f);

        if (pouchMode && sword) {
            poseStack.translate(0.04f * side, -0.025f, -0.035f);
            poseStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(-7.0f * side));
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(5.0f * side));
        }

        if ((sword || itemStack.isOf(Items.MACE)) && !sharpSword && !pouchMode) {
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-60.0f * swing));
            poseStack.translate(0.0f, 0.1f * swing, -0.1f * swing);
        }

        if (shovel && !pouchMode) {
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80.0f * swingRot));
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30.0f * swing));
        } else if (spear && !pouchMode) {
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-40.0f * swingRot));
        }

        if (bow) {
            poseStack.translate(-0.1f * side, -0.2f, 0.0f);
        }
    }

    @Unique
    private void prism$applyOriginalSwing(MatrixStack poseStack, int side,
                                          float swingRot, float swing, boolean sword,
                                          boolean axe, boolean shovel, boolean sharpSword,
                                          boolean left) {
        if (shovel) {
            poseStack.translate(0.0f, 0.15f * swingRot, -0.25f * swingRot);
            poseStack.translate(0.0f, 0.0f, -0.2f * swing);
            poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(15.0f * swingRot));
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-35.0f * swingRot));
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30.0f * swing));
        } else if (sharpSword) {
            poseStack.translate(0.1f * side * swingRot, 0.1f * swingRot, -0.5f * swing);
            poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-30.0f * swingRot));
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-20.0f * swingRot * side));
            poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(40.0f * swing));
        } else if (sword && left) {
            poseStack.translate(0.8f * side * swingRot, 0.3f * swingRot, -0.5f * swing);
            poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(15.0f * swingRot * side));
            poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-20.0f * swingRot));
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-70.0f * swingRot * side));
            poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(40.0f * swing));
        } else if (sword) {
            poseStack.translate(-0.55f * side * swingRot, -0.8f * swingRot, -0.77f * swing);
            poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(5.0f * swingRot * side));
            poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-30.0f * swingRot));
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(70.0f * swingRot * side));
            poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(50.0f * swing));
        } else if (axe) {
            poseStack.translate(0.8f * side * swingRot, 0.3f * swingRot, -0.5f * swing);
            poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(15.0f * swingRot * side));
            poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-20.0f * swingRot));
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-70.0f * swingRot * side));
            poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(30.0f * swing));
        } else {
            poseStack.translate(0.1f * side * swingRot, 0.1f * swingRot, -0.1f * swing);
            poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-30.0f * swingRot));
            poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-10.0f * swingRot * side));
            poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(40.0f * swing));
            poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(10.0f * swing * side));
        }
    }
}
