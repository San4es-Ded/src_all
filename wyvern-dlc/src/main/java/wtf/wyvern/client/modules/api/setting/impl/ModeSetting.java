package wtf.wyvern.client.modules.api.setting.impl;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import lombok.Generated;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.client.modules.api.setting.Setting;

public class ModeSetting extends Setting {
    private final List<Value> values = new ArrayList();
    private Value value;
    private final Animation expandAnimation = new Animation(200L, Easing.CUBIC_OUT);

    public ModeSetting(String name, String... modes) {
        super(name);
        String[] var3 = modes;
        int var4 = modes.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String mode = var3[var5];
            if (!mode.isEmpty()) {
                new Value(this, mode);
            }
        }

        if (!this.values.isEmpty()) {
            this.value = (Value)this.values.getFirst();
        }

    }

    public ModeSetting(String name, Supplier<Boolean> visible, String... modes) {
        super(name);
        String[] var4 = modes;
        int var5 = modes.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String mode = var4[var6];
            if (!mode.isEmpty()) {
                new Value(this, mode);
            }
        }

        if (!this.values.isEmpty()) {
            this.value = (Value)this.values.getFirst();
        }

        this.setVisible(visible);
    }

    public void set(String mode) {
        for (Value candidate : this.values) {
            if (candidate.getName().equals(mode)) {
                this.value = candidate;
                return;
            }
        }
    }

    /**
     * Adds a runtime-provided mode without duplicating an existing value.
     * Neuro rotations are loaded from disk before configs, so a saved
     * Neuro(name) value behaves exactly like a built-in mode.
     */
    public Value addMode(String mode) {
        if (mode == null || mode.isBlank()) {
            return null;
        }
        for (Value candidate : this.values) {
            if (candidate.getName().equals(mode)) {
                return candidate;
            }
        }
        return new Value(this, mode);
    }

    public String get() {
        return this.value != null ? this.value.getName() : "";
    }

    public boolean is(String mode) {
        return this.value != null && this.value.getName().equals(mode);
    }

    public boolean is(Value otherValue) {
        return this.value == otherValue;
    }

    public Value getRandomEnabledElement() {
        int selectedCount = 0;
        for (Value candidate : this.values) {
            if (candidate.isSelected()) selectedCount++;
        }
        if (selectedCount == 0) return null;

        int selectedIndex = ThreadLocalRandom.current().nextInt(selectedCount);
        for (Value candidate : this.values) {
            if (candidate.isSelected() && selectedIndex-- == 0) return candidate;
        }
        return null;
    }

    public void safe(JsonObject propertiesObject) {
        propertiesObject.addProperty(String.valueOf(this.name), this.get());
    }

    public void load(JsonObject propertiesObject) {
        this.set(propertiesObject.get(String.valueOf(this.name)).getAsString());
    }

    @Generated
    public List<Value> getValues() {
        return this.values;
    }

    @Generated
    public Value getValue() {
        return this.value;
    }

    @Generated
    public void setValue(Value value) {
        this.value = value;
    }

    public void updateExpandAnimation(boolean expanded) {
        this.expandAnimation.update(expanded ? 1.0F : 0.0F);
    }

    @Generated
    public Animation getExpandAnimation() {
        return this.expandAnimation;
    }

    public static class Value {
        private final ModeSetting parent;
        private final String name;
        private final String description;
        private final Animation animation;

        public Value(ModeSetting parent, String name) {
            this.animation = new Animation(250L, Easing.CUBIC_OUT);
            this.parent = parent;
            this.name = name;
            this.description = "";
            if (parent.values.isEmpty()) {
                this.select();
            }

            parent.values.add(this);
        }

        public Value(ModeSetting parent, String name, String description) {
            this.animation = new Animation(250L, Easing.CUBIC_OUT);
            this.parent = parent;
            this.name = name;
            this.description = description;
            if (parent.values.isEmpty()) {
                this.select();
            }

            parent.values.add(this);
        }

        public Value select() {
            this.parent.setValue(this);
            return this;
        }

        public boolean isSelected() {
            return this.parent.getValue() == this;
        }

        public String toString() {
            return this.name;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj != null && obj.getClass() == this.getClass()) {
                Value that = (Value)obj;
                return Objects.equals(this.parent, that.parent) && Objects.equals(this.name, that.name) && Objects.equals(this.description, that.description);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.parent, this.name, this.description});
        }

        @Generated
        public ModeSetting getParent() {
            return this.parent;
        }

        @Generated
        public String getName() {
            return this.name;
        }

        @Generated
        public String getDescription() {
            return this.description;
        }

        @Generated
        public Animation getAnimation() {
            return this.animation;
        }
    }
}
