package su.sacura.features.modules.impl.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.joml.Matrix4f;
import su.sacura.events.player.EventAttack;
import su.sacura.events.render.EventRender3D;
import su.sacura.events.tick.EventUpdate;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.settings.impl.BooleanSetting;
import su.sacura.features.modules.settings.impl.ModeListSetting;
import su.sacura.features.modules.settings.impl.ModeSetting;
import su.sacura.features.modules.settings.impl.SliderSetting;
import su.sacura.util.impl.math.helper.MathUtil;
import su.sacura.util.impl.render.RenderHelper;
import su.sacura.util.impl.render.providers.ColorProvider;
import su.sacura.util.type.ISetting;
import su.sacura.util.type.MinecraftWrapper;

@ModuleAnnotations(name = "Particles", category = Category.RENDER)
public class ParticlesModule extends Module {
    ModeListSetting call = (new ModeListSetting("Вызывать при", new BooleanSetting[] { new BooleanSetting("Бездействии", Boolean.valueOf(false)), new BooleanSetting("Ударе", Boolean.valueOf(false)) })).setDescription("Отображает текстуры при действиях");
    ModeSetting texture = (new ModeSetting("Текстура", "Звезда", new String[] {
            "Корона", "Доллар", "Светлячок", "Сердце", "Молния", "Линия", "Точка", "Ромб", "Снежинка", "Искра",
            "Звезда" })).setVisible(() -> Boolean.valueOf((((Boolean)this.call.getValueByName("Бездействии").get()).booleanValue() || ((Boolean)this.call.getValueByName("Ударе").get()).booleanValue())))
            .setDescription("Отображает выбранные вами текстуры");
    SliderSetting countDamage = (new SliderSetting("Количество при уроне", 20.0F, 5.0F, 50.0F, 1.0F)).setVisible(() -> (Boolean)this.call.getValueByName("Ударе").get()).setDescription("Количество текстур при ударе");
    SliderSetting sizeDamage = (new SliderSetting("Размер при уроне", 0.3F, 0.1F, 0.6F, 0.1F)).setVisible(() -> (Boolean)this.call.getValueByName("Ударе").get()).setDescription("Размер текстур при уроне");
    SliderSetting sila = (new SliderSetting("Сила разброса", 0.2F, 0.1F, 0.5F, 0.1F)).setVisible(() -> (Boolean)this.call.getValueByName("Ударе").get()).setDescription("Сила разброса в стороны текстур");
    SliderSetting time = (new SliderSetting("Время жизни", 4000.0F, 500.0F, 8000.0F, 100.0F)).setVisible(() -> (Boolean)this.call.getValueByName("Ударе").get()).setDescription("Время жизни текстуры");
    SliderSetting speedMultiplier = (new SliderSetting("Скорость", 1.2F, 0.1F, 3.0F, 0.1F)).setVisible(() -> (Boolean)this.call.getValueByName("Ударе").get()).setDescription("Скорость падения текстуры");
    BooleanSetting randomRotation = (new BooleanSetting("Рандомный поворот", Boolean.valueOf(true))).setVisible(() -> (Boolean)this.call.getValueByName("Ударе").get()).setDescription("Поворачивает выбранную текстуру");
    SliderSetting countWorld = (new SliderSetting("Количество в мире", 12.0F, 2.0F, 15.0F, 1.0F)).setVisible(() -> (Boolean)this.call.getValueByName("Бездействии").get()).setDescription("Количество текстур в мире");
    SliderSetting sizeWorld = (new SliderSetting("Размер в мире", 1.1F, 0.1F, 1.2F, 0.1F)).setVisible(() -> (Boolean)this.call.getValueByName("Бездействии").get()).setDescription("Размер летающих текстур");
    ArrayList<World> worldParticles = new ArrayList<>();
    ArrayList<Damage> damageParticles = new ArrayList<>();

