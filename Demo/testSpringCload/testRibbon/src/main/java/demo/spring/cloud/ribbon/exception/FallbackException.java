package demo.spring.cloud.ribbon.exception;

public class FallbackException extends Exception {

    private static final long serialVersionUID = 453115711084080405L;

    public FallbackException() {
        super();
    }

    public FallbackException(String message, Throwable cause) {
        super(message, cause);
    }

    public FallbackException(String message) {
        super(message);
    }

    public FallbackException(Throwable cause) {
        super(cause);
    }

}
