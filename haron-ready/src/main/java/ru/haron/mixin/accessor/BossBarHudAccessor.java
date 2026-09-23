package ru.haron.mixin.accessor;

import java.util.Map;
import java.util.UUID;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BossBarHud.class)
public interface BossBarHudAccessor {
    @Accessor(value="bossBars")
    public Map<UUID, ClientBossBar> getBossBars();
}
