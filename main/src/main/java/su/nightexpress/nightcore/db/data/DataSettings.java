package su.nightexpress.nightcore.db.data;

import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.NonNull;

public interface DataSettings {

    int getSaveInterval();

    int getCacheTimeDuration();

    @NonNull
    TimeUnit getCacheTimeUnit();
}
