import java.io.IOException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class MainTask03 {

    public static void main(String... args) throws IOException {

        String input = "a := 10\n" +
                "b := 0\n" +
                "while a >= 0 do\n" +
                "    a := a - 1\n" +
                "    b := b + 9\n" +
                "end";

        Task03Lexer lexer = new Task03Lexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Task03Parser parser = new Task03Parser(tokens);

        System.out.println(input);

        ParseTree tree = parser.start(); // Start-Regel
        System.out.println(tree.toStringTree(parser));
    }
}
