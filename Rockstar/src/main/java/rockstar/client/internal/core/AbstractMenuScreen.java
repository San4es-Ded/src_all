package rockstar.client.internal.core;




import rockstar.client.ui.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import lombok.Generated;
import rockstar.client.ui.LayeredRockstarScreen;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;

public abstract class AbstractMenuScreen
extends LayeredRockstarScreen {
    public final AnimatedValue menuAnimation = new AnimatedValue(500L, Easing.internalField1627);
    public boolean closing = true;

    @Generated
    public AnimatedValue getMenuAnimation() {
        return this.menuAnimation;
    }

    @Generated
    public boolean isClosing() {
        return this.closing;
    }

    @Generated
    public void setClosing(boolean bl) {
        this.closing = bl;
    }
}

