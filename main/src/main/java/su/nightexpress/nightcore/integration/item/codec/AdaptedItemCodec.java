package su.nightexpress.nightcore.integration.item.codec;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.integration.item.ItemBridge;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.integration.item.dummy.DummyItem;
import su.nightexpress.nightcore.integration.item.registry.RegisteredItemProvider;

@NullMarked
public final class AdaptedItemCodec {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdaptedItemCodec.class);

    private AdaptedItemCodec() {
    }

    public static AdaptedItem read(FileConfig config, String path) {
        String providerId = config.getOrSet(path + ".Provider", ConfigCodecs.STRING, ItemProviderKeys.BUKKIT);
        RegisteredItemProvider<?> entry = ItemBridge.get().getRegisteredProvider(providerId);

        if (entry == null) {
            return DummyItem.INSTANCE;
        }

        try {
            return entry.codec().read(config, path);
        }
        catch (CodecReadException exception) {
            LOGGER.error("Failed to read AdaptedItem at path {} in config {}",
                path, config.getPath().getFileName(), exception
            );
            return DummyItem.INSTANCE;
        }
    }

    public static void write(FileConfig config, String path, AdaptedItem item) {
        if (item instanceof DummyItem) {
            return;
        }

        String providerId = item.getProviderId();
        RegisteredItemProvider<?> entry = ItemBridge.get().getRegisteredProvider(providerId);
        if (entry != null) {
            config.set(path + ".Provider", providerId);

            @SuppressWarnings("unchecked") ConfigCodec<AdaptedItem> codec = (ConfigCodec<AdaptedItem>) entry.codec();
            codec.write(config, path, item);
        }
        else {
            LOGGER.warn("Failed to write AdaptedItem at path {} in config {}: provider {} is not registered",
                path, config.getPath().getFileName(), providerId
            );
        }
    }
}
