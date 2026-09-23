package rockstar.client.internal.game;





import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import rockstar.client.internal.inventory.EntityTargetFilter;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;
import rockstar.client.MinecraftClientAccess;

public class TargetSelectionManager
implements MinecraftClientAccess {
    @Nullable
    private Entity internalField0410 = null;
    private final Set<String> internalField0546 = new LinkedHashSet<String>();

    public final void internalMethod05014(EntityTargetFilter typedValue183) {
        this.internalField0410 = this.internalMethod06283(typedValue183);
    }

    public final void internalMethod04581(String string) {
        if (RockstarClient.getInstance().internalMethod03375().internalMethod06515().contains(string)) {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.target.friend_error")));
            return;
        }
        if (this.internalField0546.contains(string)) {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod00160("commands.target.already_exists", string)));
            return;
        }
        if (string.equalsIgnoreCase(internalField0149.getSession().getUsername())) {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.target.self_error")));
            return;
        }
        this.internalField0546.add(string);
        ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.target.added", string)));
    }

    public final void internalMethod02995(String string) {
        if (!this.internalField0546.contains(string)) {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod00160("commands.target.not_found", string)));
            return;
        }
        this.internalField0546.remove(string);
        ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.target.removed", string)));
    }

    public final void internalMethod01468() {
        if (this.internalField0546.isEmpty()) {
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.target.empty")));
            return;
        }
        this.internalField0546.clear();
        ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.target.cleared")));
    }

    public final void internalMethod01470() {
        if (this.internalField0546.isEmpty()) {
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.target.empty")));
            return;
        }
        int n = 1;
        for (String string : this.internalField0546) {
            ClientMessages.internalMethod01809(Text.of((String)String.format(LanguageManager.internalMethod07214("commands.target.list_entry"), n++, string)));
        }
    }

    @Nullable
    public final Entity internalMethod06283(EntityTargetFilter typedValue183) {
        if (TargetSelectionManager.internalField0149.world == null) {
            return null;
        }
        Entity entity = null;
        boolean bl = false;
        for (Entity entity2 : TargetSelectionManager.internalField0149.world.getEntities()) {
            boolean bl2;
            if (!typedValue183.internalMethod05417(entity2)) continue;
            boolean bl3 = bl2 = !this.internalField0546.isEmpty() && this.internalField0546.contains(entity2.getName().getString());
            if (entity == null) {
                entity = entity2;
                bl = bl2;
                continue;
            }
            if (bl2 && !bl) {
                entity = entity2;
                bl = true;
                continue;
            }
            if (bl2 != bl || typedValue183.internalMethod00532().compare(entity2, entity) >= 0) continue;
            entity = entity2;
        }
        return entity;
    }

    public final void internalMethod07889() {
        this.internalField0410 = null;
    }

    public final boolean internalMethod04582(String string) {
        return this.internalField0546.contains(string);
    }

    public final LivingEntity internalMethod01783() {
        LivingEntity livingEntity;
        Entity entity = RockstarClient.getInstance().internalMethod04463().internalMethod04526();
        return entity instanceof LivingEntity ? (livingEntity = (LivingEntity)entity) : null;
    }

    @Nullable
    @Generated
    public Entity internalMethod04526() {
        return this.internalField0410;
    }

    @Generated
    public Set<String> internalMethod06187() {
        return this.internalField0546;
    }
}

