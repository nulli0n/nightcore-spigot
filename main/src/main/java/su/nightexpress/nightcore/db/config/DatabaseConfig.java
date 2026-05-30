package su.nightexpress.nightcore.db.config;

import java.util.UUID;
import java.util.stream.Stream;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.configuration.ConfigTypes;
import su.nightexpress.nightcore.util.StringUtil;

public class DatabaseConfig implements Writeable {

    public static final String DEF_FILE_NAME = "data.db";
    public static final String DEF_PATH      = "Database.";

    private final int          syncInterval;
    private final DatabaseType databaseType;
    private final String       tablePrefix;
    private final long         maxLifetime;
    private final boolean      purgeEnabled;
    private final int          purgePeriod;

    private final String username;
    private final String password;
    private final String host;
    private final String database;
    private final String urlOptions;

    private final String filename;

    private final String serverId;

    public DatabaseConfig(
                          int syncInterval,
                          @NonNull DatabaseType databaseType,
                          @NonNull String tablePrefix,
                          long maxLifetime,
                          boolean purgeEnabled,
                          int purgePeriod,

                          @NonNull String username,
                          @NonNull String password,
                          @NonNull String host,
                          @NonNull String database,
                          @NonNull String urlOptions,

                          @NonNull String filename,
                          @NonNull String serverId
    ) {
        this.syncInterval = syncInterval;
        this.databaseType = databaseType;
        this.tablePrefix = tablePrefix;
        this.maxLifetime = maxLifetime;
        this.purgeEnabled = purgeEnabled;
        this.purgePeriod = purgePeriod;

        this.username = username;
        this.password = password;
        this.host = host;
        this.database = database;
        this.urlOptions = urlOptions;

        this.filename = filename;
        this.serverId = serverId;
    }

    @NonNull
    public static DatabaseConfig read(@NonNull NightPlugin plugin) {
        String defPrefix = StringUtil.lowerCaseUnderscore(plugin.getName());

        FileConfig config = plugin.getConfig();
        FileConfig engineConf = plugin.getEngineConfig();

        // ---------- MIGRATION - START ----------
        if (config.contains("Database")) {
            DatabaseConfig old = read(config, defPrefix);
            old.write(engineConf, "Database");
            if (!config.contains("Database.UserData")) {
                config.remove("Database");
            }
        }
        // ---------- MIGRATION - END ----------

        return read(engineConf, defPrefix);
    }

    @NonNull
    public static DatabaseConfig read(@NonNull FileConfig config, @NonNull String defaultPrefix) {
        return read(config, DEF_PATH, defaultPrefix, DEF_FILE_NAME);
    }

    @NonNull
    public static DatabaseConfig read(@NonNull FileConfig config, @NonNull String path, @NonNull String defaultPrefix,
                                      @NonNull String defFileName) {
        DatabaseType databaseType = ConfigValue.create(path + ".Type", DatabaseType.class, DatabaseType.SQLITE,
            "Sets database type.",
            "Available values: " + String.join(",", Stream.of(DatabaseType.values()).map(Enum::name).toList()))
            .read(config);

        int syncInterval = ConfigValue.create(path + ".Sync_Interval", -1,
            "Sets how often (in seconds) plugin data will be fetched and loaded from the remote database.",
            "Useless for " + DatabaseType.SQLITE.name() + ".",
            "Set to '-1' to disable.")
            .read(config);

        long maxLifetime = ConfigValue.create(path + ".Max_Lifetime",
            1800000,
            "This property controls the maximum lifetime of a connection in the pool.",
            "A value of 0 indicates no maximum lifetime (infinite lifetime).",
            "[The minimum allowed value is 30000ms (30 seconds)]",
            "[Default is 1800000 (30 minutes)]"
        ).read(config);

        String tablePrefix = ConfigValue.create(path + ".Table_Prefix", defaultPrefix,
            "Custom prefix for plugin tables in database.")
            .read(config);

        String serverId = config.get(ConfigTypes.STRING, path + ".MySQL.ServerId", UUID.randomUUID().toString(),
            "Custom identifier of this server instance used in data syncing."
        );

        String mysqlUser = ConfigValue.create(path + ".MySQL.Username", "root",
            "Database user name.")
            .read(config);

        String mysqlPassword = ConfigValue.create(path + ".MySQL.Password", "",
            "Database password.")
            .read(config);

        String mysqlHost = ConfigValue.create(path + ".MySQL.Host", "localhost:3306",
            "Database host. Example: localhost:3306, 127.0.0.1:3306")
            .read(config);

        String mysqlBase = ConfigValue.create(path + ".MySQL.Database", "minecraft",
            "Name of the MySQL database where plugin will create tables.")
            .read(config);

        String urlOptions = ConfigValue.create(path + ".MySQL.Options",
            "?allowPublicKeyRetrieval=true&useSSL=false",
            "Connection options. Do not touch unless you know what you're doing."
        ).read(config);

        String sqliteFilename = ConfigValue.create(path + ".SQLite.FileName", defFileName,
            "File name for the SQLite database file.",
            "Actually it's a path to the file, so you can use directories here.")
            .read(config);


        boolean purgeEnabled = ConfigValue.create(path + ".Purge.Enabled", false,
            "Enables the purge feature.",
            "Purge will remove all records from the plugin tables that are 'old' enough.")
            .read(config);

        int purgePeriod = ConfigValue.create(path + ".Purge.For_Period", 60,
            "Sets maximal 'age' for the database records before they will be purged.",
            "This option may have different behavior depends on the plugin.",
            "By default it's days of inactivity for the plugin users.")
            .read(config);

        return new DatabaseConfig(
            syncInterval, databaseType, tablePrefix, maxLifetime, purgeEnabled, purgePeriod,

            mysqlUser, mysqlPassword, mysqlHost, mysqlBase, urlOptions,

            sqliteFilename, serverId
        );
    }

    @Override
    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.set(path + ".Type", this.databaseType.name());
        config.set(path + ".Sync_Interval", this.syncInterval);
        config.set(path + ".Max_Lifetime", this.maxLifetime);
        config.set(path + ".Table_Prefix", this.tablePrefix);
        config.set(path + ".MySQL.ServerId", this.serverId);
        config.set(path + ".MySQL.Username", this.username);
        config.set(path + ".MySQL.Password", this.password);
        config.set(path + ".MySQL.Host", this.host);
        config.set(path + ".MySQL.Database", this.database);
        config.set(path + ".MySQL.Options", this.urlOptions);
        config.set(path + ".SQLite.FileName", this.filename);
        config.set(path + ".Purge.Enabled", this.purgeEnabled);
        config.set(path + ".Purge.For_Period", this.purgePeriod);
    }

    @NonNull
    public DatabaseType getStorageType() {
        return databaseType;
    }

    @NonNull
    public String getTablePrefix() {
        return tablePrefix;
    }

    public int getSyncInterval() {
        return syncInterval;
    }

    public long getMaxLifetime() {
        return this.maxLifetime;
    }

    public boolean isPurgeEnabled() {
        return purgeEnabled;
    }

    public int getPurgePeriod() {
        return purgePeriod;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getHost() {
        return host;
    }

    @NonNull
    public String getDatabase() {
        return database;
    }

    @NonNull
    public String getUrlOptions() {
        return urlOptions;
    }

    @NonNull
    public String getFilename() {
        return filename;
    }

    public @NonNull String getServerId() {
        return this.serverId;
    }
}
