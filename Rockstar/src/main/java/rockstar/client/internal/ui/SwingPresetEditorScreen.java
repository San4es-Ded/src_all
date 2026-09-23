package rockstar.client.internal.ui;








import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.framework.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.util.Collection;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import rockstar.client.ui.UiRenderContext;
import rockstar.modules.visual.MenuModule;
import rockstar.client.setting.Setting;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.HudContextMenu;
import rockstar.client.internal.script.CustomTextField;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.ScreenMetricsAccess;
import rockstar.client.internal.core.HudMenuClickHandler;
import rockstar.client.internal.framework.SwingAnimationManager;
import rockstar.client.internal.script.SwingEditorPanel;
import rockstar.client.internal.ui.SwingPresetListPanel;
import rockstar.client.internal.core.SwingPreset;
import rockstar.client.internal.config.SwingPresetManager;
import rockstar.client.ui.RockstarScreen;

public class SwingPresetEditorScreen
extends RockstarScreen
implements MinecraftClientAccess,
ScreenMetricsAccess {
    private final HudContextMenu internalField0574 = new HudContextMenu(100.0f, 100.0f).internalMethod04738("presets").internalMethod04574();
    private final HudContextMenu internalField0575 = new HudContextMenu(100.0f, 100.0f).internalMethod04738("shared").internalMethod04574();
    private final SwingEditorPanel internalField0733 = new SwingEditorPanel();

    public SwingPresetEditorScreen() {
        SwingAnimationManager typedValue108 = RockstarClient.getInstance().internalMethod00061();
        RockstarClient.getInstance().internalMethod01001().internalMethod08675();
        this.internalField0574.internalMethod03869(new SwingPresetListPanel());
        this.internalMethod00855(typedValue108.internalMethod04275().internalField0416, this.internalField0575);
        SwingAnimationManager typedValue109 = RockstarClient.getInstance().internalMethod00061();
        String string = typedValue109.internalMethod02484();
        for (SwingPreset typedValue116 : RockstarClient.getInstance().internalMethod00061().internalMethod05754()) {
            if (!typedValue116.internalMethod02665().equals(string)) continue;
            typedValue109.internalMethod01403(typedValue116);
        }
    }

    @Override
    public void render(UiRenderContext iII) {
        float f = 210.0f;
        float f2 = 230.0f;
        float f3 = 360.0f + f;
        float f4 = ScreenMetricsAccess.internalField0389.internalMethod03585() / 2.0f - f3 / 2.0f;
        float f5 = internalField0389.internalMethod03589() / 2.0f;
        float f6 = f5 - f2 / 2.0f;
        this.internalField0574.internalMethod08630(170.0f);
        this.internalField0575.internalMethod08630(170.0f);
        this.internalField0574.internalMethod03623(f4);
        this.internalField0575.internalMethod03623(f4 + 180.0f);
        this.internalField0574.internalMethod03701(f6);
        this.internalField0575.internalMethod03701(f6);
        this.internalField0574.internalMethod03398(iII);
        this.internalField0575.internalMethod03398(iII);
        this.internalField0733.internalMethod05191(f4 + 360.0f, f6, f, f2);
        this.internalField0733.internalMethod03398(iII);
    }

    @Override
    public void onMouseClicked(double d, double d2, MouseButton typedParameter1015) {
        this.internalMethod05977(typedValue190 -> typedValue190.internalMethod01643(d, d2, typedParameter1015));
        this.internalField0733.internalMethod01643(d, d2, typedParameter1015);
    }

    @Override
    public void onMouseReleased(double d, double d2, MouseButton typedParameter1015) {
        this.internalMethod05977(typedValue190 -> typedValue190.internalMethod02863(d, d2, typedParameter1015));
        this.internalField0733.internalMethod02863(d, d2, typedParameter1015);
    }

    @Override
    public void onMouseDragged(double d, double d2, MouseButton typedParameter1015, double d3, double d4) {
        this.internalField0733.internalMethod00368(d, d2, typedParameter1015, d3, d4);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.internalMethod05977(typedValue190 -> typedValue190.internalMethod02890(mouseX, mouseY, horizontalAmount, verticalAmount));
        this.internalField0733.internalMethod02890(mouseX, mouseY, horizontalAmount, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (rockstar.client.compat.InputCompat.hasControlDown() && (keyCode == 90 || keyCode == 89)) {
            boolean bl;
            boolean bl2 = bl = keyCode == 89;
            if (bl ? this.internalField0574.internalMethod02629() : this.internalField0574.internalMethod02626()) {
                return true;
            }
            if (bl ? this.internalField0575.internalMethod02629() : this.internalField0575.internalMethod02626()) {
                return true;
            }
            this.internalField0733.internalMethod05727(keyCode, scanCode, modifiers);
            return true;
        }
        this.internalMethod05977(typedValue190 -> typedValue190.internalMethod05727(keyCode, scanCode, modifiers));
        this.internalField0733.internalMethod05727(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean charTyped(char chr, int modifiers) {
        this.internalMethod05977(typedValue190 -> typedValue190.internalMethod05413(chr, modifiers));
        return super.charTyped(chr, modifiers);
    }

    private void internalMethod00855(Collection<Setting> collection, HudContextMenu typedValue190) {
        for (Setting typedValue157 : collection) {
            typedValue190.internalMethod05995(typedValue157);
        }
    }

    private void internalMethod05977(HudMenuClickHandler typedValue107) {
        typedValue107.call(this.internalField0574);
        typedValue107.call(this.internalField0575);
    }

    public boolean shouldPause() {
        return false;
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    public void close() {
        SwingPresetManager internalValue0003 = RockstarClient.getInstance().internalMethod01001();
        if (internalValue0003.internalMethod02695() != null) {
            internalValue0003.internalMethod02695().internalMethod03767();
        }
        if (CustomTextField.internalField0936 != null) {
            CustomTextField.internalField0936.internalMethod07508(false);
        }
        super.close();
        MenuModule.internalMethod09723();
    }
}
