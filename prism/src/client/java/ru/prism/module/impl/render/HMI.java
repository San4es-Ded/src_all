package ru.prism.module.impl.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;
import ru.prism.manager.event_impl.EventKey;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BindSetting;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.DelimiterSetting;
import ru.prism.module.api.settings.impl.ModeSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "HMI",
        category = Category.VISUALS,
        desc = "3D руки со скином игрока и аутентичные анимации взмахов."
)
public class HMI extends Module {

    public static HMI get() {
        return Instance.get(HMI.class);
    }

    public final ModeSetting hmiMode = new ModeSetting(this, "Режим", "HMI", "Classic", "Sharp", "Pouch");
    public final SliderSetting smoothness = new SliderSetting(this, "Плавность анимации", 1.0F, 0.35F, 2.5F, 0.05F);
    public final SliderSetting strength = new SliderSetting(this, "Сила Pouch взмаха", 1.0F, 0.55F, 1.55F, 0.05F)
            .setVisible(() -> hmiMode.is("Pouch"));
    public final BooleanSetting alternate = new BooleanSetting(this, "Чередовать взмахи", true);

    public final DelimiterSetting gripHeader = new DelimiterSetting(this, "Хват (Grip)");
    public final SliderSetting gripSlide = new SliderSetting(this, "Grip Slide", 0.0F, -0.2F, 0.4F, 0.01F);
    public final SliderSetting gripForward = new SliderSetting(this, "Grip Forward", 0.0F, -0.2F, 0.5F, 0.01F);
    public final SliderSetting gripOut = new SliderSetting(this, "Grip Out", 0.0F, -0.25F, 0.25F, 0.01F);
    public final SliderSetting gripExtend = new SliderSetting(this, "Grip Extend", 0.0F, -0.3F, 1.2F, 0.02F);
    public final SliderSetting gripRoll = new SliderSetting(this, "Grip Roll", 0.0F, -180.0F, 180.0F, 5.0F);
    public final SliderSetting gripTilt = new SliderSetting(this, "Grip Tilt", 0.0F, -90.0F, 90.0F, 5.0F);
    public final SliderSetting gripTurn = new SliderSetting(this, "Grip Turn", 0.0F, -90.0F, 90.0F, 5.0F);
    public final SliderSetting itemScale = new SliderSetting(this, "Размер предмета", 1.0F, 0.4F, 1.5F, 0.05F);

    public final DelimiterSetting thrustHeader = new DelimiterSetting(this, "Рапирный выпад");
    public final BooleanSetting thrustEnabled = new BooleanSetting(this, "Рапира (каждый 3-й)", true);
    public final SliderSetting thrustReach = new SliderSetting(this, "Дальность выпада", 0.90F, 0.78F, 1.25F, 0.01F)
            .setVisible(thrustEnabled::getValue);
    public final SliderSetting thrustAngle = new SliderSetting(this, "Угол выпада", 38.0F, 28.0F, 48.0F, 1.0F)
            .setVisible(thrustEnabled::getValue);
    public final SliderSetting thrustCenter = new SliderSetting(this, "Центрирование", 0.52F, 0.44F, 0.70F, 0.01F)
            .setVisible(thrustEnabled::getValue);

    public final DelimiterSetting inspectHeader = new DelimiterSetting(this, "Осмотр оружия");
    public final BindSetting weaponInspectKey = new BindSetting(this, "Клавиша осмотра", GLFW.GLFW_KEY_V);
    public final SliderSetting weaponInspectTime = new SliderSetting(this, "Время осмотра", 1.35F, 0.75F, 2.5F, 0.05F);
    public final ModeSetting weaponInspectStyle = new ModeSetting(this, "Стиль осмотра", "Поочерёдный", "Поочерёдный", "Осмотр с двух сторон", "Вращение 360");

    private long inspectStartedNanos;
    private int inspectVariant = -1;
    private int comboIndex;
    private boolean thrustHit;
    private float lastMainSwing;

    @Override
    protected void onEnable() {
        resetState();
    }

    @Override
    protected void onDisable() {
        resetState();
    }

    private void resetState() {
        this.inspectStartedNanos = 0L;
        this.inspectVariant = -1;
        this.comboIndex = 0;
        this.thrustHit = false;
        this.lastMainSwing = 0.0F;
    }

