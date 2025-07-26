package su.nightexpress.nightcore.bridge.text;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.text.adapter.TextComponentAdapter;
import su.nightexpress.nightcore.bridge.text.event.NightHoverEvent;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;
import su.nightexpress.nightcore.bridge.text.impl.NightTextComponent;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class NightAbstractComponent implements NightComponent {

    protected final List<NightComponent> children;

    protected final NightStyle style;

    protected NightAbstractComponent(@NotNull List<? extends NightComponent> children, @NotNull NightStyle style) {
        this.children = new ArrayList<>(children);
        this.style = style;
    }

    @NotNull
    public <T> T adapt(@NotNull TextComponentAdapter<T> adapter) {
        return adapter.adaptComponent(this);
    }

    @Override
    @NotNull
    public NightComponent append(@NotNull List<? extends NightComponent> components) {
        var children = new ArrayList<>(this.children());
        children.addAll(components);

        return this.children(children);
    }

    @NotNull
    public final List<NightComponent> children() {
        return this.children;
    }

    @NotNull
    public final NightStyle style() {
        return this.style;
    }

    @NotNull
    public NightComponent appendNewline() {
        return this.append(NightComponent.newline());
    }

    @NotNull
    public NightComponent appendSpace() {
        return this.append(NightComponent.space());
    }

    @Override
    @NotNull
    public NightComponent append(@NotNull NightComponent... components) {
        return this.append(Lists.newList(components));
    }

    @Override
    @NotNull
    public NightComponent append(@NotNull NightComponent component) {
        return this.append(new NightComponent[]{component});
    }

    @Nullable
    public NightKey font() {
        return this.style().font();
    }

    @NotNull
    public NightComponent font(@Nullable NightKey key) {
        return this.style(this.style().font(key));
    }

    @Nullable
    public Color color() {
        return this.style().color();
    }

    @Nullable
    public Color shadowColor() {
        return this.style().shadowColor();
    }

    @NotNull
    public NightComponent color(@Nullable Color color) {
        return this.style(this.style().color(color));
    }

    @NotNull
    public NightComponent shadowColor(@Nullable Color color) {
        return this.style(this.style().shadowColor(color));
    }
    
    public boolean hasDecoration(@NotNull NightTextDecoration decoration) {
        return this.decoration(decoration) == NightTextDecoration.State.TRUE;
    }

    @NotNull
    public NightComponent decorate(@NotNull NightTextDecoration decoration) {
        return this.style(this.style().decoration(decoration, NightTextDecoration.State.TRUE));
    }

    @NotNull
    public NightTextDecoration.State decoration(@NotNull NightTextDecoration decoration) {
        return this.style().decoration(decoration);
    }

    @NotNull
    public NightComponent decoration(@NotNull NightTextDecoration decoration, boolean flag) {
        return this.style(this.style().decoration(decoration, NightTextDecoration.State.byBoolean(flag)));
    }

    @NotNull
    public NightComponent decoration(@NotNull NightTextDecoration decoration, @NotNull NightTextDecoration.State state) {
        return this.style(this.style().decoration(decoration, state));
    }

    @NotNull
    public Map<NightTextDecoration, NightTextDecoration.State> decorations() {
        return this.style().decorations();
    }

    @Nullable
    public NightClickEvent clickEvent() {
        return this.style().clickEvent();
    }

    @NotNull
    public NightComponent clickEvent(@Nullable NightClickEvent event) {
        return this.style(this.style().clickEvent(event));
    }

    @Nullable
    public NightHoverEvent<?> hoverEvent() {
        return this.style().hoverEvent();
    }

    @NotNull
    public NightComponent hoverEvent(@Nullable NightHoverEvent<?> hoverEvent) {
        return this.style(this.style().hoverEvent(hoverEvent));
    }

    @Nullable
    public String insertion() {
        return this.style().insertion();
    }

    @NotNull
    public NightComponent insertion(@Nullable String insertion) {
        return this.style(this.style().insertion(insertion));
    }
    
    public boolean hasStyling() {
        return !this.style().isEmpty();
    }

    @Override
    @NotNull
    public String toJson() {
        return Software.instance().getTextComponentAdapter().toJson(this);
    }

    @Override
    @NotNull
    public String toLegacy() {
        return Software.instance().getTextComponentAdapter().toLegacy(this);
    }

    @Override
    public void send(@NotNull CommandSender sender) {
        Software.instance().getTextComponentAdapter().send(sender, this);
    }

    @Override
    public void sendActionBar(@NotNull Player player) {
        Software.instance().getTextComponentAdapter().sendActionBar(player, this);
    }

    @Override
    public boolean isEmpty() {
        return this instanceof NightTextComponent textComponent && textComponent.content().isEmpty() && textComponent.children().isEmpty();
    }
}
