package engineer.leepsky;

import java.util.*;

public class Lexer {

    // Kind of macros to construct tokens easier
    private static Token makeSpecial(Token.Special.Kind kind, ParsingState st) {
        return new Token.Special(kind, new Token.Location(st.file(), st.col(), st.row()));
    }

    private static Token makeKeyword(Token.Keyword.Kind kind, ParsingState st) {
        return new Token.Keyword(kind, new Token.Location(st.file(), st.col(), st.row()));
    }

    private static Token makeIdent(String name, ParsingState st) {
        return new Token.Identifier(name, new Token.Location(st.file(), st.col() - name.length() + 1, st.row()));
    }

    private static Token makeString(String content, ParsingState st) {
        return new Token.StringLiteral(content, new Token.Location(st.file(), st.col() - content.length() - 1, st.row()));
    }

    private static Token makeInt(String value, ParsingState st) {
        return new Token.IntLiteral(value, new Token.Location(st.file(), st.col(), st.row()));
    }

    private static Token makeFloat(String value, ParsingState st) {
        return new Token.FloatLiteral(value, new Token.Location(st.file(), st.col(), st.row()));
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

    static class Unreachable extends RuntimeException {
        public Unreachable(String msg) { super(msg); }
    }

    private static Token parseOneCharToken(String source, ParsingState st) throws Unreachable {
        switch (st.curChar(source)) {
            case SLASH          -> { return makeSpecial(Token.Special.Kind.SLASH,        st); }
            case CURLY_OPEN     -> { return makeSpecial(Token.Special.Kind.CURLY_OPEN,   st); }
            case CURLY_CLOSE    -> { return makeSpecial(Token.Special.Kind.CURLY_CLOSE,  st); }
            case PAREN_OPEN     -> { return makeSpecial(Token.Special.Kind.PAREN_OPEN,   st); }
            case PAREN_CLOSE    -> { return makeSpecial(Token.Special.Kind.PAREN_CLOSE,  st); }
            case ASTERISK       -> { return makeSpecial(Token.Special.Kind.ASTERISK,     st); }
            case PLUS           -> { return makeSpecial(Token.Special.Kind.PLUS,         st); }
            case PERCENT        -> { return makeSpecial(Token.Special.Kind.PERCENT,      st); }
            case BANG           -> { return makeSpecial(Token.Special.Kind.BANG,         st); }
            case BAR            -> { return makeSpecial(Token.Special.Kind.BAR,          st); }
            case COMMA          -> { return makeSpecial(Token.Special.Kind.COMMA,        st); }
            case SEMICOLON      -> { return makeSpecial(Token.Special.Kind.SEMICOLON,    st); }
            case DOT            -> { return makeSpecial(Token.Special.Kind.DOT,          st); }
            case HASHTAG        -> { return makeSpecial(Token.Special.Kind.HASHTAG,      st); }
            case SQUARE_OPEN    -> { return makeSpecial(Token.Special.Kind.SQUARE_OPEN,  st); }
            case SQUARE_CLOSE   -> { return makeSpecial(Token.Special.Kind.SQUARE_CLOSE, st); }
            case LESSER         -> { return makeSpecial(Token.Special.Kind.LESSER,       st); }
            case BIGGER         -> { return makeSpecial(Token.Special.Kind.BIGGER,       st); }
            case SPACE          -> { return null; }
        }
        throw new Unreachable("This code must be unreachable. Seems like switch statement at Lexer.java:83 is not exhaustive.");
    }

    private static Token parseNumericToken(String source, ParsingState st) {
        StringBuilder number = new StringBuilder();
        boolean hasDot = false;
        while (st.curIndex() != source.length() &&
                (Character.isDigit(st.curChar(source)) || st.curChar(source) == DOT)) {
            if (st.curChar(source) == DOT && hasDot)
                return new Token.Unparsed(Token.Unparsed.Fail.INVALID_FLOAT, new Token.Location(st.file(), st.col(), st.row()));
            if (st.curChar(source) == DOT)
                hasDot = true;
            number.append(st.curChar(source));
            st.incIndex(1); st.incCol(1);
        }
        st.incCol(-1); st.incIndex(-1);
        if (hasDot) return makeFloat(number.toString(), st);
        return makeInt(number.toString(), st);
    }

    private static Token parseStringLiteral(String source, ParsingState st) {
        StringBuilder content = new StringBuilder();
        st.incIndex(1); st.incCol(1);
        while (source.charAt(st.curIndex()) != DBL_QUOTE) {
            if (source.charAt(st.curIndex()) == '\n' || st.curIndex() == source.length() - 1) {
                return new Token.Unparsed(Token.Unparsed.Fail.UNCLOSED_STRING_LITERAL,
                        new Token.Location(st.file(), st.col(), st.row()));
            }
            if (st.curChar(source) != BACKSLASH) {
                content.append(source.charAt(st.curIndex()));
                st.incIndex(1);
                st.incCol(1);
            } else if (st.curIndex() != st.length() - 1) {
                st.incIndex(1); st.incCol(1);
                switch (st.curChar(source)) {
                    case CHAR_R     -> content.append('\r');
                    case CHAR_B     -> content.append('\b');
                    case CHAR_F     -> content.append('\f');
                    case CHAR_N     -> content.append('\n');
                    case CHAR_T     -> content.append('\t');
                    case QUOTE      -> content.append('\'');
                    case DBL_QUOTE  -> content.append('"');
                    default         -> { return new Token.Unparsed(Token.Unparsed.Fail.INVALID_STRING_ESCAPE,
                            new Token.Location(st.file(), st.col(), st.row())); }
                }
                st.incIndex(1); st.incCol(1);
            } else return new Token.Unparsed(Token.Unparsed.Fail.INVALID_STRING_ESCAPE,
                    new Token.Location(st.file(), st.col(), st.row()));
        }
        return makeString(content.toString(), st);
    }

    private static String getIdentOrKeywordName(String source, ParsingState st) {
        StringBuilder name = new StringBuilder();
        while (st.curIndex() != source.length() &&
                (Character.isLetterOrDigit(source.charAt(st.curIndex())) || source.charAt(st.curIndex()) == '_')) {
            name.append(source.charAt(st.curIndex()));
            st.incIndex(1); st.incCol(1);
        }
        st.incIndex(-1); st.incCol(-1);
        return name.toString();
    }

    private static Token parseOtherTokens(String source, ParsingState st) {
        switch (st.curChar(source)) {
            case COLON -> {
                if (st.curIndex() != source.length() - 1 && source.charAt(st.curIndex() + 1) == COLON) {
                    st.incIndex(1); st.incCol(1);
                    return makeSpecial(Token.Special.Kind.DBL_COLON, st);
                }
                else
                    return makeSpecial(Token.Special.Kind.COLON, st);
            }

            case EQUALS -> {
                if (st.curIndex() != source.length() - 1 && source.charAt(st.curIndex() + 1) == EQUALS) {
                    st.incIndex(1); st.incCol(1);
                    return makeSpecial(Token.Special.Kind.DBL_EQUALS, st);
                }
                else
                    return makeSpecial(Token.Special.Kind.EQUALS, st);
            }

            case DASH -> {
                if (st.curIndex() != source.length() - 1 && source.charAt(st.curIndex() + 1) == BIGGER) {
                    st.incIndex(1); st.incCol(1);
                    return makeSpecial(Token.Special.Kind.ARROW, st);
                }
                else
                    return makeSpecial(Token.Special.Kind.DASH, st);
            }
        }
        throw new Unreachable("This code must be unreachable. Seems like switch statement at Lexer.java:176 is not exhaustive.");
    }

    private static Token tryParseIdentOrKeyword(String name, ParsingState st) {
        // If the name is empty (it is not alphanumeric string slice), then it is an unknown character or
        // sequence of characters
        if (name.isEmpty()) {
            return new Token.Unparsed(Token.Unparsed.Fail.UNKNOWN_SEQUENCE_OF_CHARACTERS,
                    new Token.Location(st.file(), st.col(), st.row()));
        }
        // Trying to get a keyword with that name
        else if (Token.Keyword.getKeywordKindByName(name) != Token.Keyword.Kind.NONE) {
            return makeKeyword(
                    Token.Keyword.getKeywordKindByName(name),
                    st);
        }
        // If none of the above fits, it is an identifier
        else {
            return makeIdent(name, st);
        }
    }

    private static Token parseChar(String source, ParsingState st) {

        if (source.charAt(st.curIndex) == '\n') { st.col = 0; st.incRow(1); return null; }
        else st.incCol(1);

        switch (source.charAt(st.curIndex())) {

            case SLASH, CURLY_OPEN, CURLY_CLOSE, PAREN_OPEN, PAREN_CLOSE, ASTERISK, PLUS, PERCENT, BANG, BAR, COMMA,
                    SEMICOLON, DOT, HASHTAG, SQUARE_OPEN, SQUARE_CLOSE, SPACE, LESSER, BIGGER -> {
                return parseOneCharToken(source, st);
            }

            case ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE -> {
                return parseNumericToken(source, st);
            }

            case DBL_QUOTE -> {
                return parseStringLiteral(source, st);
            }

            case COLON, EQUALS, DASH -> {
                return parseOtherTokens(source, st);
            }

            default -> {
                // If none of the above fits, then try to parse as identifier or keyword
                String name = getIdentOrKeywordName(source, st);
                return tryParseIdentOrKeyword(name, st);
            }

        }
    }

    private static final class ParsingState {
        private int curIndex;
        private final int length;
        private final String file;
        private int col;
        private int row;

        private ParsingState(int curIndex, int length, String file, int col, int row) {
            this.curIndex = curIndex;
            this.length = length;
            this.file = file;
            this.col = col;
            this.row = row;
        }

        void incIndex(int i) {
            curIndex = curIndex + i;
        }

        public int curIndex() {
            return curIndex;
        }

        public char curChar(String source) {
            return source.charAt(curIndex);
        }

        public int length() {
            return length;
        }

        public String file() {
            return file;
        }

        public int col() {
            return col;
        }

        public void incCol(int i) {
            col = col + i;
        }

        public int row() {
            return row;
        }

        public void incRow(int i) {
            row = row + i;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (ParsingState) obj;
            return this.curIndex == that.curIndex &&
                    this.length == that.length &&
                    Objects.equals(this.file, that.file) &&
                    this.col == that.col &&
                    this.row == that.row;
        }

        @Override
        public int hashCode() {
            return Objects.hash(curIndex, length, file, col, row);
        }

        @Override
        public String toString() {
            return "ParsingState[" +
                    "curIndex=" + curIndex + ", " +
                    "length=" + length + ", " +
                    "file=" + file + ", " +
                    "col=" + col + ", " +
                    "row=" + row + ']';
        }


    }

    // Set source (lexing itself)
    private void setSource(String source, String path) {

        currentToken = -1;

        List<Token> tokenList = new ArrayList<>();

        ParsingState parsingState = new ParsingState(0, source.length(), path, 0, 1);

        while (parsingState.curIndex() < parsingState.length()) {
            Token newToken = parseChar(source, parsingState);
            if (newToken != null) {
                tokenList.add(newToken);
            }
            if (newToken instanceof Token.Unparsed) { break; }
            parsingState.incIndex(1);
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