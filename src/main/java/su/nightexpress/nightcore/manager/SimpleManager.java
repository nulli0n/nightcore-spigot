package su.nightexpress.nightcore.manager;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;

public abstract class SimpleManager<P extends NightCorePlugin> {

    protected final P plugin;

    public SimpleManager(@NotNull P plugin) {
        this.plugin = plugin;
    }

    @NotNull
    public P plugin() {
        return this.plugin;
    }

    public void setup() {
        this.onLoad();
    }

    public void shutdown() {
        this.onShutdown();
    }

    public void reload() {
        this.shutdown();
        this.setup();
    }

    protected abstract void onLoad();

    protected abstract void onShutdown();
}
