package aethereal.module.render;

import aethereal.build.BuildManager;
import aethereal.core.Category;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.ui.screen.BuildViewerScreen;

@ModuleRegister(
   a = "Build Viewer",
   b = "Открывает 3D-просмотр записанных построек и схематик",
   c = Category.Render
)
public class BuildViewer extends Module implements Interface {
   @Override
   public void b() {
      super.b();
      if (aM_.field_1755 == null) {
         BuildManager.get().reload();
         aM_.method_1507(new BuildViewerScreen());
      }

      this.c();
   }
}
