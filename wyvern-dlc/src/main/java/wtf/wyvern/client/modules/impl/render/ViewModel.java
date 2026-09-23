package wtf.wyvern.client.modules.impl.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "ViewModel",
   category = Category.RENDER,
   description = "Настройка позиции"
)
@FastNative
public final class ViewModel extends Module {
   public static final ViewModel INSTANCE = new ViewModel();
   public final SliderSetting leftX = new SliderSetting("Левая рука X", 0.0F, -1.0F, 1.0F, 0.1F);
   public final SliderSetting leftY = new SliderSetting("Левая рука Y", 0.0F, -1.0F, 1.0F, 0.1F);
   public final SliderSetting leftZ = new SliderSetting("Левая рука Z", 0.0F, -1.0F, 1.0F, 0.1F);
   public final SliderSetting leftScale = new SliderSetting("Левая рука размер", 1.0F, 0.5F, 1.5F, 0.05F);
   public final SliderSetting rightX = new SliderSetting("Правая рука X", 0.0F, -1.0F, 1.0F, 0.1F);
   public final SliderSetting rightY = new SliderSetting("Правая рука Y", 0.0F, -1.0F, 1.0F, 0.1F);
   public final SliderSetting rightZ = new SliderSetting("Правая рука Z", 0.0F, -1.0F, 1.0F, 0.1F);
   public final SliderSetting rightScale = new SliderSetting("Правая рука размер", 1.0F, 0.5F, 1.5F, 0.05F);

   private ViewModel() {
   }

   public void applyHandScale(MatrixStack matrices, Arm arm) {
      if (this.isEnabled()) {
         if (arm == Arm.RIGHT) {
            matrices.scale(this.rightScale.getCurrent(), this.rightScale.getCurrent(), this.rightScale.getCurrent());
         } else {
            matrices.scale(this.leftScale.getCurrent(), this.leftScale.getCurrent(), this.leftScale.getCurrent());
         }
      } else {
         matrices.scale(1.0F, 1.0F, 1.0F);
      }

   }

   public void applyHandPosition(MatrixStack matrices, Arm arm) {
      if (this.isEnabled()) {
         if (arm == Arm.RIGHT) {
            matrices.translate(this.rightX.getCurrent(), this.rightY.getCurrent(), this.rightZ.getCurrent());
         } else {
            matrices.translate(-this.leftX.getCurrent(), this.leftY.getCurrent(), this.leftZ.getCurrent());
         }
      } else {
         matrices.translate(0.0F, 0.0F, 0.0F);
      }

   }
}
