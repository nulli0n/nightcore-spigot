package su.nightexpress.nightcore.integration.item.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.integration.item.dummy.DummyItem;

@NullMarked
public class DummyItemCodec implements ConfigCodec<DummyItem> {

    public static final DummyItemCodec INSTANCE = new DummyItemCodec();

    @Override
    public DummyItem read(FileConfig config, String path) throws CodecReadException {
        return DummyItem.INSTANCE;
    }

    @Override
    public void write(FileConfig config, String path, DummyItem value) {

    }
}
