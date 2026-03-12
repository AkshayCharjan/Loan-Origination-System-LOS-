package LoanOriginationSystem.exception;

public class LoanAlreadyDecidedException extends RuntimeException {

    public LoanAlreadyDecidedException(String message) {
        super(message);
    }
}
