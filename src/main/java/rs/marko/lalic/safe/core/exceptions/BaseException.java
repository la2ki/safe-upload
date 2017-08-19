package rs.marko.lalic.safe.core.exceptions;

/**
 * Base class for all exceptions
 *
 * @author Marko Lalic
 */
public class BaseException extends Exception implements ErrorCode {
    /**
     * Error code
     */
    private long code = 0;

    /**
     * Default constructor.
     *
     */
    public BaseException() {
    }

    /**
     * Initializes exception and sets message using specified message string.
     *
     * @param message Error message.
     */
    public BaseException(String message) {
	super(message);
    }

    /**
     * Initializes exception and sets cause.
     *
     * @param cause Cause error
     */
    public BaseException(Throwable cause) {
	super(cause);
    }

    /**
     * Initializes exception and sets the error code
     *
     * @param code Error code.
     */
    public BaseException(long code) {
	this.code = code;
    }

    /**
     * Initializes exception and sets message using specified message string, and error code
     *
     * @param message Error message.
     * @param code Error code.
     */
    public BaseException(String message, long code) {
	super(message);
	this.code = code;
    }

    /**
     * Initializes exception and sets message and cause.
     *
     * @param message Error message
     * @param e Throwable
     */
    public BaseException(String message, Throwable e) {
	super(message, e);
    }

    /**
     * Initializes exception and cause, and error code
     *
     * @param e Throwable
     * @param code Error code.
     */
    public BaseException(Throwable e, long code) {
	super(e);
	this.code = code;
    }

    /**
     * Initializes exception and sets message, cause and error code.
     *
     * @param message Error message
     * @param e Throwable
     * @param code Error code.
     */
    public BaseException(String message, Throwable e, long code) {
	super(message, e);
	this.code = code;
    }

    /**
     * Getting error code
     *
     * @return error code
     */
    public long getErrorCode() {
	return this.code;
    }

}
