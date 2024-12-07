import java.util.ArrayList;

public class Task05ASTNode {

  public enum Type {
    PROGRAM,
    EXPR,
    LITERAL,
    SYMBOL,
    LIST,
    DEF,
    FN,
    FCALL,
    LET,
    BINDING,
    IF,
    BLOCK,
    NUMBER,
    STRING,
    BOOLEAN,
    ID,
    OP
  }

  ArrayList<Task05ASTNode> children = new ArrayList<>();
  private String value = null;
  private Type type = null;

  public String getValue() {
    return value;
  }

  public Type getType() {
    return type;
  }

  public Task05ASTNode(String value) {
    this.value = value;
    this.type = null;
  }

  public Task05ASTNode(String value, Type type) {
    this.value = value;
    this.type = type;
  }

  public Task05ASTNode(Type type) {
    this.value = null;
    this.type = type;
  }

  public void addChild(Task05ASTNode child) {
    this.children.add(child);
  }

  public void addChildren(ArrayList<Task05ASTNode> children) {
    this.children.addAll(children);
  }

  public void print() {
    print("", true);
  }

  private void print(String prefix, boolean isLast) {
    // Anzeige des aktuellen Knotens
    System.out.println(
        prefix
            + (isLast ? "'__" : "|--")
            + (this.value == null ? "" : " " + this.value)
            + (this.type == null ? "" : " (" + this.type + ")"));

    // Anzeige der Kinder
    for (int i = 0; i < children.size(); i++) {
      Task05ASTNode child = children.get(i);
      boolean lastChild = (i == children.size() - 1);
      child.print(prefix + (isLast ? "    " : "|   "), lastChild);
    }
  }
}
