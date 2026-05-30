package su.nightexpress.nightcore.util.text.tag.api;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.text.tag.TagUtils;

import java.util.HashSet;
import java.util.Set;

@Deprecated
public class Tag {

    @Deprecated
    public static final char OPEN_BRACKET  = '<';
    @Deprecated
    public static final char CLOSE_BRACKET = '>';
    @Deprecated
    public static final char CLOSE_SLASH   = '/';

    protected final String      name;
    protected final Set<String> aliases;

    public Tag(@NonNull String name) {
        this(name, new String[0]);
    }

    public Tag(@NonNull String name, @NonNull String[] aliases) {
        this.name = name.toLowerCase();
        this.aliases = new HashSet<>();

        for (String alias : aliases) {
            this.aliases.add(alias.toLowerCase());
        }
        this.aliases.add(this.name);
    }

    public boolean isCloseable() {
        return true;
    }

    @NonNull
    @Deprecated
    public static String brackets(@NonNull String str) {
        return TagUtils.brackets(str);
        //return OPEN_BRACKET + str + CLOSE_BRACKET;
    }

    @NonNull
    @Deprecated
    public static String closedBrackets(@NonNull String str) {
        return TagUtils.closedBrackets(str);//brackets(CLOSE_SLASH + str);
    }

    /*@NonNull
    @Deprecated
    public String enclose(@NonNull String text) {
        return this.getBracketsName() + text + this.getClosingName();
    }*/

    public final boolean isNamed(@NonNull String name) {
        return this.name.equalsIgnoreCase(name) || this.aliases.contains(name.toLowerCase());
    }

    @NonNull
    @Deprecated
    public final String getFullName() {
        return this.getBracketsName();
    }

    @NonNull
    public final String getClosingName() {
        return closedBrackets(this.getName());
    }

    @NonNull
    public final String getBracketsName() {
        return brackets(this.getName());
    }

    @NonNull
    public final String getName() {
        return this.name;
    }

    @NonNull
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
