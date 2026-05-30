package su.nightexpress.nightcore.ui.inventory.item.populator;

@FunctionalInterface
public interface SlotProvider {

    int[] getSlots(int size);

}
