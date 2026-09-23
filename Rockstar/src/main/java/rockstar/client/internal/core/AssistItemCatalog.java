package rockstar.client.internal.core;






import rockstar.client.rotation.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import rockstar.client.internal.inventory.AssistSwapItem;
import rockstar.client.internal.game.AntiFlightAssist;
import rockstar.client.internal.inventory.AssassinPotionSwapItem;
import rockstar.client.internal.inventory.BackpackSwapItem;
import rockstar.client.internal.game.BombAssist;
import rockstar.client.internal.game.BoomTrapAssist;
import rockstar.client.internal.game.AuraAssist;
import rockstar.client.internal.rotation.WindChargeRotation;
import rockstar.client.internal.game.DarkPulseAssist;
import rockstar.client.internal.game.DezorentAssist;
import rockstar.client.internal.game.ExperienceScrollAssist;
import rockstar.client.internal.inventory.HlopushkaPotionSwapItem;
import rockstar.client.internal.inventory.HolyWaterPotionSwapItem;
import rockstar.client.internal.inventory.PaladinPotionSwapItem;
import rockstar.client.internal.game.PilbAssist;
import rockstar.client.internal.game.PlastAssist;
import rockstar.client.internal.inventory.RadiationPotionSwapItem;
import rockstar.client.internal.inventory.SleepingPotionSwapItem;
import rockstar.client.internal.game.SmerchAssist;
import rockstar.client.internal.game.SnowAssist;
import rockstar.client.internal.game.StanAssist;
import rockstar.client.internal.game.TrapAssist;
import rockstar.client.internal.inventory.TrapkaSwapItem;
import rockstar.client.internal.inventory.WrathPotionSwapItem;

public final class AssistItemCatalog {
    private AssistItemCatalog() {
    }

    public static List<AssistSwapItem> internalMethod02335() {
        ArrayList<AssistSwapItem> arrayList = new ArrayList<AssistSwapItem>();
        arrayList.add(new TrapkaSwapItem());
        arrayList.add(new DezorentAssist());
        arrayList.add(new AuraAssist());
        arrayList.add(new SmerchAssist());
        arrayList.add(new PlastAssist());
        arrayList.add(new PilbAssist());
        arrayList.add(new StanAssist());
        arrayList.add(new SnowAssist());
        arrayList.add(new TrapAssist());
        arrayList.add(new AntiFlightAssist());
        arrayList.add(new DarkPulseAssist());
        arrayList.add(new ExperienceScrollAssist());
        arrayList.add(new BombAssist());
        arrayList.add(new BoomTrapAssist());
        arrayList.add(new HlopushkaPotionSwapItem());
        arrayList.add(new RadiationPotionSwapItem());
        arrayList.add(new SleepingPotionSwapItem());
        arrayList.add(new HolyWaterPotionSwapItem());
        arrayList.add(new WrathPotionSwapItem());
        arrayList.add(new PaladinPotionSwapItem());
        arrayList.add(new AssassinPotionSwapItem());
        arrayList.add(new WindChargeRotation());
        arrayList.add(new BackpackSwapItem());
        return arrayList;
    }
}

