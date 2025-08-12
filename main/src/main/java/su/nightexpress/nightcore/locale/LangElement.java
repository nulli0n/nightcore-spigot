package su.nightexpress.nightcore.locale;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.Set;

public interface LangElement {

    void load(@NotNull NightPlugin plugin, @NotNull FileConfig config, @NotNull String langCode);

    boolean isSupportedLocale(@NotNull String locale);

    @NotNull String getPath();

    @NotNull Set<String> getSupportedLocales();

    @NotNull LangValue getDefaultValue(@NotNull String langCode);
}
