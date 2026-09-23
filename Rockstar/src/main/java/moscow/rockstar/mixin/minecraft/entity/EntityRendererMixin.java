package moscow.rockstar.mixin.minecraft.entity;


import rockstar.client.esp.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rockstar.modules.visual.WardenHelperModule;
import rockstar.client.RockstarClient;
import rockstar.client.esp.EspManager;
import rockstar.client.esp.NametagEspFeature;

@Mixin({EntityRenderer.class})
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
   @Inject(
      method = {"getDisplayName"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderLabel(T localValue1, CallbackInfoReturnable<Text> localValue2) {
      if (localValue1 instanceof ArmorStandEntity localValue3) {
         WardenHelperModule localValue4 = RockstarClient.getInstance().getModuleManager().getModule(WardenHelperModule.class);
         if (localValue4 != null && localValue4.internalMethod07466(localValue3)) {
            localValue2.setReturnValue(null);
            return;
         }
      }

      if (localValue1 instanceof PlayerEntity localValue5) {
         NametagEspFeature localValue6 = EspManager.internalMethod06726().internalMethod05464(NametagEspFeature.class);
         if (localValue6 != null) {
            if (localValue6.internalMethod05662(localValue5)) {
               localValue2.setReturnValue(null);
            }
         }
      }
   }
}
