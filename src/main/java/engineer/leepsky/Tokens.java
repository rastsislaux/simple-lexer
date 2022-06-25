package engineer.leepsky;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tokens  {

    private int currentToken = -1;

    private List<Token> tokenList = new ArrayList<>();

    public void add(Token token) {
        tokenList.add(token);
    }

    public Token token() {
        return tokenList.get(currentToken);
    }

    public boolean nextToken() {
        currentToken++;
        return currentToken >= 0 && currentToken < tokenList.size();
    }

    public boolean prevToken() {
        currentToken--;
        return currentToken >= 0 && currentToken < tokenList.size();
    }

    @Override
    public String toString() {
        return "Tokens{" +
                "currentToken=" + currentToken +
                ", tokenList=" + tokenList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tokens tokens = (Tokens) o;
        return currentToken == tokens.currentToken && Objects.equals(tokenList, tokens.tokenList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentToken, tokenList);
    }
}
