package su.nightexpress.nightcore.language.entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.language.message.LangMessage;
import su.nightexpress.nightcore.util.Placeholders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
public class LangText extends LangEntry {

    private LangMessage message;

    public LangText(@NotNull String path, @NotNull String... defaultText) {
        super(path, String.join("\n", defaultText));
        this.setMessage(LangMessage.parse(String.join(Placeholders.TAG_LINE_BREAK, this.getDefaultText()), Engine.core().getPrefix()));
    }

    @NotNull
    public static LangText of(@NotNull String path, @NotNull String defaultText) {
        return new LangText(path, defaultText);
    }

    @NotNull
    public static LangText of(@NotNull String path, @NotNull String... defaultText) {
        return new LangText(path, defaultText);
    }

    @Override
    public void write(@NotNull FileConfig config) {
        String[] textSplit = this.defaultText.split("\n");
        config.set(this.path, textSplit.length > 1 ? Arrays.asList(textSplit) : this.defaultText);
    }

    @Override
    public void load(@NotNull NightCorePlugin plugin) {
        this.load(plugin.getLang(), plugin.getPrefix());
    }

    @Override
    public void load(@NotNull FileConfig config) {
        this.load(config, null);
    }

    public void load(@NotNull FileConfig config, @Nullable String defaultPrefix) {
        if (!config.contains(this.path)) {
            this.write(config);
        }

        List<String> text = new ArrayList<>(config.getStringList(this.path));
        if (text.isEmpty()) {
            text.add(config.getString(this.path, this.path));
        }

        this.setMessage(LangMessage.parse(String.join(Placeholders.TAG_LINE_BREAK, text), defaultPrefix));
    }

    @NotNull
    public LangMessage getMessage() {
        return this.message;
    }

    @NotNull
    public LangMessage getMessage(@NotNull NightCorePlugin plugin) {
        return this.message.setPrefix(plugin);
    }

    public void setMessage(@NotNull LangMessage message) {
        this.message = message;
    }
}
