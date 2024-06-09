package su.nightexpress.nightcore.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.NightDataPlugin;
import su.nightexpress.nightcore.database.DatabaseConfig;
import su.nightexpress.nightcore.language.LangManager;
import su.nightexpress.nightcore.util.text.tag.Tags;

import java.util.Locale;

public class PluginDetails {

    private final String   name;
    private final String   prefix;
    private final String[] commandAliases;
    private final String   language;

    private DatabaseConfig databaseConfig;

    private Class<?> configClass;
    private Class<?> langClass;
    private Class<?> permissionsClass;

    public PluginDetails(
        @NotNull String name,
        @NotNull String prefix,
        @NotNull String[] commandAliases,
        @NotNull String language
    ) {
        this.name = name;
        this.prefix = prefix;
        this.commandAliases = commandAliases;
        this.language = language.toLowerCase();
    }

    @NotNull
    public static PluginDetails create(@NotNull String name, @NotNull String[] commandAliases) {
        String prefix = Tags.LIGHT_YELLOW.enclose(Tags.BOLD.enclose(name)) + Tags.DARK_GRAY.enclose(" » ") + Tags.GRAY.getBracketsName();
        String language = Locale.getDefault().getLanguage();

        return new PluginDetails(name, prefix, commandAliases, language);
    }

    @NotNull
    public static PluginDetails read(@NotNull NightCorePlugin plugin) {
        FileConfig config = plugin.getConfig();
        PluginDetails defaults = plugin.getDetails();

        String pluginName = ConfigValue.create("Plugin.Name", defaults.getName(),
            "Localized plugin name. It's used in messages and with internal placeholders.")
            .read(config);

        String pluginPrefix = ConfigValue.create("Plugin.Prefix", defaults.getPrefix(),
            "Plugin prefix. Used in messages."
        ).read(config);

        String[] commandAliases = ConfigValue.create("Plugin.Command_Aliases", defaults.getCommandAliases(),
            "Command names that will be registered as main plugin commands.",
            "Do not leave this empty. Split multiple names with a comma.")
            .read(config);

        String languageCode = ConfigValue.create("Plugin.Language", defaults.getLanguage(),
            "Sets the plugin language.",
            "Basically it tells the plugin to use certain messages config from the '" + LangManager.DIR_LANG + "' sub-folder.",
            "If specified language is not available, default one (English) will be used instead.",
            "[Default is System Locale]")
            .read(config);

        DatabaseConfig dataConfig = null;
        if (plugin instanceof NightDataPlugin<?>) {
            plugin.info("Read database configuration...");
            dataConfig = DatabaseConfig.read(plugin);
        }

        return new PluginDetails(pluginName, pluginPrefix, commandAliases, languageCode)
            .setDatabaseConfig(dataConfig)
            .setConfigClass(defaults.getConfigClass())
            .setLangClass(defaults.getLangClass())
            .setPermissionsClass(defaults.getPermissionsClass());
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getPrefix() {
        return prefix;
    }

    @NotNull
    public String[] getCommandAliases() {
        return commandAliases;
    }

    @NotNull
    public String getLanguage() {
        return language;
    }

    @Nullable
    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    @NotNull
    public PluginDetails setDatabaseConfig(@Nullable DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
        return this;
    }

    @Nullable
    public Class<?> getConfigClass() {
        return configClass;
    }

    public PluginDetails setConfigClass(@Nullable Class<?> configClass) {
        this.configClass = configClass;
        return this;
    }

    @Nullable
    public Class<?> getLangClass() {
        return langClass;
    }

    public PluginDetails setLangClass(@Nullable Class<?> langClass) {
        this.langClass = langClass;
        return this;
    }

    @Nullable
    public Class<?> getPermissionsClass() {
        return permissionsClass;
    }

    public PluginDetails setPermissionsClass(@Nullable Class<?> permissionsClass) {
        this.permissionsClass = permissionsClass;
        return this;
    }
}
