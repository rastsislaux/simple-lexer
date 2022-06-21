package engineer.leepsky;

import java.util.*;

public class Lexer {

    // Kind of macros to construct tokens easier
    private static Token makeSpecial(Token.Special.Kind kind, String file, long col, long row) {
        return new Token.Special(kind, new Token.Location(file, col, row));
    }

    private static Token makeKeyword(Token.Keyword.Kind kind, String file, long col, long row) {
        return new Token.Keyword(kind, new Token.Location(file, col, row));
    }

    private static Token makeIdent(String name, String file, long col, long row) {
        return new Token.Identifier(name, new Token.Location(file, col, row));
    }

    private List<Token> tokens = new ArrayList<>();

    private int currentToken;

    public List<Token> getTokens() {
        return tokens;
    }

    public Lexer(String source, String path) {
        setSource(source, path);
    }

    private static final char COLON     = ':';
    private static final char EQUALS    = '=';
    private static final char ASTERISK  = '*';
    private static final char SPACE     = ' ';
    private static final char DASH      = '-';
    private static final char BIGGER    = '>';
    private static final char SLASH         = '/';
    private static final char CURLY_OPEN    = '{';
    private static final char CURLY_CLOSE   = '}';
    private static final char PAREN_OPEN    = '(';
    private static final char PAREN_CLOSE   = ')';
    private static final char PLUS          = '+';
    private static final char PERCENT       = '%';
    private static final char BANG          = '!';
    private static final char BAR           = '|';
    private static final char COMMA         = ',';
    private static final char SEMICOLON     = ';';

    // Set source (lexing itself)
    private void setSource(String source, String path) {

        currentToken = -1;

        List<Token> tokenList = new ArrayList<>();
        long row = 1;
        long col = 0;
        char_loop: for (int i = 0; i < source.length(); i++) {

            if (source.charAt(i) == '\n') { col = 1; row++; continue; }
            else col++;

            Token toAdd = null;
            switch (source.charAt(i)) {

                case SLASH          -> toAdd = makeSpecial(Token.Special.Kind.SLASH,        path, col, row);
                case CURLY_OPEN     -> toAdd = makeSpecial(Token.Special.Kind.CURLY_OPEN,   path, col, row);
                case CURLY_CLOSE    -> toAdd = makeSpecial(Token.Special.Kind.CURLY_CLOSE,  path, col, row);
                case PAREN_OPEN     -> toAdd = makeSpecial(Token.Special.Kind.PAREN_OPEN,   path, col, row);
                case PAREN_CLOSE    -> toAdd = makeSpecial(Token.Special.Kind.PAREN_CLOSE,  path, col, row);
                case ASTERISK       -> toAdd = makeSpecial(Token.Special.Kind.ASTERISK,     path, col, row);
                case PLUS           -> toAdd = makeSpecial(Token.Special.Kind.PLUS,         path, col, row);
                case PERCENT        -> toAdd = makeSpecial(Token.Special.Kind.PERCENT,      path, col, row);
                case BANG           -> toAdd = makeSpecial(Token.Special.Kind.BANG,         path, col, row);
                case BAR            -> toAdd = makeSpecial(Token.Special.Kind.BAR,          path, col, row);
                case COMMA          -> toAdd = makeSpecial(Token.Special.Kind.COMMA,        path, col, row);
                case SEMICOLON      -> toAdd = makeSpecial(Token.Special.Kind.SEMICOLON,    path, col, row);
                case SPACE          -> { continue; }

                case COLON -> {
                    if (i != source.length() - 1 && source.charAt(i + 1) == COLON) {
                        toAdd = makeSpecial(Token.Special.Kind.DBL_COLON, path, col, row);
                        i++; col++;
                    }
                    else
                        toAdd = makeSpecial(Token.Special.Kind.COLON, path, col, row);
                }

                case EQUALS -> {
                    if (i != source.length() - 1 && source.charAt(i + 1) == EQUALS) {
                        toAdd = makeSpecial(Token.Special.Kind.DBL_EQUALS, path, col, row);
                        i++; col++;
                    }
                    else
                        toAdd = makeSpecial(Token.Special.Kind.EQUALS, path, col, row);
                }

                case DASH -> {
                    if (i != source.length() - 1 && source.charAt(i + 1) == BIGGER) {
                        toAdd = makeSpecial(Token.Special.Kind.ARROW, path, col, row);
                        i++; col++;
                    }
                    else
                        toAdd = makeSpecial(Token.Special.Kind.DASH, path, col, row);
                }

                default -> {

                    // If none of the above fits, then try to parse as identifier or keyword
                    StringBuilder name = new StringBuilder();
                    while (i != source.length() && Character.isLetterOrDigit(source.charAt(i))) {
                        name.append(source.charAt(i++));
                    }
                    i--;

                    // If the name is empty (it is not alphanumeric string slice), than it is an unknown character or
                    // sequence of characters
                    if (name.isEmpty()) {
                        tokenList.add(new Token.None(new Token.Location(path, col, row)));
                        break char_loop;
                    }
                    // Trying to get a keyword with that name
                    else if (Token.Keyword.getKeywordKindByName(name.toString()) != Token.Keyword.Kind.NONE) {
                        toAdd = makeKeyword(
                                Token.Keyword.getKeywordKindByName(name.toString()),
                                path, col, row);
                        col += name.length();
                    }
                    // If none of the above fits, it is an identifier
                    else {
                        toAdd = makeIdent(name.toString(), path, col, row);
                        col += name.length();
                    }
                }

            }
            tokenList.add(toAdd);
        }

        this.tokens = tokenList;
    }

    public Token token() {
        return tokens.get(currentToken);
    }

    public boolean nextToken() {
        currentToken++;
        return currentToken >= 0 && currentToken < tokens.size();
    }

    public boolean prevToken() {
        currentToken--;
        return currentToken >= 0 && currentToken < tokens.size();
    }

    @Override
    public String toString() {
        return "Lexer" +
                tokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lexer lexer = (Lexer) o;
        return Objects.equals(tokens, lexer.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokens);
    }

}