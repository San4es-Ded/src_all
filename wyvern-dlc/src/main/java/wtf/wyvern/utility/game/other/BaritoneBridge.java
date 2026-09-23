package wtf.wyvern.utility.game.other;

import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public final class BaritoneBridge {

    private Object primaryBaritone;
    private Object settings;
    private final Map<String, Object> previousSettings = new HashMap<>();
    private boolean initialized;

    public boolean isAvailable() {
        initialize();
        return primaryBaritone != null;
    }

    public void configureSafeMovement() {
        if (!isAvailable()) {
            return;
        }
        setSetting("allowPlace", false);
        setSetting("allowParkourPlace", false);
        setSetting("allowParkourAscend", false);
        setSetting("allowParkour", false);
        setSetting("allowJumpAtBuildLimit", false);
        setSetting("allowBreak", true);
        setSetting("allowOnlyExposedOres", false);
        setSetting("blacklistClosestOnFailure", false);
        setSetting("legitMine", true);
        setSetting("walkWhileBreaking", false);
        setSetting("breakFromAbove", false);
        setSetting("goalBreakFromAbove", false);
        setSetting("chatDebug", false);
        setSetting("desktopNotifications", false);
        setSetting("notificationOnMineFail", false);
        setSetting("notificationOnPathComplete", false);
        setSetting("logger", (Consumer<Object>) ignored -> {
        });
        setSetting("toaster", (BiConsumer<Object, Object>) (ignoredTitle, ignoredBody) -> {
        });
    }

    public boolean goToBlock(BlockPos pos) {
        if (!isAvailable()) {
            return false;
        }
        try {
            setGoalAndPath("baritone.api.pathing.goals.GoalGetToBlock", pos);
            return true;
        } catch (ReflectiveOperationException exception) {
            try {
                setGoalAndPath("baritone.api.pathing.goals.GoalTwoBlocks", pos);
                return true;
            } catch (ReflectiveOperationException ignored) {
                try {
                    setGoalNearAndPath(pos, 1);
                    return true;
                } catch (ReflectiveOperationException ignoredAgain) {
                    return executeCommand("goto " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
                }
            }
        }
    }

    public boolean fleeFrom(BlockPos pos) {
        if (!isAvailable()) {
            return false;
        }
        try {
            Class<?> goalClass = Class.forName("baritone.api.pathing.goals.Goal");
            Class<?> goalBlockClass = Class.forName("baritone.api.pathing.goals.GoalBlock");
            Object goalBlock = goalBlockClass.getConstructor(BlockPos.class).newInstance(pos);
            Class<?> invertedClass = Class.forName("baritone.api.pathing.goals.GoalInverted");
            Object inverted = invertedClass.getConstructor(goalClass).newInstance(goalBlock);
            setGoalObjectAndPath(inverted);
            return true;
        } catch (ReflectiveOperationException exception) {
            return false;
        }
    }

    public boolean goNear(BlockPos pos, int range) {
        if (!isAvailable()) {
            return false;
        }
        try {
            setGoalNearAndPath(pos, range);
            return true;
        } catch (ReflectiveOperationException exception) {
            return goToBlock(pos);
        }
    }

    private void setGoalNearAndPath(BlockPos pos, int range) throws ReflectiveOperationException {
        Class<?> goalClass = Class.forName("baritone.api.pathing.goals.GoalNear");
        Object goal = goalClass.getConstructor(BlockPos.class, int.class).newInstance(pos, range);
        setGoalObjectAndPath(goal);
    }

    private void setGoalAndPath(String goalClassName, BlockPos pos) throws ReflectiveOperationException {
        Class<?> goalClass = Class.forName(goalClassName);
        Object goal = goalClass.getConstructor(BlockPos.class).newInstance(pos);
        setGoalObjectAndPath(goal);
    }

    private void setGoalObjectAndPath(Object goal) throws ReflectiveOperationException {
        Object customGoalProcess = primaryBaritone.getClass().getMethod("getCustomGoalProcess").invoke(primaryBaritone);
        Method setGoalAndPath = customGoalProcess.getClass().getMethod("setGoalAndPath", Class.forName("baritone.api.pathing.goals.Goal"));
        setGoalAndPath.invoke(customGoalProcess, goal);
    }

    public boolean isPathing() {
        if (!isAvailable()) {
            return false;
        }
        try {
            Object pathingBehavior = primaryBaritone.getClass().getMethod("getPathingBehavior").invoke(primaryBaritone);
            Object result = pathingBehavior.getClass().getMethod("isPathing").invoke(pathingBehavior);
            return result instanceof Boolean value && value;
        } catch (ReflectiveOperationException exception) {
            return false;
        }
    }

    public void stop() {
        if (!isAvailable()) {
            return;
        }
        try {
            Object pathingBehavior = primaryBaritone.getClass().getMethod("getPathingBehavior").invoke(primaryBaritone);
            pathingBehavior.getClass().getMethod("cancelEverything").invoke(pathingBehavior);
        } catch (ReflectiveOperationException ignored) {
            executeCommand("cancel");
        }
    }

    public boolean executeCommand(String command) {
        if (!isAvailable()) {
            return false;
        }
        try {
            Object commandManager = primaryBaritone.getClass().getMethod("getCommandManager").invoke(primaryBaritone);
            Object result = commandManager.getClass().getMethod("execute", String.class).invoke(commandManager, command);
            return result instanceof Boolean value && value;
        } catch (ReflectiveOperationException exception) {
            return false;
        }
    }

    public void setSetting(String name, Object value) {
        if (settings == null) {
            return;
        }
        try {
            Field field = settings.getClass().getField(name);
            Object setting = field.get(settings);
            Field valueField = setting.getClass().getField("value");
            previousSettings.putIfAbsent(name, valueField.get(setting));
            valueField.set(setting, value);
        } catch (ReflectiveOperationException | IllegalArgumentException ignored) {
        }
    }

    public void restoreSettings() {
        if (settings == null || previousSettings.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : previousSettings.entrySet()) {
            try {
                Field field = settings.getClass().getField(entry.getKey());
                Object setting = field.get(settings);
                Field valueField = setting.getClass().getField("value");
                valueField.set(setting, entry.getValue());
            } catch (ReflectiveOperationException | IllegalArgumentException ignored) {
            }
        }
        previousSettings.clear();
    }

    private void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        try {
            Class<?> apiClass = Class.forName("baritone.api.BaritoneAPI");
            Object provider = apiClass.getMethod("getProvider").invoke(null);
            primaryBaritone = provider.getClass().getMethod("getPrimaryBaritone").invoke(provider);
            settings = apiClass.getMethod("getSettings").invoke(null);
        } catch (ReflectiveOperationException exception) {
            primaryBaritone = null;
            settings = null;
        }
    }
}
