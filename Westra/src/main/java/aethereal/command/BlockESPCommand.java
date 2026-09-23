package aethereal.command;

import aethereal.render.ColorUtil;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;
import lombok.Generated;
import net.minecraft.class_2172;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

@Command(
   a = "blockesp"
)
public class BlockESPCommand extends BaseCommand {
   private final List<BlockESPCommand.a> c = new CopyOnWriteArrayList<>();

   @Generated
   public List<BlockESPCommand.a> c() {
      return this.c;
   }

   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.then(
                     ((LiteralArgumentBuilder)this.a("add").executes(context -> {
                           ChatUtil.a("Использование: .blockesp add <блок> [цвет]");
                           return 1;
                        }))
                        .then(
                           ((RequiredArgumentBuilder)this.d("блок")
                                 .suggests(
                                    (context2, suggestions) -> {
                                       Stream streamLimit = class_7923.field_41175
                                          .method_10220()
                                          .filter(block -> block != class_2246.field_10124)
                                          .map(block2 -> class_7923.field_41175.method_10221(block2).method_12832())
                                          .filter(path -> path.startsWith(suggestions.getRemainingLowerCase()))
                                          .limit(20L);
                                       streamLimit.forEach(s -> suggestions.suggest((String)s));
                                       return suggestions.buildFuture();
                                    }
                                 )
                                 .executes(context3 -> this.a(this.a(context3, "блок"), (String)null)))
                              .then(
                                 this.d("цвет")
                                    .suggests(
                                       (context4, suggestions2) -> {
                                          Stream streamFilter = Arrays.stream(BlockESPCommand.EspColor.values())
                                             .map(color -> color.name().toLowerCase())
                                             .filter(name -> name.startsWith(suggestions2.getRemainingLowerCase()));
                                          streamFilter.forEach(s -> suggestions2.suggest((String)s));
                                          return suggestions2.buildFuture();
                                       }
                                    )
                                    .executes(context5 -> this.a(this.a(context5, "блок"), this.a(context5, "цвет")))
                              )
                        )
                  ))
                  .then(((LiteralArgumentBuilder)this.a("remove").executes(context6 -> {
                     ChatUtil.a("Использование: .blockesp remove <блок>");
                     return 1;
                  })).then(
                     this.d("блок").suggests(this.a(() -> this.c, info -> class_7923.field_41175.method_10221(info.a).method_12832())).executes(context7 -> {
                        String name = this.a(context7, "блок");
                        if (this.c.removeIf(info2 -> info2.a == class_7923.field_41175.method_63535(class_2960.method_60654(name)))) {
                           ChatUtil.a("Блок &c" + name + " &7успешно удалён");
                           return 1;
                        } else {
                           ChatUtil.a("Блок с именем &c" + name + " &7отсутствует");
                           return 1;
                        }
                     })
                  )))
               .then(this.a("list").executes(context8 -> {
                  if (this.c.isEmpty()) {
                     ChatUtil.a("Список блоков не содержит элементов");
                     return 1;
                  } else {
                     ChatUtil.a("Список всех блоков (" + this.c.size() + "):");

                     for (BlockESPCommand.a info2 : this.c) {
                        ChatUtil.a("— &c" + class_7923.field_41175.method_10221(info2.a).method_12832() + (info2.b != -1 ? " &7(" + this.a(info2.b) + ")" : ""));
                     }

                     return 1;
                  }
               })))
            .then(this.a("clear").executes(context9 -> {
               ChatUtil.a("Количество удалённых блоков: " + this.c.size());
               this.c.clear();
               return 1;
            })))
         .executes(context10 -> {
            ChatUtil.a("Использование: .blockesp <add|remove|list|clear>");
            return 1;
         });
   }

   private int a(String name, String colorName) {
      class_2248 block = (class_2248)class_7923.field_41175.method_63535(class_2960.method_60654(name));
      if (block != class_2246.field_10124) {
         this.c.removeIf(info -> info.a == block);
         this.c.add(new BlockESPCommand.a(block, colorName != null ? BlockESPCommand.EspColor.a(colorName) : -1));
         ChatUtil.a("Блок &c" + name + " &7успешно добавлен" + (colorName != null ? " (&c" + colorName + "&7)" : ""));
         return 1;
      } else {
         ChatUtil.a("Блок с именем &c" + name + " &7не найден");
         return 1;
      }
   }

   private String a(int color) {
      for (BlockESPCommand.EspColor espColor : BlockESPCommand.EspColor.values()) {
         if (ColorUtil.a(espColor.t, espColor.u, espColor.v, 140) == color) {
            return espColor.name().toLowerCase();
         }
      }

      return "custom";
   }

   public static enum EspColor {
      RED(255, 0, 0),
      GREEN(0, 255, 0),
      BLUE(0, 0, 255),
      YELLOW(255, 255, 0),
      PURPLE(128, 0, 128),
      ORANGE(255, 165, 0),
      PINK(255, 192, 203),
      CYAN(0, 255, 255),
      WHITE(255, 255, 255),
      BLACK(0, 0, 0),
      GRAY(128, 128, 128),
      BROWN(165, 42, 42),
      LIME(50, 205, 50),
      MAGENTA(255, 0, 255),
      GOLD(255, 215, 0),
      CORAL(255, 127, 80),
      TURQUOISE(64, 224, 208),
      CRIMSON(220, 20, 60),
      EMERALD(80, 200, 120);

      final int t;
      final int u;
      final int v;

      @Generated
      private EspColor(final int r, final int g, final int b) {
         this.t = r;
         this.u = g;
         this.v = b;
      }

      static int a(String name) {
         try {
            BlockESPCommand.EspColor espColor = valueOf(name.toUpperCase());
            return ColorUtil.a(espColor.t, espColor.u, espColor.v, 140);
         } catch (IllegalArgumentException var2) {
            return -1;
         }
      }
   }

   public static class a {
      final class_2248 a;
      final int b;

      @Generated
      public a(class_2248 block, int color) {
         this.a = block;
         this.b = color;
      }

      @Generated
      public class_2248 a() {
         return this.a;
      }

      @Generated
      public int b() {
         return this.b;
      }
   }
}
