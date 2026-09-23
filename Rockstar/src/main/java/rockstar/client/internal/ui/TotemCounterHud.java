package rockstar.client.internal.ui;






import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.inventory.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.item.Items;

public class TotemCounterHud extends AbstractHudWidget {
   private final ModeSetting internalField0668 = new ModeSetting(this, "\u0422\u0438\u043f");
   private final ModeSetting.InternalType0088 internalField0238 = new ModeSetting.InternalType0088(
         this.internalField0668, "\u041d\u043e\u0432\u044b\u0439"
      )
      .select();
   private boolean internalField0277;

   public TotemCounterHud() {
      super("hud.totem_counter", "hud/hotbar");
      this.showing = true;
   }

   @Override
   public void update(UiRenderContext localValue1) {
      if (this.internalField0238.isSelected()) {
         this.width = 28.0F;
         this.height = 27.0F;
      } else {
         this.width = 28.0F;
         this.height = 24.0F;
      }

      if (!this.internalField0277 && this.x == 0.0F && this.y == 0.0F) {
         this.x = localValue1.getScaledWindowWidth() / 2.0F + 101.0F;
         this.y = localValue1.getScaledWindowHeight() - 21.5F;
      }

      this.internalField0277 = true;
      super.update(localValue1);
   }

   @Override
   public void renderComponent(UiRenderContext localValue1) {
      int localValue2 = this.internalMethod03067();
      if (localValue2 > 0) {
         if (this.internalField0238.isSelected()) {
            this.internalMethod07371(localValue1, localValue2);
         } else {
            this.internalMethod01168(localValue1, localValue2);
         }
      }
   }

   private void internalMethod07371(UiRenderContext localValue1, int localValue2) {
      SizedFont localValue3 = Fonts.internalField1157.internalMethod01432(6.0F);
      String localValue4 = String.valueOf(localValue2);
      float localValue5 = this.x + 6.0F;
      float localValue6 = this.y + 5.5F;
      localValue1.drawClientRect(this.x, this.y, 28.0F, 27.0F, 1.0F, 0.0F, 7.0F, 8.0F);
      localValue1.drawItem(Items.TOTEM_OF_UNDYING, localValue5, localValue6, 1.0F);
      localValue1.drawCenteredText(localValue3, localValue4, localValue5 + 8.0F + 1.5F, localValue6 + 14.0F, ThemeColors.internalMethod08459());
   }

   private void internalMethod01168(UiRenderContext localValue1, int localValue2) {
      SizedFont localValue3 = Fonts.internalField0450.internalMethod01432(7.0F);
      String localValue4 = String.valueOf(localValue2);
      float localValue5 = this.x + 6.0F;
      float localValue6 = this.y + 3.0F;
      localValue1.drawItem(Items.TOTEM_OF_UNDYING, localValue5, localValue6, 1.0F);
      localValue1.drawCenteredText(localValue3, localValue4, localValue5 + 16.1F, localValue6 + 12.3F, ThemeColors.internalMethod08459());
   }

   @Override
   public boolean show() {
      return internalField0149.currentScreen instanceof ChatScreen || this.internalMethod03068() && this.internalMethod03070() > 0;
   }

   private int internalMethod03067() {
      int localValue1 = this.internalMethod03070();
      return localValue1 > 0 ? localValue1 : (internalField0149.currentScreen instanceof ChatScreen ? 1 : 0);
   }

   private int internalMethod03070() {
      return internalField0149.player == null
         ? 0
         : InventorySlots.internalMethod03558()
            .internalMethod07591(InventorySlots.internalMethod02872())
            .internalMethod07591(InventorySlots.internalMethod07766())
            .internalMethod07591(InventorySlots.internalMethod08231())
            .internalMethod07613(Items.TOTEM_OF_UNDYING)
            .size();
   }

   private boolean internalMethod03068() {
      AutoTotemModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(AutoTotemModule.class);
      return localValue1 != null && localValue1.isEnabled();
   }
}
