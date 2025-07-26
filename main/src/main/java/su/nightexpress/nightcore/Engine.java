package su.nightexpress.nightcore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.paper.PaperBridge;
import su.nightexpress.nightcore.bridge.spigot.SpigotBridge;
import su.nightexpress.nightcore.integration.VaultHook;
import su.nightexpress.nightcore.integration.permission.PermissionProvider;
import su.nightexpress.nightcore.integration.permission.impl.LuckPermissionProvider;
import su.nightexpress.nightcore.integration.permission.impl.VaultPermissionProvider;
import su.nightexpress.nightcore.util.ItemNbt;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bukkit.NightTask;

import java.util.HashSet;
import java.util.Set;

public class Engine {

    private static final Set<NightPlugin> CHILDRENS = new HashSet<>();

    private static NightCore          core;
    private static PermissionProvider permissions;

    @NotNull
    public static Set<NightPlugin> getChildrens() {
        return CHILDRENS;
    }

    @NotNull
    public static NightCore core() {
        if (core == null) throw new IllegalStateException("NightCore is not initialized!");

        return core;
    }

    @NotNull
    public static Software software() {
        return Software.INSTANCE.get();
    }

    @Nullable
    public static PermissionProvider getPermissions() {
        return permissions;
    }

    public static boolean hasPermissions() {
        return permissions != null;
    }

    public static void clear() {
        if (Plugins.hasVault()) {
            VaultHook.shutdown();
        }

        CHILDRENS.clear();
        permissions = null;
        core = null;
    }

    private static void init(@NotNull NightCore instance) {
        core = instance;

        Version version = Version.detect();
        if (version.isDropped()) return;

        Software.INSTANCE.load(Version.isPaper() ? new PaperBridge() : new SpigotBridge());
        core.info("Server version detected as " + version.getLocalized() + ". Using " + software().getName() + ".");

        ItemNbt.load(core);
        loadPermissionsProvider();

        Plugins.detectPlugins();

        if (Plugins.hasVault()) {
            VaultHook.load();
        }
    }

    private static void loadPermissionsProvider() {
        if (Plugins.isInstalled(Plugins.LUCK_PERMS)) {
            permissions = new LuckPermissionProvider();
        }
        else if (Plugins.isInstalled(Plugins.VAULT)) {
            permissions = new VaultPermissionProvider();
        }
        else return;

        core.info("Found permissions provider: " + permissions.getName());
        NightTask.create(core, permissions::setup, 0); // Run after the server load to setup Vault services properly.
    }

    public static boolean handleEnable(@NotNull NightPlugin plugin) {
        if (plugin instanceof NightCore nightCore) {
            init(nightCore);
        }
        else {
            CHILDRENS.add(plugin);
            plugin.info("Powered by " + core.getName());
        }

        return checkVersion(plugin);
    }

    public static boolean checkVersion(@NotNull NightCorePlugin plugin) {
        Version current = Version.getCurrent();
        if (current != Version.UNKNOWN && current.isSupported()) return true;

        plugin.warn("=".repeat(35));

        if (current == Version.UNKNOWN) {
            plugin.warn("WARNING: This plugin is not supposed to run on this server version!");
            plugin.warn("If server version is newer than " + Version.values()[Version.values().length - 1] + ", then wait for an update please.");
            plugin.warn("Otherwise this plugin will not work properly or even load.");
        }
        else if (current.isDeprecated()) {
            plugin.warn("WARNING: You're running an outdated server version (" + current.getLocalized() + ")!");
            plugin.warn("This version will no longer be supported in future relases.");
            plugin.warn("Please upgrade your server to " + Lists.next(current, (Version::isSupported)).getLocalized() + ".");
        }
        else if (current.isDropped()) {
            plugin.error("ERROR: You're running an unsupported server version (" + current.getLocalized() + ")!");
            plugin.error("Please upgrade your server to " + Lists.next(current, (Version::isSupported)).getLocalized() + ".");
        }

        plugin.warn("ABSOLUTELY NO DISCORD SUPPORT WILL BE PROVIDED");
        plugin.warn("=".repeat(35));

        if (current.isDropped()) {
            plugin.getPluginManager().disablePlugin(plugin);
            return false;
        }

        return true;
    }
}
