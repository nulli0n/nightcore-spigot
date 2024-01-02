**nightcore** is updated version of the [NexEngine](https://github.com/nulli0n/NexEngine-spigot) utility plugin. Changes are unnoticeable for regular users. Almost all plugin's data is compatible, except [Text Formations](https://github.com/nulli0n/nightcore-spigot/wiki/Text-Formation).

**Technical Changes:**
- JAR size is increased due to legacy objects, so users can use old styled data for a while.
- Reduced RAM usage by various objects.
- Optimized and improved code where possible.
- Prepare to use Spigot TextComponents where possible with custom text + tags parser.
- Getting rid of unnecessary regex usage (Tab Completer, Text Parsing, Language Messages, Command Flags).
- Many packages, classes and methods renamed/relocated so they makes more sense.
- Removed useless code.

**Features:**
* Internal command & permission registration (without usage of plugin.yml).
* Easily create plugin commands with support of sub-commands and custom flags \[-f\].
* Easily manage plugin configurations with auto-generated config files, and a single method to read, write and add configuration key-value.
* Easily manage plugin language with auto-generated lang files, support for multiple language configs, and great message customization (JSON, action bar, titles, sounds, prefix).
* Easily create custom GUIs with auto-generated config files and flexible menu items.
* Easily manage SQL data with query and column builders.
* Various wrappers for better handling bukkit and java objects.
* Utility classes with a wide set of frequently used methods.
* Custom text & tag parser. Get rid of those 'magic' colors: `&1&2&3`.
* Improved `Random` class with better random generation.
