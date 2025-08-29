package su.nightexpress.nightcore.locale.message;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.LangValue;
import su.nightexpress.nightcore.locale.message.impl.ActionBarMessage;
import su.nightexpress.nightcore.locale.message.impl.ChatMessage;
import su.nightexpress.nightcore.locale.message.impl.SilentMessage;
import su.nightexpress.nightcore.locale.message.impl.TitleMessage;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class LangMessage implements LangValue {

    protected final String      text;
    protected final MessageData data;

    public LangMessage(@NotNull String text, @Nullable MessageData data) {
        this.text = text;
        this.data = data;
    }

    @NotNull
    public String getText() {
        return this.text;
    }

    @NotNull
    public static LangMessage createFromData(@NotNull String text, @NotNull MessageData data) {
        return switch (data.type()) {
            case CHAT -> new ChatMessage(text, data);
            case SILENT -> new SilentMessage(text, data);
            case TITLE -> new TitleMessage(text, data);
            case ACTION_BAR -> new ActionBarMessage(text, data);
        };
    }

    @NotNull
    public static LangMessage read(@NotNull FileConfig config, @NotNull String path) {
        List<String> text = new ArrayList<>(config.getStringList(path));
        if (text.isEmpty()) {
            text.add(config.getString(path, path));
        }

        MessageData.Builder builder = MessageData.builder();
        String dataLine = MessageData.extractAndParse(text.getFirst(), builder);
        MessageData data = builder.build();

        if (!dataLine.isEmpty()) text.set(0, dataLine);
        else text.removeFirst();

        String message = String.join(TagWrappers.BR, text);

        return createFromData(message, data);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        String[] textLines = ParserUtils.breakDownLineSplitters(this.text);
        String dataRaw = this.data == null ? "" : this.data.serialize();

        if (textLines.length == 1) {
            config.set(path, dataRaw + textLines[0]);
        }
        else {
            List<String> list = Lists.newList(textLines);
            list.addFirst(dataRaw);
            config.set(path, list);
        }
    }

    public abstract boolean isSilent();

    public void broadcast() {
        this.broadcast(null);
    }

    public void broadcast(@Nullable Consumer<Replacer> consumer) {
        if (this.isSilent()) return;

        Players.getOnline().forEach(player -> this.send(player, consumer));
        this.send(Bukkit.getServer().getConsoleSender(), consumer);
    }

    public void send(@NotNull CommandSender sender) {
        this.send(sender, replacer -> {});
    }

    public void send(@NotNull CommandSender sender, @Nullable Consumer<Replacer> consumer) {
        Replacer replacer = new Replacer();
        if (consumer != null) consumer.accept(replacer);

        if (sender instanceof Player player) {
            if (this.data != null && this.data.replacePlaceholders()) {
                replacer.replacePlaceholderAPI(player);
            }
            if (this.data != null && this.data.sound() != null) {
                this.data.sound().play(player);
            }
        }

        String text = replacer.apply(this.text);
        this.send(sender, text);
    }

    protected abstract void send(@NotNull CommandSender sender, @NotNull String text);
}
