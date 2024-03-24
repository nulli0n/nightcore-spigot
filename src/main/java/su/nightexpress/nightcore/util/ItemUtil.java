package su.nightexpress.nightcore.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.language.LangAssets;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class ItemUtil {

    //private static final String VERSION = Version.getProtocol();
    private static final String TEXTURES_HOST = "http://textures.minecraft.net/texture/";

    private static final Class<?> NBT_TAG_COMPOUND = Reflex.getClass("net.minecraft.nbt", "NBTTagCompound");
    private static final Class<?> NMS_ITEM         = Reflex.getClass("net.minecraft.world.item", "ItemStack");
    private static final Class<?> CRAFT_ITEM_STACK = Reflex.getClass(Version.CRAFTBUKKIT_PACKAGE + ".inventory", "CraftItemStack");
    private static final Class<?> NBT_IO           = Reflex.getClass("net.minecraft.nbt", "NBTCompressedStreamTools");

    private static final Constructor<?> NBT_TAG_COMPOUND_NEW = Reflex.getConstructor(NBT_TAG_COMPOUND);

    private static final Method AS_NMS_COPY    = Reflex.getMethod(CRAFT_ITEM_STACK, "asNMSCopy", ItemStack.class);
    private static final Method AS_BUKKIT_COPY = Reflex.getMethod(CRAFT_ITEM_STACK, "asBukkitCopy", NMS_ITEM);

    private static final Method NMS_ITEM_OF = Reflex.getMethod(NMS_ITEM, "a", NBT_TAG_COMPOUND);
    private static final Method NMS_SAVE    = Reflex.getMethod(NMS_ITEM, "b", NBT_TAG_COMPOUND);

    private static final Method NBT_IO_WRITE = Reflex.getMethod(NBT_IO, "a", NBT_TAG_COMPOUND, DataOutput.class);
    private static final Method NBT_IO_READ  = Reflex.getMethod(NBT_IO, "a", DataInput.class);

    @NotNull
    public static String getItemName(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return (meta == null || !meta.hasDisplayName()) ? LangAssets.get(item.getType()) : meta.getDisplayName();
    }

    public static void editMeta(@NotNull ItemStack item, @NotNull Consumer<ItemMeta> function) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        function.accept(meta);
        item.setItemMeta(meta);
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

    public static void setHeadSkin(@NotNull ItemStack item, @NotNull String urlData) {
        if (item.getType() != Material.PLAYER_HEAD) return;
        if (!(item.getItemMeta() instanceof SkullMeta meta)) return;

        if (!urlData.startsWith(TEXTURES_HOST)) {
            urlData = TEXTURES_HOST + urlData;
        }

        try {
            UUID uuid = UUID.nameUUIDFromBytes(urlData.getBytes());
            // If no name, then meta#getOwnerProfile will return 'null' (wtf?)
            // sometimes swtiching to "new" spigot api is a pain.
            // why the hell i have to dig into nms to learn that...
            PlayerProfile profile = Bukkit.createPlayerProfile(uuid, urlData.substring(0, 16));
            URL url = new URL(urlData);
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(url);
            profile.setTextures(textures);
            meta.setOwnerProfile(profile);
            item.setItemMeta(meta);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
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
        if (!(item.getItemMeta() instanceof SkullMeta meta)) return;

        UUID uuid = UUID.nameUUIDFromBytes(value.getBytes());
        GameProfile profile = new GameProfile(uuid, "null");
        profile.getProperties().put("textures", new Property("textures", value));

        Method method = Reflex.getMethod(meta.getClass(), "setProfile", GameProfile.class);
        if (method != null) {
            Reflex.invokeMethod(method, meta, profile);
        }
        else {
            Reflex.setFieldValue(meta, "profile", profile);
        }

        item.setItemMeta(meta);
    }

    @Nullable
    @Deprecated
    public static String getSkullTexture(@NotNull ItemStack item) {
        if (item.getType() != Material.PLAYER_HEAD) return null;

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta == null) return null;

        GameProfile profile = (GameProfile) Reflex.getFieldValue(meta, "profile");
        if (profile == null) return null;

        Collection<Property> properties = profile.getProperties().get("textures");
        Optional<Property> opt = properties.stream().filter(prop -> {
            String name;
            if (Version.isAtLeast(Version.V1_20_R2)) {
                name = prop.name();
            }
            else {
                name = (String) Reflex.getFieldValue(profile, "name");
            }
            return name != null && name.equalsIgnoreCase("textures");
        }).findFirst();

        if (opt.isEmpty()) return null;

        if (Version.isAtLeast(Version.V1_20_R2)) {
            return opt.get().value();
        }
        else {
            return (String) Reflex.getFieldValue(opt.get(), "value");
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
        if (Version.isAtLeast(Version.V1_19_R3)) {
            return Tag.ITEMS_SWORDS.isTagged(item.getType());
        }

        Material material = item.getType();
        return material == Material.DIAMOND_SWORD || material == Material.GOLDEN_SWORD
            || material == Material.IRON_SWORD || material == Material.NETHERITE_SWORD
            || material == Material.STONE_SWORD || material == Material.WOODEN_SWORD;
    }

    public static boolean isAxe(@NotNull ItemStack item) {
        if (Version.isAtLeast(Version.V1_19_R3)) {
            return Tag.ITEMS_AXES.isTagged(item.getType());
        }

        Material material = item.getType();
        return material == Material.DIAMOND_AXE || material == Material.GOLDEN_AXE
            || material == Material.IRON_AXE || material == Material.NETHERITE_AXE
            || material == Material.STONE_AXE || material == Material.WOODEN_AXE;
    }

    public static boolean isTrident(@NotNull ItemStack item) {
        return item.getType() == Material.TRIDENT;
    }

    public static boolean isPickaxe(@NotNull ItemStack item) {
        if (Version.isAtLeast(Version.V1_19_R3)) {
            return Tag.ITEMS_PICKAXES.isTagged(item.getType());
        }

        Material material = item.getType();
        return material == Material.DIAMOND_PICKAXE || material == Material.GOLDEN_PICKAXE
            || material == Material.IRON_PICKAXE || material == Material.NETHERITE_PICKAXE
            || material == Material.STONE_PICKAXE || material == Material.WOODEN_PICKAXE;
    }

    public static boolean isShovel(@NotNull ItemStack item) {
        if (Version.isAtLeast(Version.V1_19_R3)) {
            return Tag.ITEMS_SHOVELS.isTagged(item.getType());
        }

        Material material = item.getType();
        return material == Material.DIAMOND_SHOVEL || material == Material.GOLDEN_SHOVEL
            || material == Material.IRON_SHOVEL || material == Material.NETHERITE_SHOVEL
            || material == Material.STONE_SHOVEL || material == Material.WOODEN_SHOVEL;
    }

    public static boolean isHoe(@NotNull ItemStack item) {
        if (Version.isAtLeast(Version.V1_19_R3)) {
            return Tag.ITEMS_HOES.isTagged(item.getType());
        }

        Material material = item.getType();
        return material == Material.DIAMOND_HOE || material == Material.GOLDEN_HOE
            || material == Material.IRON_HOE || material == Material.NETHERITE_HOE
            || material == Material.STONE_HOE || material == Material.WOODEN_HOE;
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
    public static String compress(@NotNull ItemStack item) {
        if (NBT_TAG_COMPOUND_NEW == null || AS_NMS_COPY == null || NMS_SAVE == null || NBT_IO_WRITE == null) {
            //throw new UnsupportedOperationException("Unsupported server version!");
            return null;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);

        try {
            Object nbtTagCompoundItem = NBT_TAG_COMPOUND_NEW.newInstance();
            Object nmsItem = AS_NMS_COPY.invoke(null, item);
            NMS_SAVE.invoke(nmsItem, nbtTagCompoundItem);
            NBT_IO_WRITE.invoke(null, nbtTagCompoundItem, dataOutput);
            return new BigInteger(1, outputStream.toByteArray()).toString(32);
        }
        catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static ItemStack decompress(@NotNull String compressed) {
        if (NBT_IO_READ == null || NMS_ITEM_OF == null || AS_BUKKIT_COPY == null) {
            throw new UnsupportedOperationException("Unsupported server version!");
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(compressed, 32).toByteArray());

        Object nbtTagCompoundRoot;
        try {
            nbtTagCompoundRoot = NBT_IO_READ.invoke(null, new DataInputStream(inputStream));
            Object nmsItem = NMS_ITEM_OF.invoke(null, nbtTagCompoundRoot);
            return (ItemStack) AS_BUKKIT_COPY.invoke(null, nmsItem);
        }
        catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NotNull
    public static List<String> compress(@NotNull ItemStack[] items) {
        return compress(Arrays.asList(items));
    }

    @NotNull
    public static List<String> compress(@NotNull List<ItemStack> items) {
        return new ArrayList<>(items.stream().map(ItemUtil::compress).filter(Objects::nonNull).toList());
    }

    public static ItemStack[] decompress(@NotNull List<String> list) {
        List<ItemStack> items = list.stream().map(ItemUtil::decompress).filter(Objects::nonNull).toList();
        return items.toArray(new ItemStack[list.size()]);
    }
}
