package ru.prism.module.impl.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public final class HmiTransforms {
    private static final float PIVOT_X = 0.30F;
    private static final float PIVOT_Y = -0.40F;
    private static final float PIVOT_Z = 0.0F;

    private HmiTransforms() {}

    public static void applySwing(MatrixStack matrices, Arm arm, float progress,
                                  String style, float strength, float corner, float slant) {
        int i = arm == Arm.RIGHT ? 1 : -1;
        float sin1 = MathHelper.sin(progress * progress * (float) Math.PI);
        float sin2 = MathHelper.sin(MathHelper.sqrt(progress) * (float) Math.PI);
        switch (style) {
            case "Down" -> {
                matrices.translate(i * .56f, -.32f, -.72f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(76 * i));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(sin2 * -5 * strength));
                matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(sin2 * -100 * strength));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(sin2 * -155 * strength));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-100));
            }
            case "Poke" -> {
                float anim = (float) Math.sin(progress * Math.PI);
                matrices.translate(i * .56f, -.52f, -.72f);
                matrices.translate(0, 0, (strength / 3f) * -anim);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(75 * i));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((-75 * (strength / 4f) * anim - 60) * i));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-75));
            }
            case "Static" -> {
                matrices.translate(i * .56f, -.42f, -.72f);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(sin2 * -60 * strength));
                matrices.translate(0, -.1, 0);
            }
            case "Feast" -> {
                matrices.translate(i * .56f, -.32f, -.72f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(30 * i));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(sin2 * 75 * i * strength));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(sin2 * -65 * strength));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(30 * i));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(35 * i));
            }
            case "Akrien" -> {
                matrices.translate(i * .65f, -.32f, -.72f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(76 * i));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(sin2 * -5 * strength));
                matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(sin2 * -100 * strength));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(sin2 * -155 * strength));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-100));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(sin2 * 25 * strength));
                matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(sin2 * -25 * strength));
                matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(sin1 * 15 * strength));
                matrices.translate(sin2 * .18f * strength, sin2 * .59f * strength, 0);
            }
            case "Block" -> {
                float g = MathHelper.sin(MathHelper.sqrt(progress) * (float) Math.PI);
                matrices.translate(.56f * i, -.5f, -.7f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45 * i));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * -85 * strength));
                matrices.translate(-.1f * i, .28f, .2f);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-85));
            }
            case "ToBack" -> {
                float g = MathHelper.sin(MathHelper.sqrt(progress) * (float) Math.PI);
                matrices.translate(.65f * i, -.45f, -.9f);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(50));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((-30 * (1 - g * strength) - 30) * i));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(110 * i));
            }
            case "SelfBack" -> {
                float anim = (float) Math.sin(progress * Math.PI);
                matrices.translate(.65f * i, -.3f, -.8f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90 * i));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-70 * i));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-100 - 60 * strength * anim));
            }
            case "Break" -> {
                matrices.translate(.66f * i, -.3f, -.38f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270 * i));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(sin2 * 10 * strength));
                matrices.scale(.5f, .5f, .5f);
                matrices.translate(-.1f * i, .2f, 0);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-10 * i));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-105 * i));
            }
            case "DropDown" -> {
                float anim = (float) Math.sin(progress * Math.PI);
                equip(matrices, i, 0);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(80));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(corner));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-slant * anim * strength));
            }
            case "Pander" -> {
                float anim = MathHelper.sin(progress * (float) Math.PI);
                matrices.translate(i * .56f, -.52f, -.72f);
                matrices.translate((.3f - anim * .15f) * i, .2f, -.15f - anim * .13f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((76 - 10 * anim) * i));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((-16 - 8 * anim) * i));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-83 - 26 * anim));
            }
            case "Slant" -> {
                float anim = (float) Math.sin(progress * Math.PI);
                float rotate = 35 * strength;
                matrices.translate(i * .56f, -.52f, -.72f);
                matrices.translate(0, 0, -.3f * anim * strength);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(anim * -rotate));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(anim * rotate));
            }
            case "Smooth" -> smooth(matrices, i, progress, strength);
            default -> smooth(matrices, i, progress, strength);
        }
    }

    public static void applyPrettyIdle(MatrixStack matrices, Arm arm, ItemStack stack,
                                       float age, float moveSpeed, boolean sharpMode) {
        int i = arm == Arm.RIGHT ? 1 : -1;
        float breathe = MathHelper.sin(age * 0.10f) * 0.007f;
        float walk = MathHelper.sin(age * 0.65f) * Math.min(moveSpeed * 0.28f, 0.035f);
        matrices.translate(i * 0.035f, breathe + walk, -0.075f);

        boolean sword = !stack.isEmpty() && stack.isIn(ItemTags.SWORDS);
        boolean pickaxe = !stack.isEmpty() && stack.isIn(ItemTags.PICKAXES);
        if (sword && sharpMode) {
            matrices.translate(i * 0.10f, -0.055f, -0.16f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-14.0f * i));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(7.0f));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(11.0f * i));
        } else if (sword) {
            matrices.translate(i * 0.055f, -0.025f, -0.10f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-7.0f * i));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(5.0f * i));
        } else if (pickaxe) {
            matrices.translate(i * 0.04f, -0.035f, -0.08f);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(7.0f * i));
        }
    }

    public static void applyPrettyHands(MatrixStack matrices, Arm arm, float progress,
                                        ItemStack stack, float smoothness,
                                        String mode, boolean alternate, float intensity) {
        int i = arm == Arm.RIGHT ? 1 : -1;
        float p = (float) Math.pow(MathHelper.clamp(progress, 0.0f, 1.0f),
                MathHelper.clamp(smoothness, 0.35f, 2.5f));
        float swingRot = p < 0.6f
                ? MathHelper.sin(MathHelper.clamp(p, 0.0f, 0.12506f) * 12.56f)
                : MathHelper.sin(MathHelper.clamp(p, 0.62532f, 0.75038f) * 12.56f);
        float swing = easeInOutBack(MathHelper.sin(p * 3.14f));
        boolean sword = !stack.isEmpty() && stack.isIn(ItemTags.SWORDS);
        boolean pickaxe = !stack.isEmpty() && stack.isIn(ItemTags.PICKAXES);
        boolean sharpMode = "Sharp".equals(mode);

        matrices.translate(i * 0.56f, -0.52f, -0.72f);
        if ("Pouch".equals(mode)) {
            boolean weapon = sword || pickaxe || (!stack.isEmpty()
                    && (stack.isIn(ItemTags.AXES) || stack.isIn(ItemTags.SHOVELS)
                    || stack.isOf(Items.MACE)));
            applyPouchSwing(matrices, i, p, weapon, alternate, intensity);
            return;
        }

        if (sharpMode && sword) {
            matrices.translate(0.1f * i * swingRot, 0.1f * swingRot, -0.5f * swing);
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-30.0f * swingRot));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-20.0f * swingRot * i));
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(40.0f * swing));
        } else if (sword && alternate) {
            matrices.translate(0.8f * i * swingRot, 0.3f * swingRot, -0.5f * swing);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(15.0f * swingRot * i));
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-20.0f * swingRot));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-70.0f * swingRot * i));
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(40.0f * swing));
        } else if (sword) {
            matrices.translate(-0.55f * i * swingRot, -0.8f * swingRot, -0.77f * swing);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(5.0f * swingRot * i));
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-30.0f * swingRot));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(70.0f * swingRot * i));
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(50.0f * swing));
        } else if (pickaxe) {
            matrices.translate(0.1f * i * swingRot, 0.1f * swingRot, -0.5f * swing);
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-30.0f * swingRot));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-20.0f * swingRot * i));
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(40.0f * swing));
        } else {
            matrices.translate(0.1f * i * swingRot, 0.1f * swingRot, -0.1f * swing);
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-30.0f * swingRot));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-10.0f * swingRot * i));
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(40.0f * swing));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(10.0f * swing * i));
        }
    }

    public static void applyPouchHandSwing(MatrixStack matrices, int side, float progress,
                                           boolean weapon, boolean alternate, float intensity,
                                           float inverseArmHeight) {
        if (progress <= 0.0f || progress >= 1.0f) return;
        float pivotX = side * 0.545234f;
        float pivotY = -0.276085f - inverseArmHeight * 0.3f;
        float pivotZ = -0.803617f;
        matrices.translate(pivotX, pivotY, pivotZ);
        applyPouchSwing(matrices, side, progress, weapon, alternate, intensity);
        matrices.translate(-pivotX, -pivotY, -pivotZ);
    }

    public static void applyWeaponInspect(MatrixStack matrices, int side,
                                          float progress, int variant) {
        if (variant != 1) {
            applyTwoSideInspect(matrices, side, progress);
        }
    }

    private static void applyTwoSideInspect(MatrixStack matrices, int side, float progress) {
        float p = MathHelper.clamp(progress, 0.0F, 1.0F);
        float enter = smoothStep(0.0F, 0.16F, p);
        float exit = 1.0F - smoothStep(0.86F, 1.0F, p);
        float envelope = enter * exit;
        if (envelope <= 0.0001F) return;

        float rightLook = smoothStep(0.07F, 0.24F, p)
                * (1.0F - smoothStep(0.37F, 0.51F, p));
        float leftLook = smoothStep(0.39F, 0.57F, p)
                * (1.0F - smoothStep(0.77F, 0.94F, p));

        matrices.translate(-0.22F * side * envelope,
                0.075F * envelope, -0.19F * envelope);

        float pivotX = 0.56F * side;
        float pivotY = -0.52F;
        float pivotZ = -0.72F;
        matrices.translate(pivotX, pivotY, pivotZ);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(
                -side * (15.0F * envelope + 10.0F * rightLook - 8.0F * leftLook)));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(
                side * (11.0F * envelope + 57.0F * rightLook - 64.0F * leftLook)));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(
                10.0F * envelope + 7.0F * (rightLook + leftLook)));
        matrices.translate(-pivotX, -pivotY, -pivotZ);
    }

    public static void applyKarambitItemSpin(MatrixStack matrices, int side, float progress) {
        float p = MathHelper.clamp(progress, 0.0F, 1.0F);
        float spin = smoothStep(0.08F, 0.78F, p);
        float flourish = MathHelper.sin(spin * (float) Math.PI);

        matrices.translate(0.018F * side * flourish,
                0.035F * flourish, -0.025F * flourish);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-360.0F * side * spin));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(
                side * 20.0F * flourish));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(8.0F * flourish));
    }

    public static void applyPouchSwing(MatrixStack matrices, int side, float progress,
                                       boolean weapon, boolean alternate, float intensity) {
        float p = MathHelper.clamp(progress, 0.0f, 1.0f);
        if (p <= 0.0f || p >= 1.0f) return;
        float strength = MathHelper.clamp(intensity, 0.55f, 1.55f) * (weapon ? 1.0f : 0.65f);
        float direction = alternate ? -1.0f : 1.0f;

        float fold = smoothStep(0.0f, 0.30f, p)
                * (1.0f - smoothStep(0.48f, 1.0f, p));
        float press = smoothStep(0.04f, 0.36f, p)
                * (1.0f - smoothStep(0.50f, 1.0f, p));

        matrices.translate(-side * 0.10f * fold * strength,
                -0.035f * fold * strength, -0.055f * press * strength);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-side * direction * 72.0f * fold * strength));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(side * direction * 12.0f * press * strength));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-18.0f * press * strength));
    }

    public static void applyGrip(MatrixStack matrices, int side, float extend, float slide,
                                 float out, float forward, float tilt, float turn,
                                 float roll, float scale) {
        if (extend != 0.0f) {
            matrices.translate(0.32f * side * extend, 0.0f, -0.95f * extend);
        }
        if (slide != 0.0f || out != 0.0f || forward != 0.0f) {
            matrices.translate(out * side, slide, -forward);
        }

        if (tilt != 0.0f) matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(tilt));
        if (turn != 0.0f) matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(turn * side));
        if (roll != 0.0f) matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(roll * side));
        if (Math.abs(scale - 1.0f) > 1.0E-4f) matrices.scale(scale, scale, scale);
    }

    public static boolean heldAlongTheFist(ItemStack item) {
        if (item == null || item.isEmpty()) return false;
        return item.isIn(ItemTags.SWORDS) || item.isIn(ItemTags.AXES) || item.isIn(ItemTags.PICKAXES)
                || item.isIn(ItemTags.SHOVELS) || item.isIn(ItemTags.HOES)
                || item.isOf(Items.MACE) || item.isOf(Items.TRIDENT)
                || item.isOf(Items.FISHING_ROD) || item.isOf(Items.CARROT_ON_A_STICK)
                || item.isOf(Items.WARPED_FUNGUS_ON_A_STICK) || item.isOf(Items.STICK)
                || item.isOf(Items.BLAZE_ROD) || item.isOf(Items.BREEZE_ROD)
                || item.isOf(Items.BONE) || item.isOf(Items.DEBUG_STICK);
    }

    public static void applyThrust(MatrixStack matrices, ItemStack stack,
                                   Arm arm, float mainSwingProgress,
                                   float reachSetting, float angleSetting, float centerSetting) {
        if (mainSwingProgress <= 0.0F || !isRapierWeapon(stack)) return;

        float thrust = rapierTravel(mainSwingProgress);
        float ready = rapierReady(mainSwingProgress);

        int side = arm == Arm.RIGHT ? 1 : -1;

        float reach = MathHelper.clamp(reachSetting, 0.78F, 1.25F);
        float center = MathHelper.clamp(centerSetting, 0.44F, 0.70F);
        float pitch = MathHelper.clamp(angleSetting, 28.0F, 48.0F);

        matrices.translate(
                (0.018F * ready - center * thrust) * side,
                -0.050F * thrust - 0.010F * ready,
                -reach * thrust + 0.045F * ready);

        matrices.translate(PIVOT_X * side, PIVOT_Y, PIVOT_Z);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(3.0F * ready - pitch * thrust));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((4.0F * ready - 10.0F * thrust) * side));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((6.0F * ready - 8.0F * thrust) * side));
        matrices.translate(-PIVOT_X * side, -PIVOT_Y, -PIVOT_Z);
    }

    private static float rapierTravel(float progress) {
        float phase = MathHelper.clamp(progress, 0.0F, 1.0F);
        if (phase < 0.07F) return 0.0F;
        if (phase < 0.25F) {
            float out = (phase - 0.07F) / 0.18F;
            float rest = 1.0F - out;
            return 1.0F - rest * rest * rest * rest;
        }
        if (phase < 0.43F) return 1.0F;
        float back = MathHelper.clamp((phase - 0.43F) / 0.50F, 0.0F, 1.0F);
        back = back * back * (3.0F - 2.0F * back);
        return 1.0F - back;
    }

    private static float rapierReady(float progress) {
        float phase = MathHelper.clamp(progress / 0.16F, 0.0F, 1.0F);
        return MathHelper.sin(phase * (float) Math.PI);
    }

    public static boolean isRapierWeapon(ItemStack stack) {
        return stack != null && !stack.isEmpty()
                && (stack.isIn(ItemTags.SWORDS) || stack.isOf(Items.TRIDENT));
    }

    public static float easeInOutBack(float x) {
        float c1 = 1.70158f;
        float c2 = c1 * 1.525f;
        return x < 0.5f
                ? (float) (Math.pow(2.0f * x, 2.0) * ((c2 + 1.0f) * 2.0f * x - c2) / 2.0)
                : (float) ((Math.pow(2.0f * x - 2.0f, 2.0)
                * ((c2 + 1.0f) * (x * 2.0f - 2.0f) + c2) + 2.0) / 2.0);
    }

    public static float smoothStep(float edge0, float edge1, float value) {
        float t = MathHelper.clamp((value - edge0) / (edge1 - edge0), 0.0f, 1.0f);
        return t * t * (3.0f - 2.0f * t);
    }

    private static void equip(MatrixStack m, int i, float equip) {
        m.translate(i * .56f, -.52f + equip * -.6f, -.72f);
    }

    private static void smooth(MatrixStack m, int i, float p, float strength) {
        float f = MathHelper.sin(p * p * (float) Math.PI);
        float g = MathHelper.sin(MathHelper.sqrt(p) * (float) Math.PI);
        m.translate(.56f * i, -.52f, -.72f);
        m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * (45 + f * -20 * strength)));
        m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(i * g * -20 * strength));
        m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * -80 * strength));
        m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * -45));
    }
}
