package moscow.rockstar.mixin.minecraft.client;

import java.io.File;
import java.nio.file.Path;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={MinecraftClient.class})
public interface IMinecraftClient {
    @Invoker(value="doItemUse")
    public void idoItemUse();

    @Accessor(value="itemUseCooldown")
    public void setUseCooldown(int localValue1);

    @Accessor(value="itemUseCooldown")
    public int getUseCooldown();

    @Mutable
    @Accessor(value="session")
    public void setSession(Session localValue1);

    @Mutable
    @Accessor(value="runDirectory")
    public void setRunDirectory(File localValue1);

    @Mutable
    @Accessor(value="resourcePackDir")
    public void setResourcePackDir(Path localValue1);
}

