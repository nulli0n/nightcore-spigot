package su.nightexpress.nightcore.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.profile.CachedProfile;
import su.nightexpress.nightcore.util.profile.PlayerProfiles;
import su.nightexpress.nightcore.util.text.night.NightMessage;
import su.nightexpress.nightcore.util.text.night.ParserUtils;

import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

public class ItemUtil {

    public static final String TEXTURES_HOST = "http://textures.minecraft.net/texture/";

    public static void editMeta(@NotNull ItemStack item, @NotNull Consumer<ItemMeta> consumer) {
        Software.get().editMeta(item, consumer);
    }

    public static <T extends ItemMeta> void editMeta(@NotNull ItemStack item, @NotNull Class<T> clazz, @NotNull Consumer<T> consumer) {
        Software.get().editMeta(item, clazz, consumer);
    }

    @NotNull
    public static String getNameSerialized(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        String metaName = meta == null ? null : getNameSerialized(meta);
        if (metaName != null) return metaName;

        return LangUtil.getSerializedName(itemStack.getType());
    }

    @Nullable
    public static String getDisplayNameSerialized(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return null;

        return getNameSerialized(meta);
    }

    @Nullable
    public static String getNameSerialized(@NotNull ItemMeta meta) {
        String customName = getCustomNameSerialized(meta);
        if (customName != null) return customName;

        return getItemNameSerialized(meta);
    }

    @NotNull
    @Deprecated
    public static String getItemName(@NotNull ItemStack item) {
        return getNameSerialized(item);
    }

    @Nullable
    @Deprecated
    public static String getItemName(@NotNull ItemMeta meta) {
        return getNameSerialized(meta);
    }

    @NotNull
    @Deprecated
    public static String getSerializedName(@NotNull ItemStack item) {
        return getNameSerialized(item);
    }

    @Nullable
    @Deprecated
    public static String getSerializedName(@NotNull ItemMeta meta) {
        return getNameSerialized(meta);
    }

    @Nullable
    @Deprecated
    public static String getSerializedDisplayName(@NotNull ItemMeta meta) {
        return getCustomNameSerialized(meta);
    }

    @Nullable
    @Deprecated
    public static String getSerializedItemName(@NotNull ItemMeta meta) {
        return getItemNameSerialized(meta);
    }

    @NotNull
    @Deprecated
    public static List<String> getSerializedLore(@NotNull ItemStack item) {
        return getLoreSerialized(item);
    }

    @NotNull
    @Deprecated
    public static List<String> getSerializedLore(@NotNull ItemMeta meta) {
        return getLoreSerialized(meta);
    }

    @Deprecated
    public static void setDisplayName(@NotNull ItemMeta meta, @NotNull String name) {
        setCustomName(meta, name);
    }

    @Nullable
    public static String getCustomNameSerialized(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return meta == null ? null : getCustomNameSerialized(meta);
    }

    @Nullable
    public static String getCustomNameSerialized(@NotNull ItemMeta meta) {
        return Software.get().getCustomName(meta);
    }

    public static void setCustomName(@NotNull ItemStack itemStack, @NotNull String name) {
        editMeta(itemStack, meta -> setCustomName(meta, name));
    }

    public static void setCustomName(@NotNull ItemMeta meta, @NotNull String name) {
        Software.get().setCustomName(meta, NightMessage.parse(name));
    }

    public static void setCustomName(@NotNull ItemMeta meta, @Nullable NightComponent name) {
        Software.get().setCustomName(meta, name);
    }

    @Nullable
    public static String getItemNameSerialized(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return meta == null ? null : getItemNameSerialized(meta);
    }

    @Nullable
    public static String getItemNameSerialized(@NotNull ItemMeta meta) {
        return Software.get().getItemName(meta);
    }

    public static void setItemName(@NotNull ItemStack itemStack, @NotNull String name) {
        editMeta(itemStack, meta -> setItemName(meta, name));
    }

    public static void setItemName(@NotNull ItemMeta meta, @NotNull String name) {
        Software.get().setItemName(meta, NightMessage.parse(name));
    }

    @NotNull
    @Deprecated
    public static List<String> getLore(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return (meta == null || meta.getLore() == null) ? new ArrayList<>() : meta.getLore();
    }

    @NotNull
    public static List<String> getLoreSerialized(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return meta == null ? new ArrayList<>() : getLoreSerialized(meta);
    }

    @NotNull
    public static List<String> getLoreSerialized(@NotNull ItemMeta meta) {
        List<String> lore = Software.get().getLore(meta);
        return lore == null ? new ArrayList<>() : lore;
    }

