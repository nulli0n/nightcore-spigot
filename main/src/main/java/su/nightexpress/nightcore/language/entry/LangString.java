package su.nightexpress.nightcore.language.entry;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.text.TextRoot;

@Deprecated
public class LangString extends LangEntry {

    private String string;

    public LangString(@NonNull String path, @NonNull String defaultText) {
        super(path, defaultText);
        this.string = defaultText;
    }

    @NonNull
    public static LangString of(@NonNull String path, @NonNull String defaultText) {
        return new LangString(path, defaultText);
    }

    @Override
    public void write(@NonNull FileConfig config) {
        config.set(this.path, this.defaultText);
    }

    @Override
    public void load(@NonNull NightCorePlugin plugin) {
        this.load(plugin.getLang());
    }

    @Override
    public void load(@NonNull FileConfig config) {
        if (!config.contains(this.path)) {
            this.write(config);
        }

        String text = config.getString(this.path, this.path);
        this.setString(text);
    }

    @NonNull
    public String getString() {
        return this.string;
    }

    public void setString(@NonNull String string) {
        this.string = string;
    }

    @NonNull
    public NightComponent asComponent() {
        return su.nightexpress.nightcore.util.text.night.NightMessage.parse(this.string);
    }

    @NonNull
    @Deprecated
    public TextRoot getMessage() {
        return NightMessage.from(this.string);
    }

    @NonNull
    @Deprecated
    public String getLegacy() {
        return this.getMessage().toLegacy();
    }
}
