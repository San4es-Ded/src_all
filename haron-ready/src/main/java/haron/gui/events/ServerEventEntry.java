package haron.gui.events;

import haron.gui.events.ServerEventType;
import haron.gui.events.EventListCategory;
import haron.gui.events.MineEventTier;

public class ServerEventEntry {
    private final String c;
    private final int d;
    private final EventListCategory e;
    private final ServerEventType f;
    private final MineEventTier g;
    private final String h;
    private int i;
    public static int a;
    public static boolean b;

    public ServerEventEntry(String string, int n, EventListCategory nz4c562, ServerEventType f4w1ct2, MineEventTier yh3kva2, String string2, int n2) {
        this.c = string;
        this.d = n;
        this.e = nz4c562;
        this.f = f4w1ct2;
        this.g = yh3kva2;
        this.h = string2;
        this.i = n2;
    }

    public MineEventTier e() {
        return this.g;
    }

    public String i() {
        return this.h;
    }

    public int b() {
        return this.d;
    }

    public EventListCategory c() {
        return this.e;
    }

    public String h() {
        return ServerEventEntry.$sf$0(this.c, this.d);
    }

    public int f() {
        return this.i;
    }

    public ServerEventType d() {
        return this.f;
    }

    public static ServerEventEntry a(String string, int n, int n2) {
        return new ServerEventEntry(string, n, EventListCategory.UPCOMING, null, null, null, n2);
    }

    public static ServerEventEntry a(String string, int n, MineEventTier yh3kva2, String string2, int n2) {
        return new ServerEventEntry(string, n, EventListCategory.MINE, ServerEventType.MINE, yh3kva2, string2, n2);
    }

    public String a() {
        return this.c;
    }

    public void a(int n) {
        this.i = n;
    }

    public static ServerEventEntry a(String string, int n, ServerEventType f4w1ct2, MineEventTier yh3kva2, String string2, int n2) {
        return new ServerEventEntry(string, n, EventListCategory.CURRENT, f4w1ct2, yh3kva2, string2, n2);
    }

    public String g() {
        return String.format("%d:%02d", this.i / 60, this.i % 60);
    }

    public String j() {
        if (this.h != null && !this.h.isEmpty()) {
            return this.h;
        }
        if (this.f == null) {
            return this.c;
        }
        if (this.g == null) {
            return this.f.a();
        }
        if (this.e == EventListCategory.MINE) {
            return this.g.a();
        }
        return ServerEventEntry.$sf$1(this.f.a(), this.g.a());
    }

    private static /* synthetic */ String $sf$0(String string, int n) {
        return string + "  /  " + n;
    }

    private static /* synthetic */ String $sf$1(String string, String string2) {
        return string + " • " + string2;
    }
}

