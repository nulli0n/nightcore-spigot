package su.nightexpress.nightcore.manager;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;

public abstract class AbstractListener<P extends NightCorePlugin> implements SimpeListener {

    @NonNull
    public final P plugin;

    public AbstractListener(@NonNull P plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerListeners() {
        this.plugin.getPluginManager().registerEvents(this, this.plugin);
    }
}
