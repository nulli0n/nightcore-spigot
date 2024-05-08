package su.nightexpress.nightcore.util.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.tag.TagPool;

import java.util.function.UnaryOperator;

public class WrappedMessage {

    private final TagPool tagPool;

    private String          string;
    private BaseComponent[] components;

    public WrappedMessage(@NotNull String string, @NotNull TagPool tagPool) {
        this.tagPool = tagPool;
        this.string = string;
    }

    @NotNull
    public WrappedMessage copy() {
        WrappedMessage copy = new WrappedMessage(this.string, this.tagPool);
        if (this.components != null) {
            BaseComponent[] components = new BaseComponent[this.components.length];
            for (int index = 0; index < components.length; index++) {
                components[index] = this.components[index].duplicate();
            }
            copy.components = components;
        }
        return copy;
    }

    @NotNull
    public WrappedMessage compile() {
        this.parseIfAbsent();
        return this;
    }

    @NotNull
    public WrappedMessage recompile() {
        this.parse();
        return this;
    }

    @NotNull
    public String toLegacy() {
        return TextComponent.toLegacyText(this.parseIfAbsent());
    }

    @NotNull
    public String toJson() {
        return ComponentSerializer.toString(this.parseIfAbsent());
    }

    @NotNull
    public BaseComponent toComponent() {
        return TextComponent.fromArray(this.parseIfAbsent());
    }

    @NotNull
    public WrappedMessage replace(@NotNull String what, @NotNull Object object) {
        return this.replace(str -> str.replace(what, String.valueOf(object)));
        //return this.replace(what, String.valueOf(object));
    }

    @NotNull
    public WrappedMessage replace(@NotNull UnaryOperator<String> operator) {
        this.string = operator.apply(this.string);
        this.components = null;
        return this;


        /*if (this.components == null) {
            this.string = operator.apply(this.string);
            return this;
        }

        for (BaseComponent component : this.components) {
            if (component instanceof TextComponent textComponent) {
                textComponent.setText(operator.apply(textComponent.getText()));
            }
        }
        return this;*/
    }

    /*@NotNull
    public WrappedMessage replace(@NotNull String var, @NotNull String value) {
        if (this.components == null) {
            this.string = this.string.replace(var, value);
            return this;
        }

        WrappedMessage message = NightMessage.create(value);

        ComponentBuilder builder = new ComponentBuilder();
        for (BaseComponent component : this.components) {
            if (component instanceof TextComponent textComponent) {
                String text = textComponent.getText();
                String[] split = text.split(var);

                ComponentBuilder replacer = new ComponentBuilder();
                for (String part : split) {
                    if (!replacer.getParts().isEmpty()) {
                        replacer.append(message.parseIfAbsent());
                    }

                    TextComponent origin = new TextComponent(part);
                    origin.copyFormatting(textComponent);
                    replacer.append(origin);
                }
                builder.append(replacer.create());
            }
            else builder.append(component);
        }

        return this;
    }*/

    public void send(@NotNull CommandSender... senders) {
        this.parseIfAbsent();
        for (CommandSender sender : senders) {
            sender.spigot().sendMessage(this.components);
        }
    }

    public BaseComponent[] parseIfAbsent() {
        return this.components == null ? this.parse() : this.components;
    }

    public BaseComponent[] parse() {
        this.components = NightMessage.parse(this.string, this.tagPool);
        return this.components;
    }
}
