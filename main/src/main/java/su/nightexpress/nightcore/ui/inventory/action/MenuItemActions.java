package su.nightexpress.nightcore.ui.inventory.action;

public class MenuItemActions {

    public static final NamedAction NEXT_PAGE     = new NamedAction("next_page", context -> context.getViewer().navigateForward());
    public static final NamedAction PREVIOUS_PAGE = new NamedAction("previous_page", context -> context.getViewer().navigateBackward());
    public static final NamedAction CLOSE         = new NamedAction("close_menu", context -> context.getViewer().closeMenu());
}
