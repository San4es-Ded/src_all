package wtf.wyvern.client.gui.screens.menu.wonderful.figura;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.UserData;
import org.figuramc.figura.avatar.local.LocalAvatarLoader;
import org.figuramc.figura.gui.screens.WardrobeScreen;
import org.figuramc.figura.model.rendering.EntityRenderMode;
import org.figuramc.figura.utils.ui.UIHelper;
import org.joml.Vector3f;
import wtf.wyvern.client.gui.screens.menu.wonderful.CsGuiRenderer;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.integration.figura.FiguraAvatarLibrary;
import wtf.wyvern.integration.figura.FiguraAvatarLibrary.AvatarEntry;
import wtf.wyvern.integration.figura.FiguraPreviewContext;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;
import wtf.wyvern.utility.math.MathUtil;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Rich Figura browser rendered directly inside Wyvern's CS GUI. */
public final class FiguraModelsPage {
    private static final MinecraftClient MC = MinecraftClient.getInstance();
    private static final float CARD_H = 49.0F;
    private static final float CARD_GAP = 4.0F;
    private static final int GRID_COLUMNS = 3;
    private static final float LIST_HEADER_H = 20.0F;
    private static final float FOOTER_H = 0.0F;
    private static final float GUI_SURFACE_ROUNDING = 4.0F;
    private static final float CARD_MODEL_LIFT = 7.0F;
    private static final float PREVIEW_MODEL_BASELINE = 0.67F;

    private final Map<String, Identifier> previewTextures = new HashMap<>();
    private final Map<String, UserData> previewModels = new HashMap<>();
    private final Set<String> failedPreviews = new HashSet<>();
    private final Set<String> favorites = new HashSet<>();
    private volatile List<AvatarEntry> avatars = List.of();
    private List<AvatarEntry> visible = List.of();
    private AvatarEntry selected;
    private String appliedKey = "";
    private String status = "Подготовка библиотеки Figura...";
    private boolean loadingStarted;
    private boolean refreshing;
    private long lastAutoRefresh;
    private float scroll;

    private float pageX, pageY, pageW, pageH;
    private float listPanelX, listPanelY, listPanelW, listPanelH;
    private float listX, listY, listW, listH;
    private float previewX, previewY, previewW, previewH;
    private float applyX, applyY, applyW, applyH;
    private float removeX, removeY, removeW, removeH;
    private float wardrobeX, wardrobeY, wardrobeW, wardrobeH;
    private float refreshX, refreshY, refreshW, refreshH;

    public void render(DrawContext context, CustomDrawContext draw, float x, float y, float width, float height,
                       int alpha, int mouseX, int mouseY) {
        ensureLoaded();
        updateVisible();
        releaseUnusedPreviewModels();
        layout(x, y, width, height);
        ColorRGBA accent = CsGuiRenderer.getCurrentAccent().withAlpha(alpha);

        renderPageBackground(draw, accent, alpha);
        renderList(context, draw, accent, alpha, mouseX, mouseY);
        renderPreview(draw, accent, alpha, mouseX, mouseY);
    }

    private void ensureLoaded() {
        if (loadingStarted) return;
        loadingStarted = true;
        FiguraAvatarLibrary.ensureInstalled().whenComplete((entries, throwable) -> MC.execute(() -> {
            if (throwable != null) {
                status = "Ошибка установки: " + rootMessage(throwable);
                return;
            }
            avatars = entries;
            status = "Готово · доступно моделей: " + entries.size();
        }));
    }