    public ParticlesModule() {
        addSettings(new ISetting[] { (ISetting)this.call, (ISetting)this.texture, (ISetting)this.countDamage, (ISetting)this.countWorld, (ISetting)this.sizeDamage, (ISetting)this.sizeWorld, (ISetting)this.sila, (ISetting)this.time, (ISetting)this.speedMultiplier, (ISetting)this.randomRotation });
    }

    private static final Map<String, Identifier> TEXTURES = new HashMap<>();
    static {
        TEXTURES.put("Корона", Identifier.of("sacura/images/particles/crown.png"));
        TEXTURES.put("Доллар", Identifier.of("sacura/images/particles/dollar.png"));
        TEXTURES.put("Светлячок", Identifier.of("sacura/images/particles/firefly.png"));
        TEXTURES.put("Сердце", Identifier.of("sacura/images/particles/heart.png"));
        TEXTURES.put("Молния", Identifier.of("sacura/images/particles/lightning.png"));
        TEXTURES.put("Линия", Identifier.of("sacura/images/particles/line.png"));
        TEXTURES.put("Точка", Identifier.of("sacura/images/particles/point.png"));
        TEXTURES.put("Ромб", Identifier.of("sacura/images/particles/rhombus.png"));
        TEXTURES.put("Снежинка", Identifier.of("sacura/images/particles/snowflake.png"));
        TEXTURES.put("Искра", Identifier.of("sacura/images/particles/spark.png"));
        TEXTURES.put("Звезда", Identifier.of("sacura/images/particles/star.png"));
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        if (mc.player == null || mc.world == null)
            return;
        if (((Boolean)this.call.getValueByName("Бездействии").get()).booleanValue()) {
            this.worldParticles.removeIf(World::tick);
            float con = ((Float)this.countWorld.get()).floatValue() * 100.0F;
            for (int j = this.worldParticles.size(); j < con; j++) {
                boolean drop = false;
                this.worldParticles.add(new World((float)(mc.player.getX() + MathUtil.random(-48.0F, 48.0F)), (float)(mc.player.getY() + MathUtil.random(2.0F, 48.0F)), (float)(mc.player.getZ() + MathUtil.random(-48.0F, 48.0F)), drop ? 0.0F : MathUtil.random(-0.4F, 0.4F), drop ? MathUtil.random(-0.2F, -0.05F) : MathUtil.random(-0.1F, 0.1F), drop ? 0.0F : MathUtil.random(-0.4F, 0.4F)));
            }
        }
        if (((Boolean)this.call.getValueByName("Ударе").get()).booleanValue())
            this.damageParticles.removeIf(Damage::tick);
    }

    @Subscribe
    public void onAttack(EventAttack event) {
        if (!((Boolean)this.call.getValueByName("Ударе").get()).booleanValue())
            return;
        Entity target = event.getTarget();
        if (target == null)
            return;
        double x = target.getX();
        double y = target.getY() + 0.5D;
        double z = target.getZ();
        int con = ((Float)this.countDamage.get()).intValue();
        for (int i = 0; i < con; i++) {
            float motionX = MathUtil.random(-((Float)this.sila.get()).floatValue(), ((Float)this.sila.get()).floatValue()) * ((Float)this.speedMultiplier.get()).floatValue();
            float motionZ = MathUtil.random(-((Float)this.sila.get()).floatValue(), ((Float)this.sila.get()).floatValue()) * ((Float)this.speedMultiplier.get()).floatValue();
            float motionY = MathUtil.random(-0.05F, -0.1F) * ((Float)this.speedMultiplier.get()).floatValue();
            float rotation = ((Boolean)this.randomRotation.get()).booleanValue() ? MathUtil.random(0.0F, 360.0F) : 0.0F;
            int color = ColorProvider.getColorStyle((int)(Math.random() * 360.0D));
            this.damageParticles.add(new Damage((float)x, (float)y, (float)z, motionX, motionY, motionZ, ((Float)this.time.get()).longValue(), color, rotation));
        }
    }

