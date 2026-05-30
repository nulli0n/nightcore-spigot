package su.nightexpress.nightcore.util.text.tag.impl;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.util.ItemNbt;
import su.nightexpress.nightcore.util.bridge.wrapper.HoverEventType;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.decorator.Decorator;
import su.nightexpress.nightcore.util.text.tag.decorator.ShowItemDecorator;
import su.nightexpress.nightcore.util.text.tag.decorator.ShowTextDecorator;

@Deprecated
public class HoverTag extends Tag implements ContentTag {

    public static final String NAME = "hover";

    public HoverTag() {
        super(NAME);
    }

    @NonNull
    @Deprecated
    public String enclose(@NonNull String text, @NonNull String hint) {
        return this.encloseHint(text, hint);
    }

    @NonNull
    @Deprecated
    public String enclose(@NonNull HoverEventType action, @NonNull String text, @NonNull String content) {
        return this.enclose(text, action, content);
    }

    @NonNull
    @Deprecated
    public String encloseHint(@NonNull String text, @NonNull String hint) {
        return this.wrap(text, HoverEventType.SHOW_TEXT, hint);
    }

    @NonNull
    @Deprecated
    public String enclose(@NonNull String text, @NonNull HoverEventType action, @NonNull String content) {
        //String data = action.name().toLowerCase() + TagUtils.SEMICOLON + TagUtils.quoted(content);
        return this.wrap(text, action, content);//TagUtils.wrapContent(this, text, data);
    }

    @NonNull
    public String wrapShowText(@NonNull String string, @NonNull String text) {
        return this.wrap(string, HoverEventType.SHOW_TEXT, text);
    }

    @NonNull
    public String wrapShowItem(@NonNull String string, @NonNull ItemStack itemStack) {
        return this.wrapShowItem(string, String.valueOf(ItemNbt.compress(itemStack)));
    }

    @NonNull
    public String wrapShowItem(@NonNull String string, @NonNull String compressed) {
        return this.wrap(string, HoverEventType.SHOW_ITEM, compressed);
    }

    @NonNull
    public String wrap(@NonNull String string, @NonNull HoverEventType type, @NonNull String content) {
        String data = type.name().toLowerCase() + ParserUtils.DELIMITER + ParserUtils.quoted(content);
        return TagUtils.wrapContent(this, string, data);
    }

    @Override
    @Nullable
    public Decorator parse(@NonNull String tagContent) {
        HoverEventType action = null;
        for (HoverEventType global : HoverEventType.values()) {
            if (tagContent.startsWith(global.name().toLowerCase())) {
                action = global;
                break;
            }
        }
        if (action == null) return null;

        int prefixSize = action.name().toLowerCase().length() + 1; // 1 for ':', like "show_text:"
        tagContent = tagContent.substring(prefixSize);

        String value = ParserUtils.unquoted(tagContent);

        if (action == HoverEventType.SHOW_TEXT) {
            return new ShowTextDecorator(value);
        }
        else if (action == HoverEventType.SHOW_ITEM) {
            return new ShowItemDecorator(value);
        }
        return null;
    }
}
