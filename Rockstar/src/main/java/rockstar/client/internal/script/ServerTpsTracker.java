package rockstar.client.internal.script;



import rockstar.client.event.*;
import rockstar.client.*;
import java.util.Arrays;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.math.MathHelper;
import pyrock.events.network.ReceivePacketEvent;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;

public class ServerTpsTracker {
    private final float[] internalField0615 = new float[20];
    private int internalField0227 = 0;
    private long internalField0229 = -1L;
    private final EventListener<ReceivePacketEvent> internalField0157 = receivePacketEvent -> {
        if (!(receivePacketEvent.getPacket() instanceof WorldTimeUpdateS2CPacket)) {
            return;
        }
        if (this.internalField0229 != -1L) {
            float f = (float)(System.nanoTime() - this.internalField0229) / 1.0E9f;
            this.internalField0615[this.internalField0227 % this.internalField0615.length] = MathHelper.clamp((float)(20.0f / f), (float)0.0f, (float)20.0f);
            ++this.internalField0227;
        }
        this.internalField0229 = System.nanoTime();
    };

    public ServerTpsTracker() {
        Arrays.fill(this.internalField0615, 0.0f);
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    public float internalMethod00956() {
        float f = 0.0f;
        float f2 = 0.0f;
        for (float f3 : this.internalField0615) {
            if (!(f3 > 0.0f)) continue;
            f2 += f3;
            f += 1.0f;
        }
        return MathHelper.clamp((float)(f2 / f), (float)0.0f, (float)20.0f);
    }
}

