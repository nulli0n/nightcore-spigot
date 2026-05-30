package su.nightexpress.nightcore.exception;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class ModelLoadException extends Exception {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1L;

    public ModelLoadException(String message) {
        super(message);
    }

    public ModelLoadException(Throwable cause) {
        super(cause);
    }

    public ModelLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}