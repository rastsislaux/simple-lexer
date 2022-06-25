package engineer.leepsky;

import java.util.Objects;

public class Lexer {

    // Kind of macros to construct tokens easier
    private Token makeSpecial(Token.Special.Kind kind) {
        return new Token.Special(kind, new Token.Location(file_path, col, row));
    }

    private Token makeKeyword(Token.Keyword.Kind kind) {
        return new Token.Keyword(kind, new Token.Location(file_path, col, row));
    }

    private Token makeIdent(String name) {
        return new Token.Identifier(name, new Token.Location(file_path, col - name.length() + 1, row));
    }

    private Token makeString(String content) {
        return new Token.StringLiteral(content, new Token.Location(file_path, col - content.length() - 1, row));
    }

    private Token makeInt(String value) {
        return new Token.IntLiteral(value, new Token.Location(file_path, col, row));
    }

    private Token makeFloat(String value) {
        return new Token.FloatLiteral(value, new Token.Location(file_path, col, row));
    }

    // State

    private Tokens tokenList = new Tokens();

    private int curIndex;

    private char curChar() {
        return source.charAt(curIndex);
    }
    
    private final String file_path;

    private final String source;
    
    private int col;
    
    private int row;

    // Logic itself

    public static Tokens lex(String source, String path) {
        Lexer lexer = new Lexer(source, path);
        lexer._lex();
        return lexer.tokenList;
    }

    private Lexer(String source, String path) {
        this.source = source;
        this.file_path = path;
        col = 0;
        row = 1;
    }
    
    private void _lex() {
        while (curIndex < source.length()) {
            Token newToken = parseChar();
            if (newToken != null) {
                tokenList.add(newToken);
            }
            if (newToken instanceof Token.Unparsed) { break; }
            curIndex++;
        }
    }

    // Parsed characters

    static class Char {
        private Char() { }
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
        private static final char DBL_QUOTE     = '"';
        private static final char DOT           = '.';
        private static final char HASHTAG       = '#';
        private static final char ZERO          = '0';
        private static final char ONE           = '1';
        private static final char TWO           = '2';
        private static final char THREE         = '3';
        private static final char FOUR          = '4';
        private static final char FIVE          = '5';
        private static final char SIX           = '6';
        private static final char SEVEN         = '7';
        private static final char EIGHT         = '8';
        private static final char NINE          = '9';
        private static final char SQUARE_OPEN   = '[';
        private static final char SQUARE_CLOSE  = ']';
        private static final char BACKSLASH     = '\\';
        private static final char CHAR_R        = 'r';
        private static final char CHAR_N        = 'n';
        private static final char CHAR_T        = 't';
        private static final char CHAR_B        = 'b';
        private static final char CHAR_F        = 'f';
        private static final char QUOTE         = '\'';
        private static final char LESSER        = '<';
    }

    static class Unreachable extends RuntimeException {
        public Unreachable(String msg) { super(msg); }
    }

