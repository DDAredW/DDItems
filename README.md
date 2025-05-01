# DDItems | Создавай уникальные предметы
# Создавай уникальные предметы которые активируются на ПКМ и выдают эффекты. при активации могут выполнить любую команды или несколько команд от имени консоля/игрока (создавайте любые предметы какие только позволяют ваши фантазии)
# С поддержкой Hex (&#rrggbb) цветов
Зависимости
Ядро - Spigot/Paper
Java - 1.8 и выше

ФУНКЦИИ - 

Поддержка Hex цветов (&#rrggbb)
Использование одной или несколько команд сразу в 1 предмете 
Система кулдауна на повторное использование предмета
Редактируемые соообщения
Гибкая конфигурация
Гибкая настройка предметов, начиная от имени, описания, заканчивая материалом, командами при активации, кулдаун, количество использований, звуки при активации и утрате использований предмета
Настройка звука при активации
Настройка звука когда предмет исчерпал все свои использование
Количество использоаний, после которого предмет пропадёт.


Config.yml>>>
messages:
  no_permission: "&#ff4444У вас нет прав!"
  item_not_found: "&#ff5555Такой предмет не найден!"
  player_not_found: "&#ff5555Игрок не найден!"
  cooldown: "&#ffaa00Подождите &e%time% &6сек. перед следующим использованием!"
  out_of_uses: "&#ff4444Предмет &e%item% &#ff4444был израсходован и исчез!"
  reload_success: "&#44ff44Конфигурация успешно перезагружена!"
  item_given: "&#44ff44Выдан предмет &e%item% &aигроку &e%player% &7(%amount% шт.)"

items:
  heal:
    name: "&#00ff00Свисток Исцеления"
    lore:
      - "&#ffffff⚡ Используйте, чтобы исцелиться!"
      - "&#55ff55⚡ Эффект: &aМгновенное исцеление"
      - "&f⚡ Осталось использований: &a%uses%"
    material: "BLAZE_ROD"
    commands:
      - "effect give %player% minecraft:instant_health 1"
    cooldown: 10
    uses: 3
    sound_use: "ENTITY_PLAYER_LEVELUP"
    sound_break: "ENTITY_ITEM_BREAK"

  speed:
    name: "&#00aaffСвисток Скорости"
    lore:
      - "&#ffffff⚡ Используйте, чтобы ускориться!"
      - "&#55aaff⚡ Эффект: &bСкорость II (30 сек.)"
      - "&f⚡ Осталось использований: &a%uses%"
    material: "BLAZE_ROD"
    commands:
      - "effect give %player% minecraft:speed 30 1"
    cooldown: 10
    uses: 3
    sound_use: "ENTITY_PLAYER_LEVELUP"
    sound_break: "ENTITY_ITEM_BREAK"

  strength:
    name: "&#ff4444Свисток Силы"
    lore:
      - "&#ffffff⚡ Используйте, чтобы стать сильнее!"
      - "&#ff5555⚡ Эффект: &cСила II (30 сек.)"
      - "&f⚡ Осталось использований: &a%uses%"
    material: "BLAZE_ROD"
    commands:
      - "effect give %player% minecraft:strength 30 1"
    cooldown: 10
    uses: 3
    sound_use: "ENTITY_PLAYER_LEVELUP"
    sound_break: "ENTITY_ITEM_BREAK"
