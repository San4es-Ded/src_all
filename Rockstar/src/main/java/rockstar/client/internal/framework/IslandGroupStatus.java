package rockstar.client.internal.framework;




import rockstar.client.setting.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.internal.script.DynamicIslandStatus;

public class IslandGroupStatus
extends DynamicIslandStatus {
    public IslandGroupStatus(MultiSelectSetting typedValue173, String string) {
        super(typedValue173, string);
    }

    @Override
    public boolean isExpandable() {
        return true;
    }

    @Override
    public boolean canShow() {
        return false;
    }
}

