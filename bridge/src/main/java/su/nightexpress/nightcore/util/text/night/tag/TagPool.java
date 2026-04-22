package su.nightexpress.nightcore.util.text.night.tag;

import java.util.function.Predicate;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.text.night.tag.handler.ColorTagHandler;
import su.nightexpress.nightcore.util.text.night.tag.handler.DecorationTagHandler;
import su.nightexpress.nightcore.util.text.night.tag.handler.GradientTagHandler;
import su.nightexpress.nightcore.util.text.night.tag.handler.NamedColorTagHandler;
import su.nightexpress.nightcore.util.text.night.tag.handler.ShadowTagHandler;

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

    default boolean isGoodTag(@NonNull TagHandler handler) {
        return this.test(handler);
    }
}
