package su.nightexpress.nightcore.language.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.text.TextRoot;

public class LangString extends LangEntry<String> {

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
    public boolean write(@NotNull FileConfig config) {
        if (!config.contains(this.getPath())) {
            String textDefault = this.getDefaultText();
            config.set(this.getPath(), textDefault);
            return true;
        }
        return false;
    }

    @Override
    @NotNull
    public String load(@NotNull NightCorePlugin plugin) {
        FileConfig config = plugin.getLang();

        this.write(config);
        String text = config.getString(this.getPath(), this.getPath());
        this.setString(text);

        return this.getString();
    }

    @NotNull
    public String getString() {
        return string;
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
        return this.getMessage().toLegacy();
    }
}
