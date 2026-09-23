package haron.modules.utilities;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.core.BooleanCoercion;
import haron.entity.EntityCollector;
import haron.entity.EntityCategory;
import haron.entity.EntityInterpolation;
import haron.events.WorldRenderPostEvent;
import haron.events.SoundPlayEvent;
import haron.events.ClientTickEvent;
import haron.gui.friends.FriendUtils;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.hud.ClientColor;
import haron.render.world.WorldRenderUtils;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.SettingGroup;
import haron.settings.BooleanSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

@ModuleInfo(a="FT Helper", b="Предпросмотр зоны действия донат-предметов", c=ModuleCategory.UTILITIES)
public class FtHelper
extends HaronModule {
    private final BooleanSetting a = new BooleanSetting("Трапка", true);
    private final BooleanSetting b = new BooleanSetting("Пласт", true);
    private final BooleanSetting e = new BooleanSetting("Дезориентация", true);
    private final BooleanSetting f = new BooleanSetting("Явная пыль", true);
    private final BooleanSetting g = new BooleanSetting("Снежок заморозки", true);
    private final SettingGroup h = new SettingGroup("Настройки трапки");
    private final BooleanSetting i = new BooleanSetting("Таймер действия", true);
    private final BooleanSetting j = new BooleanSetting("Зона драконьей трапки", false);
    private final SettingGroup k = new SettingGroup("Настройки снежка");
    private final NumberSetting l = new NumberSetting("Жирность линии", 2.5f, 0.5f, 10.0f);
    private final SettingGroup m = new SettingGroup("Цвет");
    private final BooleanSetting n = new BooleanSetting("Цвет клиента", true);
    private final ColorSetting o = new ColorSetting("Кастомный цвет", Color.WHITE).a(() -> BooleanCoercion.from((Boolean)this.n.k() == false ? 1 : 0));
    private boolean p = false;
    private long q = 0L;
    private int r = 0;
    private long s = 0L;
    private boolean t = false;
    private boolean u = false;
    private Vec3d v = null;
    private final List<Vec3d> w = new CopyOnWriteArrayList<Vec3d>();

    private void b(MatrixStack matrixStack) {
        Box box;
        BlockPos blockPos;
        int n = 0;
        BlockPos blockPos2 = FtHelper.c.player.getBlockPos();
        float f = FtHelper.c.player.getYaw();
        float f2 = FtHelper.c.player.getPitch();
        int n2 = this.p().getRGB();
        boolean bl = (Boolean)this.j.k();
        if (Math.abs(f2) > 45.0f) {
            Box box2;
            if (f2 < -45.0f) {
                if (bl) {
                    int n3 = blockPos2.getX();
                    double d = (n3 & 0xFFFFFFFC) - (~n3 & 3);
                    int n4 = blockPos2.getY();
                    double d2 = (n4 | 3) + (n4 & 3);
                    int n5 = blockPos2.getZ();
                    double d3 = (n5 ^ 3) - 2 * (~n5 & 3);
                    int n6 = blockPos2.getX();
                    double d4 = (n6 ^ 4) + 2 * (n6 & 4);
                    int n7 = blockPos2.getY();
                    Box box3 = new Box(d, d2, d3, d4, (double)((n7 & 0xFFFFFFF8) + (7 & ~n7) + 2 * (n7 & 7)), (double)(blockPos2.getZ() - -5 - 1));
                    int n8 = blockPos2.getX();
                    double d5 = (double)(2 * (n8 & 0xFFFFFFFC) - (n8 ^ 3)) + 0.01;
                    int n9 = blockPos2.getY();
                    double d6 = (double)((n9 & 0xFFFFFFFC) + (3 & ~n9) + 2 * (n9 & 3)) + 0.01;
                    int n10 = blockPos2.getZ();
                    double d7 = (double)(2 * (n10 & 0xFFFFFFFC) - (n10 ^ 3)) + 0.01;
                    int n11 = blockPos2.getX();
                    box2 = new Box(d5, d6, d7, (double)((n11 ^ 4) + 2 * (n11 & 4)) - 0.01, (double)(blockPos2.getY() - -8 - 1) - 0.01, (double)(blockPos2.getZ() - -5 - 1) - 0.01);
                } else {
                    double d = blockPos2.getX() - 3 + 1;
                    int n12 = blockPos2.getY();
                    double d8 = 2 * (n12 | 3) - (n12 ^ 3);
                    int n13 = blockPos2.getZ();
                    double d9 = 2 * (n13 & 0xFFFFFFFD) - (n13 ^ 2);
                    int n14 = blockPos2.getX();
                    double d10 = (n14 | 3) + (n14 & 3);
                    int n15 = blockPos2.getY();
                    Box box4 = new Box(d, d8, d9, d10, (double)(2 * (n15 | 5) - (n15 ^ 5)), (double)(blockPos2.getZ() - -4 - 1));
                    double d11 = (double)(blockPos2.getX() - 3 + 1) + 0.01;
                    double d12 = (double)(blockPos2.getY() - -4 - 1) + 0.01;
                    int n16 = blockPos2.getZ();
                    double d13 = (double)(2 * (n16 & 0xFFFFFFFD) - (n16 ^ 2)) + 0.01;
                    int n17 = blockPos2.getX();
                    double d14 = (double)(2 * (n17 | 3) - (n17 ^ 3)) - 0.01;
                    int n18 = blockPos2.getY();
                    double d15 = (double)((n18 | 5) + (n18 & 5)) - 0.01;
                    int n19 = blockPos2.getZ();
                    box2 = new Box(d11, d12, d13, d14, d15, (double)(2 * (n19 | 3) - (n19 ^ 3)) - 0.01);
                }
            } else if (bl) {
                int n20 = blockPos2.getX();
                double d = 2 * (n20 & 0xFFFFFFFC) - (n20 ^ 3);
                int n21 = blockPos2.getY();
                double d16 = 2 * (n21 & 0xFFFFFFFC) - (n21 ^ 3);
                int n22 = blockPos2.getZ();
                double d17 = (n22 & 0xFFFFFFFC) - (~n22 & 3);
                int n23 = blockPos2.getX();
                double d18 = 2 * (n23 | 4) - (n23 ^ 4);
                int n24 = blockPos2.getY();
                double d19 = (n24 ^ 1) - 2 * (~n24 & 1);
                int n25 = blockPos2.getZ();
                Box box5 = new Box(d, d16, d17, d18, d19, (double)(2 * (n25 | 4) - (n25 ^ 4)));
                int n26 = blockPos2.getX();
                double d20 = (double)((n26 ^ 3) - 2 * (~n26 & 3)) + 0.01;
                double d21 = (double)(blockPos2.getY() - 3) + 0.01;
                int n27 = blockPos2.getZ();
                double d22 = (double)((n27 & 0xFFFFFFFC) - (~n27 & 3)) + 0.01;
                int n28 = blockPos2.getX();
                double d23 = (double)((n28 ^ 4) + 2 * (n28 & 4)) - 0.01;
                double d24 = (double)(blockPos2.getY() - 2 + 1) - 0.01;
                int n29 = blockPos2.getZ();
                box2 = new Box(d20, d21, d22, d23, d24, (double)((n29 ^ 4) + 2 * (n29 & 4)) - 0.01);
            } else {
                double d = blockPos2.getX() - 2;
                int n30 = blockPos2.getY();
                double d25 = (n30 ^ 3) - 2 * (~n30 & 3);
                double d26 = blockPos2.getZ() - 3 + 1;
                int n31 = blockPos2.getX();
                double d27 = 2 * (n31 | 3) - (n31 ^ 3);
                int n32 = blockPos2.getY();
                double d28 = (n32 ^ 1) - 2 * (~n32 & 1);
                int n33 = blockPos2.getZ();
                Box box6 = new Box(d, d25, d26, d27, d28, (double)((n33 & 0xFFFFFFFC) + (3 & ~n33) + 2 * (n33 & 3)));
                int n34 = blockPos2.getX();
                double d29 = (double)(2 * (n34 & 0xFFFFFFFD) - (n34 ^ 2)) + 0.01;
                double d30 = (double)(blockPos2.getY() - 4 + 1) + 0.01;
                double d31 = (double)(blockPos2.getZ() - 3 + 1) + 0.01;
                int n35 = blockPos2.getX();
                double d32 = (double)((n35 | 3) + (n35 & 3)) - 0.01;
                int n36 = blockPos2.getY();
                double d33 = (double)(2 * (n36 & 0xFFFFFFFE) - (n36 ^ 1)) - 0.01;
                int n37 = blockPos2.getZ();
                box2 = new Box(d29, d30, d31, d32, d33, (double)(2 * (n37 | 3) - (n37 ^ 3)) - 0.01);
            }
            WorldRenderUtils.b(matrixStack, box2, n2);
            return;
        }
        float f3 = (f % 360.0f + 360.0f) % 360.0f;
        if (Math.abs(f3 - 45.0f) < 22.0f || Math.abs(f3 - 135.0f) < 22.0f || Math.abs(f3 - 225.0f) < 22.0f || Math.abs(f3 - 315.0f) < 22.0f) {
            int n38;
            int n39;
            int n40;
            float f4 = Math.abs(f3 - 45.0f);
            float f5 = Math.abs(f3 - 135.0f);
            float f6 = Math.abs(f3 - 225.0f);
            float f7 = Math.min(Math.min(f4, f5), Math.min(f6, Math.abs(f3 - 315.0f)));
            int n41 = f7 == f4 ? 45 : (f7 == f5 ? 135 : (f7 == f6 ? 225 : 315));
            Math.toRadians(n41);
            if (n41 == 45) {
                n40 = -3;
                n39 = 2;
            } else if (n41 == 135) {
                n40 = -3;
                n39 = -2;
            } else if (n41 == 225) {
                n40 = 2;
                n39 = -2;
            } else {
                n40 = 2;
                n39 = 2;
            }
            BlockPos blockPos3 = blockPos2.add(n40, -1, n39);
            ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
            int n42 = bl ? -3 : -2;
            int n43 = bl ? 3 : 2;
            int n44 = n38 = bl ? 7 : 5;
            if (n41 == 45 || n41 == 225) {
                for (int i = n42; i <= n43; ++i) {
                    for (int j = 0; j < n38; ++j) {
                        arrayList.add(blockPos3.add(i, j, i));
                        arrayList.add(blockPos3.add(i - -2 - 1, j, i));
                    }
                }
            } else {
                for (int i = n42; i <= n43; ++i) {
                    for (int j = 0; j < n38; ++j) {
                        arrayList.add(blockPos3.add(i, j, -i));
                        int n45 = i;
                        arrayList.add(blockPos3.add((n45 ^ 1) + 2 * (n45 & 1), j, -i));
                    }
                }
            }
            WorldRenderUtils.b(matrixStack, arrayList, n2);
            return;
        }
        if (f3 >= 315.0f || f3 < 45.0f) {
            blockPos = blockPos2.add(0, -1, 3);
        } else if (f3 >= 45.0f && f3 < 135.0f) {
            n = 90;
            blockPos = blockPos2.add(-3, -1, 0);
        } else {
            blockPos = f3 < 135.0f || f3 >= 225.0f ? blockPos2.add(3, -1, 0) : blockPos2.add(0, -1, -3);
        }
        if (n == 0 || n == 180) {
            if (bl) {
                double d = blockPos.getY();
                int n46 = blockPos.getZ();
                double d34 = (n46 ^ 1) - 2 * (~n46 & 1);
                int n47 = blockPos.getX();
                double d35 = 2 * (n47 | 4) - (n47 ^ 4);
                int n48 = blockPos.getY();
                double d36 = (n48 & 0xFFFFFFF8) + (7 & ~n48) + 2 * (n48 & 7);
                int n49 = blockPos.getZ();
                Box box7 = new Box((double)(blockPos.getX() - 4 + 1), d, d34, d35, d36, (double)((n49 & 0xFFFFFFFE) + (1 & ~n49) + 2 * (n49 & 1)));
                int n50 = blockPos.getX();
                double d37 = (double)((n50 & 0xFFFFFFFC) - (~n50 & 3)) + 0.01;
                double d38 = (double)blockPos.getY() + 0.01;
                double d39 = (double)(blockPos.getZ() - 1) + 0.01;
                int n51 = blockPos.getX();
                double d40 = (double)((n51 ^ 4) + 2 * (n51 & 4)) - 0.01;
                int n52 = blockPos.getY();
                double d41 = (double)((n52 & 0xFFFFFFF8) + (7 & ~n52) + 2 * (n52 & 7)) - 0.01;
                int n53 = blockPos.getZ();
                box = new Box(d37, d38, d39, d40, d41, (double)(2 * (n53 | 1) - (n53 ^ 1)) - 0.01);
            } else {
                double d = blockPos.getX() - 2;
                double d42 = blockPos.getY();
                int n54 = blockPos.getZ();
                double d43 = (n54 & 0xFFFFFFFE) - (~n54 & 1);
                int n55 = blockPos.getX();
                double d44 = (n55 | 3) + (n55 & 3);
                int n56 = blockPos.getY();
                double d45 = (n56 ^ 5) + 2 * (n56 & 5);
                int n57 = blockPos.getZ();
                Box box8 = new Box(d, d42, d43, d44, d45, (double)((n57 | 1) + (n57 & 1)));
                int n58 = blockPos.getX();
                double d46 = (double)((n58 & 0xFFFFFFFD) - (~n58 & 2)) + 0.01;
                double d47 = (double)blockPos.getY() + 0.01;
                double d48 = (double)(blockPos.getZ() - 1) + 0.01;
                int n59 = blockPos.getX();
                double d49 = (double)((n59 | 3) + (n59 & 3)) - 0.01;
                int n60 = blockPos.getY();
                double d50 = (double)((n60 | 5) + (n60 & 5)) - 0.01;
                int n61 = blockPos.getZ();
                box = new Box(d46, d47, d48, d49, d50, (double)(2 * (n61 | 1) - (n61 ^ 1)) - 0.01);
            }
        } else if (bl) {
            double d = blockPos.getX() - 1;
            double d51 = blockPos.getY();
            int n62 = blockPos.getZ();
            double d52 = 2 * (n62 & 0xFFFFFFFC) - (n62 ^ 3);
            int n63 = blockPos.getX();
            double d53 = 2 * (n63 | 1) - (n63 ^ 1);
            double d54 = blockPos.getY() - -8 - 1;
            int n64 = blockPos.getZ();
            Box box9 = new Box(d, d51, d52, d53, d54, (double)(2 * (n64 | 4) - (n64 ^ 4)));
            int n65 = blockPos.getX();
            double d55 = (double)(2 * (n65 & 0xFFFFFFFE) - (n65 ^ 1)) + 0.01;
            double d56 = (double)blockPos.getY() + 0.01;
            int n66 = blockPos.getZ();
            double d57 = (double)((n66 ^ 3) - 2 * (~n66 & 3)) + 0.01;
            int n67 = blockPos.getX();
            double d58 = (double)((n67 & 0xFFFFFFFE) + (1 & ~n67) + 2 * (n67 & 1)) - 0.01;
            int n68 = blockPos.getY();
            double d59 = (double)((n68 ^ 7) + 2 * (n68 & 7)) - 0.01;
            int n69 = blockPos.getZ();
            box = new Box(d55, d56, d57, d58, d59, (double)(2 * (n69 | 4) - (n69 ^ 4)) - 0.01);
        } else {
            int n70 = blockPos.getX();
            double d = 2 * (n70 & 0xFFFFFFFE) - (n70 ^ 1);
            double d60 = blockPos.getY();
            int n71 = blockPos.getZ();
            double d61 = 2 * (n71 & 0xFFFFFFFD) - (n71 ^ 2);
            double d62 = blockPos.getX() - -2 - 1;
            int n72 = blockPos.getY();
            double d63 = (n72 & 0xFFFFFFFA) + (5 & ~n72) + 2 * (n72 & 5);
            int n73 = blockPos.getZ();
            Box box10 = new Box(d, d60, d61, d62, d63, (double)(2 * (n73 | 3) - (n73 ^ 3)));
            double d64 = (double)blockPos.getY() + 0.01;
            int n74 = blockPos.getZ();
            double d65 = (double)((n74 & 0xFFFFFFFD) - (~n74 & 2)) + 0.01;
            int n75 = blockPos.getX();
            double d66 = (double)((n75 & 0xFFFFFFFE) + (1 & ~n75) + 2 * (n75 & 1)) - 0.01;
            int n76 = blockPos.getY();
            double d67 = (double)((n76 & 0xFFFFFFFA) + (5 & ~n76) + 2 * (n76 & 5)) - 0.01;
            int n77 = blockPos.getZ();
            box = new Box((double)(blockPos.getX() - 2 + 1) + 0.01, d64, d65, d66, d67, (double)(2 * (n77 | 3) - (n77 ^ 3)) - 0.01);
        }
        WorldRenderUtils.b(matrixStack, box, n2);
    }

    private Vec3d b(float f) {
        return this.a(FtHelper.c.player.getYaw(), FtHelper.c.player.getPitch()).multiply(1.5).add(new Vec3d(0.0, FtHelper.c.player.getVelocity().y * 0.5, 0.0));
    }

    private void b(MatrixStack matrixStack, float f) {
        Vec3d position = this.c(f);
        WorldRenderUtils.a(matrixStack, position, 10.0f, 64, this.a(position, 10.0) ? -16711936 : -1, 0.1f);
    }

    private Vec3d c(float f) {
        return new Vec3d(FtHelper.c.player.prevX + (FtHelper.c.player.getX() - FtHelper.c.player.prevX) * (double)f, FtHelper.c.player.prevY + (FtHelper.c.player.getY() - FtHelper.c.player.prevY) * (double)f, FtHelper.c.player.prevZ + (FtHelper.c.player.getZ() - FtHelper.c.player.prevZ) * (double)f);
    }

    private void c(MatrixStack matrixStack, float f) {
        Vec3d position = this.c(f);
        WorldRenderUtils.a(matrixStack, position, 10.0f, 64, this.a(position, 10.0) ? -16711936 : -1, 0.1f);
    }

    private int n() {
        int n = 0;
        for (int i = 0; i < FtHelper.c.player.getInventory().size(); ++i) {
            ItemStack itemStack = FtHelper.c.player.getInventory().getStack(i);
            if (itemStack.getItem() != Items.NETHERITE_SCRAP) continue;
            int n2 = itemStack.getCount();
            int n3 = n;
            n = 2 * (n3 | n2) - (n3 ^ n2);
        }
        return n;
    }

    private void a(MatrixStack matrixStack, float f) {
        this.w.clear();
        this.a(f);
        if (this.w.size() < 2) {
            return;
        }
        Vec3d vec3d = FtHelper.c.gameRenderer.getCamera().getPos();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        RenderSystem.lineWidth((float)this.l.a());
        Color color = this.p();
        int n = color.getRed();
        int n2 = color.getGreen();
        int n3 = color.getBlue();
        int n4 = color.getAlpha();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        for (Vec3d vec3d2 : this.w) {
            Vec3d vec3d3 = vec3d2.subtract(vec3d);
            bufferBuilder.vertex(matrix4f, (float)vec3d3.x, (float)vec3d3.y, (float)vec3d3.z).color(n, n2, n3, n4);
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.lineWidth((float)1.0f);
        GL11.glDisable((int)2848);
        RenderSystem.enableCull();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    private boolean a(Box box) {
        for (PlayerEntity playerEntity : EntityCollector.<PlayerEntity>a(entity -> BooleanCoercion.from(entity instanceof PlayerEntity && box.contains(entity.getPos()) ? 1 : 0), EntityCategory.PLAYER)) {
            PlayerEntity playerEntity2;
            if (playerEntity == FtHelper.c.player || EntityInterpolation.a((LivingEntity)(playerEntity2 = playerEntity)) || FriendUtils.a(playerEntity2.getName().getString())) continue;
            return true;
        }
        return false;
    }

    private boolean a(Vec3d vec3d, double d) {
        double d2 = d * d;
        for (PlayerEntity playerEntity : EntityCollector.<PlayerEntity>a(entity -> BooleanCoercion.from(!(entity instanceof PlayerEntity) || entity.squaredDistanceTo(vec3d) > d2 ? 0 : 1), EntityCategory.PLAYER)) {
            PlayerEntity playerEntity2;
            if (playerEntity == FtHelper.c.player || EntityInterpolation.a((LivingEntity)(playerEntity2 = playerEntity)) || FriendUtils.a(playerEntity2.getName().getString())) continue;
            return true;
        }
        return false;
    }

    private void a(MatrixStack matrixStack, boolean bl) {
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        if (FtHelper.c.world == null || FtHelper.c.player == null) {
            return;
        }
        if (((Boolean)this.i.k()).booleanValue()) {
            int n = this.n();
            boolean bl = FtHelper.c.player.getItemCooldownManager().isCoolingDown(Items.NETHERITE_SCRAP.getDefaultStack());
            if (bl && !this.u) {
                long l = System.currentTimeMillis();
                int n2 = l - this.s < 500L ? 1 : 0;
                this.p = true;
                this.q = l;
                this.t = BooleanCoercion.from(n2);
            }
            this.u = bl;
            this.r = n;
        }
        if (this.p && ((Boolean)this.i.k()).booleanValue()) {
            this.o();
        }
    }

    @EventHandler
    public void a(WorldRenderPostEvent kvprd92) {
        if (FtHelper.c.world == null || FtHelper.c.player == null) {
            return;
        }
        MatrixStack matrixStack = kvprd92.a();
        float f = kvprd92.b();
        Item item = FtHelper.c.player.getMainHandStack().getItem();
        Item item2 = FtHelper.c.player.getOffHandStack().getItem();
        if (((Boolean)this.a.k()).booleanValue() && (item == Items.NETHERITE_SCRAP || item2 == Items.NETHERITE_SCRAP)) {
            this.a(matrixStack);
        }
        if (((Boolean)this.b.k()).booleanValue() && (item == Items.DRIED_KELP || item2 == Items.DRIED_KELP)) {
            this.b(matrixStack);
        }
        if (((Boolean)this.e.k()).booleanValue() && (item == Items.ENDER_EYE || item2 == Items.ENDER_EYE)) {
            this.b(matrixStack, f);
        }
        if (((Boolean)this.f.k()).booleanValue() && (item == Items.SUGAR || item2 == Items.SUGAR)) {
            this.c(matrixStack, f);
        }
        if (((Boolean)this.g.k()).booleanValue() && (item == Items.SNOWBALL || item2 == Items.SNOWBALL)) {
            this.a(matrixStack, f);
        }
    }

    private boolean a(List<BlockPos> list) {
        for (PlayerEntity playerEntity : EntityCollector.<PlayerEntity>a(entity -> {
            int n = 151;
            return entity instanceof PlayerEntity;
        }, EntityCategory.PLAYER)) {
            if (playerEntity == FtHelper.c.player || FriendUtils.a(playerEntity.getName().getString())) continue;
            BlockPos blockPos = playerEntity.getBlockPos();
            Iterator<BlockPos> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (!blockPos.equals((Object)iterator.next())) continue;
                return true;
            }
        }
        return false;
    }

    private void a(MatrixStack matrixStack) {
        Box box;
        BlockPos blockPos = FtHelper.c.player.getBlockPos();
        if (((Boolean)this.j.k()).booleanValue()) {
            double d = blockPos.getX() - 4 + 1;
            double d2 = blockPos.getY();
            double d3 = blockPos.getZ() - 4 + 1;
            double d4 = blockPos.getX() - -5 - 1;
            int n = blockPos.getY();
            double d5 = (n & 0xFFFFFFF9) + (6 & ~n) + 2 * (n & 6);
            int n2 = blockPos.getZ();
            Box box2 = new Box(d, d2, d3, d4, d5, (double)((n2 & 0xFFFFFFFB) + (4 & ~n2) + 2 * (n2 & 4)));
            int n3 = blockPos.getX();
            double d6 = (double)((n3 ^ 3) - 2 * (~n3 & 3)) + 0.01;
            double d7 = (double)blockPos.getY() + 0.01;
            int n4 = blockPos.getZ();
            double d8 = (double)(2 * (n4 & 0xFFFFFFFC) - (n4 ^ 3)) + 0.01;
            int n5 = blockPos.getX();
            double d9 = (double)((n5 ^ 4) + 2 * (n5 & 4)) - 0.01;
            int n6 = blockPos.getY();
            double d10 = (double)(2 * (n6 | 6) - (n6 ^ 6)) - 0.01;
            int n7 = blockPos.getZ();
            box = new Box(d6, d7, d8, d9, d10, (double)(2 * (n7 | 4) - (n7 ^ 4)) - 0.01);
        } else {
            int n = blockPos.getX();
            double d = (n ^ 2) - 2 * (~n & 2);
            double d11 = blockPos.getY();
            double d12 = blockPos.getZ() - 2;
            int n8 = blockPos.getX();
            double d13 = 2 * (n8 | 3) - (n8 ^ 3);
            int n9 = blockPos.getY();
            double d14 = 2 * (n9 | 4) - (n9 ^ 4);
            int n10 = blockPos.getZ();
            Box box3 = new Box(d, d11, d12, d13, d14, (double)((n10 | 3) + (n10 & 3)));
            int n11 = blockPos.getX();
            double d15 = (double)((n11 & 0xFFFFFFFD) - (~n11 & 2)) + 0.01;
            double d16 = (double)blockPos.getY() + 0.01;
            int n12 = blockPos.getZ();
            double d17 = (double)((n12 & 0xFFFFFFFD) - (~n12 & 2)) + 0.01;
            int n13 = blockPos.getX();
            double d18 = (double)((n13 & 0xFFFFFFFC) + (3 & ~n13) + 2 * (n13 & 3)) - 0.01;
            int n14 = blockPos.getY();
            box = new Box(d15, d16, d17, d18, (double)((n14 ^ 4) + 2 * (n14 & 4)) - 0.01, (double)(blockPos.getZ() - -4 - 1) - 0.01);
        }
        int n = this.p().getRGB();
        WorldRenderUtils.b(matrixStack, box, n);
    }

    @EventHandler
    public void a(SoundPlayEvent lst7uj2) {
        if (lst7uj2.d().getId().toString().equals("minecraft:entity.ender_dragon.growl")) {
            this.s = System.currentTimeMillis();
        }
    }

    private Vec3d a(float f, float f2) {
        float f3 = MathHelper.cos((float)(f2 * ((float)Math.PI / 180)));
        float f4 = MathHelper.sin((float)(f2 * ((float)Math.PI / 180)));
        float f5 = MathHelper.cos((float)(f * ((float)Math.PI / 180)));
        return new Vec3d((double)(-MathHelper.sin((float)(f * ((float)Math.PI / 180))) * f3), (double)(-f4), (double)(f5 * f3)).normalize();
    }

    private Vec3d a(float f) {
        Vec3d vec3d = FtHelper.c.player.getCameraPosVec(f);
        Vec3d vec3d2 = this.b(f);
        Vec3d vec3d3 = vec3d;
        this.w.add(vec3d3);
        for (int i = 0; i < 200; ++i) {
            Vec3d vec3d4 = vec3d3;
            BlockHitResult blockHitResult = FtHelper.c.world.raycast(new RaycastContext(vec3d4, vec3d3 = vec3d3.add(vec3d2), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)FtHelper.c.player));
            if (blockHitResult.getType() != HitResult.Type.MISS) {
                this.w.add(blockHitResult.getPos());
                return blockHitResult.getPos();
            }
            if (vec3d3.y < (double)FtHelper.c.world.getBottomY()) {
                this.w.add(vec3d3);
                return vec3d3;
            }
            this.w.add(vec3d3);
            vec3d2 = vec3d2.multiply(0.99).subtract(0.0, 0.03, 0.0);
        }
        return null;
    }

    private void o() {
        int n;
        int n2 = n = this.t ? 30000 : 15000;
        if (System.currentTimeMillis() - this.q >= (long)n) {
            this.p = false;
            this.t = false;
        } else {
            FtHelper.c.inGameHud.setOverlayMessage(Text.of((String)String.format("%s закончится через §b%.1f§f сек.", this.t ? "Драконья трапка" : "Трапка", (double)((long)n - (System.currentTimeMillis() - this.q)) / 1000.0)), false);
        }
    }

    private Color p() {
        Color color;
        if (!((Boolean)this.n.k()).booleanValue()) {
            color = (Color)this.o.k();
        } else {
            ClientColor byzlib2 = ModuleManager.CLIENT_COLOR;
            color = byzlib2.n();
        }
        return color;
    }
}
