package su.nightexpress.nightcore.util;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.geysermc.floodgate.api.FloodgateApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.integration.VaultHook;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.text.TextRoot;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Players {

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
        //return playerNames(viewer, true);
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
//
//        Set<String> names = new HashSet<>();
//        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
//            if (viewer != null && !viewer.canSee(player)) continue;
//
//            names.add(player.getName());
//            if (includeCustom && CoreConfig.RESPECT_PLAYER_DISPLAYNAME.get()) {
//                names.add(Colorizer.strip(player.getDisplayName()));
//            }
//        }
//        return names.stream().sorted(String::compareTo).toList();
    }

    @NotNull
    public static Optional<Player> find(@NotNull String nameOrNick) {
        return Optional.ofNullable(getPlayer(nameOrNick));
    }

    @Nullable
    public static Player getPlayer(@NotNull String name) {
        return Bukkit.getServer().getPlayer(name);
//
//        if (!CoreConfig.RESPECT_PLAYER_DISPLAYNAME.get()) {
//            return Bukkit.getServer().getPlayer(nameOrNick);
//        }
//
//        Player found = Bukkit.getServer().getPlayerExact(nameOrNick);
//        if (found != null) {
//            return found;
//        }
//
//        String lowerName = nameOrNick.toLowerCase();
//        int lowerLength = lowerName.length();
//        int delta = Integer.MAX_VALUE;
//
//        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
//            String nameReal = player.getName().toLowerCase(Locale.ENGLISH);
//            String nameCustom = player.getDisplayName().toLowerCase();
//
//            int length;
//            if (nameReal.startsWith(lowerName)) {
//                length = player.getName().length();
//            }
//            else if (nameCustom.startsWith(lowerName)) {
//                length = player.getDisplayName().length();
//            }
//            else continue;
//
//            int curDelta = Math.abs(length - lowerLength);
//            if (curDelta < delta) {
//                found = player;
//                delta = curDelta;
//            }
//
//            if (curDelta == 0) break;
//        }
//
//        return found;
    }

    public static boolean isBedrock(@NotNull Player player) {
        return Plugins.hasFloodgate() && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
    }

    public static boolean isReal(@NotNull Player player) {
        return Bukkit.getServer().getPlayer(player.getUniqueId()) != null;
    }

    @NotNull
    public static String getPermissionGroup(@NotNull Player player) {
        return Plugins.hasVault() && VaultHook.hasPermissions() ? VaultHook.getPermissionGroup(player).toLowerCase() : Placeholders.DEFAULT;
    }

    @NotNull
    public static Set<String> getPermissionGroups(@NotNull Player player) {
        return Plugins.hasVault() && VaultHook.hasPermissions() ? VaultHook.getPermissionGroups(player) : Set.of(Placeholders.DEFAULT);
    }

    @NotNull
    public static String getPrefix(@NotNull Player player) {
        return Plugins.hasVault() ? VaultHook.getPrefix(player) : "";
    }

    @NotNull
    public static String getSuffix(@NotNull Player player) {
        return Plugins.hasVault() ? VaultHook.getSuffix(player) : "";
    }

    public static void sendModernMessage(@NotNull CommandSender sender, @NotNull String message) {
        NightMessage.create(message).send(sender);
    }

    public static void sendActionBarText(@NotNull Player player, @NotNull String message) {
        sendActionBar(player, NightMessage.create(message));
    }

    public static void sendActionBar(@NotNull Player player, @NotNull TextRoot message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, message.parseIfAbsent());
    }

    public static void dispatchCommands(@NotNull Player player, @NotNull String... commands) {
        Bukkit.getGlobalRegionScheduler().execute(NightCore.getPlugin(NightCore.class), () -> {
            for (String command : commands) {
                dispatchCommand(player, command);
            }
        });
    }

    public static void dispatchCommands(@NotNull Player player, @NotNull List<String> commands) {
        Bukkit.getGlobalRegionScheduler().execute(NightCore.getPlugin(NightCore.class), () -> {
            for (String command : commands) {
                dispatchCommand(player, command);
            }
        });
    }

    public static void dispatchCommand(@NotNull Player player, @NotNull String command) {
        Bukkit.getGlobalRegionScheduler().execute(NightCore.getPlugin(NightCore.class),
                () -> dispatchCommand0(player,command));
    }

    private static void dispatchCommand0(@NotNull Player player, @NotNull String command) {
        CommandSender sender = Bukkit.getConsoleSender();
        if (command.startsWith(PLAYER_COMMAND_PREFIX)) {
            command = command.substring(PLAYER_COMMAND_PREFIX.length());
            sender = player;
        }

        command = Placeholders.forPlayerWithPAPI(player).apply(command).trim();

//        if (Plugins.hasPlaceholderAPI()) {
//            command = PlaceholderAPI.setPlaceholders(player, command);
//        }
        Bukkit.dispatchCommand(sender, command);
    }

    public static boolean hasEmptyInventory(@NotNull Player player) {
        return Stream.of(player.getInventory().getContents()).allMatch(item -> item == null || item.getType().isAir());
    }

    public static boolean hasEmptyContents(@NotNull Player player) {
        return Stream.of(player.getInventory().getContents()).allMatch(item -> item == null || item.getType().isAir());
    }

    public static int countItemSpace(@NotNull Player player, @NotNull ItemStack item) {
        int stackSize = item.getType().getMaxStackSize();
        return Stream.of(player.getInventory().getContents()).mapToInt(itemHas -> {
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

        World world = player.getWorld();
        ItemStack split = new ItemStack(itemStack);

        int realAmount = Math.min(split.getMaxStackSize(), amount);
        split.setAmount(realAmount);
        player.getInventory().addItem(split).values().forEach(left -> {
            world.dropItem(player.getLocation(), left);
        });

        amount -= realAmount;
        if (amount > 0) addItem(player, itemStack, amount);
    }
}
