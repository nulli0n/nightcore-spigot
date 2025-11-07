package su.nightexpress.nightcore.ui.inventory.condition;

public class ItemStateConditions {

    public static final ItemStateCondition NEXT_PAGE = context -> context.getViewer().canNavigateForward();
    public static final ItemStateCondition PREVIOUS_PAGE = context -> context.getViewer().canNavigateBackward();
    // TODO Back
}
