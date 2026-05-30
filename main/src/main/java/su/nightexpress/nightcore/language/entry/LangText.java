package su.nightexpress.nightcore.language.entry;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

    public LangText(@NonNull String path, @NonNull String... defaultText) {
        super(path, String.join("\n", defaultText));
        this.setMessage(LangMessage.parse(String.join(Placeholders.TAG_LINE_BREAK, this.getDefaultText()), Engine.core()
            .getPrefix()));
    }

    @NonNull
    public static LangText of(@NonNull String path, @NonNull String defaultText) {
        return new LangText(path, defaultText);
    }

    @NonNull
    public static LangText of(@NonNull String path, @NonNull String... defaultText) {
        return new LangText(path, defaultText);
    }

    @Override
    public void write(@NonNull FileConfig config) {
        String[] textSplit = this.defaultText.split("\n");
        config.set(this.path, textSplit.length > 1 ? Arrays.asList(textSplit) : this.defaultText);
    }

    @Override
    public void load(@NonNull NightCorePlugin plugin) {
        this.load(plugin.getLang(), plugin.getPrefix());
    }

    @Override
    public void load(@NonNull FileConfig config) {
        this.load(config, null);
    }

    public void load(@NonNull FileConfig config, @Nullable String defaultPrefix) {
        if (!config.contains(this.path)) {
            this.write(config);
        }

        List<String> text = new ArrayList<>(config.getStringList(this.path));
        if (text.isEmpty()) {
            text.add(config.getString(this.path, this.path));
        }

        this.setMessage(LangMessage.parse(String.join(Placeholders.TAG_LINE_BREAK, text), defaultPrefix));
    }

    @NonNull
    public LangMessage getMessage() {
        return this.message;
    }

    @NonNull
    public LangMessage getMessage(@NonNull NightCorePlugin plugin) {
        return this.message.setPrefix(plugin);
    }

    public void setMessage(@NonNull LangMessage message) {
        this.message = message;
    }
}
