package moscow.rockstar.mixin.accessors;

import net.minecraft.network.packet.s2c.play.PlayerRotationS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={PlayerRotationS2CPacket.class})
public interface PlayerRotationS2CPacketAccessor {
    @Mutable
    @Accessor(value="yaw")
    public void setYaw(float localValue1);

    @Mutable
    @Accessor(value="pitch")
    public void setPitch(float localValue1);
}
