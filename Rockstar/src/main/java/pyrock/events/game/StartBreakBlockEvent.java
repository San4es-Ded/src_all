package pyrock.events.game;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.util.math.BlockPos;
import pyrock.events.EventCancellable;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="start_break_block")
public class StartBreakBlockEvent
extends EventCancellable {
    private final BlockPos blockPos;

    public StartBreakBlockEvent(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    @Generated
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
}