    private Token parseOneCharToken() throws Unreachable {
        switch (curChar()) {
            case Char.SLASH          -> { return makeSpecial(Token.Special.Kind.SLASH        ); }
            case Char.CURLY_OPEN     -> { return makeSpecial(Token.Special.Kind.CURLY_OPEN   ); }
            case Char.CURLY_CLOSE    -> { return makeSpecial(Token.Special.Kind.CURLY_CLOSE  ); }
            case Char.PAREN_OPEN     -> { return makeSpecial(Token.Special.Kind.PAREN_OPEN   ); }
            case Char.PAREN_CLOSE    -> { return makeSpecial(Token.Special.Kind.PAREN_CLOSE  ); }
            case Char.ASTERISK       -> { return makeSpecial(Token.Special.Kind.ASTERISK     ); }
            case Char.PLUS           -> { return makeSpecial(Token.Special.Kind.PLUS         ); }
            case Char.PERCENT        -> { return makeSpecial(Token.Special.Kind.PERCENT      ); }
            case Char.BANG           -> { return makeSpecial(Token.Special.Kind.BANG         ); }
            case Char.BAR            -> { return makeSpecial(Token.Special.Kind.BAR          ); }
            case Char.COMMA          -> { return makeSpecial(Token.Special.Kind.COMMA        ); }
            case Char.SEMICOLON      -> { return makeSpecial(Token.Special.Kind.SEMICOLON    ); }
            case Char.DOT            -> { return makeSpecial(Token.Special.Kind.DOT          ); }
            case Char.HASHTAG        -> { return makeSpecial(Token.Special.Kind.HASHTAG      ); }
            case Char.SQUARE_OPEN    -> { return makeSpecial(Token.Special.Kind.SQUARE_OPEN  ); }
            case Char.SQUARE_CLOSE   -> { return makeSpecial(Token.Special.Kind.SQUARE_CLOSE ); }
            case Char.LESSER         -> { return makeSpecial(Token.Special.Kind.LESSER       ); }
            case Char.BIGGER         -> { return makeSpecial(Token.Special.Kind.BIGGER       ); }
            case Char.SPACE          -> { return null; }
            default             -> { throw new Unreachable("This code must be unreachable. Seems like switch statement at Lexer.java:83 is not exhaustive."); }
        }
    }

    private Token parseNumericToken() {
        StringBuilder number = new StringBuilder();
        boolean hasDot = false;
        while (curIndex != source.length() &&
                (Character.isDigit(curChar()) || curChar() == Char.DOT)) {
            if (curChar() == Char.DOT && hasDot)
                return new Token.Unparsed(Token.Unparsed.Fail.INVALID_FLOAT, new Token.Location(file_path, col, row));
            if (curChar() == Char.DOT)
                hasDot = true;
            number.append(curChar());
            curIndex++; col++;
        }
        col--; curIndex--;
        if (hasDot) return makeFloat(number.toString());
        return makeInt(number.toString());
    }

    private Token parseStringLiteral() {
        StringBuilder content = new StringBuilder();
        curIndex++; col++;
        while (curChar() != Char.DBL_QUOTE) {
            if (curChar() == '\n' || curIndex == source.length() - 1) {
                return new Token.Unparsed(Token.Unparsed.Fail.UNCLOSED_STRING_LITERAL,
                        new Token.Location(file_path, col, row));
            }
            if (curChar() != Char.BACKSLASH) {
                content.append(curChar());
                curIndex++;
                col++;
            } else if (curIndex != source.length() - 1) {
                curIndex++; col++;
                switch (curChar()) {
                    case Char.CHAR_R     -> content.append('\r');
                    case Char.CHAR_B     -> content.append('\b');
                    case Char.CHAR_F     -> content.append('\f');
                    case Char.CHAR_N     -> content.append('\n');
                    case Char.CHAR_T     -> content.append('\t');
                    case Char.QUOTE      -> content.append('\'');
                    case Char.BACKSLASH  -> content.append('\\');
                    case Char.DBL_QUOTE  -> content.append('"');
                    default         -> { return new Token.Unparsed(Token.Unparsed.Fail.INVALID_STRING_ESCAPE,
                            new Token.Location(file_path, col, row)); }
                }
                curIndex++; col++;
            } else return new Token.Unparsed(Token.Unparsed.Fail.INVALID_STRING_ESCAPE,
                    new Token.Location(file_path, col, row));
        }
        return makeString(content.toString());
    }

    private String getIdentOrKeywordName() {
        StringBuilder name = new StringBuilder();
        while (curIndex != source.length() &&
                (Character.isLetterOrDigit(curChar()) || curChar() == '_')) {
            name.append(curChar());
            curIndex++; col++;
        }
        curIndex--; col--;
        return name.toString();
    }

