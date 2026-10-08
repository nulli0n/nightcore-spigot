package su.nightexpress.nightcore.integration.item.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.integration.item.bukkit.BukkitItem;
import su.nightexpress.nightcore.util.ItemTag;

@NullMarked
public class BukkitItemCodec implements ConfigCodec<BukkitItem> {

    public static final BukkitItemCodec INSTANCE = new BukkitItemCodec();

    @Override
    public BukkitItem read(FileConfig config, String path) throws CodecReadException {
        ItemTag tag = ItemTag.read(config, path + ".Data");

        return new BukkitItem(tag);
    }

    @Override
    public void write(FileConfig config, String path, BukkitItem value) {
        ItemTag tag = value.getData();

        tag.write(config, path + ".Data");
    }
}
