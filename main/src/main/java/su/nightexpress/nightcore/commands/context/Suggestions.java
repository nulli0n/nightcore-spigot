package su.nightexpress.nightcore.commands.context;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Suggestions {

    public List<String> suggestions;

    public Suggestions() {
        this.suggestions = new ArrayList<>();
    }

    @NotNull
    public List<String> getSuggestions() {
        return this.suggestions;
    }

    public void setSuggestions(@NotNull List<String> suggestions) {
        this.suggestions = suggestions;
    }
}
