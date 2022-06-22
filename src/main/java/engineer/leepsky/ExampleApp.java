package engineer.leepsky;

import java.util.Scanner;

public class ExampleApp {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Lexer lexer;
        String input;
        do {
            System.out.print("> ");
            input = in.nextLine();
            lexer = new Lexer(input, "<stdin>");
            int i = 1;
            while (lexer.nextToken()) {
                switch (lexer.token().toStringNL().substring(6, 8)) {

                    case "Id" ->
                        System.out.printf("%d. Identifier, name: `%s`, loc: %d:%d%n",
                                i++,
                                ((Token.Identifier)(lexer.token())).getName(),
                                lexer.token().getLoc().row(),
                                lexer.token().getLoc().col());

                    case "Ke" ->
                            System.out.printf("%d. Keyword, name: `%s`, loc: %d:%d%n",
                                    i++,
                                    ((Token.Keyword)(lexer.token())).getKind(),
                                    lexer.token().getLoc().row(),
                                    lexer.token().getLoc().col());

                    case "Sp" ->
                            System.out.printf("%d. Special, name: `%s`, loc: %d:%d%n",
                                    i++,
                                    ((Token.Special)(lexer.token())).getKind(),
                                    lexer.token().getLoc().row(),
                                    lexer.token().getLoc().col());

                    case "St" ->
                            System.out.printf("%d. String Literal, content: \"%s\", loc: %d:%d%n",
                                    i++,
                                    ((Token.StringLiteral)(lexer.token())).getContent(),
                                    lexer.token().getLoc().row(),
                                    lexer.token().getLoc().col());

                    case "In" ->
                            System.out.printf("%d. Int Literal, content: \"%s\", loc: %d:%d%n",
                                    i++,
                                    ((Token.IntLiteral)(lexer.token())).getValue(),
                                    lexer.token().getLoc().row(),
                                    lexer.token().getLoc().col());

                    case "Fl" ->
                            System.out.printf("%d. Int Literal, content: \"%s\", loc: %d:%d%n",
                                    i++,
                                    ((Token.FloatLiteral)(lexer.token())).getValue(),
                                    lexer.token().getLoc().row(),
                                    lexer.token().getLoc().col());

                    case "Un" ->
                            System.out.printf("%d. Unparsed, fail: \"%s\", loc: %d:%d%n",
                                    i++,
                                    ((Token.Unparsed)(lexer.token())).getFail(),
                                    lexer.token().getLoc().row(),
                                    lexer.token().getLoc().col());

                    default -> System.out.println(lexer.token());

                }
            }
        } while (!input.equals("#q"));
    }

}
