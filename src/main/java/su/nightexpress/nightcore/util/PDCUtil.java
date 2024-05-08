package su.nightexpress.nightcore.util;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.UUID;

public class PDCUtil {

    public static final PersistentDataType<byte[], UUID> UUID = new UUIDDataType();

    @NotNull
    public static <Z> Optional<Z> get(@NotNull ItemStack holder, @NotNull PersistentDataType<?, Z> type, @NotNull NamespacedKey key) {
        ItemMeta meta = holder.getItemMeta();
        if (meta == null) return Optional.empty();

        return get(meta, type, key);
    }

    @NotNull
    public static <Z> Optional<Z> get(@NotNull PersistentDataHolder holder, @NotNull PersistentDataType<?, Z> type, @NotNull NamespacedKey key) {
        PersistentDataContainer container = holder.getPersistentDataContainer();
        if (container.has(key, type)) {
            return Optional.ofNullable(container.get(key, type));
        }
        return Optional.empty();
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, boolean value) {
        set(holder, PersistentDataType.INTEGER, key, value ? 1 : 0);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, boolean value) {
        set(holder, PersistentDataType.INTEGER, key, value ? 1 : 0);
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, double value) {
        set(holder, PersistentDataType.DOUBLE, key, value);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, double value) {
        set(holder, PersistentDataType.DOUBLE, key, value);
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, int value) {
        set(holder, PersistentDataType.INTEGER, key, value);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, int value) {
        set(holder, PersistentDataType.INTEGER, key, value);
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, long value) {
        set(holder, PersistentDataType.LONG, key, value);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, long value) {
        set(holder, PersistentDataType.LONG, key, value);
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, @Nullable String value) {
        set(holder, PersistentDataType.STRING, key, value);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, @Nullable String value) {
        set(holder, PersistentDataType.STRING, key, value);
    }

    public static void set(@NotNull ItemStack holder, @NotNull NamespacedKey key, @Nullable UUID value) {
        set(holder, UUID, key, value);
    }

    public static void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, @Nullable UUID value) {
        set(holder, UUID, key, value);
    }

    public static <T, Z> void set(@NotNull ItemStack item,
                                  @NotNull PersistentDataType<T, Z> dataType,
                                  @NotNull NamespacedKey key,
                                  @Nullable Z value) {
        ItemUtil.editMeta(item, meta -> set(meta, dataType, key, value));
    }

    public static <T, Z> void set(@NotNull PersistentDataHolder holder,
                                  @NotNull PersistentDataType<T, Z> dataType,
                                  @NotNull NamespacedKey key,
                                  @Nullable Z value) {
        if (value == null) {
            remove(holder, key);
            return;
        }

        PersistentDataContainer container = holder.getPersistentDataContainer();
        container.set(key, dataType, value);
    }

    public static void remove(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        ItemUtil.editMeta(holder, meta -> remove(meta, key));
    }

    public static void remove(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        PersistentDataContainer container = holder.getPersistentDataContainer();
        container.remove(key);
    }

    @NotNull
    public static Optional<String> getString(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.STRING, key);
    }

    @NotNull
    public static Optional<String> getString(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.STRING, key);
    }

    @NotNull
    public static Optional<Integer> getInt(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.INTEGER, key);
    }

    @NotNull
    public static Optional<Integer> getInt(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.INTEGER, key);
    }

    @NotNull
    public static Optional<Long> getLong(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.LONG, key);
    }

    @NotNull
    public static Optional<Long> getLong(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.LONG, key);
    }

    @NotNull
    public static Optional<Double> getDouble(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.DOUBLE, key);
    }

    @NotNull
    public static Optional<Double> getDouble(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.DOUBLE, key);
    }

    @NotNull
    public static Optional<Boolean> getBoolean(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.INTEGER, key).map(i -> i != 0);
    }

    @NotNull
    public static Optional<Boolean> getBoolean(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, PersistentDataType.INTEGER, key).map(i -> i != 0);
    }

    @NotNull
    public static Optional<UUID> getUUID(@NotNull ItemStack holder, @NotNull NamespacedKey key) {
        return get(holder, UUID, key);
    }

    @NotNull
    public static Optional<UUID> getUUID(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return get(holder, UUID, key);
    }

    public static class UUIDDataType implements PersistentDataType<byte[], UUID> {

        @NotNull
        @Override
        public Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @NotNull
        @Override
        public Class<UUID> getComplexType() {
            return UUID.class;
        }

        @Override
        public byte @NotNull [] toPrimitive(UUID complex, @NotNull PersistentDataAdapterContext context) {
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(complex.getMostSignificantBits());
            bb.putLong(complex.getLeastSignificantBits());
            return bb.array();
        }

        @Override
        public @NotNull UUID fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
            ByteBuffer bb = ByteBuffer.wrap(primitive);
            long firstLong = bb.getLong();
            long secondLong = bb.getLong();
            return new UUID(firstLong, secondLong);
        }
    }
}
