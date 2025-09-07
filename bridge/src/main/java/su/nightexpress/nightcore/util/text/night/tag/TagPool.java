package su.nightexpress.nightcore.util.text.night.tag;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.night.tag.handler.*;

import java.util.function.Predicate;

public interface TagPool extends Predicate<TagHandler> {

    TagPool ALL  = tag -> true;
    TagPool NONE = tag -> false;

    TagPool BASE_COLORS     = tag -> tag instanceof ColorTagHandler || tag instanceof NamedColorTagHandler;
    TagPool ADVANCED_COLORS = tag -> tag instanceof GradientTagHandler || tag instanceof ShadowTagHandler;
    TagPool DECORATIONS     = tag -> tag instanceof DecorationTagHandler;

    TagPool NO_INVERTED_DECORATIONS = tag -> !(tag instanceof DecorationTagHandler handler) || !handler.isInverted();

    Predicate<TagHandler> ALL_COLORS                  = BASE_COLORS.and(ADVANCED_COLORS);
    Predicate<TagHandler> BASE_COLORS_AND_DECORATIONS = BASE_COLORS.and(DECORATIONS);
    Predicate<TagHandler> ALL_COLORS_AND_DECORATIONS  = ALL_COLORS.and(DECORATIONS);

    default boolean isGoodTag(@NotNull TagHandler handler) {
        return this.test(handler);
    }
}
