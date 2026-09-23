package ru.prism.module.impl.display.interfaceimpl;

import ru.prism.module.api.settings.impl.DragSetting;
import ru.prism.module.impl.display.InterFace;
import ru.prism.utils.annotation.IMinecraft;

public interface element extends IMinecraft {

    void onRender(DragSetting dragSetting, InterFace interFace);

}
