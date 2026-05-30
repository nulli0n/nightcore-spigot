package su.nightexpress.nightcore.util;

import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.profile.CachedProfile;
import su.nightexpress.nightcore.util.profile.PlayerProfiles;
import su.nightexpress.nightcore.util.text.night.NightMessage;
import su.nightexpress.nightcore.util.text.night.ParserUtils;

public class ItemUtil {

    public static final String TEXTURES_HOST = "http://textures.minecraft.net/texture/";

    public static void editMeta(@NonNull ItemStack item, @NonNull Consumer<ItemMeta> consumer) {
        Software.get().editMeta(item, consumer);
    }

    public static <T extends ItemMeta> void editMeta(@NonNull ItemStack item, @NonNull Class<T> clazz,
                                                     @NonNull Consumer<T> consumer) {
        Software.get().editMeta(item, clazz, consumer);
    }

    @NonNull
    public static String getNameSerialized(@NonNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        String metaName = meta == null ? null : getNameSerialized(meta);
        if (metaName != null) return metaName;

        return LangUtil.getSerializedName(itemStack.getType());
    }

    @Nullable
    public static String getDisplayNameSerialized(@NonNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return null;

        return getNameSerialized(meta);
    }

    @Nullable
    public static String getNameSerialized(@NonNull ItemMeta meta) {
        String customName = getCustomNameSerialized(meta);
        if (customName != null) return customName;

        return getItemNameSerialized(meta);
    }

    @NonNull
    @Deprecated
    public static String getItemName(@NonNull ItemStack item) {
        return getNameSerialized(item);
    }

    @Nullable
    @Deprecated
    public static String getItemName(@NonNull ItemMeta meta) {
        return getNameSerialized(meta);
    }

    @NonNull
    @Deprecated
    public static String getSerializedName(@NonNull ItemStack item) {
        return getNameSerialized(item);
    }

    @Nullable
    @Deprecated
    public static String getSerializedName(@NonNull ItemMeta meta) {
        return getNameSerialized(meta);
    }

    @Nullable
    @Deprecated
    public static String getSerializedDisplayName(@NonNull ItemMeta meta) {
        return getCustomNameSerialized(meta);
    }

    @Nullable
    @Deprecated
    public static String getSerializedItemName(@NonNull ItemMeta meta) {
        return getItemNameSerialized(meta);
    }

    @NonNull
    @Deprecated
    public static List<String> getSerializedLore(@NonNull ItemStack item) {
        return getLoreSerialized(item);
    }

    @NonNull
    @Deprecated
    public static List<String> getSerializedLore(@NonNull ItemMeta meta) {
        return getLoreSerialized(meta);
    }

    @Deprecated
    public static void setDisplayName(@NonNull ItemMeta meta, @NonNull String name) {
        setCustomName(meta, name);
    }

    @Nullable
    public static String getCustomNameSerialized(@NonNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return meta == null ? null : getCustomNameSerialized(meta);
    }

    @Nullable
    public static String getCustomNameSerialized(@NonNull ItemMeta meta) {
        return Software.get().getCustomName(meta);
    }

    public static void setCustomName(@NonNull ItemStack itemStack, @NonNull String name) {
        editMeta(itemStack, meta -> setCustomName(meta, name));
    }

    public static void setCustomName(@NonNull ItemMeta meta, @NonNull String name) {
        Software.get().setCustomName(meta, NightMessage.parse(name));
    }

    public static void setCustomName(@NonNull ItemMeta meta, @Nullable NightComponent name) {
        Software.get().setCustomName(meta, name);
    }

    @Nullable
    public static String getItemNameSerialized(@NonNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return meta == null ? null : getItemNameSerialized(meta);
    }

    @Nullable
    public static String getItemNameSerialized(@NonNull ItemMeta meta) {
        return Software.get().getItemName(meta);
    }

    public static void setItemName(@NonNull ItemStack itemStack, @NonNull String name) {
        editMeta(itemStack, meta -> setItemName(meta, name));
    }

    public static void setItemName(@NonNull ItemMeta meta, @NonNull String name) {
        Software.get().setItemName(meta, NightMessage.parse(name));
    }

