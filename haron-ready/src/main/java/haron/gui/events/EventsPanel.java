package haron.gui.events;

import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.core.ScrollFadeOverlay;
import haron.gui.events.ServerEventCard;
import haron.gui.events.ServerEventEntry;
import haron.gui.widgets.ScrollBar;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.client.util.math.MatrixStack;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventsPanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsPanel.class);
    private static final float a = 6.0f;
    private static final float b = 3.0f;
    private static final float c = 3.0f;
    private static final float d = 2.0f;
    private final ScrollFadeOverlay l;
    private float o;
    private float p;
    private float q;
    private float r;
    private Consumer<ServerEventEntry> s;
    private final List<ServerEventEntry> e = new ArrayList<ServerEventEntry>();
    private final List<ServerEventEntry> f = new ArrayList<ServerEventEntry>();
    private final List<ServerEventEntry> g = new ArrayList<ServerEventEntry>();
    private final List<ServerEventCard> h = new ArrayList<ServerEventCard>();
    private final List<ServerEventCard> i = new ArrayList<ServerEventCard>();
    private final List<ServerEventCard> j = new ArrayList<ServerEventCard>();
    private int m = 0;
    private String n = "";
    private final ScrollBar k = new ScrollBar(2.0f, 20.0f);

    public EventsPanel() {
        LOGGER.info("EventsPanel initialized");
        this.k.b(10.0f);
        this.k.a(pryrvd.b);
        this.k.b(pryrvd.d);
        this.l = new ScrollFadeOverlay(25, 5.0f, 9.0f);
        LOGGER.debug("EventsPanel components initialized: ScrollBar, PanelFadeOverlay");
    }

    private List<ServerEventCard> e() {
        List<ServerEventCard> list;
        List<ServerEventCard> list2;
        String string = switch (this.m) {
            case 0 -> {
                list2 = this.h;
                yield "Active";
            }
            case 1 -> {
                list2 = this.i;
                yield "Upcoming";
            }
            case 2 -> {
                list2 = this.j;
                yield "Completed";
            }
            default -> {
                list2 = this.h;
                yield "Active (default)";
            }
        };
        List<ServerEventCard> list3 = list2;
        if (this.n.isEmpty()) {
            list = new ArrayList<ServerEventCard>(list3);
            LOGGER.trace("Showing all {} events in tab '{}'", (Object)list.size(), (Object)string);
        } else {
            list = list3.stream().filter(st7tbs2 -> {
                ServerEventEntry tywt852 = st7tbs2.a();
                return BooleanCoercion.from(tywt852.a().toLowerCase().contains(this.n) || String.valueOf(tywt852.b()).contains(this.n) || tywt852.i() != null && tywt852.i().toLowerCase().contains(this.n) || tywt852.d() != null && tywt852.d().a().toLowerCase().contains(this.n) || tywt852.e() != null && tywt852.e().a().toLowerCase().contains(this.n) ? 1 : 0);
            }).collect(Collectors.toList());
            LOGGER.trace("Found {} events matching '{}' in tab '{}'", new Object[]{list.size(), this.n, string});
        }
        return list;
    }

    public void b() {
        LOGGER.debug("Sorting events by priority");
        this.a(this.e, this.h);
        this.a(this.f, this.i);
        this.a(this.g, this.j);
        LOGGER.trace("Events sorted: Active={}, Upcoming={}, Completed={}", new Object[]{this.e.size(), this.f.size(), this.g.size()});
    }

    public void b(ServerEventEntry tywt852) {
        LOGGER.info("Adding upcoming event: {} (ID: {})", (Object)tywt852.a(), (Object)tywt852.b());
        this.f.add(tywt852);
        ServerEventCard st7tbs2 = new ServerEventCard(tywt852);
        st7tbs2.a(this::d);
        this.i.add(st7tbs2);
        LOGGER.trace("Upcoming events count: {}", (Object)this.f.size());
    }

    public boolean c() {
        switch (this.m) {
            case 0: {
                boolean bl = this.e.isEmpty();
                LOGGER.trace("A߸ߏive events empty: {}", (Object)bl);
                return bl;
            }
            case 1: {
                boolean bl = this.f.isEmpty();
                LOGGER.trace("Upcoming events empty: {}", (Object)bl);
                return bl;
            }
            case 2: {
                boolean bl = this.g.isEmpty();
                LOGGER.trace("Completed events empty: {}", (Object)bl);
                return bl;
            }
        }
        LOGGER.trace("Unknown tab, returning trߎe");
        return true;
    }

    public void c(ServerEventEntry tywt852) {
        LOGGER.info("Adding completed event: {} (ID: {})", (Object)tywt852.a(), (Object)tywt852.b());
        this.g.add(tywt852);
        ServerEventCard st7tbs2 = new ServerEventCard(tywt852);
        st7tbs2.a(this::d);
        this.j.add(st7tbs2);
        LOGGER.trace("Completed events count: {}", (Object)this.g.size());
    }

    private void d(ServerEventEntry tywt852) {
        LOGGER.info("Event selected: {} (ID: {})", (Object)tywt852.a(), (Object)tywt852.b());
        if (this.s != null) {
            this.s.accept(tywt852);
        }
    }

    public void d() {
        LOGGER.info("Clearing all event data");
        this.e.clear();
        this.f.clear();
        this.g.clear();
        this.h.clear();
        this.i.clear();
        this.j.clear();
        this.k.e();
        LOGGER.debug("All event lists clearedܻand scrollݻreset");
    }

    public void a(float f, int n, int n2) {
        if (GuiInput.a(this.o, this.p, this.q, this.r, (double)n, (double)n2)) {
            List<ServerEventCard> list = this.e();
            this.k.a(f, this.a(list), this.r - 3.0f - 3.0f);
            LOGGER.trace("Scroll wheel: delta={߆", (Object)Float.valueOf(f));
        }
    }

    public void a(int n, int n2, double d, double d2) {
        if (this.k.c()) {
            List<ServerEventCard> list = this.e();
            float f = this.r - 3.0f - 3.0f;
            this.k.a(n2, this.a(list), f);
            LOGGER.trace("Scroll drag: dx={}, dy={}", (Object)d, (Object)d2);
        }
    }

    public void a(int n, int n2) {
        this.k.d();
        LOGGER.trace("Scroll event at ({}, {})", (Object)n, (Object)n2);
    }

    public boolean a(float f, float f2, float f3, float f4, int n, int n2) {
        List<ServerEventCard> list = this.e();
        if (list.isEmpty()) {
            return false;
        }
        float f5 = f2 + 3.0f;
        float f6 = f4 - 3.0f - 3.0f;
        float f7 = this.a(list);
        if (f7 > f6 && this.k.a(f + f3 + 18.0f, f5, f6, f7, f6, n, n2)) {
            LOGGER.trace("Scroll bar clicked");
            return true;
        }
        if (GuiInput.a(f, f5, f3, f6, (double)n, (double)n2)) {
            float f8 = f5 - this.k.b();
            float f9 = f + 1.0f;
            float f10 = f3 - 2.0f;
            Iterator<ServerEventCard> iterator = list.iterator();
            int n3 = 0;
            while (iterator.hasNext()) {
                if (iterator.next().a(f9, f8, f10, n, n2)) {
                    LOGGER.debug("Event card clicked at index: {}", (Object)n3);
                    return true;
                }
                f8 += 42.5f;
                ++n3;
            }
        }
        return false;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, int n2, float f5) {
        boolean bl;
        this.o = f;
        this.p = f2;
        this.q = f3;
        this.r = f4;
        this.k.a();
        List<ServerEventCard> list = this.e();
        if (list.isEmpty()) {
            this.a(matrixStack, s7swsm2, f, f2, f3, f4);
            return;
        }
        float f6 = f2 + 3.0f;
        float f7 = f4 - 3.0f - 3.0f;
        float f8 = this.a(list);
        boolean bl2 = bl = f8 > f7;
        if (!bl) {
            this.k.e();
        }
        s7swsm2.b().a(f, f2 + 2.0f, f3, f4 - 8.0f, matrixStack);
        float f9 = f6 - this.k.b();
        float f10 = f + 1.0f;
        float f11 = f3 - 2.0f;
        for (ServerEventCard st7tbs2 : list) {
            if (f9 + 36.5f >= f6 && f9 <= f6 + f7) {
                st7tbs2.a(matrixStack, s7swsm2, f10, f9, f11, n, n2, f5);
            }
            f9 += 42.5f;
        }
        s7swsm2.b().a(matrixStack);
        if (bl) {
            this.k.a(matrixStack, s7swsm2, f + f3 + 18.0f, f6, f7, f8, f7, n, n2, false);
        }
        this.l.a(matrixStack, s7swsm2, f, f2 + 1.5f, f3, f4, f5);
    }

    public void a(Consumer<ServerEventEntry> consumer) {
        LOGGER.debug("Setting event consumer");
        this.s = consumer;
    }

    private void a(List<ServerEventEntry> list, List<ServerEventCard> list2) {
        if (list.size() > 1) {
            ArrayList<int[]> arrayList = new ArrayList<int[]>();
            for (int i = 0; i < list.size(); ++i) {
                arrayList.add(new int[]{i, list.get(i).f()});
            }
            arrayList.sort((nArray, nArray2) -> {
                return Integer.compare(nArray[1], nArray2[1]);
            });
            ArrayList<ServerEventEntry> arrayList2 = new ArrayList<ServerEventEntry>();
            ArrayList<ServerEventCard> arrayList3 = new ArrayList<ServerEventCard>();
            for (int[] nArray3 : arrayList) {
                arrayList2.add(list.get(nArray3[0]));
                arrayList3.add(list2.get(nArray3[0]));
            }
            list.clear();
            list.addAll(arrayList2);
            list2.clear();
            list2.addAll(arrayList3);
            LOGGER.trace("Sorted list: {} items", (Object)list.size());
        }
    }

    public void a(String string) {
        LOGGER.debug("Search query updated: '{}'", (Object)string);
        this.n = string.toLowerCase().trim();
        this.k.e();
    }

    public void a(int n) {
        if (this.m != n) {
            LOGGER.debug("Tab changed from {} to {}", (Object)this.m, (Object)n);
            this.m = n;
            this.k.e();
        }
    }

    private int a(JSONArray jSONArray, int n) {
        boolean bl = false;
        if (0 < jSONArray.length()) {
            try {
                JSONObject jSONObject = jSONArray.getJSONObject(0);
                if (Integer.parseInt(jSONObject.getString("server").replaceAll("[^0-9]", "")) == n) {
                    int n2 = jSONObject.getInt("timeSeconds");
                    LOGGER.trace("Found server {} with {} seconds remaining", (Object)n, (Object)n2);
                    return n2;
                }
            }
            catch (Exception exception) {
                LOGGER.warn("Error ߫arsing JSON for server ID ݠ}: {}", (Object)n, (Object)exception.getMessage());
            }
            return -1;
        }
        LOGGER.trace("Server {} not found in JSON array", (Object)n);
        return -1;
    }

    private void a(List<ServerEventEntry> list, List<ServerEventCard> list2, JSONArray jSONArray, int n) {
        int n2 = list.size();
        for (int i = (n2 ^ 1) - 2 * ((n2 ^ 0xFFFFFFFF) & 1); i >= 0; --i) {
            ServerEventEntry tywt852 = list.get(i);
            int n3 = this.a(jSONArray, tywt852.b());
            if (n3 <= 0) continue;
            int n4 = 2 * (n3 & ~n) - (n3 ^ n);
            if (n4 <= 0) {
                LOGGER.debug("Removing event {} (ID: {}) - expired", (Object)tywt852.a(), (Object)tywt852.b());
                list.remove(i);
                list2.remove(i);
                continue;
            }
            tywt852.a(n4);
            LOGGER.trace("Updated event {} remaining time: {}s", (Object)tywt852.a(), (Object)n4);
        }
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4) {
        String string;
        String string2;
        FontRenderer v6hnga2 = ClientFonts.b[24];
        FontRenderer v6hnga3 = ClientFonts.a[15];
        if (this.c()) {
            string2 = "На сервере пока-что нет ивентов :(";
            string = "Подожди немного, скоро начнутся";
            LOGGER.trace("Displaying empty state message: no events");
        } else {
            string2 = "Ничего не найдено";
            string = "Попробуй изменить запрос";
            LOGGER.trace("Displaying empty state message: no search results");
        }
        float f5 = v6hnga2.a(string2);
        float f6 = v6hnga3.a(string);
        float f7 = f + (f3 - f5) / 2.0f;
        float f8 = f2 + f4 / 2.0f - 20.0f;
        float f9 = f + (f3 - f6) / 2.0f;
        v6hnga2.a(string2, f7, (double)f8, pryrvd.a, matrixStack);
        v6hnga3.a(string, f9, (double)(f8 + 15.0f), pryrvd.b, matrixStack);
    }

    private float a(List<ServerEventCard> list) {
        if (list.isEmpty()) {
            return 0.0f;
        }
        float f = (float)list.size() * 42.5f - 6.0f;
        LOGGER.trace("Scroll height calculated: {} for {} cards", (Object)Float.valueOf(f), (Object)list.size());
        return f;
    }

    public void a() {
        LOGGER.info("Clearing all events");
        this.e.clear();
        this.f.clear();
        this.g.clear();
        this.h.clear();
        this.i.clear();
        this.j.clear();
        this.k.e();
        LOGGER.debug("All event lists cleared");
    }

    public void a(ServerEventEntry tywt852) {
        LOGGER.info("Adding active event: {} (ID: {})", (Object)tywt852.a(), (Object)tywt852.b());
        this.e.add(tywt852);
        ServerEventCard st7tbs2 = new ServerEventCard(tywt852);
        st7tbs2.a(this::d);
        this.h.add(st7tbs2);
        LOGGER.trace("Active events count: {}", (Object)this.e.size());
    }
}

