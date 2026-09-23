package wtf.wyvern.client.modules.impl.misc;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.utility.game.other.AuctionDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static wtf.wyvern.utility.interfaces.IMinecraft.mc;

@ModuleAnnotation(
        name = "AuctionHelper",
        category = Category.MISC,
        description = "Сканирует цены аукциона и продаёт со скидкой по бинду"
)
public final class AuctionHelper extends Module {
    public static final AuctionHelper INSTANCE = new AuctionHelper();

    // Экран пересканируется по изменению содержимого, а не по таймеру: страницы /ah
    // листаются быстрее секунды, и на таймере подсветка отставала на страницу.
    private static final long SCAN_THROTTLE_MS = 120L;
    private static final long SEARCH_COOLDOWN_MS = 10_000L;

    public static final byte TIER_BEST = 1;
    public static final byte TIER_OK = 2;

    private final BooleanSetting sell = new BooleanSetting("Sell", false);
    private final SliderSetting discount = new SliderSetting("Discount", 5.0F, 1.0F, 50.0F, 1.0F, "%");
    // Порог выгодной сделки: лот дешевле рынка на столько процентов красится зелёным.
    private final SliderSetting deal = new SliderSetting("Deal", 20.0F, 5.0F, 90.0F, 1.0F, "%");
    private final BooleanSetting chatLogs = new BooleanSetting("ChatLogs", true);

    private final Map<Integer, Byte> slotHighlight = new ConcurrentHashMap<>();

    private long lastScanAt;
    private int lastScreenHash;

    private long lastSearchAt;
    private String lastSearchKey;

    private String pendingSellKey;
    private String pendingSellName;

    public Map<Integer, Byte> getSlotHighlight() {
        return slotHighlight;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        AuctionDatabase database = AuctionDatabase.getInstance();
        log("Открой &e/ah&7 для сканирования. В базе: &a" + database.size() + " &7предметов.");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        slotHighlight.clear();
        lastScreenHash = 0;
        pendingSellKey = null;
        pendingSellName = null;
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc == null || mc.player == null) {
            return;
        }

        // Sell - разовое действие по бинду, а не режим: сразу гасим тумблер.
        if (sell.isEnabled()) {
            sell.setEnabled(false);
            if (mc.currentScreen == null && mc.player.networkHandler != null) {
                executeSell();
            }
            return;
        }

        if (!(mc.currentScreen instanceof HandledScreen<?> screen)) {
            // Подсветка привязана к slot.id, а он переиспользуется следующим экраном.
            // Не сбросив её здесь, мы бы красили чужие предметы.
            if (!slotHighlight.isEmpty()) {
                slotHighlight.clear();
            }
            lastScreenHash = 0;
            return;
        }

        long now = System.currentTimeMillis();
        if (now - lastScanAt < SCAN_THROTTLE_MS) {
            return;
        }

        int hash = screenHash(screen.getScreenHandler());
        if (hash == lastScreenHash) {
            return;
        }

