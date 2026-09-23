package haron.modules.utilities;

import haron.events.PacketEvent;
import haron.events.WorldChangedEvent;
import haron.events.ClientTickEvent;
import haron.gui.friends.FriendUtils;
import haron.media.chat.w53bpe;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.ModeSetting;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import ru.haron.mixin.accessor.PlayerListHudAccessor;

@ModuleInfo(a="Auto Leave", b="Автоматический выход при обнаружении игроков", c=ModuleCategory.UTILITIES)
public class AutoLeave
extends HaronModule {
    private final ModeSetting e = new ModeSetting("Режим отключения", new String[]{"Выход с сервера", "/hub"}, "Выход с сервера");
    private final Map<Integer, Long> f = new HashMap<Integer, Long>();
    private boolean g = false;
    private boolean h = false;
    private boolean i = false;
    private int j = -1;
    private long k = 0L;
    private long l = 0L;
    private final Pattern m = Pattern.compile("\\((.*?)\\)\\s+.*?\\s+(\\S+)\\s+[—–-]\\s+(\\d+\\.\\d+)\\s+блоков");
    private final Pattern n = Pattern.compile(".*Команда будет доступна через\\s+(\\d+)\\s+мин\\.?\\s*(\\d+)?\\s*сек\\.?");
    private final Pattern o = Pattern.compile("Вокруг вас никого нет");
    private final Pattern p = Pattern.compile("Повторите текст еще раз\\.");
    public static int a;
    public static boolean b;

    @Override
    public void e() {
        super.e();
        this.f.clear();
        this.g = false;
        this.h = false;
        this.i = false;
        this.j = -1;
        this.k = 0L;
        this.l = 0L;
        if (this.o()) {
            this.h = true;
            this.k = System.currentTimeMillis();
        }
    }

    public int n() {
        try {
            if (AutoLeave.c.inGameHud == null || AutoLeave.c.inGameHud.getPlayerListHud() == null) {
                return -1;
            }
            Text text = ((PlayerListHudAccessor)AutoLeave.c.inGameHud.getPlayerListHud()).getHeader();
            if (text == null) {
                return -1;
            }
            String string = Formatting.strip((String)text.getString());
            if (string == null || !string.contains("Анархия-")) {
                return -1;
            }
            String[] stringArray = string.split("Анархия-");
            if (stringArray.length < 2) {
                return -1;
            }
            try {
                return Integer.parseInt(stringArray[1].trim());
            }
            catch (NumberFormatException numberFormatException) {
                return -1;
            }
        }
        catch (Exception exception) {
            return -1;
        }
    }

    @Override
    public void f() {
        super.f();
        this.f.clear();
        this.g = false;
        this.h = false;
        this.i = false;
        this.l = 0L;
    }

    private void a(String string) {
        Matcher matcher = this.m.matcher(string);
        if (matcher.find()) {
            String string2 = matcher.group(2);
            if (FriendUtils.a(string2)) {
                this.g = false;
                this.i = false;
                return;
            }
            this.g = false;
            this.i = false;
            String string3 = AutoLeave.$sf$0(string2, matcher.group(3));
            if (AutoLeave.c.player != null) {
                if (this.e.b("Выход с сервера")) {
                    if (c.getNetworkHandler() != null) {
                        c.getNetworkHandler().getConnection().disconnect(Text.of((String)string3));
                    }
                } else if (this.e.b("/hub")) {
                    w53bpe.a("hub");
                    w53bpe.a((Object)string3);
                }
            }
        }
    }

    /*
     * Handled unverifiable bytecode (illegal stack merge).
     */
    private void a(Matcher matcher) {
        this.g = false;
        this.i = false;
        this.f.put(this.j, System.currentTimeMillis() + ((long)Integer.parseInt(matcher.group(1)) * 60L + (matcher.group(2) != null ? (long)Integer.parseInt(matcher.group(2)) : 0L)) * 1000L + 2000L);
    }

    @EventHandler
    private void a(ClientTickEvent q8krcw2) {
        if (this.o()) {
            Long l;
            long l2 = System.currentTimeMillis();
            int n = this.n();
            if (n != -1 && n != this.j) {
                this.j = n;
                this.g = false;
                this.i = false;
            }
            if (l2 - this.l > 1000L) {
                this.l = l2;
                this.p();
            }
            if (this.h) {
                if (this.j != -1) {
                    this.h = false;
                    l = this.f.get(this.j);
                    if (l == null || l2 >= l) {
                        this.q();
                        this.i = true;
                    }
                } else if (l2 - this.k > 5000L) {
                    this.h = false;
                }
            }
            if (this.h || this.i || this.j == -1) {
                return;
            }
            l = this.f.get(this.j);
            if (l == null || l2 >= l) {
                this.q();
                this.i = true;
            }
        }
    }

    @EventHandler
    public void a(PacketEvent g07m232) {
        if (this.o() && g07m232.d() instanceof GameMessageS2CPacket) {
            Matcher matcher;
            GameMessageS2CPacket gameMessageS2CPacket = (GameMessageS2CPacket)g07m232.d();
            String string = Formatting.strip((String)gameMessageS2CPacket.content().getString());
            if (string.contains("[✇] Радар ›")) {
                this.g = true;
            }
            if ((matcher = this.n.matcher(string)).find()) {
                this.a(matcher);
                g07m232.b();
                return;
            }
            if (this.g) {
                if (this.o.matcher(string).find()) {
                    this.g = false;
                    this.i = false;
                } else if (this.m.matcher(string).find()) {
                    this.a(string);
                } else if (this.p.matcher(string).find()) {
                    g07m232.b();
                    this.q();
                }
            }
        }
    }

    @EventHandler
    private void a(WorldChangedEvent m7z9q12) {
        if (this.o()) {
            this.g = false;
            this.h = true;
            this.i = false;
            this.k = System.currentTimeMillis();
        }
    }

    public boolean o() {
        if (AutoLeave.c.world != null) {
            if (AutoLeave.c.world.getRegistryKey().getValue().toString().equals("minecraft:overworld")) {
                if (!c.isInSingleplayer()) {
                    return true;
                }
            } else if (b) {
                // empty if block
            }
        }
        return false;
    }

    private void p() {
        long l = System.currentTimeMillis();
        Iterator<Map.Entry<Integer, Long>> iterator = this.f.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Long> entry = iterator.next();
            int n = entry.getKey();
            if (l < entry.getValue()) continue;
            iterator.remove();
            if (n != this.j || this.i) continue;
            if (!this.g) {
                this.q();
                this.i = true;
                continue;
            }
            if (!b) continue;
            throw new IllegalAccessError();
        }
    }

    private void q() {
        int n = 450;
        if (AutoLeave.c.player != null) {
            w53bpe.a("near max");
            this.g = true;
        }
    }

    private static /* synthetic */ String $sf$0(String string, String string2) {
        return "Обнаружен игрок " + string + " на расстоянии " + string2 + " блоков";
    }
}

