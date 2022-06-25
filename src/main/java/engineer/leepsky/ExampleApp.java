package engineer.leepsky;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class ExampleApp {

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        Tokens tokens;
        String input;
        do {
            System.out.print("> ");
            input = in.nextLine();
            if (!input.startsWith("#load ")) {
                tokens = Lexer.lex(input, "<stdin>");
            } else {
                 tokens = Lexer.lex(
                         new String(Files.readAllBytes(Paths.get(input.substring(6)))),
                         input.substring(6));
            }

            int i = 1;
            while (tokens.nextToken()) {
                switch (tokens.token().toStringNL().substring(6, 8)) {

                    case "Id" ->
                        System.out.printf("%d. Identifier, name: `%s`, loc: %d:%d%n",
                                i++,
                                ((Token.Identifier)(tokens.token())).getName(),
                                tokens.token().getLoc().row(),
                                tokens.token().getLoc().col());

                    case "Ke" ->
                            System.out.printf("%d. Keyword, name: `%s`, loc: %d:%d%n",
                                    i++,
                                    ((Token.Keyword)(tokens.token())).getKind(),
                                    tokens.token().getLoc().row(),
                                    tokens.token().getLoc().col());

                    case "Sp" ->
                            System.out.printf("%d. Special, name: `%s`, loc: %d:%d%n",
                                    i++,
                                    ((Token.Special)(tokens.token())).getKind(),
                                    tokens.token().getLoc().row(),
                                    tokens.token().getLoc().col());

                    case "St" ->
                            System.out.printf("%d. String Literal, content: `%s`, loc: %d:%d%n",
                                    i++,
                                    ((Token.StringLiteral)(tokens.token())).getContent(),
                                    tokens.token().getLoc().row(),
                                    tokens.token().getLoc().col());

                    case "In" ->
                            System.out.printf("%d. Int Literal, content: `%s`, loc: %d:%d%n",
                                    i++,
                                    ((Token.IntLiteral)(tokens.token())).getValue(),
                                    tokens.token().getLoc().row(),
                                    tokens.token().getLoc().col());

                    case "Fl" ->
                            System.out.printf("%d. Int Literal, content: `%s`, loc: %d:%d%n",
                                    i++,
                                    ((Token.FloatLiteral)(tokens.token())).getValue(),
                                    tokens.token().getLoc().row(),
                                    tokens.token().getLoc().col());

                    case "Un" ->
                            System.out.printf("%d. Unparsed, fail: `%s`, loc: %d:%d%n",
                                    i++,
                                    ((Token.Unparsed)(tokens.token())).getFail(),
                                    tokens.token().getLoc().row(),
                                    tokens.token().getLoc().col());

                    default -> System.out.println(tokens.token());

                }
            }
        } while (!input.equals("#q"));
    }
}
