package su.nightexpress.nightcore.util.message;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Colorizer;
import su.nightexpress.nightcore.util.Reflex;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Deprecated
public class NexMessage {

    //private static final Pattern URL = Pattern.compile("(?:(https?)://)?([-\\w_\\.]{2,}\\.[a-z]{2,4})(/\\S*)?$");

    private String message;
    private final Map<String, NexComponent> components;

    public NexMessage(@NotNull String message) {
        this.message = Colorizer.apply(message);
        this.components = new HashMap<>();

        // Originally was in 'fromLegacyText' method of the TextComponent class.
        /*Matcher matcher = RegexUtil.getMatcher(URL, Colorizer.strip(this.message));
        while (RegexUtil.matcherFind(matcher)) {
            String url = matcher.group(0);
            String link = url.startsWith("http") ? url : "http://" + url;

            this.addComponent(url, url).openURL(link);
        }*/
    }

    @NotNull
    public NexComponent addComponent(@NotNull String placeholder, @NotNull String text) {
        NexComponent component = new NexComponent(text);
        String tag = "{@" + this.components.size() + "}";

        this.components.put(tag, component);
        this.message = this.message.replaceFirst(Pattern.quote(placeholder) + "(?!\\w)", tag);
        return component;
    }

    @NotNull
    public BaseComponent[] build() {
        return this.build(this.message);
    }

    @NotNull
    private BaseComponent[] build(@NotNull String line) {
        ComponentBuilder builder = new ComponentBuilder();

        StringBuilder text = new StringBuilder();
        for (int index = 0; index < line.length(); index++) {
            char letter = line.charAt(index);
            if (letter == '{') {
                int indexEnd = line.indexOf("}", index);
                if (indexEnd > index && ++indexEnd <= line.length()) {
                    String varRaw = line.substring(index, indexEnd);
                    if (varRaw.charAt(1) == '@') {
                        if (!text.isEmpty()) {
                            append(builder, fromLegacyText(text.toString()), ComponentBuilder.FormatRetention.ALL);
                            text = new StringBuilder();
                        }

                        index += varRaw.length() - 1;

                        NexComponent component = this.components.get(varRaw);
                        if (component == null) continue;

                        append(builder, component.build(), ComponentBuilder.FormatRetention.ALL);
                        continue;
                    }
                }
            }
            text.append(letter);
        }
        if (!text.isEmpty()) {
            append(builder, fromLegacyText(text.toString()), ComponentBuilder.FormatRetention.ALL);
        }
        TO_RETAIN.clear();
        return builder.create();
    }

    public void send(@NotNull CommandSender sender) {
        if (sender instanceof Player player) {
            for (String line : this.message.split("\n")) {
                player.spigot().sendMessage(this.build(line));
            }
        }
    }

    public void send(@NotNull CommandSender... senders) {
        Stream.of(senders).forEach(this::send);
    }

    public void send(@NotNull Collection<CommandSender> senders) {
        senders.forEach(this::send);
    }

    // Фикс форматирования компонентов на основе https://github.com/SpigotMC/BungeeCord/pull/3344/
    // Так как этот фикс не встроен в API спигота, и все равно работает не так, как нужно, будем использовать свой.

    private static final Set<BaseComponent> TO_RETAIN = ConcurrentHashMap.newKeySet();
    private static final Method             GET_DUMMY = Reflex.getMethod(ComponentBuilder.class, "getDummy");

    @NotNull
    public static ComponentBuilder append(@NotNull ComponentBuilder orig,
                                          @NotNull BaseComponent[] components,
                                          @NotNull ComponentBuilder.FormatRetention retention) {
        Preconditions.checkArgument(components.length != 0, "No components to append");
        for (BaseComponent component : components) {
            append(orig, component, retention);
        }
        return orig;
    }

    @NotNull
    public static ComponentBuilder append(@NotNull ComponentBuilder orig,
                                          @NotNull BaseComponent component,
                                          @NotNull ComponentBuilder.FormatRetention retention) {
        List<BaseComponent> parts = orig.getParts();
        BaseComponent previous = parts.isEmpty() ? null : parts.get(parts.size() - 1);
        if (previous == null) {
            previous = GET_DUMMY == null ? null : (BaseComponent)  Reflex.invokeMethod(GET_DUMMY, orig);
            Reflex.setFieldValue(orig, "dummy", null);
        }

        BaseComponent inheritance = TO_RETAIN.stream().filter(has -> has.equals(component)).findFirst().orElse(null);
        if (previous != null && inheritance != null) {
            component.copyFormatting(previous, retention, false);
            TO_RETAIN.remove(inheritance);
        }

        parts.add(component);
        orig.resetCursor();
        return orig;
    }

    public static BaseComponent[] fromLegacyText(@NotNull String message) {
        return fromLegacyText(message, ChatColor.WHITE);
    }

    public static BaseComponent[] fromLegacyText(@NotNull String message, @NotNull ChatColor defaultColor) {
        ArrayList<BaseComponent> components = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        TextComponent component = new TextComponent();

        for (int index = 0; index < message.length(); index++) {
            char letter = message.charAt(index);
            if (letter == ChatColor.COLOR_CHAR) {
                if (++index >= message.length()) break;

                letter = message.charAt(index);
                if (letter >= 'A' && letter <= 'Z') {
                    letter += 32;
                }

                ChatColor format;
                if (letter == 'x' && index + 12 < message.length()) {
                    StringBuilder hex = new StringBuilder("#");
                    for (int indexHex = 0; indexHex < 6; indexHex++) {
                        hex.append(message.charAt(index + 2 + (indexHex * 2)));
                    }
                    try {
                        format = ChatColor.of(hex.toString());
                    }
                    catch (IllegalArgumentException ex) {
                        format = null;
                    }

                    index += 12;
                }
                else {
                    format = ChatColor.getByChar( letter );
                }
                if (format == null) continue;

                if (builder.length() > 0) {
                    TextComponent old = component;
                    component = new TextComponent(old);
                    old.setText(builder.toString());
                    builder = new StringBuilder();
                    components.add(old);
                }

                if (format == ChatColor.BOLD) {
                    component.setBold( true );
                }
                else if (format == ChatColor.ITALIC) {
                    component.setItalic(true);
                }
                else if (format == ChatColor.UNDERLINE) {
                    component.setUnderlined(true);
                }
                else if (format == ChatColor.STRIKETHROUGH) {
                    component.setStrikethrough(true);
                }
                else if (format == ChatColor.MAGIC) {
                    component.setObfuscated(true);
                }
                else {
                    if (format == ChatColor.RESET) {
                        format = defaultColor;
                    }
                    component = new TextComponent();
                    component.setColor(format);
                }
                continue;
            }
            builder.append(letter);
        }

        if (!component.hasFormatting()) {
            TO_RETAIN.add(component);
        }

        component.setText(builder.toString());
        components.add(component);
        return components.toArray(new BaseComponent[0]);
    }
}
