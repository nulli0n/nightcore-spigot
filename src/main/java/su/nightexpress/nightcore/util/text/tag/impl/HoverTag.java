package su.nightexpress.nightcore.util.text.tag.impl;

import net.md_5.bungee.api.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.text.decoration.Decorator;
import su.nightexpress.nightcore.util.text.decoration.ParsedDecorator;
import su.nightexpress.nightcore.util.text.decoration.ShowItemDecorator;
import su.nightexpress.nightcore.util.text.decoration.ShowTextDecorator;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;

public class HoverTag extends ContentTag {

    public static final String NAME = "hover";

    public HoverTag() {
        super(NAME);
    }

    @Override
    public int getWeight() {
        return 50;
    }

    /*@Override
    public boolean isDynamicSize() {
        return true;
    }*/

    @NotNull
    public String enclose(@NotNull String text, @NotNull String hint) {
        return this.enclose(HoverEvent.Action.SHOW_TEXT, text, hint);
    }

    @NotNull
    public String enclose(@NotNull HoverEvent.Action action, @NotNull String text, @NotNull String hint) {
        return this.enclose(action.name().toLowerCase(), hint, text);
    }

    @Override
    @Nullable
    public ParsedDecorator onParse(@NotNull String sub) {
        HoverEvent.Action action = null;
        for (HoverEvent.Action global : HoverEvent.Action.values()) {
            if (sub.startsWith(global.name().toLowerCase())) {
                action = global;
                break;
            }
        }
        if (action == null) return null;

        int prefixSize = action.name().toLowerCase().length() + 1; // 1 for ':', like "show_text:"
        sub = sub.substring(prefixSize);

        String content = StringUtil.parseQuotedContent(sub);
        if (content == null) return null;

        int length = prefixSize + content.length();// + 2; // 2 for quotes

        Decorator decorator;
        if (action == HoverEvent.Action.SHOW_TEXT) {
            decorator = new ShowTextDecorator(content);
        }
        else if (action == HoverEvent.Action.SHOW_ITEM) {
            /*ItemStack item;

            Material material = Material.getMaterial(content.toUpperCase());
            if (material != null) {
                item = new ItemStack(material);
            }
            else item = ItemUtil.decompress(content);*/

            decorator = new ShowItemDecorator(content);
        }
        else return null;

        return new ParsedDecorator(decorator, length);
    }
}
