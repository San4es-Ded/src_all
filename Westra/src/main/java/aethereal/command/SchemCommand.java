package aethereal.command;

import aethereal.build.BuildData;
import aethereal.build.BuildManager;
import aethereal.core.Westra;
import aethereal.module.misc.SchematicRecorder;
import aethereal.ui.screen.BuildViewerScreen;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import net.minecraft.class_2172;
import net.minecraft.class_2338;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;

@Command(
   a = "schem"
)
public class SchemCommand extends BaseCommand {
   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      builder.then(this.a("pos1").executes(context -> {
         class_2338 pos = this.looking();
         if (pos != null) {
            this.recorder().a(pos);
            ChatUtil.a("Точка 1: &c" + pos.method_10263() + ", " + pos.method_10264() + ", " + pos.method_10260());
         }

         return 1;
      }));
      builder.then(this.a("pos2").executes(context -> {
         class_2338 pos = this.looking();
         if (pos != null) {
            this.recorder().b(pos);
            ChatUtil.a("Точка 2: &c" + pos.method_10263() + ", " + pos.method_10264() + ", " + pos.method_10260());
         }

         return 1;
      }));
      builder.then(this.a("clear").executes(context -> {
         this.recorder().a((class_2338)null);
         this.recorder().b(null);
         ChatUtil.a("Выделение очищено.");
         return 1;
      }));
      builder.then(this.a("save").executes(context -> {
         this.recorder().s();
         return 1;
      }));
      builder.then(
         this.a("list")
            .executes(
               context -> {
                  List<BuildData> builds = BuildManager.get().all();
                  if (builds.isEmpty()) {
                     ChatUtil.a("Построек нет. Запишите свою: &c.schem pos1&7, &c.schem pos2&7, &c.schem save&7.");
                     return 1;
                  } else {
                     StringBuilder message = new StringBuilder("Постройки:");

                     for (BuildData build : builds) {
                        message.append("\n&7 - &c")
                           .append(build.name())
                           .append(" &7(")
                           .append(build.solidCount())
                           .append(" бл., ")
                           .append(build.source() == BuildData.Source.BUILTIN ? "встроенная" : "своя")
                           .append(')');
                     }

                     ChatUtil.a(message.toString());
                     return 1;
                  }
               }
            )
      );
      builder.then(this.a("open").executes(context -> {
         BuildManager.get().reload();
         aM_.method_63588(() -> aM_.method_1507(new BuildViewerScreen()));
         return 1;
      }));
   }

   private SchematicRecorder recorder() {
      return Westra.h().d().t().cE();
   }

   private class_2338 looking() {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         if (aM_.field_1765 instanceof class_3965 hit && aM_.field_1765.method_17783() == class_240.field_1332) {
            return hit.method_17777();
         } else {
            ChatUtil.a("Наведитесь на блок.");
            return null;
         }
      } else {
         ChatUtil.a("Нужно быть в мире.");
         return null;
      }
   }
}
