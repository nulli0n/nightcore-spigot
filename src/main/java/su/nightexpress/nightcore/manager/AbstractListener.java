package su.nightexpress.nightcore.manager;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;

public abstract class AbstractListener<P extends NightCorePlugin> implements SimpeListener {

    @NotNull
    public final P plugin;

    public AbstractListener(@NotNull P plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerListeners() {
        this.plugin.getPluginManager().registerEvents(this, this.plugin);
    }
}
