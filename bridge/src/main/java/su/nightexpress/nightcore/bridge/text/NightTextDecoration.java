package su.nightexpress.nightcore.bridge.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum NightTextDecoration {

    OBFUSCATED,
    BOLD,
    STRIKETHROUGH,
    UNDERLINED,
    ITALIC;

    public enum State {

        NOT_SET,
        FALSE,
        TRUE;

        public boolean bool() {
            return this == TRUE;
        }

        @NotNull
        public static State byBoolean(boolean flag) {
            return flag ? TRUE : FALSE;
        }

        @NotNull
        public static State byBoolean(@Nullable Boolean flag) {
            return flag == null ? NOT_SET : byBoolean(flag.booleanValue());
        }
    }
}
