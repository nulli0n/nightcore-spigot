package su.nightexpress.nightcore;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.chat.UniversalChatEventHandler;
import su.nightexpress.nightcore.bridge.scheduler.AdaptedScheduler;
import su.nightexpress.nightcore.command.CommandManager;
import su.nightexpress.nightcore.command.api.NightPluginCommand;
import su.nightexpress.nightcore.commands.command.NightCommand;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.language.LangManager;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangRegistry;
import su.nightexpress.nightcore.menu.impl.AbstractMenu;
import su.nightexpress.nightcore.ui.menu.MenuRegistry;
import su.nightexpress.nightcore.util.FileUtil;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Reflex;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class NightPlugin extends JavaPlugin implements NightCorePlugin {

    public static final String CONFIG_FILE = "config.yml";
    public static final String ENGINE_FILE = "engine.yml";

    protected AdaptedScheduler scheduler;

    protected NightCommand   rootCommand;
    protected List<Runnable> postLoaders;

    protected LangRegistry langRegistry;
    protected LangManager    langManager;
    protected CommandManager commandManager;

    protected FileConfig    engineConf;
    protected FileConfig    config;
    protected PluginDetails details;

    @Override
    public void onEnable() {
        if (!this.onInit() || !this.checkVersion()) {
            this.getPluginManager().disablePlugin(this);
            return;
        }

        long loadTook = System.currentTimeMillis();
        this.scheduler = Software.get().getScheduler(this);
        this.onStartup();
        this.loadManagers();
        this.info("Plugin loaded in " + (System.currentTimeMillis() - loadTook) + " ms!");
    }

    @Override
    public void onDisable() {
        this.unloadManagers();
        this.onShutdown();
    }

    protected boolean onInit() {
        NightCore.CHILDRENS.add(this);
        this.info("Powered by " + NightCore.get().getName());

        return true;
    }

    protected void onStartup() {

    }

    protected void onShutdown() {

    }

    public void reload() {
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
    @Deprecated
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
        Reflex.getStaticFields(clazz, UniPermission.class, false).forEach(permission -> {
            if (this.getPluginManager().getPermission(permission.getName()) == null) {
                this.getPluginManager().addPermission(permission);
            }
        });
    }

    protected void setupRegistries() {
        this.langRegistry = new LangRegistry(this);
        this.langRegistry.setup();

        this.addRegistries();

        this.langRegistry.loadLocale();
    }

    protected void addRegistries() {

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
        if (this.langRegistry.hasElements()) return; // Do not load if modern lang system present.

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

    protected boolean disableCommandManager() {
        return false;
    }

    protected void setupCommands() {
        if (this.disableCommandManager()) return;

        this.commandManager = new CommandManager(this);
        this.commandManager.setup();
    }

    protected void loadManagers() {
        this.postLoaders = new ArrayList<>(); // Initialize a list for post-loading code.

        this.setupConfig();         // Load configuration.
        this.setupRegistries();     // Load registries so the plugin modules can access them.
        this.setupLanguage();       // Load legacy language.
        this.setupPermissions();    // Register plugin permissions.
        this.setupCommands();       // Register plugin commands.

        this.enable();              // Load the plugin.

        this.postLoad();            // Load some stuff that needs to be injected after the rest managers.

        if (this.rootCommand != null) this.rootCommand.register();
        this.config.saveChanges();
        if (this.langManager != null) this.getLang().saveChanges();
        if (this.langRegistry != null) this.langRegistry.complete();
        this.engineConf.saveChanges();
    }

    protected void unloadManagers() {
        this.scheduler.cancelTasks(); // Stop all plugin tasks.

        this.disable();

        AbstractMenu.clearAll(this);            // Close all GUIs.
        MenuRegistry.closeAll();
        HandlerList.unregisterAll(this);        // Unregister all plugin listeners.

        if (this.rootCommand != null) this.rootCommand.unregister();
        if (this.commandManager != null) this.commandManager.shutdown();
        if (this.langRegistry != null) this.langRegistry.shutdown();
        if (this.langManager != null) this.langManager.shutdown();
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

    public void doReload(@NotNull CommandSender sender) {
        this.reload();
        CoreLang.PLUGIN_RELOADED.withPrefix(this).send(sender);
    }

    @NotNull
    @Deprecated
    public final NightPluginCommand getBaseCommand() {
        return this.commandManager.getMainCommand();
    }

    @NotNull
    @Deprecated
    public final LangManager getLangManager() {
        return this.langManager;
    }

    @NotNull
    public final LangRegistry getLangRegistry() {
        return this.langRegistry;
    }

    @NotNull
    @Deprecated
    public final CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public void registerListener(@NotNull Listener listener) {
        this.getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void addChatHandler(@NotNull EventPriority priority, @NotNull UniversalChatEventHandler handler) {
        NightCore.get().addChatHandler(priority, handler);
    }

    @Override
    public void removeChatHandler(@NotNull UniversalChatEventHandler handler) {
        NightCore.get().removeChatHandler(handler);
    }

    public void registerLang(@NotNull Class<? extends LangContainer> clazz) {
        this.langRegistry.register(clazz);
    }

    @Override
    public void injectLang(@NotNull Class<? extends LangContainer> langClass) {
        this.langRegistry.inject(langClass);
    }

    @Override
    public void injectLang(@NotNull LangContainer langContainer) {
        this.langRegistry.inject(langContainer);
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

    private boolean checkVersion() {
        Version current = Version.getCurrent();
        if (current != Version.UNKNOWN && current.isSupported()) return true;

        this.warn("=".repeat(35));

        if (current == Version.UNKNOWN) {
            this.warn("WARNING: This plugin is not supposed to run on this server version!");
            this.warn("If server version is newer than " + Version.values()[Version.values().length - 2] + ", then wait for an update please.");
            this.warn("The plugin may not work properly.");
        }
        else if (current.isDeprecated()) {
            this.warn("WARNING: You're running an outdated server version (" + current.getLocalized() + ")!");
            this.warn("This version will no longer be supported in future relases.");
            this.warn("Please upgrade your server to " + Lists.next(current, (Version::isSupported)).getLocalized() + ".");
        }
        else if (current.isDropped()) {
            this.error("ERROR: You're running an unsupported server version (" + current.getLocalized() + ")!");
            this.error("Please upgrade your server to " + Lists.next(current, (Version::isSupported)).getLocalized() + ".");
        }

        this.warn("ABSOLUTELY NO DISCORD SUPPORT WILL BE PROVIDED");
        this.warn("=".repeat(35));

        return !current.isDropped();
    }

    @Override
    @NotNull
    public PluginManager getPluginManager() {
        return this.getServer().getPluginManager();
    }

    @Override
    @NotNull
    public AdaptedScheduler scheduler() {
        return this.scheduler;
    }

    @Override
    @NotNull
    public su.nightexpress.nightcore.ui.inventory.MenuRegistry getMenuRegistry() {
        return NightCore.get().getMenuRegistry();
    }

    @Override
    public void runTask(@NotNull Runnable consumer) {
        this.scheduler.runTask(consumer);
    }

    @Override
    public void runTask(@NotNull Entity entity, @NotNull Runnable runnable) {
        this.scheduler.runTask(entity, runnable);
    }

    @Override
    public void runTask(@NotNull Location location, @NotNull Runnable runnable) {
        this.scheduler.runTask(location, runnable);
    }

    @Override
    public void runTask(@NotNull Chunk chunk, @NotNull Runnable runnable) {
        this.scheduler.runTask(chunk, runnable);
    }

    @Override
    public void runTaskAsync(@NotNull Runnable consumer) {
        this.scheduler.runTaskAsync(consumer);
    }

    @Override
    public void runTaskLater(@NotNull Runnable consumer, long delay) {
        this.scheduler.runTaskLater(consumer, delay);
    }

    @Override
    public void runTaskLaterAsync(@NotNull Runnable consumer, long delay) {
        this.scheduler.runTaskLaterAsync(consumer, delay);
    }

    @Override
    public void runTaskTimer(@NotNull Runnable consumer, long delay, long interval) {
        this.scheduler.runTaskTimer(consumer, delay, interval);
    }

    @Override
    public void runTaskTimerAsync(@NotNull Runnable consumer, long delay, long interval) {
        this.scheduler.runTaskTimerAsync(consumer, delay, interval);
    }
}
