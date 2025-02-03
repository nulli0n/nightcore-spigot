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
            if (Version.isAtLeast(Version.MC_1_21) && meta.hasItemName()) {
                // MiniMessage.miniMessage().serialize(meta.itemName()));
                return meta.getItemName();
            }
            else if (meta.hasDisplayName()) {
                // MiniMessage.miniMessage().serialize(meta.displayName()));
                return meta.getDisplayName();
            }
        }

        return LangAssets.get(item.getType());
    }

    @NotNull
    public static String getSerializedName(@NotNull ItemStack item) {
        if (Version.isSpigot()) return getItemName(item);
        if (!item.hasItemMeta()) return LangAssets.get(item.getType());

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (Version.isAtLeast(Version.MC_1_21) && meta.hasItemName()) {
                return MiniMessage.miniMessage().serialize(meta.itemName());
            }
            else if (meta.hasCustomName()) {
                var customName = meta.customName();
                if (customName != null) {
                    return MiniMessage.miniMessage().serialize(customName);
                }
            }
        }

        return LangAssets.get(item.getType());
    }

    @NotNull
    public static List<String> getSerializedLore(@NotNull ItemStack item) {
        if (Version.isSpigot()) return getLore(item);
        if (!item.hasItemMeta()) return new ArrayList<>();

        ItemMeta meta = item.getItemMeta();
        List<Component> lore = meta == null ? null : meta.lore();
        if (lore == null) return new ArrayList<>();

        return lore.stream().map(MiniMessage.miniMessage()::serialize).toList();
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
        editMeta(itemStack, meta -> hideAttributes(meta, itemStack.getType()));
    }

    public static void hideAttributes(@NotNull ItemMeta meta, @NotNull Material material) {
        if (Version.isAtLeast(Version.MC_1_20_6) && material.isItem()) {
            EquipmentSlot slot = material.getEquipmentSlot();
            material.getDefaultAttributeModifiers(slot).forEach((attribute, modifier) -> {
                if (meta.hasAttributeModifiers()) return;

                var modifiers = meta.getAttributeModifiers(attribute);
                if (modifiers == null || modifiers.isEmpty()) {
                    meta.addAttributeModifier(attribute, modifier);
                }
            });
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
//        if (urlData.isBlank()) return;
//        if (item.getType() != Material.PLAYER_HEAD) return;
//        if (!(item.getItemMeta() instanceof SkullMeta meta)) return;
//
//        String name = urlData.substring(0, 16);
//
//        if (!urlData.startsWith(TEXTURES_HOST)) {
//            urlData = TEXTURES_HOST + urlData;
//        }
//
//        try {
//            UUID uuid = UUID.nameUUIDFromBytes(urlData.getBytes());
//            // If no name, then meta#getOwnerProfile will return 'null' (wtf?)
//            // sometimes swtiching to "new" spigot api is a pain.
//            // why the hell i have to dig into nms to learn that...
//            PlayerProfile profile = Bukkit.createPlayerProfile(uuid, name);
//            URL url = URI.create(urlData).toURL();//new URL(urlData);
//            PlayerTextures textures = profile.getTextures();
//
//            textures.setSkin(url);
//            profile.setTextures(textures);
//            meta.setOwnerProfile(profile);
//            item.setItemMeta(meta);
//        }
//        catch (Exception exception) {
//            exception.printStackTrace();
//        }
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
