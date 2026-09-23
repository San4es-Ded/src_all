package rockstar.client.esp;





import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.game.*;
import lombok.Generated;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.Render3DEvent;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.esp.EspFeature;
import rockstar.client.esp.PlayerTargetType;
import rockstar.client.esp.EntityTargetType;
import rockstar.client.render.HudRenderUtils;
import rockstar.client.internal.game.PlayerMotionTracker;
import rockstar.client.internal.render.TaksaModel;

public class TaksaEspFeature
extends EspFeature {
    private final BooleanSetting internalField0650 = this.internalMethod02236("esp.taksa");
    private TaksaModel internalField0113;
    private final PlayerMotionTracker internalField0945 = new PlayerMotionTracker();
    private float internalField0205 = 0.0f;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if (!this.internalMethod06170(PlayerTargetType.internalField0026)) {
            return;
        }
        if (TaksaEspFeature.internalField0149.player == null) {
            return;
        }
        this.internalField0945.internalMethod00286((PlayerEntity)TaksaEspFeature.internalField0149.player);
        this.internalField0945.internalMethod07574();
    };
    private final EventListener<Render3DEvent> internalField0158 = render3DEvent -> {
        if (!this.internalMethod06170(PlayerTargetType.internalField0026)) {
            return;
        }
        if (TaksaEspFeature.internalField0149.player == null || TaksaEspFeature.internalField0149.world == null) {
            return;
        }
        if (this.internalField0113 == null) {
            this.internalField0113 = new TaksaModel(TaksaModel.internalMethod02526().createModel());
        }
        this.internalField0205 += 0.05f;
        MatrixStack matrixStack = render3DEvent.getMatrices();
        VertexConsumerProvider.Immediate immediate = internalField0149.getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer vertexConsumer = immediate.getBuffer(net.minecraft.client.render.RenderLayers.entityTranslucent(RockstarClient.id("textures/entity/taksa.png")));
        matrixStack.push();
        HudRenderUtils.internalMethod03474(matrixStack, this.internalField0945.internalMethod01179());
        int n = Math.max(TaksaEspFeature.internalField0149.world.getLightLevel(TaksaEspFeature.internalField0149.player.getBlockPos()) - 5, 15);
        int n2 = n << 20 | n << 4;
        this.internalField0113.internalMethod01267(matrixStack, vertexConsumer, n2, OverlayTexture.DEFAULT_UV, this.internalField0945, this.internalField0205);
        matrixStack.pop();
        immediate.draw();
    };

    public TaksaEspFeature() {
        super("taksa", EntityTargetType.internalField0027);
    }

    @Override
    public boolean internalMethod05541(PlayerTargetType typedValue092) {
        return typedValue092 == PlayerTargetType.internalField0026;
    }

    @Generated
    public BooleanSetting internalMethod07083() {
        return this.internalField0650;
    }

    @Generated
    public TaksaModel internalMethod02771() {
        return this.internalField0113;
    }

    @Generated
    public PlayerMotionTracker internalMethod01167() {
        return this.internalField0945;
    }

    @Generated
    public float internalMethod04626() {
        return this.internalField0205;
    }

    @Generated
    public EventListener<ClientPlayerTickEvent> internalMethod05539() {
        return this.internalField0157;
    }

    @Generated
    public EventListener<Render3DEvent> internalMethod06808() {
        return this.internalField0158;
    }
}

