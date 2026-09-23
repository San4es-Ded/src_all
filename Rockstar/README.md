# NoName Client 1.21.11

Чистый исходный проект NoName Client для Minecraft `1.21.11`.

## Окружение

- Minecraft `1.21.11`
- Fabric Loader `0.19.3`
- Fabric API `0.141.6+1.21.11`
- Yarn `1.21.11+build.6`
- Java `21`
- Fabric Loom `1.14.10`

## Что исправлено

- завершён перенос клиента и всех 112 модулей на Minecraft 1.21.11;
- сохранены кастомный рендер, шейдеры, HUD и визуальные модули;
- конфиги переведены на локальное хранилище `run/Rockstar/configs`;
- команды `.cfg save/load/list/remove/rename/duplicate/undo/reset` больше не зависят от RockNet;
- убраны создание и загрузка неизвестного облачного конфига `default`;
- добавлены резервные копии конфигов и локальный автосейв активного конфига.

На первом запуске конфиг не создаётся и не загружается автоматически. После
ручного `.cfg save <имя>` или `.cfg load <имя>` выбранный локальный конфиг
становится активным; если включён Auto Save Configs, дальнейшие изменения
сохраняются в него автоматически.

## Сборка

```powershell
.\gradlew.bat clean build --no-daemon
```

Готовый JAR создаётся в `build/libs/`.

## Структура модулей

| Пакет | Категория | Модулей |
| --- | --- | ---: |
| `rockstar.modules.combat` | Combat | 18 |
| `rockstar.modules.movement` | Movement | 13 |
| `rockstar.modules.visual` | Visual | 25 |
| `rockstar.modules.player` | Player | 29 |
| `rockstar.modules.other` | Other | 27 |

В репозитории оставлены только исходники, Gradle Wrapper и необходимые ресурсы.
Каталоги `run`, `build`, `.gradle`, IDE-файлы, логи и временные результаты в
чистую выдачу не входят.
