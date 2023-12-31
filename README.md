Utility Spigot plugin that provides the most common & useful features for plugin development to reduce duplicated code.

# About
The main purpose of **NexEngine** is to provide abilities for more easier plugin creation and better maintaining. And most importantly, to stay lightweight and well performed.

**List of Features:**
* Internal command & permission registration (without using plugin.yml).
* **Command API** to create commands with multiple levels of sub-commands and custom flags \[-f\].
* **Config API** with auto-generated fields and options & custom **FileConfiguration** class with some useful methods.
* **Database API** to store plugin & user data with **SQLite** and **MySQL** support. Including utility classes for building SQL queries, columns and tables.
* **Editor API** to handle player's chat / command input when prompted (used in GUI Editors mostly).
* **Language API** to handle multiple language configs and great message customization (JSON, action bar, titles, sounds, prefix).
* **Manager API** to create "manager" classes and event listeners.
* **Menu API** to create customizable flexible GUIs programatically or from config files.
* **Placeholder API** to create and handle internal placeholders and replace them in plugin texts/messages very fast instead of regular Java `.replace`.
* **Vault Integration** to obtain server's economy & permissions datas.
* **JSON Parser** custom utility to create & parse messages with multiple JSON components, such as: Hover, Run Command, Show Item, etc.
* **Wrappers** a few wrapper classes over Spigot ones for easier creation and usage.
* **Utilities** a lot of classes with different utilities, such as Location, Item, String, File, Number, Player, Time, Array and others.
* **Regex API** custom `CharSequence` implementation to prevent plugin / server stutter of unoptimized regex expressions after specified timeout.
* **Random API** custom random algorhythm with useful methods to roll chances, weights, or just numbers.
* **TriFunction** function class.

# Performance
**NexEngine**, itself, uses almost no server resources, because there is no events, tasks, GUIs, commands, etc. If you see it in your spark timings near the top, then some plugin that utilizes NexEngine is either bad designed or misuses the API.

# Paper Version

Akiranya fork, that allows to use all Paper's features.

https://github.com/Akiranya/NexEngine-paper
