package su.sacura.mixin.client.gui.hud;

import com.google.common.collect.Maps;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.sacura.Sacura;
import su.sacura.features.modules.impl.render.BetterMinecraftModule;
import su.sacura.util.impl.math.api.Animation;
import su.sacura.util.impl.math.api.Easing;

@Mixin(value={ChatHud.class})
public abstract class ChatHudMixin {
    @Shadow @Final private MinecraftClient field_2062;
    @Shadow @Final private List<ChatHudLine.Visible> field_2064;
    @Shadow private int field_2066;
    @Shadow private boolean field_2067;
    @Unique private final Map<ChatHudLine.Visible, Animation> sacura$animationMap = Maps.newHashMap();
    @Unique private final Set<ChatHudLine.Visible> sacura$noAnimate = new HashSet<>();
    @Unique private final Set<ChatHudLine.Visible> sacura$hidden = new HashSet<>();
    @Unique private boolean sacura$wasChatOpen = false;

    @Shadow public abstract boolean method_23677();
    @Shadow public abstract int method_1813();
    @Shadow public abstract double method_1814();
    @Shadow public abstract int method_1811();
    @Shadow protected abstract int method_45588(double var1, double var3);
    @Shadow protected abstract double method_44722(double var1);
    @Shadow protected abstract double method_44724(double var1);
    @Shadow protected abstract int method_44752();
    @Shadow private static double method_19348(int age) { throw new AbstractMethodError(); }
    @Shadow protected abstract int method_44720(ChatHudLine.Visible var1);
    @Shadow protected abstract void method_44719(DrawContext var1, int var2, int var3, MessageIndicator.Icon var4);

