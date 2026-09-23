package wtf.wyvern.utility.game.other;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * База цен аукциона: локальный кэш на диске + синхронизация с облаком.
 *
 * Инстанс один на клиент - иначе два модуля держали бы два независимых кэша
 * и перетирали один и тот же файл.
 */
public final class AuctionDatabase {

    private static final long SAVE_INTERVAL_S = 5L;

    private static volatile AuctionDatabase instance;

    private final Map<String, Long> prices = new ConcurrentHashMap<>();
    private final AtomicBoolean dirty = new AtomicBoolean(false);
    private final AuctionCloud cloud;

    private final ScheduledExecutorService saver =
            Executors.newSingleThreadScheduledExecutor(runnable -> {
                Thread thread = new Thread(runnable, "wyvern-auction-db");
                thread.setDaemon(true);
                return thread;
            });

    public static AuctionDatabase getInstance() {
        AuctionDatabase local = instance;
        if (local == null) {
            synchronized (AuctionDatabase.class) {
                local = instance;
                if (local == null) {
                    local = new AuctionDatabase();
                    instance = local;
                }
            }
        }
        return local;
    }

    private AuctionDatabase() {
        load();

        AuctionCloudConfig config = AuctionCloudConfig.load();
        cloud = new AuctionCloud(config);
        if (config.enabled()) {
            cloud.start(this::mergeRemote);
            // Пуш срабатывает только на изменение цены, а загруженное с диска уже лежит в
            // кэше и изменением не считается. Без этой выгрузки база, накопленная до
            // включения облака, никогда бы туда не попала.
            if (!prices.isEmpty()) {
                cloud.schedulePush(new HashMap<>(prices));
            }
        }

        saver.scheduleAtFixedRate(() -> {
            if (dirty.compareAndSet(true, false)) {
                saveNow();
            }
        }, SAVE_INTERVAL_S, SAVE_INTERVAL_S, TimeUnit.SECONDS);
    }

    private void mergeRemote(Map<String, Long> remote) {
        boolean changed = false;
        for (Map.Entry<String, Long> entry : remote.entrySet()) {
            Long current = prices.get(entry.getKey());
            if (current == null || !current.equals(entry.getValue())) {
                prices.put(entry.getKey(), entry.getValue());
                changed = true;
            }
        }
        if (changed) {
            dirty.set(true);
        }
    }

    /** Кладёт цену за штуку. Возвращает true, если значение действительно изменилось. */
    public boolean putPrice(ItemStack stack, long totalPrice) {
        String key = itemKey(stack);
        if (key == null) {
            return false;
        }
        long perItem = totalPrice / Math.max(1, stack.getCount());
        Long previous = prices.put(key, perItem);
        if (previous != null && previous == perItem) {
            return false;
        }
        dirty.set(true);
        cloud.schedulePush(Map.of(key, perItem));
        return true;
    }

    /** Пакетная запись: один пуш в облако на всю пачку вместо запроса на каждый лот. */
    public int putPrices(Map<String, Long> perItemPrices) {
        Map<String, Long> changed = new HashMap<>();
        for (Map.Entry<String, Long> entry : perItemPrices.entrySet()) {
            Long previous = prices.put(entry.getKey(), entry.getValue());
            if (previous == null || !previous.equals(entry.getValue())) {
                changed.put(entry.getKey(), entry.getValue());
            }
        }
        if (!changed.isEmpty()) {
            dirty.set(true);
            cloud.schedulePush(changed);
        }
        return changed.size();
    }

    public Long getPrice(ItemStack stack) {
        String key = itemKey(stack);
        return key == null ? null : prices.get(key);
    }

    public Long getPriceByKey(String key) {
        return key == null ? null : prices.get(key);
    }

    public int size() {
        return prices.size();
    }

    public boolean isCloudOnline() {
        return cloud.isOnline();
    }

    public String getCloudError() {
        return cloud.getLastError();
    }

