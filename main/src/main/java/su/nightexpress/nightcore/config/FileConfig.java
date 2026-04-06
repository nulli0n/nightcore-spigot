package su.nightexpress.nightcore.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.ConfigType;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.bukkit.NightSound;
import su.nightexpress.nightcore.util.sound.AbstractSound;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class FileConfig extends YamlConfiguration {

    public static final String EXTENSION = ".yml";

    private final Path path;

    private boolean changed;

    @Deprecated
    public FileConfig(@NonNull String path, @NonNull String file) {
        this(new File(path, file));
    }

    @Deprecated
    public FileConfig(@NonNull File file) {
        this.changed = false;
        this.options().width(512);

        FileUtil.create(file);
        this.path = file.toPath();
        this.reload();
    }

    private FileConfig(@NonNull Path path) {
        this.path = path;
        this.changed = false;
        this.options().width(512);
    }

    @NonNull
    public static FileConfig load(@NonNull String path, @NonNull String file) {
        return load(Path.of(path, file));
    }

    @NonNull
    public static FileConfig load(@NonNull Path path) {
        FileConfig config = new FileConfig(path);
        config.load();
        return config;
    }

    public static boolean isConfig(@NonNull File file) {
        return file.getName().endsWith(EXTENSION);
    }

    @NonNull
    public static String withExtension(@NonNull String fileName) {
        return fileName + EXTENSION;
    }

    @NonNull
    public static String getName(@NonNull File file) {
        String name = file.getName();

        if (isConfig(file)) {
            return name.substring(0, name.length() - EXTENSION.length());
        }
        return name;
    }

    @NonNull
    @Deprecated
    public static FileConfig loadOrExtract(@NonNull NightCorePlugin plugin, @NonNull String path, @NonNull String file) {
        if (!path.endsWith("/")) {
            path += "/";
        }
        return loadOrExtract(plugin, path + file);
    }

    @NonNull
    @Deprecated
    public static FileConfig loadOrExtract(@NonNull NightCorePlugin plugin, @NonNull String filePath) {
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

    @NonNull
    public static List<FileConfig> loadAll(@NonNull String path) {
        return loadAll(path, false);
    }

    @NonNull
    public static List<FileConfig> loadAll(@NonNull String path, boolean deep) {
        return FileUtil.getConfigFiles(path, deep).stream().map(FileConfig::new).toList();
    }

    public void initializeOptions(@NonNull Class<?> clazz) {
        initializeOptions(clazz, this);
    }

    public static void initializeOptions(@NonNull Class<?> clazz, @NonNull FileConfig config) {
        for (ConfigValue<?> value : Reflex.getStaticFields(clazz, ConfigValue.class, false)) {
            value.read(config);
        }
    }

    @NonNull
    @Deprecated
    public File getFile() {
        return this.path.toFile();
    }

    @NonNull
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

        //this.load(this.file);
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

    public void edit(@NonNull Consumer<FileConfig> consumer) {
        consumer.accept(this);
        this.saveChanges();
    }

    public boolean addMissing(@NonNull String path, @Nullable Object val) {
        if (this.contains(path)) return false;
        this.set(path, val);
        return true;
    }

    @NonNull
    public <T> T get(@NonNull ConfigProperty<T> property) {
        return property.read(this);
    }

    @NonNull
    public <T> T get(@NonNull ConfigType<T> type, @NonNull String path, @NonNull T def, @Nullable String... comments) {
        if (!this.contains(path)) {
            type.write(this, path, def);
            this.setComments(path, comments);
        }
        return type.read(this, path, def);
    }

    public void setArray(@NonNull String path, double[] array) {
        this.set(path, array == null ? null : ArrayUtil.arrayToString(array));
    }

    public void setArray(@NonNull String path, int[] array) {
        this.set(path, array == null ? null : ArrayUtil.arrayToString(array));
    }

    public void setArray(@NonNull String path, long[] array) {
        this.set(path, array == null ? null : ArrayUtil.arrayToString(array));
    }

    @Override
    public void set(@NonNull String path, @Nullable Object value) {
        if (value instanceof Writeable writeable) {
            writeable.write(this, path);
            this.changed = true;
            return;
        }
        else if (value instanceof UUID uuid) {
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

    public void setComments(@NonNull String path, @Nullable String... comments) {
        this.setComments(path, comments == null ? null : Arrays.asList(comments));
    }

    public void setInlineComments(@NonNull String path, @Nullable String... comments) {
        this.setInlineComments(path, comments == null ? null : Arrays.asList(comments));
    }

    @Override
    public void setComments(@NonNull String path, @Nullable List<String> comments) {
        if (!this.areCommentsDifferent(this.getComments(path), comments)) return;

        super.setComments(path, comments);
        this.changed = true;
    }

    @Override
    public void setInlineComments(@NonNull String path, @Nullable List<String> comments) {
        if (!this.areCommentsDifferent(this.getInlineComments(path), comments)) return;

        super.setInlineComments(path, comments);
        this.changed = true;
    }

    private boolean areCommentsDifferent(@NonNull List<String> current, @Nullable List<String> comments) {
        if ((comments == null || comments.isEmpty())) {
            return !current.isEmpty();
        }

        return !new HashSet<>(current).equals(new HashSet<>(comments));
    }

    public boolean remove(@NonNull String path) {
        if (!this.contains(path)) return false;
        this.set(path, null);
        return true;
    }

    @NonNull
    public Set<String> getSection(@NonNull String path) {
        ConfigurationSection section = this.getConfigurationSection(path);
        return section == null ? Collections.emptySet() : section.getKeys(false);
    }

    @Nullable
    public UUID getUUID(@NonNull String path) {
        return this.getUUID(path, null);
    }

    @Nullable
    public UUID getUUID(@NonNull String path, @Nullable UUID def) {
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

    @NonNull
    public String getStringOrEmpty(@NonNull String path) {
        String str = super.getString(path);
        return str == null ? "" : str;
    }

    @Override
    @Nullable
    public String getString(@NonNull String path) {
        String str = super.getString(path);
        return str == null || str.isEmpty() ? null : str;
//        return super.getString(path);
    }

    @Override
    @NonNull
    public String getString(@NonNull String path, @Nullable String def) {
        String str = super.getString(path, def);
        return str == null ? "" : str;
    }

    @NonNull
    public Set<String> getStringSet(@NonNull String path) {
        return new HashSet<>(this.getStringList(path));
    }

    @Override
    @Nullable
    @Deprecated
    public Location getLocation(@NonNull String path) {
        String raw = this.getString(path);
        return raw == null ? null : LocationUtil.deserialize(raw);
    }

    @Deprecated
    public void setIntArray(@NonNull String path, int[] arr) {
        this.setArray(path, arr);
    }

    public int[] getIntArray(@NonNull String path) {
        return getIntArray(path, new int[0]);
    }

    public int[] getIntArray(@NonNull String path, int[] defaultArray) {
        String str = this.getString(path);
        return str == null ? defaultArray : ArrayUtil.parseIntArray(str);
    }

    public double[] getDoubleArray(@NonNull String path) {
        return getDoubleArray(path, new double[0]);
    }

    public double[] getDoubleArray(@NonNull String path, double[] defaultArray) {
        String str = this.getString(path);
        return str == null ? defaultArray : ArrayUtil.parseDoubleArray(str);
    }

    public long[] getLongArray(@NonNull String path) {
        return getLongArray(path, new long[0]);
    }

    public long[] getLongArray(@NonNull String path, long[] defaultArray) {
        String str = this.getString(path);
        return str == null ? defaultArray : ArrayUtil.parseLongArray(str);
    }

    @NonNull
    public String[] getStringArray(@NonNull String path) {
        return this.getStringArray(path, new String[0]);
    }

    @NonNull
    public String[] getStringArray(@NonNull String path, @NonNull String[] def) {
        String str = this.getString(path);
        return str == null ? def : str.split(",");
    }

    public void setStringArray(@NonNull String path, String[] arr) {
        this.set(path, arr == null ? null : String.join(",", arr));
    }

    @Nullable
    public <T extends Enum<T>> T getEnum(@NonNull String path, @NonNull Class<T> clazz) {
        return Enums.get(this.getString(path), clazz);
    }

    @NonNull
    public <T extends Enum<T>> T getEnum(@NonNull String path, @NonNull Class<T> clazz, @NonNull T def) {
        return Enums.parse(this.getString(path), clazz).orElse(def);
    }

    @NonNull
    public <T extends Enum<T>> List<T> getEnumList(@NonNull String path, @NonNull Class<T> clazz) {
        return this.getStringSet(path).stream().map(str -> Enums.parse(str, clazz).orElse(null))
            .filter(Objects::nonNull).toList();
    }

    /*@NonNull
    public Set<FireworkEffect> getFireworkEffects(@NonNull String path) {
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

    @NonNull
    public NightItem getCosmeticItem(@NonNull String path, @NonNull NightItem def) {
        return this.contains(path) ? this.getCosmeticItem(path) : def;
    }

    @NonNull
    public NightItem getCosmeticItem(@NonNull String path) {
        return NightItem.read(this, path);
    }

    @NonNull
    @Deprecated
    public NightSound getSound(@NonNull String path) {
        return NightSound.read(this, path); // Update
    }

    @Nullable
    public su.nightexpress.nightcore.bridge.wrap.NightSound readSound(@NonNull String path) {
        return AbstractSound.read(this, path);
    }

    @Nullable
    public su.nightexpress.nightcore.bridge.wrap.NightSound readSound(@NonNull String path, su.nightexpress.nightcore.bridge.wrap.@NonNull NightSound def) {
        su.nightexpress.nightcore.bridge.wrap.NightSound sound = this.readSound(path);
        return sound == null ? def : sound;
    }

    @Deprecated
    public void setSound(@NonNull String path, @NonNull NightSound sound) {
        this.set(path, sound);
    }

    @NonNull
    @Deprecated
    public ItemStack getItem(@NonNull String path, @Nullable ItemStack def) {
        ItemStack item = this.getItem(path);
        return item.getType().isAir() && def != null ? def : item;
    }

    @NonNull
    @Deprecated
    public ItemStack getItem(@NonNull String path) {
        if (!path.isEmpty() && !path.endsWith(".")) path = path + ".";

        Material material = BukkitThing.getMaterial(this.getString(path + "Material", BukkitThing.toString(Material.AIR)));
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

                String url = element.getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
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
            flags.stream().map(str -> StringUtil.getEnum(str, ItemFlag.class).orElse(null)).filter(Objects::nonNull).forEach(meta::addItemFlags);
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
    public void setItem(@NonNull String path, @Nullable ItemStack item) {
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
    public ItemStack getItemEncoded(@NonNull String path) {
        String compressed = this.getString(path);
        if (compressed == null) return null;

        return ItemNbt.decompress(compressed);
    }

    @Deprecated
    public void setItemEncoded(@NonNull String path, @Nullable ItemStack item) {
        this.set(path, item == null ? null : ItemNbt.compress(item));
    }

    @NonNull
    @Deprecated
    public ItemStack[] getItemsEncoded(@NonNull String path) {
        return ItemNbt.decompress(this.getStringList(path));
    }

    @Deprecated
    public void setItemsEncoded(@NonNull String path, @NonNull List<ItemStack> item) {
        this.set(path, ItemNbt.compress(item));
    }
}
