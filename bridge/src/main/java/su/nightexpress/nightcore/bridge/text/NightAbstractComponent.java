package su.nightexpress.nightcore.bridge.text;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

    protected NightAbstractComponent(@NonNull List<? extends NightComponent> children, @NonNull NightStyle style) {
        this.children = new ArrayList<>(children);
        this.style = style;
    }

    @NonNull
    public <T> T adapt(@NonNull TextComponentAdapter<T> adapter) {
        return adapter.adaptComponent(this);
    }

    @Override
    @NonNull
    public NightComponent append(@NonNull List<? extends NightComponent> components) {
        var children = new ArrayList<>(this.children());
        children.addAll(components);

        return this.children(children);
    }

    @NonNull
    public final List<NightComponent> children() {
        return this.children;
    }

    @NonNull
    public final NightStyle style() {
        return this.style;
    }

    @NonNull
    public NightComponent appendNewline() {
        return this.append(NightComponent.newline());
    }

    @NonNull
    public NightComponent appendSpace() {
        return this.append(NightComponent.space());
    }

    @Override
    @NonNull
    public NightComponent append(@NonNull NightComponent... components) {
        return this.append(Lists.newList(components));
    }

    @Override
    @NonNull
    public NightComponent append(@NonNull NightComponent component) {
        return this.append(new NightComponent[]{component});
    }

    @Nullable
    public NightKey font() {
        return this.style().font();
    }

    @NonNull
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

    @NonNull
    public NightComponent color(@Nullable Color color) {
        return this.style(this.style().color(color));
    }

    @NonNull
    public NightComponent shadowColor(@Nullable Color color) {
        return this.style(this.style().shadowColor(color));
    }

    public boolean hasDecoration(@NonNull NightTextDecoration decoration) {
        return this.decoration(decoration) == NightTextDecoration.State.TRUE;
    }

    @NonNull
    public NightComponent decorate(@NonNull NightTextDecoration decoration) {
        return this.style(this.style().decoration(decoration, NightTextDecoration.State.TRUE));
    }

    public NightTextDecoration.@NonNull State decoration(@NonNull NightTextDecoration decoration) {
        return this.style().decoration(decoration);
    }

    @NonNull
    public NightComponent decoration(@NonNull NightTextDecoration decoration, boolean flag) {
        return this.style(this.style().decoration(decoration, NightTextDecoration.State.byBoolean(flag)));
    }

    @NonNull
    public NightComponent decoration(@NonNull NightTextDecoration decoration,
                                     NightTextDecoration.@NonNull State state) {
        return this.style(this.style().decoration(decoration, state));
    }

    @NonNull
    public Map<NightTextDecoration, NightTextDecoration.State> decorations() {
        return this.style().decorations();
    }

    @Nullable
    public NightClickEvent clickEvent() {
        return this.style().clickEvent();
    }

    @NonNull
    public NightComponent clickEvent(@Nullable NightClickEvent event) {
        return this.style(this.style().clickEvent(event));
    }

    @Nullable
    public NightHoverEvent<?> hoverEvent() {
        return this.style().hoverEvent();
    }

    @NonNull
    public NightComponent hoverEvent(@Nullable NightHoverEvent<?> hoverEvent) {
        return this.style(this.style().hoverEvent(hoverEvent));
    }

    @Nullable
    public String insertion() {
        return this.style().insertion();
    }

    @NonNull
    public NightComponent insertion(@Nullable String insertion) {
        return this.style(this.style().insertion(insertion));
    }

    public boolean hasStyling() {
        return !this.style().isEmpty();
    }

    @Override
    @NonNull
    public String toJson() {
        return Software.instance().getTextComponentAdapter().toJson(this);
    }

    @Override
    @NonNull
    public String toLegacy() {
        return Software.instance().getTextComponentAdapter().toLegacy(this);
    }

    @Override
    public void send(@NonNull CommandSender sender) {
        Software.instance().getTextComponentAdapter().send(sender, this);
    }

    @Override
    public void sendActionBar(@NonNull Player player) {
        Software.instance().getTextComponentAdapter().sendActionBar(player, this);
    }

    @Override
    public boolean isEmpty() {
        return this instanceof NightTextComponent textComponent && textComponent.content().isEmpty() && textComponent
            .children().isEmpty();
    }
}
