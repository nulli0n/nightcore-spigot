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
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        this.broadcastWith((PlaceholderContext) null);
    }

    @Deprecated
    public void broadcast(@Nullable Consumer<Replacer> consumer) {
        if (this.isSilent()) return;

        Players.getOnline().forEach(player -> this.send(player, consumer));
        this.send(Bukkit.getServer().getConsoleSender(), consumer);
    }

    public void broadcastWith(@Nullable Consumer<PlaceholderContext.Builder> consumer) {
        PlaceholderContext context = null;
        if (consumer != null) {
            PlaceholderContext.Builder builder = PlaceholderContext.builder();
            consumer.accept(builder);
            context = builder.build();
        }

        this.broadcastWith(context);
    }

    public void broadcastWith(@Nullable PlaceholderContext context) {
        if (this.isSilent()) return;

        Players.getOnline().forEach(player -> this.sendWith(player, context));
        this.sendWith(Bukkit.getServer().getConsoleSender(), context);
    }

    public void send(@NotNull CommandSender sender) {
        this.sendWith(sender, (PlaceholderContext) null);
    }

    public void send(@NotNull Collection<? extends CommandSender> receivers) {
        this.sendWith(receivers, (PlaceholderContext) null);
    }

    @Deprecated
    public void send(@NotNull CommandSender sender, @Nullable Consumer<Replacer> consumer) {
        this.send(Collections.singleton(sender), consumer);
    }

    @Deprecated
    public void send(@NotNull Collection<? extends CommandSender> receivers, @Nullable Consumer<Replacer> consumer) {
        Replacer replacer = new Replacer();
        if (consumer != null) consumer.accept(replacer);

        if (this.data != null && this.data.sound() != null) {
            receivers.forEach(sender -> {
                if (sender instanceof Player player) this.data.sound().play(player);
            });
        }
        if (this.data != null && this.data.replacePlaceholders()) {
            receivers.forEach(sender -> {
                if (sender instanceof Player player) this.send(Collections.singleton(player), replacer.replacePlaceholderAPI(player).apply(this.text));
            });
        }
        else {
            this.send(receivers, replacer.apply(this.text));
        }
    }



    public void sendWith(@NotNull CommandSender sender, @Nullable Consumer<PlaceholderContext.Builder> consumer) {
        this.sendWith(Collections.singleton(sender), consumer);
    }

    public void sendWith(@NotNull Collection<? extends CommandSender> receivers, @Nullable Consumer<PlaceholderContext.Builder> consumer) {
        PlaceholderContext context = null;
        if (consumer != null) {
            PlaceholderContext.Builder builder = PlaceholderContext.builder();
            consumer.accept(builder);
            context = builder.build();
        }

        this.sendWith(receivers, context);
    }

    public void sendWith(@NotNull CommandSender sender, @Nullable PlaceholderContext context) {
        this.sendWith(Collections.singleton(sender), context);
    }

    public void sendWith(@NotNull Collection<? extends CommandSender> receivers, @Nullable PlaceholderContext placeholderContext) {
        if (this.data != null && this.data.sound() != null) {
            receivers.forEach(sender -> {
                if (sender instanceof Player player) this.data.sound().play(player);
            });
        }

        String replaced = placeholderContext == null ? this.text : placeholderContext.apply(this.text);
        List<? extends CommandSender> recipients = new ArrayList<>(receivers);

        if (this.data != null && this.data.replacePlaceholders()) {
            recipients.removeIf(sender -> {
                if (sender instanceof Player player) {
                    this.send(Collections.singleton(player), CommonPlaceholders.forPlaceholderAPI(player).apply(replaced));
                    return true;
                }
                return false;
            });
        }

        if (recipients.isEmpty()) return;

        this.send(recipients, replaced);
    }

    protected abstract void send(@NotNull Collection<? extends CommandSender> receivers, @NotNull String text);
}
