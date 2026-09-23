package pyrock.classes;



import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import rockstar.client.internal.inventory.EntityComparatorUtils;
import rockstar.client.internal.game.TargetSelectionManager;
import rockstar.client.internal.inventory.EntityTargetFilter;
import rockstar.client.RockstarClient;

public class PyTarget {
    public InternalType0300 query() {
        return new InternalType0300();
    }

    @Nullable
    public Entity best(InternalType0300 nestedValue0111) {
        if (nestedValue0111 == null) {
            return null;
        }
        return PyTarget.manager().internalMethod06283(nestedValue0111.build());
    }

    public List<Entity> all(InternalType0300 nestedValue0111) {
        ArrayList<Entity> arrayList = new ArrayList<Entity>();
        if (nestedValue0111 == null || PyTarget.mc().world == null) {
            return arrayList;
        }
        EntityTargetFilter typedValue183 = nestedValue0111.build();
        for (Entity entity : PyTarget.mc().world.getEntities()) {
            if (!typedValue183.internalMethod05417(entity)) continue;
            arrayList.add(entity);
        }
        arrayList.sort(typedValue183.internalMethod00532());
        return arrayList;
    }

    public boolean valid(InternalType0300 nestedValue0111, Entity entity) {
        return nestedValue0111 != null && entity != null && nestedValue0111.build().internalMethod05417(entity);
    }

    @Nullable
    public Entity current() {
        return PyTarget.manager().internalMethod04526();
    }

    @Nullable
    public LivingEntity living() {
        return PyTarget.manager().internalMethod01783();
    }

    public void add(String string) {
        if (string != null && !string.isBlank()) {
            PyTarget.manager().internalMethod04581(string);
        }
    }

    public void remove(String string) {
        if (string != null && !string.isBlank()) {
            PyTarget.manager().internalMethod02995(string);
        }
    }

    public void clear() {
        PyTarget.manager().internalMethod01468();
    }

    public boolean isTarget(String string) {
        return string != null && PyTarget.manager().internalMethod04582(string);
    }

    public List<String> list() {
        return new ArrayList<String>(PyTarget.manager().internalMethod06187());
    }

    static Comparator<Entity> comparator(String string) {
        return switch (string.toLowerCase(Locale.ROOT)) {
            case "health", "hp" -> EntityComparatorUtils.internalField0758;
            case "fov", "angle", "crosshair" -> EntityComparatorUtils.internalField1302;
            case "bad_armor", "weakest" -> EntityComparatorUtils.internalField1304;
            case "good_armor", "strongest" -> EntityComparatorUtils.internalField1303;
            default -> EntityComparatorUtils.internalField0757;
        };
    }

    private static TargetSelectionManager manager() {
        return RockstarClient.getInstance().internalMethod04463();
    }

    private static MinecraftClient mc() {
        return MinecraftClient.getInstance();
    }

    public static final class InternalType0300 {
        private boolean players = true;
        private boolean animals;
        private boolean mobs;
        private boolean users;
        private boolean invisibles;
        private boolean naked = true;
        private boolean friends;
        private boolean armorStands;
        private boolean excludeTeammates;
        private float range = -1.0f;
        private String sort = "distance";

        public InternalType0300 players(boolean bl) {
            this.players = bl;
            return this;
        }

        public InternalType0300 animals(boolean bl) {
            this.animals = bl;
            return this;
        }

        public InternalType0300 mobs(boolean bl) {
            this.mobs = bl;
            return this;
        }

        public InternalType0300 users(boolean bl) {
            this.users = bl;
            return this;
        }

        public InternalType0300 invisibles(boolean bl) {
            this.invisibles = bl;
            return this;
        }

        public InternalType0300 naked(boolean bl) {
            this.naked = bl;
            return this;
        }

        public InternalType0300 friends(boolean bl) {
            this.friends = bl;
            return this;
        }

        public InternalType0300 armorStands(boolean bl) {
            this.armorStands = bl;
            return this;
        }

        public InternalType0300 excludeTeammates(boolean bl) {
            this.excludeTeammates = bl;
            return this;
        }

        public InternalType0300 range(double d) {
            this.range = (float)d;
            return this;
        }

        public InternalType0300 sort(String string) {
            this.sort = string == null ? "distance" : string;
            return this;
        }

        EntityTargetFilter build() {
            return new EntityTargetFilter.InternalType0309().internalMethod00547(this.players).internalMethod06455(this.animals).internalMethod08543(this.mobs).internalMethod08126(this.users).internalMethod07990(this.invisibles).internalMethod09114(this.naked).internalMethod09525(this.friends).internalMethod09298(this.armorStands).internalMethod09220(this.excludeTeammates).internalMethod03468(this.range).internalMethod04109(PyTarget.comparator(this.sort)).internalMethod03528();
        }
    }
}

