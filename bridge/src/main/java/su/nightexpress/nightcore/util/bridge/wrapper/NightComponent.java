package su.nightexpress.nightcore.util.bridge.wrapper;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.text.NightStyle;
import su.nightexpress.nightcore.bridge.text.NightTextDecoration;
import su.nightexpress.nightcore.bridge.text.adapter.TextComponentAdapter;
import su.nightexpress.nightcore.bridge.text.contents.NightObjectContents;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;
import su.nightexpress.nightcore.bridge.text.event.NightHoverEvent;
import su.nightexpress.nightcore.bridge.text.impl.NightKeybindComponent;
import su.nightexpress.nightcore.bridge.text.impl.NightObjectComponent;
import su.nightexpress.nightcore.bridge.text.impl.NightTextComponent;
import su.nightexpress.nightcore.bridge.text.impl.NightTranslatableComponent;
import su.nightexpress.nightcore.bridge.text.impl.NightTranslationArgument;

public interface NightComponent {

    @NonNull
    static NightTextComponent empty() {
        return NightTextComponent.EMPTY;
    }

    @NonNull
    static NightTextComponent newline() {
        return NightTextComponent.NEWLINE;
    }

    @NonNull
    static NightTextComponent space() {
        return NightTextComponent.SPACE;
    }

    @NonNull
    static NightKeybindComponent keybind(@NonNull String keybind) {
        return keybind(keybind, NightStyle.EMPTY);
    }

    @NonNull
    static NightKeybindComponent keybind(@NonNull String keybind, @NonNull NightStyle style) {
        return NightKeybindComponent.create(keybind, style);
    }

    @NonNull
    static NightObjectComponent object(@NonNull NightStyle style, @NonNull NightObjectContents contents) {
        return NightObjectComponent.create(style, contents);
    }

    @NonNull
    static NightTextComponent text(@NonNull String content, @NonNull NightStyle style) {
        return content.isEmpty() ? empty() : NightTextComponent.create(content, style);
    }

    @NonNull
    static NightTextComponent text(@NonNull String content) {
        return text(content, NightStyle.EMPTY);
    }

    @NonNull
    static NightTextComponent text(boolean value) {
        return text(String.valueOf(value));
    }

    @NonNull
    static NightTextComponent text(char value) {
        if (value == '\n') return newline();
        if (value == ' ') return space();
        return text(String.valueOf(value));
    }

    @NonNull
    static NightTextComponent text(double value) {
        return text(String.valueOf(value));
    }

    @NonNull
    static NightTextComponent text(float value) {
        return text(String.valueOf(value));
    }

    @NonNull
    static NightTextComponent text(int value) {
        return text(String.valueOf(value));
    }

    @NonNull
    static NightTextComponent text(long value) {
        return text(String.valueOf(value));
    }

    @NonNull
    static NightTranslatableComponent translatable(@NonNull String key) {
        return translatable(key, null);
    }

    @NonNull
    static NightTranslatableComponent translatable(@NonNull String key, @Nullable String fallback) {
        return translatable(key, fallback, NightStyle.EMPTY);
    }

    @NonNull
    static NightTranslatableComponent translatable(@NonNull String key, @Nullable String fallback,
                                                   @NonNull NightStyle style) {
        return translatable(key, fallback, Collections.emptyList(), style);
    }

    @NonNull
    static NightTranslatableComponent translatable(@NonNull String key, @Nullable String fallback,
                                                   @NonNull List<NightTranslationArgument> args,
                                                   @NonNull NightStyle style) {
        return NightTranslatableComponent.create(key, fallback, args, style);
    }


    @NonNull
    <T> T adapt(@NonNull TextComponentAdapter<T> adapter);

    @NonNull
    String toJson();

    @NonNull
    String toLegacy();

    void send(@NonNull CommandSender sender);

    void sendActionBar(@NonNull Player player);

    boolean isEmpty();

    boolean hasStyling();

    @NonNull
    NightComponent appendNewline();

    @NonNull
    NightComponent appendSpace();

    @NonNull
    NightComponent append(@NonNull NightComponent component);

    @NonNull
    NightComponent append(@NonNull NightComponent... components);

    @NonNull
    NightComponent append(@NonNull List<? extends NightComponent> components);

    @NonNull
    List<NightComponent> children();

    @NonNull
    NightComponent children(@NonNull List<? extends NightComponent> children);

    @NonNull
    NightStyle style();

    @NonNull
    NightComponent style(@NonNull NightStyle style);

    @Nullable
    NightKey font();

    @NonNull
    NightComponent font(@Nullable NightKey key);

    @Nullable
    Color color();

    @Nullable
    Color shadowColor();

    @NonNull
    NightComponent color(@Nullable Color color);

    @NonNull
    NightComponent shadowColor(@Nullable Color color);

    boolean hasDecoration(@NonNull NightTextDecoration decoration);

    @NonNull
    NightComponent decorate(@NonNull NightTextDecoration decoration);

    NightTextDecoration.@NonNull State decoration(@NonNull NightTextDecoration decoration);

    @NonNull
    NightComponent decoration(@NonNull NightTextDecoration decoration, boolean flag);

    @NonNull
    NightComponent decoration(@NonNull NightTextDecoration decoration, NightTextDecoration.@NonNull State state);

    @NonNull
    Map<NightTextDecoration, NightTextDecoration.State> decorations();

    @Nullable
    NightClickEvent clickEvent();

    @NonNull
    NightComponent clickEvent(@Nullable NightClickEvent event);

    @Nullable
    NightHoverEvent<?> hoverEvent();

    @NonNull
    NightComponent hoverEvent(@Nullable NightHoverEvent<?> hoverEvent);

    @Nullable
    String insertion();

    @NonNull
    NightComponent insertion(@Nullable String insertion);
}
