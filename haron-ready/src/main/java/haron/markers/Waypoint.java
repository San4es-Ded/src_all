package haron.markers;

import haron.markers.MarkerIcon;
import java.awt.Color;

public class Waypoint {
    private String name;
    private int x;
    private int y;
    private int z;
    private Color color;
    private MarkerIcon icon;
    private boolean serverBound;
    private long createdAt;
    private int serverId;
    private String serverAddress;
    private long expiresAt;
    private boolean showTimer;

    public Waypoint(String string, int n, int n2, int n3, Color color, MarkerIcon h1tssl2) {
        this.name = string;
        this.x = n;
        this.y = n2;
        this.z = n3;
        this.color = color;
        this.icon = h1tssl2 != null ? h1tssl2 : MarkerIcon.EVENT;
        this.createdAt = System.currentTimeMillis();
    }

    public Color e() {
        return this.color;
    }

    public boolean i() {
        return this.icon == MarkerIcon.FAST;
    }

    public void b(int n) {
        this.y = n;
    }

    public int b() {
        return this.x;
    }

    public void b(String string) {
        this.serverAddress = string;
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
        return this.icon == MarkerIcon.DEATH;
    }

    public MarkerIcon f() {
        return this.icon;
    }

    public int l() {
        return this.serverId;
    }

    public void d(int n) {
        this.serverId = n;
    }

    public int d() {
        return this.z;
    }

    public void a(Color color) {
        int n = 135;
        this.color = color;
    }

    public void a(boolean bl) {
        this.showTimer = bl;
    }

    public String a() {
        return this.name;
    }

    public static Waypoint a(String string, int n, int n2, int n3, Color color, MarkerIcon h1tssl2, int n4, String string2, long l, boolean bl) {
        Waypoint yo0tnu2 = new Waypoint(string, n, n2, n3, color, h1tssl2);
        yo0tnu2.serverBound = true;
        yo0tnu2.serverId = n4;
        yo0tnu2.serverAddress = string2;
        yo0tnu2.expiresAt = System.currentTimeMillis() + l;
        yo0tnu2.showTimer = bl;
        return yo0tnu2;
    }

    public void a(long l) {
        this.expiresAt = l;
    }

    public void a(int n) {
        int n2 = 27;
        this.x = n;
    }

    public void a(String string) {
        this.name = string;
    }

    public void a(MarkerIcon h1tssl2) {
        this.icon = h1tssl2 != null ? h1tssl2 : MarkerIcon.EVENT;
    }

    public String m() {
        return this.serverAddress;
    }

    public boolean o() {
        return this.serverBound && this.expiresAt > 0L && System.currentTimeMillis() > this.expiresAt;
    }

    public long p() {
        if (!this.serverBound || this.expiresAt <= 0L) {
            return -1L;
        }
        return Math.max(0L, this.expiresAt - System.currentTimeMillis());
    }

    public long k() {
        return this.createdAt;
    }

    public String g() {
        return this.x + ", " + this.y + ", " + this.z;
    }

    public boolean j() {
        return this.serverBound;
    }

    public boolean q() {
        return this.icon == MarkerIcon.EVENT;
    }

    public boolean r() {
        return this.showTimer;
    }
}

