package haron.settings;

import haron.config.ClientConfigCoordinator;
import haron.config.LocalConfigManager;
import java.util.Objects;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Setting<ConfigTextDialog> {
    private static final Logger LOG = LogManager.getLogger((String)"haron/config");
    private final String name;
    private final String description;
    private ConfigTextDialog value;
    private final ConfigTextDialog defaultValue;
    private final ConfigTextDialog minValue;
    private final ConfigTextDialog maxValue;
    private Supplier<Boolean> visibleCondition = () -> {
        return true;
    };

    public ConfigTextDialog minValue() {
        return this.minValue;
    }

    public boolean isVisible() {
        return this.visibleCondition.get();
    }

    public Setting<ConfigTextDialog> visibleWhen(Supplier<Boolean> supplier) {
        this.visibleCondition = supplier != null ? supplier : () -> {
            return true;
        };
        return this;
    }

    protected Setting(String string, String string2, ConfigTextDialog ConfigTextDialog) {
        this(string, string2, ConfigTextDialog, null, null);
    }

    protected Setting(String string, String string2, ConfigTextDialog ConfigTextDialog, ConfigTextDialog ConfigTextDialog2, ConfigTextDialog ConfigTextDialog3) {
        this.name = string;
        this.description = string2;
        this.value = ConfigTextDialog;
        this.defaultValue = ConfigTextDialog;
        this.minValue = ConfigTextDialog2;
        this.maxValue = ConfigTextDialog3;
    }

    public String name() {
        return this.name;
    }

    public void reset() {
        this.value = this.defaultValue;
    }

    public ConfigTextDialog value() {
        return this.value;
    }

    public ConfigTextDialog i() {
        return this.defaultValue();
    }

    public Setting<ConfigTextDialog> b(Supplier<Boolean> supplier) {
        return this.visibleWhen(supplier);
    }

    public Supplier<Boolean> h() {
        return this.visibleCondition;
    }

    public String f() {
        return this.name();
    }

    public void l() {
        this.reset();
    }

    public void a(ConfigTextDialog ConfigTextDialog) {
        this.setValue(ConfigTextDialog);
    }

    public boolean m() {
        return this.isVisible();
    }

    public ConfigTextDialog k() {
        return this.value();
    }

    public String g() {
        return this.description();
    }

    public ConfigTextDialog j() {
        return this.minValue();
    }

    public ConfigTextDialog defaultValue() {
        return this.defaultValue;
    }

    public void setValue(ConfigTextDialog ConfigTextDialog) {
        if (Objects.equals(this.value, ConfigTextDialog)) {
            return;
        }
        this.value = ConfigTextDialog;
        if (LocalConfigManager.get().isApplying()) {
            return;
        }
        LOG.debug("[Pulse] Setting '{}' -> {}", (Object)this.name, ConfigTextDialog);
        ClientConfigCoordinator.a().h();
    }

    public ConfigTextDialog maxValue() {
        return this.maxValue;
    }

    public String description() {
        int n = 376;
        return this.description;
    }
}

