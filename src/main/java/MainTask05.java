import java.io.IOException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class MainTask05 {
  public static void main(String... args) throws IOException {
    String input =
        ";;     name   params  body\n"
            + "(defn  hello  (n)     (str \"hello \" n))  ;; Definition einer Funktion \"hello\" mit einem Parameter\n"
            + "\n"
            + "(def n (hello \"world\"))                          ;; Aufruf der Funktion \"hello\" mit dem Argument \"world\""
            + "(+ \"abc\" \"def\")"
            + "(nth (list 1 2 3) 2)"
            + "(list 1 2 3)          ;; (1 2 3)\n"
            + "\n"
            + "(def v (list 1 2 3))  ;; v = (1 2 3)\n"
            + "v                     ;; (1 2 3)"
            + "(def x 99)   ;; globale Variable x\n"
            + "(def y 101)  ;; globale Variable y\n"
            + "(def z 42)   ;; globale Variable z\n"
            + "(let (x 1   ;; lokales x mit Wert 1(verdeckt globales x)\n"
            + "      y 2)  ;; lokales y mit Wert 2\n"
            + "     (+ x y z))  ;; 1+2+42\n"
            + "\n"
            + "(defn  hello\n"
            + "       (n)\n"
            + "       (let (l 42)  ;; l is valid in this scope\n"
            + "            (str \"hello \" n \": \" l)\n"
            + "       )  ;; end of local scope\n"
            + ")  ;; end of function definition"
            + "(def x 42)  ;; definiert eine neue Variable mit dem Namen \"x\" und dem Wert 42\n"
            + "\n"
            + "x           ;; liefert 42\n"
            + "(+ x 7)     ;; liefert 49\n"
            + "(if (< 1 2)\n"
            + "    (do\n"
            + "    (print \"wuppie\")\n"
            + "    (print \"fluppie\")\n"
            + "    (print \"foo\")\n"
            + "    (print \"bar\"))\n"
            + "    (print \"false\"))";

    MiniLispBLexer lexer = new MiniLispBLexer(CharStreams.fromString(input));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MiniLispBParser parser = new MiniLispBParser(tokens);

    System.out.println("Input:\n" + input + "\n");

    ParseTree tree = parser.program(); // Start-Regel
    System.out.println("ParseTree:\n" + tree.toStringTree(parser) + "\n");

    Task05ParseTreeVisitor eval = new Task05ParseTreeVisitor();

    Task05ASTNode ast = eval.visit(tree);
    ast.print();

    Task05ASTScopeVisitor scopeVisitor = new Task05ASTScopeVisitor();
    Task05ASTNode astScope = scopeVisitor.visit(ast);

    scopeVisitor.scope.print();

    Task05ASTTypeCheckVisitor typeChecker = new Task05ASTTypeCheckVisitor(scopeVisitor.scope);
    typeChecker.visit(astScope);

    Task05Interpreter interpreter = new Task05Interpreter(scopeVisitor.scope);
    interpreter.eval(astScope);

    astScope.print();
  }
}
