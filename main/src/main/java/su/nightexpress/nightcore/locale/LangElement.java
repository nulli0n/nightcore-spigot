package su.nightexpress.nightcore.locale;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.FileConfig;

public interface LangElement {

    @Deprecated
    default void load(@NonNull NightPlugin plugin, @NonNull FileConfig config, @NonNull String langCode) {
        this.load(plugin, config);
    }

    void load(@NonNull NightPlugin plugin, @NonNull FileConfig config);

    @NonNull
    String getPath();

    @NonNull
    LangValue getDefaultValue();
}
