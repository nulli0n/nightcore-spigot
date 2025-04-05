package su.nightexpress.nightcore.util.geodata;

public class GeoUtils {

    public static int shiftToChunk(int x) {
        return x >> 4;
    }

    public static int shiftToCoord(int chunkX) {
        return chunkX << 4;
    }
}
