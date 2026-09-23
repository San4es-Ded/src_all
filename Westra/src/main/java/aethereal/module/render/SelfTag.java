package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;

@ModuleRegister(
   a = "Self Tag",
   b = "Показывает вашу собственную табличку с ником при виде от третьего лица",
   c = Category.Render
)
public class SelfTag extends Module implements Interface {
}
