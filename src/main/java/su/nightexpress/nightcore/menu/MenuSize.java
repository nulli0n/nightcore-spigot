package su.nightexpress.nightcore.menu;

public enum MenuSize {

    CHEST_9(9),
    CHEST_18(18),
    CHEST_27(27),
    CHEST_36(36),
    CHEST_45(45),
    CHEST_54(54)
    ;

    private final int size;

    MenuSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