    public void beginWeaponInspect() {
        if (mc.player == null || mc.player.isUsingItem() || mc.player.isUsingSpyglass()) return;
        ItemStack stack = mc.player.getMainHandStack();
        if (!HmiTransforms.heldAlongTheFist(stack)) return;

        if (this.weaponInspectStyle.is("Вращение 360")) {
            this.inspectVariant = 1;
        } else if (this.weaponInspectStyle.is("Осмотр с двух сторон")) {
            this.inspectVariant = 0;
        } else {
            this.inspectVariant = this.inspectVariant == 1 ? 0 : 1;
        }
        this.inspectStartedNanos = System.nanoTime();
    }

    public float weaponInspectProgress() {
        if (this.inspectStartedNanos == 0L) return -1.0F;
        float variantTime = this.inspectVariant == 1 ? 0.90F : 1.18F;
        long durationNanos = Math.max(1L,
                (long) (this.weaponInspectTime.getValue() * variantTime * 1_000_000_000.0F));
        float progress = (float) ((System.nanoTime() - this.inspectStartedNanos) / (double) durationNanos);
        if (progress >= 1.0F) {
            this.inspectStartedNanos = 0L;
            return -1.0F;
        }
        return Math.max(0.0F, progress);
    }

    public void applyGrip(MatrixStack matrices, Arm arm, ItemStack stack, Hand hand) {
        if (!HmiTransforms.heldAlongTheFist(stack)) return;
        if (mc.player == null || mc.player.isUsingSpyglass()) return;
        if (mc.player.isUsingItem() && mc.player.getActiveHand() == hand) return;

        HmiTransforms.applyGrip(matrices, arm == Arm.RIGHT ? 1 : -1,
                gripExtend.getValue(), gripSlide.getValue(),
                gripOut.getValue(), gripForward.getValue(),
                gripTilt.getValue(), gripTurn.getValue(),
                gripRoll.getValue(), itemScale.getValue());
    }

    public void applyWeaponInspectArm(MatrixStack matrices, int side) {
        if (!isEnabled() || this.inspectStartedNanos == 0L || this.inspectVariant == 1) return;
        if (mc.player == null || !HmiTransforms.heldAlongTheFist(mc.player.getMainHandStack())) {
            this.inspectStartedNanos = 0L;
            return;
        }
        float progress = weaponInspectProgress();
        if (progress < 0.0F) return;
        HmiTransforms.applyWeaponInspect(matrices, side, progress, this.inspectVariant);
    }

    public void applyWeaponInspectItem(MatrixStack matrices, ItemStack stack, Hand hand, int side) {
        if (!isEnabled() || this.inspectStartedNanos == 0L || this.inspectVariant != 1
                || hand != Hand.MAIN_HAND || !HmiTransforms.heldAlongTheFist(stack)) return;
        float progress = weaponInspectProgress();
        if (progress < 0.0F) return;
        HmiTransforms.applyKarambitItemSpin(matrices, side, progress);
    }

    public void trackMainHandSwing(float mainSwingProgress) {
        boolean restarted = mainSwingProgress > 0.0F
                && (lastMainSwing <= 0.0F || mainSwingProgress < lastMainSwing - 0.01F);
        if (restarted) {
            this.inspectStartedNanos = 0L;
            comboIndex = (comboIndex + 1) % 3;
            thrustHit = comboIndex == 2;
        } else if (mainSwingProgress <= 0.0F) {
            thrustHit = false;
        }
        lastMainSwing = mainSwingProgress;
    }

    public void applyThrust(MatrixStack matrices, ItemStack stack, Hand hand, Arm arm, float mainSwingProgress) {
        if (!thrustHit || hand != Hand.MAIN_HAND || !thrustEnabled.getValue()) return;
        HmiTransforms.applyThrust(matrices, stack, arm, mainSwingProgress,
                thrustReach.getValue(), thrustAngle.getValue(), thrustCenter.getValue());
    }

    public boolean isThrustHit() {
        return thrustHit;
    }

    @EventHandler
    public void onKey(EventKey event) {
        if (isEnabled() && event.getKey() == weaponInspectKey.getValue()) {
            beginWeaponInspect();
        }
    }
}