    private void layout(float x, float y, float width, float height) {
        pageX = x;
        pageY = y;
        pageW = width;
        pageH = height;
        float bodyY = y;
        float bodyH = height - (bodyY - y) - FOOTER_H - 3.0F;
        listPanelX = x;
        listPanelY = bodyY;
        listPanelW = width * 0.68F;
        listPanelH = bodyH;
        listX = listPanelX + 4.0F;
        listY = listPanelY + LIST_HEADER_H;
        listW = listPanelW - 8.0F;
        listH = listPanelH - LIST_HEADER_H - 4.0F;
        refreshW = 38.0F;
        refreshH = 12.0F;
        refreshX = listPanelX + listPanelW - refreshW - 6.0F;
        refreshY = listPanelY + 4.0F;
        previewX = listPanelX + listPanelW + 6.0F;
        previewY = bodyY;
        previewW = width - listPanelW - 6.0F;
        previewH = bodyH;
        applyX = previewX + 5.0F;
        applyY = previewY + previewH - 43.0F;
        applyW = previewW - 10.0F;
        applyH = 16.0F;
        removeX = applyX;
        removeY = applyY + 20.0F;
        removeW = (applyW - 4.0F) * 0.42F;
        removeH = 15.0F;
        wardrobeX = removeX + removeW + 4.0F;
        wardrobeY = removeY;
        wardrobeW = applyW - removeW - 4.0F;
        wardrobeH = removeH;
    }

    private void renderPageBackground(CustomDrawContext draw, ColorRGBA accent, int alpha) {
        drawGuiSurface(draw, pageX - 2.0F, pageY - 2.0F, pageW + 4.0F, pageH + 2.0F,
                GUI_SURFACE_ROUNDING, alpha, false);
    }

    private void renderList(DrawContext context, CustomDrawContext draw, ColorRGBA accent, int alpha,
                            int mouseX, int mouseY) {
        drawGuiSurface(draw, listPanelX, listPanelY, listPanelW, listPanelH,
                GUI_SURFACE_ROUNDING, alpha, false);
        draw.drawText(Fonts.MEDIUM.getFont(5.7F), "Мои аватары", listPanelX + 6.0F, listPanelY + 7.0F,
                ColorRGBA.WHITE.withAlpha(alpha));
        button(draw, refreshX, refreshY, refreshW, refreshH,
                refreshing ? "..." : "Обновить", accent, false, alpha,
                MathUtil.isHovered(mouseX, mouseY, refreshX, refreshY, refreshW, refreshH));
        String count = visible.size() + (visible.size() == 1 ? " модель" : " моделей");
        float countW = Fonts.MEDIUM.getWidth(count, 4.4F) + 9.0F;
        float countX = refreshX - countW - 4.0F;
        float countY = listPanelY + 4.5F;
        BorderRadius countRadius = BorderRadius.all(3.0F);
        DrawUtil.drawBlur(draw.getMatrices(), countX, countY, countW, 11.0F, 12.0F, countRadius,
                ColorRGBA.WHITE.withAlpha(alpha));
        DrawUtil.drawRoundedRect(draw.getMatrices(), countX, countY, countW, 11.0F, countRadius,
                new ColorRGBA(255, 255, 255, scaledAlpha(16, alpha)));
        draw.drawText(Fonts.MEDIUM.getFont(4.4F), count,
                countX + 4.5F, listPanelY + 8.0F,
                new ColorRGBA(205, 207, 218, alpha));

        int rows = (visible.size() + GRID_COLUMNS - 1) / GRID_COLUMNS;
        float contentH = rows == 0 ? 0.0F : rows * CARD_H + (rows - 1) * CARD_GAP;
        float maxScroll = Math.max(0.0F, contentH - listH);
        scroll = Math.max(0.0F, Math.min(maxScroll, scroll));
        context.enableScissor((int) listX, (int) listY, (int) (listX + listW), (int) (listY + listH));
        float cardW = gridCardWidth();
        for (int i = 0; i < visible.size(); i++) {
            int column = i % GRID_COLUMNS;
            int row = i / GRID_COLUMNS;
            float cx = listX + column * (cardW + CARD_GAP);
            float cy = listY + row * (CARD_H + CARD_GAP) - scroll;
            if (cy + CARD_H >= listY && cy <= listY + listH) {
                renderCard(draw, visible.get(i), cx, cy, cardW, accent, alpha, mouseX, mouseY);
            }
        }
        context.disableScissor();
        if (maxScroll > 0.0F) {
            float trackX = listPanelX + listPanelW - 2.8F;
            float thumbH = Math.max(18.0F, listH * (listH / contentH));
            float thumbY = listY + (listH - thumbH) * (scroll / maxScroll);
            DrawUtil.drawRoundedRect(draw.getMatrices(), trackX, listY, 1.2F, listH,
                    BorderRadius.all(0.6F), new ColorRGBA(255, 255, 255, scaledAlpha(10, alpha)));
            DrawUtil.drawRoundedRect(draw.getMatrices(), trackX, thumbY, 1.2F, thumbH,
                    BorderRadius.all(0.6F), accent.withAlpha(scaledAlpha(170, alpha)));
        }
        if (avatars.isEmpty()) {
            renderEmptyState(draw, accent, alpha, "Подготавливаем модели", "Это займёт всего несколько секунд");
        } else if (visible.isEmpty()) {
            renderEmptyState(draw, accent, alpha, "Ничего не найдено", "Попробуй другой запрос или раздел");
        }
    }

