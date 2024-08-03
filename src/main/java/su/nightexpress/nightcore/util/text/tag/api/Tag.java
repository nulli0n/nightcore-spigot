package su.nightexpress.nightcore.util.text.tag.api;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Tag {

    public static final char OPEN_BRACKET  = '<';
    public static final char CLOSE_BRACKET = '>';
    public static final char CLOSE_SLASH   = '/';

    protected final String      name;
    protected final Set<String> aliases;

    public Tag(@NotNull String name) {
        this(name, new String[0]);
    }

    public Tag(@NotNull String name, @NotNull String[] aliases) {
        this.name = name.toLowerCase();
        this.aliases = new HashSet<>();

        for (String alias : aliases) {
            this.aliases.add(alias.toLowerCase());
        }
        this.aliases.add(this.name);
    }

    @NotNull
    public static String brackets(@NotNull String str) {
        return OPEN_BRACKET + str + CLOSE_BRACKET;
    }

    /*@NotNull
    @Deprecated
    public String enclose(@NotNull String text) {
        return this.getBracketsName() + text + this.getClosingName();
    }*/

    @NotNull
    @Deprecated
    public final String getFullName() {
        return this.getBracketsName();
    }

    @NotNull
    public final String getClosingName() {
        return brackets(CLOSE_SLASH + this.getName());
    }

    public final boolean isNamed(@NotNull String name) {
        return this.name.equalsIgnoreCase(name) || this.aliases.contains(name.toLowerCase());
    }

    @NotNull
    public final String getBracketsName() {
        return brackets(this.getName());
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    @NotNull
    public final Set<String> getAliases() {
        return this.aliases;
    }

    @Override
    public String toString() {
        return "Tag{" +
            "name='" + name + '\'' +
            '}';
    }
}
