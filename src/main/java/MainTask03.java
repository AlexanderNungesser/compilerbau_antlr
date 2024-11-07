import java.io.IOException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class MainTask03 {

  public static void main(String... args) throws IOException {

    String input =
        "a     := 0\n"
            + "    if    10 < 1\n"
            + "       do\n"
            + "a    :=     42      # Zuweisung des Wertes 42 an die Variable a\n"
            + "else do\n"
            + "        a :=      7\n"
            + "  end";

    Task03Lexer lexer = new Task03Lexer(CharStreams.fromString(input));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    Task03Parser parser = new Task03Parser(tokens);

    System.out.println("Input:\n" + input + "\n");

    ParseTree tree = parser.program(); // Start-Regel
    System.out.println("ParseTree:\n" + tree.toStringTree(parser) + "\n");

    MyTask03Visitor eval = new MyTask03Visitor();

    String output = eval.visit(tree);

    System.out.println("Output:\n" + output + "!");
  }
}
