package rockstar.modules.combat;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import pyrock.events.game.InternalAttackEvent;
import rockstar.client.setting.ModeSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.MathUtils;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationUtils;
import rockstar.client.module.Module;

@ModuleInfo(name="Criticals", category=ModuleCategory.COMBAT)
public class CriticalsModule
extends Module {
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private int internalField0227;
    private final EventListener<InternalAttackEvent> internalField0157 = internalAttackEvent -> {
        if (internalAttackEvent.isCancelled()) {
            return;
        }
        if (CriticalsModule.internalField0149.player == null || CriticalsModule.internalField0149.world == null) {
            return;
        }
        RotationManager typedValue269 = RockstarClient.getInstance().internalMethod02368();
        if (this.internalField0668.internalMethod06103(this.internalField0238)) {
            this.internalMethod01963((InternalAttackEvent)internalAttackEvent);
            return;
        }
        if (CriticalsModule.internalField0149.player.isTouchingWater()) {
            return;
        }
        if (!this.internalMethod09913()) {
            return;
        }
        Rotation typedValue266 = typedValue269.internalMethod01525() ? typedValue269.internalMethod07496() : typedValue269.internalMethod09074();
        Rotation typedValue267 = RotationUtils.internalMethod02284(typedValue269.internalMethod09074(), new Rotation(typedValue266.internalMethod00169() + MathUtils.internalMethod05368(-5.0, 5.0), typedValue266.internalMethod00171() + MathUtils.internalMethod05368(-5.0, 5.0)));
        CriticalsModule.internalField0149.player.fallDistance = MathUtils.internalMethod05368(1.0E-5f, 1.0E-4f);
        CriticalsModule.internalField0149.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.Full(CriticalsModule.internalField0149.player.getX(), CriticalsModule.internalField0149.player.getY() - (double)CriticalsModule.internalField0149.player.fallDistance, CriticalsModule.internalField0149.player.getZ(), typedValue267.internalMethod00169(), typedValue267.internalMethod00171(), CriticalsModule.internalField0149.player.isOnGround(), CriticalsModule.internalField0149.player.horizontalCollision));
    };

    public CriticalsModule() {
        this.internalMethod09912();
    }

    private void internalMethod09912() {
        this.internalField0668 = new ModeSetting(this, "modules.settings.criticals.mode");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.criticals.mode.default").select();
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.criticals.mode.reallyworld");
    }

    @Override
    public void internalMethod08229() {
        this.internalField0227 = this.internalMethod09914() ? ++this.internalField0227 : 0;
    }

    public boolean internalMethod09913() {
        if (this.internalField0668.internalMethod06103(this.internalField0238)) {
            return !this.internalMethod09263() || this.internalMethod09265();
        }
        return CriticalsModule.internalField0149.player != null && CriticalsModule.internalField0149.player.fallDistance <= 0.0f && !CriticalsModule.internalField0149.player.isOnGround() && this.internalField0227 > 0;
    }

    public boolean internalMethod09914() {
        if (!this.isEnabled() || CriticalsModule.internalField0149.player == null || CriticalsModule.internalField0149.world == null) {
            return false;
        }
        if (this.internalField0668.internalMethod06103(this.internalField0238)) {
            return this.internalMethod09263();
        }
        return CriticalsModule.internalField0149.player.fallDistance <= 0.0f && !CriticalsModule.internalField0149.player.isOnGround();
    }

    public boolean internalMethod09260() {
        return this.isEnabled() && !this.internalField0668.internalMethod06103(this.internalField0238);
    }

    private boolean internalMethod09265() {
        if (CriticalsModule.internalField0149.player == null || CriticalsModule.internalField0149.world == null || CriticalsModule.internalField0149.player.isOnGround()) {
            return false;
        }
        double d = CriticalsModule.internalField0149.player.getY();
        return d != (double)((int)d) && (CriticalsModule.internalField0149.player.isInLava() || this.internalMethod09264());
    }

    private void internalMethod01963(InternalAttackEvent internalAttackEvent) {
        float f;
        if (internalAttackEvent.getEntity() == null || internalAttackEvent.getEntity() instanceof EndCrystalEntity || !this.internalMethod09265()) {
            return;
        }
        CriticalsModule.internalField0149.player.fallDistance = f = MathUtils.internalMethod05368(1.0E-7f, 1.0E-6f);
        CriticalsModule.internalField0149.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.Full(CriticalsModule.internalField0149.player.getX(), CriticalsModule.internalField0149.player.getY() - (double)f, CriticalsModule.internalField0149.player.getZ(), CriticalsModule.internalField0149.player.getYaw(), CriticalsModule.internalField0149.player.getPitch(), false, CriticalsModule.internalField0149.player.horizontalCollision));
    }

    public boolean internalMethod09263() {
        return this.isEnabled() && CriticalsModule.internalField0149.player != null && CriticalsModule.internalField0149.world != null && (CriticalsModule.internalField0149.player.hasStatusEffect(StatusEffects.SLOW_FALLING) || this.internalMethod09264());
    }

    public boolean internalMethod09264() {
        if (CriticalsModule.internalField0149.player == null || CriticalsModule.internalField0149.world == null) {
            return false;
        }
        Box box = CriticalsModule.internalField0149.player.getBoundingBox();
        int n = (int)Math.floor(box.minX);
        int n2 = (int)Math.floor(box.minY);
        int n3 = (int)Math.floor(box.minZ);
        int n4 = (int)Math.ceil(box.maxX);
        int n5 = (int)Math.ceil(box.maxY);
        int n6 = (int)Math.ceil(box.maxZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int i = n; i < n4; ++i) {
            for (int j = n2; j < n5; ++j) {
                for (int k = n3; k < n6; ++k) {
                    if (!CriticalsModule.internalField0149.world.getBlockState((BlockPos)mutable.set(i, j, k)).isOf(Blocks.COBWEB)) continue;
                    return true;
                }
            }
        }
        return false;
    }
}
