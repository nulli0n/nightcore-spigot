package su.nightexpress.nightcore.bridge.dialog;

public class DialogDefaults {

    public static final int DEFAULT_BUTTON_WIDTH       = 150;
    public static final int DEFAULT_PLAIN_BODY_WIDTH   = 200;
    public static final int DEFAULT_SINGLE_INPUT_WIDTH = 200;
    public static final int DEFAULT_TEXT_INPUT_WIDTH   = 200;
    public static final int DEFAULT_TEXT_INPUT_LENGTH  = 32;
    public static final int DEFAULT_NUMBER_INPUT_WIDTH   = 200;

    public static final int MIN_WIDTH = 1;
    public static final int MAX_WIDTH = 1024;

    public static final int MAX_ITEM_BODY_WIDTH = 256;
    public static final int MAX_ITEM_BODY_HEIGHT = 256;

    public static final int DEFAULT_ITEM_BODY_WIDTH = 16;
    public static final int DEFAULT_ITEM_BODY_HEIGHT = 16;

    public static int clampWidth(int width) {
        return Math.clamp(width, MIN_WIDTH, MAX_WIDTH);
    }
}
