package haron.modules.utilities;

import haron.events.CriticalHitEvent;
import haron.events.AttackTargetEvent;
import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.BooleanSetting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.util.InputUtil;

@ModuleInfo(a="Shift Tab", b="Briefly sneaks while attacking.", c=ModuleCategory.UTILITIES)
public class ShiftTab
extends HaronModule {
    private static final int SNEAK_TICKS = 2;
    private final BooleanSetting triggerBeforeAttack = new BooleanSetting("До атаки", "If disabled, sneaking starts after the attack event.", true);
    private int sneakTicksLeft;
    private boolean restorePending;
    private boolean previousSneakPressed;

    private void startSneakBurst() {
        if (ShiftTab.c.player == null) {
            return;
        }
        this.previousSneakPressed = this.isSneakKeyPhysicallyPressed();
        this.sneakTicksLeft = 2;
        this.restorePending = true;
        ShiftTab.c.options.sneakKey.setPressed(true);
    }

    private boolean isSneakKeyPhysicallyPressed() {
        return c.getWindow() != null && InputUtil.isKeyPressed((long)c.getWindow().getHandle(), (int)ShiftTab.c.options.sneakKey.getDefaultKey().getCode());
    }

    @Override
    public void f() {
        super.f();
        this.sneakTicksLeft = 0;
        this.restorePending = false;
        if (ShiftTab.c.options == null) {
            return;
        }
        ShiftTab.c.options.sneakKey.setPressed(this.isSneakKeyPhysicallyPressed());
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        if (ShiftTab.c.player == null) {
            return;
        }
        if (this.sneakTicksLeft > 0) {
            ShiftTab.c.options.sneakKey.setPressed(true);
            --this.sneakTicksLeft;
        } else if (this.restorePending) {
            ShiftTab.c.options.sneakKey.setPressed(this.previousSneakPressed);
            this.restorePending = false;
        }
    }

    @EventHandler
    public void a(CriticalHitEvent bjxb112) {
        if (((Boolean)this.triggerBeforeAttack.k()).booleanValue()) {
            this.startSneakBurst();
        }
    }

    @EventHandler
    public void a(AttackTargetEvent dt813s2) {
        if (((Boolean)this.triggerBeforeAttack.k()).booleanValue()) {
            return;
        }
        this.startSneakBurst();
    }
}