    @Overwrite
    public void method_1805(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused) {
        int v;
        int u;
        int t;
        BetterMinecraftModule betterMinecraft = Sacura.getInstance().getModuleManager().getModule(BetterMinecraftModule.class);
        if (betterMinecraft == null || !betterMinecraft.enable) {
            this.renderOriginal(context, currentTick, mouseX, mouseY, focused);
            return;
        }
        if (focused && !this.sacura$wasChatOpen) {
            this.sacura$animationMap.clear();
            this.sacura$noAnimate.clear();
            this.sacura$hidden.clear();
            for (ChatHudLine.Visible vis : this.field_2064) {
                if (vis == null) continue;
                int age = currentTick - vis.addedTime();
                double fade = ChatHudMixin.method_19348(age);
                if (fade <= 0.01) {
                    continue;
                } else if (fade < 0.99) {
                    this.sacura$hidden.add(vis);
                } else {
                    this.sacura$noAnimate.add(vis);
                }
            }
        }
        this.sacura$wasChatOpen = focused;
        if (this.method_23677()) {
            return;
        }
        int i = this.method_1813();
        int j = this.field_2064.size();
        if (j <= 0) {
            return;
        }
        Profiler profiler = Profilers.get();
        profiler.push("chat");
        float f = (float)this.method_1814();
        int k = MathHelper.ceil((float)((float)this.method_1811() / f));
        int l = context.getScaledWindowHeight();
        context.getMatrices().push();
        context.getMatrices().scale(f, f, 1.0f);
        context.getMatrices().translate(4.0f, 0.0f, 0.0f);
        int m = MathHelper.floor((float)((float)(l - 40) / f));
        int n = this.method_45588(this.method_44722(mouseX), this.method_44724(mouseY));
        double d = (Double)this.field_2062.options.getChatOpacity().getValue() * (double)0.9f + (double)0.1f;
        double e = (Double)this.field_2062.options.getTextBackgroundOpacity().getValue();
        double g = (Double)this.field_2062.options.getChatLineSpacing().getValue();
        int o = this.method_44752();
        int p = (int)Math.round(-8.0 * (g + 1.0) + 4.0 * g);
        int q = 0;
        for (int r = 0; r + this.field_2066 < this.field_2064.size() && r < i; ++r) {
            Animation animation;
            int s = r + this.field_2066;
            ChatHudLine.Visible visible = this.field_2064.get(s);
            if (visible == null || (t = currentTick - visible.addedTime()) >= 200 && !focused) continue;
            if (this.sacura$hidden.contains(visible)) continue;
            double h = focused ? 1.0 : ChatHudMixin.method_19348(t);
            u = (int)(255.0 * h * d);
            v = (int)(255.0 * h * e);
            ++q;
            if (u <= 3) continue;
            int x = m - r * o;
            int y = x + p;
            if (((Boolean)betterMinecraft.chatImprove.get()).booleanValue()) {
                int textWidth = this.field_2062.textRenderer.getWidth(visible.content());
                context.fill(-4, x - o, textWidth + 4, x, v << 24);
            } else {
                context.fill(-4, x - o, k + 4, x, v << 24);
            }
            MessageIndicator messageIndicator = visible.indicator();
            if (messageIndicator != null) {
                int z = messageIndicator.indicatorColor() | u << 24;
                context.fill(-4, x - o, -2, x, z);
                if (s == n && messageIndicator.icon() != null) {
                    int aa = this.method_44720(visible);
                    Objects.requireNonNull(this.field_2062.textRenderer);
                    int ab = y + 9;
                    this.method_44719(context, aa, ab, messageIndicator.icon());
                }
            }
            context.getMatrices().push();
            context.getMatrices().translate(0.0, 0.0, 50.0);
            if (((Boolean)betterMinecraft.chatAnimation.get()).booleanValue()
                    && !this.sacura$noAnimate.contains(visible)
                    && !(animation = this.sacura$animationMap.computeIfAbsent(visible, vis -> new Animation(400, 1.0, true, Easing.EASE_OUT_CIRC))).isDone()) {
                double output = animation.getOutput();
                float textWidth = this.field_2062.textRenderer.getWidth(visible.content());
                float animationOffset = (float)((double)(-textWidth) * (1.0 - output));
                context.getMatrices().translate((double)animationOffset, 0.0, 0.0);
            }
            context.drawTextWithShadow(this.field_2062.textRenderer, visible.content(), 0, y, ColorHelper.withAlpha((int)u, (int)-1));
            context.getMatrices().pop();
        }
        long ac = this.field_2062.getMessageHandler().getUnprocessedMessageCount();
        if (ac > 0L) {
            int ad = (int)(128.0 * d);
            t = (int)(255.0 * e);
            context.getMatrices().push();
            context.getMatrices().translate(0.0f, (float)m, 50.0f);
            context.fill(-2, 0, k + 4, 9, t << 24);
            context.drawTextWithShadow(this.field_2062.textRenderer, (Text)Text.translatable((String)"chat.queue", (Object[])new Object[]{ac}), 0, 1, 0xFFFFFF + (ad << 24));
            context.getMatrices().pop();
        }
        if (focused) {
            int ad = this.method_44752();
            t = j * ad;
            int ae = q * ad;
            int af = this.field_2066 * ae / j - m;
            u = ae * ae / t;
            if (t != ae) {
                v = af > 0 ? 170 : 96;
                int w = this.field_2067 ? 0xCC3333 : 0x3333AA;
                int x_scrollbar = k + 4;
                context.fill(x_scrollbar, -af, x_scrollbar + 2, -af - u, 100, w + (v << 24));
                context.fill(x_scrollbar + 2, -af, x_scrollbar + 1, -af - u, 100, 0xCCCCCC + (v << 24));
            }
        }
        context.getMatrices().pop();
        profiler.pop();
    }

