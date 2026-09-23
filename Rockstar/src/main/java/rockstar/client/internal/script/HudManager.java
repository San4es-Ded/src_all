package rockstar.client.internal.script;













import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.notification.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.esp.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import org.lwjgl.opengl.GL11;
import pyrock.events.render.ChatRenderEvent;
import pyrock.events.render.HudRenderEvent;
import pyrock.events.window.ChatClickEvent;
import pyrock.events.window.ChatReleaseEvent;
import pyrock.events.window.ChatScrollEvent;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.Fonts;
import rockstar.modules.visual.ViewModelModule;
import rockstar.client.internal.ui.SettingHistoryManager;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.event.EventListener;
import rockstar.client.internal.script.WaypointContextMenuHandler;
import rockstar.client.internal.script.HudTooltip;
import rockstar.client.internal.script.HudContextMenu;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.internal.script.HudAlignmentGuides;
import rockstar.client.internal.ui.AbstractHudWidget;
import rockstar.client.internal.script.HudLayerEvents;
import rockstar.client.internal.core.HudPositionHistory;
import rockstar.client.internal.script.HudElementBase;
import rockstar.client.internal.script.HotbarHud;
import rockstar.client.internal.script.EffectsHud;
import rockstar.client.internal.script.ItemBindsHud;
import rockstar.client.internal.script.KeybindsHud;
import rockstar.client.internal.script.StaffListHud;
import rockstar.client.internal.script.TargetHud;
import rockstar.client.internal.ui.TotemCounterHud;
import rockstar.client.internal.script.DynamicIslandHud;
import rockstar.client.RockstarClient;
import rockstar.client.internal.script.ArmorHud;
import rockstar.client.internal.ui.PlayerStatsHud;
import rockstar.client.internal.ui.WorldInfoHud;
import rockstar.client.animation.Easing;
import rockstar.client.esp.EspManager;
import rockstar.client.esp.NametagEspFeature;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.ScreenMetricsAccess;
import rockstar.client.render.HudRenderUtils;
import rockstar.client.render.UiBatchRenderer;
import rockstar.client.util.Stopwatch;
import rockstar.client.notification.NotificationType;

