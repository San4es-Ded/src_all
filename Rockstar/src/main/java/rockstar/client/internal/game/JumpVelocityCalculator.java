package rockstar.client.internal.game;


import rockstar.client.*;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import rockstar.client.MinecraftClientAccess;

public class JumpVelocityCalculator
implements MinecraftClientAccess {
    private double internalField0194;
    private double internalField0193;
    private int internalField0227;
    private int internalField0228;

    public double internalMethod01329(boolean bl, boolean bl2, boolean bl3, float f) {
        float f2;
        if (JumpVelocityCalculator.internalField0149.player == null || JumpVelocityCalculator.internalField0149.world == null) {
            return 0.0;
        }
        ClientPlayerEntity clientPlayerEntity = JumpVelocityCalculator.internalField0149.player;
        boolean bl4 = clientPlayerEntity.isOnGround();
        boolean bl5 = clientPlayerEntity.getVelocity().y > 0.0;
        float f3 = this.internalMethod03030(clientPlayerEntity);
        float f4 = this.internalMethod00274(clientPlayerEntity);
        float f5 = f2 = clientPlayerEntity.hasStatusEffect(StatusEffects.JUMP_BOOST) && clientPlayerEntity.isUsingItem() ? 0.88f : 0.91f;
        if (bl4) {
            f2 = f4;
        }
        float f6 = 0.16277136f / (f2 * f2 * f2);
        float f7 = bl4 ? f3 * f6 - (bl5 ? 4.0f : 0.0133f) : (bl && bl2 && (bl3 || JumpVelocityCalculator.internalField0149.options.jumpKey.isPressed()) ? f - 0.25f : 0.0255f);
        boolean bl6 = false;
        double d = this.internalField0194 + (double)f7;
        double d2 = 0.0;
        if (clientPlayerEntity.isUsingItem() && !bl5) {
            double d3 = this.internalField0194 + (double)(f7 * 0.25f);
            double d4 = clientPlayerEntity.getVelocity().y;
            if (d4 != 0.0 && Math.abs(d4) < 0.08) {
                d3 += 0.055;
            }
            if (d > (d2 = Math.max(0.043, d3))) {
                bl6 = true;
                ++this.internalField0228;
            } else {
                this.internalField0228 = Math.max(this.internalField0228 - 1, 0);
            }
        } else {
            this.internalField0228 = 0;
        }
        d = this.internalField0228 > 3 ? d2 - (clientPlayerEntity.hasStatusEffect(StatusEffects.JUMP_BOOST) && clientPlayerEntity.isUsingItem() ? 0.3 : 0.019) : Math.max(bl6 ? 0.0 : 0.25, d) - (this.internalField0227++ % 2 == 0 ? 0.001 : 0.002);
        this.internalField0193 = f2;
        return d;
    }

    public void internalMethod00917(double d) {
        this.internalField0194 = d * this.internalField0193;
    }

    public void internalMethod04901() {
        this.internalField0194 = 0.0;
        this.internalField0193 = 0.0;
        this.internalField0227 = 0;
        this.internalField0228 = 0;
    }

    private float internalMethod03030(ClientPlayerEntity clientPlayerEntity) {
        boolean bl = clientPlayerEntity.isSprinting();
        clientPlayerEntity.setSprinting(false);
        float f = (float)clientPlayerEntity.getAttributeValue(EntityAttributes.MOVEMENT_SPEED) * 1.3f;
        clientPlayerEntity.setSprinting(bl);
        return f;
    }

    private float internalMethod00274(ClientPlayerEntity clientPlayerEntity) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(clientPlayerEntity.getX(), clientPlayerEntity.getBoundingBox().minY - 1.0, clientPlayerEntity.getZ());
        Block block = clientPlayerEntity.getEntityWorld().getBlockState((BlockPos)mutable).getBlock();
        return block.getSlipperiness() * 0.91f;
    }

    @Generated
    public double internalMethod04899() {
        return this.internalField0194;
    }

    @Generated
    public double internalMethod04904() {
        return this.internalField0193;
    }

    @Generated
    public int internalMethod04900() {
        return this.internalField0227;
    }

    @Generated
    public int internalMethod04905() {
        return this.internalField0228;
    }
}

