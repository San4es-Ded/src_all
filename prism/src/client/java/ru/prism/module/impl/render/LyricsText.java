package ru.prism.module.impl.render;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import ru.prism.manager.event_impl.EventRender3D;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.ModeSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.animation.Animation;
import ru.prism.utils.animation.Easings;
import ru.prism.utils.colors.ColorUtil;
import ru.prism.utils.lyrics.LyricLine;
import ru.prism.utils.lyrics.LyricWord;
import ru.prism.utils.lyrics.Lyrics;
import ru.prism.utils.lyrics.LyricsService;
import ru.prism.utils.other.LyricsMedia;

import java.util.List;

@ModuleInfo(
        name = "Lyrics Text",
        desc = "Строки играющей песни висят в мире перед вами.",
        category = Category.VISUALS
)
public class LyricsText extends Module {

    public final ModeSetting showMode = new ModeSetting(this, "Показывать", "Строки", "Слова");
    public final ModeSetting layout = new ModeSetting(this, "Раскладка", "Стандарт", "Вразброс", "360")
            .setVisible(() -> showMode.is("Слова"));
    public final SliderSetting size = new SliderSetting(this, "Размер строки", 0.3F, 0.05F, 2.0F, 0.01F);
    public final SliderSetting opacity = new SliderSetting(this, "Прозрачность", 1.0F, 0.1F, 1.0F, 0.05F);
    public final BooleanSetting throughWalls = new BooleanSetting(this, "Сквозь стены", false);
    public final BooleanSetting autoCaptions = new BooleanSetting(this, "Авто-титры", true);
    public final SliderSetting glow = new SliderSetting(this, "Свечение", 1.0F, 0.0F, 3.0F, 0.05F);
    public final SliderSetting radius = new SliderSetting(this, "Радиус", 3.5F, 1.0F, 16.0F, 0.1F);
    public final SliderSetting height = new SliderSetting(this, "Высота", 1.8F, -2.0F, 8.0F, 0.1F);
    public final SliderSetting limit = new SliderSetting(this, "Кусков", 3, 1, 8, 1);
    public final SliderSetting sync = new SliderSetting(this, "Смещение", 0, -1500, 1500, 10);

    private final BufferAllocator allocator = new BufferAllocator(1 << 13);
    private final Animation alpha = new Animation();
    private String lastTrack = "";

    @Override
    protected void onDisable() {
        lastTrack = "";
        LyricsService.reset();
    }

    @EventHandler
    public void onRender(EventRender3D e) {
        if (mc.player == null || mc.world == null) return;

        LyricsMedia.tick();
        LyricsMedia.Track track = LyricsMedia.getTrack();
        boolean active = track != null && !track.isEmpty() && LyricsMedia.isPlaying();

        alpha.update();
        alpha.run(active ? 1.0F : 0.0F, 0.15F, Easings.SINE_OUT);
        float alphaPC = alpha.get();
        if (!active || alphaPC <= 0.001F) return;

        String key = track.title() + "|" + track.artist();
        if (!key.equals(lastTrack)) {
            lastTrack = key;
            LyricsService.request(track.title(), track.artist(), track.durationMillis());
        }

        Lyrics lyrics = LyricsService.get();
        if (lyrics == null || lyrics.isEmpty()) return;

        long time = LyricsMedia.positionMillis() + sync.getValue().longValue();
        List<LyricLine> lines = lyrics.getLines();
        int index = lyrics.indexAt(time);
        if (index < 0) index = 0;

        float scale = size.getValue() / 12.0F;
        float yaw = (float) Math.toRadians(mc.gameRenderer.getCamera().getYaw());
        Vec3d base = mc.player.getLerpedPos(e.getTickDelta());
        double dirX = -Math.sin(yaw);
        double dirZ = Math.cos(yaw);
        Vec3d anchor = new Vec3d(
                base.x + dirX * radius.getValue(),
                base.y + height.getValue(),
                base.z + dirZ * radius.getValue()
        );

        MatrixStack matrices = e.getMatrixStack();
        Vec3d camPos = mc.gameRenderer.getCamera().getCameraPos();
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(allocator);
        TextRenderer.TextLayerType layer = throughWalls.getValue()
                ? TextRenderer.TextLayerType.SEE_THROUGH
                : TextRenderer.TextLayerType.NORMAL;

        if (showMode.is("Слова")) {
            renderWords(matrices, immediate, camPos, anchor, base, yaw, lines, index, time, scale, alphaPC, layer);
        } else {
            renderLines(matrices, immediate, camPos, anchor, lines, index, time, scale, alphaPC, layer);
        }

        immediate.draw();
    }