    @NonNull
    @Deprecated
    public static List<String> getLore(@NonNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return (meta == null || meta.getLore() == null) ? new ArrayList<>() : meta.getLore();
    }

    @NonNull
    public static List<String> getLoreSerialized(@NonNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return meta == null ? new ArrayList<>() : getLoreSerialized(meta);
    }

    @NonNull
    public static List<String> getLoreSerialized(@NonNull ItemMeta meta) {
        List<String> lore = Software.get().getLore(meta);
        return lore == null ? new ArrayList<>() : lore;
    }

    public static void setLore(@NonNull ItemStack itemStack, @NonNull List<String> lore) {
        editMeta(itemStack, meta -> setLore(meta, lore));
    }

    public static void setLore(@NonNull ItemMeta meta, @NonNull List<String> lore) {
        // It seems that direct '\n' usage is not supported for item meta anymore since ~1.21.7.
        setItemLore(meta, Lists.modify(ParserUtils.breakDownLineSplitters(lore), NightMessage::parse));
    }

    public static void setItemLore(@NonNull ItemMeta meta, @NonNull List<NightComponent> lore) {
        Software.get().setLore(meta, lore);
    }

    @Deprecated
    public static void hideAttributes(@NonNull ItemStack itemStack) {
        Software.get().hideComponents(itemStack);
    }

    public static void setCustomModelData(@NonNull ItemStack itemStack, float data) {
        ItemUtil.editMeta(itemStack, meta -> setCustomModelData(meta, data));
    }

    public static void setCustomModelData(@NonNull ItemMeta meta, float data) {
        CustomModelDataComponent component = meta.getCustomModelDataComponent();
        component.setFloats(Lists.newList(data));
        meta.setCustomModelDataComponent(component);
    }

    @Nullable
    public static Float getCustomModelData(@NonNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return meta == null ? null : getCustomModelData(meta);
    }

    @Nullable
    public static Float getCustomModelData(@NonNull ItemMeta meta) {
        List<Float> floats = meta.getCustomModelDataComponent().getFloats();
        return floats.isEmpty() ? null : floats.getFirst();
    }

