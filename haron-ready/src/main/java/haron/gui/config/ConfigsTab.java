package haron.gui.config;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.client.MinecraftClientAccess;
import haron.config.ConfigProfileEntry;
import haron.config.LocalConfigManager;
import haron.core.BooleanCoercion;
import haron.gui.config.ConfigRenameListener;
import haron.gui.config.ConfigListPanel;
import haron.gui.core.CategorySelectionModel;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.gui.core.CategorySelectorOverlay;
import haron.gui.core.ScrollFadeOverlay;
import haron.gui.core.ClickGuiTab;
import haron.gui.core.ClickGuiScreen;
import haron.gui.core.ClickGuiTabType;
import haron.gui.widgets.SearchBox;
import haron.media.chat.w53bpe;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import java.lang.reflect.Method;
import net.minecraft.client.util.math.MatrixStack;

public class ConfigsTab
implements CategorySelectionModel,
ClickGuiTab {
    private static final String[] a = new String[]{"Конфигурации"};
    private static final float b = 19.0f;
    private static final float c = 48.0f;
    private static final float d = 117.5f;
    private static final float e = 15.0f;
    private static final float f = 8.0f;
    private static final float g = 8.0f;
    private int i;
    private int j;
    private int h = 0;
    private final AnimatedValue o = new AnimatedValue();
    private final AnimatedValue p = new AnimatedValue();
    private final AnimatedValue q = new AnimatedValue();
    private final AnimatedValue backdrop = new AnimatedValue();
    private boolean r = false;
    private boolean s = false;
    private boolean t = false;
    private final CategorySelectorOverlay k = new CategorySelectorOverlay(this);
    private final SearchBox l = new SearchBox(117.5f, 15.0f);
    private final ConfigListPanel m = new ConfigListPanel(this.l);
    private final ScrollFadeOverlay n = new ScrollFadeOverlay(25, 10.0f, 7.5f);

    private static ConfigRenameListener proxy_$3(ConfigsTab zpnv5n2) {
        return (lhvtx72, string2, string3) -> {
            LocalConfigManager.get().renameProfile(lhvtx72.a(), string2, () -> {
                lhvtx72.a(string2);
            }, string -> {
                w53bpe.a(ConfigsTab.$sf$4(string));
            });
        };
    }

    public ConfigsTab() {
        this.j();
    }

    public void e() {
        int n = 69;
        this.h();
    }

    private void i() {
    }

    @Override
    public boolean b() {
        return BooleanCoercion.from(this.l.c() || this.m.d() ? 1 : 0);
    }

    private String b(String string) {
        if (string == null) {
            return "Не удалось удалить конфиг.";
        }
        switch (string) {
            case "Not connected": {
                return "Нет соединения с сервером. Проверьте интернет-подключение.";
            }
            case "Not initialized": {
                return "Сервис конфигов ещё не инициализирован. Попробуйте позже.";
            }
            case "Cannot delete active config": {
                return "Нельзя удалить активный конфиг. Сначала переключитесь на другой.";
            }
            case "CONFIG_NOT_FOUND": {
                return "Конфиг не найден. Возможно, он уже был удалён.";
            }
            case "No response": {
                return "Сервер не отвечает. Попробуйте позже.";
            }
        }
        return string.contains("timeout") || string.contains("Timeout") ? "Превышено время ожидания. Проверьте интернет-подключение." : ConfigsTab.$sf$1(string);
    }

    @Override
    public void b(float f, float f2, int n, int n2) {
        this.a(f, f2, n, n2);
    }

    @Override
    public void c(float f, float f2, int n, int n2) {
        this.m.a(n, n2);
    }

    private String c(String string) {
        if (string == null) {
            return "Не удалось переименовать конфиг.";
        }
        switch (string) {
            case "Not connected": {
                return "Нет соединения с сервером. Проверьте интернет-подключение.";
            }
            case "Not initialized": {
                return "Сервис конфигов ещё не инициализирован. Попробуйте позже.";
            }
            case "Cannot rename active config": {
                return "Нельзя переименовать активный конфиг. Сначала переключитесь на другой.";
            }
            case "NAME_TAKEN": 
            case "Config already exists": {
                return "Конфиг с таким именем уже существует.";
            }
            case "CONFIG_NOT_FOUND": {
                return "Конфиг не найден. Возможно, он был удалён.";
            }
            case "INVALID_NAME": {
                return "Недопустимое имя конфига. Используйте только буквы, цифры и пробелы.";
            }
            case "No response": {
                return "Сервер не отвечает. Попробуйте позже.";
            }
        }
        return string.contains("timeout") || string.contains("Timeout") ? "Превышено время ожидания. Проверьте интернет-подключение." : ConfigsTab.$sf$2(string);
    }

    @Override
    public String[] c() {
        return a;
    }

    private void h() {
        Object object;
        try {
            LocalConfigManager.get();
            object = ConfigListPanel.class.getDeclaredMethod("j", new Class[0]);
            ((Method)object).setAccessible(true);
            ((Method)object).invoke((Object)this.m, new Object[0]);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            object = LocalConfigManager.get();
            for (ConfigProfileEntry lhvtx72 : ((LocalConfigManager)object).toConfigEntries()) {
                lhvtx72.a(lhvtx72.a().equals(((LocalConfigManager)object).activeProfile()));
                this.m.a(lhvtx72);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public boolean f() {
        return this.l.c();
    }

    private void l() {
        this.m.h();
    }

    @Override
    public int d() {
        return this.h;
    }

    private String d(String string) {
        if (string == null) {
            return "Не удалось создать конфиг.";
        }
        switch (string) {
            case "Not connected": {
                return "Нет соединения с сервером. Проверьте интернет-подключение.";
            }
            case "Not initialized": {
                return "Сервис конфигов ещё не инициализирован. Попробуйте позже.";
            }
            case "Config already exists": {
                return "Конфиг с таким именем уже существует.";
            }
            case "CONFLICT": {
                return "Конфликт версий. Попробуйте ещё раз.";
            }
            case "LIMIT_REACHED": {
                return "Достигнут лимит конфигов. Удалите ненужные конфиги.";
            }
            case "INVALID_NAME": {
                return "Недопустимое имя конфига. Используйте только буквы, цифры и пробелы.";
            }
            case "No response": {
                return "Сервер не отвечает. Попробуйте позже.";
            }
        }
        return string.contains("timeout") || string.contains("Timeout") ? "Превышено время ожидания. Проверьте интернет-подключение." : ConfigsTab.$sf$3(string);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2) {
        this.m.a(matrixStack, s7swsm2, (float)MinecraftClientAccess.d.getWidth() / 2.0f, (float)MinecraftClientAccess.d.getHeight() / 2.0f, this.i, this.j);
    }

    @Override
    public void a(float f, float f2, int n, int n2) {
        if (this.m.e()) {
            this.m.a(f + 19.0f, f2 + 48.0f, ClickGuiScreen.d() - 38.0f, ClickGuiScreen.e() - 48.0f - 9.5f - 8.0f, n, n2);
            return;
        }
        if (this.l.a(n, n2)) {
            return;
        }
        this.k.a(f, f2, n, n2);
        float f3 = ClickGuiScreen.d();
        float f4 = ClickGuiScreen.e();
        float f5 = f + f3 - 19.0f - 117.5f - 8.0f - 8.0f;
        float f6 = f2 + 19.0f + 3.5f - 3.0f;
        if (GuiInput.a(f5 - 16.0f - 16.0f, f6, 8.0f, 8.0f, (double)n, (double)n2)) {
            this.m();
            return;
        }
        if (GuiInput.a(f5 - 8.0f - 8.0f, f6, 8.0f, 8.0f, (double)n, (double)n2)) {
            this.l();
            return;
        }
        if (GuiInput.a(f5, f6, 8.0f, 8.0f, (double)n, (double)n2)) {
            this.k();
            return;
        }
        this.m.a(f + 19.0f, f2 + 48.0f, f3 - 38.0f, f4 - 48.0f - 9.5f - 8.0f, n, n2);
    }

    private void a(AnimatedValue urhlup2, boolean bl, boolean bl2, boolean bl3) {
        if (bl3 && bl2) {
            urhlup2.a(0.0, 0.15, Easings.h);
        } else {
            if (bl3 || bl == bl2) {
                return;
            }
            urhlup2.a(bl ? 1.0 : 0.0, 0.15, Easings.h);
        }
    }

    @Override
    public void a(int n) {
        if (n < 0 || n >= a.length || this.h == n) {
            return;
        }
        this.h = n;
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, String string) {
        String string2;
        Color color = ColorUtils.a(pryrvd.b, pryrvd.d, f4);
        switch (string) {
            case "add": {
                string2 = "";
                break;
            }
            case "save": {
                string2 = "";
                break;
            }
            case "key": {
                string2 = "";
                break;
            }
            default: {
                return;
            }
        }
        ClientFonts.e[14].a(string2, f, (double)f2, color, matrixStack);
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        this.i = n;
        this.j = n2;
        this.k.a(matrixStack, s7swsm2, f, f2, n, n2);
        this.o.a();
        this.p.a();
        this.q.a();
        float f3 = ClickGuiScreen.d();
        float f4 = ClickGuiScreen.e();
        float f5 = f + f3 - 19.0f - 117.5f;
        float f6 = f2 + 19.0f - 3.0f;
        float f7 = f5 - 8.0f - 8.0f;
        float f8 = f2 + 19.0f + 3.5f - 3.0f;
        int n3 = InteractionOverlayController.a().b() || this.m.e() ? 1 : 0;
        int n4 = n3 == 0 && GuiInput.a(f7 - 16.0f - 16.0f, f8, 8.0f, 8.0f, (double)n, (double)n2) ? 1 : 0;
        int n5 = n3 == 0 && GuiInput.a(f7 - 8.0f - 8.0f, f8, 8.0f, 8.0f, (double)n, (double)n2) ? 1 : 0;
        int n6 = n3 == 0 && GuiInput.a(f7, f8, 8.0f, 8.0f, (double)n, (double)n2) ? 1 : 0;
        this.a(this.o, BooleanCoercion.from(n4), this.r, BooleanCoercion.from(n3));
        this.r = BooleanCoercion.from(n3 != 0 || n4 == 0 ? 0 : 1);
        this.a(this.p, BooleanCoercion.from(n5), this.s, BooleanCoercion.from(n3));
        this.s = BooleanCoercion.from(n3 != 0 || n5 == 0 ? 0 : 1);
        this.a(this.q, BooleanCoercion.from(n6), this.t, BooleanCoercion.from(n3));
        this.t = BooleanCoercion.from(n3 != 0 || n6 == 0 ? 0 : 1);
        this.a(matrixStack, s7swsm2, f7 - 16.0f - 16.0f, f8, 8.0f, (float)this.o.j(), "add");
        this.a(matrixStack, s7swsm2, f7 - 8.0f - 8.0f, f8, 8.0f, (float)this.p.j(), "save");
        this.a(matrixStack, s7swsm2, f7, f8, 8.0f, (float)this.q.j(), "key");
        this.l.a(matrixStack, s7swsm2, f5, f6, 117.5f, 15.0f, n, n2);
        this.m.a(matrixStack, s7swsm2, f + 19.0f, f2 + 48.0f, f3 - 38.0f, f4 - 48.0f - 9.5f - 8.0f, n, n2, 1.0f);
        this.n.a(matrixStack, s7swsm2, f, f2, n, n2);
        this.backdrop.a();
        boolean bl = this.m.e();
        if (bl && this.backdrop.i() < 1.0) {
            this.backdrop.a(1.0, 0.25, Easings.h);
        } else if (!bl && this.backdrop.i() > 0.0) {
            this.backdrop.a(0.0, 0.18, Easings.h);
        }
        float f9 = (float)this.backdrop.j();
        if (f9 > 0.01f) {
            float f10 = MinecraftClientAccess.d.getWidth();
            float f11 = MinecraftClientAccess.d.getHeight();
            s7swsm2.a(0.0f, 0.0f, f10, f11, 0.0f, new Color(0, 0, 0, (int)(160.0f * f9)), matrixStack);
        }
        this.m.a(matrixStack, s7swsm2, (float)MinecraftClientAccess.d.getWidth() / 2.0f, (float)MinecraftClientAccess.d.getHeight() / 2.0f, n, n2);
        if (n4 == 0 && n5 == 0 && n6 == 0) {
            return;
        }
        GuiInput.g();
    }

    @Override
    public void a(float f) {
        this.m.a(f, this.i, this.j);
    }

    private String a(String string) {
        if (string == null) {
            return "Не удалось переключить конфиг.";
        }
        switch (string) {
            case "Not connected": {
                return "Нет соединения с сервером. Проверьте интернет-подключение.";
            }
            case "Not initialized": {
                return "Сервис конфигов ещё не инициализирован. Попробуйте позже.";
            }
            case "CONFIG_NOT_FOUND": {
                return "Конфиг не найден. Возможно, он был удалён.";
            }
            case "No response": {
                return "Сервер не отвечает. Попробуйте позже.";
            }
        }
        return string.contains("timeout") || string.contains("Timeout") ? "Превышено время ожидания. Проверьте интернет-подключение." : ConfigsTab.$sf$0(string);
    }

    @Override
    public ClickGuiTabType a() {
        return ClickGuiTabType.CONFIGS;
    }

    public boolean a(char c, int n) {
        if (this.m.d()) {
            return this.m.a(c, n);
        }
        return this.l.c() ? this.l.a(c, n) : this.m.a(c, n);
    }

    @Override
    public boolean a(int n, int n2, int n3) {
        if (this.m.d()) {
            return this.m.a(n, n2, n3);
        }
        return this.l.c() ? this.l.a(n, n2, n3) : this.m.a(n, n2, n3);
    }

    @Override
    public void a(float f, float f2, int n, int n2, double d, double d2) {
        int n3 = 465;
        this.m.a(n, n2, d, d2);
    }

    private void m() {
        this.m.openCreate();
    }

    private void k() {
        this.m.g();
    }

    public boolean g() {
        return this.m.e();
    }

    private void j() {
        this.m.a((ConfigProfileEntry lhvtx72) -> {
            LocalConfigManager.get().loadProfile(lhvtx72.a(), () -> {
                for (ConfigProfileEntry lhvtx73 : this.m.a()) {
                    lhvtx73.a(lhvtx73.a().equals(lhvtx72.a()));
                }
            }, string -> {
                w53bpe.a(ConfigsTab.$sf$8(string));
            });
        });
        this.m.b((ConfigProfileEntry lhvtx72) -> {
            LocalConfigManager.get().deleteProfile(lhvtx72.a(), () -> {
                this.e();
            }, string -> {
                int n = 283;
                w53bpe.a(ConfigsTab.$sf$7(string));
            });
        });
        this.m.a(ConfigsTab.proxy_$3(this));
        this.m.c((ConfigProfileEntry lhvtx72) -> {
            w53bpe.a(this.d(lhvtx72.a()));
        });
        this.m.setCreateCallback(string2 -> {
            if (string2 == null || string2.trim().isEmpty()) {
                w53bpe.a((Object)"Укажите имя конфига.");
                return;
            }
            try {
                LocalConfigManager.get().createProfile((String)string2, () -> {
                    w53bpe.a((Object)ConfigsTab.$sf$6(string2));
                    this.e();
                }, string -> {
                    w53bpe.a((Object)ConfigsTab.$sf$5(string));
                });
            }
            catch (Throwable throwable) {
                w53bpe.a((Object)"Ошибка создания конфига.");
            }
        });
        this.h();
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "Не удалось переключить конфиг: " + string;
    }

    private static /* synthetic */ String $sf$4(String string) {
        return "§cОшибка переименования: " + string;
    }

    private static /* synthetic */ String $sf$3(String string) {
        return "Не удалось создать конфиг: " + string;
    }

    private static /* synthetic */ String $sf$7(String string) {
        int n = 550;
        return "§cОшибка удаления: " + string;
    }

    private static /* synthetic */ String $sf$8(String string) {
        return "§cОшибка загрузки: " + string;
    }

    private static /* synthetic */ String $sf$5(String string) {
        return "Ошибка создания: " + string;
    }

    private static /* synthetic */ String $sf$1(String string) {
        return "Не удалось удалить конфиг: " + string;
    }

    private static /* synthetic */ String $sf$6(String string) {
        return "Конфиг " + string + " создан.";
    }

    private static /* synthetic */ String $sf$2(String string) {
        return "Не удалось переименовать конфиг: " + string;
    }
}

