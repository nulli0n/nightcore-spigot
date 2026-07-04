package su.nightexpress.nightcore.util.bridge;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarFlag;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.bridge.bossbar.NightBossBar;
import su.nightexpress.nightcore.bridge.chat.UniversalChatListenerCallback;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickHandler;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.event.EventAdapter;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.scheduler.AdaptedScheduler;
import su.nightexpress.nightcore.bridge.text.adapter.TextComponentAdapter;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

@NullMarked
public interface Software {

    Holder INSTANCE = new Holder();

    final class Holder {

        @Nullable
        private Software software;

        public void load(Software software) {
            if (this.software != null) throw new IllegalStateException("Software is already initialized!");

            this.software = software;
            this.software.initialize();
        }


        public Software get() {
            if (this.software == null) throw new IllegalStateException("Software is not yet initialized!");

            return this.software;
        }
    }


    @Deprecated
    static Software instance() {
        return get();
    }


    static Software get() {
        return INSTANCE.get();
    }

    boolean initialize();


    String getName();

    boolean isPaper();

    int nextEntityId();


    EventAdapter eventAdapter();


    Listener createChatListener(UniversalChatListenerCallback callback);


    Listener createDialogListener(DialogClickHandler handler);


    AdaptedScheduler getScheduler(JavaPlugin plugin);

    void disallowLogin(AsyncPlayerPreLoginEvent event, AsyncPlayerPreLoginEvent.Result result,
                       NightComponent message);

    void closeDialog(Player player);

    void showDialog(Player player, WrappedDialog dialog);


    TextComponentAdapter<?> getTextComponentAdapter();


    SimpleCommandMap getCommandMap();


    Map<String, Command> getKnownCommands(SimpleCommandMap commandMap);


    InventoryView createView(MenuType menuType, NightComponent title, Player player);

    boolean isValidKeyNamespace(String namespace);

    boolean isValidKeyValue(String value);

    boolean allowedInNamespace(char character);

    boolean allowedInValue(char character);

    AdaptedKey createKey(Plugin plugin, String value);

    AdaptedKey createKey(String namespace, String value);

    AdaptedKey createKey(String string);

    AdaptedKey getKey(World world);

    NightProfile createProfile(UUID uuid);


    NightProfile createProfile(String name);


    NightProfile createProfile(@Nullable UUID uuid, @Nullable String name);


    NightProfile getProfile(OfflinePlayer player);


    void sendTitles(Player player, NightComponent title, NightComponent subtitle, int fadeIn,
                    int stay, int fadeOut);


    Set<String> getCommonComponentsToHide();


    Set<String> getHiddenComponents(ItemStack itemStack);


    String getTranslationKey(Material material);


    String getTranslationKey(Attribute attribute);


    String getTranslationKey(Enchantment enchantment);


    String getTranslationKey(EntityType entityType);


    String getTranslationKey(PotionEffectType effectType);


    String getDisplayNameSerialized(Player player);

    void setDisplayName(Player player, @Nullable NightComponent component);

    @Nullable
    String getPlayerListHeaderSerialized(Player player);

    @Nullable
    String getPlayerListFooterSerialized(Player player);

    void setPlayerListHeaderFooter(Player player, @Nullable NightComponent header,
                                   @Nullable NightComponent footer);


    String getPlayerListNameSerialized(Player player);

    void setPlayerListName(Player player, NightComponent name);

    void kick(Player player, @Nullable NightComponent component);


    void setCustomName(Nameable entity, @Nullable NightComponent component);

    @Nullable
    String getCustomName(Nameable entity);


    ItemStack setType(ItemStack itemStack, Material material);

    void editMeta(ItemStack itemStack, Consumer<ItemMeta> consumer);

    <T extends ItemMeta> void editMeta(ItemStack itemStack, Class<T> clazz,
                                       Consumer<T> consumer);

    @Nullable
    String getCustomName(ItemMeta meta);

    void setCustomName(ItemMeta meta, @Nullable NightComponent name);

    @Nullable
    String getItemName(ItemMeta meta);

    void setItemName(ItemMeta meta, NightComponent name);

    @Nullable
    List<String> getLore(ItemMeta meta);

    void setLore(ItemMeta meta, @Nullable List<NightComponent> lore);

    @Nullable
    NightProfile getOwnerProfile(ItemStack itemStack);

    void hideComponents(ItemStack itemStack);

    void hideComponents(ItemStack itemStack, Set<String> componentNames);


    NightBossBar createBossBar(NightComponent title, NightBarColor barColor,
                               NightBarOverlay barOverlay, NightBarFlag... barFlags);
}
