package su.nightexpress.nightcore.ui.dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.util.text.TextRoot;

@Deprecated
public class Dialog {

    private static final int DEFAULT_TIMEOUT = 30;

    private final Player        player;
    private final String        prompt;
    private final DialogHandler handler;
    private final List<String>  suggestions;
    private final boolean       suggestionAutoRun;

    private Menu lastMenu;
    private int  lastPage;
    private int  lifetime;

    public Dialog(@NonNull Player player, @NonNull String prompt, @NonNull DialogHandler handler, @Nullable List<String> suggestions, boolean suggestionAutoRun, int lifetime) {
        this.player = player;
        this.prompt = prompt;
        this.handler = handler;
        this.suggestions = suggestions == null ? null : suggestions.stream().sorted(String::compareTo).collect(
            Collectors.toCollection(ArrayList::new));
        this.suggestionAutoRun = suggestionAutoRun;
        this.lifetime = lifetime;
    }

    public void tick() {
        this.lifetime--;
    }

    public boolean isExpired() {
        return this.lifetime <= 0;
    }

    @NonNull
    public Player getPlayer() {
        return this.player;
    }

    @NonNull
    public DialogHandler getHandler() {
        return this.handler;
    }

    @NonNull
    public String getPrompt() {
        return this.prompt;
    }

    @Nullable
    public Menu getLastMenu() {
        return this.lastMenu;
    }

    @NonNull
    public Dialog setLastMenu(@Nullable Menu lastMenu) {
        this.lastMenu = lastMenu;
        return this;
    }

    public int getLastPage() {
        return this.lastPage;
    }

    public Dialog setLastPage(int lastPage) {
        this.lastPage = Math.max(1, lastPage);
        return this;
    }

    @Nullable
    public List<String> getSuggestions() {
        return this.suggestions;
    }

    public boolean isSuggestionAutoRun() {
        return this.suggestionAutoRun;
    }

    @Deprecated
    public long getTimeoutDate() {
        return this.lifetime;
    }

    public int getLifetime() {
        return this.lifetime;
    }

    public long getLifetimeMillis() {
        return this.getLifetime() * 1000L;
    }

    @NonNull
    public static Builder builder(@NonNull MenuViewer viewer, @NonNull LangString prompt, @NonNull DialogHandler handler) {
        return builder(viewer.getPlayer(), prompt, handler);
    }

    @NonNull
    public static Builder builder(@NonNull MenuViewer viewer, @NonNull String prompt, @NonNull DialogHandler handler) {
        return builder(viewer.getPlayer(), prompt, handler);
    }

    @NonNull
    public static Builder builder(@NonNull Player player, @NonNull LangString prompt, @NonNull DialogHandler handler) {
        return builder(player, handler).setPrompt(prompt);
    }

    @NonNull
    public static Builder builder(@NonNull Player player, @NonNull String prompt, @NonNull DialogHandler handler) {
        return builder(player, handler).setPrompt(prompt);
    }

    @NonNull
    public static Builder builder(@NonNull MenuViewer viewer, @NonNull DialogHandler handler) {
        return builder(viewer.getPlayer(), handler);
    }

    @NonNull
    public static Builder builder(@NonNull Player player, @NonNull DialogHandler handler) {
        return new Builder(player, handler);
    }

    @Deprecated
    public static class Builder {

        private final Player        player;
        private final DialogHandler handler;

        private String       prompt;
        private List<String> suggestions;
        private boolean      suggestionAutoRun;
        private int          timeout;

        public Builder(@NonNull Player player, @NonNull DialogHandler handler) {
            this.player = player;
            this.handler = handler;
            this.setPrompt(CoreLang.DIALOG_DEFAULT_PROMPT.getString());
            this.setTimeout(DEFAULT_TIMEOUT);
        }

        @NonNull
        public Player getPlayer() {
            return this.player;
        }

        @NonNull
        public Dialog build() {
            return new Dialog(this.player, this.prompt, this.handler, this.suggestions, this.suggestionAutoRun, this.timeout);
        }

        public void initialize() {
            DialogManager.startDialog(this);
        }

        public Builder setPrompt(@NonNull TextRoot text) {
            return this.setPrompt(text.getString());
        }

        public Builder setPrompt(@NonNull LangString text) {
            return this.setPrompt(text.getString());
        }

        public Builder setPrompt(@NonNull String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder setSuggestions(@NonNull Collection<String> suggestions, boolean autoRun) {
            this.suggestions = new ArrayList<>(suggestions);
            this.suggestionAutoRun = autoRun;
            return this;
        }

        public Builder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }
    }
}
