package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public class InsertionTagHandler extends ClassicTagHandler {

    @Override
    protected void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        if (tagContent == null) return;

        String insetion = ParserUtils.unquoted(tagContent);
        group.setStyle(style -> style.insertion(insetion));
    }

    @Override
    protected void onHandleClose(@NotNull EntryGroup group) {

    }
}
