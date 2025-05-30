package su.nightexpress.nightcore.language.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.text.TextRoot;

public class LangString extends LangEntry {

    private String   string;
    private TextRoot message;

    public LangString(@NotNull String path, @NotNull String defaultText) {
        super(path, defaultText);
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
        FileConfig config = plugin.getLang();

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
        this.message = NightMessage.from(string);
    }

    @NotNull
    public TextRoot getMessage() {
        return this.message;
    }

    @NotNull
    public String getLegacy() {
        return this.message.toLegacy();
    }
}
