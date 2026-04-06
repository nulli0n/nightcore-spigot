package su.nightexpress.nightcore.ui.inventory.item.type;

import su.nightexpress.nightcore.ui.inventory.item.ItemType;

public class CustomItemType implements ItemType {

    /*@Override
    @NonNull
    public String getConfigSection() {
        return "Items";
    }*/

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public boolean isStatesLocked() {
        return false;
    }

    @Override
    public boolean isActionLocked() {
        return false;
    }

    @Override
    public boolean isConditionLocked() {
        return false;
    }

    @Override
    public boolean isDisplayModifierLocked() {
        return false;
    }

    @Override
    public boolean isClickCommandsAllowed() {
        return true;
    }
}
