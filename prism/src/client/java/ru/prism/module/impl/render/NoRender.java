package ru.prism.module.impl.render;

import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "No Render",
        desc = "Убирает всякую хуйню на твоем ебале.",
        category = Category.VISUALS
)
public class NoRender extends Module {

    public static NoRender getInstance() {
        return Instance.get(NoRender.class);
    }

    public BooleanSetting ignoreFire = new BooleanSetting(this,"Убирать огонь",true);
    public BooleanSetting ignoreLava = new BooleanSetting(this,"Убирать туман лавы",true);
    public BooleanSetting ignoreZalupa = new BooleanSetting(this,"Убирать плохие эффекты",true);
    public BooleanSetting ignoreScoreboard = new BooleanSetting(this,"Убирать Скорборт",false);
    public BooleanSetting ignoreBossBar = new BooleanSetting(this,"Убирать Босс бар",false);
    public BooleanSetting noCameraClip = new BooleanSetting(this,"Камера сквозь блоки",false);
    public BooleanSetting ignoreTotemPop = new BooleanSetting(this,"Убирать тотем на экране",true);
    public BooleanSetting removeCamreZalupa = new BooleanSetting(this,"Убирать дерганее камеры",true);




}
