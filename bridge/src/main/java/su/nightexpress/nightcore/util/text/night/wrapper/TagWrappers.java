package su.nightexpress.nightcore.util.text.night.wrapper;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;
import su.nightexpress.nightcore.bridge.text.event.NightHoverEvent;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.tag.TagShortNames;

import java.util.function.BiFunction;
import java.util.function.Function;

public class TagWrappers {

    public static final SimpleTagWrapper BOLD          = TagWrapper.simple(TagShortNames.BOLD);
    public static final SimpleTagWrapper ITALIC        = TagWrapper.simple(TagShortNames.ITALIC);
    public static final SimpleTagWrapper OBFUSCATED    = TagWrapper.simple(TagShortNames.OBFUSCATED);
    public static final SimpleTagWrapper STRIKETHROUGH = TagWrapper.simple(TagShortNames.STRIKETHROUGH);
    public static final SimpleTagWrapper UNDERLINED    = TagWrapper.simple(TagShortNames.UNDERLINED);

    public static final OneArgument<SimpleTagWrapper>   COLOR        = hex -> TagWrapper.withArguments(TagShortNames.COLOR, hex);
    public static final OneArgument<SimpleTagWrapper>   SHADOW       = hex -> TagWrapper.withArguments(TagShortNames.SHADOW_COLOR, hex);
    public static final OneArgument<SimpleTagWrapper>   SHADOW_1_0       = hex -> TagWrapper.withArguments(TagShortNames.SHADOW_COLOR, hex, String.valueOf(1f));
    public static final TwoArgument<SimpleTagWrapper>   SHADOW_ALPHA = (hex, alpha) -> TagWrapper.withArguments(TagShortNames.SHADOW_COLOR, hex, alpha);
    public static final TwoArgument<SimpleTagWrapper>   GRADIENT     = (first, second) -> TagWrapper.withArguments(TagShortNames.GRADIENT, first, second);
    public static final ThreeArgument<SimpleTagWrapper> GRADIENT_3   = (first, second, third) -> TagWrapper.withArguments(TagShortNames.GRADIENT, first, second, third);

    public static final String BR      = TagWrapper.simple(TagShortNames.BR).opening();
    public static final String NEWLINE = TagWrapper.simple(TagShortNames.NEWLINE).opening();

    public static final OneArgument<SimpleTagWrapper> FONT      = font -> TagWrapper.withArguments(TagShortNames.FONT, font);
    public static final Function<String, String>      KEY       = key -> TagWrapper.withArguments(TagShortNames.KEYBIND, key).opening();
    public static final OneArgument<SimpleTagWrapper> INSERTION = text -> TagWrapper.withArguments(TagShortNames.INSERTION, text);

    public static final BiFunction<String, String, String> SPRITE          = (atlas, sprite) -> TagWrapper.withArguments(TagShortNames.SPRITE, ParserUtils.quoted(atlas), ParserUtils.quoted(sprite)).opening();
    @Deprecated
    public static final Function<String, String>           SPRITE_NO_ATLAS = sprite -> TagWrapper.withArguments(TagShortNames.SPRITE, ParserUtils.quoted(sprite)).opening();
    public static final Function<String, String>           SPRITE_BLOCKS   = sprite -> TagWrapper.withArguments(TagShortNames.SPRITE, ParserUtils.quoted("blocks"), ParserUtils.quoted(sprite)).opening();
    public static final Function<String, String>           SPRITE_ITEMS    = sprite -> TagWrapper.withArguments(TagShortNames.SPRITE, ParserUtils.quoted("items"), ParserUtils.quoted(sprite)).opening();
    public static final Function<String, String>           SPRITE_GUI    = sprite -> TagWrapper.withArguments(TagShortNames.SPRITE, ParserUtils.quoted("gui"), ParserUtils.quoted(sprite)).opening();

    public static final Function<Material, String> SPRITE_BLOCK = blockType -> SPRITE_BLOCKS.apply("block/" + BukkitThing.getValue(blockType));
    public static final Function<Material, String> SPRITE_ITEM  = itemType -> SPRITE_ITEMS.apply("item/" + BukkitThing.getValue(itemType));

    public static final BiFunction<String, Boolean, String> HEAD     = (data, hat) -> TagWrapper.withArguments(TagShortNames.HEAD, ParserUtils.quoted(data), String.valueOf(hat)).opening();
    public static final Function<String, String>            HEAD_HAT = data -> TagWrapper.withArguments(TagShortNames.HEAD, ParserUtils.quoted(data)).opening();

    public static final Function<String, String>           LANG    = key -> TagWrapper.withArguments(TagShortNames.LANG, ParserUtils.quoted(key)).opening();
    public static final BiFunction<String, String, String> LANG_OR = (key, fallback) -> TagWrapper.withArguments(TagShortNames.LANG_OR, ParserUtils.quoted(key), ParserUtils.quoted(fallback)).opening();

    public static final OneArgument<SimpleTagWrapper> SHOW_TEXT = text -> TagWrapper.withArguments(TagShortNames.HOVER, NightHoverEvent.Action.SHOW_TEXT.name(), ParserUtils.quoted(ParserUtils.escapeQuotes(text)));
    public static final OneArgument<SimpleTagWrapper> SHOW_ITEM = text -> TagWrapper.withArguments(TagShortNames.HOVER, NightHoverEvent.Action.SHOW_ITEM.name(), ParserUtils.quoted(ParserUtils.escapeQuotes(text)));

