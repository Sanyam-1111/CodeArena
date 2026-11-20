package exceptions;

public class CodeRankModuleException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String errorCode;

    public CodeRankModuleException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

