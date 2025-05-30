package su.nightexpress.nightcore.language.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;

public interface LangElement {

    void write(@NotNull FileConfig config);

    void load(@NotNull NightCorePlugin plugin);
}
