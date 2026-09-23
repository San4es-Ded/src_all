package rockstar.client.internal.framework;



import rockstar.client.setting.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import rockstar.client.setting.Setting;
import rockstar.client.setting.SettingOwner;

public class SettingGroupBase
implements SettingOwner {
    public final List<Setting> internalField0416 = new ArrayList<Setting>();

    @Override
    @Generated
    public List<Setting> getSettings() {
        return this.internalField0416;
    }
}

