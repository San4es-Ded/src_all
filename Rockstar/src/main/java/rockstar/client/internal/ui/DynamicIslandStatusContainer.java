package rockstar.client.internal.ui;





import rockstar.client.ui.*;
import rockstar.client.animation.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import java.util.List;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.animation.Motion;
import rockstar.client.internal.script.DynamicIslandHud;
import rockstar.client.internal.script.DynamicIslandStatus;
import rockstar.client.ui.UiTransition;
import rockstar.client.animation.Easing;
import rockstar.client.ui.UiNode;
import rockstar.client.ui.UiContainer;

public final class DynamicIslandStatusContainer
extends UiContainer {
    private final DynamicIslandStatus internalField0212;
    private final DynamicIslandHud internalField0209;
    private UiNode internalField0633;

    public DynamicIslandStatusContainer(DynamicIslandStatus typedValue202, DynamicIslandHud typedValue201) {
        this.internalField0212 = typedValue202;
        this.internalField0209 = typedValue201;
        this.internalMethod08791().internalMethod09213().internalMethod09801().snapSize().interactive(false).motion(Motion.internalMethod01328(500L, Easing.internalField1327)).lifeMotion(Motion.internalMethod01328(360L, Easing.internalField1327)).enter(UiTransition.internalField1389).exit(UiTransition.internalField1389);
    }

    @Override
    public void measure() {
        this.internalMethod08139();
        if (this.internalField0633 == null) {
            this.prefW = this.internalField0212.getSize().internalField0205;
            this.prefH = this.internalField0212.getSize().internalField0206;
            return;
        }
        super.measure();
        this.internalField0212.getSize().internalMethod03295(this.desiredW(), this.desiredH());
    }

    @Override
    public void drawSelf(UiRenderContext iII, float f) {
        if (this.internalField0633 == null) {
            this.internalField0212.render(iII, this.internalField0209, this.x(), this.y(), this.w(), this.h(), f);
        }
    }

    private void internalMethod08139() {
        UiNode typedValue004 = this.internalField0212.content(this.internalField0209);
        if (typedValue004 == this.internalField0633) {
            return;
        }
        this.internalField0633 = typedValue004;
        if (this.internalField0633 == null) {
            this.internalMethod03628();
        } else {
            this.internalMethod07849(List.of(this.internalField0633));
        }
    }
}
