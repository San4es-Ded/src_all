# GUI & HUD — как дополнять проект

Документ для ИИ и людей: как добавлять и дорабатывать интерфейс (меню/экраны) и HUD в PrismVisuals.
Правило номер один: **не изобретать своё** — повторять приёмы, которые уже есть в проекте.

## 0. Общие конвенции

- Только client-side код: `src/client/java/ru/prism/...`, ресурсы — `src/client/resources/assets/client/...`.
- Брендинг: всё новое (элементы, ассеты, шрифты, пути, ключи) — сразу под брендом prism; никаких имён и путей других клиентов (Nightix, Pulse, arbuz, rockstar и т.п.).
- Все пользовательские строки — **по-русски**, стиль как у существующих модулей (неформальный).
  Новые строки добавлять в `ru/prism/lang/Translations.java` (ключ = русская строка), UI рисует их через `Lang.tr(...)`.
- Никакого хардкода цветов. Только:
  - `ru/prism/theme/ThemeColor` — `getVisualColor()`, `getHudColor()`, `getTextColor()`, `getBackgroundColor()`,
    `getOutlineColor()`, `getSeparatorColor()`, `getOpacity()` (базовая альфа худа), `byIndex(int)`;
  - `ru/prism/utils/colors/ColorUtil` — `getColor(r,g,b,a)`, `getColor(brightness, alpha)`, `replAlpha(color, alpha)`,
    `multAlpha`, `multDark`, `overCol(base, tint, t)`, `background()`, `client()`, `getClientColor(1)`, `getHealthColor(hp, max)`.
- Масштаб HUD: в каждом элементе каждый кадр заново `float S = InterFace.getInstance().sizeHud.getValue();`
  и все размеры/отступы множить на `S` (статические константы тоже пересчитывать в рендере).
- Анимации:
  - hover/select: `ru/prism/utils/animation/satoshi/Animation` — `new EaseInOutQuad(ms, 1)`,
    `setDirection(Direction.FORWARDS / BACKWARDS)`, `getOutput()`. Кэшировать по ключу в `Map<String, Animation>`
    (пример ключа: `"h:" + i`).
  - простые: `ru/prism/utils/animation/Animation` + `Easings` (`SINE_OUT`, `BACK_OUT`, `LINEAR`) —
    `update()`, `run(target, seconds, easing[, safe])`, `get()`.
- 2D-рисование: `ru/prism/utils/render/RenderUtil`:
  - `RenderUtil.Render2D.rect(x, y, w, h, color[, radius])`, `gradientRect(x, y, w, h, int[] colors, radius)`,
    `clientRect(...)`, `outline(...)`, `glassOutline(...)`;
    `RenderUtil.Render2D.glow(...)` — **заглушка**, не использовать;
  - `RenderUtil.Blur.glass(x, y, w, h, alpha, radius, tintColor, distortion, waveSize, edgeLight, shine)`
    (типовой тинт: `ColorUtil.replAlpha(ColorUtil.background(), alphaHUD * a)`), `RenderUtil.Blur.blur(x, y, w, h, alpha, radius, tint)`;
  - `RenderUtil.Images.texture(Identifier, x, y, w, h[, color][, radius][, u0, v0, u1, v1][, color, radius])`.
- Текст: `ru/prism/utils/render/font/Fonts` — `sf_regular`, `sf_medium`, `sf_bold`, `icon`, `category`, `gui`, `prism_2`;
  методы `draw(text, x, y, size, color)`, `drawCentered(...)`, `drawFadingText(text, x, y, maxWidth, color, size)`,
  `getWidth(text, size)`, `getHeight(size)`.
- Иконки-предметы: `ru/prism/utils/render/ItemRender` — `drawItemWithContext(ctx, stack, x, y, scale, alpha)`,
  `drawItemCenteredWithContext(ctx, stack, cx, cy, scale, alpha)`.
- Звуки: `GuiSounds` (`chipMulti(bool)`, `expand(bool)`, `button()` и т.п.).
- 3D в мире: только `VertexConsumer` + `RenderLayer`/`RenderPipeline` (как в модулях рендера).
  `Tessellator`/`BufferBuilder`/`RenderSystem` в проекте **не используются**.
- Файлы писать в UTF-8 **без BOM**. PowerShell-скрипты с кириллицей запускать только BOM-копией.
- Сборка: `gradlew build --offline --console=plain` в корне проекта, затем jar
  `build/libs/prism-1.0-SNAPSHOT.jar` → `%APPDATA%\.tlauncher\legacy\Minecraft\game\mods\prism-1.0-SNAPSHOT.jar`.

## 1. Как добавить HUD-элемент (InterFace)

HUD живёт в модуле `ru/prism/module/impl/display/InterFace` (`@ModuleInfo name = "Inter Face"`).
У каждого элемента есть свой `DragSetting` (позиция) и тумблер в `MultiBooleanSetting element` («Элементы»).

1) Создать класс `src/client/java/ru/prism/module/impl/display/interfaceimpl/<Name>.java`:

```java
public class MyElement implements element {
    @Override
    public void onRender(DragSetting drag, InterFace interFace) {
        // рисуем
    }

    // Нужен DrawContext (предметы/gui-текстуры)? Добавить перегрузку,
    // а двухаргументную оставить пустой (так сделано в Notify/Potions/TargetHud):
    public void onRender(DragSetting drag, InterFace interFace, EventDisplay eventDisplay) {
        // eventDisplay.getDrawContext(), eventDisplay.getPartialTicks()
    }
}
```

Правила внутри рендера:
- `float S = InterFace.getInstance().sizeHud.getValue();`
- позиция — `drag.position.x`, `drag.position.y` (тип `Vector2f`), размер выставлять в конце:
  `drag.size.set(w, h); drag.active = true;`
