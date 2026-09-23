package rockstar.client.internal.config;



import rockstar.client.setting.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;

public class SharedBooleanSetting
extends BooleanSetting {
    private static final Map<String, boolean[]> internalField0543 = new HashMap<String, boolean[]>();
    private final boolean[] internalField0637;

    public SharedBooleanSetting(@NotNull SettingOwner typedValue159, String string2) {
        super(typedValue159, string2);
        this.internalField0637 = internalField0543.computeIfAbsent(string2, string -> new boolean[1]);
    }

    @Override
    public boolean internalMethod04496() {
        return this.internalField0637[0];
    }

    @Override
    public void internalMethod02034(boolean bl) {
        if (this.internalField0637[0] == bl) {
            return;
        }
        this.notifyChanged();
        this.internalField0637[0] = bl;
    }

    @Override
    public void toggle() {
        this.internalMethod02034(!this.internalField0637[0]);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(Boolean.valueOf(this.internalField0637[0]));
    }
}

