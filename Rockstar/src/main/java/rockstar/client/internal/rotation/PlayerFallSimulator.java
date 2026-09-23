package rockstar.client.internal.rotation;



import rockstar.client.rotation.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import rockstar.client.MinecraftClientAccess;

public class PlayerFallSimulator {
    private final ClientPlayerEntity internalField0086;
    private double internalField0194;
    private double internalField0193;
    private double internalField1045;
    private double internalField1043;
    private double internalField1042;
    private double internalField1044;
    private final float internalField0205;
    private int internalField0227;

    public PlayerFallSimulator(ClientPlayerEntity clientPlayerEntity, double d, double d2, double d3, double d4, double d5, double d6, float f) {
        this.internalField0086 = clientPlayerEntity;
        this.internalField0194 = d;
        this.internalField0193 = d2;
        this.internalField1045 = d3;
        this.internalField1043 = d4;
        this.internalField1042 = d5;
        this.internalField1044 = d6;
        this.internalField0205 = f;
        this.internalField0227 = 0;
    }

    public static PlayerFallSimulator internalMethod02421(ClientPlayerEntity clientPlayerEntity) {
        return new PlayerFallSimulator(clientPlayerEntity, clientPlayerEntity.getEntityPos().getX(), clientPlayerEntity.getEntityPos().getY(), clientPlayerEntity.getEntityPos().getZ(), clientPlayerEntity.getVelocity().x, clientPlayerEntity.getVelocity().y, clientPlayerEntity.getVelocity().z, clientPlayerEntity.getYaw());
    }

    public boolean internalMethod00777(float f) {
        double d = this.internalField1042;
        d *= 0.98;
        return (d -= 0.08) <= (double)(-f);
    }

    public boolean internalMethod04604(float f, int n) {
        double d = this.internalField1042;
        for (int i = 0; i < n; ++i) {
            d *= 0.98;
            if (!((d -= 0.08) <= (double)(-f))) continue;
            return true;
        }
        return false;
    }

    public int internalMethod01002(float f, int n, boolean bl) {
        float f2;
        int n2 = 0;
        if (bl && (f2 = MinecraftClientAccess.internalField0149.player.getAttackCooldownProgress(0.0f)) < 0.8f) {
            double d = MinecraftClientAccess.internalField0149.player.getAttributeValue(EntityAttributes.ATTACK_SPEED);
            float f3 = (float)(20.0 / d);
            n2 = (int)Math.ceil(f3 * (1.0f - f2));
        }
        double d = this.internalField1043;
        double d2 = this.internalField1042;
        double d3 = this.internalField1044;
        double d4 = this.internalField0194;
        double d5 = this.internalField0193;
        double d6 = this.internalField1045;
        for (int i = 0; i < n; ++i) {
            d4 += d;
            d5 += d2;
            d6 += d3;
            d2 -= 0.08;
            d *= 0.91;
            d2 *= 0.98;
            d3 *= 0.91;
            if (!(d2 <= (double)(-f)) || i < n2) continue;
            return i;
        }
        return n;
    }

    @Generated
    public PlayerFallSimulator internalMethod04342(double d) {
        this.internalField0194 = d;
        return this;
    }

    @Generated
    public PlayerFallSimulator internalMethod01218(double d) {
        this.internalField0193 = d;
        return this;
    }

    @Generated
    public PlayerFallSimulator internalMethod08917(double d) {
        this.internalField1045 = d;
        return this;
    }

    @Generated
    public PlayerFallSimulator internalMethod08316(double d) {
        this.internalField1043 = d;
        return this;
    }

    @Generated
    public PlayerFallSimulator internalMethod08954(double d) {
        this.internalField1042 = d;
        return this;
    }

    @Generated
    public PlayerFallSimulator internalMethod08358(double d) {
        this.internalField1044 = d;
        return this;
    }

    @Generated
    public PlayerFallSimulator internalMethod02719(int n) {
        this.internalField0227 = n;
        return this;
    }
}

