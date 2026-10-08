package su.nightexpress.nightcore.integration.item.codec;

import java.util.function.Function;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.item.AdaptedDataItem;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@NullMarked
public final class IdentifiableItemCodec {

    private IdentifiableItemCodec() {
    }

    public static <I extends AdaptedItem & AdaptedDataItem<ItemIdData>> ConfigCodec<I> create(Function<ItemIdData, I> construct) {
        return new ConfigCodec<>() {

            @Override
            public I read(FileConfig config, String path) throws CodecReadException {
                String itemId = config.getString(path + ".Data.ID", "null");
                int amount = config.getInt(path + ".Data.Amount");

                ItemIdData data = new ItemIdData(itemId, amount);

                return construct.apply(data);
            }

            @Override
            public void write(FileConfig config, String path, @NonNull I value) {
                ItemIdData data = value.getData();
                config.set(path + ".Data.ID", data.getItemId());
                config.set(path + ".Data.Amount", data.getAmount());
            }
        };
    }
}
