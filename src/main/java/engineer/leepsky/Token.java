package engineer.leepsky;

import java.util.Objects;

public abstract class Token {

    record Location(String pathToFile, long col, long row) { }

    protected Location loc;

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    Token(Location loc) { this.loc = loc; }

    public static class Identifier extends Token {

        protected String name;

        Identifier(String name, Location loc) {
            super(loc);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Token.Identifier{" +
                    "name='" + name + '\'' +
                    ", loc=" + loc +
                    '}';
        }

        @Override
        public String toStringNL() {
            return "Token.Identifier{" + name + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Identifier that = (Identifier) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), name);
        }

    }

    public static class Keyword extends Token {
        enum Kind {
            EXAMPLE_KEYWORD_1,
            EXAMPLE_KEYWORD_2,
            NONE
        }

        private Kind keywordKind;

        public Kind getKind() {
            return keywordKind;
        }

        public void setKind(Kind keywordKind) {
            this.keywordKind = keywordKind;
        }

        public Keyword(Keyword.Kind kind, Location loc) {
            super(loc);
            this.keywordKind = kind;
        }

        public static Kind getKeywordKindByName(String name) {
            switch (name) {
                case "example1" -> { return Kind.EXAMPLE_KEYWORD_1; }
                case "example2" -> { return Kind.EXAMPLE_KEYWORD_2; }
                default ->  { return Kind.NONE; }
            }
        }

        @Override
        public String toString() {
            return "Token.Keyword{" +
                    "keywordKind=" + keywordKind +
                    ", loc=" + loc +
                    '}';
        }

        @Override
        public String toStringNL() {
            return "Token.Keyword{" + keywordKind + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Keyword keyword = (Keyword) o;
            return keywordKind == keyword.keywordKind;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), keywordKind);
        }
    }

    public static class Special extends Token {

        enum Kind {
            // Brackets
            CURLY_OPEN,
            CURLY_CLOSE,
            PAREN_OPEN,
            PAREN_CLOSE,

            // Other special symbols
            COMMA,
            EQUALS,
            DBL_EQUALS,
            BAR,
            BANG,
            PLUS,
            DASH,
            ARROW,
            ASTERISK,
            SLASH,
            HASHTAG,
            PERCENT,
            COLON,
            DBL_COLON,
            SEMICOLON,
            DOT
        }

        private Kind specialKind;

        public Kind getKind() {
            return specialKind;
        }

        public void setKind(Kind kind) {
            this.specialKind = kind;
        }

        Special(Kind specialKind, Location loc) {
            super(loc);
            this.specialKind = specialKind;
        }

        @Override
        public String toString() {
            return "Token.Special{" +
                    "specialKind=" + specialKind +
                    ", loc=" + loc +
                    '}';
        }

        @Override
        public String toStringNL() {
            return "Token.Special{" + specialKind + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Special special = (Special) o;
            return specialKind == special.specialKind;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), specialKind);
        }
    }

    public static class StringLiteral extends Token {

        private String content;
       StringLiteral(String content, Location loc) {
           super(loc);
           this.content = content;
       }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toStringNL() {
            return "Token.StringLiteral{\"" + content + "\"}";
        }

        @Override
        public String toString() {
            return "StringLiteral{" +
                    "content='" + content + '\'' +
                    ", loc=" + loc +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            StringLiteral that = (StringLiteral) o;
            return Objects.equals(content, that.content);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), content);
        }
    }

    public static class IntLiteral extends Token {
        private String value;

        public IntLiteral(String value, Location loc) {
            super(loc);
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "IntLiteral{" +
                    "value=" + value +
                    ", loc=" + loc +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            IntLiteral that = (IntLiteral) o;
            return Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), value);
        }

        @Override
        public String toStringNL() {
            return "Token.IntLiteral{\"" + value + "\"}";
        }
    }

    public static class FloatLiteral extends Token {
        private String value;

        public FloatLiteral(String value, Location loc) {
            super(loc);
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "FloatLiteral{" +
                    "value=" + value +
                    ", loc=" + loc +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            FloatLiteral that = (FloatLiteral) o;
            return Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), value);
        }

        @Override
        public String toStringNL() {
            return "Token.FloatLiteral{\"" + value + "\"}";
        }
    }
    public static class Unparsed extends Token {

        enum Fail {
            UNCLOSED_STRING_LITERAL,
            UNKNOWN_SEQUENCE_OF_CHARACTERS,
            INVALID_FLOAT
        }

        private Fail fail;

        Fail getFail() {
            return fail;
        }

        Unparsed(Fail fail, Location loc) { super(loc); this.fail = fail; }

        @Override
        public String toString() {
            return "Token.Unparsed{" +
                    "fail=" + fail +
                    ", loc=" + loc +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Unparsed unparsed = (Unparsed) o;
            return fail == unparsed.fail;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), fail);
        }

        @Override
        public String toStringNL() {
            return "Token.Unparsed{" + fail + '}';
        }
    }

    @Override
    public String toString() {
        return "Token{" +
                "loc=" + loc +
                '}';
    }

    public String toStringNL() {
        return "Token.Abstract";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(loc, token.loc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loc);
    }
}