public class HudManager
implements MinecraftClientAccess,
ScreenMetricsAccess {
    private final List<AbstractHudWidget> internalField0416 = new ArrayList<AbstractHudWidget>();
    private final List<AbstractHudWidget> internalField0417 = new ArrayList<AbstractHudWidget>();
    private final List<AbstractHudWidget> internalField1145 = Collections.unmodifiableList(this.internalField0416);
    private final List<AbstractHudWidget> internalField1146 = Collections.unmodifiableList(this.internalField0417);
    private final List<AbstractHudWidget> internalField1148 = new InternalType0165();
    private final List<HudContextMenu> internalField1147 = new ArrayList<HudContextMenu>();
    public DynamicIslandHud internalField0209;
    public HotbarHud internalField0016;
    private final HudPositionHistory internalField0948 = new HudPositionHistory();
    private final HudAlignmentGuides internalField0937 = new HudAlignmentGuides();
    private String internalField0248 = "";
    private HudTooltip internalField0565;
    private final Stopwatch internalField0519 = new Stopwatch();
    private final Set<AbstractHudWidget> failedHudElements = Collections.newSetFromMap(new IdentityHashMap<>());
    private float internalField0205;
    private float internalField0206;
    private final EventListener<HudRenderEvent> internalField0157 = hudRenderEvent -> {
        UiRenderContext iII = UiRenderContext.internalMethod02316(hudRenderEvent.getContext(), HudManager.internalField0149.currentScreen == null ? -1 : (int)UiUtils.internalMethod03634().x(), HudManager.internalField0149.currentScreen == null ? -1 : (int)UiUtils.internalMethod03634().y(), MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false));
        if (this.internalField0565 == null) {
            this.internalField0565 = new HudTooltip(Fonts.internalField1154.internalMethod01432(10.0f), 10.0f, 300L, Easing.internalField0812).internalMethod01888();
        }
        this.internalField0248 = "";
        this.internalMethod07012();
        this.internalField0937.internalMethod02073();
        HudLayerEvents.internalMethod01097(iII, this.internalField1148.size());
        try (UiBatchRenderer typedValue250 = UiBatchRenderer.internalMethod04455();){
            for (AbstractHudWidget internalValue0002 : this.internalField1148) {
                this.renderHudElement(iII, internalValue0002);
            }
            this.internalField0937.internalMethod05682(iII);
            this.internalField0565.internalMethod04932(internalField0389.internalMethod03585() / 2.0f, 30.0f);
            if (!this.internalField0248.contains(".description")) {
                this.internalField0565.internalMethod06105(this.internalField0248);
                this.internalField0565.internalMethod03398(iII);
            }
        }
        HudLayerEvents.internalMethod02900(iII, this.internalField1148.size());
        boolean bl = HudManager.internalField0149.currentScreen instanceof ChatScreen;
        for (HudContextMenu typedValue191 : this.internalField1147) {
            if (bl) continue;
            typedValue191.internalMethod05781(false);
        }
        if (!bl && !this.internalField1147.isEmpty()) {
            this.internalMethod04634(iII);
        }
        if (!bl) {
            for (AbstractHudWidget typedValue197 : this.internalField1148) {
                if (!typedValue197.isDragging()) continue;
                typedValue197.onMouseReleased(typedValue197.getX(), typedValue197.getY(), MouseButton.internalField0102);
            }
            CursorManager.internalMethod06882(CursorType.internalField0566);
        }
        this.internalField1147.removeIf(typedValue190 -> typedValue190.internalMethod06960().internalMethod02881() == 0.0f && !typedValue190.internalMethod08805());
    };

    private void renderHudElement(UiRenderContext context, AbstractHudWidget element) {
        int scissorDepth = ScissorStack.internalMethod07642();
        float[] shaderColor = RenderSystem.getShaderColor().clone();
        boolean blendEnabled = RenderSystem.isBlendEnabled();
        boolean cullEnabled = RenderSystem.isCullEnabled();
        boolean depthEnabled = RenderSystem.isDepthTestEnabled();

        try {
            element.render(context);
            if (element.getSelecting().internalMethod02881() > 0.0f) {
                float visibility = element.getAnimation().internalMethod02881() * element.getVisible().internalMethod02881();
                float scale = 0.5f + visibility * 0.5f - 0.05f * element.getSelecting().internalMethod02881();
                element.getLoadingAnim().internalMethod07061(1500L);
                element.getLoadingAnim().internalMethod07059(1.0f);
                if (element.getLoadingAnim().internalMethod02881() == 1.0f) {
                    element.getLoadingAnim().internalMethod07060(0.0f);
                }

                HudRenderUtils.internalMethod08976(context.getMatrices(), element.getX() + element.getWidth() / 2.0f, element.getY() + element.getHeight() / 2.0f, scale);
                try {
                    context.drawLoadingRect(element.getX(), element.getY(), element.getWidth(), element instanceof HudElementBase ? Math.max(20.0f, element.getHeight()) : element.getHeight(), element.getLoadingAnim().internalMethod02881() * 2.2f - 0.5f, CornerRadii.internalMethod03908(7.0f), ColorRGBA.WHITE.withAlpha(100.0f * element.getSelecting().internalMethod02881()));
                } finally {
                    HudRenderUtils.internalMethod00012(context.getMatrices());
                }
            }

            this.failedHudElements.remove(element);
        } catch (VirtualMachineError error) {
            throw error;
        } catch (ThreadDeath error) {
            throw error;
        } catch (Throwable throwable) {
            if (this.failedHudElements.add(element)) {
                RockstarClient.internalField0572.error("Failed to render HUD element {}. The element was isolated from the rest of the interface.", element.getName(), throwable);
            }
            element.rebuild();
        } finally {
            try {
                ScissorStack.restoreDepth(scissorDepth);
            } finally {
                RenderSystem.setShaderColor(shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3]);
                if (blendEnabled) {
                    RenderSystem.enableBlend();
                } else {
                    RenderSystem.disableBlend();
                }
                if (cullEnabled) {
                    RenderSystem.enableCull();
                } else {
                    RenderSystem.disableCull();
                }
                if (depthEnabled) {
                    RenderSystem.enableDepthTest();
                } else {
                    RenderSystem.disableDepthTest();
                }
            }
        }
    }
    private final EventListener<ChatRenderEvent> internalField0158 = chatRenderEvent -> {
        UiRenderContext iII = UiRenderContext.internalMethod02316(chatRenderEvent.getContext(), HudManager.internalField0149.currentScreen == null ? -1 : (int)UiUtils.internalMethod03634().x(), HudManager.internalField0149.currentScreen == null ? -1 : (int)UiUtils.internalMethod03634().y(), MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false));
        this.internalMethod04634(iII);
    };
    private final EventListener<ChatScrollEvent> internalField1028 = chatScrollEvent -> {
        for (HudContextMenu typedValue190 : this.internalField1147) {
            typedValue190.internalMethod02890(chatScrollEvent.getX(), chatScrollEvent.getY(), chatScrollEvent.getHorizontal(), chatScrollEvent.getVertical());
        }
    };
    private final EventListener<ChatClickEvent> internalField1029 = chatClickEvent -> {
        for (HudContextMenu object : this.internalField1147) {
            object.internalMethod01643(chatClickEvent.getX(), chatClickEvent.getY(), MouseButton.internalMethod01669(chatClickEvent.getButton()));
            if (object.internalMethod04933(chatClickEvent.getX(), chatClickEvent.getY())) {
                return;
            }
            object.internalMethod05781(false);
        }
        for (AbstractHudWidget typedValue197 : this.internalField1148) {
            typedValue197.onMouseClicked(chatClickEvent.getX(), chatClickEvent.getY(), MouseButton.internalMethod01669(chatClickEvent.getButton()));
            if ((!typedValue197.isHovered(chatClickEvent.getX(), chatClickEvent.getY()) || !typedValue197.isShowing()) && !typedValue197.isDragging()) continue;
            return;
        }
        if (chatClickEvent.getButton() == 1 && this.internalMethod07288(chatClickEvent.getX(), chatClickEvent.getY())) {
            return;
        }
        if (chatClickEvent.getButton() == 1 && !this.internalMethod04611().isEmpty()) {
            HudContextMenu typedValue191 = new HudContextMenu(chatClickEvent.getX(), chatClickEvent.getY(), 90.0f, 6.0f).internalMethod01608(120.0f).internalMethod05789(LanguageManager.internalMethod07214("whatadd"), 8, true);
            for (AbstractHudWidget typedValue197 : this.internalMethod04611()) {
                typedValue191.internalMethod03323(LanguageManager.internalMethod07214(typedValue197.getName()), typedValue197.getIcon(), typedValue190 -> {
                    typedValue197.pos(chatClickEvent.getX(), chatClickEvent.getY());
                    typedValue197.setShowing(true);
                    typedValue190.internalMethod05781(false);
                    RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
                });
            }
            this.internalField1147.add(typedValue191);
        } else if (chatClickEvent.getButton() == 1 && this.internalMethod04611().isEmpty() && this.internalField0519.internalMethod02365(600L)) {
            RockstarClient.getInstance().internalMethod02503().internalMethod00599(NotificationType.internalField0705, LanguageManager.internalMethod07214("hud.no_elements"), LanguageManager.internalMethod07214("hud.no_elements.desc"));
            this.internalField0519.internalMethod00701();
        }
    };
    private final EventListener<ChatReleaseEvent> internalField1030 = chatReleaseEvent -> {
        for (HudContextMenu internalValue0002 : this.internalField1147) {
            internalValue0002.internalMethod02863(chatReleaseEvent.getX(), chatReleaseEvent.getY(), MouseButton.internalMethod01669(chatReleaseEvent.getButton()));
            if (!internalValue0002.internalMethod04933(chatReleaseEvent.getX(), chatReleaseEvent.getY())) continue;
            return;
        }
        for (AbstractHudWidget typedValue197 : this.internalField1148) {
            typedValue197.onMouseReleased(chatReleaseEvent.getX(), chatReleaseEvent.getY(), MouseButton.internalMethod01669(chatReleaseEvent.getButton()));
        }
    };

    private void internalMethod07008() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
        AbstractHudWidget[] var1 = new AbstractHudWidget[]{new EffectsHud(), new KeybindsHud(), new TargetHud(), new ItemBindsHud(), new DynamicIslandHud(), new WorldInfoHud(), new PlayerStatsHud(), new ArmorHud(), new TotemCounterHud(), new StaffListHud(), new HotbarHud()};
        this.internalField1148.addAll(List.of(var1));
        this.internalField0209 = new DynamicIslandHud();
        this.internalField0016 = new HotbarHud();
    }

    public HudManager() {
        this.internalMethod07008();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void internalMethod04634(UiRenderContext iII) {
        boolean bl = RenderSystem.isDepthTestEnabled();
        RenderSystem.disableDepthTest();
        iII.getMatrices().pushMatrix();
        iII.getMatrices().translate(0.0f, 0.0f);
        try (UiBatchRenderer typedValue250 = UiBatchRenderer.internalMethod04455();){
            for (HudContextMenu typedValue190 : this.internalField1147) {
                typedValue190.internalMethod04739(10.0f).internalMethod03398(iII);
            }
        }
        finally {
            iII.getMatrices().popMatrix();
            if (bl) {
                RenderSystem.enableDepthTest();
            } else {
                RenderSystem.disableDepthTest();
            }
        }
    }

    public boolean internalMethod04745(int n, int n2, int n3) {
        for (int i = this.internalField1147.size() - 1; i >= 0; --i) {
            HudContextMenu typedValue190 = this.internalField1147.get(i);
            if (!typedValue190.internalMethod08805() || !typedValue190.internalMethod00980(n, n2, n3)) continue;
            return true;
        }
        return false;
    }

    public boolean internalMethod07287(char c, int n) {
        for (int i = this.internalField1147.size() - 1; i >= 0; --i) {
            HudContextMenu typedValue190 = this.internalField1147.get(i);
            if (!typedValue190.internalMethod08805() || !typedValue190.internalMethod05413(c, n)) continue;
            return true;
        }
        return false;
    }

    public boolean internalMethod07289(int n, int n2) {
        long l;
        boolean bl;
        boolean bl2;
        if ((n2 & 2) == 0) {
            return false;
        }
        boolean bl3 = bl2 = n == 89;
        if (!bl2 && n != 90) {
            return false;
        }
        boolean bl4 = false;
        for (int i = this.internalField1147.size() - 1; i >= 0; --i) {
            HudContextMenu typedValue190 = this.internalField1147.get(i);
            if (!typedValue190.internalMethod08805()) continue;
            bl4 = true;
            if (!(bl2 ? typedValue190.internalMethod02629() : typedValue190.internalMethod02626())) continue;
            return true;
        }
        if (bl4) {
            return true;
        }
        ViewModelModule typedValue323 = RockstarClient.getInstance().getModuleManager().getModule(ViewModelModule.class);
        boolean bl5 = bl = typedValue323 != null && typedValue323.internalMethod09268();
        long l2 = !bl ? Long.MIN_VALUE : (bl2 ? SettingHistoryManager.internalMethod03021(typedValue323) : SettingHistoryManager.internalMethod06204(typedValue323));
        long l3 = l = bl2 ? this.internalField0948.internalMethod05243() : this.internalField0948.internalMethod05237();
        if (l2 > l && (bl2 ? SettingHistoryManager.internalMethod03022(typedValue323) : SettingHistoryManager.internalMethod06205(typedValue323))) {
            return true;
        }
        if (l != Long.MIN_VALUE) {
            if (bl2) {
                this.internalField0948.internalMethod05244();
            } else {
                this.internalField0948.internalMethod05238();
            }
            return true;
        }
        return bl && (bl2 ? SettingHistoryManager.internalMethod03022(typedValue323) : SettingHistoryManager.internalMethod06205(typedValue323));
    }

    private void internalMethod07012() {
        float f = internalField0389.internalMethod03585();
        float f2 = internalField0389.internalMethod03589();
        if (this.internalField0205 == 0.0f || this.internalField0206 == 0.0f) {
            this.internalField0205 = f;
            this.internalField0206 = f2;
            return;
        }
        if (f == this.internalField0205 && f2 == this.internalField0206) {
            return;
        }
        for (AbstractHudWidget typedValue197 : this.internalField1148) {
            typedValue197.reanchor(this.internalField0205, this.internalField0206, f, f2);
        }
        this.internalField0205 = f;
        this.internalField0206 = f2;
    }

    private boolean internalMethod07288(float f, float f2) {
        NametagEspFeature typedValue095 = EspManager.internalMethod06726().internalMethod05464(NametagEspFeature.class);
        if (typedValue095 != null && typedValue095.internalMethod00775(f, f2)) {
            return true;
        }
        WaypointContextMenuHandler typedValue189 = RockstarClient.getInstance().internalMethod06122();
        return typedValue189 != null && typedValue189.internalMethod04179(f, f2);
    }

    public List<AbstractHudWidget> internalMethod06922() {
        return this.internalField1145;
    }

    public List<AbstractHudWidget> internalMethod04611() {
        return this.internalField1146;
    }

    public void internalMethod00050(AbstractHudWidget typedValue197) {
        if (!this.internalField1148.contains(typedValue197)) {
            return;
        }
        this.internalMethod08486();
    }

    public void internalMethod08486() {
        this.internalField0416.clear();
        this.internalField0417.clear();
        for (AbstractHudWidget typedValue197 : this.internalField1148) {
            if (typedValue197.isShowing()) {
                this.internalField0416.add(typedValue197);
                continue;
            }
            this.internalField0417.add(typedValue197);
        }
    }

    public <T extends AbstractHudWidget> T internalMethod06084(String string) {
        return (T)((AbstractHudWidget)this.internalField1148.stream().filter(typedValue197 -> typedValue197.getName().equalsIgnoreCase(string)).findFirst().orElse(null));
    }

    @Generated
    public List<AbstractHudWidget> internalMethod08183() {
        return this.internalField0416;
    }

    @Generated
    public List<AbstractHudWidget> internalMethod08756() {
        return this.internalField0417;
    }

    @Generated
    public List<AbstractHudWidget> internalMethod09118() {
        return this.internalField1145;
    }

    @Generated
    public List<AbstractHudWidget> internalMethod08665() {
        return this.internalField1146;
    }

    @Generated
    public List<AbstractHudWidget> internalMethod09520() {
        return this.internalField1148;
    }

    @Generated
    public List<HudContextMenu> internalMethod09283() {
        return this.internalField1147;
    }

    @Generated
    public DynamicIslandHud internalMethod01259() {
        return this.internalField0209;
    }

    @Generated
    public HotbarHud internalMethod01440() {
        return this.internalField0016;
    }

    @Generated
    public HudPositionHistory internalMethod07612() {
        return this.internalField0948;
    }

    @Generated
    public HudAlignmentGuides internalMethod07611() {
        return this.internalField0937;
    }

    @Generated
    public String internalMethod04408() {
        return this.internalField0248;
    }

    @Generated
    public HudTooltip internalMethod04396() {
        return this.internalField0565;
    }

    @Generated
    public Stopwatch internalMethod03860() {
        return this.internalField0519;
    }

    @Generated
    public float internalMethod07007() {
        return this.internalField0205;
    }

    @Generated
    public float internalMethod07011() {
        return this.internalField0206;
    }

    @Generated
    public EventListener<HudRenderEvent> internalMethod07491() {
        return this.internalField0157;
    }

    @Generated
    public EventListener<ChatRenderEvent> internalMethod00739() {
        return this.internalField0158;
    }

    @Generated
    public EventListener<ChatScrollEvent> internalMethod08770() {
        return this.internalField1028;
    }

    @Generated
    public EventListener<ChatClickEvent> internalMethod09004() {
        return this.internalField1029;
    }

    @Generated
    public EventListener<ChatReleaseEvent> internalMethod08877() {
        return this.internalField1030;
    }

    @Generated
    public void internalMethod00688(String string) {
        this.internalField0248 = string;
    }

    final class InternalType0165
    extends ArrayList<AbstractHudWidget> {
        InternalType0165() {
        }

        public boolean internalMethod07253(AbstractHudWidget typedValue197) {
            boolean bl = super.add(typedValue197);
            if (bl) {
                HudManager.this.internalMethod08486();
            }
            return bl;
        }

        public void internalMethod07382(int n, AbstractHudWidget typedValue197) {
            super.add(n, typedValue197);
            HudManager.this.internalMethod08486();
        }

        @Override
        public boolean addAll(Collection<? extends AbstractHudWidget> collection) {
            boolean bl = super.addAll(collection);
            if (bl) {
                HudManager.this.internalMethod08486();
            }
            return bl;
        }

        @Override
        public boolean addAll(int n, Collection<? extends AbstractHudWidget> collection) {
            boolean bl = super.addAll(n, collection);
            if (bl) {
                HudManager.this.internalMethod08486();
            }
            return bl;
        }

        public AbstractHudWidget internalMethod04203(int n) {
            AbstractHudWidget typedValue197 = (AbstractHudWidget)super.remove(n);
            HudManager.this.internalMethod08486();
            return typedValue197;
        }

        @Override
        public boolean remove(Object object) {
            boolean bl = super.remove(object);
            if (bl) {
                HudManager.this.internalMethod08486();
            }
            return bl;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            boolean bl = super.removeAll(collection);
            if (bl) {
                HudManager.this.internalMethod08486();
            }
            return bl;
        }

        @Override
        public boolean removeIf(Predicate<? super AbstractHudWidget> predicate) {
            boolean bl = super.removeIf(predicate);
            if (bl) {
                HudManager.this.internalMethod08486();
            }
            return bl;
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            boolean bl = super.retainAll(collection);
            if (bl) {
                HudManager.this.internalMethod08486();
            }
            return bl;
        }

        public AbstractHudWidget internalMethod06616(int n, AbstractHudWidget typedValue197) {
            AbstractHudWidget typedValue198 = super.set(n, typedValue197);
            HudManager.this.internalMethod08486();
            return typedValue198;
        }

        @Override
        public void clear() {
            if (this.isEmpty()) {
                return;
            }
            super.clear();
            HudManager.this.internalMethod08486();
        }

    }
}
