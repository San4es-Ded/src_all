# PrismVisuals — контекст для ИИ

## Что это
Клиентский (только client-side) мод-клиент для **Minecraft 1.21.11 на Fabric**. Визуальный клиент: косметика, HUD, визуальные эффекты, собственный интерфейс. Без читов и PvP-функционала. Интерфейс на русском. PrismVisuals.

## Стек
- Java 21, Fabric (client-only), Yarn mappings 1.21.11.
- Исходники: `src/client/java/ru.prism`, ресурсы: `src/client/resources`.
- Точка входа: `ru.prism.Client#onInitializeClient`.
- Собственный UI и рендер: `screen/Menu.java` (+ ClickGui), MSDF-шрифты (`utils/render/font`), рендер-утилиты (`utils/render`), тема (`theme/ThemeColor`).
- Модули: `module/api/ModuleManager`, `Module`, `Category` (RENDER / OTHER), настройки (`ModeSetting`, `SliderSetting`, `BooleanSetting`, `ColorSetting`, `MultiBooleanSetting`, `DragSetting`, `ButtonSetting`, `DelimiterSetting`).

## Что есть
- HUD-модуль `InterFace`: watermark, информация, keybinds, potions, музыка, ArrayList, уведомления, Target HUD.
- Визуальные модули: Target Esp (много режимов), Particles, Trails, World Cubes, Trajectories, FireFlies, Svetoch, ChinaHat, Jump Circle/Cube, Kill Effect, Hands, Glass Hands/Block, Shader Sky, Fog Blur, World Tweaks, Gamma, CrossHair, Totem Ghost, Scan World и др.
- Косметика (`module/impl/render/Cosmetics`): 63 косметики (плащи, крылья, спины, петы, шапки) на geo-моделях Bedrock (`ru.prism.cosmetic.geo`), загрузчик `CosmeticLoader`, рендер `CosmeticRenderer` / `CosmeticFeatureRenderer`, GUI-превью `screen/CosmeticsPreviewScreen` + `CosmeticPreviewRenderer`.
- Конфиг и настройки пользователя: `config/ConfigManager`.

## Сборка и установка
- Сборка: `gradlew build --offline --console=plain` в корне проекта.
- Артефакт: `build/libs/prism-1.0-SNAPSHOT.jar`.
- Установка (TLauncher): копировать jar в `%APPDATA%\.tlauncher\legacy\Minecraft\game\mods\prism-1.0-SNAPSHOT.jar`.

## Конвенции
- Только client-side код; серверные API не нужны.
- Новый модуль — в `module/impl/<категория>`, регистрация в `ModuleManager.init()`.
- Настройки создаются как field initializers; имена настроек и описания модулей — на русском, в неформальном стиле проекта.
- Рендер: не использовать Tessellator/BufferBuilder/RenderSystem напрямую — только принятый в проекте стек (`VertexConsumer`, `RenderLayer`, `RenderPipeline`, собственные пайплайны и шейдеры).
- Файлы — UTF-8 без BOM.
- Брендинг: всё новое (модули, ассеты, шрифты, конфиги, пути, миксин-члены, косметика, ключи шейдеров) — сразу под брендом prism; никаких следов и имён других клиентов (Nightix, Pulse, arbuz, rockstar и т.п.). При переносе чего-либо из сторонних исходников — сразу переименовывать: пути `C:/prism/...`, префиксы миксинов `prism$`, ассеты и шрифты `prism*` или нейтральные имена, косметика `prism_*`.

## Репозиторий
- https://github.com/San4es-Ded/PrismVisuals.git — ветка `main`.
