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
        public String type() {
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
        public String type() {
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
            PERCENT,
            COLON,
            DBL_COLON,
            SEMICOLON,
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
        public String type() {
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

    public static class None extends Token {
        None(Location loc) { super(loc); }

        @Override
        public String toString() {
            return "Token.None{" +
                    "loc=" + loc +
                    '}';
        }

        @Override
        public String type() {
            return "Token.None";
        }
    }

    @Override
    public String toString() {
        return "Token{" +
                "loc=" + loc +
                '}';
    }

    public String type() {
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
