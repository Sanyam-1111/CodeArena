package models;

public class Token {

    private final String type;
    private final String value;
    private final int lineNumber;

    public Token(String type, String value, int lineNumber) {

        if (type == null || value == null) {
            throw new IllegalArgumentException(
                    "Token type and value cannot be null."
            );
        }

        if (lineNumber < 1) {
            throw new IllegalArgumentException(
                    "Line number must be positive."
            );
        }

        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        return type + "(" + value + ") at line " + lineNumber;
    }
}