    private void renderLines(MatrixStack matrices, VertexConsumerProvider.Immediate immediate, Vec3d camPos,
                             Vec3d anchor, List<LyricLine> lines, int index, long time, float scale,
                             float alphaPC, TextRenderer.TextLayerType layer) {
        int rows = Math.max(1, limit.getValue().intValue());
        float rowStep = 9.0F * scale * 1.35F;

        for (int row = 0; row < rows; row++) {
            int lineIndex = index + row;
            if (lineIndex >= lines.size()) break;

            LyricLine line = lines.get(lineIndex);
            String text = line.getText();
            if (text == null || text.isBlank()) continue;

            float rowAlpha = rowAlpha(row, alphaPC);
            float heat = line.contains(time) ? line.progress(time) : (time >= line.getEndMillis() ? 1.0F : 0.0F);
            int color = ColorUtil.replAlpha(sungColor(heat), (int) (255.0F * rowAlpha));
            Vec3d pos = anchor.add(0.0, -row * rowStep, 0.0);

            drawGlow(matrices, immediate, camPos, pos, text, scale, layer, rowAlpha);
            drawBillboard(matrices, immediate, camPos, pos, text, color, scale, layer);
        }
    }

    private void renderWords(MatrixStack matrices, VertexConsumerProvider.Immediate immediate, Vec3d camPos,
                             Vec3d anchor, Vec3d base, float yaw, List<LyricLine> lines, int index, long time,
                             float scale, float alphaPC, TextRenderer.TextLayerType layer) {
        int rows = Math.max(1, limit.getValue().intValue());
        float rowStep = 9.0F * scale * 1.35F;
        boolean scattered = layout.is("Вразброс");
        boolean radial = layout.is("360");

        Vector3f right = mc.gameRenderer.getCamera().getRotation().transform(new Vector3f(1.0F, 0.0F, 0.0F));
        Vector3f up = mc.gameRenderer.getCamera().getRotation().transform(new Vector3f(0.0F, 1.0F, 0.0F));

        for (int row = 0; row < rows; row++) {
            int lineIndex = index + row;
            if (lineIndex >= lines.size()) break;

            LyricLine line = lines.get(lineIndex);
            List<LyricWord> words = line.getWords();
            float rowAlpha = rowAlpha(row, alphaPC);

            if (words.isEmpty()) {
                String text = line.getText();
                if (text == null || text.isBlank()) continue;
                Vec3d pos = anchor.add(0.0, -row * rowStep, 0.0);
                drawGlow(matrices, immediate, camPos, pos, text, scale, layer, rowAlpha);
                drawBillboard(matrices, immediate, camPos, pos, text,
                        ColorUtil.replAlpha(0xFFFFFFFF, (int) (255.0F * rowAlpha)), scale, layer);
                continue;
            }

            float total = 0.0F;
            for (LyricWord word : words) {
                total += mc.textRenderer.getWidth(word.getText()) + mc.textRenderer.getWidth(" ");
            }

            float cursor = -total / 2.0F;
            for (int wi = 0; wi < words.size(); wi++) {
                LyricWord word = words.get(wi);
                String text = word.getText();
                float width = mc.textRenderer.getWidth(text);

                float heat = word.contains(time) ? word.progress(time) : (time >= word.getEndMillis() ? 1.0F : 0.0F);
                float eased = heat * heat * (3.0F - 2.0F * heat);

                Vec3d pos;
                if (radial) {
                    double angle = Math.toRadians(wi * 137.5);
                    double dist = radius.getValue();
                    pos = new Vec3d(
                            base.x - Math.sin(angle) * dist,
                            anchor.y + wobble(wi) * 2.4,
                            base.z + Math.cos(angle) * dist
                    );
                } else if (scattered) {
                    pos = anchor.add(
                            right.x * wobble(wi) * 1.0 + up.x * wobble(wi + 31) * 2.4,
                            right.y * wobble(wi) * 1.0 + up.y * wobble(wi + 31) * 2.4,
                            right.z * wobble(wi) * 1.0 + up.z * wobble(wi + 31) * 2.4
                    );
                } else {
                    float offset = (cursor + width / 2.0F) * scale;
                    pos = anchor.add(
                            right.x * offset,
                            -row * rowStep + right.y * offset,
                            right.z * offset
                    );
                }

                int color = ColorUtil.replAlpha(sungColor(eased), (int) (255.0F * rowAlpha));
                drawGlow(matrices, immediate, camPos, pos, text, scale, layer, rowAlpha);
                drawBillboard(matrices, immediate, camPos, pos, text, color, scale, layer);

                cursor += width + mc.textRenderer.getWidth(" ");
            }
        }
    }