    @Subscribe
    public void onRender3D(EventRender3D e) {
        MatrixStack matrixStack = e.getMatrixStack();
        RenderHelper.enable(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        // Исправлено: POSITION_TEXTURE_COLOR -> POSITION_TEX_COLOR
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        // Исправлено: field_27382 -> VertexFormat.DrawMode.QUADS
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        if (((Boolean)this.call.getValueByName("Бездействии").get()).booleanValue())
            this.worldParticles.forEach(p -> p.render(bufferBuilder));
        if (((Boolean)this.call.getValueByName("Ударе").get()).booleanValue()) {
            Identifier tex = TEXTURES.getOrDefault(this.texture.get(), TEXTURES.get("Звезда"));
            RenderSystem.setShaderTexture(0, tex);
            this.damageParticles.forEach(p -> p.render(bufferBuilder));
        }
        RenderHelper.end(bufferBuilder);
        RenderSystem.depthMask(true);
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
    }

    public class Damage {
        private float prevposX;
        private float prevposY;
        private float prevposZ;
        private float posX;
        private float posY;
        private float posZ;
        private float motionX;
        private float motionY;
        private float motionZ;
        private final long createdTime;
        private final long maxAge;
        private int currentColor;
        private final float rotation;

        public Damage(float posX, float posY, float posZ, float motionX, float motionY, float motionZ, long maxAge, int color, float rotation) {
            this.posX = this.prevposX = posX;
            this.posY = this.prevposY = posY;
            this.posZ = this.prevposZ = posZ;
            this.motionX = motionX;
            this.motionY = motionY;
            this.motionZ = motionZ;
            this.createdTime = System.currentTimeMillis();
            this.maxAge = maxAge;
            this.currentColor = color;
            this.rotation = rotation;
        }

        public boolean tick() {
            long now = System.currentTimeMillis();
            if (now - this.createdTime > this.maxAge)
                return true;
            this.prevposX = this.posX;
            this.prevposY = this.posY;
            this.prevposZ = this.posZ;
            for (int i = 0; i < 4; i++)
                tryMove(this.motionX / 4.0F, this.motionY / 4.0F, this.motionZ / 4.0F);
            this.motionX *= 0.97F;
            this.motionY *= 0.97F;
            this.motionZ *= 0.97F;
            return false;
        }

        private boolean isSolid(float x, float y, float z) {
            BlockPos pos = new BlockPos((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
            // Исправлено: getOutlineShape возвращает VoxelShape, проверяем на пустоту
            return (MinecraftWrapper.mc.world != null && !MinecraftWrapper.mc.world.getBlockState(pos).getOutlineShape((BlockView)MinecraftWrapper.mc.world, pos).isEmpty());
        }

        private void tryMove(float dx, float dy, float dz) {
            float newX = this.posX + dx;
            float newY = this.posY + dy;
            float newZ = this.posZ + dz;
            if (!isSolid(newX, this.posY, this.posZ)) {
                this.posX = newX;
            } else {
                this.motionX *= -0.4F;
            }
            if (!isSolid(this.posX, newY, this.posZ)) {
                this.posY = newY;
            } else {
                this.motionY *= -0.4F;
            }
            if (!isSolid(this.posX, this.posY, newZ)) {
                this.posZ = newZ;
            } else {
                this.motionZ *= -0.4F;
            }
        }

        public void render(BufferBuilder bufferBuilder) {
            Camera camera = MinecraftWrapper.mc.gameRenderer.getCamera();
            Vec3d pos = MathUtil.interpolatePos(this.prevposX, this.prevposY, this.prevposZ, this.posX, this.posY, this.posZ);
            MatrixStack matrices = new MatrixStack();
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
            matrices.translate(pos.x, pos.y, pos.z);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotation(this.rotation));
            float alpha = Math.max(0.0F, 1.0F - (float)(System.currentTimeMillis() - this.createdTime) / (float)this.maxAge);
            int colorWithAlpha = ColorProvider.injectAlpha(this.currentColor, (int)(255.0F * alpha));
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            float s = ((Float)ParticlesModule.this.sizeDamage.get()).floatValue();
            bufferBuilder.vertex(matrix, 0.0F, -s, 0.0F).texture(0.0F, 1.0F).color(colorWithAlpha);
            bufferBuilder.vertex(matrix, -s, -s, 0.0F).texture(1.0F, 1.0F).color(colorWithAlpha);
            bufferBuilder.vertex(matrix, -s, 0.0F, 0.0F).texture(1.0F, 0.0F).color(colorWithAlpha);
            bufferBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).texture(0.0F, 0.0F).color(colorWithAlpha);
        }
    }

    public class World {
        protected float prevposX;
        protected float prevposY;
        protected float prevposZ;
        protected float posX;
        protected float posY;
        protected float posZ;
        protected float motionX;
        protected float motionY;
        protected float motionZ;
        protected int age;
        protected int maxAge;

        public World(float posX, float posY, float posZ, float motionX, float motionY, float motionZ) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.prevposX = posX;
            this.prevposY = posY;
            this.prevposZ = posZ;
            this.motionX = motionX;
            this.motionY = motionY;
            this.motionZ = motionZ;
            this.age = (int)MathUtil.random(100.0F, 300.0F);
            this.maxAge = this.age;
        }

        public boolean tick() {
            if (MinecraftWrapper.mc.player.squaredDistanceTo(this.posX, this.posY, this.posZ) > 4096.0D) {
                this.age -= 8;
            } else {
                this.age--;
            }
            if (this.age < 0)
                return true;
            this.prevposX = this.posX;
            this.prevposY = this.posY;
            this.prevposZ = this.posZ;
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            this.motionX *= 0.9F;
            this.motionY *= 0.9F;
            this.motionZ *= 0.9F;
            this.motionY -= 0.001F;
            return false;
        }

        public void render(BufferBuilder bufferBuilder) {
            Identifier tex = ParticlesModule.TEXTURES.getOrDefault(ParticlesModule.this.texture.get(), ParticlesModule.TEXTURES.get("Звезда"));
            RenderSystem.setShaderTexture(0, tex);
            Camera camera = MinecraftWrapper.mc.gameRenderer.getCamera();
            int color1 = ColorProvider.getColorStyle((this.age * 2));
            Vec3d pos = MathUtil.interpolatePos(this.prevposX, this.prevposY, this.prevposZ, this.posX, this.posY, this.posZ);
            MatrixStack matrices = new MatrixStack();
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
            matrices.translate(pos.x, pos.y, pos.z);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
            Matrix4f matrix1 = matrices.peek().getPositionMatrix();
            float size = ((Float)ParticlesModule.this.sizeWorld.get()).floatValue();
            bufferBuilder.vertex(matrix1, 0.0F, -size, 0.0F).texture(0.0F, 1.0F).color(ColorProvider.injectAlpha(color1, (int)(255.0F * this.age / this.maxAge)));
            bufferBuilder.vertex(matrix1, -size, -size, 0.0F).texture(1.0F, 1.0F).color(ColorProvider.injectAlpha(color1, (int)(255.0F * this.age / this.maxAge)));
            bufferBuilder.vertex(matrix1, -size, 0.0F, 0.0F).texture(1.0F, 0.0F).color(ColorProvider.injectAlpha(color1, (int)(255.0F * this.age / this.maxAge)));
            bufferBuilder.vertex(matrix1, 0.0F, 0.0F, 0.0F).texture(0.0F, 0.0F).color(ColorProvider.injectAlpha(color1, (int)(255.0F * this.age / this.maxAge)));
        }
    }
}