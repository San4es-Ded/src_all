package haron.gui.config;

import haron.config.ConfigProfileEntry;
import haron.config.RemoteProfileSummary;
import haron.config.RemoteConfigRepository;
import haron.config.ClientConfigCoordinator;
import haron.config.HaronConfigSnapshot;
import haron.core.BooleanCoercion;
import haron.gui.config.ImportConfigDialog;
import haron.gui.config.ShareConfigDialog;
import haron.gui.config.ConfigAction;
import haron.gui.config.ConfigActionMenu;
import haron.gui.config.ConfigCard;
import haron.gui.config.ConfigRenameListener;
import haron.gui.config.ShareKeysDialog;
import haron.gui.config.CreateConfigDialog;
import haron.gui.core.GuiInput;
import haron.gui.core.ScrollFadeOverlay;
import haron.gui.widgets.ScrollBar;
import haron.gui.widgets.SearchBox;
import haron.media.chat.w53bpe;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.client.util.math.MatrixStack;

public class ConfigListPanel {
    private static final float c = 6.0f;
    private static final float d = -5.0f;
    private static final float e = 4.0f;
    private static final float f = 2.0f;
    private final ScrollBar i;
    private final ScrollFadeOverlay j;
    private final SearchBox k;
    private final ConfigActionMenu l;
    private final ImportConfigDialog m;
    private final ShareConfigDialog n;
    private final ShareKeysDialog o;
    private final CreateConfigDialog createDialog = new CreateConfigDialog();
    private Consumer<ConfigProfileEntry> r;
    private Consumer<ConfigProfileEntry> s;
    private ConfigRenameListener t;
    private Consumer<ConfigProfileEntry> u;
    private Consumer<String> createCallback;
    private float v;
    private float w;
    private float x;
    private float y;
    private ConfigProfileEntry z;
    public static int a;
    public static boolean b;
    private final List<ConfigProfileEntry> g = new ArrayList<ConfigProfileEntry>();
    private final List<ConfigCard> h = new ArrayList<ConfigCard>();
    private String p = "";
    private List<ConfigCard> q = new ArrayList<ConfigCard>();

    public void openCreate() {
        int n = 746;
        this.createDialog.a();
    }

    public void setCreateCallback(Consumer<String> consumer) {
        this.createCallback = consumer;
    }

    public ConfigListPanel(SearchBox kjs1oo2) {
        this.k = kjs1oo2;
        this.k.a(this::d);
        this.i = new ScrollBar(2.0f, 20.0f);
        this.i.b(10.0f);
        this.i.a(pryrvd.b);
        this.i.b(pryrvd.d);
        this.j = new ScrollFadeOverlay(25, 5.0f, 9.0f);
        this.l = new ConfigActionMenu();
        this.m = new ImportConfigDialog();
        this.n = new ShareConfigDialog();
        this.o = new ShareKeysDialog();
        this.i();
    }

    public boolean e() {
        return BooleanCoercion.from(this.l.b() || this.m.c() || this.createDialog.c() || this.n.b() || this.o.b() ? 1 : 0);
    }

    private void i() {
        this.l.a((ConfigAction gpg0qq2) -> {
            this.a((ConfigAction)((Object)gpg0qq2));
        });
        this.l.a(() -> {
            this.z = null;
        });
        this.createDialog.a((String string) -> {
            if (this.createCallback != null) {
                this.createCallback.accept((String)string);
            }
        });
        this.m.a((String string2) -> {
            ClientConfigCoordinator tooa2u2 = ClientConfigCoordinator.a();
            if (tooa2u2.i()) {
                tooa2u2.c((String)string2, () -> {
                    w53bpe.a((Object)"Конфиг импортирован!");
                    this.j();
                }, string -> {
                    w53bpe.a((Object)this.a((String)string));
                });
            } else {
                w53bpe.a((Object)"Конфиги ещё загружаются...");
            }
        });
        this.n.a((Integer n, Integer n2) -> {
            ClientConfigCoordinator tooa2u2 = ClientConfigCoordinator.a();
            if (!tooa2u2.i()) {
                w53bpe.a((Object)"Конфиги ещё загружаются...");
                return;
            }
            ConfigProfileEntry lhvtx72 = this.n.d();
            if (lhvtx72 != null) {
                tooa2u2.a(lhvtx72.a(), (int)n, null, (Integer)n2, string -> {
                    this.o.a((String)string, lhvtx72);
                }, string -> {
                    w53bpe.a((Object)this.b((String)string));
                });
            }
        });
    }

    public void b(ConfigProfileEntry lhvtx72) {
        int n = this.g.indexOf(lhvtx72);
        if (n >= 0) {
            this.g.remove(n);
            this.h.remove(n);
            this.k();
            float f = this.y - -5.0f - 4.0f;
            float f2 = this.l();
            if (f2 > f) {
                this.i.b(f2, f);
            } else {
                this.i.e();
            }
            if (this.s != null) {
                this.s.accept(lhvtx72);
            }
        }
    }

