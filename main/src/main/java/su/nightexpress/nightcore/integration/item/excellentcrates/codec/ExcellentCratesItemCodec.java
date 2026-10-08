package su.nightexpress.nightcore.integration.item.excellentcrates.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.integration.item.excellentcrates.ExcellentCratesItem;
import su.nightexpress.nightcore.integration.item.excellentcrates.ExcellentCratesItemContext;
import su.nightexpress.nightcore.integration.item.excellentcrates.ExcellentCratesItemType;

@NullMarked
public class ExcellentCratesItemCodec implements ConfigCodec<ExcellentCratesItem> {

    public static final ExcellentCratesItemCodec INSTANCE = new ExcellentCratesItemCodec();

    @Override
    public ExcellentCratesItem read(FileConfig config, String path) throws CodecReadException {
        Identifier itemId = config.getOrSet(path + ".item_id", Identifier.class, new Identifier("null"));
        ExcellentCratesItemType type = config.getOrSet(path + ".type",
            ExcellentCratesItemType.class,
            ExcellentCratesItemType.CRATE
        );
        int amount = config.getOrSet(path + ".amount", ConfigCodecs.INT, 1);

        ExcellentCratesItemContext context = new ExcellentCratesItemContext(type, itemId, amount);

        return new ExcellentCratesItem(context);
    }

    @Override
    public void write(FileConfig config, String path, ExcellentCratesItem value) {
        ExcellentCratesItemContext context = value.getData();
        config.set(path + ".item_id", context.itemId());
        config.set(path + ".type", context.type().name());
        config.set(path + ".amount", context.amount());
    }
}