    private void renderEmptyState(CustomDrawContext draw, ColorRGBA accent, int alpha, String title, String hint) {
        float cx = listX + listW * 0.5F;
        float cy = listY + listH * 0.5F - 11.0F;
        DrawUtil.drawRoundedRect(draw.getMatrices(), cx - 10.0F, cy - 8.0F, 20.0F, 20.0F,
                BorderRadius.all(6.0F), accent.withAlpha(scaledAlpha(26, alpha)));
        draw.drawText(Fonts.LUPA.getFont(6.0F), "\uF002", cx - 3.0F, cy - 1.0F,
                accent.withAlpha(alpha));
        float titleW = Fonts.MEDIUM.getWidth(title, 5.6F);
        draw.drawText(Fonts.MEDIUM.getFont(5.6F), title, cx - titleW * 0.5F, cy + 17.0F,
                ColorRGBA.WHITE.withAlpha(alpha));
        float hintW = Fonts.MEDIUM.getWidth(hint, 4.4F);
        draw.drawText(Fonts.MEDIUM.getFont(4.4F), hint, cx - hintW * 0.5F, cy + 27.0F,
                new ColorRGBA(127, 130, 147, alpha));
    }

    private void renderCard(CustomDrawContext draw, AvatarEntry avatar, float x, float y, float width,
                            ColorRGBA accent, int alpha, int mouseX, int mouseY) {
        boolean active = selected != null && selected.key().equals(avatar.key());
        boolean hover = MathUtil.isHovered(mouseX, mouseY, x, y, width, CARD_H);
        drawGuiSurface(draw, x, y, width, CARD_H, GUI_SURFACE_ROUNDING, alpha, hover);
        if (active) {
            DrawUtil.drawRoundedRect(draw.getMatrices(), x, y, width, CARD_H,
                    BorderRadius.all(GUI_SURFACE_ROUNDING), accent.withAlpha(scaledAlpha(54, alpha)));
        }
        Identifier thumbnail = preview(avatar);
        if (thumbnail != null) {
            float imageSize = 32.0F;
            draw.drawTexture(thumbnail, x + (width - imageSize) * 0.5F, y + 2.0F,
                    imageSize, imageSize, ColorRGBA.WHITE.withAlpha(alpha));
        } else {
            renderAvatarModel(draw, avatar, x + 3.0F, y - CARD_MODEL_LIFT, width - 6.0F, 33.0F, 18.0F);
        }
        boolean favorite = favorites.contains(avatar.key());
        draw.drawText(Fonts.MEDIUM.getFont(6.4F), favorite ? "★" : "☆", x + width - 10.0F, y + 4.0F,
                favorite ? accent.brighter(0.24F).withAlpha(alpha) : new ColorRGBA(112, 115, 133, alpha));
    }

    private float gridCardWidth() {
        return (listW - CARD_GAP * (GRID_COLUMNS - 1)) / GRID_COLUMNS;
    }

