package su.nightexpress.nightcore.util.bridge;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.util.bridge.wrapper.ComponentBuildable;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public interface Software {

    boolean initialize();

    @NotNull String getName();

    boolean isPaper();

    int nextEntityId();

    @NotNull SimpleCommandMap getCommandMap();

    @NotNull Map<String, Command> getKnownCommands(@NotNull SimpleCommandMap commandMap);

    @NotNull NightComponent textComponent(@NotNull String text);

    @NotNull NightComponent translateComponent(@NotNull String key);

    @NotNull NightComponent translateComponent(@NotNull String key, @Nullable String fallback);

    @NotNull NightComponent buildComponent(@NotNull List<ComponentBuildable> childrens);

    @NotNull InventoryView createView(@NotNull MenuType menuType, @NotNull NightComponent title, @NotNull Player player);


    @NotNull NightProfile createProfile(@NotNull UUID uuid);

    @NotNull NightProfile createProfile(@NotNull String name);

    @NotNull NightProfile createProfile(@Nullable UUID uuid, @Nullable String name);

    @NotNull NightProfile getProfile(@NotNull OfflinePlayer player);


    void sendTitles(@NotNull Player player, @NotNull NightComponent title, @NotNull NightComponent subtitle, int fadeIn, int stay, int fadeOut);


    @NotNull Set<String> getCommonComponentsToHide();

    @NotNull Set<String> getHiddenComponents(@NotNull ItemStack itemStack);


    @NotNull String getTranslationKey(@NotNull Material material);

    @NotNull String getTranslationKey(@NotNull Attribute attribute);

    @NotNull String getTranslationKey(@NotNull Enchantment enchantment);

    @NotNull String getTranslationKey(@NotNull EntityType entityType);

    @NotNull String getTranslationKey(@NotNull PotionEffectType effectType);


    @NotNull ItemStack setType(@NotNull ItemStack itemStack, @NotNull Material material);

    void editMeta(@NotNull ItemStack itemStack, @NotNull Consumer<ItemMeta> consumer);

    <T extends ItemMeta> void editMeta(@NotNull ItemStack itemStack, @NotNull Class<T> clazz, @NotNull Consumer<T> consumer);

    @Nullable String getCustomName(@NotNull ItemMeta meta);

    void setCustomName(@NotNull ItemMeta meta, @NotNull NightComponent name);

    @Nullable String getItemName(@NotNull ItemMeta meta);

    void setItemName(@NotNull ItemMeta meta, @NotNull NightComponent name);

    @Nullable List<String> getLore(@NotNull ItemMeta meta);

    void setLore(@NotNull ItemMeta meta, @NotNull List<NightComponent> lore);

    @Nullable NightProfile getOwnerProfile(@NotNull ItemStack itemStack);

    //void setOwnerProfile(@NotNull SkullMeta meta, @NotNull NightProfile profile);

    void hideComponents(@NotNull ItemStack itemStack);

    void hideComponents(@NotNull ItemStack itemStack, @NotNull Set<String> componentNames);
}
