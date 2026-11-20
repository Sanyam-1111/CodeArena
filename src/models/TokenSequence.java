package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TokenSequence {

    private final List<Token> tokens;

    public TokenSequence(List<Token> tokens) {
        this.tokens = new ArrayList<>(tokens);
    }

    public List<Token> getTokens() {
        return Collections.unmodifiableList(tokens);
    }

    public int size() {
        return tokens.size();
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }
}