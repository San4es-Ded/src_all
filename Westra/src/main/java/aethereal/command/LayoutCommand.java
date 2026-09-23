package aethereal.command;

import aethereal.core.EventTarget;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2172;
import net.minecraft.class_2371;
import net.minecraft.class_2815;
import platform.inject.accessors.SlotAccessor;

@Command(
   a = "layout"
)
public class LayoutCommand extends BaseCommand {
   private final List<LayoutCommand.a> c = new ArrayList<>();
   private final List<LayoutCommand.a> d = new ArrayList<>();
   private int e;

   @Generated
   public List<LayoutCommand.a> c() {
      return this.c;
   }

   private void d() {
      Westra.h().d().t().b("default");
   }

   private void a(List<LayoutCommand.a> layout) {
      this.d.clear();
      this.d.addAll(layout);
      this.e = 0;
   }

   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.then(
                        ((LiteralArgumentBuilder)this.a("save").executes(context -> {
                           ChatUtil.a("Использование: .layout save <название>");
                           return 1;
                        })).then(this.d("название").executes(context2 -> {
                           String name = this.a(context2, "название");
                           List<LayoutCommand.a> layouts = this.c;
                           layouts.removeIf(layout -> layout.a().equalsIgnoreCase(name));

                           for (class_1735 slot : aM_.field_1724.field_7498.field_7761) {
                              if (slot.field_7871 == aM_.field_1724.method_31548() && !slot.method_7677().method_7960()) {
                                 layouts.add(new LayoutCommand.a(name, slot.method_7677().method_7972(), slot.field_7874));
                              }
                           }

                           this.d();
                           ChatUtil.a("Раскладка &c" + name + " &7сохранена.");
                           return 1;
                        }))
                     ))
                     .then(((LiteralArgumentBuilder)this.a("load").executes(context3 -> {
                        ChatUtil.a("Использование: .layout load <название>");
                        return 1;
                     })).then(this.d("название").suggests(this.a(this::c, v0 -> v0.a())).executes(context4 -> {
                        String name = this.a(context4, "название");
                        List<LayoutCommand.a> target = this.c.stream().filter(layout -> layout.a().equalsIgnoreCase(name)).toList();
                        if (target.isEmpty()) {
                           ChatUtil.a("Раскладка &c" + name + " &7не найдена");
                           return 1;
                        } else {
                           this.a(target);
                           return 1;
                        }
                     }))))
                  .then(((LiteralArgumentBuilder)this.a("remove").executes(context5 -> {
                     ChatUtil.a("Использование: .layout remove <название>");
                     return 1;
                  })).then(this.d("название").suggests(this.a(this::c, v0 -> v0.a())).executes(context6 -> {
                     String name = this.a(context6, "название");
                     List<LayoutCommand.a> layouts = this.c;
                     if (layouts.stream().noneMatch(layout -> layout.a().equalsIgnoreCase(name))) {
                        ChatUtil.a("Раскладка &c" + name + " &7не найдена");
                        return 1;
                     } else {
                        layouts.removeIf(layout2 -> layout2.a().equalsIgnoreCase(name));
                        this.d();
                        ChatUtil.a("Раскладка &c" + name + " &7удалена");
                        return 1;
                     }
                  }))))
               .then(this.a("list").executes(context7 -> {
                  List<LayoutCommand.a> layouts = this.c;
                  if (layouts.isEmpty()) {
                     ChatUtil.a("Список раскладок пуст.");
                     return 1;
                  } else {
                     ChatUtil.a("Сохраненные раскладки:");
                     layouts.stream().map(v0 -> v0.a()).distinct().forEach(name -> ChatUtil.a("— &c" + name));
                     return 1;
                  }
               })))
            .then(this.a("clear").executes(context8 -> {
               this.c.clear();
               this.d();
               ChatUtil.a("Раскладки очищены.");
               return 1;
            })))
         .executes(context9 -> {
            ChatUtil.a("Использование: .layout <save|load|remove|list|clear> <название>");
            return 1;
         });
   }

   @EventTarget
   public void a(TickEvent event) {
      if (!this.d.isEmpty()) {
         int i = this.e--;
         if (i > 1) {
            return;
         }

         this.e = 2;
         class_2371 class_2371Var = aM_.field_1724.field_7498.field_7761;
         List<String> missing = new ArrayList<>();

         for (LayoutCommand.a info : this.d) {
            class_1735 target = (class_1735)class_2371Var.get(info.c());
            class_1792 item = info.b().method_7909();
            if (target.method_7677().method_7909() != item) {
               class_1735 source = class_2371Var.stream()
                  .filter(slot -> ((SlotAccessor)slot).getInventory() == aM_.field_1724.method_31548())
                  .filter(slot2 -> slot2.method_7677().method_7909() == item)
                  .filter(
                     slot3 -> this.d.stream().noneMatch(other -> other.c() == slot3.field_7874 && other.b().method_7909() == slot3.method_7677().method_7909())
                  )
                  .findFirst()
                  .orElse(null);
               if (source != null) {
                  int hotbar = source.field_7874 != 36 && target.field_7874 != 36 ? 0 : (source.field_7874 != 37 && target.field_7874 != 37 ? 1 : 2);
                  aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, source.field_7874, hotbar, class_1713.field_7791, aM_.field_1724);
                  aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, target.field_7874, hotbar, class_1713.field_7791, aM_.field_1724);
                  aM_.field_1761.method_2906(aM_.field_1724.field_7498.field_7763, source.field_7874, hotbar, class_1713.field_7791, aM_.field_1724);
                  if (this.d.stream().noneMatch(other -> ((class_1735)class_2371Var.get(other.c())).method_7677().method_7909() != other.b().method_7909())) {
                     aM_.field_1724.field_3944.method_52787(new class_2815(aM_.field_1724.field_7498.field_7763));
                     this.d.clear();
                     return;
                  }

                  return;
               }

               String name = info.b().method_7964().getString();
               if (!missing.contains(name)) {
                  missing.add(name);
               }
            }
         }

         if (missing.isEmpty()) {
            ChatUtil.a("Раскладка разложена.");
         } else {
            ChatUtil.a("Раскладка разложена, не хватило:");
            Iterator<String> it = missing.iterator();

            while (it.hasNext()) {
               ChatUtil.a("- &c" + it.next());
            }
         }

         this.d.clear();
      }
   }

   public static class a {
      private final String a;
      private final class_1799 b;
      private final int c;

      @Generated
      public a(String name, class_1799 stack, int slotId) {
         this.a = name;
         this.b = stack;
         this.c = slotId;
      }

      @Generated
      public String a() {
         return this.a;
      }

      @Generated
      public class_1799 b() {
         return this.b;
      }

      @Generated
      public int c() {
         return this.c;
      }
   }
}
