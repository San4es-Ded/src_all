package pulse.modules.visuals;

import java.awt.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.component.DataComponentTypes;
import pulse.events.HudRenderPostEvent;
import pulse.module.ClientModule;
import pulse.module.ModuleCategory;
import pulse.module.ModuleInfo;
import pulse.render.Renderer2DImpl;
import pulse.render.font.FontManager;
import pulse.render.font.FontRenderer;
import pulse.render.world.WorldToScreen;

@ModuleInfo(a = "Arrow Nametag", b = "Показывает названия стрел, прилетевших в землю", c = ModuleCategory.VISUALS)
public class ArrowNametag extends ClientModule {
   private String getArrowName(PersistentProjectileEntity arrow) {
      if (arrow.getCustomName() != null) {
         return arrow.getCustomName().getString();
      }

      ItemStack stack = arrow.getItemStack();
      if (stack != null && !stack.isEmpty()) {
         try {
            PotionContentsComponent potionContents = (PotionContentsComponent)stack.get(DataComponentTypes.POTION_CONTENTS);
            if (potionContents != null) {
               StringBuilder potionNames = new StringBuilder();
               if (potionContents.potion().isPresent()) {
                  Potion potion = (Potion)((RegistryEntry)potionContents.potion().get()).value();

                  for (StatusEffectInstance effect : potion.getEffects()) {
                     if (potionNames.length() > 0) {
                        potionNames.append(", ");
                     }

                     potionNames.append(((StatusEffect)effect.getEffectType().value()).getName().getString());
                  }
               }

               for (StatusEffectInstance effect : potionContents.customEffects()) {
                  if (potionNames.length() > 0) {
                     potionNames.append(", ");
                  }

                  potionNames.append(((StatusEffect)effect.getEffectType().value()).getName().getString());
               }

               if (potionNames.length() > 0) {
                  return "Стрела (" + potionNames.toString() + ")";
               }
            }
         } catch (Throwable var8) {
         }

         return stack.getName().getString();
      } else {
         return "Стрела";
      }
   }

   @EventHandler
   private void onHudRender(HudRenderPostEvent event) {
      if (c.player != null && c.world != null) {
         DrawContext ctx = Renderer2DImpl.currentDrawContext;
         if (ctx != null) {
            FontRenderer font = FontManager.REGULAR[14];

            for (Entity entity : c.world.getEntities()) {
               if (entity instanceof PersistentProjectileEntity arrow && (arrow.isOnGround() || arrow.getVelocity().lengthSquared() < 0.5 || arrow.age > 10)) {
                  Vec3d arrowPos = new Vec3d(arrow.getX(), arrow.getY() + 0.3, arrow.getZ());
                  Vec3d screenPos = WorldToScreen.a(arrowPos);
                  if (screenPos != null && WorldToScreen.b(screenPos)) {
                     float sx = (float)screenPos.x;
                     float sy = (float)screenPos.y;
                     String text = "★ " + this.getArrowName(arrow);
                     float textWidth = font != null ? font.a(text) : c.textRenderer.getWidth(text);
                     int bgWidth = (int)textWidth + 8;
                     int bgHeight = 14;
                     int bgX = (int)(sx - bgWidth / 2.0F);
                     int bgY = (int)(sy - bgHeight / 2.0F);
                     ctx.fill(bgX, bgY, bgX + bgWidth, bgY + bgHeight, -1879048192);
                     if (font != null) {
                        font.a(text, sx - textWidth / 2.0F, sy - 4.5F, Color.WHITE);
                     } else {
                        ctx.drawText(c.textRenderer, text, (int)(sx - textWidth / 2.0F), (int)(sy - 4.0F), -1, true);
                     }
                  }
               }
            }
         }
      }
   }
}
