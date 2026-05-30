package su.nightexpress.nightcore.ui.inventory.display;

import java.util.function.UnaryOperator;

import su.nightexpress.nightcore.ui.inventory.item.DisplayModifier;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

public class DisplayModifiers {

    private DisplayModifiers() {
    }

    public static final NamedDisplayModifier VIEWER_SKULL = new NamedDisplayModifier(
        "viewer_skull", (context, item) -> {
            item.setPlayerProfile(Players.getProfile(context.getPlayer()));
        });

    public static final DisplayModifier PAPI = (context, item) -> {
        UnaryOperator<String> operator = CommonPlaceholders.forPlaceholderAPI(context.getPlayer());
        item.replaceNameWithLore(operator);
    };
}
