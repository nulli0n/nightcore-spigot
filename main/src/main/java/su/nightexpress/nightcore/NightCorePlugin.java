package su.nightexpress.nightcore;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.foliaScheduler.FoliaScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.CommandManager;
import su.nightexpress.nightcore.command.api.NightPluginCommand;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.language.LangManager;
import su.nightexpress.nightcore.util.wrapper.UniTask;

import java.util.function.Consumer;

public interface NightCorePlugin extends Plugin {

    //boolean isEngine();

    void enable();

    void disable();

    void reload();

    @Deprecated
    NightPluginCommand getBaseCommand();

    @Override
    @NotNull FileConfig getConfig();

    @NotNull FileConfig getLang();

    @NotNull PluginDetails getDetails();

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

    @NotNull LangManager getLangManager();

    @NotNull CommandManager getCommandManager();

    @Deprecated
    @NotNull
    default BukkitScheduler getScheduler() {
        return this.getServer().getScheduler();
    }

    @NotNull
    default FoliaScheduler getFoliaScheduler() {
        return new FoliaScheduler(this);
    }

    @NotNull
    default PluginManager getPluginManager() {
        return this.getServer().getPluginManager();
    }

    default void runTask(@NotNull Runnable consumer) {
        this.getFoliaScheduler().runTask(this, consumer);
    }

    default void runTask(Entity entity, @NotNull Runnable consumer) {
        if (!UniversalScheduler.isFolia) {
            this.runTask(consumer);
            return;
        }

        this.getFoliaScheduler().execute(entity, consumer);
    }

    default void runTask(Location location, @NotNull Runnable consumer) {
        if (!UniversalScheduler.isFolia) {
            this.runTask(consumer);
            return;
        }

        this.getFoliaScheduler().execute(location, consumer);
    }

    default void runTask(Chunk chunk, @NotNull Runnable consumer) {
        if (!UniversalScheduler.isFolia) {
            this.runTask(consumer);
            return;
        }

        Bukkit.getServer().getRegionScheduler().execute(this, chunk.getWorld(), chunk.getX(), chunk.getZ(),
            consumer);
    }

    default void runTaskAsync(@NotNull Runnable consumer) {
        this.getFoliaScheduler().runTaskAsynchronously(this, consumer);
    }

    default void runTaskLater(@NotNull Runnable consumer, long delay) {
        this.getFoliaScheduler().runTaskLater(this, consumer, delay);
    }

    default void runTaskLaterAsync(@NotNull Runnable consumer, long delay) {
        this.getFoliaScheduler().runTaskLaterAsynchronously(this, consumer, delay);
    }

    default void runTaskTimer(@NotNull Runnable consumer, long delay, long interval) {
        this.getFoliaScheduler().runTaskTimer(this, consumer, delay, interval);
    }

    default void runTaskTimerAsync(@NotNull Runnable consumer, long delay, long interval) {
        this.getFoliaScheduler().runTaskTimerAsynchronously(this, consumer, delay, interval);
    }

    @Deprecated
    default void runTask(@NotNull Consumer<BukkitTask> consumer) {
        this.getScheduler().runTask(this, consumer);
    }

    @Deprecated
    default void runTaskAsync(@NotNull Consumer<BukkitTask> consumer) {
        this.getScheduler().runTaskAsynchronously(this, consumer);
    }

    @Deprecated
    default void runTaskLater(@NotNull Consumer<BukkitTask> consumer, long delay) {
        this.getScheduler().runTaskLater(this, consumer, delay);
    }

    @Deprecated
    default void runTaskLaterAsync(@NotNull Consumer<BukkitTask> consumer, long delay) {
        this.getScheduler().runTaskLaterAsynchronously(this, consumer, delay);
    }

    @Deprecated
    default void runTaskTimer(@NotNull Consumer<BukkitTask> consumer, long delay, long interval) {
        this.getScheduler().runTaskTimer(this, consumer, delay, interval);
    }

    @Deprecated
    default void runTaskTimerAsync(@NotNull Consumer<BukkitTask> consumer, long delay, long interval) {
        this.getScheduler().runTaskTimerAsynchronously(this, consumer, delay, interval);
    }

    @Deprecated
    @NotNull
    default UniTask createTask(@NotNull Runnable runnable) {
        return new UniTask(this, runnable);
    }

    @Deprecated
    @NotNull
    default UniTask createAsyncTask(@NotNull Runnable runnable) {
        return this.createTask(runnable).setAsync();
    }
}
