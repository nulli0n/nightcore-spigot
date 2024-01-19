package su.nightexpress.nightcore.util.text.tag.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.decoration.DecoratorParser;
import su.nightexpress.nightcore.util.text.decoration.ParsedDecorator;

public abstract class ContentTag extends DynamicTag implements DecoratorParser {

    public ContentTag(@NotNull String name) {
        super(name);
    }

    @Override
    @Nullable
    public final ParsedDecorator parse(@NotNull String str) {
        int tagLength = this.getName().length();
        String sub = str.substring(tagLength);

        if (sub.charAt(0) == ':') {
            sub = sub.substring(1);
            tagLength += 1;
        }

        ParsedDecorator decorator = this.onParse(sub);
        if (decorator != null) {
            decorator.setLength(decorator.getLength() + tagLength + 2); // 2 for quotes
        }
        return decorator;
    }

    @Nullable
    protected abstract ParsedDecorator onParse(@NotNull String sub);
}
