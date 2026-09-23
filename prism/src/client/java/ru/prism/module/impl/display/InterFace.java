package ru.prism.module.impl.display;

import org.joml.Vector2f;
import ru.prism.manager.event_impl.AttackEvent;
import ru.prism.manager.event_impl.EventDisplay;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.manager.event_impl.EventUpdate;
import ru.prism.manager.event_impl.MousePressEvent;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.*;
import ru.prism.module.impl.display.interfaceimpl.*;
import ru.prism.utils.other.Instance;

import java.io.PrintStream;

@ModuleInfo(
        name = "Inter Face",
        desc = "Твой ебаный HUD, настраивай как хочешь.",
        category = Category.HUD
)
public class InterFace extends Module {

    public static InterFace getInstance() {
        return Instance.get(InterFace.class);
    }

    public MultiBooleanSetting element = new MultiBooleanSetting(this, "Элементы",
            new BooleanSetting("Water mark", true),
            new BooleanSetting("Information", true),
            new BooleanSetting("Key Binds", true),
            new BooleanSetting("Potions", true),
            new BooleanSetting("Music Player", true),
            new BooleanSetting("Notifications", true),
            new BooleanSetting("Отображение таргета", true),
            new BooleanSetting("Cooldowns", true));

    public SliderSetting volume = new SliderSetting(this, "Громкость уведомления", 0.5F, 0.1F, 1.0F, 0.1F);
    public ModeSetting typeNotify = new ModeSetting(this, "Тип уведомления",
            "Первый", "Второй", "Третий");


    public SliderSetting sizeHud = new SliderSetting(this,"Размер интерфейса",1.0F,0.5F,1.5F,0.05F);
    public SliderSetting alphaHUD = new SliderSetting(this,"Прозрачность худа",0.6F,0.0F,0.9F,0.1F);

    public SliderSetting musicScale = new SliderSetting(this, "Масштаб плеера", 1.0F, 0.5F, 2.0F, 0.05F).setVisible(() -> false);

    public BooleanSetting notifyEffects = new BooleanSetting(this, "эффектах", true);
    public BooleanSetting notifyModules = new BooleanSetting(this, "модулях", true);
    public BooleanSetting notifyArmor = new BooleanSetting(this, "броне", true);

    public DragSetting waterMark = new DragSetting(this, "WaterMark", new Vector2f(10, 10));
    public DragSetting information = new DragSetting(this, "Information", new Vector2f(10, 30));
    public DragSetting keyBind = new DragSetting(this, "Key Binds", new Vector2f(10, 50));
    public DragSetting potion = new DragSetting(this, "Potions", new Vector2f(90, 50));
    public DragSetting music = new DragSetting(this, "Music Player", new Vector2f(10, 400));
    public DragSetting notifications = new DragSetting(this, "Notifications", new Vector2f(0, 200));
    public DragSetting target = new DragSetting(this, "Target HUD", new Vector2f(200, 150));
    public DragSetting cooldowns = new DragSetting(this, "Cooldowns", new Vector2f(200, 250));

    private final WaterMark waterMarkElemnt = new WaterMark();
    private final Information informationElemnt = new Information();
    private final KeyBinds keyBinds = new KeyBinds();
    private final Potions potions = new Potions();
    private final MusicHud musicHud = new MusicHud();
    private final Notify notifyHud = new Notify();
    private final TargetHud targetHud = new TargetHud();
    private final CooldownsHud cooldownsHud = new CooldownsHud();

    public InterFace() {
        notifications.lockX = true;
    }

    @Override
    protected void onDisable() {
        musicHud.shutdown();
    }

    @EventHandler
    public void onUpdate(EventUpdate event) {
        if (mc.player == null || mc.world == null || !isEnabled()) return;
        if (element.getValue("Music Player")) musicHud.onTick();
        if (element.getValue("Notifications")) {
            notifyHud.onTick(notifyModules, notifyArmor, notifyEffects);
        }
        if (element.getValue("Cooldowns")) cooldownsHud.onTick(event);
    }

    @EventHandler
    public void onAttack(AttackEvent event) {
        if (!isEnabled()) return;
        if (element.getValue("Cooldowns")) cooldownsHud.onAttack(event.getTarget());
    }

    @EventHandler
    public void onMousePress(MousePressEvent event) {
        if (!isEnabled()) return;
        if (element.getValue("Music Player")) musicHud.onMouseClick(event);
    }

    @EventHandler
    public void onDisplayEvent(EventDisplay eventDisplay) {
        if (mc.player == null || mc.world == null || !isEnabled()) return;

        if (element.getValue("Potions")) potions.onRender(potion, this, eventDisplay);
        if (element.getValue("Information")) informationElemnt.onRender(information, this);
        if (element.getValue("Key Binds")) keyBinds.onRender(keyBind, this);
        if (element.getValue("Water mark")) waterMarkElemnt.onRender(waterMark, this);
        if (element.getValue("Music Player")) musicHud.onRender(music, this);
        if (element.getValue("Notifications")) notifyHud.onRender(notifications, this, eventDisplay);
        if (element.getValue("Отображение таргета")) targetHud.onRender(target, this, eventDisplay);
        if (element.getValue("Cooldowns")) cooldownsHud.onRender(cooldowns, this);
    }
}
