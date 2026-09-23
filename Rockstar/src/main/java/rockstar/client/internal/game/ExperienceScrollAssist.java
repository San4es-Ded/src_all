package rockstar.client.internal.game;





import rockstar.client.util.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.item.Items;
import rockstar.client.internal.core.ItemCooldownHolder;
import rockstar.client.util.KeybindUtils;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.core.AssistItemCategory;
import rockstar.client.internal.core.ServerTypeChecks;
import rockstar.client.internal.inventory.AbstractKeywordPotionSwapItem;
import rockstar.client.util.Stopwatch;

public class ExperienceScrollAssist
extends AbstractKeywordPotionSwapItem
implements MinecraftClientAccess {
    private final Stopwatch internalField0519 = new Stopwatch();
    private boolean internalField0277;

    public ExperienceScrollAssist() {
        super("modules.settings.assist.experience_scroll", Items.FLOWER_BANNER_PATTERN, AssistItemCategory.internalField0460, "\u0441\u0432\u0438\u0442", "\u043e\u043f\u044b\u0442");
    }

    @Override
    public void internalMethod03235() {
        this.internalField0277 = true;
        this.internalField0519.internalMethod00701();
        this.internalMethod08069();
    }

    @Override
    public boolean internalMethod07961() {
        return true;
    }

    @Override
    public boolean internalMethod03236() {
        return ServerTypeChecks.internalMethod08904();
    }

    @Override
    public boolean internalMethod04619() {
        return this.internalField0277;
    }

    @Override
    public void internalMethod04618() {
        if (!this.internalField0277) {
            return;
        }
        if (ExperienceScrollAssist.internalField0149.player == null || ExperienceScrollAssist.internalField0149.world == null || ExperienceScrollAssist.internalField0149.currentScreen != null || !this.internalMethod08071()) {
            this.internalField0277 = false;
            return;
        }
        if (this.internalField0519.internalMethod02365(50L)) {
            this.internalField0519.internalMethod00701();
            this.internalMethod08069();
        }
    }

    private void internalMethod08069() {
        if (!ItemCooldownHolder.internalField0006.internalMethod03576()) {
            ItemCooldownHolder.internalField0006.internalMethod04839(this.internalMethod03447(), this::internalMethod05467, this.internalMethod00556());
        }
    }

    private boolean internalMethod08071() {
        return KeybindUtils.internalMethod08521(this.internalMethod03234());
    }
}

