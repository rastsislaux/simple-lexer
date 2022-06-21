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
            int i = 0;
            while (lexer.nextToken()) {
                switch (lexer.token().type().substring(6, 7)) {

                    case "I" ->
                        System.out.printf("%d. Identifier, name: `%s`, loc: %d:%d%n",
                                i++,
                                ((Token.Identifier)(lexer.token())).getName(),
                                lexer.token().getLoc().row(),
                                lexer.token().getLoc().col());

                    case "K" ->
                            System.out.printf("%d. Keyword, name: `%s`, loc: %d:%d%n",
                                    i++,
                                    ((Token.Keyword)(lexer.token())).getKind(),
                                    lexer.token().getLoc().row(),
                                    lexer.token().getLoc().col());

                    case "S" ->
                            System.out.printf("%d. Special, name: `%s`, loc: %d:%d%n",
                                    i++,
                                    ((Token.Special)(lexer.token())).getKind(),
                                    lexer.token().getLoc().row(),
                                    lexer.token().getLoc().col());

                }
            }
        } while (!input.equals("#q"));
    }

}
