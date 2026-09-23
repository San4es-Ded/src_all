package moscow.rockstar.mixin.minecraft.client.gui.screen;




import rockstar.client.server.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import moscow.rockstar.mixin.accessors.ScreenAccessor;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.server.ServerUtils;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.inventory.ArmorSlots;

@Mixin({InventoryScreen.class})
public abstract class InventoryScreenMixin extends RecipeBookScreen<PlayerScreenHandler> implements MinecraftClientAccess {
   public InventoryScreenMixin(PlayerScreenHandler localValue1, RecipeBookWidget<?> localValue2, PlayerInventory localValue3, Text localValue4) {
      super(localValue1, localValue2, localValue3, localValue4);
   }

   @Inject(
      method = {"init"},
      at = {@At("TAIL")}
   )
   private void dropButton(CallbackInfo localValue1) {
      if (!RockstarClient.internalField0240.internalMethod06896()) {
         Text localValue2 = Text.of(LanguageManager.internalMethod07214("inventory.button.drop_all"));
         Text localValue3 = Text.of(LanguageManager.internalMethod07214("\u041e\u0447\u0438\u0449\u0430\u0442\u044c \u0432\u0441\u0435"));
         int localValue4 = internalField0149.textRenderer.getWidth(localValue2) + 20;
         int localValue5 = internalField0149.textRenderer.getWidth(localValue2) + 20;
         byte localValue6 = 80;
         short localValue7 = 200;
         int localValue8 = Math.max(localValue6, Math.min(localValue7, localValue4));
         int localValue9 = Math.max(localValue6, Math.min(localValue7, localValue5));
         ButtonWidget localValue10 = ButtonWidget.builder(localValue2, localValue1x -> this.dropAll())
            .dimensions(this.x + this.backgroundWidth / 2 - localValue8 / 2, this.y - 20, localValue8, 18)
            .build();
         ButtonWidget localValue11 = ButtonWidget.builder(localValue3, localValue1x -> this.clearAll())
            .dimensions(this.x + this.backgroundWidth / 2 - localValue9 / 2, this.y - 40, localValue9, 18)
            .build();
         ((ScreenAccessor)(Object)this).invokeAddDrawableChild(localValue10);
         ((ScreenAccessor)(Object)this).invokeAddDrawableChild(localValue11);
      }
   }

   @Unique
   private void dropAll() {
      SlotCollection localValue1 = InventorySlots.internalMethod03558()
         .internalMethod07591(InventorySlots.internalMethod02872())
         .internalMethod07591(InventorySlots.internalMethod07766())
         .internalMethod07591(new ArmorSlots());

      for (InventorySlot localValue3 : (Iterable<InventorySlot>)(Iterable<?>)localValue1.internalMethod02638()) {
         if (!localValue3.internalMethod06664()) {
            internalField0149.interactionManager
               .clickSlot(internalField0149.player.currentScreenHandler.syncId, localValue3.internalMethod06662(), 1, SlotActionType.THROW, internalField0149.player);
         }
      }
   }

   @Unique
   private void clearAll() {
      if (internalField0149.player != null && internalField0149.interactionManager != null) {
         SlotCollection localValue1 = InventorySlots.internalMethod03558()
            .internalMethod07591(InventorySlots.internalMethod02872())
            .internalMethod07591(InventorySlots.internalMethod07766())
            .internalMethod07591(InventorySlots.internalMethod08231());
         int localValue2 = internalField0149.player.currentScreenHandler.syncId;

         for (InventorySlot localValue4 : (Iterable<InventorySlot>)(Iterable<?>)localValue1.internalMethod02638()) {
            if (!localValue4.internalMethod06664()) {
               if (ServerUtils.internalMethod08700()) {
                  InventoryUtils.internalMethod08821(localValue4.internalMethod06662(), 45);
               } else {
                  internalField0149.interactionManager.clickSlot(localValue2, localValue4.internalMethod06662(), 1, SlotActionType.THROW, internalField0149.player);
               }
            }
         }
      }
   }
}
