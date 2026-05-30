package su.nightexpress.nightcore.configuration.codec.core;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.ArrayUtil;

@NullMarked
public class IntArrayCodec implements ConfigCodec<int[]> {

    @Override
    public int[] read(FileConfig config, String path) throws CodecReadException {
        String str = config.getString(path);
        return str == null ? new int[0] : ArrayUtil.parseIntArray(str);
    }

    @Override
    public void write(FileConfig config, String path, int[] array) {
        config.set(path, ArrayUtil.arrayToString(array));
    }
}
