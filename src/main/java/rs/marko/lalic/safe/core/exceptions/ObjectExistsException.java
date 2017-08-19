package rs.marko.lalic.safe.core.exceptions;

/**
 * Thrown if Object already exists.
 *
 * @author Marko Lalic
 */
public class ObjectExistsException extends BaseException {

    /**
     * Default constructor.
     *
     */
    public ObjectExistsException() {
    }

    /**
     * Initializes exception and sets message using specified message string.
     *
     * @param message Error message.
     */
    public ObjectExistsException(String message) {
	super(message);
    }

    /**
     * Initializes exception and sets the error code
     *
     * @param code Error code.
     */
    public ObjectExistsException(long code) {
	super(code);
    }

    /**
     * Initializes exception and sets message using specified message string, and error code
     *
     * @param message Error message.
     * @param code Error code.
     */
    public ObjectExistsException(String message, long code) {
	super(message, code);
    }

    /**
     * Initializes exception and sets message and cause.
     *
     * @param message Error message
     * @param e Throwable
     */
    public ObjectExistsException(String message, Throwable e) {
	super(message, e);
    }

    /**
     * Initializes exception and cause, and error code
     *
     * @param e Throwable
     * @param code Error code.
     */
    public ObjectExistsException(Throwable e, long code) {
	super(e, code);
    }

    /**
     * Initializes exception and sets message, cause and error code.
     *
     * @param message Error message
     * @param e Throwable
     * @param code Error code.
     */
    public ObjectExistsException(String message, Throwable e, long code) {
	super(message, e, code);
    }
}
