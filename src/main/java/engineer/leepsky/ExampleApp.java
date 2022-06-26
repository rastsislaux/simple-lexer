package engineer.leepsky;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class ExampleApp {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        List<Token> tokens;
        String input;
        do {

            System.out.print("> ");
            input = in.nextLine();
            if (!input.startsWith("#load ")) {
                tokens = Lexer.lex(input, "<stdin>");
            } else {
                try {
                    tokens = Lexer.lex(
                            new String(Files.readAllBytes(Paths.get(input.substring(6)))),
                            input.substring(6));
                }
                catch (IOException e) {
                    System.out.printf("IO Exception while #load: %s%n", e.getMessage());
                    continue;
                }
            }

            for (Token token : tokens) {
                System.out.printf("%d:%02d\t | ", token.getLoc().row(), token.getLoc().col());
                switch (token.toStringNL().substring(6, 8)) {
                    case "Id" -> System.out.printf("IDENT\t | `%s`",    ((Token.Identifier)(token)).getName()       );
                    case "Ke" -> System.out.printf("KEYWORD\t | `%s`",  ((Token.Keyword)(token)).getKind()          );
                    case "Sp" -> System.out.printf("SPECIAL\t | `%s`",  ((Token.Special)(token)).getKind()          );
                    case "St" -> System.out.printf("STRING\t | `%s`",   ((Token.StringLiteral)(token)).getContent() );
                    case "In" -> System.out.printf("INT\t\t | `%s`",    ((Token.IntLiteral)(token)).getValue()      );
                    case "Fl" -> System.out.printf("FLOAT\t | `%s`",    ((Token.FloatLiteral)(token)).getValue()    );
                    case "Un" -> System.out.printf("Unparsed\t | `%s`", ((Token.Unparsed)(token)).getFail()         );
                    default -> System.out.println(token);
                }
                System.out.printf("%n");
            }

        } while (!input.equals("#q"));
    }
}
