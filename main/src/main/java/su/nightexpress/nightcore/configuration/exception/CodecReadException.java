package su.nightexpress.nightcore.configuration.exception;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class CodecReadException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -9049933635911110650L;

    public CodecReadException(String message) {
        super(message);
    }

    public CodecReadException(Throwable cause) {
        super(cause);
    }

    public CodecReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
