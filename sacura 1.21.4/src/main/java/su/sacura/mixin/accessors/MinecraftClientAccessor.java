package su.sacura.mixin.accessors;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({MinecraftClient.class})
public interface MinecraftClientAccessor {
  @Accessor("itemUseCooldown")
  void setUseCooldown(int paramInt);
}


