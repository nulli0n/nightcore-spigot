package su.nightexpress.nightcore.commands.context;


import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Suggestions {

    public List<String> suggestions;

    public Suggestions() {
        this.suggestions = new ArrayList<>();
    }

    @NonNull
    public List<String> getSuggestions() {
        return this.suggestions;
    }

    public void setSuggestions(@NonNull List<String> suggestions) {
        this.suggestions = suggestions;
    }
}