    @NonNull
    @Deprecated
    public static ItemStack createCustomHead(@NonNull String texture) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        setSkullTexture(item, texture);
        return item;
    }

    @NonNull
    @Deprecated
    public static ItemStack getSkinHead(@NonNull String texture) {
        return getCustomHead(texture);
    }

    @NonNull
    public static ItemStack getCustomHead(@NonNull String texture) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        setProfileBySkinURL(item, texture);
        return item;
    }

    @Nullable
    @Deprecated
    public static PlayerProfile createSkinProfile(@NonNull String urlData) {
        if (urlData.isBlank()) return null;

        String name = urlData.substring(0, 16);

        if (!urlData.startsWith(TEXTURES_HOST)) {
            urlData = TEXTURES_HOST + urlData;
        }

        try {
            UUID uuid = UUID.nameUUIDFromBytes(urlData.getBytes());
            // If no name, then meta#getOwnerProfile will return 'null'.
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

    @Deprecated
    public static void setHeadSkin(@NonNull ItemStack item, @NonNull String urlData) {
        setProfileBySkinURL(item, urlData);
    }

    @Nullable
    @Deprecated
    public static String getHeadSkin(@NonNull ItemStack item) {
        return getProfileSkinURL(item);
    }

    @Nullable
    public static NightProfile getOwnerProfile(@NonNull ItemStack itemStack) {
        return Software.get().getOwnerProfile(itemStack);
    }

    @Nullable
    public static String getProfileSkinURL(@NonNull ItemStack itemStack) {
        NightProfile profile = getOwnerProfile(itemStack);
        if (profile == null) return null;

        return PlayerProfiles.getProfileSkinURL(profile);
    }

    public static void setSkinURL(@NonNull ItemStack itemStack, @NonNull String urlData) {
        editMeta(itemStack, SkullMeta.class,
            skullMeta -> PlayerProfiles.createStaticTexturedProfile(urlData).apply(skullMeta));
    }

    public static void setProfileBySkinURL(@NonNull ItemStack itemStack, @NonNull String urlData) {
        CachedProfile profile = PlayerProfiles.createProfileBySkinURL(urlData);
        if (profile == null) return;

        editMeta(itemStack, SkullMeta.class, skullMeta -> profile.query().apply(skullMeta));
    }

    @Deprecated
    public static void setSkullTexture(@NonNull ItemStack item, @NonNull String value) {
        if (item.getType() != Material.PLAYER_HEAD) return;

        try {
            byte[] decoded = Base64.getDecoder().decode(value);
            String decodedStr = new String(decoded, StandardCharsets.UTF_8);
            JsonElement element = JsonParser.parseString(decodedStr);

            String url = element.getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("SKIN").get("url")
                .getAsString();
            url = url.substring(ItemUtil.TEXTURES_HOST.length());

            setProfileBySkinURL(item, url);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static boolean isTool(@NonNull ItemStack item) {
        return isAxe(item) || isHoe(item) || isPickaxe(item) || isShovel(item);
    }

    public static boolean isArmor(@NonNull ItemStack item) {
        return isHelmet(item) || isChestplate(item) || isLeggings(item) || isBoots(item);
    }

    public static boolean isBow(@NonNull ItemStack item) {
        return item.getType() == Material.BOW || item.getType() == Material.CROSSBOW;
    }

    public static boolean isSword(@NonNull ItemStack item) {
        return Tag.ITEMS_SWORDS.isTagged(item.getType());
    }

    public static boolean isAxe(@NonNull ItemStack item) {
        return Tag.ITEMS_AXES.isTagged(item.getType());
    }

    @Deprecated
    public static boolean isTrident(@NonNull ItemStack item) {
        return item.getType() == Material.TRIDENT;
    }

    public static boolean isPickaxe(@NonNull ItemStack item) {
        return Tag.ITEMS_PICKAXES.isTagged(item.getType());
    }

    public static boolean isShovel(@NonNull ItemStack item) {
        return Tag.ITEMS_SHOVELS.isTagged(item.getType());
    }

    public static boolean isHoe(@NonNull ItemStack item) {
        return Tag.ITEMS_HOES.isTagged(item.getType());
    }

    @Deprecated
    public static boolean isElytra(@NonNull ItemStack item) {
        return item.getType() == Material.ELYTRA;
    }

    @Deprecated
    public static boolean isFishingRod(@NonNull ItemStack item) {
        return item.getType() == Material.FISHING_ROD;
    }

    public static boolean isHelmet(@NonNull ItemStack item) {
        return getEquipmentSlot(item) == EquipmentSlot.HEAD;
    }

    public static boolean isChestplate(@NonNull ItemStack item) {
        return getEquipmentSlot(item) == EquipmentSlot.CHEST;
    }

    public static boolean isLeggings(@NonNull ItemStack item) {
        return getEquipmentSlot(item) == EquipmentSlot.LEGS;
    }

    public static boolean isBoots(@NonNull ItemStack item) {
        return getEquipmentSlot(item) == EquipmentSlot.FEET;
    }

    @NonNull
    public static EquipmentSlot getEquipmentSlot(@NonNull ItemStack item) {
        Material material = item.getType();
        return material.isItem() ? material.getEquipmentSlot() : EquipmentSlot.HAND;
    }

    @Nullable
    @Deprecated
    public static String compress(@NonNull ItemStack item) {
        return ItemNbt.compress(item);
    }

    @Nullable
    @Deprecated
    public static ItemStack decompress(@NonNull String compressed) {
        return ItemNbt.decompress(compressed);
    }

    @NonNull
    @Deprecated
    public static List<String> compress(@NonNull ItemStack[] items) {
        return ItemNbt.compress(Arrays.asList(items));
    }

    @NonNull
    @Deprecated
    public static List<String> compress(@NonNull List<ItemStack> items) {
        return new ArrayList<>(items.stream().map(ItemNbt::compress).filter(Objects::nonNull).toList());
    }

    @Deprecated
    public static ItemStack[] decompress(@NonNull List<String> list) {
        List<ItemStack> items = list.stream().map(ItemNbt::decompress).filter(Objects::nonNull).toList();
        return items.toArray(new ItemStack[list.size()]);
    }
}
