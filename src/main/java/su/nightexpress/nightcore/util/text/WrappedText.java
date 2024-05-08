package su.nightexpress.nightcore.util.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.decoration.*;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.Tags;
import su.nightexpress.nightcore.util.text.tag.impl.ColorTag;
import su.nightexpress.nightcore.util.text.tag.impl.FontStyleTag;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrappedText {

    private final StringBuilder          textBuilder;
    private final List<Tag>              tags;
    private final Map<String, Decorator> decoratorMap;

    public WrappedText() {
        this.textBuilder = new StringBuilder();
        this.tags = new ArrayList<>();
        this.decoratorMap = new HashMap<>();
    }

    public WrappedText(@NotNull String text) {
        this();
        this.setText(text);
    }

    @NotNull
    public WrappedText nested() {
        WrappedText nested = new WrappedText();
        nested.getTags().addAll(this.getTags());
        nested.getDecoratorMap().putAll(this.getDecoratorMap());
        return nested;
    }

    @Nullable
    public Decorator getDecorator(@NotNull Tag tag) {
        return this.getDecorator(tag.getName());
    }

    @Nullable
    public Decorator getDecorator(@NotNull String tagName) {
        return this.getDecoratorMap().get(tagName.toLowerCase());
    }

    @NotNull
    public BaseComponent toComponent() {
        TextComponent component = new TextComponent(this.getText());

        for (Tag tag : this.getTags()) {
            Decorator decorator = this.getDecorator(tag);
            if (decorator == null) continue;

            decorator.decorate(component);
        }

        return component;
    }

    public void addTag(@NotNull Tag tag, @Nullable Decorator decorator) {
        //this.getTags().removeIf(tag::conflictsWith);
        this.getTags().removeIf(other -> other.getName().equals(tag.getName()));
        this.getTags().add(tag);
        if (decorator != null) {
            this.decoratorMap.put(tag.getName(), decorator);
        }
    }

    public void removeLatestTag(@NotNull Tag tag) {
        for (int indexTag = this.getTags().size() - 1; indexTag > -1; indexTag--) {
            Tag has = this.getTags().get(indexTag);
            if (has.getName().equalsIgnoreCase(tag.getName())) {
                this.getTags().remove(indexTag);
                this.getDecoratorMap().remove(tag.getName());
                break ;
            }
        }
    }

    public void removeTag(@NotNull Tag tag) {
        this.getTags().remove(tag);
        this.getDecoratorMap().remove(tag.getName());
    }

    @NotNull
    public List<Tag> getTags() {
        return tags;
    }

    @NotNull
    public Map<String, Decorator> getDecoratorMap() {
        return decoratorMap;
    }

    @NotNull
    public StringBuilder getTextBuilder() {
        return textBuilder;
    }

    @NotNull
    public String getText() {
        return this.getTextBuilder().toString();
    }

    public void setText(@NotNull String text) {
        this.textBuilder.setLength(0);
        this.textBuilder.append(text);
    }

    @Override
    public String toString() {
        return "ParsedText{" +
            "tags=" + tags +
            ", text=" + textBuilder +
            '}';
    }

    @NotNull
    public static Builder builder(@NotNull String text) {
        return new Builder(new WrappedText(text));
    }

    public static class Builder {

        private final WrappedText text;

        public Builder(@NotNull WrappedText text) {
            this.text = text;
        }

        @NotNull
        public WrappedText getText() {
            return text;
        }

        @NotNull
        public Builder tag(@NotNull Tag tag) {
            return this.tag(tag, tag instanceof Decorator decorator ? decorator : null);
        }

        @NotNull
        public Builder tag(@NotNull Tag tag, @Nullable Decorator decorator) {
            this.getText().addTag(tag, decorator);
            return this;
        }

        public Builder color(@NotNull Color color) {
            return this.color(new ColorTag(color));
        }

        public Builder color(@NotNull ColorTag tag) {
            return this.tag(tag);
        }

        public Builder bold() {
            return style(Tags.BOLD);
        }

        public Builder style(@NotNull FontStyleTag tag) {
            return this.tag(tag);
        }

        public Builder gradient(@NotNull Color from, @NotNull Color to) {
            return this.tag(Tags.GRADIENT, new GradientDecorator(from, to));
        }

        public Builder showText(@NotNull String... text) {
            return this.tag(Tags.HOVER, new ShowTextDecorator(text));
        }

        public Builder showItem(@NotNull ItemStack itemStack) {
            return this.tag(Tags.HOVER, ShowItemDecorator.from(itemStack));
        }

        public Builder runCommand(@NotNull String value) {
            return this.clickEvent(ClickEvent.Action.RUN_COMMAND, value);
        }

        public Builder suggestCommand(@NotNull String value) {
            return this.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, value);
        }

        public Builder clickEvent(@NotNull ClickEvent.Action action, @NotNull String value) {
            return this.tag(Tags.CLICK, new ClickEventDecorator(action, value));
        }
    }
}
