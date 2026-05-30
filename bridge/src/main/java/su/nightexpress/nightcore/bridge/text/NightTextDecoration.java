package su.nightexpress.nightcore.bridge.text;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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

        @NonNull
        public static State byBoolean(boolean flag) {
            return flag ? TRUE : FALSE;
        }

        @NonNull
        public static State byBoolean(@Nullable Boolean flag) {
            return flag == null ? NOT_SET : byBoolean(flag.booleanValue());
        }
    }
}
