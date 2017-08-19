package rs.marko.lalic.safe.core.exceptions;

/**
 * Exception used when user is not authorized to perform operation
 *
 * @author Marko Lalic
 */
public class UnauthorizedException extends BaseException implements ErrorCode {

    /**
     * Default constructor
     */
    public UnauthorizedException() {
    }

    /**
     * Initializes exception and sets message using specified message string.
     *
     * @param message Error message.
     */
    public UnauthorizedException(String message) {
	super(message);
    }

    /**
     * Initializes exception and sets cause.
     *
     * @param cause Cause error
     */
    public UnauthorizedException(Throwable cause) {
	super(cause);
    }

    /**
     * Initializes exception and sets the error code
     *
     * @param code Error code.
     */
    public UnauthorizedException(long code) {
	super(code);
    }

    /**
     * Initializes exception and sets message using specified message string, and error code
     *
     * @param message Error message.
     * @param code    Error code.
     */
    public UnauthorizedException(String message, long code) {
	super(message, code);
    }

    /**
     * Initializes exception and sets message and cause.
     *
     * @param message Error message
     * @param e       Throwable
     */
    public UnauthorizedException(String message, Throwable e) {
	super(message, e);
    }

    /**
     * Initializes exception and cause, and error code
     *
     * @param e    Throwable
     * @param code Error code.
     */
    public UnauthorizedException(Throwable e, long code) {
	super(e, code);
    }

    /**
     * Initializes exception and sets message, cause and error code.
     *
     * @param message Error message
     * @param e       Throwable
     * @param code    Error code.
     */
    public UnauthorizedException(String message, Throwable e, long code) {
	super(message, e, code);
    }
}
