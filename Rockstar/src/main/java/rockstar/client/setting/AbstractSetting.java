package rockstar.client.setting;


import rockstar.client.*;
import rockstar.client.internal.ui.*;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import rockstar.client.setting.Setting;
import rockstar.client.internal.ui.SettingHistoryManager;
import rockstar.client.setting.SettingOwner;

public abstract class AbstractSetting
implements Setting {
    protected final String internalField0248;
    private final SettingOwner internalField0645;
    @NotNull
    private final BooleanSupplier internalField0424;

    public AbstractSetting(@NotNull SettingOwner typedValue159, String string, @NotNull BooleanSupplier booleanSupplier) {
        this.internalField0645 = typedValue159;
        this.internalField0248 = string;
        this.internalField0424 = booleanSupplier;
        this.registerWithOwner(typedValue159);
    }

    public AbstractSetting(@NotNull SettingOwner typedValue159, String string) {
        this(typedValue159, string, () -> false);
    }

    @Override
    public final void registerWithOwner(SettingOwner typedValue159) {
        typedValue159.getSettings().add(this);
    }

    public final void notifyChanged() {
        SettingHistoryManager.internalMethod06169(this);
    }

    @Override
    public final String getDescriptionKey() {
        return this.getName() + ".description";
    }

    @Override
    @Generated
    public String getName() {
        return this.internalField0248;
    }

    @Generated
    public SettingOwner internalMethod01453() {
        return this.internalField0645;
    }

    @Override
    @NotNull
    @Generated
    public BooleanSupplier getHiddenCondition() {
        return this.internalField0424;
    }
}