    public static void setLore(@NotNull ItemStack itemStack, @NotNull List<String> lore) {
        editMeta(itemStack, meta -> setLore(meta, lore));
    }

    public static void setLore(@NotNull ItemMeta meta, @NotNull List<String> lore) {
        // It seems that direct '\n' usage is not supported for item meta anymore since ~1.21.7.
        setItemLore(meta, Lists.modify(ParserUtils.breakDownLineSplitters(lore), NightMessage::parse));
    }

    public static void setItemLore(@NotNull ItemMeta meta, @NotNull List<NightComponent> lore) {
        Software.get().setLore(meta, lore);
    }

    @Deprecated
    public static void hideAttributes(@NotNull ItemStack itemStack) {
        if (Version.isAtLeast(Version.MC_1_21_5)) {
            Software.get().hideComponents(itemStack);
            return;
        }

        editMeta(itemStack, meta -> hideAttributes(meta, itemStack.getType()));
    }

    @Deprecated
    private static void hideAttributes(@NotNull ItemMeta meta, @NotNull Material material) {
        if (Version.isBehind(Version.MC_1_21_5)) {
            if (material.isItem()) {
                EquipmentSlot slot = material.getEquipmentSlot();
                material.getDefaultAttributeModifiers(slot).forEach((attribute, modifier) -> {
                    var modifiers = meta.getAttributeModifiers() == null ? null : meta.getAttributeModifiers(attribute);
                    if (modifiers == null || modifiers.isEmpty() || !meta.getAttributeModifiers().containsKey(attribute)) {
                        meta.addAttributeModifier(attribute, modifier);
                    }
                });
            }
        }

        meta.addItemFlags(ItemFlag.values());
    }

    public static void setCustomModelData(@NotNull ItemStack itemStack, float data) {
        ItemUtil.editMeta(itemStack, meta -> setCustomModelData(meta, data));
    }

    public static void setCustomModelData(@NotNull ItemMeta meta, float data) {
        if (Version.isAtLeast(Version.MC_1_21_5)) {
            CustomModelDataComponent component = meta.getCustomModelDataComponent();
            component.setFloats(Lists.newList(data));
            meta.setCustomModelDataComponent(component);
        }
        else {
            meta.setCustomModelData((int) data);
        }
    }

    @Nullable
    public static Float getCustomModelData(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return meta == null ? null : getCustomModelData(meta);
    }

    @Nullable
    public static Float getCustomModelData(@NotNull ItemMeta meta) {
        if (Version.isAtLeast(Version.MC_1_21_5)) {
            List<Float> floats = meta.getCustomModelDataComponent().getFloats();
            return floats.isEmpty() ? null : floats.getFirst();
        }
        return meta.hasCustomModelData() ? (float) meta.getCustomModelData() : null;
    }

    @NotNull
    @Deprecated
    public static ItemStack createCustomHead(@NotNull String texture) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        setSkullTexture(item, texture);
        return item;
    }

    @NotNull
    @Deprecated
    public static ItemStack getSkinHead(@NotNull String texture) {
        return getCustomHead(texture);
    }

    @NotNull
    public static ItemStack getCustomHead(@NotNull String texture) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        setProfileBySkinURL(item, texture);
        return item;
    }

    @Nullable
    @Deprecated
    public static PlayerProfile createSkinProfile(@NotNull String urlData) {
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
    public static void setHeadSkin(@NotNull ItemStack item, @NotNull String urlData) {
        setProfileBySkinURL(item, urlData);
    }

    @Nullable
    @Deprecated
    public static String getHeadSkin(@NotNull ItemStack item) {
        return getProfileSkinURL(item);
    }

    @Nullable
    public static NightProfile getOwnerProfile(@NotNull ItemStack itemStack) {
        return Software.get().getOwnerProfile(itemStack);
    }

    @Nullable
    public static String getProfileSkinURL(@NotNull ItemStack itemStack) {
        NightProfile profile = getOwnerProfile(itemStack);
        if (profile == null) return null;

        return PlayerProfiles.getProfileSkinURL(profile);
    }

    public static void setProfileBySkinURL(@NotNull ItemStack itemStack, @NotNull String urlData) {
        CachedProfile profile = PlayerProfiles.createProfileBySkinURL(urlData);
        if (profile == null) return;

        editMeta(itemStack, SkullMeta.class, skullMeta -> profile.query().apply(skullMeta));
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

            setProfileBySkinURL(item, url);
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

    @Deprecated
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

    @Deprecated
    public static boolean isElytra(@NotNull ItemStack item) {
        return item.getType() == Material.ELYTRA;
    }

    @Deprecated
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
