package globals.client.messages;

import globals.client.messages.Message;
import globals.shared.proto.Packets;
import lombok.Generated;
import net.minecraft.util.math.BlockPos;

public class CordsMessage
extends Message {
    private final BlockPos pos;

    public CordsMessage(Packets.InternalType0451 nestedValue0158, String string, boolean bl, BlockPos blockPos) {
        super(nestedValue0158, string, bl);
        this.pos = blockPos;
    }

    @Generated
    public BlockPos pos() {
        return this.pos;
    }
}

