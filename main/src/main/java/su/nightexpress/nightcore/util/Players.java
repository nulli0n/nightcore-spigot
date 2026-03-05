package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.geysermc.floodgate.api.FloodgateApi;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.integration.permission.PermissionBridge;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.profile.CachedProfile;
import su.nightexpress.nightcore.util.profile.PlayerProfiles;
import su.nightexpress.nightcore.util.text.TextRoot;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Players {

    @Deprecated
    public static final String TEXTURES_HOST         = PlayerProfiles.TEXTURES_HOST;
    public static final String PLAYER_COMMAND_PREFIX = "player:";

    @NonNull
    public static Set<Player> getOnline() {
        return new HashSet<>(Bukkit.getServer().getOnlinePlayers());
    }

    @NonNull
    public static List<String> playerNames() {
        return playerNames(null);
    }

    @NonNull
    public static List<String> playerNames(@Nullable Player viewer) {
        return getOnline().stream().filter(player -> viewer == null || viewer.canSee(player)).map(Player::getName).sorted(String::compareTo).toList();
    }

    @NonNull
    @Deprecated
    public static List<String> realPlayerNames() {
        return realPlayerNames(null);
    }

    @NonNull
    @Deprecated
    public static List<String> realPlayerNames(@Nullable Player viewer) {
        return playerNames(viewer, false);
    }

    @NonNull
    @Deprecated
    public static List<String> playerNames(@Nullable Player viewer, boolean includeCustom) {
        return playerNames(viewer);
    }

    public static boolean isOnline(@NonNull UUID playerId) {
        return getPlayer(playerId) != null;
    }

    @NonNull
    @Deprecated
    public static Optional<Player> find(@NonNull String nameOrNick) {
        return Optional.ofNullable(getPlayer(nameOrNick));
    }

    @NonNull
    public static Optional<Player> findById(@NonNull UUID playerId) {
        return Optional.ofNullable(getPlayer(playerId));
    }

    @NonNull
    public static Optional<Player> findByName(@NonNull String playerName) {
        return Optional.ofNullable(getPlayer(playerName));
    }

    @Nullable
    public static Player getPlayer(@NonNull String name) {
        return Bukkit.getServer().getPlayer(name);
    }

    @Nullable
    public static Player getPlayer(@NonNull UUID uuid) {
        return Bukkit.getServer().getPlayer(uuid);
    }

    public static boolean isBedrock(@NonNull Player player) {
        return Plugins.hasFloodgate() && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
    }

    @Deprecated
    public static boolean isReal(@NonNull Player player) {
        return player.isOnline();
    }

    public static void closeDialog(@NonNull Player player) {
        Software.get().closeDialog(player);
    }

    @NonNull
    public static String getDisplayNameSerialized(@NonNull Player player) {
        return Software.get().getDisplayNameSerialized(player);
    }

    public static void setDisplayName(@NonNull Player player, @NonNull String name) {
        setDisplayName(player, NightMessage.parse(name));
    }

    public static void setDisplayName(@NonNull Player player, @NonNull NightComponent name) {
        Software.get().setDisplayName(player, name);
    }

    @Nullable
    public static String getPlayerListHeaderSerialized(@NonNull Player player) {
        return Software.get().getPlayerListHeaderSerialized(player);
    }

    @Nullable
    public static String getPlayerListFooterSerialized(@NonNull Player player) {
        return Software.get().getPlayerListFooterSerialized(player);
    }

    public static void setPlayerListHeaderFooter(@NonNull Player player, @Nullable String header, @Nullable String footer) {
        setPlayerListHeaderFooter(player, header == null ? null : NightMessage.parse(header), footer == null ? null : NightMessage.parse(footer));
    }

    public static void setPlayerListHeaderFooter(@NonNull Player player, @Nullable NightComponent header, @Nullable NightComponent footer) {
        Software.get().setPlayerListHeaderFooter(player, header, footer);
    }

    @NonNull
    public static String getPlayerListNameSerialized(@NonNull Player player) {
        return Software.get().getPlayerListNameSerialized(player);
    }

    public static void setPlayerListName(@NonNull Player player, @NonNull String name) {
        setPlayerListName(player, NightMessage.parse(name));
    }

    public static void setPlayerListName(@NonNull Player player, @NonNull NightComponent name) {
        Software.get().setPlayerListName(player, name);
    }

    public static void kick(@NonNull Player player, @NonNull String reason) {
        kick(player, NightMessage.parse(reason));
    }

    public static void kick(@NonNull Player player, @NonNull NightComponent reason) {
        Software.get().kick(player, reason);
    }

    public static void disallowLogin(@NonNull AsyncPlayerPreLoginEvent event, AsyncPlayerPreLoginEvent.@NonNull Result result, @NonNull String message) {
        disallowLogin(event, result, NightMessage.parse(message));
    }

    public static void disallowLogin(@NonNull AsyncPlayerPreLoginEvent event, AsyncPlayerPreLoginEvent.@NonNull Result result, @NonNull NightComponent message) {
        Software.get().disallowLogin(event, result, message);
    }

    @NonNull
    @Deprecated
    public static NightProfile getProfile(@NonNull OfflinePlayer player) {
        return PlayerProfiles.getProfile(player).query();
    }

    @NonNull
    @Deprecated
    public static NightProfile createProfile(@NonNull UUID uuid) {
        return PlayerProfiles.createProfile(uuid).query();
    }

    @NonNull
    @Deprecated
    public static NightProfile createProfile(@NonNull String name) {
        return PlayerProfiles.createProfile(name);
    }

    @NonNull
    @Deprecated
    public static NightProfile createProfile(@NonNull UUID uuid, @Nullable String name) {
        return PlayerProfiles.createProfile(uuid, name).query();
    }

    @Nullable
    @Deprecated
    public static NightProfile createProfileBySkinURL(@NonNull String urlData) {
        CachedProfile profile = PlayerProfiles.createProfileBySkinURL(urlData);
        return profile == null ? null : profile.query();
    }

    @Nullable
    @Deprecated
    public static String getProfileSkinURL(@NonNull NightProfile profile) {
        return PlayerProfiles.getProfileSkinURL(profile);
    }

    @NonNull
    @Deprecated
    public static String getPermissionGroup(@NonNull Player player) {
        return getPrimaryGroupOrDefault(player);
    }

    @Nullable
    public static String getPrimaryGroup(@NonNull Player player) {
        return PermissionBridge.provider().map(provider -> provider.getPrimaryGroup(player)).orElse(null);
    }

    @NonNull
    public static String getPrimaryGroup(@NonNull Player player, @NonNull String fallback) {
        String group = getPrimaryGroup(player);
        return group == null ? fallback : group;
    }

    @NonNull
    public static CompletableFuture<String> getPrimaryGroup(@NonNull UUID playerId) {
        return PermissionBridge.provider().map(provider -> provider.getPrimaryGroup(playerId)).orElse(CompletableFuture.completedFuture(null));
    }

    @NonNull
    public static String getPrimaryGroupOrDefault(@NonNull Player player) {
        return getPrimaryGroup(player, Placeholders.DEFAULT);
    }

    @NonNull
    @Deprecated
    public static Set<String> getPermissionGroups(@NonNull Player player) {
        return getInheritanceGroupsOrDefault(player);
    }

    @NonNull
    public static Set<String> getInheritanceGroups(@NonNull Player player) {
        return PermissionBridge.provider().map(provider -> provider.getPermissionGroups(player)).orElse(Collections.emptySet());
    }

    @NonNull
    public static Set<String> getInheritanceGroups(@NonNull Player player, @NonNull Set<String> fallback) {
        Set<String> groups = getInheritanceGroups(player);
        return groups.isEmpty() ? fallback : groups;
    }

    @NonNull
    public static CompletableFuture<Set<String>> getInheritanceGroups(@NonNull UUID playerId) {
        return PermissionBridge.provider().map(provider -> provider.getPermissionGroups(playerId)).orElse(CompletableFuture.completedFuture(Collections.emptySet()));
    }

    @NonNull
    public static Set<String> getInheritanceGroupsOrDefault(@NonNull Player player) {
        return getInheritanceGroups(player, Lists.newSet(Placeholders.DEFAULT));
    }

    @NonNull
    @Deprecated
    public static String getPrefix(@NonNull Player player) {
        return getPrefixOrEmpty(player);
    }

    @NonNull
    public static String getPrefixOrEmpty(@NonNull Player player) {
        return getPrefix(player, "");

    }

    @Nullable
    public static String getRawPrefix(@NonNull Player player) {
        return PermissionBridge.provider().map(provider -> provider.getPrefix(player)).orElse(null);
    }

    @NonNull
    public static CompletableFuture<String> getRawPrefix(@NonNull UUID playerId) {
        return PermissionBridge.provider().map(provider -> provider.getPrefix(playerId)).orElse(CompletableFuture.completedFuture(null));
    }

    @NonNull
    public static String getPrefix(@NonNull Player player, @NonNull String fallback) {
        String prefix = getRawPrefix(player);
        return prefix == null ? fallback : prefix;
    }

    @NonNull
    @Deprecated
    public static String getSuffix(@NonNull Player player) {
        return getSuffixOrEmpty(player);
    }

    @NonNull
    public static String getSuffixOrEmpty(@NonNull Player player) {
        return getSuffix(player, "");
    }

    @Nullable
    public static String getRawSuffix(@NonNull Player player) {
        return PermissionBridge.provider().map(provider -> provider.getSuffix(player)).orElse(null);
    }

    @NonNull
    public static CompletableFuture<String> getRawSuffix(@NonNull UUID playerId) {
        return PermissionBridge.provider().map(provider -> provider.getSuffix(playerId)).orElse(CompletableFuture.completedFuture(null));
    }

    @NonNull
    public static String getSuffix(@NonNull Player player, @NonNull String fallback) {
        String suffix = getRawSuffix(player);
        return suffix == null ? fallback : suffix;
    }

    @Deprecated
    public static void sendModernMessage(@NonNull CommandSender sender, @NonNull String message) {
        sendMessage(sender, message);
    }

    public static void sendMessage(@NonNull CommandSender sender, @NonNull String message) {
        sendMessage(sender, NightMessage.parse(message));
    }

    public static void sendMessage(@NonNull CommandSender sender, @NonNull NightComponent component) {
        Software.get().getTextComponentAdapter().send(sender, component);
    }

    @Deprecated
    public static void sendActionBarText(@NonNull Player player, @NonNull String message) {
        sendActionBar(player, message);
    }

    @Deprecated
    public static void sendActionBar(@NonNull Player player, @NonNull TextRoot message) {
        sendActionBar(player, message.getString());
    }

    public static void sendActionBar(@NonNull Player player, @NonNull String message) {
        sendActionBar(player, NightMessage.parse(message));
    }

    public static void sendActionBar(@NonNull Player player, @NonNull NightComponent component) {
        Software.get().getTextComponentAdapter().sendActionBar(player, component);
    }

    @Deprecated
    public static void sendTitle(@NonNull Player player, @NonNull String title, @NonNull String subtitle, int fadeIn, int stay, int fadeOut) {
        sendTitles(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public static void sendTitles(@NonNull Player player, @NonNull String title, @NonNull String subtitle, int fadeIn, int stay, int fadeOut) {
        sendTitles(player, NightMessage.parse(title), NightMessage.parse(subtitle), fadeIn, stay, fadeOut);
    }

    public static void sendTitles(@NonNull Player player, @NonNull NightComponent title, @NonNull NightComponent subtitle, int fadeIn, int stay, int fadeOut) {
        Software.get().sendTitles(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public static void dispatchCommands(@NonNull Player player, @NonNull String... commands) {
        for (String command : commands) {
            dispatchCommand0(player, command);
        }
    }

    public static void dispatchCommands(@NonNull Player player, @NonNull List<String> commands) {
        for (String command : commands) {
            dispatchCommand0(player, command);
        }
    }

    public static void dispatchCommand(@NonNull Player player, @NonNull String command) {
        dispatchCommand0(player, command);
    }

    private static void dispatchCommand0(@NonNull Player player, @NonNull String command) {
        CommandSender sender = Bukkit.getConsoleSender();
        boolean usePlayer = false;

        if (command.startsWith(PLAYER_COMMAND_PREFIX)) {
            command = command.substring(PLAYER_COMMAND_PREFIX.length());
            sender = player;
            usePlayer = true;
        }

        command = Placeholders.forPlayerWithPAPI(player).apply(command).trim();

        if (Version.isFolia()) {
            if (usePlayer) {
                CommandSender playerSender = sender;
                String playerCommand = command;
                NightCore.get().runTask(player, () -> Bukkit.dispatchCommand(playerSender, playerCommand));
            }
            else {
                CommandSender consoleSender = sender;
                String consoleCommand = command;
                NightCore.get().runTask(() -> Bukkit.dispatchCommand(consoleSender, consoleCommand));
            }
        }
        else {
            Bukkit.dispatchCommand(sender, command);
        }
    }

    public static boolean hasEmptyInventory(@NonNull Player player) {
        return Stream.of(player.getInventory().getContents()).allMatch(item -> item == null || item.getType().isAir());
    }

    public static boolean hasEmptyContents(@NonNull Player player) {
        return Stream.of(player.getInventory().getContents()).allMatch(item -> item == null || item.getType().isAir());
    }

    public static int countItemSpace(@NonNull Player player, @NonNull ItemStack item) {
        int stackSize = item.getType().getMaxStackSize();
        return Stream.of(player.getInventory().getStorageContents()).mapToInt(itemHas -> {
            if (itemHas == null || itemHas.getType().isAir()) {
                return stackSize;
            }
            if (itemHas.isSimilar(item)) {
                return (stackSize - itemHas.getAmount());
            }
            return 0;
        }).sum();
    }

    public static int countItem(@NonNull Player player, @NonNull Predicate<ItemStack> predicate) {
        return Stream.of(player.getInventory().getContents())
              .filter(item -> item != null && predicate.test(item))
              .mapToInt(ItemStack::getAmount).sum();
    }

    public static int countItem(@NonNull Player player, @NonNull ItemStack item) {
        return countItem(player, item::isSimilar);
    }

    public static int countItem(@NonNull Player player, @NonNull Material material) {
        return countItem(player, itemHas -> itemHas.getType() == material);
    }

    public static void takeItem(@NonNull Player player, @NonNull ItemStack item) {
        takeItem(player, item, -1);
    }

    public static void takeItem(@NonNull Player player, @NonNull ItemStack item, int amount) {
        takeItem(player, itemHas -> itemHas.isSimilar(item), amount);
    }

    public static void takeItem(@NonNull Player player, @NonNull Material material) {
        takeItem(player, material, -1);
    }

    public static void takeItem(@NonNull Player player, @NonNull Material material, int amount) {
        takeItem(player, itemHas -> itemHas.getType() == material, amount);
    }

    public static void takeItem(@NonNull Player player, @NonNull Predicate<ItemStack> predicate) {
        takeItem(player, predicate, -1);
    }

    public static void takeItem(@NonNull Player player, @NonNull Predicate<ItemStack> predicate, int amount) {
        int takenAmount = 0;

        Inventory inventory = player.getInventory();
        for (ItemStack itemHas : inventory.getContents()) {
            if (itemHas == null || !predicate.test(itemHas)) continue;

            if (amount < 0) {
                itemHas.setAmount(0);
                continue;
            }

            int hasAmount = itemHas.getAmount();
            if (takenAmount + hasAmount > amount) {
                int diff = (takenAmount + hasAmount) - amount;
                itemHas.setAmount(diff);
                break;
            }

            itemHas.setAmount(0);
            if ((takenAmount += hasAmount) == amount) {
                break;
            }
        }
    }

    public static void addItem(@NonNull Player player, @NonNull ItemStack... items) {
        for (ItemStack item : items) {
            addItem(player, item, item.getAmount());
        }
    }

    public static void addItem(@NonNull Player player, @NonNull ItemStack itemStack, int amount) {
        if (amount <= 0 || itemStack.getType().isAir()) return;

        ItemStack split = new ItemStack(itemStack);

        int realAmount = Math.min(split.getMaxStackSize(), amount);
        split.setAmount(realAmount);

        ItemStack copy = split.clone();
        World world = player.getWorld();
        if (Version.isFolia()) {
            NightCore.get().runTask(player, () -> player.getInventory().addItem(copy).values().forEach(left -> world.dropItem(player.getLocation(), left)));
        }
        else {
            player.getInventory().addItem(copy).values().forEach(left -> world.dropItem(player.getLocation(), left));
        }

        amount -= realAmount;
        if (amount > 0) addItem(player, itemStack, amount);
    }
}
