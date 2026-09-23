package rockstar.client.internal.inventory;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import rockstar.modules.player.AutoFarmModule;
import rockstar.client.internal.core.AutoFarmPhase;
import rockstar.client.internal.script.FarmStatsTracker;
import rockstar.client.internal.core.FarmStatsMetric;
import rockstar.client.internal.core.AutoFarmUnit;
import rockstar.client.setting.ModeSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.util.ClientMessages;
import rockstar.client.MinecraftClientAccess;

public abstract class AutoFarmModeState
extends ModeSetting.InternalType0088
implements MinecraftClientAccess {
    public final AutoFarmModule internalField0063;

    public AutoFarmModeState(AutoFarmModule typedValue221, ModeSetting typedValue170, String string) {
        super(typedValue170, string);
        this.internalField0063 = typedValue221;
    }

    public void internalMethod04694() {
    }

    public void internalMethod04697() {
    }

    public void internalMethod08382() {
    }

    public AutoFarmPhase internalMethod02315() {
        return AutoFarmPhase.internalField0847;
    }

    public String internalMethod06901() {
        return null;
    }

    public AutoFarmUnit internalMethod02317() {
        return AutoFarmUnit.internalField0853;
    }

    public FarmStatsMetric internalMethod00321() {
        return this.internalMethod02317() == AutoFarmUnit.internalField0853 ? FarmStatsMetric.internalField0850 : FarmStatsMetric.internalField0851;
    }

    public ItemStack internalMethod04126() {
        return new ItemStack((ItemConvertible)Items.WHEAT);
    }

    public final FarmStatsTracker internalMethod00320() {
        return this.internalField0063.internalMethod03987();
    }

    public final void internalMethod08387() {
        this.internalField0063.disable();
    }

    public final void internalMethod05961(String string) {
        ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214(string)));
    }

    public final void internalMethod04510(String string) {
        ClientMessages.internalMethod03058(Text.of((String)LanguageManager.internalMethod07214(string)));
    }

    public final void internalMethod08995(String string) {
        ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214(string)));
    }
}
