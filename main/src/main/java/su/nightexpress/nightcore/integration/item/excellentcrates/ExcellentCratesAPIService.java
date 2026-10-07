package su.nightexpress.nightcore.integration.item.excellentcrates;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.ExcellentCratesAPI;
import su.nightexpress.nightcore.util.ServerUtils;

@NullMarked
public final class ExcellentCratesAPIService {

    private ExcellentCratesAPIService() {
    }

    public static @Nullable ExcellentCratesAPI resolve() {
        return ServerUtils.getProvider(ExcellentCratesAPI.class);
    }
}
