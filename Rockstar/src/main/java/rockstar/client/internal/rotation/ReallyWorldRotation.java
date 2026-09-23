package rockstar.client.internal.rotation;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.SharedConstants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import rockstar.client.setting.ModeSetting;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;
import rockstar.client.util.MathUtils;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationUtils;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.rotation.RotationState;
import rockstar.client.internal.rotation.AuraRotationMode;
import rockstar.client.internal.game.BlockTraceFilter;

public class ReallyWorldRotation
extends AuraRotationMode {
    private Rotation internalField0118 = Rotation.internalField0118;
    private int internalField1053 = -1;
    private boolean internalField0277;
    private boolean internalField0276;
    private boolean internalField1099;

    public ReallyWorldRotation(ModeSetting typedValue170) {
        super(typedValue170, "ReallyWorld");
    }

    @Override
    public void rotate(RotationManager typedValue269, float f, boolean bl, boolean bl2, RotationBehavior typedValue264, LivingEntity livingEntity) {
        Rotation typedValue266;
        if (!this.internalMethod04414()) {
            ClientMessages.internalMethod09025(Text.of((String)"\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0432\u0435\u0440\u0441\u0438\u044e \u043e\u0442 1.17 \u0434\u043e 1.20.4"));
            this.aura().disable();
            this.internalField0276 = false;
            this.internalMethod08395();
            return;
        }
        this.internalField0118 = typedValue266 = RotationUtils.internalMethod04766(livingEntity, this.aura());
        this.internalField0276 = this.internalMethod07435(livingEntity);
        if (this.internalField0276) {
            this.internalMethod05387(typedValue269);
            this.internalMethod04413();
            return;
        }
        this.internalMethod08395();
        typedValue269.internalMethod00418(typedValue266, typedValue264, 180.0f, 180.0f, 180.0f, RotationPriority.internalField1010);
    }

    @Override
    public boolean canAttack() {
        LivingEntity livingEntity;
        LivingEntity livingEntity2;
        Entity entity = RockstarClient.getInstance().internalMethod04463().internalMethod04526();
        LivingEntity livingEntity3 = livingEntity2 = entity instanceof LivingEntity ? (livingEntity = (LivingEntity)entity) : null;
        if (ReallyWorldRotation.internalField0149.player == null || ReallyWorldRotation.internalField0149.world == null || livingEntity2 == null) {
            this.internalMethod08395();
            return false;
        }
        this.internalField0276 = this.internalMethod07435(livingEntity2);
        if (!this.internalField0276) {
            this.internalMethod08395();
            return true;
        }
        if (this.internalField0277 && this.internalField1053 == livingEntity2.getId()) {
            if (this.internalMethod06415(livingEntity2)) {
                this.internalMethod04411();
                return true;
            }
            this.internalMethod06414(livingEntity2);
            return false;
        }
        this.internalMethod06414(livingEntity2);
        return false;
    }

    @Override
    public void attack() {
        this.internalMethod08395();
    }

    @Override
    public void targetNull() {
        this.internalField0276 = false;
        this.internalMethod08395();
    }

    public boolean internalMethod04412() {
        return this.internalField0276;
    }

    public boolean internalMethod06415(LivingEntity livingEntity) {
        return this.internalField0276 && (MathUtils.internalMethod04490(this.aura().internalMethod05151().internalMethod08576(), this.internalField0118.internalMethod00169(), this.internalField0118.internalMethod00171(), (Entity)ReallyWorldRotation.internalField0149.player, (Entity)livingEntity, this.aura().internalMethod05948()) || !this.aura().internalMethod09123().internalMethod04496() || this.aura().internalMethod05949().internalMethod03184() != null && this.aura().internalMethod05949().internalMethod05984() > 1);
    }

    private boolean internalMethod07435(LivingEntity livingEntity) {
        if (livingEntity == null || ReallyWorldRotation.internalField0149.player == null) {
            return false;
        }
        Rotation typedValue266 = RotationUtils.internalMethod04766(livingEntity, this.aura());
        return !MathUtils.internalMethod04490(this.aura().internalMethod05151().internalMethod08576(), typedValue266.internalMethod00169(), typedValue266.internalMethod00171(), (Entity)ReallyWorldRotation.internalField0149.player, (Entity)livingEntity, BlockTraceFilter.internalField0305);
    }

    private void internalMethod06414(LivingEntity livingEntity) {
        Rotation typedValue266 = RotationUtils.internalMethod04766(livingEntity, this.aura());
        this.internalField0118 = typedValue266 = RotationUtils.internalMethod02284(RockstarClient.getInstance().internalMethod02368().internalMethod07496(), typedValue266);
        this.internalField1053 = livingEntity.getId();
        this.internalField0277 = true;
        this.internalMethod04411();
    }

    private void internalMethod04411() {
        ReallyWorldRotation.internalField0149.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.Full(ReallyWorldRotation.internalField0149.player.getX(), ReallyWorldRotation.internalField0149.player.getY(), ReallyWorldRotation.internalField0149.player.getZ(), this.internalField0118.internalMethod00169(), this.internalField0118.internalMethod00171(), ReallyWorldRotation.internalField0149.player.isOnGround(), ReallyWorldRotation.internalField0149.player.horizontalCollision));
        ReallyWorldRotation.internalField0149.player.networkHandler.sendPacket((Packet)new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, this.internalField0118.internalMethod00169(), this.internalField0118.internalMethod00171()));
    }

    private void internalMethod04413() {
        this.internalField1099 = !this.internalField1099;
        ReallyWorldRotation.internalField0149.player.setYaw(ReallyWorldRotation.internalField0149.player.getYaw() + (this.internalField1099 ? 0.1f : -0.1f));
    }

    private void internalMethod05387(RotationManager typedValue269) {
        Rotation typedValue266 = typedValue269.internalMethod07496();
        typedValue269.internalMethod01494(null);
        typedValue269.internalMethod01493(RotationState.internalField0126);
        typedValue269.internalMethod01570(typedValue266);
        typedValue269.internalMethod08167(typedValue266);
        typedValue269.internalMethod09105(typedValue266);
    }

    private void internalMethod08395() {
        this.internalField0277 = false;
        this.internalField1053 = -1;
    }

    private boolean internalMethod04414() {
        int n = this.internalMethod04410();
        return n >= 755 && n <= 765;
    }

    private int internalMethod04410() {
        Integer n = this.internalMethod04035();
        if (n != null) {
            return n;
        }
        return SharedConstants.getProtocolVersion();
    }

    private Integer internalMethod04035() {
        try {
            Integer n;
            Class<?> clazz = Class.forName("com.viaversion.viafabricplus.ViaFabricPlus");
            Object object = clazz.getMethod("getImpl", new Class[0]).invoke(null, new Object[0]);
            Object object2 = object.getClass().getMethod("getTargetVersion", new Class[0]).invoke(object, new Object[0]);
            Object object3 = object2.getClass().getMethod("getVersion", new Class[0]).invoke(object2, new Object[0]);
            return object3 instanceof Integer ? (n = (Integer)object3) : null;
        }
        catch (LinkageError | ReflectiveOperationException | RuntimeException throwable) {
            return null;
        }
    }

    @Generated
    public Rotation internalMethod04941() {
        return this.internalField0118;
    }
}
