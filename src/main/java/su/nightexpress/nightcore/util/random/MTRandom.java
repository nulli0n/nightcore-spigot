package su.nightexpress.nightcore.util.random;

import java.util.Random;

public class MTRandom extends Random {

    private final static int UPPER_MASK = 0x80000000;
    private final static int LOWER_MASK = 0x7fffffff;

    private final static int   N             = 624;
    private final static int   M             = 397;
    private final static int[] MAGIC         = {0x0, 0x9908b0df};
    private final static int   MAGIC_FACTOR1 = 1812433253;
    private final static int   MAGIC_FACTOR2 = 1664525;
    private final static int   MAGIC_FACTOR3 = 1566083941;
    private final static int   MAGIC_MASK1   = 0x9d2c5680;
    private final static int   MAGIC_MASK2   = 0xefc60000;
    private final static int   MAGIC_SEED    = 19650218;
    private final static long  DEFAULT_SEED  = 5489L;

    // Internal state
    private transient int[] mt;
    private transient int   mti;

    // Temporary buffer used during setSeed(long)
    private transient int[] ibuf;

    public MTRandom() {

    }

    private void setSeed() {
        if (mt == null) mt = new int[N];

        // ---- Begin Mersenne Twister Algorithm ----
        mt[0] = MTRandom.MAGIC_SEED;
        for (mti = 1; mti < N; mti++) {
            mt[mti] = (MAGIC_FACTOR1 * (mt[mti - 1] ^ (mt[mti - 1] >>> 30)) + mti);
        }
        // ---- End Mersenne Twister Algorithm ----
    }

    public final synchronized void setSeed(long seed) {
        if (ibuf == null) ibuf = new int[2];

        ibuf[0] = (int) seed;
        ibuf[1] = (int) (seed >>> 32);
        setSeed(ibuf);
    }

    protected final synchronized int next(int bits) {
        // ---- Begin Mersenne Twister Algorithm ----
        int y, kk;
        if (mti >= N) {

            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ MAGIC[y & 0x1];
            }
            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ MAGIC[y & 0x1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ MAGIC[y & 0x1];

            mti = 0;
        }

        y = mt[mti++];

        // Tempering
        y ^= (y >>> 11);
        y ^= (y << 7) & MAGIC_MASK1;
        y ^= (y << 15) & MAGIC_MASK2;
        y ^= (y >>> 18);
        // ---- End Mersenne Twister Algorithm ----
        return (y >>> (32 - bits));
    }

    // This is a fairly obscure little code section to pack a
    // byte[] into an int[] in little endian ordering.

    public final synchronized void setSeed(int[] buf) {
        int length = buf.length;
        if (length == 0) throw new IllegalArgumentException("Seed buffer may not be empty");
        // ---- Begin Mersenne Twister Algorithm ----
        int i = 1, j = 0, k = (Math.max(N, length));
        setSeed();
        for (; k > 0; k--) {
            mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * MAGIC_FACTOR2)) + buf[j] + j;
            i++;
            j++;
            if (i >= N) {
                mt[0] = mt[N - 1];
                i = 1;
            }
            if (j >= length) j = 0;
        }
        for (k = N - 1; k > 0; k--) {
            mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * MAGIC_FACTOR3)) - i;
            i++;
            if (i >= N) {
                mt[0] = mt[N - 1];
                i = 1;
            }
        }
        mt[0] = UPPER_MASK; // MSB is 1; assuring non-zero initial array
        // ---- End Mersenne Twister Algorithm ----
    }
}