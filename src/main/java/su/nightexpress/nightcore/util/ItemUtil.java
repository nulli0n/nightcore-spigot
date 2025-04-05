package su.nightexpress.nightcore.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.language.LangAssets;

import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

public class ItemUtil {

    public static final String TEXTURES_HOST = "http://textures.minecraft.net/texture/";

    @NotNull
    public static String getItemName(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String name = getItemName(meta);
            if (name != null) return name;
        }

        return LangAssets.get(item.getType());
    }

    @Nullable
    public static String getItemName(@NotNull ItemMeta meta) {
        if (Version.isAtLeast(Version.MC_1_21) && meta.hasItemName()) {
            return meta.getItemName();
        }
        if (meta.hasDisplayName()) {
            return meta.getDisplayName();
        }

        return null;
    }

    @NotNull
    public static String getSerializedName(@NotNull ItemStack item) {
        if (Version.isSpigot()) return getItemName(item);
        if (!item.hasItemMeta()) return LangAssets.get(item.getType());

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String name = getSerializedName(meta);
            if (name != null) return name;
        }

        return LangAssets.get(item.getType());
    }

    @Nullable
    public static String getSerializedName(@NotNull ItemMeta meta) {
        if (Version.isAtLeast(Version.MC_1_21) && meta.hasItemName()) {
            return getSerializedItemName(meta);
        }

        if (meta.hasDisplayName()) {
            return getSerializedDisplayName(meta);
        }

        return null;
    }

    @Nullable
    public static String getSerializedDisplayName(@NotNull ItemMeta meta) {
        if (Version.isSpigot()) return getItemName(meta);

        var displayName = meta.hasDisplayName() ? meta.displayName() : null;
        return displayName == null ? null : MiniMessage.miniMessage().serialize(displayName);
    }

    @Nullable
    public static String getSerializedItemName(@NotNull ItemMeta meta) {
        if (Version.isSpigot()) return getItemName(meta);
        if (Version.isBehind(Version.MC_1_21)) return null;

        return meta.hasItemName() ? MiniMessage.miniMessage().serialize(meta.itemName()) : null;
    }

    @NotNull
    public static List<String> getSerializedLore(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) return new ArrayList<>();

        ItemMeta meta = item.getItemMeta();

        return meta == null ? new ArrayList<>() : getSerializedLore(meta);
    }

    @NotNull
    public static List<String> getSerializedLore(@NotNull ItemMeta meta) {
        if (Version.isSpigot()) {
            List<String> lore = meta.getLore();
            return lore == null ? new ArrayList<>() : lore;
        }

        List<Component> lore = meta.lore();

        return lore == null ? new ArrayList<>() : lore.stream().map(MiniMessage.miniMessage()::serialize).toList();
    }

    public static void setDisplayName(@NotNull ItemMeta meta, @NotNull String name) {
        Version.software().setDisplayName(meta, name);
    }

    public static void setItemName(@NotNull ItemMeta meta, @NotNull String name) {
        Version.software().setItemName(meta, name);
    }

    public static void setLore(@NotNull ItemMeta meta, @NotNull List<String> lore) {
        Version.software().setLore(meta, lore);
    }

    public static void editMeta(@NotNull ItemStack item, @NotNull Consumer<ItemMeta> function) {
        editMeta(item, ItemMeta.class, function);
    }

    public static <T extends ItemMeta> void editMeta(@NotNull ItemStack item, @NotNull Class<T> clazz, @NotNull Consumer<T> function) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        if (!clazz.isAssignableFrom(meta.getClass())) return;

        T specific = clazz.cast(meta);

        function.accept(specific);
        item.setItemMeta(specific);
    }

    public static void hideAttributes(@NotNull ItemStack itemStack) {
        if (Version.isAtLeast(Version.MC_1_21_5)) {
            Version.software().hideComponents(itemStack);
            return;
        }

        editMeta(itemStack, meta -> hideAttributes(meta, itemStack.getType()));
    }

    private static void hideAttributes(@NotNull ItemMeta meta, @NotNull Material material) {
        if (Version.isBehind(Version.MC_1_21_5) && Version.isAtLeast(Version.MC_1_20_6)) {
            if (material.isItem()) {
                EquipmentSlot slot = material.getEquipmentSlot();
                material.getDefaultAttributeModifiers(slot).forEach((attribute, modifier) -> {
                    var modifiers = meta.getAttributeModifiers() == null ? null : meta.getAttributeModifiers(attribute);
                    if (modifiers == null || modifiers.isEmpty()) {
                        meta.addAttributeModifier(attribute, modifier);
                    }
                });
            }
        }

        meta.addItemFlags(ItemFlag.values());
    }

    @NotNull
    public static List<String> getLore(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return (meta == null || meta.getLore() == null) ? new ArrayList<>() : meta.getLore();
    }

    @NotNull
    @Deprecated
    public static ItemStack createCustomHead(@NotNull String texture) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        setSkullTexture(item, texture);
        return item;
    }

    @NotNull
    public static ItemStack getSkinHead(@NotNull String texture) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        setHeadSkin(item, texture);
        return item;
    }

    @Nullable
    public static PlayerProfile createSkinProfile(@NotNull String urlData) {
        if (urlData.isBlank()) return null;

        String name = urlData.substring(0, 16);

        if (!urlData.startsWith(TEXTURES_HOST)) {
            urlData = TEXTURES_HOST + urlData;
        }

        try {
            UUID uuid = UUID.nameUUIDFromBytes(urlData.getBytes());
            // If no name, then meta#getOwnerProfile will return 'null' (wtf?)
            // sometimes swtiching to "new" spigot api is a pain.
            // why the hell i have to dig into nms to learn that...
            PlayerProfile profile = Bukkit.createPlayerProfile(uuid, name);
            URL url = URI.create(urlData).toURL();//new URL(urlData);
            PlayerTextures textures = profile.getTextures();

            textures.setSkin(url);
            profile.setTextures(textures);
            return profile;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static void setHeadSkin(@NotNull ItemStack item, @NotNull String urlData) {
        editMeta(item, SkullMeta.class, meta -> {
            meta.setOwnerProfile(createSkinProfile(urlData));
        });
    }

    @Nullable
    public static String getHeadSkin(@NotNull ItemStack item) {
        if (item.getType() != Material.PLAYER_HEAD) return null;
        if (!(item.getItemMeta() instanceof SkullMeta meta)) return null;

        PlayerProfile profile = meta.getOwnerProfile();
        if (profile == null) return null;

        URL skin = profile.getTextures().getSkin();
        if (skin == null) return null;

        String raw = skin.toString();
        return raw.substring(TEXTURES_HOST.length());
    }

    @Deprecated
    public static void setSkullTexture(@NotNull ItemStack item, @NotNull String value) {
        if (item.getType() != Material.PLAYER_HEAD) return;

        try {
            byte[] decoded = Base64.getDecoder().decode(value);
            String decodedStr = new String(decoded, StandardCharsets.UTF_8);
            JsonElement element = JsonParser.parseString(decodedStr);

            String url = element.getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            url = url.substring(ItemUtil.TEXTURES_HOST.length());

            setHeadSkin(item, url);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static boolean isTool(@NotNull ItemStack item) {
        return isAxe(item) || isHoe(item) || isPickaxe(item) || isShovel(item);
    }

    public static boolean isArmor(@NotNull ItemStack item) {
        return isHelmet(item) || isChestplate(item) || isLeggings(item) || isBoots(item);
    }

    public static boolean isBow(@NotNull ItemStack item) {
        return item.getType() == Material.BOW || item.getType() == Material.CROSSBOW;
    }

    public static boolean isSword(@NotNull ItemStack item) {
        return Tag.ITEMS_SWORDS.isTagged(item.getType());
    }

    public static boolean isAxe(@NotNull ItemStack item) {
        return Tag.ITEMS_AXES.isTagged(item.getType());
    }

    public static boolean isTrident(@NotNull ItemStack item) {
        return item.getType() == Material.TRIDENT;
    }

    public static boolean isPickaxe(@NotNull ItemStack item) {
        return Tag.ITEMS_PICKAXES.isTagged(item.getType());
    }

    public static boolean isShovel(@NotNull ItemStack item) {
        return Tag.ITEMS_SHOVELS.isTagged(item.getType());
    }

    public static boolean isHoe(@NotNull ItemStack item) {
        return Tag.ITEMS_HOES.isTagged(item.getType());
    }

    public static boolean isElytra(@NotNull ItemStack item) {
        return item.getType() == Material.ELYTRA;
    }

    public static boolean isFishingRod(@NotNull ItemStack item) {
        return item.getType() == Material.FISHING_ROD;
    }

    public static boolean isHelmet(@NotNull ItemStack item) {
        return getEquipmentSlot(item) == EquipmentSlot.HEAD;
    }

    public static boolean isChestplate(@NotNull ItemStack item) {
        return getEquipmentSlot(item) == EquipmentSlot.CHEST;
    }

    public static boolean isLeggings(@NotNull ItemStack item) {
        return getEquipmentSlot(item) == EquipmentSlot.LEGS;
    }

    public static boolean isBoots(@NotNull ItemStack item) {
        return getEquipmentSlot(item) == EquipmentSlot.FEET;
    }

    @NotNull
    public static EquipmentSlot getEquipmentSlot(@NotNull ItemStack item) {
        Material material = item.getType();
        return material.isItem() ? material.getEquipmentSlot() : EquipmentSlot.HAND;
    }

    @Nullable
    @Deprecated
    public static String compress(@NotNull ItemStack item) {
        return ItemNbt.compress(item);
    }

    @Nullable
    @Deprecated
    public static ItemStack decompress(@NotNull String compressed) {
        return ItemNbt.decompress(compressed);
    }

    @NotNull
    @Deprecated
    public static List<String> compress(@NotNull ItemStack[] items) {
        return ItemNbt.compress(Arrays.asList(items));
    }

    @NotNull
    @Deprecated
    public static List<String> compress(@NotNull List<ItemStack> items) {
        return new ArrayList<>(items.stream().map(ItemNbt::compress).filter(Objects::nonNull).toList());
    }

    @Deprecated
    public static ItemStack[] decompress(@NotNull List<String> list) {
        List<ItemStack> items = list.stream().map(ItemNbt::decompress).filter(Objects::nonNull).toList();
        return items.toArray(new ItemStack[list.size()]);
    }
}
