package haron.hud.core;

import haron.events.EventDispatcher;
import haron.hud.core.HudServiceInfo;

public abstract class HudService {
    private boolean enabled;

    public void toggle() {
        this.setEnabled(!this.enabled);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void initialize() {
        HudServiceInfo geskfq2 = this.getClass().getAnnotation(HudServiceInfo.class);
        this.setEnabled(geskfq2 == null || geskfq2.enabledByDefault());
    }

    public void setEnabled(boolean bl) {
        if (this.enabled == bl) {
            return;
        }
        this.enabled = bl;
        if (bl) {
            EventDispatcher.EVENT_BUS.subscribe((Object)this);
        } else {
            EventDispatcher.EVENT_BUS.unsubscribe((Object)this);
        }
    }
}
