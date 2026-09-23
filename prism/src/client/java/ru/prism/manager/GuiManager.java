package ru.prism.manager;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class GuiManager {

    public static final File file = new File("C:/prism/client1_21_11/config", "theme/theme.json");
    private Theme currentTheme = Theme.NIGHT;
    private int customColor = Theme.customColor;


    public void init() {
        try {
            // создаём родительские папки
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
                saveSettings();
            } else {
                readSettings();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setGuiTheme(Theme theme) {
        currentTheme = theme;
        saveSettings();
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public int getCustomColor() {
        return customColor;
    }

    public void setCustomColor(int color) {
        customColor = color;
        saveSettings();
    }

    private void saveSettings() {
        try (FileWriter writer = new FileWriter(file)) {
            Properties props = new Properties();
            props.setProperty("theme", currentTheme.name());
            props.setProperty("custom_color", String.valueOf(customColor));
            props.store(writer, "GUI Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readSettings() {
        try (FileReader reader = new FileReader(file)) {
            Properties props = new Properties();
            props.load(reader);
            currentTheme = Theme.valueOf(props.getProperty("theme", Theme.NIGHT.name()));
            try {
                customColor = (int) Long.parseLong(props.getProperty("custom_color", String.valueOf(Theme.customColor)));
            } catch (NumberFormatException e) {
                customColor = Theme.customColor;
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}