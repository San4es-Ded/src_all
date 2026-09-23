package su.sacura.features.modules.impl.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import su.sacura.Sacura;
import su.sacura.events.render.EventHeldItemRenderer;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.settings.impl.ModeSetting;
import su.sacura.features.modules.settings.impl.SliderSetting;
import su.sacura.util.type.ISetting;

@ModuleAnnotations(name = "Swing Animations", category = Category.RENDER)
public class SwingAnimationsModule extends Module {
    private final ModeSetting mode = (new ModeSetting("Мод анимации", "Плавная", new String[] { "Плавная", "Akrien", "Возращение", "Возвращение к себе", "Круговая", "DeadCode", "Кастомная" })).setDescription("Устанавливает выбранную анимацию рук");
    public final SliderSetting slowAnimationSpeed = (new SliderSetting("Скорость анимации", 12.0F, 1.0F, 50.0F, 1.0F)).setDescription("Скорость анимации рук");
    private final SliderSetting corner = (new SliderSetting("Угол", 12.0F, 1.0F, 360.0F, 1.0F)).setDescription("Угол анимации Кастомная").setVisible(() -> Boolean.valueOf(this.mode.is("Кастомная")));
    private final SliderSetting slant = (new SliderSetting("Наклон", 12.0F, 1.0F, 360.0F, 1.0F)).setDescription("Наклон анимации Кастомная").setVisible(() -> Boolean.valueOf(this.mode.is("Кастомная")));

    public SwingAnimationsModule() {
        addSettings(new ISetting[] { (ISetting)this.mode, (ISetting)this.slowAnimationSpeed, (ISetting)this.corner, (ISetting)this.slant });
    }

