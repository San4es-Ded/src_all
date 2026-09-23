package wtf.wyvern.client.modules.impl.combat;

import wtf.wyvern.core.eventbus.EventTarget;
import lombok.Generated;
import net.minecraft.item.Items;
import wtf.wyvern.core.events.impl.player.EventMoveInput;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BindSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.utility.game.player.PlayerInventoryUtil;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "ClickPearl",
   category = Category.COMBAT,
   description = "Кидает жемчуг по клавише"
)
@FastNative
public final class ClickPearl extends Module {
   private final ModeSetting mode = new ModeSetting("Мод", new String[]{"Рейдж", "Легит"});
   private final BindSetting bind = new BindSetting("Кнопка броска");
   public static final ClickPearl INSTANCE = new ClickPearl();
   private boolean ignore;
   private int legitSlowTicks;

   private ClickPearl() {
   }

   @EventTarget
   private void onKey(wtf.wyvern.core.events.impl.input.EventKey e) {
      if (e.isKeyDown(this.bind.getKeyCode())) {
         this.setIgnore(true);
         if (this.mode.is("Рейдж")) {
            PlayerInventoryUtil.swapAndUseHvH(Items.ENDER_PEARL);
         } else {
            PlayerInventoryUtil.swapAndUseLegit(Items.ENDER_PEARL);
            legitSlowTicks = 4;
         }
         this.setIgnore(false);
      }
   }

   @EventTarget
   private void onMoveInput(EventMoveInput event) {
      if (legitSlowTicks <= 0) {
         return;
      }
      event.setForward(event.getForward() * 0.65F);
      event.setStrafe(event.getStrafe() * 0.65F);
      --legitSlowTicks;
   }

   @Override
   public void onDisable() {
      legitSlowTicks = 0;
      super.onDisable();
   }

   @Generated
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ClickPearl)) {
         return false;
      } else {
         ClickPearl other = (ClickPearl)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (!super.equals(o)) {
            return false;
         } else if (this.isIgnore() != other.isIgnore()) {
            return false;
         } else {
            Object this$mode = this.getMode();
            Object other$mode = other.getMode();
            if (this$mode == null) {
               if (other$mode != null) {
                  return false;
               }
            } else if (!this$mode.equals(other$mode)) {
               return false;
            }

            Object this$bind = this.getBind();
            Object other$bind = other.getBind();
            if (this$bind == null) {
               if (other$bind != null) {
                  return false;
               }
            } else if (!this$bind.equals(other$bind)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof ClickPearl;
   }

   @Generated
   public int hashCode() {
      int PRIME = 1;
      int result = super.hashCode();
      result = result * 59 + (this.isIgnore() ? 79 : 97);
      Object $mode = this.getMode();
      result = result * 59 + ($mode == null ? 43 : $mode.hashCode());
      Object $bind = this.getBind();
      result = result * 59 + ($bind == null ? 43 : $bind.hashCode());
      return result;
   }

   @Generated
   public ModeSetting getMode() {
      return this.mode;
   }

   @Generated
   public BindSetting getBind() {
      return this.bind;
   }

   @Generated
   public boolean isIgnore() {
      return this.ignore;
   }

   @Generated
   public void setIgnore(boolean ignore) {
      this.ignore = ignore;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getMode());
      return "ClickPearl(mode=" + var10000 + ", bind=" + this.getBind() + ", ignore=" + this.isIgnore() + ")";
   }
}