    public void b() {
        this.g.clear();
        this.h.clear();
        this.q.clear();
        this.i.e();
    }

    private String b(String string) {
        if (string == null) {
            return "Неизвестная ошибка";
        }
        int n = -1;
        switch (string.hashCode()) {
            case -2026653947: {
                break;
            }
            case -1926714738: {
                break;
            }
            case -1557786393: {
                break;
            }
            case -1038596745: {
                break;
            }
            case -532953636: {
                break;
            }
            case -347113380: {
                break;
            }
            case -288711231: {
                break;
            }
            case 782165529: {
                break;
            }
            case 1266212642: {
                break;
            }
        }
        return ConfigListPanel.$sf$1(string);
    }

    public void b(Consumer<ConfigProfileEntry> consumer) {
        this.s = consumer;
    }

    public void c(Consumer<ConfigProfileEntry> consumer) {
        this.u = consumer;
    }

    public void c() {
        for (int i = this.g.size() - 1; i >= 0; --i) {
            if (!this.g.get(i).f()) continue;
            this.g.remove(i);
            this.h.remove(i);
        }
        this.k();
    }

    private void c(ConfigProfileEntry lhvtx72) {
        if (lhvtx72.f()) {
            return;
        }
        Iterator<ConfigProfileEntry> iterator = this.g.iterator();
        while (iterator.hasNext()) {
            iterator.next().a(false);
        }
        lhvtx72.a(true);
        if (this.r != null) {
            this.r.accept(lhvtx72);
        }
    }

    private String c(String string) {
        if (string == null) {
            return "Не удалось применить премиум конфиг.";
        }
        int n = -1;
        switch (string.hashCode()) {
            case -1557786393: {
                break;
            }
            case -1110739040: {
                break;
            }
            case -916893481: {
                break;
            }
            case -532953636: {
                break;
            }
            case 280577614: {
                break;
            }
        }
        return string.contains("timeout") || string.contains("Timeout") ? "Превышено время ожидания. Проверьте интернет-подключение." : ConfigListPanel.$sf$2(string);
    }

    public void h() {
        if (this.l.b()) {
            this.l.a();
        }
        if (this.m.c()) {
            this.m.b();
        }
        if (this.createDialog.c()) {
            this.createDialog.b();
        }
        if (this.n.b()) {
            this.n.a();
        }
        if (this.o.b()) {
            this.o.a();
        }
        for (ConfigCard jd20vn2 : this.h) {
            if (jd20vn2.d()) {
                jd20vn2.a(false);
                continue;
            }
            if (!b) continue;
        }
    }

    public boolean f() {
        return this.g.isEmpty();
    }

    private float l() {
        if (this.q.isEmpty()) {
            return 0.0f;
        }
        return (float)this.q.size() * 42.5f - 6.0f;
    }

    private void d(String string) {
        this.p = string.toLowerCase().trim();
        this.k();
        this.i.e();
    }

    public boolean d() {
        return BooleanCoercion.from(this.m.e() || this.createDialog.e() || this.n.e() || this.o.d() || this.m() ? 1 : 0);
    }

    private void d(ConfigProfileEntry lhvtx72) {
        if (lhvtx72.f()) {
            ClientConfigCoordinator tooa2u2 = ClientConfigCoordinator.a();
            if (tooa2u2.i()) {
                tooa2u2.a(lhvtx72.g(), xc39mk2 -> {
                    tooa2u2.a((HaronConfigSnapshot)xc39mk2);
                    tooa2u2.e();
                    w53bpe.a((Object)ConfigListPanel.$sf$3(lhvtx72.a()));
                }, string -> {
                    w53bpe.a((Object)this.c((String)string));
                });
            } else {
                w53bpe.a((Object)"Сервис конфигов ещё не инициализирован.");
            }
        }
    }