    private boolean renderAvatarModel(CustomDrawContext draw, AvatarEntry entry, float x, float y,
                                      float width, float height, float scale) {
        if (MC.player == null) return false;
        UserData data = previewModels.computeIfAbsent(entry.key(), key -> {
            UserData created = new UserData(MC.player.getUuid());
            LocalAvatarLoader.loadAvatar(entry.path(), created);
            return created;
        });
        Avatar avatar = data.getMainAvatar();
        if (avatar == null || !avatar.loaded) return false;
        FiguraPreviewContext.renderWith(avatar, () -> UIHelper.drawEntity(
                x + width * 0.5F, y + height, scale, -15.0F, 30.0F,
                MC.player, draw, new Vector3f(), EntityRenderMode.FIGURA_GUI));
        return true;
    }

    private void renderPreview(CustomDrawContext draw, ColorRGBA accent, int alpha, int mouseX, int mouseY) {
        drawGuiSurface(draw, previewX, previewY, previewW, previewH,
                GUI_SURFACE_ROUNDING, alpha, false);
        draw.drawText(Fonts.MEDIUM.getFont(5.6F), "Предпросмотр", previewX + 6.0F, previewY + 6.5F,
                new ColorRGBA(183, 186, 201, alpha));
        if (selected == null) {
            float labelW = Fonts.MEDIUM.getWidth("Выбери модель", 5.3F);
            draw.drawText(Fonts.MEDIUM.getFont(5.3F), "Выбери модель",
                    previewX + (previewW - labelW) * 0.5F, previewY + previewH * 0.5F,
                    new ColorRGBA(137, 140, 156, alpha));
            return;
        }
        float imageAreaTop = previewY + 17.0F;
        float imageAreaBottom = applyY - 5.0F;
        float modelX = previewX + 6.0F;
        float modelW = previewW - 12.0F;
        float modelH = imageAreaBottom - imageAreaTop;
        DrawUtil.drawRoundedRect(draw.getMatrices(), modelX, imageAreaTop, modelW, modelH,
                BorderRadius.all(GUI_SURFACE_ROUNDING),
                new ColorRGBA(255, 255, 255, scaledAlpha(10, alpha)));
        renderAvatarModel(draw, selected, modelX, imageAreaTop, modelW,
                modelH * PREVIEW_MODEL_BASELINE, 38.0F);

        button(draw, applyX, applyY, applyW, applyH, "Применить", accent, true, alpha,
                MathUtil.isHovered(mouseX, mouseY, applyX, applyY, applyW, applyH));
        button(draw, removeX, removeY, removeW, removeH, "Снять", accent, false, alpha,
                MathUtil.isHovered(mouseX, mouseY, removeX, removeY, removeW, removeH));
        button(draw, wardrobeX, wardrobeY, wardrobeW, wardrobeH, "Гардероб", accent, false, alpha,
                MathUtil.isHovered(mouseX, mouseY, wardrobeX, wardrobeY, wardrobeW, wardrobeH));
    }

    private void button(CustomDrawContext draw, float x, float y, float w, float h, String label,
                        ColorRGBA accent, boolean primary, int alpha, boolean hover) {
        BorderRadius radius = BorderRadius.all(3.0F);
        DrawUtil.drawBlur(draw.getMatrices(), x, y, w, h, 12.0F, radius,
                ColorRGBA.WHITE.withAlpha(alpha));
        ColorRGBA background;
        if (primary) {
            background = accent.withAlpha(scaledAlpha(hover ? 220 : 185, alpha));
        } else {
            background = new ColorRGBA(255, 255, 255, scaledAlpha(hover ? 28 : 16, alpha));
        }
        DrawUtil.drawRoundedRect(draw.getMatrices(), x, y, w, h, radius, background);
        float size = 4.9F;
        float tw = Fonts.MEDIUM.getWidth(label, size);
        draw.drawText(Fonts.MEDIUM.getFont(size), label, x + (w - tw) * 0.5F, y + (h - size) * 0.5F + 0.5F,
                (primary ? ColorRGBA.WHITE : new ColorRGBA(205, 207, 218)).withAlpha(alpha));
    }

