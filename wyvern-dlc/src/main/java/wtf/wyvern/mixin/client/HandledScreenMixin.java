package wtf.wyvern.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.wyvern.client.modules.impl.misc.AuctionHelper;
import wtf.wyvern.client.modules.impl.render.ShulkerPreview;

@Mixin({HandledScreen.class})
public abstract class HandledScreenMixin {
   @Inject(
      method = {"render"},
      at = {@At("RETURN")}
   )
   public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      ShulkerPreview.INSTANCE.renderFromMixin(context, mouseX, mouseY);
   }

   @Inject(
      method = {"drawSlot"},
      at = {@At("HEAD")}
   )
   private void wyvern$drawAuctionHighlight(DrawContext context, Slot slot, CallbackInfo ci) {
      AuctionHelper helper = AuctionHelper.INSTANCE;
      if (!helper.isEnabled()) {
         return;
      }

      Byte tier = helper.getSlotHighlight().get(slot.id);
      if (tier == null) {
         return;
      }

      int color = tier == AuctionHelper.TIER_BEST ? 0x5000CC00 : 0x50FF8800;
      context.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, color);
   }
}
