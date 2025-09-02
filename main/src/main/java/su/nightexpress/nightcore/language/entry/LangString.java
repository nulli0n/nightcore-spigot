package su.nightexpress.nightcore.language.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.text.TextRoot;

@Deprecated
public class LangString extends LangEntry {

    private String   string;

    public LangString(@NotNull String path, @NotNull String defaultText) {
        super(path, defaultText);
        this.string = defaultText;
    }

    @NotNull
    public static LangString of(@NotNull String path, @NotNull String defaultText) {
        return new LangString(path, defaultText);
    }

    @Override
    public void write(@NotNull FileConfig config) {
        config.set(this.path, this.defaultText);
    }

    @Override
    public void load(@NotNull NightCorePlugin plugin) {
        this.load(plugin.getLang());
    }

    @Override
    public void load(@NotNull FileConfig config) {
        if (!config.contains(this.path)) {
            this.write(config);
        }

        String text = config.getString(this.path, this.path);
        this.setString(text);
    }

    @NotNull
    public String getString() {
        return this.string;
    }

    public void setString(@NotNull String string) {
        this.string = string;
    }

    @NotNull
    public NightComponent asComponent() {
        return su.nightexpress.nightcore.util.text.night.NightMessage.parse(this.string);
    }

    @NotNull
    @Deprecated
    public TextRoot getMessage() {
        return NightMessage.from(this.string);
    }

    @NotNull
    @Deprecated
    public String getLegacy() {
        return this.getMessage().toLegacy();
    }
}
