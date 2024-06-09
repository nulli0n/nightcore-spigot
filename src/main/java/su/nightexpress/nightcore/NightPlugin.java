package su.nightexpress.nightcore;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.CommandManager;
import su.nightexpress.nightcore.command.api.NightPluginCommand;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.language.LangManager;
import su.nightexpress.nightcore.menu.impl.AbstractMenu;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

import java.io.File;
import java.util.function.Predicate;

public abstract class NightPlugin extends JavaPlugin implements NightCorePlugin {

    protected LangManager    langManager;
    protected CommandManager commandManager;

    protected FileConfig    config;
    protected PluginDetails details;
    private   boolean       isEngine;

    public final boolean isEngine() {
        return this.isEngine;
    }

    @Override
    public void onEnable() {
        long loadTook = System.currentTimeMillis();
        this.isEngine = this instanceof NightCore;

        if (!this.isEngine()) {
            Plugins.CORE.addChildren(this);
        }

        Version version = Version.getCurrent();
        if (version == Version.UNKNOWN) {
            this.warn("=".repeat(35));
            this.warn("WARNING: You're running an unsupported server version!");
            this.warn("Expect bugs and broken features.");
            this.warn("! NO DISCORD SUPPORT WILL BE GIVEN !");
            this.warn("=".repeat(35));
        }
        else if (version.isDeprecated()) {
            this.warn("=".repeat(35));
            this.warn("WARNING: You're running an outdated/deprecated server version (" + Version.getCurrent().getLocalized() + ")!");
            this.warn("Support for this version will be dropped soon.");
            this.warn("Please, upgrade your server to at least " + Lists.next(Version.getCurrent(), Predicate.not(Version::isDeprecated)).getLocalized() + ".");
            this.warn("=".repeat(35));
        }

        this.loadManagers();
        this.info("Plugin loaded in " + (System.currentTimeMillis() - loadTook) + " ms!");
    }

    @Override
    public void onDisable() {
        this.unloadManagers();
    }

    public final void reload() {
        if (this.isEngine()) {
            this.setupConfig();
            this.setupLanguage();
            return;
        }
        this.unloadManagers();
        this.loadManagers();
    }

    /*@Override
    public final void reloadConfig() {
        this.getConfig().reload();
        this.loadConfig();
    }

    public final void reloadLang() {
        this.getLang().reload();
        this.loadLang();
    }*/

    @Override
    @NotNull
    public final FileConfig getConfig() {
        return this.config;
    }

    @NotNull
    public final FileConfig getLang() {
        return this.getLangManager().getConfig();
    }

    @NotNull
    @Override
    public PluginDetails getDetails() {
        return this.details == null ? this.getDefaultDetails() : this.details;
    }

    @NotNull
    protected abstract PluginDetails getDefaultDetails();

    public void registerPermissions(@NotNull Class<?> clazz) {
        Reflex.getFields(clazz, UniPermission.class).forEach(permission -> {
            if (this.getPluginManager().getPermission(permission.getName()) == null) {
                this.getPluginManager().addPermission(permission);
            }
        });
    }

    protected void setupConfig() {
        this.config = FileConfig.loadOrExtract(this, "config.yml");
        this.details = PluginDetails.read(this);

        if (this.getDetails().getConfigClass() != null) {
            this.getConfig().initializeOptions(this.getDetails().getConfigClass());
        }
    }

    protected void setupLanguage() {
        this.langManager = new LangManager(this);
        this.langManager.setup();

        if (this.getDetails().getLangClass() != null) {
            this.getLangManager().loadEntries(this.getDetails().getLangClass());
        }
    }

    protected void setupPermissions() {
        Class<?> clazz = this.getDetails().getPermissionsClass();
        if (clazz == null) return;

        this.registerPermissions(clazz);
    }

    protected void setupCommands() {
        this.commandManager = new CommandManager(this);
        this.commandManager.setup();
    }

    protected void loadManagers() {
        this.setupConfig();         // Load configuration.
        this.setupLanguage();       // Load language.
        this.setupPermissions();    // Register plugin permissions.
        this.setupCommands();       // Register plugin commands.

        this.enable();              // Load the plugin.

        this.getConfig().saveChanges();
        this.getLang().saveChanges();
    }

    protected void unloadManagers() {
        this.getScheduler().cancelTasks(this); // Stop all plugin tasks.

        this.disable();

        AbstractMenu.clearAll(this); // Close all GUIs.
        HandlerList.unregisterAll(this); // Unregister all plugin listeners.

        this.getCommandManager().shutdown();
        this.getLangManager().shutdown();
        this.details = null; // Reset so it will use default ones on config read.
    }

    @NotNull
    public final NightPluginCommand getBaseCommand() {
        return this.getCommandManager().getMainCommand();
    }

    @NotNull
    public final LangManager getLangManager() {
        return this.langManager;
    }

    @NotNull
    public final CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public void extractResources(@NotNull String jarPath) {
        this.extractResources(jarPath, this.getDataFolder() + jarPath);
    }

    @Override
    public void extractResources(@NotNull String jarPath, @NotNull String targetPath) {
        File destination = new File(targetPath);
        if (destination.exists()) return;

        if (jarPath.startsWith("/")) {
            jarPath = jarPath.substring(1);
        }
        if (jarPath.endsWith("/")) {
            jarPath = jarPath.substring(0, jarPath.length() - 1);
        }

        FileUtil.extractResources(this.getFile(), jarPath, destination);
    }
}
