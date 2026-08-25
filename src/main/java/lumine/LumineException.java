package lumine;
/**
 * Represents an input error that can be explained to the user.
 */
public class LumineException extends RuntimeException {

    public LumineException(String message) {
        super(message);
    }

}
