package ru.haron.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.module.ModuleManager;
import haron.modules.hud.ClientColor;
import haron.modules.utilities.StreamerMode;
import haron.modules.visuals.Animations;
import haron.player.HaronPlayerTracker;
import haron.render.icons.HaronIcons;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={PlayerListHud.class})
public class PlayerListHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    private Text header;
    @Unique
    private static final Logger PULSE_LOG = LogManager.getLogger((String)"HaronTab");
    @Unique
    private static final int ICON_SIZE = 8;
    @Unique
    private static final int ICON_PADDING = 2;
    @Unique
    private static final long NAMES_REFRESH_MS = 1000L;
    @Unique
    private static final long LABEL_CACHE_TTL_MS = 2000L;
    @Unique
    private static final long FLAGS_TTL_MS = 500L;
    @Unique
    private static final String NO_MATCH = "";
    @Unique
    private final List<String[]> tabNamesLower = new ArrayList<String[]>();
    @Unique
    private long tabNamesTime = 0L;
    @Unique
    private final Map<String, String> labelCache = new HashMap<String, String>();
    @Unique
    private long labelCacheTime = 0L;
    @Unique
    private long flagsTime = 0L;
    @Unique
    private boolean fHighlightSelf = false;
    @Unique
    private boolean fHideHeader = false;
    @Unique
    private String selfName = null;
    @Unique
    private Text cachedHeaderSource = null;
    @Unique
    private Text cachedHeaderResult = null;
    @Unique
    private boolean cachedHeaderHideFlag = false;

    @Unique
    private void refreshFlags() {
        long now = System.currentTimeMillis();
        if (now - this.flagsTime < 500L) {
            return;
        }
        this.flagsTime = now;
        try {
            Animations animations = ModuleManager.ANIMATIONS;
            this.fHighlightSelf = animations.k() && (Boolean)animations.tabHighlight.k() != false && this.client.player != null;
            this.selfName = this.client.player != null ? this.client.player.getGameProfile().getName() : null;
            StreamerMode streamerMode = ModuleManager.STREAMER_MODE;
            this.fHideHeader = streamerMode.k() && (Boolean)streamerMode.hideServerNumber.k() != false;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    @Unique
    private List<String[]> getTabNames() {
        long now = System.currentTimeMillis();
        if (now - this.tabNamesTime > 1000L) {
            this.tabNamesTime = now;
            this.tabNamesLower.clear();
            if (this.client.player != null && this.client.player.networkHandler != null) {
                for (PlayerListEntry PlayerListEntryVar : this.client.player.networkHandler.getListedPlayerListEntries()) {
                    String name;
                    if (PlayerListEntryVar.getProfile() == null || PlayerListEntryVar.getProfile().getName() == null || (name = PlayerListEntryVar.getProfile().getName()).length() < 3) continue;
                    this.tabNamesLower.add(new String[]{name.toLowerCase(), name});
                }
            }
        }
        return this.tabNamesLower;
    }

    @Unique
    private boolean isSelfRow(String label) {
        if (!this.fHighlightSelf) {
            return false;
        }
        return this.selfName != null && label.contains(this.selfName) || label.contains("Haron");
    }

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"))
    private int redirectDrawText(DrawContext DrawContextVar, TextRenderer TextRendererVar, Text TextVar, int i, int i2, int i3) {
        String strFindHaronPlayer;
        this.refreshFlags();
        String string = TextVar.getString();
        if (this.isSelfRow(string)) {
            Color accent = ClientColor.currentColor();
            int highlight = 0x46000000 | (accent.getRed() & 0xFF) << 16 | (accent.getGreen() & 0xFF) << 8 | accent.getBlue() & 0xFF;
            int textW = TextRendererVar.getWidth((StringVisitable)TextVar);
            DrawContextVar.fill(i - 2, i2 - 1, i + textW + 2, i2 + 9, highlight);
        }
        if ((strFindHaronPlayer = this.findHaronPlayer(string)) == null) {
            return DrawContextVar.drawTextWithShadow(TextRendererVar, TextVar, i, i2, i3);
        }
        this.renderLogo(DrawContextVar, i, i2);
        return DrawContextVar.drawTextWithShadow(TextRendererVar, TextVar, i + 8 + 2, i2, i3);
    }

    @Unique
    private String findHaronPlayer(String str) {
        String cached;
        if (str == null || str.isBlank()) {
            return null;
        }
        long now = System.currentTimeMillis();
        if (now - this.labelCacheTime > 2000L) {
            this.labelCacheTime = now;
            this.labelCache.clear();
        }
        if ((cached = this.labelCache.get(str)) != null) {
            return cached == NO_MATCH ? null : cached;
        }
        String result = null;
        String lowerCase = str.toLowerCase();
        for (String[] name : this.getTabNames()) {
            if (!lowerCase.contains(name[0]) || !HaronPlayerTracker.get().has(name[1])) continue;
            result = name[1];
            break;
        }
        this.labelCache.put(str, result == null ? NO_MATCH : result);
        return result;
    }

    @Redirect(method={"render"}, at=@At(value="FIELD", target="Lnet/minecraft/client/gui/hud/PlayerListHud;header:Lnet/minecraft/text/Text;", opcode=180))
    private Text getModifiedHeader(PlayerListHud PlayerListHudVar) {
        Text result;
        this.refreshFlags();
        if (!this.fHideHeader || this.header == null) {
            return this.header;
        }
        if (this.header == this.cachedHeaderSource && this.cachedHeaderHideFlag) {
            return this.cachedHeaderResult;
        }
        if (!this.header.getString().contains("Анархия-")) {
            result = this.header;
        } else {
            try {
                MinecraftClient MinecraftClientVarGetInstance = MinecraftClient.getInstance();
                result = Text.Serialization.fromJson((String)Text.Serialization.toJsonString((Text)this.header, (RegistryWrapper.WrapperLookup)MinecraftClientVarGetInstance.getNetworkHandler().getRegistryManager()).replaceAll("Анархия-\\d+", "Анархия-***"), (RegistryWrapper.WrapperLookup)MinecraftClientVarGetInstance.getNetworkHandler().getRegistryManager());
            }
            catch (Exception e) {
                result = Text.literal((String)this.header.getString().replaceAll("Анархия-\\d+", "Анархия-***"));
            }
        }
        this.cachedHeaderSource = this.header;
        this.cachedHeaderResult = result;
        this.cachedHeaderHideFlag = true;
        return result;
    }

    @Unique
    private void renderLogo(DrawContext DrawContextVar, int i, int i2) {
        Identifier IdentifierVar = HaronIcons.get("logo");
        Color cc = ClientColor.currentColor();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX);
        RenderSystem.setShaderTexture((int)0, (Identifier)IdentifierVar);
        RenderSystem.setShaderColor((float)((float)cc.getRed() / 255.0f), (float)((float)cc.getGreen() / 255.0f), (float)((float)cc.getBlue() / 255.0f), (float)1.0f);
        DrawContextVar.drawTexture(RenderLayer::getGuiTextured, IdentifierVar, i, i2, 0.0f, 0.0f, 8, 8, 8, 8);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.disableBlend();
    }
}

