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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.IntStream;

public class FileConfig extends YamlConfiguration {

    public static final String EXTENSION = ".yml";

    private final File    file;
    private       boolean changed;

    public FileConfig(@NotNull String path, @NotNull String file) {
        this(new File(path, file));
    }

    public FileConfig(@NotNull File file) {
        this.changed = false;
        this.options().width(512);

        FileUtil.create(file);
        this.file = file;
        this.reload();
    }

    public static boolean isConfig(@NotNull File file) {
        return file.getName().endsWith(EXTENSION);
    }

    @NotNull
    public static String getName(@NotNull File file) {
        String name = file.getName();

        if (isConfig(file)) {
            return name.substring(0, name.length() - EXTENSION.length());
        }
        return name;
    }

    @NotNull
    public static FileConfig loadOrExtract(@NotNull NightCorePlugin plugin, @NotNull String path, @NotNull String file) {
        if (!path.endsWith("/")) {
            path += "/";
        }
        return loadOrExtract(plugin, path + file);
    }

    @NotNull
    public static FileConfig loadOrExtract(@NotNull NightCorePlugin plugin, @NotNull String filePath) {
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

    @NotNull
    public static List<FileConfig> loadAll(@NotNull String path) {
        return loadAll(path, false);
    }

    @NotNull
    public static List<FileConfig> loadAll(@NotNull String path, boolean deep) {
        return FileUtil.getConfigFiles(path, deep).stream().map(FileConfig::new).toList();
    }

    public void initializeOptions(@NotNull Class<?> clazz) {
        initializeOptions(clazz, this);
    }

    public static void initializeOptions(@NotNull Class<?> clazz, @NotNull FileConfig config) {
        for (ConfigValue<?> value : Reflex.getFields(clazz, ConfigValue.class)) {
            value.read(config);
        }
    }

    @NotNull
    public File getFile() {
        return this.file;
    }

    public void save() {
        try {
            this.save(this.file);
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

    public boolean reload() {
        try {
            this.load(this.file);
            this.changed = false;
            return true;
        }
        catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean addMissing(@NotNull String path, @Nullable Object val) {
        if (this.contains(path)) return false;
        this.set(path, val);
        return true;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if (value instanceof String str) {
            value = Colorizer.plain(str);
        }
        else if (value instanceof Collection<?> collection) {
            List<Object> list = new ArrayList<>(collection);
            list.replaceAll(obj -> obj instanceof String str ? Colorizer.plain(str) : obj);
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

    public void setComments(@NotNull String path, @Nullable String... comments) {
        this.setComments(path, Arrays.asList(comments));
    }

    public void setInlineComments(@NotNull String path, @Nullable String... comments) {
        this.setInlineComments(path, Arrays.asList(comments));
    }

    @Override
    public void setComments(@NotNull String path, @Nullable List<String> comments) {
        if (this.getComments(path).equals(comments)) return;

        super.setComments(path, comments);
        this.changed = true;
    }

    @Override
    public void setInlineComments(@NotNull String path, @Nullable List<String> comments) {
        super.setInlineComments(path, comments);
    }

    public boolean remove(@NotNull String path) {
        if (!this.contains(path)) return false;
        this.set(path, null);
        return true;
    }

    @NotNull
    public Set<String> getSection(@NotNull String path) {
        ConfigurationSection section = this.getConfigurationSection(path);
        return section == null ? Collections.emptySet() : section.getKeys(false);
    }

    @Override
    @Nullable
    public String getString(@NotNull String path) {
        String str = super.getString(path);
        return str == null || str.isEmpty() ? null : str;
    }

    @Override
    @NotNull
    public String getString(@NotNull String path, @Nullable String def) {
        String str = super.getString(path, def);
        return str == null ? "" : str;
    }

    @NotNull
    public Set<String> getStringSet(@NotNull String path) {
        return new HashSet<>(this.getStringList(path));
    }

    @Override
    @Nullable
    public Location getLocation(@NotNull String path) {
        String raw = this.getString(path);
        return raw == null ? null : LocationUtil.deserialize(raw);
    }

    public int[] getIntArray(@NotNull String path) {
        return getIntArray(path, new int[0]);
    }

    public int[] getIntArray(@NotNull String path, int[] def) {
        String str = this.getString(path);
        return str == null ? def : NumberUtil.getIntArray(str);
    }

    public void setIntArray(@NotNull String path, int[] arr) {
        if (arr == null) {
            this.set(path, null);
            return;
        }
        this.set(path, String.join(",", IntStream.of(arr).boxed().map(String::valueOf).toList()));
    }

    @NotNull
    public String[] getStringArray(@NotNull String path, @NotNull String[] def) {
        String str = this.getString(path);
        return str == null ? def : str.split(",");
    }

    public void setStringArray(@NotNull String path, String[] arr) {
        if (arr == null) {
            this.set(path, null);
            return;
        }
        this.set(path, String.join(",", arr));
    }

    @Nullable
    public <T extends Enum<T>> T getEnum(@NotNull String path, @NotNull Class<T> clazz) {
        return StringUtil.getEnum(this.getString(path), clazz).orElse(null);
    }

    @NotNull
    public <T extends Enum<T>> T getEnum(@NotNull String path, @NotNull Class<T> clazz, @NotNull T def) {
        return StringUtil.getEnum(this.getString(path), clazz).orElse(def);
    }

    @NotNull
    public <T extends Enum<T>> List<T> getEnumList(@NotNull String path, @NotNull Class<T> clazz) {
        return this.getStringSet(path).stream().map(str -> StringUtil.getEnum(str, clazz).orElse(null))
            .filter(Objects::nonNull).toList();
    }

    /*@NotNull
    public Set<FireworkEffect> getFireworkEffects(@NotNull String path) {
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

    @NotNull
    public ItemStack getItem(@NotNull String path, @Nullable ItemStack def) {
        ItemStack item = this.getItem(path);
        return item.getType().isAir() && def != null ? def : item;
    }

    @NotNull
    public ItemStack getItem(@NotNull String path) {
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
            ItemUtil.setHeadSkin(item, headSkin);
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        int durability = this.getInt(path + "Durability");
        if (durability > 0 && meta instanceof Damageable damageable) {
            damageable.setDamage(durability);
        }

        String name = this.getString(path + "Name");
        meta.setDisplayName(name != null ? NightMessage.asLegacy(name) : null);
        meta.setLore(NightMessage.asLegacy(this.getStringList(path + "Lore")));

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

    public void setItem(@NotNull String path, @Nullable ItemStack item) {
        if (item == null) {
            this.set(path, null);
            return;
        }

        if (!path.endsWith(".")) path = path + ".";
        this.set(path.substring(0, path.length() - 1), null);

        Material material = item.getType();
        this.set(path + "Material", material.name());
        this.set(path + "Amount", item.getAmount() <= 1 ? null : item.getAmount());
        this.set(path + "SkinURL", ItemUtil.getHeadSkin(item));
//        if (!this.contains(path + "SkinURL")) {
//            this.set(path + "Head_Texture", ItemUtil.getSkullTexture(item));
//        }

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
    public ItemStack getItemEncoded(@NotNull String path) {
        String compressed = this.getString(path);
        if (compressed == null) return null;

        return ItemNbt.decompress(compressed);
    }

    public void setItemEncoded(@NotNull String path, @Nullable ItemStack item) {
        this.set(path, item == null ? null : ItemNbt.compress(item));
    }

    @NotNull
    public ItemStack[] getItemsEncoded(@NotNull String path) {
        return ItemNbt.decompress(this.getStringList(path));
    }

    public void setItemsEncoded(@NotNull String path, @NotNull List<ItemStack> item) {
        this.set(path, ItemNbt.compress(item));
    }
}
