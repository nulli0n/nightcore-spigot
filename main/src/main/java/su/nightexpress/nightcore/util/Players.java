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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    @NotNull
    public static Set<Player> getOnline() {
        return new HashSet<>(Bukkit.getServer().getOnlinePlayers());
    }

    @NotNull
    public static List<String> playerNames() {
        return playerNames(null);
    }

    @NotNull
    public static List<String> playerNames(@Nullable Player viewer) {
        return getOnline().stream().filter(player -> viewer == null || viewer.canSee(player)).map(Player::getName).sorted(String::compareTo).toList();
    }

    @NotNull
    @Deprecated
    public static List<String> realPlayerNames() {
        return realPlayerNames(null);
    }

    @NotNull
    @Deprecated
    public static List<String> realPlayerNames(@Nullable Player viewer) {
        return playerNames(viewer, false);
    }

    @NotNull
    @Deprecated
    public static List<String> playerNames(@Nullable Player viewer, boolean includeCustom) {
        return playerNames(viewer);
    }

    @NotNull
    public static Optional<Player> find(@NotNull String nameOrNick) {
        return Optional.ofNullable(getPlayer(nameOrNick));
    }

    @Nullable
    public static Player getPlayer(@NotNull String name) {
        return Bukkit.getServer().getPlayer(name);
    }

    @Nullable
    public static Player getPlayer(@NotNull UUID uuid) {
        return Bukkit.getServer().getPlayer(uuid);
    }

    public static boolean isBedrock(@NotNull Player player) {
        return Plugins.hasFloodgate() && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
    }

    @Deprecated
    public static boolean isReal(@NotNull Player player) {
        return player.isOnline();
    }

    public static void closeDialog(@NotNull Player player) {
        Software.get().closeDialog(player);
    }

    @NotNull
    public static String getDisplayNameSerialized(@NotNull Player player) {
        return Software.get().getDisplayNameSerialized(player);
    }

    public static void setDisplayName(@NotNull Player player, @NotNull String name) {
        setDisplayName(player, NightMessage.parse(name));
    }

    public static void setDisplayName(@NotNull Player player, @NotNull NightComponent name) {
        Software.get().setDisplayName(player, name);
    }

    @Nullable
    public static String getPlayerListHeaderSerialized(@NotNull Player player) {
        return Software.get().getPlayerListHeaderSerialized(player);
    }

    @Nullable
    public static String getPlayerListFooterSerialized(@NotNull Player player) {
        return Software.get().getPlayerListFooterSerialized(player);
    }

    public static void setPlayerListHeaderFooter(@NotNull Player player, @Nullable String header, @Nullable String footer) {
        setPlayerListHeaderFooter(player, header == null ? null : NightMessage.parse(header), footer == null ? null : NightMessage.parse(footer));
    }

    public static void setPlayerListHeaderFooter(@NotNull Player player, @Nullable NightComponent header, @Nullable NightComponent footer) {
        Software.get().setPlayerListHeaderFooter(player, header, footer);
    }

    @NotNull
    public static String getPlayerListNameSerialized(@NotNull Player player) {
        return Software.get().getPlayerListNameSerialized(player);
    }

    public static void setPlayerListName(@NotNull Player player, @NotNull String name) {
        setPlayerListName(player, NightMessage.parse(name));
    }

    public static void setPlayerListName(@NotNull Player player, @NotNull NightComponent name) {
        Software.get().setPlayerListName(player, name);
    }

    public static void kick(@NotNull Player player, @NotNull String reason) {
        kick(player, NightMessage.parse(reason));
    }

    public static void kick(@NotNull Player player, @NotNull NightComponent reason) {
        Software.get().kick(player, reason);
    }

    public static void disallowLogin(@NotNull AsyncPlayerPreLoginEvent event, @NotNull AsyncPlayerPreLoginEvent.Result result, @NotNull String message) {
        disallowLogin(event, result, NightMessage.parse(message));
    }

    public static void disallowLogin(@NotNull AsyncPlayerPreLoginEvent event, @NotNull AsyncPlayerPreLoginEvent.Result result, @NotNull NightComponent message) {
        Software.get().disallowLogin(event, result, message);
    }

    @NotNull
    @Deprecated
    public static NightProfile getProfile(@NotNull OfflinePlayer player) {
        return PlayerProfiles.getProfile(player).query();
    }

    @NotNull
    @Deprecated
    public static NightProfile createProfile(@NotNull UUID uuid) {
        return PlayerProfiles.createProfile(uuid).query();
    }

    @NotNull
    @Deprecated
    public static NightProfile createProfile(@NotNull String name) {
        return PlayerProfiles.createProfile(name);
    }

    @NotNull
    @Deprecated
    public static NightProfile createProfile(@NotNull UUID uuid, @Nullable String name) {
        return PlayerProfiles.createProfile(uuid, name).query();
    }

    @Nullable
    @Deprecated
    public static NightProfile createProfileBySkinURL(@NotNull String urlData) {
        CachedProfile profile = PlayerProfiles.createProfileBySkinURL(urlData);
        return profile == null ? null : profile.query();
    }

    @Nullable
    @Deprecated
    public static String getProfileSkinURL(@NotNull NightProfile profile) {
        return PlayerProfiles.getProfileSkinURL(profile);
    }

    @NotNull
    @Deprecated
    public static String getPermissionGroup(@NotNull Player player) {
        return getPrimaryGroupOrDefault(player);
    }

    @Nullable
    public static String getPrimaryGroup(@NotNull Player player) {
        return PermissionBridge.provider().map(provider -> provider.getPrimaryGroup(player)).orElse(null);
    }

    @NotNull
    public static String getPrimaryGroup(@NotNull Player player, @NotNull String fallback) {
        String group = getPrimaryGroup(player);
        return group == null ? fallback : group;
    }

    @NotNull
    public static CompletableFuture<String> getPrimaryGroup(@NotNull UUID playerId) {
        return PermissionBridge.provider().map(provider -> provider.getPrimaryGroup(playerId)).orElse(CompletableFuture.completedFuture(null));
    }

    @NotNull
    public static String getPrimaryGroupOrDefault(@NotNull Player player) {
        return getPrimaryGroup(player, Placeholders.DEFAULT);
    }

    @NotNull
    @Deprecated
    public static Set<String> getPermissionGroups(@NotNull Player player) {
        return getInheritanceGroupsOrDefault(player);
    }

    @NotNull
    public static Set<String> getInheritanceGroups(@NotNull Player player) {
        return PermissionBridge.provider().map(provider -> provider.getPermissionGroups(player)).orElse(Collections.emptySet());
    }

    @NotNull
    public static Set<String> getInheritanceGroups(@NotNull Player player, @NotNull Set<String> fallback) {
        Set<String> groups = getInheritanceGroups(player);
        return groups.isEmpty() ? fallback : groups;
    }

    @NotNull
    public static CompletableFuture<Set<String>> getInheritanceGroups(@NotNull UUID playerId) {
        return PermissionBridge.provider().map(provider -> provider.getPermissionGroups(playerId)).orElse(CompletableFuture.completedFuture(Collections.emptySet()));
    }

    @NotNull
    public static Set<String> getInheritanceGroupsOrDefault(@NotNull Player player) {
        return getInheritanceGroups(player, Lists.newSet(Placeholders.DEFAULT));
    }

    @NotNull
    @Deprecated
    public static String getPrefix(@NotNull Player player) {
        return getPrefixOrEmpty(player);
    }

    @NotNull
    public static String getPrefixOrEmpty(@NotNull Player player) {
        return getPrefix(player, "");

    }

    @Nullable
    public static String getRawPrefix(@NotNull Player player) {
        return PermissionBridge.provider().map(provider -> provider.getPrefix(player)).orElse(null);
    }

    @NotNull
    public static CompletableFuture<String> getRawPrefix(@NotNull UUID playerId) {
        return PermissionBridge.provider().map(provider -> provider.getPrefix(playerId)).orElse(CompletableFuture.completedFuture(null));
    }

    @NotNull
    public static String getPrefix(@NotNull Player player, @NotNull String fallback) {
        String prefix = getRawPrefix(player);
        return prefix == null ? fallback : prefix;
    }

    @NotNull
    @Deprecated
    public static String getSuffix(@NotNull Player player) {
        return getSuffixOrEmpty(player);
    }

    @NotNull
    public static String getSuffixOrEmpty(@NotNull Player player) {
        return getSuffix(player, "");
    }

    @Nullable
    public static String getRawSuffix(@NotNull Player player) {
        return PermissionBridge.provider().map(provider -> provider.getSuffix(player)).orElse(null);
    }

    @NotNull
    public static CompletableFuture<String> getRawSuffix(@NotNull UUID playerId) {
        return PermissionBridge.provider().map(provider -> provider.getSuffix(playerId)).orElse(CompletableFuture.completedFuture(null));
    }

    @NotNull
    public static String getSuffix(@NotNull Player player, @NotNull String fallback) {
        String suffix = getRawSuffix(player);
        return suffix == null ? fallback : suffix;
    }

    @Deprecated
    public static void sendModernMessage(@NotNull CommandSender sender, @NotNull String message) {
        sendMessage(sender, message);
    }

    public static void sendMessage(@NotNull CommandSender sender, @NotNull String message) {
        sendMessage(sender, NightMessage.parse(message));
    }

    public static void sendMessage(@NotNull CommandSender sender, @NotNull NightComponent component) {
        Software.get().getTextComponentAdapter().send(sender, component);
    }

    @Deprecated
    public static void sendActionBarText(@NotNull Player player, @NotNull String message) {
        sendActionBar(player, message);
    }

    @Deprecated
    public static void sendActionBar(@NotNull Player player, @NotNull TextRoot message) {
        sendActionBar(player, message.getString());
    }

    public static void sendActionBar(@NotNull Player player, @NotNull String message) {
        sendActionBar(player, NightMessage.parse(message));
    }

    public static void sendActionBar(@NotNull Player player, @NotNull NightComponent component) {
        Software.get().getTextComponentAdapter().sendActionBar(player, component);
    }

    @Deprecated
    public static void sendTitle(@NotNull Player player, @NotNull String title, @NotNull String subtitle, int fadeIn, int stay, int fadeOut) {
        sendTitles(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public static void sendTitles(@NotNull Player player, @NotNull String title, @NotNull String subtitle, int fadeIn, int stay, int fadeOut) {
        sendTitles(player, NightMessage.parse(title), NightMessage.parse(subtitle), fadeIn, stay, fadeOut);
    }

    public static void sendTitles(@NotNull Player player, @NotNull NightComponent title, @NotNull NightComponent subtitle, int fadeIn, int stay, int fadeOut) {
        Software.get().sendTitles(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public static void dispatchCommands(@NotNull Player player, @NotNull String... commands) {
        for (String command : commands) {
            dispatchCommand0(player, command);
        }
    }

    public static void dispatchCommands(@NotNull Player player, @NotNull List<String> commands) {
        for (String command : commands) {
            dispatchCommand0(player, command);
        }
    }

    public static void dispatchCommand(@NotNull Player player, @NotNull String command) {
        dispatchCommand0(player, command);
    }

    private static void dispatchCommand0(@NotNull Player player, @NotNull String command) {
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

    public static boolean hasEmptyInventory(@NotNull Player player) {
        return Stream.of(player.getInventory().getContents()).allMatch(item -> item == null || item.getType().isAir());
    }

    public static boolean hasEmptyContents(@NotNull Player player) {
        return Stream.of(player.getInventory().getContents()).allMatch(item -> item == null || item.getType().isAir());
    }

    public static int countItemSpace(@NotNull Player player, @NotNull ItemStack item) {
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

    public static int countItem(@NotNull Player player, @NotNull Predicate<ItemStack> predicate) {
        return Stream.of(player.getInventory().getContents())
              .filter(item -> item != null && predicate.test(item))
              .mapToInt(ItemStack::getAmount).sum();
    }

    public static int countItem(@NotNull Player player, @NotNull ItemStack item) {
        return countItem(player, item::isSimilar);
    }

    public static int countItem(@NotNull Player player, @NotNull Material material) {
        return countItem(player, itemHas -> itemHas.getType() == material);
    }

    public static void takeItem(@NotNull Player player, @NotNull ItemStack item) {
        takeItem(player, item, -1);
    }

    public static void takeItem(@NotNull Player player, @NotNull ItemStack item, int amount) {
        takeItem(player, itemHas -> itemHas.isSimilar(item), amount);
    }

    public static void takeItem(@NotNull Player player, @NotNull Material material) {
        takeItem(player, material, -1);
    }

    public static void takeItem(@NotNull Player player, @NotNull Material material, int amount) {
        takeItem(player, itemHas -> itemHas.getType() == material, amount);
    }

    public static void takeItem(@NotNull Player player, @NotNull Predicate<ItemStack> predicate) {
        takeItem(player, predicate, -1);
    }

    public static void takeItem(@NotNull Player player, @NotNull Predicate<ItemStack> predicate, int amount) {
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

    public static void addItem(@NotNull Player player, @NotNull ItemStack... items) {
        for (ItemStack item : items) {
            addItem(player, item, item.getAmount());
        }
    }

    public static void addItem(@NotNull Player player, @NotNull ItemStack itemStack, int amount) {
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
