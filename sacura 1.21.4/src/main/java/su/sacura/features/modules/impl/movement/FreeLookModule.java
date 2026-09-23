package su.sacura.features.modules.impl.movement;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.option.Perspective;
import su.sacura.events.input.KeyEvent;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.impl.movement.freelook.FreeLookState;
import su.sacura.features.modules.settings.impl.BindSetting;
import su.sacura.util.type.ISetting;

@ModuleAnnotations(name = "Free Look", category = Category.MOVEMENT)
public class FreeLookModule extends Module {
    private final BindSetting bind = (new BindSetting("Кнопка", Integer.valueOf(88))).setDescription("Кнопка включения");

    private Perspective previousPerspective;

    public FreeLookModule() {
        addSettings(new ISetting[] { (ISetting)this.bind });
    }

    @Subscribe
    public void onEvent(KeyEvent event) {
        if (!this.bind.matches(event))
            return;
        if (mc == null || mc.options == null)
            return;
        if (event.action() == 1) {
            FreeLookState.active = true;
            this.previousPerspective = mc.options.getPerspective();
            // Исправлено: mouseY -> THIRD_PERSON_BACK
            mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
        } else if (event.action() == 0) {
            FreeLookState.active = false;
            // Исправлено: mouseX -> FIRST_PERSON
            mc.options.setPerspective((this.previousPerspective != null) ? this.previousPerspective : Perspective.FIRST_PERSON);
        }
    }

    public void onDisable() {
        FreeLookState.active = false;
        if (mc != null && mc.options != null)
            // Исправлено: mouseX -> FIRST_PERSON
            mc.options.setPerspective((this.previousPerspective != null) ? this.previousPerspective : Perspective.FIRST_PERSON);
        this.previousPerspective = null;
        super.onDisable();
    }
}