    private void renderSwordAnimation(MatrixStack matrices, float swingProgress, float equipProgress, Arm arm) {
        float n, g, anim, m, f1;
        // Исправлено: Arm.field_6183 -> Arm.RIGHT
        int i = (arm == Arm.RIGHT) ? 1 : -1;
        switch ((String)this.mode.get()) {
            case "Плавная":
                matrices.translate(0.56F * i, -0.52F, -0.72F);
                applySwingOffset(matrices, arm, swingProgress);
                break;
            case "Akrien":
                if (swingProgress > 0.0F) {
                    float f = MathHelper.sin(MathHelper.sqrt(swingProgress) * 3.1415927F);
                    matrices.translate(0.56F * i, equipProgress * -0.2F - 0.5F, -0.7F);
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((45 * i)));
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f * -85.0F));
                    matrices.translate(-0.1F * i, 0.28F, 0.2F);
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-85.0F));
                    break;
                }
                n = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * 3.1415927F);
                m = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * 6.2831855F);
                f1 = -0.2F * MathHelper.sin(swingProgress * 3.1415927F);
                matrices.translate(n * i, m, f1);
                applyEquipOffset(matrices, arm, equipProgress);
                applySwingOffset(matrices, arm, swingProgress);
                break;
            case "Возращение":
                g = MathHelper.sin(MathHelper.sqrt(swingProgress) * 3.1415927F);
                applyEquipOffset(matrices, arm, 0.0F);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(50.0F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((-30.0F * (1.0F - g) - 30.0F) * i));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(110.0F * i));
                break;
            case "Возвращение к себе":
                anim = (float)Math.sin(swingProgress * 1.5707963267948966D * 2.0D);
                applyEquipOffset(matrices, arm, 0.0F);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((90 * i)));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((-70 * i)));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-100.0F - 60.0F * anim));
                break;
            case "Круговая":
                matrices.translate(0.56F * i, -0.52F, -0.72F);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-swingProgress * 360.0F));
                break;
            case "Кастомная":
                anim = (float)Math.sin(swingProgress * 1.5707963267948966D * 2.0D);
                applyEquipOffset(matrices, arm, 0.0F);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(80.0F));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(((Float)this.corner.get()).floatValue()));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-((Float)this.slant.get()).floatValue() * anim));
                break;
            case "DeadCode":
                anim = (float)Math.sin(swingProgress * 1.5707963267948966D * 2.0D);
                applyEquipOffset(matrices, arm, 0.0F);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45.0F));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(anim * -40.0F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(30.0F));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80.0F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(60.0F));
                break;
        }
    }

    public void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (!player.isSpectator()) {
            // Исправлено: Hand.field_5808 -> Hand.MAIN_HAND
            boolean bl = (hand == Hand.MAIN_HAND);
            // Исправлено: Arm.field_6183 -> Arm.RIGHT
            Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
            matrices.push();
            // Исправлено: Items.field_8399 -> Items.CROSSBOW
            if (item.isOf(Items.CROSSBOW)) {
                boolean bl2 = CrossbowItem.isCharged(item);
                // Исправлено: Arm.field_6183 -> Arm.RIGHT
                boolean bl3 = (arm == Arm.RIGHT);
                int i = bl3 ? 1 : -1;
                if (player.isSneaking() && player.getItemUseTime() > 0 && player.getActiveHand() == hand) {
                    applyEquipOffset(matrices, arm, equipProgress);
                    matrices.translate(i * -0.4785682F, -0.094387F, 0.05731531F);
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-11.935F));
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * 65.3F));
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(i * -9.785F));
                    float f = item.getMaxUseTime((LivingEntity)mc.player) - mc.player.getItemUseTime() - tickDelta + 1.0F;
                    float g = f / CrossbowItem.getPullTime(item, (LivingEntity)mc.player);
                    if (g > 1.0F)
                        g = 1.0F;
                    if (g > 0.1F) {
                        float h = MathHelper.sin((f - 0.1F) * 1.3F);
                        float j = g - 0.1F;
                        float k = h * j;
                        matrices.translate(k * 0.0F, k * 0.004F, k * 0.0F);
                    }
                    matrices.translate(g * 0.0F, g * 0.0F, g * 0.04F);
                    matrices.scale(1.0F, 1.0F, 1.0F + g * 0.2F);
                    matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(i * 45.0F));
                } else {
                    float fx = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * 3.1415927F);
                    float gx = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * 6.2831855F);
                    float h = -0.2F * MathHelper.sin(swingProgress * 3.1415927F);
                    matrices.translate(i * fx, gx, h);
                    applyEquipOffset(matrices, arm, equipProgress);
                    applySwingOffset(matrices, arm, swingProgress);
                    if (bl2 && swingProgress < 0.001F && bl) {
                        matrices.translate(i * -0.641864F, 0.0F, 0.0F);
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * 10.0F));
                    }
                }
                EventHeldItemRenderer event = new EventHeldItemRenderer(hand, item, equipProgress, matrices);
                Sacura.getInstance().getEventBus().post(event);
                // Исправлено: ModelTransformationMode.field_4322 -> ModelTransformationMode.FIRST_PERSON_RIGHT_HAND
                // Исправлено: ModelTransformationMode.field_4321 -> ModelTransformationMode.FIRST_PERSON_LEFT_HAND
                renderItem((LivingEntity)player, item, bl3 ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND, !bl3, matrices, vertexConsumers, light);
            } else {
                // Исправлено: Arm.field_6183 -> Arm.RIGHT
                boolean bl2 = (arm == Arm.RIGHT);
                ViewModelModule viewModel = (ViewModelModule)Sacura.getInstance().getModuleManager().getModule(ViewModelModule.class);
                if (viewModel.enable)
                    if (bl2) {
                        matrices.translate(((Float)viewModel.right_x.get()).floatValue(), ((Float)viewModel.right_y.get()).floatValue(), ((Float)viewModel.right_z.get()).floatValue());
                    } else {
                        matrices.translate(-((Float)viewModel.left_x.get()).floatValue(), ((Float)viewModel.left_y.get()).floatValue(), ((Float)viewModel.left_z.get()).floatValue());
                    }
                if (player.isSneaking() && player.getItemUseTime() > 0 && player.getActiveHand() == hand) {
                    float mx, fxx, m, fx;
                    int l = bl2 ? 1 : -1;
                    switch (item.getUseAction()) {
                        // Исправлено: field_8952 -> UseAction.NONE
                        case NONE:
                            // Исправлено: field_8949 -> UseAction.BLOCK
                        case BLOCK:
                            applyEquipOffset(matrices, arm, equipProgress);
                            break;
                        // Исправлено: field_8950 -> UseAction.DRINK
                        // Исправлено: field_8946 -> UseAction.EAT
                        case DRINK:
                        case EAT:
                            applyEatOrDrinkTransformation(matrices, tickDelta, arm, item);
                            applyEquipOffset(matrices, arm, equipProgress);
                            break;
                        // Исправлено: field_8953 -> UseAction.BOW
                        case BOW:
                            applyEquipOffset(matrices, arm, equipProgress);
                            matrices.translate(l * -0.2785682F, 0.18344387F, 0.15731531F);
                            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-13.935F));
                            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 35.3F));
                            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(l * -9.785F));
                            mx = item.getMaxUseTime((LivingEntity)mc.player) - mc.player.getItemUseTime() - tickDelta + 1.0F;
                            fxx = mx / 20.0F;
                            fxx = (fxx * fxx + fxx * 2.0F) / 3.0F;
                            if (fxx > 1.0F)
                                fxx = 1.0F;
                            if (fxx > 0.1F) {
                                float gx = MathHelper.sin((mx - 0.1F) * 1.3F);
                                float h = fxx - 0.1F;
                                float j = gx * h;
                                matrices.translate(j * 0.0F, j * 0.004F, j * 0.0F);
                            }
                            matrices.translate(fxx * 0.0F, fxx * 0.0F, fxx * 0.04F);
                            matrices.scale(1.0F, 1.0F, 1.0F + fxx * 0.2F);
                            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(l * 45.0F));
                            break;
                        // Исправлено: field_8951 -> UseAction.SPEAR
                        case SPEAR:
                            applyEquipOffset(matrices, arm, equipProgress);
                            matrices.translate(l * -0.5F, 0.7F, 0.1F);
                            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-55.0F));
                            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 35.3F));
                            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(l * -9.785F));
                            m = item.getMaxUseTime((LivingEntity)mc.player) - mc.player.getItemUseTime() - tickDelta + 1.0F;
                            fx = m / 10.0F;
                            if (fx > 1.0F)
                                fx = 1.0F;
                            if (fx > 0.1F) {
                                float gx = MathHelper.sin((m - 0.1F) * 1.3F);
                                float h = fx - 0.1F;
                                float j = gx * h;
                                matrices.translate(j * 0.0F, j * 0.004F, j * 0.0F);
                            }
                            matrices.translate(0.0F, 0.0F, fx * 0.2F);
                            matrices.scale(1.0F, 1.0F, 1.0F + fx * 0.2F);
                            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(l * 45.0F));
                            break;
                        // Исправлено: field_42717 -> UseAction.BRUSH
                        case BRUSH:
                            applyBrushTransformation(matrices, tickDelta, arm, item, equipProgress);
                            break;
                    }
                } else if (player.isUsingItem()) {
                    applyEquipOffset(matrices, arm, equipProgress);
                    int l = bl2 ? 1 : -1;
                    matrices.translate(l * -0.4F, 0.8F, 0.3F);
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 65.0F));
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(l * -85.0F));
                } else if (arm == mc.player.getMainArm() && this.enable) {
                    renderSwordAnimation(matrices, swingProgress, equipProgress, arm);
                } else {
                    float n = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * 3.1415927F);
                    float mxx = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * 6.2831855F);
                    float fxxx = -0.2F * MathHelper.sin(swingProgress * 3.1415927F);
                    int o = bl2 ? 1 : -1;
                    matrices.translate(o * n, mxx, fxxx);
                    applyEquipOffset(matrices, arm, equipProgress);
                    applySwingOffset(matrices, arm, swingProgress);
                }
                EventHeldItemRenderer event = new EventHeldItemRenderer(hand, item, equipProgress, matrices);
                Sacura.getInstance().getEventBus().post(event);
                // Исправлено: ModelTransformationMode.field_4322 -> ModelTransformationMode.FIRST_PERSON_RIGHT_HAND
                // Исправлено: ModelTransformationMode.field_4321 -> ModelTransformationMode.FIRST_PERSON_LEFT_HAND
                renderItem((LivingEntity)player, item, bl2 ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND, !bl2, matrices, vertexConsumers, light);
            }
            matrices.pop();
        }
    }

    private void applyBrushTransformation(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, float equipProgress) {
        applyEquipOffset(matrices, arm, equipProgress);
        float f = (mc.player.getItemUseTime() % 10);
        float g = f - tickDelta + 1.0F;
        float h = 1.0F - g / 10.0F;
        float n = -15.0F + 75.0F * MathHelper.cos(h * 2.0F * 3.1415927F);
        // Исправлено: Arm.field_6183 -> Arm.RIGHT
        if (arm != Arm.RIGHT) {
            matrices.translate(0.1D, 0.83D, 0.35D);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(n));
            matrices.translate(-0.3D, 0.22D, 0.35D);
        } else {
            matrices.translate(-0.25D, 0.22D, 0.35D);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(0.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(n));
        }
    }

    private void applyEatOrDrinkTransformation(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack) {
        float f = mc.player.getItemUseTime() - tickDelta + 1.0F;
        float g = f / stack.getMaxUseTime((LivingEntity)mc.player);
        if (g < 0.8F) {
            float f1 = MathHelper.abs(MathHelper.cos(f / 4.0F * 3.1415927F) * 0.1F);
            matrices.translate(0.0F, f1, 0.0F);
        }
        float h = 1.0F - (float)Math.pow(g, 27.0D);
        // Исправлено: Arm.field_6183 -> Arm.RIGHT
        int i = (arm == Arm.RIGHT) ? 1 : -1;
        matrices.translate(h * 0.6F * i, h * -0.5F, h * 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * h * 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(h * 10.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(i * h * 30.0F));
    }

    private void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress) {
        // Исправлено: Arm.field_6183 -> Arm.RIGHT
        int i = (arm == Arm.RIGHT) ? 1 : -1;
        matrices.translate(i * 0.56F, -0.52F + equipProgress * -0.6F, -0.72F);
    }

    private void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress) {
        // Исправлено: Arm.field_6183 -> Arm.RIGHT
        int i = (arm == Arm.RIGHT) ? 1 : -1;
        float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * (45.0F + f * -20.0F)));
        float g = MathHelper.sin(MathHelper.sqrt(swingProgress) * 3.1415927F);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(i * g * -20.0F));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * -80.0F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * -45.0F));
    }

    public void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (!stack.isEmpty())
            mc.getItemRenderer().renderItem(entity, stack, renderMode, leftHanded, matrices, vertexConsumers, entity.getWorld(), light, OverlayTexture.DEFAULT_UV, entity.getId() + renderMode.ordinal());
    }
}