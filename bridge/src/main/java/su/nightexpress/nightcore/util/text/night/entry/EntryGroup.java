package su.nightexpress.nightcore.util.text.night.entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.text.NightStyle;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.ParserResolvers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EntryGroup implements Entry {

    private final String          name;
    private final List<Entry>     childrens;
    private final ParserResolvers resolvers;

    private EntryGroup parent;
    private NightStyle style;
    private boolean    styleLocked;

    public EntryGroup(@NotNull String name) {
        this.name = name;
        this.childrens = new ArrayList<>();
        this.resolvers = new ParserResolvers();
        this.style = NightStyle.EMPTY;
    }

    public void closeResolvers() {
        //System.out.println("closing resolvers for " + this.name + " [parent: " + this.parentName() + "]");
        this.resolvers.removeAll().forEach(resolver -> resolver.handleClose(this));
    }

/*    private String parentName() {
        return this.parent == null ? null : this.parent.getName();
    }*/

    @NotNull
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

    @NotNull
    public EntryGroup downward(@NotNull String name) {
        EntryGroup group = new EntryGroup(name);
        group.parent = this;
        group.style = this.style;
        return this.addChildren(group);
    }

    @NotNull
    public EntryGroup upward(@NotNull String parentName) {
        EntryGroup upperGroup = this.parent;
        while (upperGroup != null && !upperGroup.name.equalsIgnoreCase(parentName)) {
            upperGroup = upperGroup.parent;
        }

        return upperGroup == null ? this : upperGroup;
    }

    @NotNull
    public EntryGroup backTo(@NotNull String parentName) {
        if (this.name.equalsIgnoreCase(parentName)) return this;

        return this.upward(parentName);
    }

    @NotNull
    public EntryGroup upward() {
        return this.parent == null ? this : this.parent;
    }

    @NotNull
    public TextEntry appendTextEntry(@NotNull String text) {
        //System.out.println("append text '" + text + "' to the '" + this.name + "' group");
        return this.addChildren(new TextEntry(this, text));
    }

    @NotNull
    public LangEntry appendLangEntry(@NotNull String key, @Nullable String fallback) {
        return this.addChildren(new LangEntry(this, key, fallback));
    }

    @NotNull
    public KeybindEntry appendKeybindEntry(@NotNull String key) {
        return this.addChildren(new KeybindEntry(this, key));
    }

    @NotNull
    public <T extends ChildEntry> T appendEntry(@NotNull T entry) {
        return this.addChildren(entry);
    }

    @NotNull
    private <T extends Entry> T addChildren(@NotNull T child) {
        this.childrens.add(child);
        return child;
    }

    @Override
    @NotNull
    public NightComponent toComponent() {
        List<NightComponent> children = this.childrens.stream().map(Entry::toComponent).toList();

        return NightComponent.empty().children(children);
    }

    @NotNull
    public ParserResolvers getResolvers() {
        return this.resolvers;
    }

    @NotNull
    public String name() {
        return this.name;
    }

    @Nullable
    public EntryGroup parent() {
        return this.parent;
    }

    @NotNull
    public NightStyle style() {
        return this.style;
    }

    public void setStyle(@NotNull NightStyle style) {
        if (this.styleLocked) return;

        this.style = style;
    }

    public void setStyle(@NotNull Function<NightStyle, NightStyle> consumer) {
        if (this.styleLocked) return;

        this.setStyle(consumer.apply(this.style()));
    }

    public boolean isStyleLocked() {
        return styleLocked;
    }

    public void setStyleLocked(boolean styleLocked) {
        this.styleLocked = styleLocked;
    }

    @NotNull
    public List<Entry> getChildrens() {
        return this.childrens;
    }

    public void setChildrens(@NotNull List<Entry> childrens) {
        this.childrens.clear();
        this.childrens.addAll(childrens);
    }
}