        // Содержимое сменилось - старая подсветка относится к предметам, которых уже нет.
        slotHighlight.clear();
        lastScanAt = now;
        lastScreenHash = hash;
        scanScreen(screen);
    }

    private void executeSell() {
        ItemStack held = mc.player.getMainHandStack();
        if (held == null || held.isEmpty()) {
            log("&cВозьми предмет в руку");
            return;
        }

        AuctionDatabase database = AuctionDatabase.getInstance();
        Long perItem = database.getPrice(held);
        if (perItem != null && perItem > 0) {
            int count = Math.max(1, held.getCount());
            long market = perItem * count;
            long salePrice = discountedPrice(market);
            mc.player.networkHandler.sendChatCommand("ah sell " + salePrice);
            log("Продаю &fx" + count + " &7за &a" + formatPrice(salePrice)
                    + " &7(скидка &e" + (int) discount.getCurrent() + "%&7, рынок &f" + formatPrice(market)
                    + " &7= &f" + formatPrice(perItem) + "&7/шт)");
            pendingSellKey = null;
            pendingSellName = null;
            return;
        }

        String itemName = AuctionDatabase.itemDisplayName(held);
        String key = AuctionDatabase.itemKey(held);
        long now = System.currentTimeMillis();
        // Не спамим сервер поиском одного и того же предмета, пока прошлый запрос ещё актуален.
        if (key != null && key.equals(lastSearchKey) && now - lastSearchAt < SEARCH_COOLDOWN_MS) {
            log("&7Ищу цену, открой &e/ah&7 чтобы просканировать...");
            return;
        }
        lastSearchKey = key;
        lastSearchAt = now;
        pendingSellKey = key;
        pendingSellName = itemName;
        mc.player.networkHandler.sendChatCommand("ah search " + itemName);
        log("Цена неизвестна, ищу &e" + itemName + "&7. Открой результаты - я сам просканирую.");
    }

    private void scanScreen(HandledScreen<?> screen) {
        ScreenHandler handler = screen.getScreenHandler();
        if (handler == null) {
            return;
        }

        AuctionDatabase database = AuctionDatabase.getInstance();

        Map<String, List<Long>> pricesByKey = new HashMap<>();
        Map<Integer, Long> priceBySlot = new HashMap<>();
        Map<Integer, String> keyBySlot = new HashMap<>();
        // Точный ключ несёт зачарования, поэтому почти каждый лот уникален и сравнивать
        // его не с чем. Рынок считаем по группе - лотам того же предмета.
        Map<String, List<Long>> pricesByGroup = new HashMap<>();
        Map<Integer, String> groupBySlot = new HashMap<>();
        boolean pendingFound = false;

        for (Slot slot : handler.slots) {
            ItemStack stack = slot.getStack();
            if (stack == null || stack.isEmpty()) {
                continue;
            }
            long price = AuctionDatabase.parsePriceFromLore(stack);
            if (price <= 0) {
                continue;
            }
            String key = AuctionDatabase.itemKey(stack);
            if (key == null) {
                continue;
            }

            long perItem = price / Math.max(1, stack.getCount());
            String group = AuctionDatabase.groupKey(stack);
            priceBySlot.put(slot.id, perItem);
            keyBySlot.put(slot.id, key);
            groupBySlot.put(slot.id, group);
            pricesByKey.computeIfAbsent(key, ignored -> new ArrayList<>()).add(perItem);
            pricesByGroup.computeIfAbsent(group, ignored -> new ArrayList<>()).add(perItem);
            if (pendingSellKey != null && pendingSellKey.equals(key)) {
                pendingFound = true;
            }
        }

        // Эталон снимаем ДО записи в базу: иначе рыночной ценой станет цена этого же лота
        // и дешёвый предмет никогда не окажется дешевле "рынка".
        Map<String, Long> marketByGroup = new HashMap<>();
        for (String group : pricesByGroup.keySet()) {
            Long known = database.getPriceByKey(group);
            if (known != null && known > 0) {
                marketByGroup.put(group, known);
            }
        }

        double dealFactor = 1.0 - deal.getCurrent() / 100.0;
        Map<Integer, Byte> highlight = new HashMap<>();
        for (Map.Entry<Integer, Long> entry : priceBySlot.entrySet()) {
            long price = entry.getValue();
            String group = groupBySlot.get(entry.getKey());
            List<Long> samples = pricesByGroup.get(group);
            if (samples == null || samples.isEmpty()) {
                continue;
            }

            // Накопленный рынок надёжнее одной страницы, но пока его нет - хоть какой-то
            // ориентир лучше, чем не подсветить ничего.
            Long market = marketByGroup.get(group);
            long reference = market != null ? market : median(samples);
            if (reference <= 0) {
                continue;
            }

            if (price <= reference * dealFactor) {
                highlight.put(entry.getKey(), TIER_BEST);
            } else if (price < reference) {
                highlight.put(entry.getKey(), TIER_OK);
            }
        }
        slotHighlight.putAll(highlight);

        // Рынок - медиана, а не цена каждого лота: один демпингующий продавец иначе
        // обнулял бы эталон для всех остальных.
        Map<String, Long> batch = new HashMap<>();
        for (Map.Entry<String, List<Long>> entry : pricesByKey.entrySet()) {
            batch.put(entry.getKey(), median(entry.getValue()));
        }
        for (Map.Entry<String, List<Long>> entry : pricesByGroup.entrySet()) {
            batch.put(entry.getKey(), median(entry.getValue()));
        }
        database.putPrices(batch);

        if (pendingFound) {
            Long found = database.getPriceByKey(pendingSellKey);
            log("&aЦена найдена&7 для &e" + pendingSellName + "&7: &f"
                    + (found == null ? "?" : formatPrice(found)) + "&7. Закрой меню и нажми Sell.");
            pendingSellKey = null;
            pendingSellName = null;
        }
    }

    // Пересканируем экран только когда его содержимое поменялось - иначе каждую страницу
    // /ah пришлось бы разбирать по таймеру впустую.
    private int screenHash(ScreenHandler handler) {
        if (handler == null) {
            return 0;
        }
        int hash = 0;
        for (Slot slot : handler.slots) {
            ItemStack stack = slot.getStack();
            if (stack == null || stack.isEmpty()) {
                continue;
            }
            hash ^= slot.id * 31 + stack.getItem().hashCode() * 1000003 + stack.getCount();
        }
        return hash;
    }

    private static long median(List<Long> samples) {
        List<Long> sorted = new ArrayList<>(samples);
        Collections.sort(sorted);
        return sorted.get(sorted.size() / 2);
    }

    private long discountedPrice(long marketPrice) {
        double percent = discount.getCurrent() / 100.0;
        return Math.max(1L, Math.round(marketPrice * (1.0 - percent)));
    }

    private static String formatPrice(long price) {
        return String.format("%,d", price).replace(',', '.');
    }

    private void log(String message) {
        if (!chatLogs.isEnabled() || mc == null) {
            return;
        }
        MutableText line = Text.literal("AuctionHelper").setStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true))
                .append(Text.literal(" | ").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY).withBold(false)))
                .append(Text.literal(message.replace('&', '§')).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withBold(false)));
        mc.execute(() -> {
            if (mc.inGameHud != null && mc.inGameHud.getChatHud() != null) {
                mc.inGameHud.getChatHud().addMessage(line);
            } else if (mc.player != null) {
                mc.player.sendMessage(line, false);
            }
        });
    }
}
