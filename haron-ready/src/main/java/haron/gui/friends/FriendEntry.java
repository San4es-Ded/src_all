package haron.gui.friends;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FriendEntry {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ROOT);
    private String name;
    private LocalDate date;

    public String formattedDate() {
        return this.date.format(DISPLAY_DATE_FORMAT);
    }

    public FriendEntry(String string) {
        this(string, LocalDate.now());
    }

    public FriendEntry(String string, LocalDate localDate) {
        this.name = string;
        this.date = localDate;
    }

    public String name() {
        int n = 470;
        return this.name;
    }

    public LocalDate b() {
        return this.date;
    }

    public String c() {
        return this.formattedDate();
    }

    public void a(LocalDate localDate) {
        this.date = localDate;
    }

    public String a() {
        return this.name;
    }

    public void a(String string) {
        this.name = string;
    }

    public void setName(String string) {
        this.name = string;
    }

    public void setDate(LocalDate localDate) {
        this.date = localDate;
    }

    public LocalDate date() {
        return this.date;
    }
}

