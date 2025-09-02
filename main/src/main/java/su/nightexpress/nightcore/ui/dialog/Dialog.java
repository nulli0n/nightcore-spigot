package su.nightexpress.nightcore.ui.dialog;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.util.text.TextRoot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class Dialog {

    private static final int DEFAULT_TIMEOUT = 30;

    private final Player        player;
    private final String        prompt;
    private final DialogHandler handler;
    private final List<String>  suggestions;
    private final boolean       suggestionAutoRun;

    private Menu lastMenu;
    private int lastPage;
    private int lifetime;

    public Dialog(@NotNull Player player,
                  @NotNull String prompt,
                  @NotNull DialogHandler handler,
                  @Nullable List<String> suggestions,
                  boolean suggestionAutoRun,
                  int lifetime) {
        this.player = player;
        this.prompt = prompt;
        this.handler = handler;
        this.suggestions = suggestions == null ? null : suggestions.stream().sorted(String::compareTo).collect(Collectors.toCollection(ArrayList::new));
        this.suggestionAutoRun = suggestionAutoRun;
        this.lifetime = lifetime;
    }

    public void tick() {
        this.lifetime--;
    }

    public boolean isExpired() {
        return this.lifetime <= 0;
    }

    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    @NotNull
    public DialogHandler getHandler() {
        return this.handler;
    }

    @NotNull
    public String getPrompt() {
        return this.prompt;
    }

    @Nullable
    public Menu getLastMenu() {
        return this.lastMenu;
    }

    @NotNull
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

    @NotNull
    public static Builder builder(@NotNull MenuViewer viewer, @NotNull LangString prompt, @NotNull DialogHandler handler) {
        return builder(viewer.getPlayer(), prompt, handler);
    }

    @NotNull
    public static Builder builder(@NotNull MenuViewer viewer, @NotNull String prompt, @NotNull DialogHandler handler) {
        return builder(viewer.getPlayer(), prompt, handler);
    }

    @NotNull
    public static Builder builder(@NotNull Player player, @NotNull LangString prompt, @NotNull DialogHandler handler) {
        return builder(player, handler).setPrompt(prompt);
    }

    @NotNull
    public static Builder builder(@NotNull Player player, @NotNull String prompt, @NotNull DialogHandler handler) {
        return builder(player, handler).setPrompt(prompt);
    }

    @NotNull
    public static Builder builder(@NotNull MenuViewer viewer, @NotNull DialogHandler handler) {
        return builder(viewer.getPlayer(), handler);
    }

    @NotNull
    public static Builder builder(@NotNull Player player, @NotNull DialogHandler handler) {
        return new Builder(player, handler);
    }

    @Deprecated
    public static class Builder {

        private final Player player;
        private final DialogHandler handler;

        private String prompt;
        private List<String> suggestions;
        private boolean suggestionAutoRun;
        private int timeout;

        public Builder(@NotNull Player player, @NotNull DialogHandler handler) {
            this.player = player;
            this.handler = handler;
            this.setPrompt(CoreLang.DIALOG_DEFAULT_PROMPT.getString());
            this.setTimeout(DEFAULT_TIMEOUT);
        }

        @NotNull
        public Player getPlayer() {
            return this.player;
        }

        @NotNull
        public Dialog build() {
            return new Dialog(this.player, this.prompt, this.handler, this.suggestions, this.suggestionAutoRun, this.timeout);
        }

        public void initialize() {
            DialogManager.startDialog(this);
        }

        public Builder setPrompt(@NotNull TextRoot text) {
            return this.setPrompt(text.getString());
        }

        public Builder setPrompt(@NotNull LangString text) {
            return this.setPrompt(text.getString());
        }

        public Builder setPrompt(@NotNull String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder setSuggestions(@NotNull Collection<String> suggestions, boolean autoRun) {
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
