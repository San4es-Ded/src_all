package ru.prism.module.impl.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import ru.prism.cosmetic.loader.CosmeticLoader;
import ru.prism.cosmetic.model.CosmeticModel;
import ru.prism.cosmetic.render.CosmeticRenderer;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.ButtonSetting;
import ru.prism.module.api.settings.impl.MultiBooleanSetting;
import ru.prism.screen.CosmeticsPreviewScreen;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Cosmetics",
        desc = "Вешает на тебя шапки, крылья, рюкзаки и петов, чтобы ты был самым модным на сервере.",
        category = Category.VISUALS
)
public class Cosmetics extends Module {


    public static final String[] ITEM_NAMES = {
        "prism_Cape 2",
        "prism_Cape",
        "prism_Dark_Cape",
        "akvi4_Cape_2",
        "bro9i_Cape_3",
        "bro9i_Cape_2",
        "fluger_Fluger2 Cape",
        "fluger_Fluger3 Cape",
        "fluger_Fluger5 Cape",
        "bro9i_Cape",
        "akvi4_Cape",
        "Diamond_sword Cape",
        "Glitch Cape",
        "Sweatgod Cape",
        "prism_Wings",
        "prism_Dark Wings",
        "prism_Ghoul Wings",
        "prism_Rocker Wings",
        "wota_Wings",
        "fluger_Wings",
        "bro9i_Trident Wings",
        "Easter Wings",
        "Angel Wings",
        "Baby Dragon Wings",
        "Dragon Wings Blue",
        "Spider Wings",
        "Vulcano Wings",
        "prism_Katana Back",
        "prism_Frog Backpack",
        "Backpack 5",
        "Backpack 6",
        "Backpack 7",
        "Backpack 8",
        "Backpack 4",
        "Backpack 9",
        "Backpack 3",
        "Backpack 2",
        "Backpack 1",
        "prism_Pet 4",
        "prism_Pet 3",
        "prism_Pet 2",
        "prism_Pet 1",
        "prism_Cake Pet",
        "prism_Bee",
        "akvi4_Pet 2",
        "fluger_Pet",
        "bro9i_Diamond Pet",
        "Radish Pet",
        "Demon Pet",
        "Angel Pet",
        "prism_Dark Hat",
        "prism_Nimb",
        "prism_Leaf Hat",
        "prism_Frog Hat",
        "bro9i_Hat Mask",
        "akvi4_Penguin Hat",
        "fluger_Hat",
        "prism_Bear Hat",
        "Armadillo Hat",
        "Camel Hat",
        "Chicken Hat",
        "Frog Hat",
        "Sheep Hat"
    };

    private final MultiBooleanSetting items;
    private final ButtonSetting visualizeButton;
    private final CosmeticRenderer renderer = new CosmeticRenderer();
    private boolean loadStarted;


    public Cosmetics() {
        BooleanSetting[] values = new BooleanSetting[ITEM_NAMES.length];
        for (int i = 0; i < ITEM_NAMES.length; i++) {
            values[i] = new BooleanSetting(ITEM_NAMES[i], false);
        }
        this.items = new MultiBooleanSetting(this, "Косметика", values);
        this.visualizeButton = new ButtonSetting(this, "Визуализация", () -> {
            MinecraftClient client = MinecraftClient.getInstance();
            client.setScreen(new CosmeticsPreviewScreen(client.currentScreen));
        });
    }

    public static Cosmetics getInstance() {
        return Instance.get(Cosmetics.class);
    }

    public MultiBooleanSetting getItems() {
        return this.items;
    }

    public Identifier getSelectedCapeTexture() {
        this.ensureLoaded();
        CosmeticLoader loader = CosmeticLoader.getInstance();
        int capeCount = Math.min(14, ITEM_NAMES.length);

        for (int i = 0; i < capeCount; i++) {
            BooleanSetting setting = this.items.get(ITEM_NAMES[i]);
            if (setting == null || !setting.getValue()) {
                continue;
            }

            Identifier cape = loader.getCapeTexture(i);
            if (cape != null) {
                return cape;
            }
        }

        return null;
    }

    public boolean isSelectedCapeSkinLayout() {
        this.ensureLoaded();
        CosmeticLoader loader = CosmeticLoader.getInstance();
        int capeCount = Math.min(14, ITEM_NAMES.length);

        for (int i = 0; i < capeCount; i++) {
            BooleanSetting setting = this.items.get(ITEM_NAMES[i]);
            if (setting == null || !setting.getValue()) {
                continue;
            }

            if (loader.getCapeTexture(i) != null) {
                return loader.isCapeSkinLayout(i);
            }
        }

        return false;
    }

    @Override
    protected void onEnable() {
        this.ensureLoaded();
    }

    @Override
    protected void onDisable() {
        CosmeticLoader.getInstance().reset();
        this.renderer.clearCaches();
        this.loadStarted = false;
    }

    // ───────────────────────────── предпоказ ─────────────────────────────


    private void ensureLoaded() {
        if (this.loadStarted) {
            return;
        }

        ResourceManager resourceManager = mc.getResourceManager();
        if (resourceManager == null) {
            return;
        }

        this.loadStarted = true;
        CosmeticLoader.getInstance().loadAll(resourceManager, ITEM_NAMES.length);
    }

    public void renderCosmetics(MatrixStack matrices, int light, PlayerEntityModel playerModel) {
        this.ensureLoaded();

        CosmeticLoader loader = CosmeticLoader.getInstance();
        VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
        boolean rendered = false;

        for (int i = 0; i < ITEM_NAMES.length; i++) {
            BooleanSetting setting = this.items.get(ITEM_NAMES[i]);
            if (setting == null || !setting.getValue()) {
                continue;
            }

            CosmeticModel cosmetic = loader.getCosmetic(i);
            if (cosmetic == null || cosmetic.getTextureId() == null) {
                continue;
            }

            RenderLayer layer = RenderLayers.entityCutoutNoCull(cosmetic.getTextureId());
            VertexConsumer consumer = immediate.getBuffer(layer);
            this.renderer.render(cosmetic, matrices, consumer, light, playerModel);
            rendered = true;
        }

        if (rendered) {
            immediate.draw();
        }
    }
}