    public static final OneArgument<SimpleTagWrapper> SUGGEST_COMMAND   = command -> TagWrapper.withArguments(TagShortNames.CLICK, NightClickEvent.Action.SUGGEST_COMMAND.toString(), ParserUtils.quoted(command));
    public static final OneArgument<SimpleTagWrapper> RUN_COMMAND       = command -> TagWrapper.withArguments(TagShortNames.CLICK, NightClickEvent.Action.RUN_COMMAND.toString(), ParserUtils.quoted(command));
    public static final OneArgument<SimpleTagWrapper> OPEN_FILE         = filePath -> TagWrapper.withArguments(TagShortNames.CLICK, NightClickEvent.Action.OPEN_FILE.toString(), ParserUtils.quoted(filePath));
    public static final OneArgument<SimpleTagWrapper> OPEN_URL          = url -> TagWrapper.withArguments(TagShortNames.CLICK, NightClickEvent.Action.OPEN_URL.toString(), ParserUtils.quoted(url));
    public static final OneArgument<SimpleTagWrapper> CHANGE_PAGE       = page -> TagWrapper.withArguments(TagShortNames.CLICK, NightClickEvent.Action.CHANGE_PAGE.toString(), ParserUtils.quoted(page));
    public static final OneArgument<SimpleTagWrapper> COPY_TO_CLIPBOARD = text -> TagWrapper.withArguments(TagShortNames.CLICK, NightClickEvent.Action.COPY_TO_CLIPBOARD.toString(), ParserUtils.quoted(text));

    public static final SimpleTagWrapper RESET = TagWrapper.simple(TagShortNames.RESET);

    public static final SimpleTagWrapper BLACK = TagWrapper.simple(TagShortNames.BLACK);
    public static final SimpleTagWrapper WHITE = TagWrapper.simple(TagShortNames.WHITE);

    public static final SimpleTagWrapper GRAY      = TagWrapper.simple(TagShortNames.GRAY);
    public static final SimpleTagWrapper SOFT_GRAY = TagWrapper.simple(TagShortNames.SOFT_GRAY);
    public static final SimpleTagWrapper DARK_GRAY = TagWrapper.simple(TagShortNames.DARK_GRAY);

    public static final SimpleTagWrapper RED      = TagWrapper.simple(TagShortNames.RED);
    public static final SimpleTagWrapper SOFT_RED = TagWrapper.simple(TagShortNames.SOFT_RED);
    public static final SimpleTagWrapper DARK_RED = TagWrapper.simple(TagShortNames.DARK_RED);

    public static final SimpleTagWrapper GREEN      = TagWrapper.simple(TagShortNames.GREEN);
    public static final SimpleTagWrapper SOFT_GREEN = TagWrapper.simple(TagShortNames.SOFT_GREEN);
    public static final SimpleTagWrapper DARK_GREEN = TagWrapper.simple(TagShortNames.DARK_GREEN);

    public static final SimpleTagWrapper BLUE      = TagWrapper.simple(TagShortNames.BLUE);
    public static final SimpleTagWrapper SOFT_BLUE = TagWrapper.simple(TagShortNames.SOFT_BLUE);
    public static final SimpleTagWrapper DARK_BLUE = TagWrapper.simple(TagShortNames.DARK_BLUE);

    public static final SimpleTagWrapper YELLOW      = TagWrapper.simple(TagShortNames.YELLOW);
    public static final SimpleTagWrapper SOFT_YELLOW = TagWrapper.simple(TagShortNames.SOFT_YELLOW);
    public static final SimpleTagWrapper DARK_YELLOW = TagWrapper.simple(TagShortNames.DARK_YELLOW);

    public static final SimpleTagWrapper ORANGE      = TagWrapper.simple(TagShortNames.ORANGE);
    public static final SimpleTagWrapper SOFT_ORANGE = TagWrapper.simple(TagShortNames.SOFT_ORANGE);
    public static final SimpleTagWrapper GOLD        = TagWrapper.simple(TagShortNames.GOLD);

    public static final SimpleTagWrapper AQUA      = TagWrapper.simple(TagShortNames.AQUA);
    public static final SimpleTagWrapper SOFT_AQUA = TagWrapper.simple(TagShortNames.SOFT_AQUA);
    public static final SimpleTagWrapper DARK_AQUA = TagWrapper.simple(TagShortNames.DARK_AQUA);

    public static final SimpleTagWrapper PURPLE       = TagWrapper.simple(TagShortNames.PURPLE);
    public static final SimpleTagWrapper SOFT_PURPLE  = TagWrapper.simple(TagShortNames.SOFT_PURPLE);
    public static final SimpleTagWrapper LIGHT_PURPLE = TagWrapper.simple(TagShortNames.LIGHT_PURPLE);
    public static final SimpleTagWrapper DARK_PURPLE  = TagWrapper.simple(TagShortNames.DARK_PURPLE);

    public static final SimpleTagWrapper PINK      = TagWrapper.simple(TagShortNames.PINK);
    public static final SimpleTagWrapper SOFT_PINK = TagWrapper.simple(TagShortNames.SOFT_PINK);


    public interface OneArgument<T extends TagWrapper> {

        @NotNull T with(@NotNull String arg);
    }

    public interface TwoArgument<T extends TagWrapper> {

        @NotNull T with(@NotNull String first, @NotNull String second);
    }

    public interface ThreeArgument<T extends TagWrapper> {

        @NotNull T with(@NotNull String first, @NotNull String second, @NotNull String third);
    }
}
