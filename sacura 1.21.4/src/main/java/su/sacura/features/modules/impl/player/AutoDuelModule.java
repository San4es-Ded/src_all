package su.sacura.features.modules.impl.player;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import su.sacura.events.packet.EventPacket;
import su.sacura.events.tick.EventUpdate;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.settings.impl.BooleanSetting;
import su.sacura.features.modules.settings.impl.ModeSetting;
import su.sacura.features.modules.settings.impl.SliderSetting;
import su.sacura.features.modules.settings.impl.StringSetting;

@ModuleAnnotations(name="Auto Duel", category=Category.PLAYER)
public class AutoDuelModule
        extends Module {
    Pattern pattern = Pattern.compile("^\\w{3,16}$");
    ModeSetting mode = new ModeSetting("Режим дуэли", "Шары", "Шары", "Щит", "Шипы 3", "Незеритка", "Читерский рай", "Лук", "Классик", "Тотемы", "Нодебафф").setDescription("Выберите режим на котором будете играть");
    SliderSetting slowTime = new SliderSetting("Скорость отправки", 500.0f, 300.0f, 1000.0f, 100.0f).setDescription("Скорость с которой будет отправляться дуэль");
    BooleanSetting babki = new BooleanSetting("Играть на деньги", false).setDescription("Кидает запросы на дуель за деньги");
    StringSetting money = new StringSetting("Монет", "10000").setDescription("Напишите свою цену за дуэль").setVisible(() -> (Boolean)this.babki.get());
    double lastPosX;
    double lastPosY;
    double lastPosZ;
    List<String> sent = Lists.newArrayList();
    Counter counter = Counter.create();
    Counter counter2 = Counter.create();
    Counter counterChoice = Counter.create();
    Counter counterTo = Counter.create();

    public AutoDuelModule() {
        this.addSettings(this.mode, this.slowTime, this.babki, this.money);
    }

    @Subscribe
    public void onEvent(EventUpdate event) {
        this.xuesos();
    }

    private void xuesos() {
        // Исправлено: удалено объявление String player2; так как оно конфликтует с циклом for
        List<String> players = this.getOnlinePlayers();
        double distance = Math.sqrt(Math.pow(this.lastPosX - AutoDuelModule.mc.player.getX(), 2.0) + Math.pow(this.lastPosY - AutoDuelModule.mc.player.getY(), 2.0) + Math.pow(this.lastPosZ - AutoDuelModule.mc.player.getZ(), 2.0));
        if (distance > 500.0) {
            this.toggle();
        }
        this.lastPosX = AutoDuelModule.mc.player.getX();
        this.lastPosY = AutoDuelModule.mc.player.getY();
        this.lastPosZ = AutoDuelModule.mc.player.getZ();
        if (this.counter2.hasReached(800L * (long)players.size())) {
            this.sent.clear();
            this.counter2.reset();
        }
        // Исправлено: переменная цикла переименована в playerName, чтобы не конфликтовать с внешней переменной
        for (String playerName : players) {
            // Исправлено: getName().getName() -> getName().getString()
            if (this.sent.contains(playerName) || playerName.equals(AutoDuelModule.mc.player.getName().getString()) || !this.counter.hasReached(((Float)this.slowTime.get()).longValue())) continue;
            if (((Boolean)this.babki.get()).booleanValue()) {
                AutoDuelModule.mc.player.networkHandler.sendCommand("duel " + playerName + " " + (String)this.money.get());
            } else {
                AutoDuelModule.mc.player.networkHandler.sendCommand("duel " + playerName);
            }
            this.sent.add(playerName);
            this.counter.reset();
        }
        // Исправлено: проверка типа ScreenHandler вынесена в отдельную переменную handler
        if (AutoDuelModule.mc.currentScreen != null && AutoDuelModule.mc.player.currentScreenHandler != null) {
            ScreenHandler handler = AutoDuelModule.mc.player.currentScreenHandler;
            String title = AutoDuelModule.mc.currentScreen.getTitle().getString();
            if (title.contains("Выбор набора (1/1)")) {
                int slotID = -1;
                if (this.counterChoice.hasReached(150L)) {
                    if (this.mode.is("Щит")) {
                        slotID = 0;
                    }
                    if (this.mode.is("Шипы 3")) {
                        slotID = 1;
                    }
                    if (this.mode.is("Лук")) {
                        slotID = 2;
                    }
                    if (this.mode.is("Тотемы")) {
                        slotID = 3;
                    }
                    if (this.mode.is("Нодебафф")) {
                        slotID = 4;
                    }
                    if (this.mode.is("Шары")) {
                        slotID = 5;
                    }
                    if (this.mode.is("Классик")) {
                        slotID = 6;
                    }
                    if (this.mode.is("Читерский рай")) {
                        slotID = 7;
                    }
                    if (this.mode.is("Незеритка")) {
                        slotID = 8;
                    }
                    if (slotID >= 0) {
                        AutoDuelModule.mc.interactionManager.clickSlot(handler.syncId, slotID, 0, SlotActionType.QUICK_MOVE, (PlayerEntity)AutoDuelModule.mc.player);
                    }
                    this.counterChoice.reset();
                }
            } else if (title.contains("Настройка поединка") && this.counterTo.hasReached(150L)) {
                // Исправлено: используется handler вместо String chest
                AutoDuelModule.mc.interactionManager.clickSlot(handler.syncId, 0, 0, SlotActionType.QUICK_MOVE, (PlayerEntity)AutoDuelModule.mc.player);
                this.counterTo.reset();
            }
        }
    }

    @Subscribe
    private void pidor(EventPacket event) {
        GameMessageS2CPacket chat;
        String text;
        Packet packet;
        if (event.isReceivePacket() && (packet = event.getPacket()) instanceof GameMessageS2CPacket && ((text = (chat = (GameMessageS2CPacket)packet).content().toString()).contains("начало") && text.contains("через") && text.contains("секунд!") || text.equals("дуэли » во время поединка запрещено использовать команды"))) {
            this.toggle();
        }
    }

    private List<String> getOnlinePlayers() {
        List<String> result = AutoDuelModule.mc.player.networkHandler.getPlayerList().stream().map(PlayerListEntry::getProfile).map(GameProfile::getName).filter(profileName -> this.pattern.matcher((CharSequence)profileName).matches()).collect(Collectors.toList());
        return result;
    }

    public static class Counter {
        private long lastMS;

        private Counter() {
            this.reset();
        }

        public static Counter create() {
            return new Counter();
        }

        public void reset() {
            this.lastMS = System.currentTimeMillis();
        }

        public long elapsedTime() {
            return System.currentTimeMillis() - this.lastMS;
        }

        public boolean hasReached(long time) {
            return this.elapsedTime() >= time;
        }
    }
}