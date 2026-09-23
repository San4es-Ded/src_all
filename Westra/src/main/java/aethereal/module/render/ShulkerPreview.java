package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.ContainerEvent;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.util.ProjectUtil;
import java.util.List;
import net.minecraft.class_1542;
import net.minecraft.class_1735;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_1921;
import net.minecraft.class_2480;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_9288;
import net.minecraft.class_9334;
import org.joml.Vector2f;
import platform.inject.accessors.HandledScreenAccessor;

@ModuleRegister(
   a = "Shulker Preview",
   b = "Показывает содержимое шалкеров в инвентаре зажав ALT (и на земле, без зажатия, если античит слабый)",
   c = Category.Render
)
public class ShulkerPreview extends Module {
   private final Vector2f b = new Vector2f(175.0F, 70.0F);

   @EventTarget
   public void a(ContainerEvent event) {
      class_1735 hovered;
      if (event.h() == ContainerEvent.Phase.POST
         && (hovered = ((HandledScreenAccessor)event.b()).getFocusedSlot()) != null
         && hovered.method_7677() != null
         && hovered.method_7681()) {
         class_1799 hoveredStack = hovered.method_7677();
         if (hoveredStack.method_57824(class_9334.field_49622) != null
            && hoveredStack.method_7909() instanceof class_1747 class_1747VarMethod_7909
            && class_1747VarMethod_7909.method_7711() instanceof class_2480) {
            this.a(
               event.d(),
               hoveredStack,
               ((class_9288)hoveredStack.method_57824(class_9334.field_49622)).method_57489().toList(),
               event.f() + 8,
               event.g() - this.b.y() - 16.0F,
               1.0F,
               true
            );
         }
      }
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b()) {
         for (class_1542 itemEntity : aM_.field_1687.method_8390(class_1542.class, aM_.field_1724.method_5829().method_1014(64.0), entity -> true)) {
            class_1799 stack;
            if (itemEntity.method_6983().method_7909() instanceof class_1747 class_1747VarMethod_7909
               && class_1747VarMethod_7909.method_7711() instanceof class_2480
               && (stack = itemEntity.method_6983()) != null
               && !stack.method_7960()) {
               Vector2f projected = ProjectUtil.a(
                  itemEntity.field_6014 + (itemEntity.method_23317() - itemEntity.field_6014) * aM_.method_61966().method_60637(false),
                  itemEntity.field_6036 + (itemEntity.method_23318() - itemEntity.field_6036) * aM_.method_61966().method_60637(false) + 0.5,
                  itemEntity.field_5969 + (itemEntity.method_23321() - itemEntity.field_5969) * aM_.method_61966().method_60637(false)
               );
               class_9288 container = (class_9288)stack.method_57824(class_9334.field_49622);
               if (container != null && !container.method_57489().toList().isEmpty()) {
                  this.a(
                     event.i(),
                     stack,
                     container.method_57489().toList(),
                     projected.x() - this.b.x() * 0.5F / 2.0F,
                     projected.y() - this.b.y() * 0.5F,
                     0.5F,
                     false
                  );
               }
            }
         }
      }
   }

   private void a(class_332 context, class_1799 itemStack, List<class_1799> stacks, float x, float y, float scale, boolean overlay) {
      class_4587 matrices = context.method_51448();
      matrices.method_22903();
      matrices.method_46416(x, y, 500.0F);
      matrices.method_22905(scale, scale, 1.0F);
      context.method_25291(
         class_1921::method_62277,
         class_2960.method_60655("westra", "pictures/minecraft/3x9.png"),
         0,
         0,
         0.0F,
         0.0F,
         (int)this.b.x(),
         (int)this.b.y(),
         256,
         256,
         ColorUtil.a(((class_1747)itemStack.method_7909()).method_7711().method_26403().field_16011, 255)
      );

      for (int i = 0; i < Math.min(stacks.size(), 27); i++) {
         class_1799 stack = stacks.get(i);
         if (stack != null && !stack.method_7960()) {
            int slotX = 9 + i % 9 * 18 + 1;
            int slotY = 9 + i / 9 * 18 + 1;
            Westra.h().d().j().a(context, stack, slotX, slotY, 0, 1.0F, 0.75F, overlay);
         }
      }

      matrices.method_22909();
   }
}
