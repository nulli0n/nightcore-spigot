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
import su.nightexpress.nightcore.ui.menu.MenuRegistry;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class NightPlugin extends JavaPlugin implements NightCorePlugin {

    public static final String CONFIG_FILE = "config.yml";
    public static final String ENGINE_FILE = "engine.yml";

    protected List<Runnable> postLoaders;

    protected LangManager    langManager;
    protected CommandManager commandManager;

    protected FileConfig    engineConf;
    protected FileConfig    config;
    protected PluginDetails details;

    private boolean isCore() {
        return this instanceof NightCore;
    }

    @Override
    public void onEnable() {
        Version.printCaution(this);
        if (Version.getCurrent().isDropped()) {
            this.getPluginManager().disablePlugin(this);
            return;
        }

        if (!this.isCore()) {
            Plugins.getCore().addChildren(this);
        }

        long loadTook = System.currentTimeMillis();
        this.loadManagers();
        this.info("Plugin loaded in " + (System.currentTimeMillis() - loadTook) + " ms!");
    }

    @Override
    public void onDisable() {
        this.unloadManagers();
    }

    public final void reload() {
        if (this.isCore()) {
            this.setupConfig();
            this.setupLanguage();
            return;
        }
        this.unloadManagers();
        this.loadManagers();
    }

    @Override
    @NotNull
    public final FileConfig getConfig() {
        return this.config;
    }

    @NotNull
    public FileConfig getEngineConfig() {
        return this.engineConf;
    }

    @NotNull
    public final FileConfig getLang() {
        return this.langManager.getConfig();
    }

    @NotNull
    @Override
    public PluginDetails getDetails() {
        if (this.details == null) throw new IllegalStateException("Plugin is not yet initialized!");

        return this.details;
        //return this.details == null ? this.getDefaultDetails() : this.details;
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
        this.engineConf = FileConfig.loadOrExtract(this, ENGINE_FILE);
        this.config = FileConfig.loadOrExtract(this, CONFIG_FILE);

        // ---------- MIGRATION - START ----------
        if (this.config.contains("Plugin")) {
            PluginDetails details = PluginDetails.read(this, this.config, this.getDefaultDetails());
            details.write(this.engineConf, "");
            this.config.remove("Plugin");
        }
        // ---------- MIGRATION - END ----------

        this.details = PluginDetails.read(this, this.engineConf, this.getDefaultDetails());

        if (this.details.getConfigClass() != null) {
            this.config.initializeOptions(this.details.getConfigClass());
        }
    }

    protected void setupLanguage() {
        this.langManager = new LangManager(this);
        this.langManager.setup();

        if (this.details.getLangClass() != null) {
            this.langManager.loadEntries(this.details.getLangClass());
        }
    }

    protected void setupPermissions() {
        Class<?> clazz = this.details.getPermissionsClass();
        if (clazz == null) return;

        this.registerPermissions(clazz);
    }

    protected void setupCommands() {
        this.commandManager = new CommandManager(this);
        this.commandManager.setup();
    }

    protected void loadManagers() {
        this.postLoaders = new ArrayList<>(); // Initialize a list for post-loading code.

        this.setupConfig();         // Load configuration.
        this.setupLanguage();       // Load language.
        this.setupPermissions();    // Register plugin permissions.
        this.setupCommands();       // Register plugin commands.

        this.enable();              // Load the plugin.

        this.postLoad();            // Load some stuff that needs to be injected after the rest managers.

        this.config.saveChanges();
        this.getLang().saveChanges();
        this.engineConf.saveChanges();
    }

    protected void unloadManagers() {
        this.getScheduler().cancelTasks(this);  // Stop all plugin tasks.

        this.disable();

        AbstractMenu.clearAll(this);            // Close all GUIs.
        MenuRegistry.closeAll();
        HandlerList.unregisterAll(this);        // Unregister all plugin listeners.

        this.commandManager.shutdown();
        this.langManager.shutdown();
        this.details = null;                           // Reset so it will use default ones on config read.
    }

    protected void postLoad() {
        //this.debug("Inject post-loading code...");
        this.postLoaders.forEach(Runnable::run);
        this.postLoaders.clear();
        this.postLoaders = null; // Prevent from adding post-load code during runtime.
    }

    public void onPostLoad(@NotNull Runnable runnable) {
        if (this.postLoaders == null) {
            throw new IllegalStateException("Can't inject post loading code during runtime.");
        }
        this.postLoaders.add(runnable);
    }

    @NotNull
    @Deprecated
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
