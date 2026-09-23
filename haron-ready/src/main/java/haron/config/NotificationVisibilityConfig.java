package haron.config;

public class NotificationVisibilityConfig {
    private boolean notificationsEnabled = true;
    private boolean musicEnabled = true;
    private boolean performanceEnabled = true;
    private boolean eventsEnabled = true;
    private boolean modulesEnabled = true;

    public boolean areModulesEnabled() {
        return this.modulesEnabled;
    }

    public void setModulesEnabled(boolean enabled) {
        this.modulesEnabled = enabled;
    }

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
    }

    public boolean isMusicEnabled() {
        return this.musicEnabled;
    }

    public void setPerformanceEnabled(boolean enabled) {
        this.performanceEnabled = enabled;
    }

    public boolean isPerformanceEnabled() {
        return this.performanceEnabled;
    }

    public void setEventsEnabled(boolean enabled) {
        this.eventsEnabled = enabled;
    }

    public boolean areEventsEnabled() {
        return this.eventsEnabled;
    }

    public boolean areNotificationsEnabled() {
        return this.notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean enabled) {
        this.notificationsEnabled = enabled;
    }
}
