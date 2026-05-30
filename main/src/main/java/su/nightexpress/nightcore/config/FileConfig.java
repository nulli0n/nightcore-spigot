package su.nightexpress.nightcore.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.ConfigType;
import su.nightexpress.nightcore.configuration.ConfigTypes;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.ArrayUtil;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.FileUtil;
import su.nightexpress.nightcore.util.ItemNbt;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.LegacyColors;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.Reflex;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.bukkit.NightSound;
import su.nightexpress.nightcore.util.sound.AbstractSound;
import su.nightexpress.nightcore.util.text.night.NightMessage;

@NullMarked
public class FileConfig extends YamlConfiguration {

    public static final String EXTENSION = ".yml";

    private final Path   path;
    private final Logger logger;

    private boolean changed;

    @Deprecated
    public FileConfig(String path, String file) {
        this(new File(path, file));
    }

    @Deprecated
    public FileConfig(File file) {
        this.changed = false;
        this.options().width(512);

        FileUtil.create(file);
        this.path = file.toPath();
        this.reload();
        this.logger = Logger.getLogger(this.path.toString());
    }

    private FileConfig(Path path) {
        this.path = path;
        this.changed = false;
        this.options().width(512);
        this.logger = Logger.getLogger(path.toString());
    }

    public static FileConfig load(String path, String file) {
        return load(Path.of(path, file));
    }

    public static FileConfig load(Path path) {
        FileConfig config = new FileConfig(path);
        config.load();
        return config;
    }

    public static boolean isConfig(File file) {
        return file.getName().endsWith(EXTENSION);
    }

    public static String withExtension(String fileName) {
        return fileName + EXTENSION;
    }

    public static String getName(File file) {
        String name = file.getName();

        if (isConfig(file)) {
            return name.substring(0, name.length() - EXTENSION.length());
        }
        return name;
    }

    @Deprecated
    public static FileConfig loadOrExtract(NightCorePlugin plugin, String path,
                                           String file) {
        if (!path.endsWith("/")) {
            path += "/";
        }
        return loadOrExtract(plugin, path + file);
    }

