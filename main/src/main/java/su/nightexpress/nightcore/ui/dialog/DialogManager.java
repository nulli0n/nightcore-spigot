package su.nightexpress.nightcore.ui.dialog;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.MenuRegistry;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bridge.wrapper.ClickEventType;
import su.nightexpress.nightcore.util.time.TimeFormats;

import java.util.*;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class DialogManager {

    private static final Map<UUID, Dialog> DIALOG_MAP = new HashMap<>();

    public static final String EXIT   = "#exit";
    public static final String VALUES = "#values";

    public static void shutdown() {
        DIALOG_MAP.clear();
    }

    @NotNull
    public static Set<Dialog> getDialogs() {
        return new HashSet<>(DIALOG_MAP.values());
    }

    public static void tickDialogs() {
        getDialogs().forEach(DialogManager::tickDialog);
    }

    private static void tickDialog(@NotNull Dialog dialog) {
        if (dialog.isExpired()) {
            stopDialog(dialog);
            return;
        }

        displayPrompt(dialog, 0);
        dialog.tick();
    }

    private static void displayPrompt(@NotNull Dialog dialog, int fade) {
        Player player = dialog.getPlayer();
        String title = /*NightMessage.asLegacy(*/CoreLang.DIALOG_HEADER.getString().replace(Placeholders.GENERIC_TIME, TimeFormats.toLiteral(dialog.getLifetimeMillis()));
        String sub = /*NightMessage.asLegacy(*/dialog.getPrompt();

        //player.sendTitle(title, sub, fade, 40, 20);
        Players.sendTitle(player, title, sub, fade, 40, 20);
    }

    public static boolean isInDialog(@NotNull Player player) {
        return getDialog(player) != null;
    }

    @Nullable
    public static Dialog getDialog(@NotNull Player player) {
        return DIALOG_MAP.get(player.getUniqueId());
    }

    public static void startDialog(@NotNull Dialog.Builder builder) {
        startDialog(builder.build());
    }

    public static void startDialog(@NotNull Dialog dialog) {
        Player player = dialog.getPlayer();

        MenuViewer viewer = MenuRegistry.getViewer(player);
        if (viewer != null) {
            dialog.setLastMenu(viewer.getMenu());
            dialog.setLastPage(viewer.getPage());
        }

        displaySuggestions(dialog, 1);
        displayPrompt(dialog, 20);

        DIALOG_MAP.put(player.getUniqueId(), dialog);
        CoreLang.DIALOG_INFO_EXIT.getMessage().send(player);
    }

    public static void stopDialog(@NotNull Player player) {
        Dialog dialog = getDialog(player);
        if (dialog == null) return;

        stopDialog(dialog);
    }

    public static void stopDialog(@NotNull Dialog dialog) {
        Player player = dialog.getPlayer();
        Menu menu = dialog.getLastMenu();
        if (menu != null) {
            menu.flush(player, viewer -> viewer.setPage(dialog.getLastPage()));
        }

        DIALOG_MAP.remove(player.getUniqueId());
    }

    public static void displaySuggestions(@NotNull Dialog dialog, int page) {
        List<String> suggestions = dialog.getSuggestions();
        if (suggestions == null || suggestions.isEmpty()) return;

        boolean autoRun = dialog.isSuggestionAutoRun();

        int perPage = 10;
        int pages = (int) Math.ceil((double) suggestions.size() / (double) perPage);
        if (page < 1) page = 1;
        else if (page > pages) page = pages;
        int skip = (page - 1) * perPage;

        boolean isLastPage = page == pages;
        boolean isFirstPage = page == 1;
        List<String> items = suggestions.stream().skip(skip).limit(perPage).toList();
        ClickEventType action = autoRun ? ClickEventType.RUN_COMMAND : ClickEventType.SUGGEST_COMMAND;

        StringBuilder builder = new StringBuilder()
            .append(ORANGE.enclose("=".repeat(8) + "[ " + YELLOW.enclose("Value Helper") + " ]" + "=".repeat(8)))
            .append(Placeholders.TAG_LINE_BREAK);

        items.forEach(element -> {
            String hoverHint = GRAY.enclose("Click me to select " + CYAN.enclose(element) + ".");
            String clickCommand = element.charAt(0) == '/' ? element : '/' + element;

            builder.append(DARK_GRAY.enclose("> ")).append(GREEN.enclose(HOVER.wrapShowText(CLICK.wrap(element, action, clickCommand), hoverHint)));
            builder.append(Placeholders.TAG_LINE_BREAK);
        });

        builder.append(ORANGE.enclose("=".repeat(9))).append(" ");

        if (isFirstPage) {
            builder.append(GRAY.enclose("[<]"));
        }
        else {
            builder.append(LIGHT_RED.enclose(HOVER.wrapShowText(CLICK.wrapRunCommand("[<]", "/" + VALUES + " " + (page - 1)), GRAY.enclose("Previous Page"))));
        }

        builder.append(YELLOW.enclose(" " + page));
        builder.append(ORANGE.enclose("/"));
        builder.append(YELLOW.enclose(pages + " "));

        if (isLastPage) {
            builder.append(GRAY.enclose("[>]"));
        }
        else {
            builder.append(LIGHT_RED.enclose(HOVER.wrapShowText(CLICK.wrapRunCommand("[>]", "/" + VALUES + " " + (page + 1)), GRAY.enclose("Next Page"))));

        }

        builder.append(ORANGE.enclose(" " + "=".repeat(9)));

        Players.sendModernMessage(dialog.getPlayer(), builder.toString());
    }
}