    public void a(ConfigProfileEntry lhvtx73) {
        ConfigCard jd20vn2 = new ConfigCard(lhvtx73);
        if (lhvtx73.f()) {
            jd20vn2.c(this::d);
            this.g.add(lhvtx73);
            this.h.add(jd20vn2);
        } else {
            jd20vn2.a((ConfigProfileEntry lhvtx72, Float[] floatArray) -> {
                this.z = lhvtx72;
                this.l.a(floatArray[0].floatValue(), floatArray[1].floatValue(), floatArray[2].floatValue(), (ConfigProfileEntry)lhvtx72);
            });
            jd20vn2.b(this::c);
            jd20vn2.b((String string, String string2) -> {
                if (this.g.stream().anyMatch(candidate -> BooleanCoercion.from(candidate != lhvtx73 && candidate.a().equalsIgnoreCase(string2) ? 1 : 0))) {
                    lhvtx73.a((String)string);
                } else if (this.t != null) {
                    this.t.onRenamed(lhvtx73, (String)string, (String)string2);
                }
            });
            int n = 0;
            for (int i = 0; i < this.g.size(); ++i) {
                if (this.g.get(i).f()) {
                    n = i;
                    break;
                }
                n = i - -2 - 1;
            }
            this.g.add(n, lhvtx73);
            this.h.add(n, jd20vn2);
        }
        this.k();
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, int n2, float f5) {
        boolean bl;
        this.v = f;
        this.w = f2;
        this.x = f3;
        this.y = f4;
        this.i.a();
        if (this.q.isEmpty()) {
            this.a(matrixStack, s7swsm2, f, f2, f3, f4);
            return;
        }
        float f6 = f2 + -5.0f;
        float f7 = f4 - -5.0f - 4.0f;
        float f8 = this.l();
        boolean bl2 = bl = f8 > f7;
        if (!bl) {
            this.i.e();
        }
        s7swsm2.b().a(f, f2 + 4.0f - 6.0f, f3, f4 - 8.0f + 6.0f, matrixStack);
        float f9 = f6 - this.i.b();
        for (ConfigCard jd20vn2 : this.h) {
            float f10 = f9 + 4.0f;
            if (f10 + 36.5f >= f6 && f10 <= f6 + f7) {
                jd20vn2.a(matrixStack, s7swsm2, f + 1.0f, f10, f3 - 2.0f, n, n2, f5);
            }
            f9 += 42.5f;
        }
        s7swsm2.b().a(matrixStack);
        if (bl) {
            this.i.a(matrixStack, s7swsm2, f + f3 + 18.0f, f6, f7, f8, f7, n, n2, false);
        } else if (b) {
            // empty if block
        }
        this.j.a(matrixStack, s7swsm2, f, f2 + 1.5f, f3, f4, f5);
    }

    public void a(int n, int n2) {
        this.i.d();
        if (this.o.b()) {
            this.o.a(n, n2);
        }
    }

    public void a(int n, int n2, double d, double d2) {
        if (this.o.b()) {
            this.o.a(n, n2, d, d2);
        } else if (this.i.c()) {
            float f = this.y - -5.0f - 4.0f;
            this.i.a(n2, this.l(), f);
        }
    }

    public void a(float f, int n, int n2) {
        if (this.o.b()) {
            this.o.a(f, n, n2);
            return;
        }
        if (this.l.b()) {
            return;
        }
        if (this.m.c() || this.n.b() || this.m() || !GuiInput.a(this.v, this.w, this.x, this.y, (double)n, (double)n2)) {
            return;
        }
        this.i.a(f, this.l(), this.y - -5.0f - 4.0f);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        this.l.a(matrixStack, s7swsm2, n, n2);
        this.m.a(matrixStack, s7swsm2, f, f2, n, n2);
        this.createDialog.a(matrixStack, s7swsm2, f, f2, n, n2);
        this.n.a(matrixStack, s7swsm2, f, f2, n, n2);
        this.o.a(matrixStack, s7swsm2, f, f2, n, n2);
    }

    private String a(String string) {
        if (string == null) {
            return "Неизвестная ошибка";
        }
        switch (string) {
            case "KEY_NOT_FOUND": {
                return "Ключ не найден. Проверьте правильность ввода.";
            }
            case "KEY_EXPIRED": {
                return "Срок действия ключа истёк.";
            }
            case "KEY_EXHAUSTED": {
                return "Ключ уже использован максимальное количество раз.";
            }
            case "key is required": {
                return "Введите ключ для активации.";
            }
            case "Internal error": {
                return "Внутренняя ошибка сервера. Попробуйте позже.";
            }
            case "Not connected": {
                return "Нет подключения к серверу.";
            }
        }
        return ConfigListPanel.$sf$0(string);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4) {
        FontRenderer v6hnga2 = ClientFonts.b[14];
        String string = !this.g.isEmpty() ? "Ничего не найдено" : "У тебя пока-что нету конфигов :(";
        v6hnga2.a(string, f + (f3 - v6hnga2.a(string)) / 2.0f, (double)(f2 + f4 / 2.0f - v6hnga2.b(string) / 2.0f), pryrvd.b, matrixStack);
    }

    private void a(ConfigAction gpg0qq2) {
        if (this.z != null) {
            boolean bl = this.z.e();
            block0 : switch (gpg0qq2) {
                case SAVE_TO: {
                    if (!this.z.f()) break;
                    this.d(this.z);
                    break;
                }
                case SHARE: {
                    this.n.a(this.z);
                    break;
                }
                case RENAME: {
                    if (bl) break;
                    for (ConfigCard jd20vn2 : this.h) {
                        if (jd20vn2.a() != this.z) continue;
                        jd20vn2.b();
                        break block0;
                    }
                    break;
                }
                case DELETE: {
                    if (bl) break;
                    this.b(this.z);
                }
            }
        }
    }

