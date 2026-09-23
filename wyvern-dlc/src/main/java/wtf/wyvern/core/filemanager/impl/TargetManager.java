package wtf.wyvern.core.filemanager.impl;

import com.google.common.reflect.TypeToken;
import java.util.HashSet;
import java.util.Set;
import wtf.wyvern.core.filemanager.api.ManagerFileAbstract;

public final class TargetManager extends ManagerFileAbstract<String> {
    public TargetManager() {
        super("targets.json", "", new TypeToken<Set<String>>() {}.getType(), HashSet::new);
    }

    public boolean isTarget(String name) {
        return getItems().stream().anyMatch(target -> target.equalsIgnoreCase(name));
    }
}