    @Deprecated
    public static FileConfig loadOrExtract(NightCorePlugin plugin, String filePath) {
        if (!filePath.startsWith("/")) {
            filePath = "/" + filePath;
        }

        File file = new File(plugin.getDataFolder() + filePath);
        if (FileUtil.create(file)) {
            try (InputStream input = plugin.getClass().getResourceAsStream(filePath)) {
                if (input != null) FileUtil.copy(input, file);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return new FileConfig(file);
    }

    public static List<FileConfig> loadAll(String path) {
        return loadAll(path, false);
    }

    public static List<FileConfig> loadAll(String path, boolean deep) {
        return FileUtil.getConfigFiles(path, deep).stream().map(FileConfig::new).toList();
    }

    public void initializeOptions(Class<?> clazz) {
        initializeOptions(clazz, this);
    }

    public static void initializeOptions(Class<?> clazz, FileConfig config) {
        for (ConfigValue<?> value : Reflex.getStaticFields(clazz, ConfigValue.class, false)) {
            value.read(config);
        }
    }

    @Deprecated
    public File getFile() {
        return this.path.toFile();
    }

    public Path getPath() {
        return this.path;
    }

    @Deprecated
    public boolean reload() {
        this.load();
        return true;
    }

    public void load() {
        FileUtil.createFileIfNotExists(this.path);
        this.changed = false;

        try (BufferedReader reader = Files.newBufferedReader(this.path, StandardCharsets.UTF_8)) {
            this.load(reader);
        }
        catch (IOException | InvalidConfigurationException | IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        FileUtil.createFileIfNotExists(this.path);

        try (BufferedWriter writer = Files.newBufferedWriter(this.path, StandardCharsets.UTF_8)) {
            writer.write(this.saveToString());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public boolean saveChanges() {
        if (!this.changed) return false;

        this.save();
        this.changed = false;
        return true;
    }

    public void edit(Consumer<FileConfig> consumer) {
        consumer.accept(this);
        this.saveChanges();
    }

    public boolean addMissing(String path, @Nullable Object val) {
        if (this.contains(path)) return false;
        this.set(path, val);
        return true;
    }

    public <T> T get(ConfigProperty<T> property) {
        return property.loadWithDefaults(this);
    }

    public <T> void set(ConfigProperty<T> property) {
        property.write(this);
    }

    public <T> void set(ConfigProperty<T> property, T value) {
        property.writeValue(this, value);
    }

    @Deprecated
    public <T> T get(ConfigType<T> type, String path, T def, @Nullable String... comments) {
        return this.get((ConfigCodec<T>) type, path, def, comments);
    }

    @Deprecated
    public <T> T get(ConfigCodec<T> type, String path, T def, @Nullable String... comments) {
        if (!this.contains(path)) {
            type.write(this, path, def);
            this.setComments(path, comments);
        }
        return type.read(this, path, def);
    }

    private <T> ConfigCodec<T> resolveCodec(Class<T> type) {
        ConfigCodec<T> codec = ConfigCodecs.getCodec(type);
        if (codec == null) {
            throw new IllegalArgumentException("No codec registered for " + type);
        }
        return codec;
    }

    // Unsafe Methods

    public <T> @Nullable T read(String path, ConfigCodec<T> codec) throws CodecReadException {
        if (!this.contains(path)) return null;

        return codec.read(this, path);
    }

    public <T> T read(String path, ConfigCodec<T> codec, T defaultValue) throws CodecReadException {
        T value = this.read(path, codec);
        return value == null ? defaultValue : value;
    }

    public <T> @Nullable T read(String path, Class<T> type) throws CodecReadException {
        return this.read(path, this.resolveCodec(type));
    }

    public <T> T read(String path, Class<T> type, T defaultValue) throws CodecReadException {
        return this.read(path, this.resolveCodec(type), defaultValue);
    }

    // Safe Methods

    public <T> @Nullable T get(String path, ConfigCodec<T> codec) {
        try {
            return this.read(path, codec);
        }
        catch (CodecReadException exception) {
            this.logger.warning("Failed to read at " + path + ": " + exception.getMessage());
            return null;
        }
    }

    @Deprecated
    public <T> T get(String path, ConfigCodec<T> codec, T defaultValue) {
        return this.getOrSet(path, codec, defaultValue);
    }

    public <T> T getOrSet(String path, ConfigCodec<T> codec, T defaultValue) {
        T value = this.get(path, codec);

        if (value == null) {
            this.set(path, codec, defaultValue);
            return defaultValue;
        }

        return value;
    }

    public <T> @Nullable T get(String path, Class<T> type) {
        return this.get(path, this.resolveCodec(type));
    }

    @Deprecated
    public <T> T get(String path, Class<T> type, T defaultValue) {
        return this.getOrSet(path, type, defaultValue);
    }

    public <T> T getOrSet(String path, Class<T> type, T defaultValue) {
        return this.getOrSet(path, this.resolveCodec(type), defaultValue);
    }


    public <T> void set(String path, ConfigCodec<T> codec, @Nullable T value) {
        if (value == null) {
            this.set(path, null);
            return;
        }

        codec.write(this, path, value);
    }

    @SuppressWarnings("unchecked")
    public <T> void writeByCodec(String path, @Nullable T object) {
        if (object == null) {
            this.set(path, null);
            return;
        }

        ConfigCodec<T> codec = ConfigCodecs.getCodec(object);
        if (codec == null) {
            throw new IllegalArgumentException("No codec registered for " + object.getClass());
        }

        Class<?> dispatcherType = codec.getDispatcherType();
        if (dispatcherType != null) {
            ConfigCodec<Object> dispatcher = (ConfigCodec<Object>) ConfigCodecs.getCodec(dispatcherType);
            if (dispatcher == null) {
                throw new IllegalStateException("Dispatcher codec " + dispatcherType + " is not registered!");
            }
            this.set(path, dispatcher, object);
        }

        this.set(path, codec, object);
    }

    public void setArray(String path, double @Nullable [] array) {
        this.set(path, array == null ? null : ArrayUtil.arrayToString(array));
    }

    public void setArray(String path, int @Nullable [] array) {
        this.set(path, ConfigCodecs.INT_ARRAY, array);
    }

    public void setArray(String path, long @Nullable [] array) {
        this.set(path, array == null ? null : ArrayUtil.arrayToString(array));
    }

    public <T> void set(ConfigType<T> type, String path, T def) {
        type.write(this, path, def);
    }

    @Override
    public void set(String path, @Nullable Object value) {
        if (value != null && ConfigCodecs.isRegistered(value.getClass())) {
            this.writeByCodec(path, value);
            return;
        }

        if (value instanceof Writeable writeable) {
            writeable.write(this, path);
            this.changed = true;
            return;
        }

        if (value instanceof UUID uuid) {
            value = uuid.toString();
        }
        else if (value instanceof String str) {
            value = LegacyColors.plainColors(str);
        }
        else if (value instanceof Collection<?> collection) {
            List<Object> list = new ArrayList<>(collection);
            list.replaceAll(obj -> obj instanceof String str ? LegacyColors.plainColors(str) : obj);
            value = list;
        }
        else if (value instanceof Location location) {
            value = LocationUtil.serialize(location);
        }
        else if (value instanceof Enum<?> en) {
            value = en.name();
        }
        super.set(path, value);
        this.changed = true;
    }

    public void setComments(String path, String @Nullable... comments) {
        this.setComments(path, comments == null ? null : Arrays.asList(comments));
    }

    public void setInlineComments(String path, String @Nullable... comments) {
        this.setInlineComments(path, comments == null ? null : Arrays.asList(comments));
    }

    @Override
    public void setComments(String path, @Nullable List<String> comments) {
        if (!this.areCommentsDifferent(this.getComments(path), comments)) return;

        super.setComments(path, comments);
        this.changed = true;
    }

    @Override
    public void setInlineComments(String path, @Nullable List<String> comments) {
        if (!this.areCommentsDifferent(this.getInlineComments(path), comments)) return;

        super.setInlineComments(path, comments);
        this.changed = true;
    }

    private boolean areCommentsDifferent(List<String> current, @Nullable List<String> comments) {
        if ((comments == null || comments.isEmpty())) {
            return !current.isEmpty();
        }

        return !new HashSet<>(current).equals(new HashSet<>(comments));
    }

    public boolean remove(String path) {
        if (!this.contains(path)) return false;
        this.set(path, null);
        return true;
    }


    public Set<String> getSection(String path) {
        ConfigurationSection section = this.getConfigurationSection(path);
        return section == null ? Collections.emptySet() : section.getKeys(false);
    }

    @Nullable
    public UUID getUUID(String path) {
        return this.getUUID(path, null);
    }

    @Nullable
    public UUID getUUID(String path, @Nullable UUID def) {
        String string = this.getString(path);
        if (string == null) return def;

        try {
            return UUID.fromString(string);
        }
        catch (IllegalArgumentException exception) {
            exception.printStackTrace();
            return def;
        }
    }


    public String getStringOrEmpty(String path) {
        String str = super.getString(path);
        return str == null ? "" : str;
    }

    @Override
    @Nullable
    public String getString(String path) {
        String str = super.getString(path);
        return str == null || str.isEmpty() ? null : str;
    }

    @Override

    public String getString(String path, @Nullable String def) {
        String str = super.getString(path, def);
        return str == null ? "" : str;
    }


    public Set<String> getStringSet(String path) {
        return new HashSet<>(this.getStringList(path));
    }

    @Override
    @Nullable
    @Deprecated
    public Location getLocation(String path) {
        String raw = this.getString(path);
        return raw == null ? null : LocationUtil.deserialize(raw);
    }

    @Deprecated
    public void setIntArray(String path, int[] arr) {
        this.setArray(path, arr);
    }

    public int @Nullable [] getIntArray(String path) {
        return getIntArray(path, new int[0]);
    }

    public int @Nullable [] getIntArray(String path, int[] defaultArray) {
        return this.get(path, ConfigTypes.INT_ARRAY, defaultArray);
    }

    public double[] getDoubleArray(String path) {
        return getDoubleArray(path, new double[0]);
    }

    public double[] getDoubleArray(String path, double[] defaultArray) {
        String str = this.getString(path);
        return str == null ? defaultArray : ArrayUtil.parseDoubleArray(str);
    }

    public long[] getLongArray(String path) {
        return getLongArray(path, new long[0]);
    }

    public long[] getLongArray(String path, long[] defaultArray) {
        String str = this.getString(path);
        return str == null ? defaultArray : ArrayUtil.parseLongArray(str);
    }


    public String[] getStringArray(String path) {
        return this.getStringArray(path, new String[0]);
    }


    public String[] getStringArray(String path, String[] def) {
        String str = this.getString(path);
        return str == null ? def : str.split(",");
    }

    public void setStringArray(String path, String @Nullable [] arr) {
        this.set(path, arr == null ? null : String.join(",", arr));
    }

    public <T extends Enum<T>> @Nullable T getEnum(String path, Class<T> clazz) {
        return Enums.get(this.getString(path, ""), clazz);
    }


    public <T extends Enum<T>> T getEnum(String path, Class<T> clazz, T def) {
        return Enums.parse(this.getString(path), clazz).orElse(def);
    }


    public <T extends Enum<T>> List<T> getEnumList(String path, Class<T> clazz) {
        return this.getStringSet(path).stream().map(str -> Enums.parse(str, clazz).orElse(null))
            .filter(Objects::nonNull).toList();
    }

    /*
    public Set<FireworkEffect> getFireworkEffects( String path) {
        Set<FireworkEffect> effects = new HashSet<>();
        for (String sId : this.getSection(path)) {
            String path2 = path + "." + sId + ".";
            FireworkEffect.Type type = this.getEnum(path2 + "Type", FireworkEffect.Type.class);
            if (type == null) continue;
    
            boolean flicker = this.getBoolean(path2 + "Flicker");
            boolean trail = this.getBoolean(path2 + "Trail");
    
            Set<Color> colors = new HashSet<>();
            for (String colorRaw : this.getStringList(path2 + "Colors")) {
                colors.add(StringUtil.parseColor(colorRaw));
            }
    
            Set<Color> fadeColors = new HashSet<>();
            for (String colorRaw : this.getStringList(path2 + "Fade_Colors")) {
                fadeColors.add(StringUtil.parseColor(colorRaw));
            }
    
            FireworkEffect.Builder builder = FireworkEffect.builder()
                .with(type).flicker(flicker).trail(trail).withColor(colors).withFade(fadeColors);
            effects.add(builder.build());
        }
    
        return effects;
    }*/


    public NightItem getCosmeticItem(String path, NightItem def) {
        return this.contains(path) ? this.getCosmeticItem(path) : def;
    }


    public NightItem getCosmeticItem(String path) {
        return NightItem.read(this, path);
    }


    @Deprecated
    public NightSound getSound(String path) {
        return NightSound.read(this, path); // Update
    }

    public su.nightexpress.nightcore.bridge.wrap.@Nullable NightSound readSound(String path) {
        return AbstractSound.read(this, path);
    }

    public su.nightexpress.nightcore.bridge.wrap.@Nullable NightSound readSound(String path,
                                                                                su.nightexpress.nightcore.bridge.wrap.NightSound def) {
        su.nightexpress.nightcore.bridge.wrap.NightSound sound = this.readSound(path);
        return sound == null ? def : sound;
    }

    @Deprecated
    public void setSound(String path, NightSound sound) {
        this.set(path, sound);
    }


    @Deprecated
    public ItemStack getItem(String path, @Nullable ItemStack def) {
        ItemStack item = this.getItem(path);
        return item.getType().isAir() && def != null ? def : item;
    }


    @Deprecated
    public ItemStack getItem(String path) {
        if (!path.isEmpty() && !path.endsWith(".")) path = path + ".";

        Material material = BukkitThing.getMaterial(this.getString(path + "Material", BukkitThing.toString(
            Material.AIR)));
        if (material == null || material.isAir()) return new ItemStack(Material.AIR);

        ItemStack item = new ItemStack(material);
        item.setAmount(this.getInt(path + "Amount", 1));

        // -------- UPDATE OLD TEXTURE FIELD - START --------
        String headTexture = this.getString(path + "Head_Texture");
        if (headTexture != null && !headTexture.isEmpty()) {

            try {
                byte[] decoded = Base64.getDecoder().decode(headTexture);
                String decodedStr = new String(decoded, StandardCharsets.UTF_8);
                JsonElement element = JsonParser.parseString(decodedStr);

                String url = element.getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("SKIN").get("url")
                    .getAsString();
                url = url.substring(ItemUtil.TEXTURES_HOST.length());

                this.set(path + "SkinURL", url);
                this.remove(path + "Head_Texture");
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        // -------- UPDATE OLD TEXTURE FIELD - END --------

        String headSkin = this.getString(path + "SkinURL");
        if (headSkin != null) {
            ItemUtil.setProfileBySkinURL(item, headSkin);
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        int durability = this.getInt(path + "Durability");
        if (durability > 0 && meta instanceof Damageable damageable) {
            damageable.setDamage(durability);
        }

        String name = this.getString(path + "Name");
        /*        meta.setDisplayName(name != null ? NightMessage.asLegacy(name) : null);
        meta.setLore(NightMessage.asLegacy(this.getStringList(path + "Lore")));*/
        ItemUtil.setCustomName(meta, name == null ? null : NightMessage.parse(name));
        ItemUtil.setLore(meta, this.getStringList(path + "Lore"));

        for (String sKey : this.getSection(path + "Enchants")) {
            Enchantment enchantment = BukkitThing.getEnchantment(sKey);
            if (enchantment == null) continue;

            int level = this.getInt(path + "Enchants." + sKey);
            if (level <= 0) continue;

            meta.addEnchant(enchantment, level, true);
        }

        int model = this.getInt(path + "Custom_Model_Data");
        meta.setCustomModelData(model != 0 ? model : null);

        List<String> flags = this.getStringList(path + "Item_Flags");
        if (flags.contains(Placeholders.WILDCARD)) {
            meta.addItemFlags(ItemFlag.values());
        }
        else {
            flags.stream().map(str -> StringUtil.getEnum(str, ItemFlag.class).orElse(null)).filter(Objects::nonNull)
                .forEach(meta::addItemFlags);
        }

        String colorRaw = this.getString(path + "Color");
        if (colorRaw != null && !colorRaw.isEmpty()) {
            Color color = StringUtil.getColor(colorRaw);
            if (meta instanceof LeatherArmorMeta armorMeta) {
                armorMeta.setColor(color);
            }
            else if (meta instanceof PotionMeta potionMeta) {
                potionMeta.setColor(color);
            }
        }

        meta.setUnbreakable(this.getBoolean(path + "Unbreakable"));
        item.setItemMeta(meta);

        return item;
    }

    @Deprecated
    public void setItem(String path, @Nullable ItemStack item) {
        if (item == null) {
            this.set(path, null);
            return;
        }

        if (!path.endsWith(".")) path = path + ".";
        this.set(path.substring(0, path.length() - 1), null);

        Material material = item.getType();
        this.set(path + "Material", material.name());
        this.set(path + "Amount", item.getAmount() <= 1 ? null : item.getAmount());
        this.set(path + "SkinURL", ItemUtil.getProfileSkinURL(item));

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        if (meta instanceof Damageable damageable) {
            this.set(path + "Durability", damageable.getDamage() <= 0 ? null : damageable.getDamage());
        }

        this.set(path + "Name", meta.getDisplayName().isEmpty() ? null : meta.getDisplayName());
        this.set(path + "Lore", meta.getLore());

        for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
            this.set(path + "Enchants." + entry.getKey().getKey().getKey(), entry.getValue());
        }
        this.set(path + "Custom_Model_Data", meta.hasCustomModelData() ? meta.getCustomModelData() : null);

        Color color = null;
        String colorRaw = null;
        if (meta instanceof PotionMeta potionMeta) {
            color = potionMeta.getColor();
        }
        else if (meta instanceof LeatherArmorMeta armorMeta) {
            color = armorMeta.getColor();
        }
        if (color != null) {
            colorRaw = color.getRed() + "," + color.getGreen() + "," + color.getBlue();
        }
        this.set(path + "Color", colorRaw);

        List<String> itemFlags = new ArrayList<>(meta.getItemFlags().stream().map(ItemFlag::name).toList());
        this.set(path + "Item_Flags", itemFlags.isEmpty() ? null : itemFlags);
        this.set(path + "Unbreakable", meta.isUnbreakable() ? true : null);
    }

    @Nullable
    @Deprecated
    public ItemStack getItemEncoded(String path) {
        String compressed = this.getString(path);
        if (compressed == null) return null;

        return ItemNbt.decompress(compressed);
    }

    @Deprecated
    public void setItemEncoded(String path, @Nullable ItemStack item) {
        this.set(path, item == null ? null : ItemNbt.compress(item));
    }


    @Deprecated
    public ItemStack[] getItemsEncoded(String path) {
        return ItemNbt.decompress(this.getStringList(path));
    }

    @Deprecated
    public void setItemsEncoded(String path, List<ItemStack> item) {
        this.set(path, ItemNbt.compress(item));
    }
}