    private void drawGuiSurface(CustomDrawContext draw, float x, float y, float width, float height,
                                float rounding, int alpha, boolean hover) {
        BorderRadius radius = BorderRadius.all(rounding);
        DrawUtil.drawBlur(draw.getMatrices(), x, y, width, height, 25.0F, radius,
                ColorRGBA.WHITE.withAlpha(alpha));
        ColorRGBA overlay = hover
                ? new ColorRGBA(20, 21, 28, scaledAlpha(245, alpha))
                : new ColorRGBA(10, 11, 18, scaledAlpha(225, alpha));
        DrawUtil.drawRoundedRect(draw.getMatrices(), x, y, width, height, radius, overlay);
    }

    private void renderStatus(CustomDrawContext draw, ColorRGBA accent, int alpha) {
        float y = pageY + pageH - 7.5F;
        DrawUtil.drawRoundedRect(draw.getMatrices(), pageX + 1.0F, y + 1.6F, 2.5F, 2.5F,
                BorderRadius.all(1.3F), accent.withAlpha(scaledAlpha(210, alpha)));
        float favoritesW = favorites.isEmpty() ? 0.0F
                : Fonts.MEDIUM.getWidth("★ " + favorites.size(), 4.5F) + 8.0F;
        draw.drawText(Fonts.MEDIUM.getFont(4.7F), trim(status, pageW - favoritesW - 13.0F, 4.7F),
                pageX + 7.0F, y, new ColorRGBA(145, 148, 165, alpha));
        if (!favorites.isEmpty()) {
            String label = "★ " + favorites.size();
            DrawUtil.drawRoundedRect(draw.getMatrices(), pageX + pageW - favoritesW, y - 1.0F, favoritesW, 9.5F,
                    BorderRadius.all(4.7F), accent.withAlpha(scaledAlpha(22, alpha)));
            draw.drawText(Fonts.MEDIUM.getFont(4.5F), label, pageX + pageW - favoritesW + 4.0F, y + 1.7F,
                    accent.brighter(0.24F).withAlpha(alpha));
        }
    }