    public void a(Consumer<ConfigProfileEntry> consumer) {
        this.r = consumer;
    }

    public void a(ConfigRenameListener p0wmbj2) {
        this.t = p0wmbj2;
    }

    public boolean a(float f, float f2, float f3, float f4, int n, int n2) {
        if (this.o.b()) {
            return this.o.a(this.x + 100.0f, this.y + 100.0f, n, n2);
        }
        if (this.m.c()) {
            return this.m.a(this.x + 100.0f, this.y + 100.0f, n, n2);
        }
        if (this.createDialog.c()) {
            return this.createDialog.a(this.x + 100.0f, this.y + 100.0f, n, n2);
        }
        if (this.n.b()) {
            return this.n.a(this.x + 100.0f, this.y + 100.0f, n, n2);
        }
        if (this.l.b() && this.l.a(n, n2)) {
            return true;
        }
        float f5 = f2 + -5.0f;
        float f6 = f4 - -5.0f - 4.0f;
        float f7 = this.l();
        if (f7 > f6 && this.i.a(f + f3 + 18.0f, f5, f6, f7, f6, n, n2)) {
            return true;
        }
        if (GuiInput.a(f, f2, f3, f4, (double)n, (double)n2)) {
            float f8 = f5 - this.i.b();
            Iterator<ConfigCard> iterator = this.h.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().a(f + 1.0f, f8 + 4.0f, f3 - 2.0f, n, n2)) {
                    return true;
                }
                f8 += 42.5f;
            }
        }
        return false;
    }

    public List<ConfigProfileEntry> a() {
        int n = 783;
        return this.g;
    }

    public boolean a(char c, int n) {
        for (ConfigCard jd20vn2 : this.h) {
            if (!jd20vn2.d()) continue;
            return jd20vn2.a(c, n);
        }
        if (this.o.b()) {
            return this.o.a(c, n);
        }
        if (this.m.c()) {
            return this.m.a(c, n);
        }
        if (this.createDialog.c()) {
            return this.createDialog.a(c, n);
        }
        if (this.n.b()) {
            return this.n.a(c, n);
        }
        return false;
    }

    public boolean a(int n, int n2, int n3) {
        for (ConfigCard jd20vn2 : this.h) {
            if (!jd20vn2.d()) continue;
            return jd20vn2.a(n, n2, n3);
        }
        if (this.o.b()) {
            return this.o.a(n, n2, n3);
        }
        if (this.m.c()) {
            return this.m.a(n, n2, n3);
        }
        if (this.createDialog.c()) {
            return this.createDialog.a(n, n2, n3);
        }
        if (this.n.b()) {
            return this.n.a(n, n2, n3);
        }
        return false;
    }

    private boolean m() {
        Iterator<ConfigCard> iterator = this.h.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().d()) continue;
            return true;
        }
        return false;
    }

    private void k() {
        this.q = this.p.isEmpty() ? new ArrayList<ConfigCard>(this.h) : this.h.stream().filter(jd20vn2 -> {
            String string = jd20vn2.a().b();
            return BooleanCoercion.from(jd20vn2.a().a().toLowerCase().contains(this.p) || string != null && string.toLowerCase().contains(this.p) ? 1 : 0);
        }).collect(Collectors.toList());
    }

    public void g() {
        this.m.a();
    }

    private void j() {
        ClientConfigCoordinator tooa2u2 = ClientConfigCoordinator.a();
        if (tooa2u2.i()) {
            this.b();
            RemoteConfigRepository rdqdh62 = tooa2u2.j();
            if (rdqdh62 != null) {
                String string = tooa2u2.g();
                for (RemoteProfileSummary ozellm2 : rdqdh62.f()) {
                    ConfigProfileEntry lhvtx72 = new ConfigProfileEntry(ozellm2.b, ozellm2.c, LocalDateTime.ofInstant(Instant.ofEpochMilli(ozellm2.g), ZoneId.systemDefault()));
                    lhvtx72.a(ozellm2.b.equals(string));
                    this.a(lhvtx72);
                }
            }
        }
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "Ошибка активации: " + string;
    }

    private static /* synthetic */ String $sf$3(String string) {
        return "Премиум конфиг \"" + string + "\" применён к активному конфигу.";
    }

    private static /* synthetic */ String $sf$1(String string) {
        return "Ошибка создания ключа: " + string;
    }

    private static /* synthetic */ String $sf$2(String string) {
        return "Не удалось применить премиум конфиг: " + string;
    }
}
