package su.nightexpress.nightcore.ui.inventory.item.type;

import su.nightexpress.nightcore.ui.inventory.item.ItemType;

public class ButtonItemType implements ItemType {

    /*@Override
    @NonNull
    public String getConfigSection() {
        return "Buttons";
    }*/

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public boolean isStatesLocked() {
        return true;
    }

    @Override
    public boolean isActionLocked() {
        return true;
    }

    @Override
    public boolean isConditionLocked() {
        return true;
    }

    @Override
    public boolean isDisplayModifierLocked() {
        return true;
    }

    @Override
    public boolean isClickCommandsAllowed() {
        return false;
    }
}
