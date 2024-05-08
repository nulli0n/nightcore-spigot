package su.nightexpress.nightcore.util.text.tag.api;

import org.jetbrains.annotations.NotNull;

public abstract class Tag {

    public static final char OPEN_BRACKET  = '<';
    public static final char CLOSE_BRACKET = '>';
    public static final char CLOSE_MARK    = '/';

    protected final String name;
    protected final String[] aliases;

    public Tag(@NotNull String name) {
        this(name, new String[0]);
    }

    public Tag(@NotNull String name, @NotNull String[] aliases) {
        this.name = name.toLowerCase();
        this.aliases = aliases;
    }

    @NotNull
    public static String brackets(@NotNull String str) {
        return OPEN_BRACKET + str + CLOSE_BRACKET;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String[] getAliases() {
        return aliases;
    }

    @NotNull
    public String enclose(@NotNull String text) {
        return this.getFullName() + text + this.getClosingName();
    }

    public abstract int getWeight();

    @NotNull
    public final String getFullName() {
        return brackets(this.getName());
    }

    @NotNull
    public final String getClosingName() {
        return brackets(CLOSE_MARK + this.getName());
    }

    public boolean conflictsWith(@NotNull Tag tag) {
        return tag.getName().equalsIgnoreCase(this.getName());
    }

    @Override
    public String toString() {
        return "Tag{" +
            "name='" + name + '\'' +
            '}';
    }
}
