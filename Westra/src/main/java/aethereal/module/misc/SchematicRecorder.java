package aethereal.module.misc;

import aethereal.build.BuildData;
import aethereal.build.BuildManager;
import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.StringSetting;
import aethereal.ui.screen.BuildViewerScreen;
import aethereal.util.ChatUtil;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2487;
import net.minecraft.class_2586;
import net.minecraft.class_2680;

@ModuleRegister(
   a = "Schematic Recorder",
   b = "Записывает постройку из мира в схематику по двум точкам",
   c = Category.Misc
)
public class SchematicRecorder extends Module implements Interface {
   private static final int LIMIT = 500000;
   private final StringSetting b = new StringSetting("Название", "my_build");
   private final BooleanSetting c = new BooleanSetting("Открыть просмотр после записи", true);
   private class_2338 first;
   private class_2338 second;

   public SchematicRecorder() {
      this.a(new Setting[]{this.b, this.c});
   }

   @Override
   public void c() {
      super.c();
      this.first = null;
      this.second = null;
   }

   public void a(class_2338 pos) {
      this.first = pos;
   }

   public void b(class_2338 pos) {
      this.second = pos;
   }

   public class_2338 q() {
      return this.first;
   }

   public class_2338 r() {
      return this.second;
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.c() && aM_.field_1687 != null) {
         int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
         if (this.first != null) {
            event.e().a(event.h(), new class_238(this.first), ColorUtil.a(-11468976, 0.8F), 1.0F);
         }

         if (this.second != null) {
            event.e().a(event.h(), new class_238(this.second), ColorUtil.a(-44976, 0.8F), 1.0F);
         }

         if (this.first != null && this.second != null) {
            class_238 area = new class_238(this.first).method_991(new class_238(this.second));
            event.e().a(event.h(), area, ColorUtil.a(accent, 0.8F), 1.5F);
         }
      }
   }

   public void s() {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         if (this.first != null && this.second != null) {
            int minX = Math.min(this.first.method_10263(), this.second.method_10263());
            int minY = Math.min(this.first.method_10264(), this.second.method_10264());
            int minZ = Math.min(this.first.method_10260(), this.second.method_10260());
            int maxX = Math.max(this.first.method_10263(), this.second.method_10263());
            int maxY = Math.max(this.first.method_10264(), this.second.method_10264());
            int maxZ = Math.max(this.first.method_10260(), this.second.method_10260());
            int width = maxX - minX + 1;
            int height = maxY - minY + 1;
            int length = maxZ - minZ + 1;
            int volume = width * height * length;
            if (volume > 500000) {
               ChatUtil.a("Область слишком большая: &c" + volume + "&7 блоков, максимум &c500000&7.");
            } else {
               BuildData build = new BuildData(this.b.c(), aM_.field_1724.method_5477().getString(), BuildData.Source.USER);
               build.size(width, height, length);

               for (int x = minX; x <= maxX; x++) {
                  for (int y = minY; y <= maxY; y++) {
                     for (int z = minZ; z <= maxZ; z++) {
                        class_2338 pos = new class_2338(x, y, z);
                        class_2680 state = aM_.field_1687.method_8320(pos);
                        class_2487 nbt = null;
                        class_2586 entity = aM_.field_1687.method_8321(pos);
                        if (entity != null) {
                           try {
                              nbt = entity.method_38242(aM_.field_1687.method_30349());
                           } catch (Throwable var20) {
                              nbt = null;
                           }
                        }

                        build.add(x - minX, y - minY, z - minZ, state, nbt);
                     }
                  }
               }

               build.recalculate();
               BuildManager.get().save(build);
               ChatUtil.a("Записано &c" + build.solidCount() + "&7 блоков (" + width + "x" + height + "x" + length + ") как &c" + build.name() + "&7.");
               if (this.c.c()) {
                  aM_.method_1507(new BuildViewerScreen(build));
               }
            }
         } else {
            ChatUtil.a("Сначала отметьте обе точки: &c.schem pos1 &7и &c.schem pos2&7.");
         }
      }
   }
}
