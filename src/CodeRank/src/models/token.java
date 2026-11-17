package models;

public class token {
    private final String type;
    private final String value;
    private final int lineNumber;

    public token(String type, String value, int lineNumber) {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
    }

    public String getType() { return type; }
    public String getValue() { return value; }
    public int getLineNumber() { return lineNumber; }

    @Override
    public String toString() {
        return type + "(" + value + ")";
    }
}