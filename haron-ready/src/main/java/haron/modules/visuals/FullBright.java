package haron.modules.visuals;

import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.option.SimpleOption;

@ModuleInfo(a="Full Bright", b="Increases world brightness", c=ModuleCategory.VISUALS)
public class FullBright
extends HaronModule {
    private static final double FULL_BRIGHT_GAMMA = 9999.0;
    private double previousGamma = 1.0;

    @Override
    public void e() {
        if (FullBright.c.options == null) {
            return;
        }
        this.previousGamma = (Double)FullBright.c.options.getGamma().getValue();
        FullBright.c.options.getGamma().setValue(9999.0);
    }

    @Override
    public void f() {
        if (FullBright.c.options != null) {
            FullBright.c.options.getGamma().setValue(this.previousGamma);
        }
        super.f();
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        if (FullBright.c.options == null) {
            return;
        }
        SimpleOption<Double> simpleOption = FullBright.c.options.getGamma();
        if (simpleOption.getValue() != 9999.0) {
            simpleOption.setValue(9999.0);
        }
    }
}
