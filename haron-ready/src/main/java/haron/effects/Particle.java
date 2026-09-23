package haron.effects;

import haron.client.MinecraftClientAccess;
import haron.core.BooleanCoercion;
import haron.effects.ParticleRenderData;
import haron.effects.ParticleSystem;
import haron.util.ColorUtils;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.render.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;

public class Particle {
    final int lifetimeTicks;
    Vec3d previousPosition;
    Vec3d position;
    Vec3d velocity;
    final float size;
    final CopyOnWriteArrayList<Particle> ownerList;
    final Identifier texture;
    final double gravity;
    final String physicsMode;
    final int color;
    private static final float VELOCITY_DAMPING = 0.98f;
    final float rotationDegrees = ThreadLocalRandom.current().nextFloat() * 360.0f;
    int ageTicks = 0;
    float opacity = 0.0f;

    /*
     * Ignored method signature, as it can't be verified against descriptor
     */
    Particle(Vec3d position, Vec3d velocity, int lifetimeTicks, float size, Identifier texture,
             CopyOnWriteArrayList<Particle> ownerList, int color, double gravity, String physicsMode) {
        this.lifetimeTicks = lifetimeTicks;
        this.ownerList = ownerList;
        this.position = position;
        this.previousPosition = position;
        this.velocity = velocity;
        this.size = size;
        this.texture = texture;
        this.color = color;
        this.gravity = gravity;
        this.physicsMode = physicsMode;
    }

    private boolean isInsideSolidBlock() {
        BlockPos blockPos = BlockPos.ofFloored((Position)this.position);
        if (!MinecraftClientAccess.c.world.getBlockState(blockPos).isSolidBlock((BlockView)MinecraftClientAccess.c.world, blockPos)) {
            return false;
        }
        double d = (double)this.size * 0.5;
        double d2 = this.position.x - (double)blockPos.getX();
        double d3 = this.position.y - (double)blockPos.getY();
        double d4 = this.position.z - (double)blockPos.getZ();
        return BooleanCoercion.from(d2 <= d || d2 >= 1.0 - d || d3 <= d || d3 >= 1.0 - d || d4 <= d || d4 >= 1.0 - d ? 0 : 1);
    }