    private Token parseOtherTokens() {
        switch (curChar()) {
            case Char.COLON -> {
                if (curIndex != source.length() - 1 && source.charAt(curIndex + 1) == Char.COLON) {
                    curIndex++; col++;
                    return makeSpecial(Token.Special.Kind.DBL_COLON);
                }
                else
                    return makeSpecial(Token.Special.Kind.COLON);
            }

            case Char.EQUALS -> {
                if (curIndex != source.length() - 1 && source.charAt(curIndex + 1) == Char.EQUALS) {
                    curIndex++; col++;
                    return makeSpecial(Token.Special.Kind.DBL_EQUALS);
                }
                else
                    return makeSpecial(Token.Special.Kind.EQUALS);
            }

            case Char.DASH -> {
                if (curIndex != source.length() - 1 && source.charAt(curIndex + 1) == Char.BIGGER) {
                    curIndex++; col++;
                    return makeSpecial(Token.Special.Kind.ARROW);
                }
                else
                    return makeSpecial(Token.Special.Kind.DASH);
            }

            default -> throw new Unreachable("This code must be unreachable. Seems like switch statement at Lexer.java:176 is not exhaustive.");
        }
    }

    private Token tryParseIdentOrKeyword(String name) {
        // If the name is empty (it is not alphanumeric string slice), then it is an unknown character or
        // sequence of characters
        if (name.isEmpty()) {
            return new Token.Unparsed(Token.Unparsed.Fail.UNKNOWN_SEQUENCE_OF_CHARACTERS,
                    new Token.Location(file_path, col, row));
        }
        // Trying to get a keyword with that name
        else if (Token.Keyword.getKeywordKindByName(name) != Token.Keyword.Kind.NONE) {
            return makeKeyword(
                    Token.Keyword.getKeywordKindByName(name));
        }
        // If none of the above fits, it is an identifier
        else {
            return makeIdent(name);
        }
    }

    private Token parseChar() {

        if (curChar() == '\n') { col = 0; row++; return null; }
        else col++;

        switch (curChar()) {

            case Char.SLASH,
                    Char.CURLY_OPEN,
                    Char.CURLY_CLOSE,
                    Char.PAREN_OPEN,
                    Char.PAREN_CLOSE,
                    Char.ASTERISK,
                    Char.PLUS,
                    Char.PERCENT,
                    Char.BANG,
                    Char.BAR,
                    Char.COMMA,
                    Char.SEMICOLON,
                    Char.DOT,
                    Char.HASHTAG,
                    Char.SQUARE_OPEN,
                    Char.SQUARE_CLOSE,
                    Char.SPACE,
                    Char.LESSER,
                    Char.BIGGER -> {
                return parseOneCharToken();
            }

            case Char.ZERO, Char.ONE, Char.TWO, Char.THREE, Char.FOUR, Char.FIVE, Char.SIX, Char.SEVEN, Char.EIGHT, Char.NINE -> {
                return parseNumericToken();
            }

            case Char.DBL_QUOTE -> {
                return parseStringLiteral();
            }

            case Char.COLON, Char.EQUALS, Char.DASH -> {
                return parseOtherTokens();
            }

            default -> {
                // If none of the above fits, then try to parse as identifier or keyword
                String name = getIdentOrKeywordName();
                return tryParseIdentOrKeyword(name);
            }

        }
    }

    @Override
    public String toString() {
        return "Lexer{" +
                "tokenList=" + tokenList +
                ", curIndex=" + curIndex +
                ", file_path='" + file_path + '\'' +
                ", source='" + source + '\'' +
                ", col=" + col +
                ", row=" + row +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lexer lexer = (Lexer) o;
        return curIndex == lexer.curIndex && col == lexer.col && row == lexer.row && Objects.equals(tokenList, lexer.tokenList) && Objects.equals(file_path, lexer.file_path) && Objects.equals(source, lexer.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenList, curIndex, file_path, source, col, row);
    }
}