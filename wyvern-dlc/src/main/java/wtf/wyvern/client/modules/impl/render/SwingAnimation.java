package wtf.wyvern.client.modules.impl.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.impl.combat.Aura;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;

import java.util.function.Supplier;

import static net.minecraft.util.math.RotationAxis.*;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "SwingAnimation", category = Category.RENDER, description = "Изменяет анимацию взмаха рукой")
public final class SwingAnimation extends Module {
    public static final SwingAnimation INSTANCE = new SwingAnimation();

    public static final ModeSetting swordAnim = new ModeSetting("Тип", "HMI",
            "Мод 1", "Мод 2", "Мод 3", "Мод 4", "Мод 5", "Мод 6", "Мод 7", "Slant", "Fade");

    public final SliderSetting angle = new SliderSetting("Угол", 100, 0, 360, 1, new Supplier<Boolean>() {
        @Override
        public Boolean get() {
            return swordAnim.is("Мод 2") || swordAnim.is("Мод 4");
        }
    });

    public final SliderSetting swipePower = new SliderSetting("Сила взмаха", 8, 1, 100, 1, new Supplier<Boolean>() {
        @Override
        public Boolean get() {
            return swordAnim.is("Мод 1") || swordAnim.is("Мод 2") || swordAnim.is("Мод 3") || swordAnim.is("Мод 4") || swordAnim.is("Мод 6") || swordAnim.is("Fade");
        }
    });

    public static final SliderSetting swipeSpeed = new SliderSetting("Плавность взмаха", 11, 1, 20, 1);
    public static final BooleanSetting onlyAura = new BooleanSetting("Только с Aura", false);

    // HMI mode fields
    public boolean swimmingAnimation = true;
    public boolean climbAndCrawl = true;
    public boolean mb3DCompat = false;

    /**
     * Checks whether the swing animation should be active for the given hand.
     * Non-HMI modes only animate the main hand.
     */
    @FastNative
    public boolean shouldAnimate(Hand hand) {
        if (!swordAnim.is("HMI")) {
            return hand == Hand.MAIN_HAND;
        }
        return true;
    }

    /**
     * Whether SwingAnimation should replace the vanilla animation right now.
     * "Only with Aura" must fall back to vanilla instead of swallowing swings.
     */
    @FastNative
    public boolean isAnimationActive() {
        if (!isEnabled()) {
            return false;
        }
        return !onlyAura.isEnabled()
                || Aura.INSTANCE.isEnabled() && Aura.INSTANCE.getTarget() != null;
    }

