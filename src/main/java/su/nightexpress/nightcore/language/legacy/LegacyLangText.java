package su.nightexpress.nightcore.language.legacy;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.language.entry.LangEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
public class LegacyLangText extends LangEntry<LegacyLangMessage> {

    private LegacyLangMessage message;

    public LegacyLangText(@NotNull String path, @NotNull String... defaultText) {
        super(path, String.join("\n", defaultText));
    }

    @NotNull
    public static LegacyLangText of(@NotNull String path, @NotNull String defaultText) {
        return new LegacyLangText(path, defaultText);
    }

    @NotNull
    public static LegacyLangText of(@NotNull String path, @NotNull String... defaultText) {
        return new LegacyLangText(path, defaultText);
    }

    @Override
    public boolean write(@NotNull FileConfig config) {
        if (!config.contains(this.getPath())) {
            String textDefault = this.getDefaultText();
            String[] textSplit = textDefault.split("\n");
            config.set(this.getPath(), textSplit.length > 1 ? Arrays.asList(textSplit) : textDefault);
            return true;
        }
        return false;
    }

    @Override
    @NotNull
    public LegacyLangMessage load(@NotNull NightCorePlugin plugin) {
        FileConfig config = plugin.getLang();

        this.write(config);

        List<String> text = new ArrayList<>(config.getStringList(this.getPath()));
        if (text.isEmpty()) {
            text.add(config.getString(this.getPath(), "<" + this.getPath() + ">"));
        }

        //String text = !list.isEmpty() ? String.join("\\n", list) : config.getString(this.getPath(), "<" + this.getPath() + ">");
        this.setMessage(new LegacyLangMessage(plugin, text));

        return this.getMessage();
    }

    @NotNull
    public LegacyLangMessage getMessage() {
        return message;
    }

    public void setMessage(@NotNull LegacyLangMessage message) {
        this.message = message;
    }
}
