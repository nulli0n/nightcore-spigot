package su.nightexpress.nightcore.util.bridge;

import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarFlag;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.bridge.bossbar.NightBossBar;
import su.nightexpress.nightcore.bridge.chat.UniversalChatListenerCallback;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickHandler;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.event.EventAdapter;
import su.nightexpress.nightcore.bridge.scheduler.AdaptedScheduler;
import su.nightexpress.nightcore.bridge.text.adapter.TextComponentAdapter;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public interface Software {

    Holder INSTANCE = new Holder();

    final class Holder {

        private Software software;

        public void load(@NotNull Software software) {
            if (this.software != null) throw new IllegalStateException("Software is already initialized!");

            this.software = software;
            this.software.initialize();
        }

        @NotNull
        public Software get() {
            if (this.software == null) throw new IllegalStateException("Software is not yet initialized!");

            return this.software;
        }
    }

    @NotNull
    @Deprecated
    static Software instance() {
        return get();
    }

    @NotNull
    static Software get() {
        return INSTANCE.get();
    }

    boolean initialize();

    @NotNull String getName();

    boolean isPaper();

    int nextEntityId();

    @NotNull EventAdapter eventAdapter();

    @NotNull Listener createChatListener(@NotNull UniversalChatListenerCallback callback);

    @NotNull Listener createDialogListener(@NotNull DialogClickHandler handler);

    @NotNull AdaptedScheduler getScheduler(@NotNull JavaPlugin plugin);

    void disallowLogin(@NotNull AsyncPlayerPreLoginEvent event, @NotNull AsyncPlayerPreLoginEvent.Result result, @NotNull NightComponent message);

    void closeDialog(@NotNull Player player);

    void showDialog(@NotNull Player player, @NotNull WrappedDialog dialog);

    @NotNull TextComponentAdapter<?> getTextComponentAdapter();

    @NotNull SimpleCommandMap getCommandMap();

    @NotNull Map<String, Command> getKnownCommands(@NotNull SimpleCommandMap commandMap);

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


    @NotNull String getDisplayNameSerialized(@NotNull Player player);

    void setDisplayName(@NotNull Player player, @NotNull NightComponent component);

    @Nullable String getPlayerListHeaderSerialized(@NotNull Player player);

    @Nullable String getPlayerListFooterSerialized(@NotNull Player player);

    void setPlayerListHeaderFooter(@NotNull Player player, @Nullable NightComponent header, @Nullable NightComponent footer);

    @NotNull String getPlayerListNameSerialized(@NotNull Player player);

    void setPlayerListName(@NotNull Player player, @NotNull NightComponent name);

    void kick(@NotNull Player player, @Nullable NightComponent component);


    void setCustomName(@NotNull Nameable entity, @Nullable NightComponent component);

    @Nullable String getCustomName(@NotNull Nameable entity);


    @NotNull ItemStack setType(@NotNull ItemStack itemStack, @NotNull Material material);

    void editMeta(@NotNull ItemStack itemStack, @NotNull Consumer<ItemMeta> consumer);

    <T extends ItemMeta> void editMeta(@NotNull ItemStack itemStack, @NotNull Class<T> clazz, @NotNull Consumer<T> consumer);

    @Nullable String getCustomName(@NotNull ItemMeta meta);

    void setCustomName(@NotNull ItemMeta meta, @Nullable NightComponent name);

    @Nullable String getItemName(@NotNull ItemMeta meta);

    void setItemName(@NotNull ItemMeta meta, @NotNull NightComponent name);

    @Nullable List<String> getLore(@NotNull ItemMeta meta);

    void setLore(@NotNull ItemMeta meta, @Nullable List<NightComponent> lore);

    @Nullable NightProfile getOwnerProfile(@NotNull ItemStack itemStack);

    void hideComponents(@NotNull ItemStack itemStack);

    void hideComponents(@NotNull ItemStack itemStack, @NotNull Set<String> componentNames);






    @NotNull NightBossBar createBossBar(@NotNull NightComponent title, @NotNull NightBarColor barColor, @NotNull NightBarOverlay barOverlay, @NotNull NightBarFlag... barFlags);
}
