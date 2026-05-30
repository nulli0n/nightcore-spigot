package su.nightexpress.nightcore;

import java.util.function.Consumer;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.chat.UniversalChatEventHandler;
import su.nightexpress.nightcore.bridge.scheduler.AdaptedScheduler;
import su.nightexpress.nightcore.command.CommandManager;
import su.nightexpress.nightcore.command.api.NightPluginCommand;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.language.LangManager;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangElement;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;
import su.nightexpress.nightcore.ui.inventory.MenuRegistry;
import su.nightexpress.nightcore.util.wrapper.UniTask;

public interface NightCorePlugin extends Plugin {

    void enable();

    void disable();

    void reload();

    @Deprecated
    NightPluginCommand getBaseCommand();

    @Override
    @NonNull
    FileConfig getConfig();

    @Deprecated
    @NonNull
    FileConfig getLang();

    @NonNull
    PluginDetails getDetails();

    void extractResources(@NonNull String jarPath);

    void extractResources(@NonNull String jarParh, @NonNull String toPath);

    void injectLang(@NonNull Class<? extends LangContainer> langClass);

    /**
     * Saves and loads {@link LangElement} objects from the provided {@link LangContainer} object into the lang config
     * file according to selected
     * language during the "enable" plugin's phase if the same can not be achieved through
     * {@link NightPlugin#registerLang(Class)}
     * <br>
     * <b>Note:</b> This can not be used outside of the {@link NightPlugin#enable()} phase.
     * 
     * @param langContainer LangContainer object with some LangElement fields defined.
     * @see NightPlugin#registerLang(Class)
     */
    void injectLang(@NonNull LangContainer langContainer);

    @NonNull
    default String getNameLocalized() {
        return this.getDetails().getName();
    }

    @NonNull
    default String getPrefix() {
        return this.getDetails().getPrefix();
    }

    @NonNull
    default String[] getCommandAliases() {
        return this.getDetails().getCommandAliases();
    }

    @NonNull
    @Deprecated
    default String getLanguage() {
        return this.getDetails().getLanguage();
    }

    default void info(@NonNull String msg) {
        this.getLogger().info(msg);
    }

    default void warn(@NonNull String msg) {
        this.getLogger().warning(msg);
    }

    default void error(@NonNull String msg) {
        this.getLogger().severe(msg);
    }

    default void debug(@NonNull String msg) {
        this.info("[DEBUG] " + msg);
    }

    void registerListener(@NonNull Listener listener);

    void addChatHandler(@NonNull EventPriority priority, @NonNull UniversalChatEventHandler handler);

    void removeChatHandler(@NonNull UniversalChatEventHandler handler);

    @Deprecated
    @NonNull
    LangManager getLangManager();

    @Deprecated
    @NonNull
    CommandManager getCommandManager();

    @NonNull
    MenuRegistry getMenuRegistry();

    @Deprecated
    @NonNull
    default BukkitScheduler getScheduler() {
        return this.getServer().getScheduler();
    }

    @NonNull
    AdaptedScheduler scheduler();

    @NonNull
    DialogRegistry dialogRegistry();

    @NonNull
    PluginManager getPluginManager();

    void runTask(@NonNull Runnable consumer);

    void runTask(Entity entity, @NonNull Runnable consumer);

    void runTask(Location location, @NonNull Runnable consumer);

    void runTask(Chunk chunk, @NonNull Runnable consumer);

    void runTaskAsync(@NonNull Runnable consumer);

    void runTaskLater(@NonNull Runnable consumer, long delay);

    void runTaskLaterAsync(@NonNull Runnable consumer, long delay);

    void runTaskTimer(@NonNull Runnable consumer, long delay, long interval);

    void runTaskTimerAsync(@NonNull Runnable consumer, long delay, long interval);

    @Deprecated
    default void runTask(@NonNull Consumer<BukkitTask> consumer) {
        this.getScheduler().runTask(this, consumer);
    }

    @Deprecated
    default void runTaskAsync(@NonNull Consumer<BukkitTask> consumer) {
        this.getScheduler().runTaskAsynchronously(this, consumer);
    }

    @Deprecated
    default void runTaskLater(@NonNull Consumer<BukkitTask> consumer, long delay) {
        this.getScheduler().runTaskLater(this, consumer, delay);
    }

    @Deprecated
    default void runTaskLaterAsync(@NonNull Consumer<BukkitTask> consumer, long delay) {
        this.getScheduler().runTaskLaterAsynchronously(this, consumer, delay);
    }

    @Deprecated
    default void runTaskTimer(@NonNull Consumer<BukkitTask> consumer, long delay, long interval) {
        this.getScheduler().runTaskTimer(this, consumer, delay, interval);
    }

    @Deprecated
    default void runTaskTimerAsync(@NonNull Consumer<BukkitTask> consumer, long delay, long interval) {
        this.getScheduler().runTaskTimerAsynchronously(this, consumer, delay, interval);
    }

    @NonNull
    @Deprecated
    default UniTask createTask(@NonNull Runnable runnable) {
        return new UniTask(this, runnable);
    }

    @NonNull
    @Deprecated
    default UniTask createAsyncTask(@NonNull Runnable runnable) {
        return this.createTask(runnable).setAsync();
    }
}
