package exceptions;

public class CodeRankModuleException extends RuntimeException {
    private final String errorCode;

    public CodeRankModuleException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}