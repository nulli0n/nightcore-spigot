package su.nightexpress.nightcore.ui.inventory.condition;

public class ItemStateConditions {

    public static final NamedCondition NEXT_PAGE     = new NamedCondition("has_next_page", context -> context.getViewer().canNavigateForward());
    public static final NamedCondition PREVIOUS_PAGE = new NamedCondition("has_previous_page", context -> context.getViewer().canNavigateBackward());

}
