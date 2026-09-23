package rockstar.client.internal.ui;




import rockstar.client.ui.*;
import rockstar.client.module.*;
import rockstar.client.*;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.ui.ModuleSettingsPanel;
import rockstar.client.ui.LayeredRockstarScreen;
import rockstar.client.ui.MouseButton;
import rockstar.client.ui.UiElement;
import rockstar.client.ui.UiNode;

public class ModuleSettingsScreen
extends LayeredRockstarScreen {
    private final UiNode internalField0633 = new UiElement().size(0.0f, 0.0f).interactive(false);
    private ModuleSettingsPanel internalField0096;

    @Override
    public void init() {
        super.init();
        this.clearRoots();
        this.internalField0096 = new ModuleSettingsPanel(this.internalField0633, (typedValue145, typedValue157) -> {}).internalMethod02427(null);
        this.add(this.internalField0096);
        this.internalField0096.internalMethod08158();
    }

    @Override
    public void render(UiRenderContext iII) {
        super.render(iII);
        if (this.internalField0096 != null && !this.internalField0096.internalMethod00442()) {
            this.close();
        }
    }

    @Override
    public void onMouseClicked(double d, double d2, MouseButton typedParameter1015) {
        super.onMouseClicked(d, d2, typedParameter1015);
        if (this.internalField0096 != null) {
            this.internalField0096.internalMethod04148((float)d, (float)d2);
        }
    }
}

