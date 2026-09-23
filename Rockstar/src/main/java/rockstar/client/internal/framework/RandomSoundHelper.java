package rockstar.client.internal.framework;




import rockstar.client.internal.media.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.time.LocalTime;
import java.util.Random;
import lombok.Generated;
import rockstar.modules.other.SoundsModule;
import rockstar.client.RockstarClient;
import rockstar.client.internal.media.ModSoundInstance;
import rockstar.client.internal.core.ClientSoundEvents;

public final class RandomSoundHelper {
    private static final Random internalField0362 = new Random();

    public static void internalMethod07535() {
        ModSoundInstance typedValue276 = switch (internalField0362.nextInt(4)) {
            case 0 -> ClientSoundEvents.internalField1431;
            case 1 -> ClientSoundEvents.internalField1430;
            default -> ClientSoundEvents.internalField1684;
        };
        typedValue276.internalMethod03864(RandomSoundHelper.internalMethod07534());
    }

    public static void internalMethod07538() {
        ModSoundInstance typedValue276 = switch (internalField0362.nextInt(4)) {
            case 0 -> ClientSoundEvents.internalField1690;
            case 1 -> ClientSoundEvents.internalField1838;
            default -> RandomSoundHelper.internalMethod06606();
        };
        typedValue276.internalMethod03864(RandomSoundHelper.internalMethod07534());
    }

    public static void internalMethod08440() {
        ModSoundInstance typedValue276 = switch (internalField0362.nextInt(8)) {
            case 0 -> ClientSoundEvents.internalField1677;
            case 1 -> ClientSoundEvents.internalField1682;
            case 2 -> ClientSoundEvents.internalField1676;
            case 3 -> ClientSoundEvents.internalField1683;
            case 4 -> ClientSoundEvents.internalField1680;
            case 5 -> ClientSoundEvents.internalField1687;
            case 6 -> ClientSoundEvents.internalField1688;
            default -> ClientSoundEvents.internalField1679;
        };
        typedValue276.internalMethod03864(RandomSoundHelper.internalMethod07534());
    }

    public static void internalMethod08441() {
        ModSoundInstance typedValue276 = switch (internalField0362.nextInt(4)) {
            case 0 -> ClientSoundEvents.internalField1837;
            case 1 -> ClientSoundEvents.internalField1835;
            default -> ClientSoundEvents.internalField1836;
        };
        typedValue276.internalMethod03864(RandomSoundHelper.internalMethod07534());
    }

    public static void internalMethod08455() {
        ModSoundInstance typedValue276 = switch (internalField0362.nextInt(4)) {
            case 0 -> ClientSoundEvents.internalField1678;
            case 1 -> ClientSoundEvents.internalField1681;
            default -> ClientSoundEvents.internalField1686;
        };
        typedValue276.internalMethod03864(RandomSoundHelper.internalMethod07534());
    }

    private static float internalMethod07534() {
        return RockstarClient.getInstance().getModuleManager().getModule(SoundsModule.class).internalMethod01798();
    }

    private static ModSoundInstance internalMethod06606() {
        LocalTime localTime = LocalTime.now();
        int n = localTime.getHour();
        if (n >= 6 && n < 12) {
            return ClientSoundEvents.internalField1685;
        }
        if (n >= 12 && n < 18) {
            return ClientSoundEvents.internalField1691;
        }
        return ClientSoundEvents.internalField1689;
    }

    @Generated
    private RandomSoundHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
