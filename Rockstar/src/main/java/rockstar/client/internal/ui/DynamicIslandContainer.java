package rockstar.client.internal.ui;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.internal.script.DynamicIslandHud;
import rockstar.client.render.ScissorStack;
import rockstar.client.ui.UiContainer;

public class DynamicIslandContainer
extends UiContainer {
    private final DynamicIslandHud internalField0209;

    public DynamicIslandContainer(DynamicIslandHud typedValue201) {
        this.internalField0209 = typedValue201;
    }

    @Override
    public void drawSelf(UiRenderContext iII, float f) {
        this.internalField0209.internalMethod04864(iII, this, f);
    }

    @Override
    public void drawChildren(UiRenderContext iII, float f) {
        ScissorStack.internalMethod06303(iII.getMatrices(), this.x(), this.y(), this.w(), this.h());
        super.drawChildren(iII, f);
        ScissorStack.internalMethod07643();
    }
}
