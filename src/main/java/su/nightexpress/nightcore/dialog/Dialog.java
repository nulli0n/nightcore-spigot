package su.nightexpress.nightcore.dialog;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.menu.api.Menu;
import su.nightexpress.nightcore.menu.impl.AbstractMenu;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.text.TextRoot;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class Dialog {

    private static final Map<UUID, Dialog> DIALOG_MAP = new ConcurrentHashMap<>();
    private static final int DEFAULT_TIMEOUT = 60;

    public static final String EXIT       = "#exit";
    public static final String VALUES     = "#values";

    private final Player        player;
    private final DialogHandler handler;

    private Menu lastMenu;
    private List<String> suggestions;
    private long timeoutDate;

    public Dialog(@NotNull Player player, @NotNull DialogHandler handler) {
        this.player = player;
        this.handler = handler;
        this.suggestions = new ArrayList<>();
        this.setTimeout(DEFAULT_TIMEOUT);
    }

    public boolean isTimedOut() {
        return this.timeoutDate > 0 && System.currentTimeMillis() >= this.timeoutDate;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public DialogHandler getHandler() {
        return handler;
    }

    @Nullable
    public Menu getLastMenu() {
        return lastMenu;
    }

    @NotNull
    public Dialog setLastMenu(@Nullable Menu lastMenu) {
        this.lastMenu = lastMenu;
        return this;
    }

    @NotNull
    public List<String> getSuggestions() {
        return suggestions;
    }

    @NotNull
    public Dialog setSuggestions(@NotNull Collection<String> suggestions, boolean autoRun) {
        this.suggestions = suggestions.stream().sorted(String::compareTo).collect(Collectors.toCollection(ArrayList::new));
        this.displaySuggestions(autoRun, 1);
        return this;
    }

    public long getTimeoutDate() {
        return timeoutDate;
    }

    public void setTimeout(int timeout) {
        this.timeoutDate = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(timeout, TimeUnit.SECONDS);
    }

    public void displaySuggestions(boolean autoRun, int page) {
        List<String> values = this.getSuggestions();
        if (values.isEmpty()) return;

        int perPage = 10;
        int pages = (int) Math.ceil((double) values.size() / (double) perPage);
        if (page < 1) page = 1;
        else if (page > pages) page = pages;
        int skip = (page - 1) * perPage;

        boolean isLastPage = page == pages;
        boolean isFirstPage = page == 1;
        List<String> items = values.stream().skip(skip).limit(perPage).toList();
        ClickEvent.Action action = autoRun ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND;

        StringBuilder builder = new StringBuilder()
            .append(ORANGE.enclose("=".repeat(8) + "[ " + YELLOW.enclose("Value Helper") + " ]" + "=".repeat(8)))
            .append(Placeholders.TAG_LINE_BREAK);

        items.forEach(element -> {
            String hoverHint = GRAY.enclose("Click me to select " + CYAN.enclose(element) + ".");
            String clickCommand = element.charAt(0) == '/' ? element : '/' + element;

            builder.append(DARK_GRAY.enclose("> ")).append(GREEN.enclose(HOVER.encloseHint(CLICK.encloseRun(element, clickCommand), hoverHint)));
            builder.append(Placeholders.TAG_LINE_BREAK);
        });

        builder.append(ORANGE.enclose("=".repeat(9))).append(" ");

        if (isFirstPage) {
            builder.append(GRAY.enclose("[<]"));
        }
        else {
            builder.append(LIGHT_RED.enclose(HOVER.encloseHint(CLICK.encloseRun("[<]", "/" + VALUES + " " + (page - 1) + " " + autoRun), GRAY.enclose("Previous Page"))));
        }

        builder.append(YELLOW.enclose(" " + page));
        builder.append(ORANGE.enclose("/"));
        builder.append(YELLOW.enclose(pages + " "));

        if (isLastPage) {
            builder.append(GRAY.enclose("[>]"));
        }
        else {
            builder.append(LIGHT_RED.enclose(HOVER.encloseHint(CLICK.encloseRun("[>]", "/" + VALUES + " " + (page + 1) + " " + autoRun), GRAY.enclose("Next Page"))));

        }

        builder.append(ORANGE.enclose(" " + "=".repeat(9)));

        Players.sendModernMessage(this.player, builder.toString());
    }

    @Deprecated
    public void prompt(@NotNull String text) {
        this.sendInfo(CoreLang.EDITOR_INPUT_HEADER_MAIN.getMessage().toLegacy(), NightMessage.asLegacy(text));
    }

    public void prompt(@NotNull TextRoot text) {
        this.info(CoreLang.EDITOR_INPUT_HEADER_MAIN.getMessage(), text);
    }

    @Deprecated
    public void error(@NotNull String text) {
        this.sendInfo(CoreLang.EDITOR_INPUT_HEADER_ERROR.getMessage().toLegacy(), NightMessage.asLegacy(text));
    }

    public void error(@NotNull TextRoot text) {
        this.info(CoreLang.EDITOR_INPUT_HEADER_ERROR.getMessage(), text);
    }

    public void info(@NotNull TextRoot title, @NotNull TextRoot text) {
        this.sendInfo(title.toLegacy(), text.toLegacy());
    }

    @Deprecated
    public void info(@NotNull String title, @NotNull String text) {
        this.sendInfo(NightMessage.asLegacy(title), NightMessage.asLegacy(text));
    }

    private void sendInfo(@NotNull String title, @NotNull String text) {
        this.getPlayer().sendTitle(title, text, 20, Short.MAX_VALUE, 20);
    }

    public static void checkTimeOut() {
        new HashSet<>(DIALOG_MAP.values()).forEach(dialog -> {
            if (dialog.isTimedOut()) {
                stop(dialog.player);
            }
        });
    }

    @NotNull
    public static Dialog create(@NotNull Player player, @NotNull DialogHandler handler) {
        Dialog dialog = new Dialog(player, handler)
            .setLastMenu(AbstractMenu.getMenu(player));

        DIALOG_MAP.put(player.getUniqueId(), dialog);
        CoreLang.EDITOR_ACTION_EXIT.getMessage().send(player);
        return dialog;
    }

    @Nullable
    public static Dialog get(@NotNull Player player) {
        return DIALOG_MAP.get(player.getUniqueId());
    }

    public static void stop(@NotNull Player player) {
        Dialog dialog = DIALOG_MAP.remove(player.getUniqueId());
        if (dialog == null) return;

        Menu menu = dialog.getLastMenu();
        if (menu != null) {
            menu.open(player);
        }
        player.sendTitle("", "", 1,  1, 1);
    }

    public static void shutdown() {
        DIALOG_MAP.clear();
    }

    public static boolean contains(@NotNull Player player) {
        return get(player) != null;
    }
}
