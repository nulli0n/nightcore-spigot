package su.nightexpress.nightcore.util.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.text.tag.decorator.ColorDecorator;
import su.nightexpress.nightcore.util.text.tag.decorator.Decorator;
import su.nightexpress.nightcore.util.text.tag.decorator.GradientColorDecorator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TextGroup implements ComponentBuildable {

    private final String                   name;
    private final List<ComponentBuildable> childrens;
    private final Set<Decorator>           decorators;

    private ColorDecorator colorDecorator;
    private TextGroup      parent;

    public TextGroup(@NotNull String name) {
        this.name = name;
        this.childrens = new ArrayList<>();
        this.decorators = new LinkedHashSet<>();
    }

    @NotNull
    public TextGroup createChildren(@NotNull String name) {
        TextGroup group = new TextGroup(name);
        group.parent = this;
        group.colorDecorator = this.colorDecorator;
        group.decorators.addAll(this.decorators);
        this.childrens.add(group);
        return group;
    }

    @NotNull
    public TextNode createNode() {
        TextNode node = new TextNode(this);
        this.childrens.add(node);
        return node;
    }

    public void addDecorator(@NotNull Decorator decorator) {
        if (decorator instanceof ColorDecorator color) {
            this.colorDecorator = color;
        }
        else {
            this.decorators.add(decorator);
        }
    }

    public void clearDecorators() {
        this.colorDecorator = null;
        this.decorators.clear();
    }

    @Override
    @NotNull
    public BaseComponent toComponent() {
        ComponentBuilder builder = new ComponentBuilder();

        // Gradient colors must be predefined based on text length.
        // Text length is calculated from group's childrens that are TextNodes.
        if (this.colorDecorator instanceof GradientColorDecorator gradientDecorator && !gradientDecorator.isCreated()) {
            AtomicInteger length = new AtomicInteger(0);
            this.countLength(this, length);

            gradientDecorator.createGradient(length.get()); // This will create and store Color[] array.
        }

        this.childrens.forEach(child -> {
            BaseComponent childComponent = child.toComponent();
            //if (!TextRoot.isEmpty(childComponent)) {
                builder.append(childComponent);
            //}
        });

        return this.build(builder);
    }

    private BaseComponent build(@NotNull ComponentBuilder builder) {
        if (Version.isAtLeast(Version.MC_1_20_6)) {
            return builder.build();
        }

        TextComponent base = new TextComponent();
        if (!builder.getParts().isEmpty()) {
            List<BaseComponent> cloned = new ArrayList<>(builder.getParts());
            cloned.replaceAll(BaseComponent::duplicate);
            base.setExtra(cloned);
        }
        return base;
    }

    private void countLength(@NotNull TextGroup parent, @NotNull AtomicInteger length) {
        for (ComponentBuildable buildable : parent.getChildrens()) {
            if (buildable instanceof TextNode textNode) {
                length.addAndGet(textNode.textLength());
            }

            if (buildable instanceof TextGroup textGroup && textGroup.getColor() == this.colorDecorator) {
                this.countLength(textGroup, length);
            }
        }
    }

    @NotNull
    public String getName() {
        return name;
    }

    @Nullable
    public TextGroup getParent() {
        return parent;
    }

    @Nullable
    public ColorDecorator getColor() {
        return colorDecorator;
    }

    @NotNull
    public Set<Decorator> getDecorators() {
        return decorators;
    }

    @NotNull
    public List<ComponentBuildable> getChildrens() {
        return childrens;
    }
}
