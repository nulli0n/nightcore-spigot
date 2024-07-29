package su.nightexpress.nightcore.database;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.stream.Stream;

public class DatabaseConfig {

    private final int          saveInterval;
    private final int          syncInterval;
    private final DatabaseType databaseType;
    private final String       tablePrefix;
    private final boolean      purgeEnabled;
    private final int          purgePeriod;

    private final String username;
    private final String password;
    private final String host;
    private final String database;
    private final String urlOptions;

    private final String filename;

    public DatabaseConfig(
        int saveInterval,
        int syncInterval,
        @NotNull DatabaseType databaseType,
        @NotNull String tablePrefix,
        boolean purgeEnabled,
        int purgePeriod,

        @NotNull String username,
        @NotNull String password,
        @NotNull String host,
        @NotNull String database,
        @NotNull String urlOptions,

        @NotNull String filename
    ) {
        this.saveInterval = saveInterval;
        this.syncInterval = syncInterval;
        this.databaseType = databaseType;
        this.tablePrefix = tablePrefix;
        this.purgeEnabled = purgeEnabled;
        this.purgePeriod = purgePeriod;

        this.username = username;
        this.password = password;
        this.host = host;
        this.database = database;
        this.urlOptions = urlOptions;

        this.filename = filename;
    }

    @NotNull
    public static DatabaseConfig read(@NotNull NightCorePlugin plugin) {
        String defPrefix = StringUtil.lowerCaseUnderscore(plugin.getName());
        return read(plugin.getConfig(), defPrefix);
    }

    @NotNull
    public static DatabaseConfig read(@NotNull FileConfig config, @NotNull String defaultPrefix) {
        return read(config, defaultPrefix, "data.db");
    }

    @NotNull
    public static DatabaseConfig read(@NotNull FileConfig config, @NotNull String defaultPrefix, @NotNull String defFileName) {
        String path = "Database.";

        DatabaseType databaseType = ConfigValue.create(path + "Type", DatabaseType.class, DatabaseType.SQLITE,
            "Sets database type.",
            "Available values: " + String.join(",", Stream.of(DatabaseType.values()).map(Enum::name).toList()))
            .read(config);

        int saveInterval = ConfigValue.create(path + "Auto_Save_Interval", 20,
            "Sets how often (in minutes) plugin data of online players will be saved to the database.",
            "Set to '-1' to disable.")
            .read(config);

        int syncInterval = ConfigValue.create(path + "Sync_Interval", -1,
            "Sets how often (in seconds) plugin data will be fetched and loaded from the remote database.",
            "Useless for " + DatabaseType.SQLITE.name() + ".",
            "Set to '-1' to disable.")
            .read(config);

        String tablePrefix = ConfigValue.create(path + "Table_Prefix", defaultPrefix,
            "Custom prefix for plugin tables in database.")
            .read(config);

        String mysqlUser = ConfigValue.create(path + "MySQL.Username", "root",
            "Database user name.")
            .read(config);

        String mysqlPassword = ConfigValue.create(path + "MySQL.Password", "root",
            "Database password.")
            .read(config);

        String mysqlHost = ConfigValue.create(path + "MySQL.Host", "localhost:3306",
            "Database host. Example: localhost:3306, 127.0.0.1:3306")
            .read(config);

        String mysqlBase = ConfigValue.create(path + "MySQL.Database", "minecraft",
            "Name of the MySQL database where plugin will create tables.")
            .read(config);

        String urlOptions = ConfigValue.create(path + "MySQL.Options",
            "?allowPublicKeyRetrieval=true&useSSL=false",
            "Connection options. Do not touch unless you know what you're doing."
        ).read(config);

        String sqliteFilename = ConfigValue.create(path + "SQLite.FileName", defFileName,
            "File name for the SQLite database file.",
            "Actually it's a path to the file, so you can use directories here.")
            .read(config);


        path = "Database.Purge.";
        boolean purgeEnabled = ConfigValue.create(path + "Enabled", false,
            "Enables the purge feature.",
            "Purge will remove all records from the plugin tables that are 'old' enough.")
            .read(config);

        int purgePeriod = ConfigValue.create(path + "For_Period", 60,
            "Sets maximal 'age' for the database records before they will be purged.",
            "This option may have different behavior depends on the plugin.",
            "By default it's days of inactivity for the plugin users.")
            .read(config);

        return new DatabaseConfig(
            saveInterval, syncInterval,
            databaseType, tablePrefix,
            purgeEnabled, purgePeriod,

            mysqlUser, mysqlPassword, mysqlHost, mysqlBase, urlOptions,

            sqliteFilename
        );
    }

    @NotNull
    public DatabaseType getStorageType() {
        return databaseType;
    }

    @NotNull
    public String getTablePrefix() {
        return tablePrefix;
    }

    public int getSaveInterval() {
        return saveInterval;
    }

    public int getSyncInterval() {
        return syncInterval;
    }

    public boolean isPurgeEnabled() {
        return purgeEnabled;
    }

    public int getPurgePeriod() {
        return purgePeriod;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    @NotNull
    public String getHost() {
        return host;
    }

    @NotNull
    public String getDatabase() {
        return database;
    }

    @NotNull
    public String getUrlOptions() {
        return urlOptions;
    }

    @NotNull
    public String getFilename() {
        return filename;
    }
}
