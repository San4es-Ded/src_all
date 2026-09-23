package haron.modules.utilities;

import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.TextTokenType;
import haron.settings.ValidatedTextSetting;
import java.util.regex.Pattern;
import meteordevelopment.orbit.EventHandler;

@ModuleInfo(a="Auto Invest", b="Automatically invests clan money above the configured limit.", c=ModuleCategory.UTILITIES)
public class AutoInvest
extends HaronModule {
    private final ValidatedTextSetting limit = new ValidatedTextSetting("Limit", "Minimum balance before investing.", TextTokenType.PRICE, "1000000", "$");
    private final Pattern numberPattern = Pattern.compile("\\d+");
    private long lastInvestTime = -1L;

    private long n() {
        try {
            return Long.parseLong(((String)this.limit.k()).replace("$", "").replace(" ", ""));
        }
        catch (NumberFormatException numberFormatException) {
            return 0L;
        }
    }

    @Override
    public void f() {
        super.f();
        this.lastInvestTime = -1L;
    }

    @EventHandler
    private void a(ClientTickEvent q8krcw2) {
    }

    private long o() {
        return this.lastInvestTime;
    }
}

