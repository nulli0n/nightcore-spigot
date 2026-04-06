package su.nightexpress.nightcore.ui.inventory.item;

public interface ItemType {

    boolean isPersistent();

    boolean isStatesLocked();

    boolean isActionLocked();

    boolean isConditionLocked();

    boolean isDisplayModifierLocked();

    boolean isClickCommandsAllowed();
}
