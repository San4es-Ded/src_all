package rockstar.modules.visual;




import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;

import lombok.Generated;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import rockstar.client.setting.SliderSetting;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.internal.game.EntityRenderStateAccessor;
import rockstar.client.module.Module;

@ModuleInfo(name="Anti Invisible", category=ModuleCategory.VISUALS, internalMethod08049=true)
public class AntiInvisibleModule
extends Module {
    private SliderSetting internalField0383;

    public AntiInvisibleModule() {
        this.internalMethod09661();
    }

    private void internalMethod09661() {
        this.internalField0383 = new SliderSetting(this, "modules.settings.anti_invisible.opacity").internalMethod05900(10.0f).internalMethod02732(100.0f).internalMethod08673(1.0f).internalMethod08074(70.0f).internalMethod05660(f -> "%");
    }

    public boolean internalMethod06737(EntityRenderState entityRenderState) {
        Entity entity = ((EntityRenderStateAccessor)entityRenderState).rockstar$getEntity();
        return entity.isInvisible();
    }

    @Generated
    public SliderSetting internalMethod04278() {
        return this.internalField0383;
    }
}
