package su.nightexpress.nightcore.ui.inventory.action;

public class MenuItemActions {

    public static final MenuItemAction NEXT_PAGE     = context -> context.getViewer().navigateForward();
    public static final MenuItemAction PREVIOUS_PAGE = context -> context.getViewer().navigateBackward();
    public static final MenuItemAction CLOSE         = context -> context.getViewer().closeMenu();
}
