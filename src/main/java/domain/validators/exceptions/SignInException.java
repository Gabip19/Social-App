package domain.validators.exceptions;

public class SignInException extends RuntimeException {
    public SignInException() {
        super();
    }

    public SignInException(String message) {
        super(message);
    }

    public SignInException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignInException(Throwable cause) {
        super(cause);
    }

    protected SignInException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
