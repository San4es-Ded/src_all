package haron.modules.visuals;

import haron.animation.ChatInputAnimation;
import haron.animation.PerspectiveDistanceAnimation;
import haron.animation.HotbarSelectionAnimation;
import haron.animation.PlayerListAnimation;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.SettingGroup;
import haron.settings.BooleanSetting;
import java.util.Objects;

@ModuleInfo(a="Animations", b="Interface animations", c=ModuleCategory.VISUALS)
public class Animations
extends HaronModule {
    private final SettingGroup playerListGroup = new SettingGroup("Список игроков");
    public final BooleanSetting playerListEnabled = new BooleanSetting("Таблица игроков", true);
    public final BooleanSetting tabHighlight = new BooleanSetting("Подсветка таба", true);
    public final NumberSetting playerListDuration;
    private final SettingGroup chatGroup;
    public final BooleanSetting chatMessagesEnabled;
    public final NumberSetting chatDuration;
    private final SettingGroup hotbarGroup;
    public final BooleanSetting hotbarSelectorEnabled;
    public final NumberSetting hotbarDuration;
    private final SettingGroup cameraGroup;
    public final BooleanSetting perspectiveEnabled;
    public final NumberSetting perspectiveDuration;
    private static final PlayerListAnimation PLAYER_LIST_ANIMATION = new PlayerListAnimation();
    private static final ChatInputAnimation CHAT_INPUT_ANIMATION = new ChatInputAnimation();
    private static final HotbarSelectionAnimation HOTBAR_SELECTION_ANIMATION = new HotbarSelectionAnimation();
    private static final PerspectiveDistanceAnimation PERSPECTIVE_DISTANCE_ANIMATION = new PerspectiveDistanceAnimation();

    public Animations() {
        NumberSetting by6erl2 = new NumberSetting("Длительность таба", 200.0f, 50.0f, 500.0f, 10.0f);
        BooleanSetting xcv91t2 = this.playerListEnabled;
        Objects.requireNonNull(xcv91t2);
        this.playerListDuration = by6erl2.a(xcv91t2::a);
        this.chatGroup = new SettingGroup("Чат");
        this.chatMessagesEnabled = new BooleanSetting("Сообщения чата", true);
        NumberSetting by6erl3 = new NumberSetting("Длительность чата", 200.0f, 50.0f, 500.0f, 10.0f);
        BooleanSetting xcv91t3 = this.chatMessagesEnabled;
        Objects.requireNonNull(xcv91t3);
        this.chatDuration = by6erl3.a(xcv91t3::a);
        this.hotbarGroup = new SettingGroup("Хотбар");
        this.hotbarSelectorEnabled = new BooleanSetting("Селектор хотбара", true);
        NumberSetting by6erl4 = new NumberSetting("Длительность хотбара", 100.0f, 50.0f, 500.0f, 10.0f);
        BooleanSetting xcv91t4 = this.hotbarSelectorEnabled;
        Objects.requireNonNull(xcv91t4);
        this.hotbarDuration = by6erl4.a(xcv91t4::a);
        this.cameraGroup = new SettingGroup("Камера");
        this.perspectiveEnabled = new BooleanSetting("Перспектива", true);
        NumberSetting by6erl5 = new NumberSetting("Длительность перспективы", 300.0f, 50.0f, 1000.0f, 10.0f);
        BooleanSetting xcv91t5 = this.perspectiveEnabled;
        Objects.requireNonNull(xcv91t5);
        this.perspectiveDuration = by6erl5.a(xcv91t5::a);
    }

    public static PlayerListAnimation playerListAnimation() {
        return PLAYER_LIST_ANIMATION;
    }

    public static ChatInputAnimation chatInputAnimation() {
        return CHAT_INPUT_ANIMATION;
    }

    public static HotbarSelectionAnimation hotbarSelectionAnimation() {
        return HOTBAR_SELECTION_ANIMATION;
    }

    public static PerspectiveDistanceAnimation perspectiveDistanceAnimation() {
        return PERSPECTIVE_DISTANCE_ANIMATION;
    }
}
