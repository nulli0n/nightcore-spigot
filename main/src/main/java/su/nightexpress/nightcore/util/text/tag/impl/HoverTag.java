package su.nightexpress.nightcore.util.text.tag.impl;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.ItemNbt;
import su.nightexpress.nightcore.util.bridge.wrapper.HoverEventType;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.decorator.Decorator;
import su.nightexpress.nightcore.util.text.tag.decorator.ShowItemDecorator;
import su.nightexpress.nightcore.util.text.tag.decorator.ShowTextDecorator;

public class HoverTag extends Tag implements ContentTag {

    public static final String NAME = "hover";

    public HoverTag() {
        super(NAME);
    }

    @NotNull
    @Deprecated
    public String enclose(@NotNull String text, @NotNull String hint) {
        return this.encloseHint(text, hint);
    }

    @NotNull
    @Deprecated
    public String enclose(@NotNull HoverEventType action, @NotNull String text, @NotNull String content) {
        return this.enclose(text, action, content);
    }

    @NotNull
    @Deprecated
    public String encloseHint(@NotNull String text, @NotNull String hint) {
        return this.wrap(text, HoverEventType.SHOW_TEXT, hint);
    }

    @NotNull
    @Deprecated
    public String enclose(@NotNull String text, @NotNull HoverEventType action, @NotNull String content) {
        //String data = action.name().toLowerCase() + TagUtils.SEMICOLON + TagUtils.quoted(content);
        return this.wrap(text, action, content);//TagUtils.wrapContent(this, text, data);
    }

    @NotNull
    public String wrapShowText(@NotNull String string, @NotNull String text) {
        return this.wrap(string, HoverEventType.SHOW_TEXT, text);
    }

    @NotNull
    public String wrapShowItem(@NotNull String string, @NotNull ItemStack itemStack) {
        return this.wrapShowItem(string, String.valueOf(ItemNbt.compress(itemStack)));
    }

    @NotNull
    public String wrapShowItem(@NotNull String string, @NotNull String compressed) {
        return this.wrap(string, HoverEventType.SHOW_ITEM, compressed);
    }

    @NotNull
    public String wrap(@NotNull String string, @NotNull HoverEventType type, @NotNull String content) {
        String data = type.name().toLowerCase() + TagUtils.SEMICOLON + TagUtils.quoted(content);
        return TagUtils.wrapContent(this, string, data);
    }

    @Override
    @Nullable
    public Decorator parse(@NotNull String tagContent) {
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

        String value = TagUtils.unquoted(tagContent);

        if (action == HoverEventType.SHOW_TEXT) {
            return new ShowTextDecorator(value);
        }
        else if (action == HoverEventType.SHOW_ITEM) {
            return new ShowItemDecorator(value);
        }
        return null;
    }
}
