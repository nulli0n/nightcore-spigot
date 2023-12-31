package su.nightexpress.nightcore.util.text.tag.api;

import org.jetbrains.annotations.NotNull;

public abstract class Tag {

    public static final char OPEN_BRACKET  = '<';
    public static final char CLOSE_BRACKET = '>';
    public static final char CLOSE_MARK    = '/';

    protected final String name;

    public Tag(@NotNull String name) {
        this.name = name.toLowerCase();
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
