package su.nightexpress.nightcore;

import com.github.Anon8281.universalScheduler.foliaScheduler.FoliaScheduler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.CommandManager;
import su.nightexpress.nightcore.command.api.NightPluginCommand;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.language.LangManager;
import su.nightexpress.nightcore.util.wrapper.UniTask;

public interface NightCorePlugin extends Plugin {

    boolean isEngine();

    void enable();

    void disable();

    void reload();

    NightPluginCommand getBaseCommand();

    @Override
    @NotNull
    FileConfig getConfig();

    @NotNull
    FileConfig getLang();

    @NotNull
    PluginDetails getDetails();

    void extractResources(@NotNull String jarPath);

    void extractResources(@NotNull String jarParh, @NotNull String toPath);

    @NotNull
    default String getNameLocalized() {
        return this.getDetails().getName();
    }

    @NotNull
    default String getPrefix() {
        return this.getDetails().getPrefix();
    }

    @NotNull
    default String[] getCommandAliases() {
        return this.getDetails().getCommandAliases();
    }

    @NotNull
    default String getLanguage() {
        return this.getDetails().getLanguage();
    }

    default void info(@NotNull String msg) {
        this.getLogger().info(msg);
    }

    default void warn(@NotNull String msg) {
        this.getLogger().warning(msg);
    }

    default void error(@NotNull String msg) {
        this.getLogger().severe(msg);
    }

    default void debug(@NotNull String msg) {
        this.info("[DEBUG] " + msg);
    }

    @NotNull
    LangManager getLangManager();

    @NotNull
    CommandManager getCommandManager();

    @NotNull
    default FoliaScheduler getScheduler() {
        return new FoliaScheduler(this);
    }

    @NotNull
    default PluginManager getPluginManager() {
        return this.getServer().getPluginManager();
    }

    default void runTask(@NotNull Runnable consumer) {
        this.getScheduler().runTask(consumer);
    }

    default void runTaskAsync(@NotNull Runnable consumer) {
        this.getScheduler().runTaskAsynchronously(consumer);
    }

    default void runTaskLater(@NotNull Runnable consumer, long delay) {
        this.getScheduler().runTaskLater(consumer, delay);
    }

    default void runTaskLaterAsync(@NotNull Runnable consumer, long delay) {
        this.getScheduler().runTaskLaterAsynchronously(consumer, delay);
    }

    default void runTaskTimer(@NotNull Runnable consumer, long delay, long interval) {
        this.getScheduler().runTaskTimer(consumer, delay, interval);
    }

    default void runTaskTimerAsync(@NotNull Runnable consumer, long delay, long interval) {
        this.getScheduler().runTaskTimerAsynchronously(consumer, delay, interval);
    }

    @NotNull
    default UniTask createTask(@NotNull Runnable runnable) {
        return new UniTask(this, runnable);
    }

    @NotNull
    default UniTask createAsyncTask(@NotNull Runnable runnable) {
        return this.createTask(runnable).setAsync();
    }
}