    private void drawGlow(MatrixStack matrices, VertexConsumerProvider.Immediate immediate, Vec3d camPos,
                          Vec3d pos, String text, float scale, TextRenderer.TextLayerType layer, float rowAlpha) {
        float amount = glow.getValue();
        if (amount <= 0.01F) return;

        int alpha = (int) (255.0F * rowAlpha * Math.min(1.0F, amount) * 0.22F);
        if (alpha <= 2) return;

        int color = ColorUtil.replAlpha(ColorUtil.fade(0), alpha);
        float spread = 1.6F * scale;

        drawBillboard(matrices, immediate, camPos, pos.add(0.0, spread, 0.0), text, color, scale, layer);
        drawBillboard(matrices, immediate, camPos, pos.add(0.0, -spread, 0.0), text, color, scale, layer);
        drawBillboard(matrices, immediate, camPos, pos.add(spread, 0.0, 0.0), text, color, scale, layer);
        drawBillboard(matrices, immediate, camPos, pos.add(-spread, 0.0, 0.0), text, color, scale, layer);
    }

    private void drawBillboard(MatrixStack matrices, VertexConsumerProvider.Immediate immediate, Vec3d camPos,
                               Vec3d pos, String text, int color, float scale, TextRenderer.TextLayerType layer) {
        matrices.push();
        matrices.translate(pos.x - camPos.x, pos.y - camPos.y, pos.z - camPos.z);
        matrices.multiply(mc.gameRenderer.getCamera().getRotation());
        matrices.scale(scale, -scale, scale);
        Matrix4f matrix = new Matrix4f(matrices.peek().getPositionMatrix());
        matrices.pop();

        float x = -mc.textRenderer.getWidth(text) / 2.0F;
        mc.textRenderer.draw(text, x, 0.0F, color, false, matrix, immediate, layer, 0,
                LightmapTextureManager.MAX_LIGHT_COORDINATE);
    }

    private static int sungColor(float heat) {
        float eased = heat * heat * (3.0F - 2.0F * heat);
        int gray = 0x9A;
        int value = (int) (gray + (255 - gray) * eased);
        return ColorUtil.getColor(value, value, value, 255);
    }

    private float rowAlpha(int row, float alphaPC) {
        float fade = row == 0 ? 1.0F : Math.max(0.25F, 1.0F - row * 0.3F);
        return alphaPC * opacity.getValue() * fade;
    }

    private static float wobble(int seed) {
        double value = Math.sin(seed * 12.9898) * 43758.5453;
        return (float) ((value - Math.floor(value)) * 2.0 - 1.0);
    }
}
