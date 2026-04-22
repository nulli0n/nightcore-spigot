package su.nightexpress.nightcore.util.text.night.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.bridge.text.NightStyle;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.ParserResolvers;

public class EntryGroup implements Entry {

    private final String          name;
    private final List<Entry>     childrens;
    private final ParserResolvers resolvers;

    private EntryGroup parent;
    private NightStyle style;
    private boolean    styleLocked;

    public EntryGroup(@NonNull String name) {
        this.name = name;
        this.childrens = new ArrayList<>();
        this.resolvers = new ParserResolvers();
        this.style = NightStyle.EMPTY;
    }

    public void closeResolvers() {
        this.resolvers.removeAll().forEach(resolver -> resolver.handleClose(this));
    }

    @NonNull
    public List<EntryGroup> getChildGroups() {
        List<EntryGroup> groups = new ArrayList<>();
        groups.add(this);

        this.childrens.forEach(entry -> {
            if (entry instanceof EntryGroup group) {
                groups.addAll(group.getChildGroups());
            }
        });

        return groups;
    }

    @NonNull
    public EntryGroup downward(@NonNull String name) {
        EntryGroup group = new EntryGroup(name);
        group.parent = this;
        group.style = this.style;
        return this.addChildren(group);
    }

    @NonNull
    public EntryGroup upward(@NonNull String parentName) {
        EntryGroup upperGroup = this.parent;
        while (upperGroup != null && !upperGroup.name.equalsIgnoreCase(parentName)) {
            upperGroup = upperGroup.parent;
        }

        return upperGroup == null ? this : upperGroup;
    }

    @NonNull
    public EntryGroup backTo(@NonNull String parentName) {
        if (this.name.equalsIgnoreCase(parentName)) return this;

        return this.upward(parentName);
    }

    @NonNull
    public EntryGroup upward() {
        return this.parent == null ? this : this.parent;
    }

    @NonNull
    public TextEntry appendTextEntry(@NonNull String text) {
        return this.addChildren(new TextEntry(this, text));
    }

    @NonNull
    public LangEntry appendLangEntry(@NonNull String key, @Nullable String fallback) {
        return this.addChildren(new LangEntry(this, key, fallback));
    }

    @NonNull
    public KeybindEntry appendKeybindEntry(@NonNull String key) {
        return this.addChildren(new KeybindEntry(this, key));
    }

    @NonNull
    public <T extends ChildEntry> T appendEntry(@NonNull T entry) {
        return this.addChildren(entry);
    }

    @NonNull
    private <T extends Entry> T addChildren(@NonNull T child) {
        this.childrens.add(child);
        return child;
    }

    @Override
    @NonNull
    public NightComponent toComponent() {
        List<NightComponent> children = this.childrens.stream().map(Entry::toComponent).toList();

        return NightComponent.empty().children(children);
    }

    @NonNull
    public ParserResolvers getResolvers() {
        return this.resolvers;
    }

    @NonNull
    public String name() {
        return this.name;
    }

    @Nullable
    public EntryGroup parent() {
        return this.parent;
    }

    @NonNull
    public NightStyle style() {
        return this.style;
    }

    public void setStyle(@NonNull NightStyle style) {
        if (this.styleLocked) return;

        this.style = style;
    }

    public void setStyle(@NonNull UnaryOperator<NightStyle> consumer) {
        if (this.styleLocked) return;

        this.setStyle(consumer.apply(this.style()));
    }

    public boolean isStyleLocked() {
        return styleLocked;
    }

    public void setStyleLocked(boolean styleLocked) {
        this.styleLocked = styleLocked;
    }

    @NonNull
    public List<Entry> getChildrens() {
        return this.childrens;
    }

    public void setChildrens(@NonNull List<Entry> childrens) {
        this.childrens.clear();
        this.childrens.addAll(childrens);
    }
}
