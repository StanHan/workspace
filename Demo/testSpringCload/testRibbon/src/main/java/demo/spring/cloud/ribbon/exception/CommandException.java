package demo.spring.cloud.ribbon.exception;

public class CommandException extends Exception {

    private static final long serialVersionUID = 453115711084080405L;

    public CommandException() {
        super();
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }

}
