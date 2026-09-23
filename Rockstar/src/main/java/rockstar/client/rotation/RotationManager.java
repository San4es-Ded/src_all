package rockstar.client.rotation;





import rockstar.client.util.*;
import rockstar.client.server.*;
import rockstar.client.event.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pyrock.events.network.SendPacketEvent;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.MathUtils;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.RotationResetMode;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationUtils;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.rotation.RotationState;
import rockstar.client.rotation.RotationRequest;
import rockstar.client.rotation.RotationEventHandler;
import rockstar.client.util.Stopwatch;

public class RotationManager
implements MinecraftClientAccess {
    private final RotationEventHandler internalField0129;
    private Rotation internalField0118 = Rotation.internalField0118;
    private final Rotation internalField0119 = Rotation.internalField0118;
    private Rotation internalField1007 = Rotation.internalField0118;
    private Rotation internalField1008 = Rotation.internalField0118;
    private RotationState internalField0126 = RotationState.internalField0126;
    @Nullable
    private RotationRequest internalField0128;
    private final Stopwatch internalField0519 = new Stopwatch();
    @Nullable
    private Rotation internalField1006;
    private final EventListener<SendPacketEvent> internalField0157 = sendPacketEvent -> {
        Packet<?> packet;
        if (this.internalMethod01525() || !((packet = sendPacketEvent.getPacket()) instanceof PlayerInteractItemC2SPacket)) {
            return;
        }
        PlayerInteractItemC2SPacket playerInteractItemC2SPacket = (PlayerInteractItemC2SPacket)packet;
        if (this.internalField1006 == null) {
            this.internalField1006 = new Rotation(this.internalField0118.internalMethod00169(), this.internalField0118.internalMethod00171());
        }
        this.internalField0119.internalMethod03239(this.internalField1006.internalMethod00169());
        this.internalField0119.internalMethod03289(this.internalField1006.internalMethod00171());
        if (playerInteractItemC2SPacket.getYaw() != this.internalField1006.internalMethod00169() || playerInteractItemC2SPacket.getPitch() != this.internalField1006.internalMethod00171()) {
            sendPacketEvent.setPacket((Packet<?>)new PlayerInteractItemC2SPacket(playerInteractItemC2SPacket.getHand(), playerInteractItemC2SPacket.getSequence(), this.internalField1006.internalMethod00169(), this.internalField1006.internalMethod00171()));
        }
    };

    public RotationManager(RotationEventHandler typedValue275) {
        this.internalField0129 = typedValue275;
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    public boolean internalMethod01525() {
        return this.internalField0126 == RotationState.internalField0126;
    }

    @ApiStatus.Internal
    public void internalMethod01524() {
        this.internalField1007 = this.internalField0118;
        if (this.internalField0128 == null) {
            this.internalField0118 = this.internalMethod07496();
            return;
        }
        if (this.internalField0519.internalMethod02365(70L)) {
            RotationResetMode typedValue265 = this.internalField0128.internalMethod01387();
            if (typedValue265 == RotationResetMode.internalField1005) {
                float f = Math.clamp(this.internalField0118.internalMethod00171(), -90.0f, 90.0f);
                float f2 = RotationManager.internalMethod02140(this.internalField0118.internalMethod00169(), f);
                this.internalField0118 = new Rotation(f2, f);
                this.internalField0126 = RotationState.internalField0126;
                this.internalField0128 = null;
                return;
            }
            if (typedValue265 == RotationResetMode.internalField0117) {
                this.internalField0118 = this.internalMethod07496();
                this.internalField0126 = RotationState.internalField0126;
                this.internalField0128 = null;
                return;
            }
            if (this.internalMethod07496().internalMethod00735(this.internalField0118) < Math.max(0.1f, RotationUtils.internalMethod03158())) {
                RotationManager.internalMethod02140(this.internalField0118.internalMethod00169(), this.internalField0118.internalMethod00171());
                this.internalField0126 = RotationState.internalField0126;
                this.internalField0128 = null;
            } else {
                Rotation typedValue266;
                this.internalField0126 = RotationState.internalField1013;
                RotationManager.internalField0149.player.setYaw(RotationUtils.internalMethod02018(RotationManager.internalField0149.player.getYaw(), RotationUtils.internalMethod08850(this.internalField0118.internalMethod00169(), RotationManager.internalField0149.player.getYaw())));
                Rotation typedValue267 = typedValue266 = this.internalField0128.internalMethod01390() == null ? null : this.internalField0128.internalMethod01390().returnStep(this.internalField0118, this.internalMethod07496());
                if (typedValue266 == null) {
                    float f = 5.0f;
                    float f3 = 88.0f;
                    if (ServerUtils.internalMethod01786(KnownServer.internalField0579)) {
                        f3 = 45.0f;
                    }
                    typedValue266 = new Rotation(RotationManager.internalMethod06343(this.internalField0118.internalMethod00169(), this.internalMethod07496().internalMethod00169(), MathUtils.internalMethod07919(f, f3)), RotationManager.internalMethod06343(this.internalField0118.internalMethod00171(), this.internalMethod07496().internalMethod00171(), MathUtils.internalMethod07919(f, f3) / MathUtils.internalMethod07919(1.9f, 2.2f)));
                }
                this.internalField0118 = RotationUtils.internalMethod02284(this.internalField0118, typedValue266);
            }
            return;
        }
        this.internalField0126 = RotationState.internalField0127;
        this.internalMethod08225();
    }

    public void internalMethod02727(float f) {
        if (RotationManager.internalField0149.player == null) {
            return;
        }
        float f2 = MathUtils.internalMethod02587(this.internalField1007.internalMethod00169(), this.internalField0118.internalMethod00169(), f);
        float f3 = this.internalField1007.internalMethod00171() + (this.internalField0118.internalMethod00171() - this.internalField1007.internalMethod00171()) * f;
        if (f3 <= -85.0f) {
            // empty if block
        }
        this.internalField1008 = new Rotation(f2, f3);
        if (RockstarClient.getInstance().internalMethod04463().internalMethod04526() != null) {
            // empty if block
        }
    }

    public void internalMethod00418(Rotation typedValue266, RotationBehavior typedValue264, float f, float f2, float f3, RotationPriority typedValue270) {
        this.internalMethod01596(typedValue266, typedValue264, f, f2, f3, typedValue270, true);
    }

    public void internalMethod01596(Rotation typedValue266, RotationBehavior typedValue264, float f, float f2, float f3, RotationPriority typedValue270, boolean bl) {
        int n = typedValue270.internalMethod06447();
        if (this.internalField0128 == null || this.internalField0128.internalMethod00377() <= n || this.internalField0126 != RotationState.internalField0127) {
            typedValue266.internalMethod03239(RotationUtils.internalMethod08850(this.internalField0128 == null ? this.internalMethod07496().internalMethod00169() : this.internalField0128.internalMethod01388().internalMethod00169(), typedValue266.internalMethod00169()));
            this.internalField0128 = new RotationRequest(typedValue266, typedValue264, f, f2, f3, n, bl);
            this.internalField0519.internalMethod00701();
            this.internalField0126 = RotationState.internalField0127;
            this.internalMethod08225();
        }
    }

    public void internalMethod03098(Rotation typedValue266, RotationBehavior typedValue264, float f, float f2, float f3) {
        this.internalMethod00418(typedValue266, typedValue264, f, f2, f3, RotationPriority.internalField0122);
    }

    public void internalMethod02154(Rotation typedValue266, RotationPriority typedValue270) {
        this.internalMethod00418(typedValue266, RotationBehavior.internalField0114, 180.0f, 180.0f, 180.0f, typedValue270);
    }

    public void internalMethod04698(Rotation typedValue266) {
        this.internalMethod00418(typedValue266, RotationBehavior.internalField0114, 180.0f, 180.0f, 180.0f, RotationPriority.internalField0122);
    }

    public void internalMethod01526() {
        if (this.internalField0128 != null && this.internalField0126 == RotationState.internalField0127) {
            this.internalField0519.internalMethod00701();
        }
    }

    public static float internalMethod02140(float f, float f2) {
        if (RotationManager.internalField0149.player == null) {
            return f;
        }
        float f3 = RotationManager.internalField0149.player.getYaw() + MathHelper.wrapDegrees((float)(f - RotationManager.internalField0149.player.getYaw()));
        float f4 = MathHelper.clamp((float)f2, (float)-90.0f, (float)90.0f);
        RotationManager.internalField0149.player.setYaw(f3);
        RotationManager.internalField0149.player.setPitch(f4);
        RotationManager.internalField0149.player.lastYaw = f3;
        RotationManager.internalField0149.player.lastPitch = f4;
        return f3;
    }

    public static float internalMethod06343(float f, float f2, float f3) {
        float f4 = RotationUtils.internalMethod08495(f, f2);
        if (Math.abs(f4) <= f3) {
            return f + f4;
        }
        return f + Math.signum(f4) * f3;
    }

    private void internalMethod08225() {
        if (this.internalField0128 == null) {
            return;
        }
        Rotation typedValue266 = new Rotation(RotationManager.internalMethod06343(this.internalField0118.internalMethod00169(), this.internalField0128.internalMethod01388().internalMethod00169(), this.internalField0128.internalMethod00376()), RotationManager.internalMethod06343(this.internalField0118.internalMethod00171(), this.internalField0128.internalMethod01388().internalMethod00171(), this.internalField0128.internalMethod00840()));
        this.internalField0118 = this.internalField0128.internalMethod00378() ? RotationUtils.internalMethod02284(this.internalField0118, typedValue266) : typedValue266;
    }

    public void internalMethod03926(Entity entity, long l, long l2, long l3, RotationPriority typedValue270, RotationBehavior typedValue264) {
        if (entity == null || RotationManager.internalField0149.player == null) {
            return;
        }
        double d = entity.getX();
        double d2 = entity.getY() + (double)entity.getEyeHeight(entity.getPose());
        double d3 = entity.getZ();
        double d4 = d - RotationManager.internalField0149.player.getX();
        double d5 = d2 - (RotationManager.internalField0149.player.getY() + (double)RotationManager.internalField0149.player.getEyeHeight(RotationManager.internalField0149.player.getPose()));
        Rotation typedValue266 = RotationManager.internalMethod05490(d3, d4, d5);
        this.internalMethod00418(typedValue266, typedValue264, l, l2, l3, typedValue270);
    }

    @NotNull
    private static Rotation internalMethod05490(double d, double d2, double d3) {
        double d4 = d - RotationManager.internalField0149.player.getZ();
        double d5 = Math.sqrt(d2 * d2 + d4 * d4);
        float f = (float)Math.toDegrees(Math.atan2(d4, d2)) - 90.0f;
        float f2 = (float)(-Math.toDegrees(Math.atan2(d3, d5)));
        Rotation typedValue266 = new Rotation(f, f2);
        return typedValue266;
    }

    public Rotation internalMethod01270(LivingEntity livingEntity) {
        return new Rotation(livingEntity.getYaw(), livingEntity.getPitch());
    }

    public Rotation internalMethod07496() {
        if (RotationManager.internalField0149.player == null) {
            return Rotation.internalField0118;
        }
        return this.internalMethod01270((LivingEntity)RotationManager.internalField0149.player);
    }

    public Rotation internalMethod00024() {
        return this.internalField0126 == RotationState.internalField0126 ? this.internalMethod07496() : this.internalMethod09074();
    }

    @Generated
    public RotationEventHandler internalMethod07552() {
        return this.internalField0129;
    }

    @Generated
    public Rotation internalMethod09074() {
        return this.internalField0118;
    }

    @Generated
    public Rotation internalMethod08209() {
        return this.internalField0119;
    }

    @Generated
    public Rotation internalMethod08456() {
        return this.internalField1007;
    }

    @Generated
    public Rotation internalMethod08582() {
        return this.internalField1008;
    }

    @Generated
    public RotationState internalMethod07550() {
        return this.internalField0126;
    }

    @Generated
    public Stopwatch internalMethod02633() {
        return this.internalField0519;
    }

    @Generated
    public EventListener<SendPacketEvent> internalMethod03799() {
        return this.internalField0157;
    }

    @Generated
    public void internalMethod01570(Rotation typedValue266) {
        this.internalField0118 = typedValue266;
    }

    @Generated
    public void internalMethod08167(Rotation typedValue266) {
        this.internalField1007 = typedValue266;
    }

    @Generated
    public void internalMethod09105(Rotation typedValue266) {
        this.internalField1008 = typedValue266;
    }

    @Generated
    public void internalMethod01493(RotationState typedValue272) {
        this.internalField0126 = typedValue272;
    }

    @Generated
    public void internalMethod01494(@Nullable RotationRequest typedValue274) {
        this.internalField0128 = typedValue274;
    }

    @Nullable
    @Generated
    public RotationRequest internalMethod07551() {
        return this.internalField0128;
    }

    @Nullable
    @Generated
    public Rotation internalMethod09639() {
        return this.internalField1006;
    }

    @Generated
    public void internalMethod08198(@Nullable Rotation typedValue266) {
        this.internalField1006 = typedValue266;
    }
}
