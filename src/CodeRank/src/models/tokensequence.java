package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class tokensequence {
    private final List<token> tokens;

    public tokensequence(List<token> tokens) {
        this.tokens = new ArrayList<>(tokens);
    }

    public List<token> getTokens() {
        return Collections.unmodifiableList(tokens);
    }

    public int size() {
        return tokens.size();
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }
}