    private static int scaledAlpha(int value, int alpha) {
        return Math.max(0, Math.min(255, Math.round(value * (alpha / 255.0F))));
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return inside(mouseX, mouseY);
        if (MathUtil.isHovered(mouseX, mouseY, refreshX, refreshY, refreshW, refreshH)) {
            refreshModels();
            return true;
        }
        if (MathUtil.isHovered(mouseX, mouseY, listX, listY, listW, listH)) {
            float cardW = gridCardWidth();
            for (int i = 0; i < visible.size(); i++) {
                int column = i % GRID_COLUMNS;
                int row = i / GRID_COLUMNS;
                float cx = listX + column * (cardW + CARD_GAP);
                float cy = listY + row * (CARD_H + CARD_GAP) - scroll;
                if (MathUtil.isHovered(mouseX, mouseY, cx, cy, cardW, CARD_H)) {
                    AvatarEntry avatar = visible.get(i);
                    if (mouseX >= cx + cardW - 13.0F && mouseY <= cy + 15.0F) {
                        if (!favorites.add(avatar.key())) favorites.remove(avatar.key());
                    } else {
                        selected = avatar;
                    }
                    return true;
                }
            }
        }
        if (selected != null && MathUtil.isHovered(mouseX, mouseY, applyX, applyY, applyW, applyH)) {
            applySelected();
            return true;
        }
        if (MathUtil.isHovered(mouseX, mouseY, removeX, removeY, removeW, removeH)) {
            clearAvatar();
            return true;
        }
        if (MathUtil.isHovered(mouseX, mouseY, wardrobeX, wardrobeY, wardrobeW, wardrobeH)) {
            MC.setScreen(new WardrobeScreen(MC.currentScreen));
            return true;
        }
        return inside(mouseX, mouseY);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!MathUtil.isHovered(mouseX, mouseY, listX, listY, listW, listH)) return false;
        scroll = Math.max(0.0F, scroll - (float) amount * 18.0F);
        return true;
    }

    public boolean charTyped(char chr) {
        return false;
    }

    public boolean keyPressed(int keyCode) {
        return false;
    }

    private void refreshModels() {
        if (refreshing) return;
        refreshing = true;
        lastAutoRefresh = System.currentTimeMillis();
        status = "Обновляем список моделей...";
        FiguraAvatarLibrary.refreshIndex().whenComplete((entries, throwable) -> MC.execute(() -> {
            if (throwable != null) {
                refreshing = false;
                status = "Ошибка обновления: " + rootMessage(throwable);
                return;
            }

            avatars = entries;
            clearPreviewModels();
            previewTextures.clear();
            failedPreviews.clear();
            if (selected == null || entries.stream().noneMatch(entry -> entry.key().equals(selected.key()))) {
                selected = entries.isEmpty() ? null : entries.getFirst();
            }
            refreshing = false;
            status = "Список моделей обновлён: " + entries.size();
        }));
    }

    private void applySelected() {
        AvatarEntry avatar = selected;
        if (avatar == null) return;
        status = "Загружаем " + avatar.name() + "...";
        try {
            AvatarManager.loadLocalAvatar(avatar.path());
            appliedKey = avatar.key();
            status = "Применено: " + avatar.name();
        } catch (Throwable error) {
            status = "Ошибка Figura: " + rootMessage(error);
        }
    }

    private void releaseUnusedPreviewModels() {
        String selectedKey = selected == null ? "" : selected.key();
        previewModels.entrySet().removeIf(entry -> {
            if (entry.getKey().equals(selectedKey)) return false;
            AvatarEntry avatar = avatars.stream()
                    .filter(candidate -> candidate.key().equals(entry.getKey()))
                    .findFirst().orElse(null);
            if (avatar != null && avatar.thumbnail() == null) return false;
            entry.getValue().clear();
            return true;
        });
    }

    private void clearPreviewModels() {
        for (UserData data : previewModels.values()) data.clear();
        previewModels.clear();
    }

    private void clearAvatar() {
        if (MC.player != null) AvatarManager.clearAvatars(MC.player.getUuid());
        AvatarManager.localUploaded = false;
        appliedKey = "";
        status = "Аватар снят";
    }

    private Identifier preview(AvatarEntry avatar) {
        if (avatar.thumbnail() == null || failedPreviews.contains(avatar.key())) return null;
        Identifier cached = previewTextures.get(avatar.key());
        if (cached != null) return cached;
        try (InputStream input = Files.newInputStream(avatar.thumbnail())) {
            NativeImage image = NativeImage.read(input);
            Identifier id = Identifier.of("wyvern", "figura_preview/" + Integer.toUnsignedString(avatar.key().hashCode(), 36));
            MC.getTextureManager().registerTexture(id, new NativeImageBackedTexture(image));
            previewTextures.put(avatar.key(), id);
            return id;
        } catch (Exception exception) {
            failedPreviews.add(avatar.key());
            return null;
        }
    }

    private void updateVisible() {
        List<AvatarEntry> result = new ArrayList<>();
        Map<String, Integer> sourceOrder = new HashMap<>();
        for (int i = 0; i < avatars.size(); i++) {
            sourceOrder.put(avatars.get(i).key(), i);
        }
        for (AvatarEntry avatar : avatars) {
            result.add(avatar);
        }
        result.sort((left, right) -> {
            int favorite = Boolean.compare(favorites.contains(right.key()), favorites.contains(left.key()));
            if (favorite != 0) return favorite;
            return Integer.compare(
                    sourceOrder.getOrDefault(left.key(), Integer.MAX_VALUE),
                    sourceOrder.getOrDefault(right.key(), Integer.MAX_VALUE));
        });
        visible = result;
    }

    private boolean inside(double mouseX, double mouseY) {
        return MathUtil.isHovered(mouseX, mouseY, pageX, pageY, pageW, pageH);
    }

    private static String trim(String text, float maxWidth, float size) {
        if (Fonts.MEDIUM.getWidth(text, size) <= maxWidth) return text;
        String suffix = "...";
        String value = text;
        while (!value.isEmpty() && Fonts.MEDIUM.getWidth(value + suffix, size) > maxWidth) {
            value = value.substring(0, value.length() - 1);
        }
        return value + suffix;
    }

    private static String rootMessage(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null) root = root.getCause();
        return root.getMessage() == null ? root.getClass().getSimpleName() : root.getMessage();
    }
}
