package rockstar.client.internal.core;




import rockstar.client.bot.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import java.security.SecureRandom;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import rockstar.client.internal.script.BotRosterManager;
import rockstar.client.bot.BotTargetManager;

public final class BotSpawnCoordinator {
    private static final SecureRandom internalField0858 = new SecureRandom();
    private static final ScheduledExecutorService internalField0220 = Executors.newSingleThreadScheduledExecutor(new ThreadFactory(){

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "Rockstar-BotStarter");
            thread.setDaemon(true);
            return thread;
        }
    });
    private static final AtomicBoolean internalField0020 = new AtomicBoolean(false);
    private static final AtomicBoolean internalField0019 = new AtomicBoolean(false);
    private static final AtomicLong internalField0053 = new AtomicLong();

    private BotSpawnCoordinator() {
    }

    public static BotTargetManager internalMethod02098(String string, String string2) {
        BotRosterManager.InternalType0457 nestedValue0159 = BotRosterManager.internalMethod00405(string2);
        return nestedValue0159 == null ? null : BotSpawnCoordinator.internalMethod02103(string, nestedValue0159.internalMethod05503(), nestedValue0159.internalMethod03719());
    }

    public static BotTargetManager internalMethod02103(String string, String string2, int n) {
        return BotRosterManager.internalMethod03065().internalMethod04594(string, string2, n);
    }

    public static void internalMethod03544(int n, String string) {
        BotRosterManager.InternalType0457 nestedValue0159 = BotRosterManager.internalMethod00405(string);
        if (nestedValue0159 != null) {
            BotSpawnCoordinator.internalMethod07347(n, nestedValue0159.internalMethod05503(), nestedValue0159.internalMethod03719());
        }
    }

    public static void internalMethod07347(int n, String string, int n2) {
        if (n <= 0) {
            return;
        }
        BotRosterManager iIIIIIRockstarClient = BotRosterManager.internalMethod03065();
        internalField0019.set(false);
        long l = internalField0053.incrementAndGet();
        int n3 = Math.max(1, iIIIIIRockstarClient.internalMethod03066().internalMethod09191());
        String string2 = iIIIIIRockstarClient.internalMethod03066().internalMethod01093();
        ConcurrentHashMap.KeySetView keySetView = ConcurrentHashMap.newKeySet();
        for (BotTargetManager typedValue055 : iIIIIIRockstarClient.internalMethod01257()) {
            keySetView.add(typedValue055.internalMethod03426());
        }
        int n4 = 0;
        while (n4 < n) {
            int n5 = n4++;
            internalField0220.schedule(() -> {
                if (!BotSpawnCoordinator.internalMethod03331(l)) {
                    return;
                }
                String string3 = BotSpawnCoordinator.internalMethod02392(string2, keySetView, n5);
                keySetView.add(string3);
                iIIIIIRockstarClient.internalMethod04594(string3, string, n2);
            }, (long)n5 * (long)n3, TimeUnit.SECONDS);
        }
    }

    public static void internalMethod01149() {
        internalField0019.set(true);
        internalField0020.set(false);
        internalField0053.incrementAndGet();
    }

    public static boolean internalMethod01150() {
        return internalField0020.compareAndSet(false, true);
    }

    public static boolean internalMethod01152() {
        return internalField0020.compareAndSet(true, false);
    }

    public static boolean internalMethod08351() {
        return internalField0020.get();
    }

    private static boolean internalMethod03331(long l) {
        while (internalField0020.get()) {
            if (internalField0019.get() || internalField0053.get() != l) {
                return false;
            }
            try {
                Thread.sleep(150L);
            }
            catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return !internalField0019.get() && internalField0053.get() == l;
    }

    private static String internalMethod02392(String string, Set<String> set, int n) {
        String string2;
        String string3;
        String string4 = string3 = string == null ? "Bot" : string.replaceAll("[^A-Za-z0-9_]", "");
        if (string3.isBlank()) {
            string3 = "Bot";
        }
        String string5 = String.valueOf(n % 1000);
        int n2 = 16 - string3.length();
        if (n2 <= string5.length()) {
            string3 = string3.substring(0, Math.max(1, 16 - string5.length() - 1));
            n2 = 16 - string3.length();
        }
        int n3 = Math.max(1, n2 - string5.length());
        int n4 = 0;
        while (set.contains(string2 = string3 + BotSpawnCoordinator.internalMethod03260(n3) + string5) && ++n4 < 25) {
        }
        return string2;
    }

    private static String internalMethod03260(int n) {
        String string = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder stringBuilder = new StringBuilder(n);
        for (int i = 0; i < n; ++i) {
            stringBuilder.append(string.charAt(internalField0858.nextInt(string.length())));
        }
        return stringBuilder.toString();
    }
}

