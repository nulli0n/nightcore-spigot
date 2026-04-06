package su.nightexpress.nightcore.ui.inventory.display;

public class DisplayModifiers {

    public static final NamedDisplayModifier VIEWER_SKULL = new NamedDisplayModifier("viewer_skull", (context, item) -> item.setPlayerProfile(context.getPlayer()));

}
