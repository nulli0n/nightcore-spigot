package su.nightexpress.nightcore.util.text.tag;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.decorator.ColorDecorator;
import su.nightexpress.nightcore.util.text.tag.impl.FontStyleTag;
import su.nightexpress.nightcore.util.text.tag.impl.GradientTag;
import su.nightexpress.nightcore.util.text.tag.impl.HexColorTag;
import su.nightexpress.nightcore.util.text.tag.impl.ShortHexColorTag;

import java.util.function.Predicate;

public class TagPool {

    public static final TagPool ALL  = new TagPool(tag -> true);
    public static final TagPool NONE = new TagPool(tag -> false);

    public static final TagPool ALL_COLORS = new TagPool(
        tag -> tag instanceof ColorDecorator ||
            tag instanceof GradientTag ||
            tag instanceof HexColorTag ||
            tag instanceof ShortHexColorTag
    );

    public static final TagPool ALL_COLORS_AND_STYLES = new TagPool(
        tag -> tag instanceof ColorDecorator ||
            tag instanceof GradientTag ||
            tag instanceof HexColorTag ||
            tag instanceof ShortHexColorTag ||
            tag instanceof FontStyleTag
    );

    public static final TagPool BASE_COLORS = new TagPool(
        tag -> tag instanceof ColorDecorator ||
            tag instanceof HexColorTag ||
            tag instanceof ShortHexColorTag
    );

    public static final TagPool BASE_COLORS_AND_STYLES = new TagPool(
        tag -> tag instanceof ColorDecorator ||
            tag instanceof HexColorTag ||
            tag instanceof ShortHexColorTag ||
            tag instanceof FontStyleTag
    );

    private Predicate<Tag> predicate;

    public TagPool(@NotNull Predicate<Tag> predicate) {
        this.predicate = predicate;
    }

    @NotNull
    public TagPool and(@NotNull Predicate<Tag> more) {
        this.predicate = this.predicate.and(more);
        return this;
    }

    @NotNull
    public TagPool or(@NotNull Predicate<Tag> more) {
        this.predicate = this.predicate.or(more);
        return this;
    }

    public boolean isGoodTag(@NotNull Tag tag) {
        return this.predicate.test(tag);
    }
}
