package su.nightexpress.nightcore.util.bridge.wrapper;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.text.NightStyle;
import su.nightexpress.nightcore.bridge.text.NightTextDecoration;
import su.nightexpress.nightcore.bridge.text.adapter.TextComponentAdapter;
import su.nightexpress.nightcore.bridge.text.contents.NightObjectContents;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;
import su.nightexpress.nightcore.bridge.text.event.NightHoverEvent;
import su.nightexpress.nightcore.bridge.text.impl.*;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface NightComponent {

    @NotNull
    static NightTextComponent empty() {
        return NightTextComponent.EMPTY;
    }

    @NotNull
    static NightTextComponent newline() {
        return NightTextComponent.NEWLINE;
    }

    @NotNull
    static NightTextComponent space() {
        return NightTextComponent.SPACE;
    }

    @NotNull
    static NightKeybindComponent keybind(@NotNull String keybind) {
        return keybind(keybind, NightStyle.EMPTY);
    }

    @NotNull
    static NightKeybindComponent keybind(@NotNull String keybind, @NotNull NightStyle style) {
        return NightKeybindComponent.create(keybind, style);
    }

    @NotNull
    static NightObjectComponent object(@NotNull NightStyle style, @NotNull NightObjectContents contents) {
        return NightObjectComponent.create(style, contents);
    }

    @NotNull
    static NightTextComponent text(@NotNull String content, @NotNull NightStyle style) {
        return content.isEmpty() ? empty() : NightTextComponent.create(content, style);
    }

    @NotNull
    static NightTextComponent text(@NotNull String content) {
        return text(content, NightStyle.EMPTY);
    }

    @NotNull
    static NightTextComponent text(boolean value) {
        return text(String.valueOf(value));
    }

    @NotNull
    static NightTextComponent text(char value) {
        if (value == '\n') return newline();
        if (value == ' ') return space();
        return text(String.valueOf(value));
    }

    @NotNull
    static NightTextComponent text(double value) {
        return text(String.valueOf(value));
    }

    @NotNull
    static NightTextComponent text(float value) {
        return text(String.valueOf(value));
    }

    @NotNull
    static NightTextComponent text(int value) {
        return text(String.valueOf(value));
    }

    @NotNull
    static NightTextComponent text(long value) {
        return text(String.valueOf(value));
    }

    @NotNull
    static NightTranslatableComponent translatable(@NotNull String key) {
        return translatable(key, null);
    }

    @NotNull
    static NightTranslatableComponent translatable(@NotNull String key, @Nullable String fallback) {
        return translatable(key, fallback, NightStyle.EMPTY);
    }

    @NotNull
    static NightTranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull NightStyle style) {
        return translatable(key, fallback, Collections.emptyList(), style);
    }

    @NotNull
    static NightTranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull List<NightTranslationArgument> args, @NotNull NightStyle style) {
        return NightTranslatableComponent.create(key, fallback, args, style);
    }



    @NotNull<T> T adapt(@NotNull TextComponentAdapter<T> adapter);

    @NotNull String toJson();

    @NotNull String toLegacy();

    void send(@NotNull CommandSender sender);

    void sendActionBar(@NotNull Player player);

    boolean isEmpty();

    boolean hasStyling();

    @NotNull NightComponent appendNewline();

    @NotNull NightComponent appendSpace();

    @NotNull NightComponent append(@NotNull NightComponent component);

    @NotNull NightComponent append(@NotNull NightComponent... components);

    @NotNull NightComponent append(@NotNull List<? extends NightComponent> components);

    @NotNull List<NightComponent> children();

    @NotNull NightComponent children(@NotNull List<? extends NightComponent> children);

    @NotNull NightStyle style();

    @NotNull NightComponent style(@NotNull NightStyle style);

    @Nullable NightKey font();

    @NotNull NightComponent font(@Nullable NightKey key);

    @Nullable Color color();

    @Nullable Color shadowColor();

    @NotNull NightComponent color(@Nullable Color color);

    @NotNull NightComponent shadowColor(@Nullable Color color);

    boolean hasDecoration(@NotNull NightTextDecoration decoration);

    @NotNull NightComponent decorate(@NotNull NightTextDecoration decoration);

    @NotNull NightTextDecoration.State decoration(@NotNull NightTextDecoration decoration);

    @NotNull NightComponent decoration(@NotNull NightTextDecoration decoration, boolean flag);

    @NotNull NightComponent decoration(@NotNull NightTextDecoration decoration, @NotNull NightTextDecoration.State state);

    @NotNull Map<NightTextDecoration, NightTextDecoration.State> decorations();

    @Nullable NightClickEvent clickEvent();

    @NotNull NightComponent clickEvent(@Nullable NightClickEvent event);

    @Nullable NightHoverEvent<?> hoverEvent();

    @NotNull NightComponent hoverEvent(@Nullable NightHoverEvent<?> hoverEvent);

    @Nullable String insertion();

    @NotNull NightComponent insertion(@Nullable String insertion);
}