    /**
     * Ключ предмета: id + кастомное имя + зачарования. Именно этой тройкой на фантайме
     * различаются лоты одного и того же предмета с разной ценой.
     */
    public static String itemKey(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        String id = Registries.ITEM.getId(stack.getItem()).toString();

        String customName = "";
        Text name = stack.getCustomName();
        if (name != null) {
            customName = normalizeName(stripFormatting(name.getString()));
        }

        List<String> enchantments = new ArrayList<>();
        ItemEnchantmentsComponent component = EnchantmentHelper.getEnchantments(stack);
        if (component != null && !component.isEmpty()) {
            for (var entry : component.getEnchantmentEntries()) {
                enchantments.add(entry.getKey().getIdAsString() + ":" + entry.getIntValue());
            }
            Collections.sort(enchantments);
        }

        return id + "|" + customName + "|" + String.join(",", enchantments);
    }

    /**
     * Ключ рыночной группы - все лоты одного предмета без учёта зачарований и имени.
     *
     * Точный ключ для оценки рынка не годится: с зачарованиями почти каждый лот уникален,
     * выборка вырождается в один элемент. Кастомное имя тоже не подходит - продавцы пишут
     * туда свои подписи и дробят группу на десятки кусков.
     */
    public static String groupKey(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return "grp|" + Registries.ITEM.getId(stack.getItem());
    }

    /** Возвращает цену лота из лора или -1, если строки с ценой нет. */
    public static long parsePriceFromLore(ItemStack stack) {
        LoreComponent lore = stack.get(DataComponentTypes.LORE);
        if (lore == null) {
            return -1;
        }
        for (Text line : lore.lines()) {
            String raw = stripFormatting(line.getString());
            String lower = raw.toLowerCase(Locale.ROOT);
            if (!lower.contains("цен") && !lower.contains("price") && !raw.contains("$")) {
                continue;
            }

            int colon = raw.indexOf(':');
            String numberPart = colon >= 0 ? raw.substring(colon + 1) : raw;

            StringBuilder digits = new StringBuilder();
            boolean started = false;
            for (int i = 0; i < numberPart.length(); i++) {
                char c = numberPart.charAt(i);
                if (c >= '0' && c <= '9') {
                    digits.append(c);
                    started = true;
                } else if (started) {
                    // Разделители разрядов пропускаем, на любом другом символе число кончилось.
                    if (c == ' ' || c == ',' || c == '.' || c == ' ' || c == '\'') {
                        continue;
                    }
                    break;
                }
            }
            if (!digits.isEmpty()) {
                try {
                    return Long.parseLong(digits.toString());
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return -1;
    }

    public static String itemDisplayName(ItemStack stack) {
        String name = stripFormatting(stack.getName().getString());
        return name.replaceAll("\\[[^\\]]*\\]", "").replaceAll("\\s+", " ").trim();
    }

    private static String normalizeName(String name) {
        if (name == null) {
            return "";
        }
        return name.replaceAll("\\[[^\\]]*\\]", "")
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    private static String stripFormatting(String text) {
        return text.replaceAll("(?i)[§&][0-9a-fk-or]", "");
    }

    private static Path databasePath() {
        return FabricLoader.getInstance().getConfigDir().resolve("wyvern").resolve("auction_db.json");
    }

    private static Gson gson() {
        return GsonHolder.GSON;
    }

    private static final class GsonHolder {
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    }

    private void load() {
        Path path = databasePath();
        if (!Files.exists(path)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            Map<String, Long> loaded = gson().fromJson(reader, new TypeToken<Map<String, Long>>() {}.getType());
            if (loaded != null) {
                prices.putAll(loaded);
            }
        } catch (Exception ignored) {
        }
    }

    private void saveNow() {
        try {
            Map<String, Long> snapshot = new HashMap<>(prices);
            Path path = databasePath();
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                gson().toJson(snapshot, writer);
            }
        } catch (Exception ignored) {
        }
    }
}
