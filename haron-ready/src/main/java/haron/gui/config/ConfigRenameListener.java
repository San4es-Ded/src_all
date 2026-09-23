package haron.gui.config;

import haron.config.ConfigProfileEntry;

@FunctionalInterface
public interface ConfigRenameListener {
    public void onRenamed(ConfigProfileEntry var1, String var2, String var3);

}

