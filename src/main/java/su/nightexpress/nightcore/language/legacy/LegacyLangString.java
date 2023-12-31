package su.nightexpress.nightcore.language.legacy;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.language.entry.LangEntry;
import su.nightexpress.nightcore.util.Colorizer;

@Deprecated
public class LegacyLangString extends LangEntry<String> {

    private String string;

    public LegacyLangString(@NotNull String path, @NotNull String defaultText) {
        super(path, defaultText);
    }

    @NotNull
    public static LegacyLangString of(@NotNull String path, @NotNull String defaultText) {
        return new LegacyLangString(path, defaultText);
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
        String text = config.getString(this.getPath(), "<" + this.getPath() + ">");
        this.setString(text);

        return this.getString();
    }

    @NotNull
    public String getString() {
        return string;
    }

    public void setString(@NotNull String string) {
        this.string = Colorizer.apply(string);
    }
}
