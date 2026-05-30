package su.nightexpress.nightcore.language.entry;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;

@Deprecated
public interface LangElement {

    void write(@NonNull FileConfig config);

    void load(@NonNull NightCorePlugin plugin);

    void load(@NonNull FileConfig config);
}
