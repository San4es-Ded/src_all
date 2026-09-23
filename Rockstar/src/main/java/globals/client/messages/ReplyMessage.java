package globals.client.messages;

import globals.client.messages.Message;
import globals.shared.proto.Packets;
import lombok.Generated;
import pyrock.utility.render.Rect;

public class ReplyMessage
extends Message {
    private final long reply;
    private Rect quoteRect = Rect.EMPTY;

    public ReplyMessage(Packets.InternalType0451 nestedValue0158, String string, boolean bl, long l) {
        super(nestedValue0158, string, bl);
        this.reply = l;
    }

    @Generated
    public long reply() {
        return this.reply;
    }

    @Generated
    public Rect quoteRect() {
        return this.quoteRect;
    }

    @Generated
    public ReplyMessage quoteRect(Rect rect) {
        this.quoteRect = rect;
        return this;
    }
}