- если рисовать нечего — `drag.active = false; return;` (обязательно, иначе останется пустой хитбокс);
- альфа: `float a = ThemeColor.getOpacity(); if (a > 0.98F) a = 0.98F;`
  (или `interFace.alphaHUD.getValue()`), умножать на выход анимаций появления;
- фон плашек — `RenderUtil.Blur.glass(...)`, текст — `Fonts.*`, цвета — `ThemeColor`/`ColorUtil`.

2) Зарегистрировать в `InterFace.java`:
- в `element` добавить `new BooleanSetting("<Имя>", true)`;
- добавить `public DragSetting <field> = new DragSetting(this, "<Имя>", new Vector2f(x, y));`
  (при необходимости в конструкторе: `<field>.lockX = true;` — как у `notifications`);
- добавить поле элемента: `private final MyElement <field> = new MyElement();`
- в `onDisplayEvent(EventDisplay)` добавить строку по образцу:
  `if (element.getValue("<Имя>")) <field>.onRender(<drag>, this);` — или с `eventDisplay`, если нужен DrawContext;
- если элементу нужны тики/мышь — добавить вызовы в `onUpdate(EventUpdate)` и/или `onMousePress(MousePressEvent)`
  под тем же `element.getValue("<Имя>")` (как `MusicHud` и `Notify`).

3) Добавить русскую строку `<Имя>` в `lang/Translations.java` (ключ → английский вариант), если это новое слово в UI.

## 2. Как добавить новый экран (Screen)

- `public final class XScreen extends Screen implements IMinecraft`.
- В `render(DrawContext context, int mouseX, int mouseY, float delta)`:
  - привести координаты: `float scaleFix = 2F / MinecraftClient.getInstance().getWindow().getScaleFactor();`
    и работать в «пикселях интерфейса»: `mouseX /= scaleFix; mouseY /= scaleFix;`
  - обернуть рисование: `Render2D.beginOverlay(); ... Render2D.endOverlay();`
  - фон: `ScreenBlur.capture(2);` + `RenderUtil.Blur.blur(0, 0, w, h, alpha, ColorUtil.getColor(0, alpha * 0.2F));`
  - панель: `RenderUtil.Blur.blur(panelX, panelY, panelW, panelH, alpha, 8,
    ColorUtil.multAlpha(ColorUtil.multDark(ColorUtil.background(), 0.6F), alpha))` + `RenderUtil.Render2D.outline(...)`;
  - обрезка содержимого: `Scissor.enable(x, y, w, h, radius)` / `Scissor.disable()`;
  - тексты — `Fonts.*`, клики — `GuiSounds.*`.
- 3D-модели внутри экрана: образец — `ru/prism/screen/CosmeticsPreviewScreen` + `ru/prism/cosmetic/render/CosmeticPreviewRenderer`
  (свой пиксельный пайплайн, ортопроекция, depth test). Порядок: сначала 2D-фон/картинки, затем `DrawBatcher.flushPending()`,
  затем `previewRenderer.flush()` — только после этого 2D-подписи.
- Возврат: `close()` → `client.setScreen(parent)`, ESC — в `keyPressed(...)`.
- Открытие: `MinecraftClient.getInstance().setScreen(new XScreen(currentScreen));`
  (так делают кнопка «Визуализация» в `Cosmetics` и автооткрытие в `Menu.java`).

## 3. Как добавить настройку/строку в панель модуля (Menu.java)

- Настройки — `ru/prism/module/api/settings/impl/*`:
  `BooleanSetting`, `SliderSetting`, `ModeSetting`, `ColorSetting`, `MultiBooleanSetting`, `DragSetting`,
  `ButtonSetting`, `DelimiterSetting`, `BindSetting`, `StringSetting`, `ListSetting`, `BlockListSetting`, `PixelGridSetting`.
  Создаются в полях модуля: `new SliderSetting(this, "Имя", def, min, max, step).setVisible(() -> ...)`.
- Отрисовка строк настроек — в `screen/Menu.java`, цикл `for (Setting setting : f.getSettings())` в `render(...)`;
  у каждого типа свой блок (Slider, ModeSetting-чипы, MultiBoolean-чипы, Boolean, Button, Drag).
  Каждый блок наращивает `yST` (вертикальная позиция) на свою высоту × `fa * vis`.
- Клики — отдельный цикл в `mouseClicked(...)` (перетаскивание слайдера через `draggingSlider`,
  чипы Mode/MultiBoolean через `s.set(...)`/`b.set(!b.getValue())` + `GuiSounds`, `ButtonSetting.press()`).
- Высоты чипов считать через `chipsHeight(...)` и кэшировать в `chipsHeights`
  (`IdentityHashMap<Setting<?>, float[]>`), чтобы не считать заново каждый кадр.
- Лейблы/значения рисовать через `Lang.tr(...)`; имена настроек — по-русски.
- Свой виджет в редакторе — `screen/editor/*`: `SettingsPanel`, `EditorWidgets`, `EditorTheme`, `EditorButton`, `Rect`;
  готовые HUD-виджеты: `ModeSettingHud`, `BooleanSettingHud`.

## 4. Чек-лист

- [ ] имена/описания по-русски, новые строки добавлены в `Translations.java`
- [ ] цвета только `ThemeColor`/`ColorUtil`, размеры умножены на `S`
- [ ] элемент/настройка зарегистрированы (`InterFace`, `ModuleManager`)
- [ ] у DragSetting выставляются `active` и `size`
- [ ] `gradlew build --offline --console=plain` → BUILD SUCCESSFUL
- [ ] jar скопирован в `%APPDATA%\.tlauncher\legacy\Minecraft\game\mods\prism-1.0-SNAPSHOT.jar`
- [ ] пуш на GitHub — только по явной просьбе
