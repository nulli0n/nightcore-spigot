package su.nightexpress.nightcore.userdata;

import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.configuration.AbstractConfig;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.ConfigTypes;
import su.nightexpress.nightcore.db.data.DataSettings;

public class UserDataSettings extends AbstractConfig implements DataSettings {

    private final ConfigProperty<String> dataTableName = this.addProperty(ConfigTypes.STRING,
        "Data.Table-Name",
        "user_data"
    );

    private final ConfigProperty<Boolean> dataLoadAll = this.addProperty(ConfigTypes.BOOLEAN,
        "Data.Load-All-Users",
        false,
        "When enabled, loads and caches all user datas on startup."
    );

    private final ConfigProperty<Integer> dataSaveInterval = this.addProperty(ConfigTypes.INT,
        "Data.Save-Interval",
        900
    );

    private final ConfigProperty<Integer> dataCacheTimeDuration = this.addProperty(ConfigTypes.INT,
        "Data.CacheTime.Duration",
        3600
    );

    public @NonNull String getTableName() {
        return this.dataTableName.get();
    }

    @Override
    public int getCacheTimeDuration() {
        return this.dataCacheTimeDuration.get();
    }

    @Override
    public @NonNull TimeUnit getCacheTimeUnit() {
        return TimeUnit.SECONDS;
    }

    @Override
    public int getSaveInterval() {
        return this.dataSaveInterval.get();
    }

    public boolean getLoadAllUsers() {
        return this.dataLoadAll.get();
    }
}