    private void simulateCollision() {
        this.velocity = this.velocity.multiply((double)0.98f);
        this.velocity = this.velocity.add(new Vec3d(0.0, -this.gravity, 0.0));
        this.velocity = this.velocity.add(new Vec3d(ThreadLocalRandom.current().nextDouble(-0.01, 0.01), ThreadLocalRandom.current().nextDouble(-0.01, 0.01), ThreadLocalRandom.current().nextDouble(-0.01, 0.01)));
        Vec3d vec3d = this.position.add(this.velocity);
        BlockHitResult blockHitResult = MinecraftClientAccess.c.world.raycast(new RaycastContext(this.position, vec3d, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)MinecraftClientAccess.c.player));
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            Vec3d vec3d2 = blockHitResult.getPos();
            Direction direction = blockHitResult.getSide();
            Vec3d vec3d3 = new Vec3d((double)direction.getOffsetX(), (double)direction.getOffsetY(), (double)direction.getOffsetZ());
            Vec3d vec3d4 = this.velocity.subtract(vec3d3.multiply(2.0 * this.velocity.dotProduct(vec3d3)));
            this.velocity = vec3d4.length() >= 0.1 ? vec3d4.multiply(0.5) : new Vec3d(0.0, 0.0, 0.0);
            vec3d = vec3d2.add(this.velocity.normalize().multiply(0.001));
        }
        this.previousPosition = this.position;
        this.position = vec3d;
    }

    private void simulateNoCollision() {
        this.velocity = this.velocity.multiply((double)0.98f);
        this.velocity = this.velocity.add(new Vec3d(0.0, -this.gravity, 0.0));
        this.velocity = this.velocity.add(new Vec3d(ThreadLocalRandom.current().nextDouble(-0.01, 0.01), ThreadLocalRandom.current().nextDouble(-0.01, 0.01), ThreadLocalRandom.current().nextDouble(-0.01, 0.01)));
        this.previousPosition = this.position;
        this.position = this.position.add(this.velocity);
        if (this.isInsideSolidBlock()) {
            this.ownerList.remove(this);
        }
    }

    private void simulateAttraction() {
        Vec3d vec3d = MinecraftClientAccess.c.player.getPos().add(0.0, (double)MinecraftClientAccess.c.player.getEyeHeight(MinecraftClientAccess.c.player.getPose()), 0.0).subtract(this.position);
        double d = vec3d.length();
        if (d > 0.1) {
            this.velocity = this.velocity.add(vec3d.normalize().multiply(0.02 * (1.0 / Math.max(d, 1.0))));
        }
        this.velocity = this.velocity.multiply((double)0.98f);
        this.velocity = this.velocity.add(new Vec3d(ThreadLocalRandom.current().nextDouble(-0.008, 0.008), ThreadLocalRandom.current().nextDouble(-0.008, 0.008), ThreadLocalRandom.current().nextDouble(-0.008, 0.008)));
        this.previousPosition = this.position;
        this.position = this.position.add(this.velocity);
    }

    private void simulateNoPhysics() {
        this.previousPosition = this.position;
        this.position = this.position.add(this.velocity);
        if (this.isInsideSolidBlock()) {
            this.ownerList.remove(this);
        }
    }

    ParticleRenderData buildRenderData(Frustum frustum) {
        Vec3d vec3d = this.previousPosition.add(this.position.subtract(this.previousPosition).multiply((double)MinecraftClientAccess.c.getRenderTickCounter().getTickDelta(false))).add(0.0, 0.25, 0.0);
        double d = (double)this.size * 0.5;
        if (!frustum.isVisible(new Box(vec3d.x - d, vec3d.y - d, vec3d.z - d, vec3d.x + d, vec3d.y + d, vec3d.z + d))) {
            return null;
        }
        int n = ColorUtils.a(this.color, (int)(this.opacity * 255.0f));
        int n2 = n >> 16;
        int n3 = (~n2 | 0xFF) - ~n2;
        int n4 = n >> 8;
        int n5 = (~n4 | 0xFF) - ~n4;
        int n6 = (~n | 0xFF) - ~n;
        int n7 = n >> 24;
        int n8 = (~n7 | 0xFF) - ~n7;
        int n9 = 2 * (n3 | n5) - (n3 ^ n5);
        return new ParticleRenderData(vec3d, this.size, this.rotationDegrees, n3, n5, n6, n8, (float)(2 * (n9 | n6) - (n9 ^ n6)) / 765.0f);
    }

    void tick() {
        int n;
        int n2 = this.ageTicks;
        this.ageTicks = (n2 | 1) + (n2 & 1);
        if (this.ageTicks > this.lifetimeTicks) {
            this.ownerList.remove(this);
        }
        float f = 4.0f;
        if ((float)this.ageTicks <= 4.0f) {
            this.opacity = (float)this.ageTicks / 4.0f;
        } else if ((float)this.ageTicks < (float)this.lifetimeTicks - 4.0f) {
            this.opacity = 1.0f;
        } else {
            int n3 = this.ageTicks;
            n = this.lifetimeTicks;
            this.opacity = (float)((n & ~n3) - (~n & n3)) / 4.0f;
        }
        if (MinecraftClientAccess.c.world == null || MinecraftClientAccess.c.player == null) {
            this.ownerList.remove(this);
            return;
        }
        switch (this.physicsMode) {
            case "Без коллизий" -> this.simulateNoCollision();
            case "Без физики" -> this.simulateNoPhysics();
            case "Притяжение" -> this.simulateAttraction();
            default -> this.simulateCollision();
        }
    }
}
