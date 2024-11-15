import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class MainTask04 {
    public static void main(String... args) throws IOException {

        String input =
                "int f95(int n) {\n" +
                        "    if (n == 0) {\n" +
                        "        return 1;\n" +
                        "    } else {\n" +
                        "        if (n == 1) {\n" +
                        "            return 1;\n" +
                        "        } else {\n" +
                        "            return f95(n - 1) + f95(n - 2) + f95(n - 3) + f95(n - 4) + f95(n - 5);\n" +
                        "        }\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "int n = 10;\n" +
                        "f95(n);";

        MiniCLexer lexer = new MiniCLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniCParser parser = new MiniCParser(tokens);

        System.out.println("Input:\n" + input + "\n");

        ParseTree tree = parser.program(); // Start-Regel
        System.out.println("ParseTree:\n" + tree.toStringTree(parser) + "\n");

        ASTVisitor eval = new ASTVisitor();

        ASTNode ast = eval.visit(tree);


        ast.print();
    }
}