    @Unique
    private void renderOriginal(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused) {
        int v;
        int u;
        int t;
        if (this.method_23677()) {
            return;
        }
        int i = this.method_1813();
        int j = this.field_2064.size();
        if (j <= 0) {
            return;
        }
        Profiler profiler = Profilers.get();
        profiler.push("chat");
        float f = (float)this.method_1814();
        int k = MathHelper.ceil((float)((float)this.method_1811() / f));
        int l = context.getScaledWindowHeight();
        context.getMatrices().push();
        context.getMatrices().scale(f, f, 1.0f);
        context.getMatrices().translate(4.0f, 0.0f, 0.0f);
        int m = MathHelper.floor((float)((float)(l - 40) / f));
        int n = this.method_45588(this.method_44722(mouseX), this.method_44724(mouseY));
        double d = (Double)this.field_2062.options.getChatOpacity().getValue() * (double)0.9f + (double)0.1f;
        double e = (Double)this.field_2062.options.getTextBackgroundOpacity().getValue();
        double g = (Double)this.field_2062.options.getChatLineSpacing().getValue();
        int o = this.method_44752();
        int p = (int)Math.round(-8.0 * (g + 1.0) + 4.0 * g);
        int q = 0;
        for (int r = 0; r + this.field_2066 < this.field_2064.size() && r < i; ++r) {
            int s = r + this.field_2066;
            ChatHudLine.Visible visible = this.field_2064.get(s);
            if (visible == null || (t = currentTick - visible.addedTime()) >= 200 && !focused) continue;
            double h = focused ? 1.0 : ChatHudMixin.method_19348(t);
            u = (int)(255.0 * h * d);
            v = (int)(255.0 * h * e);
            ++q;
            if (u <= 3) continue;
            int x = m - r * o;
            int y = x + p;
            context.fill(-4, x - o, k + 4, x, v << 24);
            MessageIndicator messageIndicator = visible.indicator();
            if (messageIndicator != null) {
                int z = messageIndicator.indicatorColor() | u << 24;
                context.fill(-4, x - o, -2, x, z);
                if (s == n && messageIndicator.icon() != null) {
                    int aa = this.method_44720(visible);
                    Objects.requireNonNull(this.field_2062.textRenderer);
                    int ab = y + 9;
                    this.method_44719(context, aa, ab, messageIndicator.icon());
                }
            }
            context.getMatrices().push();
            context.getMatrices().translate(0.0, 0.0, 50.0);
            context.drawTextWithShadow(this.field_2062.textRenderer, visible.content(), 0, y, ColorHelper.withAlpha((int)u, (int)-1));
            context.getMatrices().pop();
        }
        long ac = this.field_2062.getMessageHandler().getUnprocessedMessageCount();
        if (ac > 0L) {
            int ad = (int)(128.0 * d);
            t = (int)(255.0 * e);
            context.getMatrices().push();
            context.getMatrices().translate(0.0f, (float)m, 50.0f);
            context.fill(-2, 0, k + 4, 9, t << 24);
            context.drawTextWithShadow(this.field_2062.textRenderer, (Text)Text.translatable((String)"chat.queue", (Object[])new Object[]{ac}), 0, 1, 0xFFFFFF + (ad << 24));
            context.getMatrices().pop();
        }
        if (focused) {
            int ad = this.method_44752();
            t = j * ad;
            int ae = q * ad;
            int af = this.field_2066 * ae / j - m;
            u = ae * ae / t;
            if (t != ae) {
                v = af > 0 ? 170 : 96;
                int w = this.field_2067 ? 0xCC3333 : 0x3333AA;
                int x_scrollbar = k + 4;
                context.fill(x_scrollbar, -af, x_scrollbar + 2, -af - u, 100, w + (v << 24));
                context.fill(x_scrollbar + 2, -af, x_scrollbar + 1, -af - u, 100, 0xCCCCCC + (v << 24));
            }
        }
        context.getMatrices().pop();
        profiler.pop();
    }

    @Inject(method={"clear(Z)V"}, at={@At(value="HEAD")})
    private void onClear(boolean clearHistory, CallbackInfo ci) {
        this.sacura$animationMap.clear();
        this.sacura$noAnimate.clear();
        this.sacura$hidden.clear();
    }
}