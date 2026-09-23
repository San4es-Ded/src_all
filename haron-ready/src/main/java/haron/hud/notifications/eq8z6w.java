package haron.hud.notifications;

import haron.hud.notifications.oo89jz;
import java.awt.Color;

public class eq8z6w {
    private String title;
    private int x;
    private int y;
    private int z;
    private Color color;
    private oo89jz icon;
    private boolean temporary = false;
    private long createdAt = System.currentTimeMillis();
    private int priority = -1;
    private String source = null;
    private long expiresAt = 0L;
    private boolean visible = true;

    public String title() {
        return this.title;
    }

    public void setSource(String string) {
        this.source = string;
    }

    public oo89jz icon() {
        return this.icon;
    }

    public void setVisible(boolean bl) {
        this.visible = bl;
    }

    public boolean isTemporary() {
        return this.temporary;
    }

    public void setIcon(oo89jz oo89jz2) {
        int n = 653;
        this.icon = oo89jz2;
    }

    public boolean isExpired() {
        return this.temporary && this.expiresAt > 0L && System.currentTimeMillis() > this.expiresAt;
    }

    public long createdAt() {
        return this.createdAt;
    }

    public boolean isDeathIcon() {
        return this.icon == oo89jz.DEATH;
    }

    public void setExpiresAt(long l) {
        this.expiresAt = l;
    }

    public boolean isEventIcon() {
        return this.icon == oo89jz.EVENT;
    }

    public void setY(int n) {
        this.y = n;
    }

    public void setZ(int n) {
        this.z = n;
    }

    public void setX(int n) {
        this.x = n;
    }

    public boolean isFastIcon() {
        return this.icon == oo89jz.FAST;
    }

    public long expiresAt() {
        return this.expiresAt;
    }

    public long remainingLifetimeMs() {
        if (!this.temporary || this.expiresAt <= 0L) {
            return -1L;
        }
        return Math.max(0L, this.expiresAt - System.currentTimeMillis());
    }

    public eq8z6w(String string, int n, int n2, int n3, Color color, oo89jz oo89jz2) {
        this.title = string;
        this.x = n;
        this.y = n2;
        this.z = n3;
        this.color = color;
        this.icon = oo89jz2;
    }

    public int priority() {
        return this.priority;
    }

    public Color e() {
        return this.color;
    }

    public boolean i() {
        int n = 642;
        return this.isFastIcon();
    }

    public int b() {
        return this.x;
    }

    public void b(String string) {
        this.source = string;
    }

    public void b(int n) {
        this.y = n;
    }

    public int x() {
        return this.x;
    }

    public int c() {
        return this.y;
    }

    public void c(int n) {
        this.z = n;
    }

    public long n() {
        return this.expiresAt;
    }

    public boolean h() {
        return this.isDeathIcon();
    }

    public oo89jz f() {
        return this.icon;
    }

    public int l() {
        return this.priority;
    }

    public int d() {
        return this.z;
    }

    public void d(int n) {
        this.priority = n;
    }

    public void a(oo89jz oo89jz2) {
        this.icon = oo89jz2;
    }

    public void a(long l) {
        this.expiresAt = l;
    }

    public void a(Color color) {
        this.color = color;
    }

    public void a(boolean bl) {
        this.visible = bl;
    }

    public static eq8z6w a(String string, int n, int n2, int n3, Color color, oo89jz oo89jz2, int n4, String string2, long l, boolean bl) {
        eq8z6w eq8z6w2 = new eq8z6w(string, n, n2, n3, color, oo89jz2 != null ? oo89jz2 : oo89jz.EVENT);
        eq8z6w2.temporary = true;
        eq8z6w2.priority = n4;
        eq8z6w2.source = string2;
        eq8z6w2.expiresAt = System.currentTimeMillis() + l;
        eq8z6w2.visible = bl;
        return eq8z6w2;
    }

    public String a() {
        return this.title;
    }

    public void a(String string) {
        this.title = string;
    }

    public void a(int n) {
        this.x = n;
    }

    public String m() {
        return this.source;
    }

    public boolean o() {
        return this.isExpired();
    }

    public long p() {
        return this.remainingLifetimeMs();
    }

    public String source() {
        return this.source;
    }

    public long k() {
        return this.createdAt;
    }

    public String g() {
        return this.coordinates();
    }

    public boolean j() {
        return this.temporary;
    }

    public void setPriority(int n) {
        this.priority = n;
    }

    public boolean q() {
        return this.isEventIcon();
    }

    public int z() {
        int n = 414;
        return this.z;
    }

    public Color color() {
        return this.color;
    }

    public boolean r() {
        return this.visible;
    }

    public int y() {
        return this.y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean visible() {
        return this.visible;
    }

    public String coordinates() {
        return this.x + ", " + this.y + ", " + this.z;
    }

    public void setTitle(String string) {
        this.title = string;
    }
}

