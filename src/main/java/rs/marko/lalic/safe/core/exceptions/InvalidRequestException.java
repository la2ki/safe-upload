package rs.marko.lalic.safe.core.exceptions;

/**
 * Thrown if Request is not valid. This is applicable for any kind of request
 *
 * @author Marko Lalic
 */
public class InvalidRequestException extends BaseException implements ErrorCode {

    /**
     * Default constructor.
     *
     */
    public InvalidRequestException() {
    }

    /**
     * Initializes exception and sets message using specified message string.
     *
     * @param message Error message.
     */
    public InvalidRequestException(String message) {
	super(message);
    }

    /**
     * Initializes exception and sets the error code
     *
     * @param code Error code.
     */
    public InvalidRequestException(long code) {
	super(code);
    }

    /**
     * Initializes exception and sets  cause.
     *
     * @param e Throwable
     */
    public InvalidRequestException(Throwable e) {
	super(e);
    }

    /**
     * Initializes exception and sets message using specified message string, and error code
     *
     * @param message Error message.
     * @param code    Error code.
     */
    public InvalidRequestException(String message, long code) {
	super(message, code);
    }

    /**
     * Initializes exception and sets message and cause.
     *
     * @param message Error message
     * @param e       Throwable
     */
    public InvalidRequestException(String message, Throwable e) {
	super(message, e);
    }

    /**
     * Initializes exception and cause, and error code
     *
     * @param e    Throwable
     * @param code Error code.
     */
    public InvalidRequestException(Throwable e, long code) {
	super(e, code);
    }

    /**
     * Initializes exception and sets message, cause and error code.
     *
     * @param message Error message
     * @param e       Throwable
     * @param code    Error code.
     */
    public InvalidRequestException(String message, Throwable e, long code) {
	super(message, e, code);
    }
}
