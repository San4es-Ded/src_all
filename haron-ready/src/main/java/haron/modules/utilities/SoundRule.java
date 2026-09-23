package haron.modules.utilities;

import haron.settings.NumberSetting;
import haron.settings.BooleanSetting;

class SoundRule {
    private final BooleanSetting enabled;
    private final NumberSetting volume;

    public SoundRule(BooleanSetting enabled, NumberSetting volume) {
        this.enabled = enabled;
        this.volume = volume;
    }

    public boolean isEnabled() {
        return this.enabled.a();
    }

    public float volumePercent() {
        return this.volume.a();
    }

}
