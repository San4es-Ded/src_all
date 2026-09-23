package haron.modules.utilities;

import haron.core.BooleanCoercion;
import haron.entity.EntityCollector;
import haron.entity.EntityCategory;
import haron.entity.EntityInterpolation;
import haron.events.WorldRenderPostEvent;
import haron.gui.friends.FriendUtils;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.render.world.WorldRenderUtils;
import haron.settings.BooleanSetting;
import java.util.Iterator;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(a="FC Helper", b="Предпросмотр зоны действия донат-предметов", c=ModuleCategory.UTILITIES)
public class FcHelper
extends HaronModule {
    private final BooleanSetting e = new BooleanSetting("Шаровая молния", true);
    private final BooleanSetting f = new BooleanSetting("Ком слизи", true);
    private final BooleanSetting g = new BooleanSetting("Черепаший захват", true);
    private final BooleanSetting h = new BooleanSetting("Паутина судьбы", true);
    private final BooleanSetting i = new BooleanSetting("Стан", true);
    private final BooleanSetting j = new BooleanSetting("Магнитный шар", true);
    public static int a;
    public static boolean b;

    private boolean b(Box box) {
        for (PlayerEntity playerEntity : EntityCollector.<PlayerEntity>a(entity -> BooleanCoercion.from(entity instanceof PlayerEntity && box.contains(entity.getPos()) ? 1 : 0), EntityCategory.PLAYER)) {
            PlayerEntity playerEntity2;
            if (playerEntity == FcHelper.c.player || EntityInterpolation.a((LivingEntity)(playerEntity2 = playerEntity)) || FriendUtils.a(playerEntity2.getName().getString())) continue;
            return true;
        }
        return false;
    }

    private void b(MatrixStack matrixStack, float f) {
        Vec3d vec3d = this.a(f);
        double d = vec3d.y + 1.0;
        WorldRenderUtils.a(matrixStack, new Box(vec3d.x - 5.0, d, vec3d.z - 5.0, vec3d.x + 5.0, d, vec3d.z + 5.0), !this.b(new Box(vec3d.x - 5.0, d - 5.0, vec3d.z - 5.0, vec3d.x + 5.0, d + 5.0, vec3d.z + 5.0)) ? -1 : -16711936);
    }

    private void b(MatrixStack matrixStack) {
        BlockPos blockPos = FcHelper.c.player.getBlockPos();
        float f = (FcHelper.c.player.getYaw() % 360.0f + 360.0f) % 360.0f;
        BlockPos blockPos2 = f >= 315.0f || f < 45.0f ? blockPos.add(0, 0, 3) : (f < 45.0f || f >= 135.0f ? (f < 135.0f || f >= 225.0f ? blockPos.add(3, 0, 0) : blockPos.add(0, 0, -3)) : blockPos.add(-3, 0, 0));
        double minY = blockPos2.getY();
        double maxY = blockPos2.getY() + 3;
        double minX;
        double maxX;
        double minZ;
        double maxZ;
        if (f >= 315.0f || f < 45.0f || f >= 135.0f && f < 225.0f) {
            minX = blockPos2.getX() - 1;
            maxX = blockPos2.getX() + 2;
            minZ = blockPos2.getZ();
            maxZ = blockPos2.getZ() + 1;
        } else {
            minX = blockPos2.getX();
            maxX = blockPos2.getX() + 1;
            minZ = blockPos2.getZ() - 1;
            maxZ = blockPos2.getZ() + 2;
        }
        Box box = new Box(minX, minY, minZ, maxX, maxY, maxZ);
        WorldRenderUtils.a(matrixStack, box, !this.b(box) ? -1 : -16711936);
    }

    private void c(MatrixStack matrixStack, float f) {
        Vec3d vec3d = this.a(f);
        Box box = new Box(vec3d.x - 30.0, vec3d.y - 30.0, vec3d.z - 30.0, vec3d.x + 30.0, vec3d.y + 30.0, vec3d.z + 30.0);
        int n = this.b(box) ? -16711936 : -1;
        WorldRenderUtils.a(matrixStack, box, n);
    }

    private void d(MatrixStack matrixStack, float f) {
        Vec3d vec3d = this.a(f);
        WorldRenderUtils.a(matrixStack, new Box(vec3d.x - 7.0, vec3d.y + 1.0, vec3d.z - 7.0, vec3d.x + 7.0, vec3d.y + 1.0, vec3d.z + 7.0), !this.b(new Box(vec3d.x - 7.0, vec3d.y - 6.0, vec3d.z - 7.0, vec3d.x + 7.0, vec3d.y + 8.0, vec3d.z + 7.0)) ? -1 : -16711936);
    }

    private void a(MatrixStack matrixStack, float f) {
        Vec3d vec3d = this.a(f);
        Box box = new Box(vec3d.x - 10.0, vec3d.y - 10.0, vec3d.z - 10.0, vec3d.x + 10.0, vec3d.y + 10.0, vec3d.z + 10.0);
        WorldRenderUtils.a(matrixStack, box, !this.a(box) ? -1 : -16711936);
    }

    private boolean a(Box box) {
        for (PlayerEntity playerEntity : EntityCollector.<PlayerEntity>a(entity -> BooleanCoercion.from(entity instanceof PlayerEntity && box.contains(entity.getPos()) ? 1 : 0), EntityCategory.PLAYER)) {
            PlayerEntity playerEntity2;
            if (playerEntity == FcHelper.c.player || EntityInterpolation.a((LivingEntity)(playerEntity2 = playerEntity)) || FriendUtils.a(playerEntity2.getName().getString()) || playerEntity2.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.ELYTRA) continue;
            return true;
        }
        return false;
    }

    @EventHandler
    public void a(WorldRenderPostEvent kvprd92) {
        if (FcHelper.c.world == null || FcHelper.c.player == null) {
            return;
        }
        MatrixStack matrixStack = kvprd92.a();
        float f = kvprd92.b();
        Item item = FcHelper.c.player.getMainHandStack().getItem();
        Item item2 = FcHelper.c.player.getOffHandStack().getItem();
        if (((Boolean)this.e.k()).booleanValue() && (item == Items.NETHER_STAR || item2 == Items.NETHER_STAR)) {
            this.a(matrixStack, f);
        }
        if (((Boolean)this.f.k()).booleanValue() && (item == Items.SLIME_BALL || item2 == Items.SLIME_BALL)) {
            this.b(matrixStack, f);
        }
        if (((Boolean)this.g.k()).booleanValue() && (item == Items.TURTLE_SCUTE || item2 == Items.TURTLE_SCUTE)) {
            this.a(matrixStack);
        }
        if (((Boolean)this.h.k()).booleanValue() && (item == Items.COBWEB || item2 == Items.COBWEB)) {
            this.b(matrixStack);
        }
        if (((Boolean)this.i.k()).booleanValue() && (item == Items.ENDER_EYE || item2 == Items.ENDER_EYE)) {
            this.c(matrixStack, f);
        }
        if (((Boolean)this.j.k()).booleanValue() && (item == Items.FIREWORK_STAR || item2 == Items.FIREWORK_STAR)) {
            this.d(matrixStack, f);
        }
    }

    private Vec3d a(float f) {
        return new Vec3d(FcHelper.c.player.prevX + (FcHelper.c.player.getX() - FcHelper.c.player.prevX) * (double)f, FcHelper.c.player.prevY + (FcHelper.c.player.getY() - FcHelper.c.player.prevY) * (double)f, FcHelper.c.player.prevZ + (FcHelper.c.player.getZ() - FcHelper.c.player.prevZ) * (double)f);
    }

    private void a(MatrixStack matrixStack) {
        BlockPos blockPos = FcHelper.c.player.getBlockPos();
        Box box = new Box(
                blockPos.getX() - 3, blockPos.getY() - 1, blockPos.getZ() - 3,
                blockPos.getX() + 4, blockPos.getY() + 5, blockPos.getZ() + 4);
        WorldRenderUtils.a(matrixStack, box, !this.b(box) ? -1 : -16711936);
    }

    private boolean a(List<BlockPos> list) {
        for (PlayerEntity playerEntity : EntityCollector.<PlayerEntity>a(entity -> {
            return entity instanceof PlayerEntity;
        }, EntityCategory.PLAYER)) {
            if (playerEntity == FcHelper.c.player || FriendUtils.a(playerEntity.getName().getString())) continue;
            BlockPos blockPos = playerEntity.getBlockPos();
            Iterator<BlockPos> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (!blockPos.equals((Object)iterator.next())) continue;
                return true;
            }
        }
        return false;
    }
}
