package su.nightexpress.nightcore.commands.context;


import java.util.ArrayList;
import java.util.List;

public class Suggestions {

    public List<String> suggestions;

    public Suggestions() {
        this.suggestions = new ArrayList<>();
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
}