    public void renderSwordAnimation(MatrixStack matrixStack, float swingProgress, float equipProgress, Arm arm) {
        if (!isAnimationActive()) {
            return;
        }

        float pi = (float) Math.PI;
        float anim = MathHelper.sin(MathHelper.sqrt(swingProgress) * pi);
        String mode = swordAnim.get();

        boolean isLeft = arm == Arm.LEFT;
        int sideFactor = isLeft ? -1 : 1;

        matrixStack.translate(0.56F * sideFactor, -0.52F, -0.72F);

        switch (mode) {
                case "Мод 1": {
                    float swingSqrt = MathHelper.sqrt(swingProgress);
                    matrixStack.multiply(POSITIVE_Y.rotationDegrees(sideFactor * (20.0F + MathHelper.sin(swingProgress * swingProgress * pi) / 4.0F * -10.0F)));
                    matrixStack.multiply(POSITIVE_Z.rotationDegrees(sideFactor * MathHelper.sin(swingSqrt * pi) * -20.0F));
                    matrixStack.multiply(POSITIVE_X.rotationDegrees(MathHelper.sin(swingSqrt * pi) * -swipePower.getCurrent() * 10.0F));
                    matrixStack.multiply(POSITIVE_Y.rotationDegrees(-sideFactor * 45.0F));
                    break;
                }
                case "Мод 2": {
                    matrixStack.translate(0.0D, 0.15D, -0.3D);
                    matrixStack.multiply(POSITIVE_Y.rotationDegrees(isLeft ? -90.0F : 90.0F));
                    matrixStack.multiply(POSITIVE_Z.rotationDegrees(isLeft ? 60.0F : -60.0F));
                    matrixStack.multiply(POSITIVE_X.rotationDegrees(-angle.getCurrent() - swipePower.getCurrent() * 10.0F * anim));
                    break;
                }
                case "Мод 3": {
                    matrixStack.multiply(POSITIVE_X.rotationDegrees(swingProgress * pi - swipePower.getCurrent() * 10.0F * anim));
                    break;
                }
                case "Мод 4": {
                    matrixStack.translate(0.0D, 0.15D, -0.3D);
                    matrixStack.multiply(POSITIVE_Y.rotationDegrees(isLeft ? -70.0F : 70.0F));
                    matrixStack.multiply(POSITIVE_Z.rotationDegrees(isLeft ? 30.0F : -30.0F));
                    matrixStack.scale(0.9F, 0.9F, 0.9F);
                    matrixStack.multiply(POSITIVE_X.rotationDegrees(-angle.getCurrent() - swipePower.getCurrent() * 10.0F * anim));
                    break;
                }
                case "Мод 5": {
                    matrixStack.multiply(POSITIVE_Y.rotationDegrees(sideFactor * 45.0F));
                    matrixStack.multiply(POSITIVE_X.rotationDegrees(anim * -20.0F));
                    matrixStack.multiply(POSITIVE_Z.rotationDegrees(sideFactor * anim * -20.0F));
                    matrixStack.multiply(POSITIVE_X.rotationDegrees(anim * -80.0F));
                    matrixStack.translate(sideFactor * 0.4F, 0.2F, 0.2F);
                    matrixStack.translate(sideFactor * -0.5F, 0.08F, 0.0F);
                    matrixStack.multiply(POSITIVE_Y.rotationDegrees(sideFactor * 20.0F));
                    matrixStack.multiply(POSITIVE_X.rotationDegrees(-80.0F));
                    matrixStack.multiply(POSITIVE_Y.rotationDegrees(sideFactor * 20.0F));
                    break;
                }
                case "Мод 6": {
                    float f = MathHelper.sin(swingProgress * swingProgress * pi);
                    matrixStack.multiply(POSITIVE_Y.rotationDegrees(sideFactor * (45.0F + f * -20.0F)));
                    float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * pi);
                    matrixStack.multiply(POSITIVE_Z.rotationDegrees(sideFactor * f1 * -20.0F));
                    matrixStack.multiply(POSITIVE_X.rotationDegrees(f1 * -swipePower.getCurrent() * 10.0F));
                    matrixStack.multiply(POSITIVE_Y.rotationDegrees(sideFactor * -45.0F));
                    break;
                }
                case "Мод 7": {
                    matrixStack.translate(sideFactor * (0.4F - anim * 0.3F), 0.0D, -anim * 0.2F);
                    matrixStack.multiply(POSITIVE_Y.rotationDegrees(sideFactor * 90.0F));
                    matrixStack.multiply(POSITIVE_Z.rotationDegrees(sideFactor * -30.0F));
                    matrixStack.multiply(POSITIVE_X.rotationDegrees(-70.0F - 30.0F * anim));
                    break;
                }
                case "Slant": {
                    matrixStack.translate(0.0D, 0.0D, -0.3D * anim);
                    matrixStack.multiply(POSITIVE_X.rotationDegrees(anim * -35.0F));
                    matrixStack.multiply(POSITIVE_Z.rotationDegrees(anim * 35.0F));
                    break;
                }
                case "Fade": {
                    float f2 = MathHelper.sin(swingProgress * swingProgress * pi);
                    matrixStack.multiply(POSITIVE_Y.rotationDegrees(sideFactor * (45.0F + f2 * -5.0F)));
                    float f13 = MathHelper.sin(MathHelper.sqrt(swingProgress * swingProgress) * pi);
                    matrixStack.multiply(POSITIVE_Z.rotationDegrees(sideFactor * f13 * -20.0F));
                    matrixStack.multiply(POSITIVE_X.rotationDegrees(f13 * -(swipePower.getCurrent() * 8.0F)));
                    matrixStack.multiply(POSITIVE_Y.rotationDegrees(sideFactor * -45.0F));
                    break;
                }
                case "HMI": {
                    // HMI mode is handled separately in HeldItemRendererMixin
                    break;
                }
        }

    }
}
