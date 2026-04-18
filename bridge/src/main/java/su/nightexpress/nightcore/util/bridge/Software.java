package su.nightexpress.nightcore.util.bridge;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

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
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

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

public interface Software {

    Holder INSTANCE = new Holder();

    final class Holder {

        private Software software;

        public void load(@NonNull Software software) {
            if (this.software != null) throw new IllegalStateException("Software is already initialized!");

            this.software = software;
            this.software.initialize();
        }

        @NonNull
        public Software get() {
            if (this.software == null) throw new IllegalStateException("Software is not yet initialized!");

            return this.software;
        }
    }

    @NonNull
    @Deprecated
    static Software instance() {
        return get();
    }

    @NonNull
    static Software get() {
        return INSTANCE.get();
    }

    boolean initialize();

    @NonNull
    String getName();

    boolean isPaper();

    int nextEntityId();

    @NonNull
    EventAdapter eventAdapter();

    @NonNull
    Listener createChatListener(@NonNull UniversalChatListenerCallback callback);

    @NonNull
    Listener createDialogListener(@NonNull DialogClickHandler handler);

    @NonNull
    AdaptedScheduler getScheduler(@NonNull JavaPlugin plugin);

    void disallowLogin(@NonNull AsyncPlayerPreLoginEvent event, AsyncPlayerPreLoginEvent.@NonNull Result result, @NonNull NightComponent message);

    void closeDialog(@NonNull Player player);

    void showDialog(@NonNull Player player, @NonNull WrappedDialog dialog);

    @NonNull
    TextComponentAdapter<?> getTextComponentAdapter();

    @NonNull
    SimpleCommandMap getCommandMap();

    @NonNull
    Map<String, Command> getKnownCommands(@NonNull SimpleCommandMap commandMap);

    @NonNull
    InventoryView createView(@NonNull MenuType menuType, @NonNull NightComponent title, @NonNull Player player);


    @NonNull
    NightProfile createProfile(@NonNull UUID uuid);

    @NonNull
    NightProfile createProfile(@NonNull String name);

    @NonNull
    NightProfile createProfile(@Nullable UUID uuid, @Nullable String name);

    @NonNull
    NightProfile getProfile(@NonNull OfflinePlayer player);


    void sendTitles(@NonNull Player player, @NonNull NightComponent title, @NonNull NightComponent subtitle, int fadeIn, int stay, int fadeOut);


    @NonNull
    Set<String> getCommonComponentsToHide();

    @NonNull
    Set<String> getHiddenComponents(@NonNull ItemStack itemStack);


    @NonNull
    String getTranslationKey(@NonNull Material material);

    @NonNull
    String getTranslationKey(@NonNull Attribute attribute);

    @NonNull
    String getTranslationKey(@NonNull Enchantment enchantment);

    @NonNull
    String getTranslationKey(@NonNull EntityType entityType);

    @NonNull
    String getTranslationKey(@NonNull PotionEffectType effectType);


    @NonNull
    String getDisplayNameSerialized(@NonNull Player player);

    void setDisplayName(@NonNull Player player, @Nullable NightComponent component);

    @Nullable
    String getPlayerListHeaderSerialized(@NonNull Player player);

    @Nullable
    String getPlayerListFooterSerialized(@NonNull Player player);

    void setPlayerListHeaderFooter(@NonNull Player player, @Nullable NightComponent header, @Nullable NightComponent footer);

    @NonNull
    String getPlayerListNameSerialized(@NonNull Player player);

    void setPlayerListName(@NonNull Player player, @NonNull NightComponent name);

    void kick(@NonNull Player player, @Nullable NightComponent component);


    void setCustomName(@NonNull Nameable entity, @Nullable NightComponent component);

    @Nullable
    String getCustomName(@NonNull Nameable entity);


    @NonNull
    ItemStack setType(@NonNull ItemStack itemStack, @NonNull Material material);

    void editMeta(@NonNull ItemStack itemStack, @NonNull Consumer<ItemMeta> consumer);

    <T extends ItemMeta> void editMeta(@NonNull ItemStack itemStack, @NonNull Class<T> clazz, @NonNull Consumer<T> consumer);

    @Nullable
    String getCustomName(@NonNull ItemMeta meta);

    void setCustomName(@NonNull ItemMeta meta, @Nullable NightComponent name);

    @Nullable
    String getItemName(@NonNull ItemMeta meta);

    void setItemName(@NonNull ItemMeta meta, @NonNull NightComponent name);

    @Nullable
    List<String> getLore(@NonNull ItemMeta meta);

    void setLore(@NonNull ItemMeta meta, @Nullable List<NightComponent> lore);

    @Nullable
    NightProfile getOwnerProfile(@NonNull ItemStack itemStack);

    void hideComponents(@NonNull ItemStack itemStack);

    void hideComponents(@NonNull ItemStack itemStack, @NonNull Set<String> componentNames);


    @NonNull
    NightBossBar createBossBar(@NonNull NightComponent title, @NonNull NightBarColor barColor, @NonNull NightBarOverlay barOverlay, @NonNull NightBarFlag... barFlags);
}
