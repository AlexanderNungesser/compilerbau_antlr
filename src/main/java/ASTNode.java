import java.util.ArrayList;

public class ASTNode {
  ArrayList<ASTNode> children = new ArrayList<>();
  private String value = null;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public ASTNode(String value) {
    this.value = value;
  }

  public void addChild(ASTNode child) {
    this.children.add(child);
  }

  public void print() {
    printHelper(0);
  }

  private void printHelper(int depth) {
    String indent = "  ".repeat(depth);
    System.out.println(indent + "[" + this.value + "]");
    for (ASTNode child : this.children) {
      child.printHelper(depth + 1);
    }
  }

//  private void print(String prefix, boolean isLast) {
//    // Anzeige des aktuellen Knotens
//    System.out.println(prefix + (isLast ? "'__ " : "|-- ") + this.type + (this.value.isEmpty() ? "" : " (" + this.value + ")"));
//
//    // Anzeige der Kinder
//    for (int i = 0; i < children.size(); i++) {
//      ASTNode child = (ASTNode) children.get(i);
//      boolean lastChild = (i == children.size() - 1);
//      child.print(prefix + (isLast ? "    " : "|   "), lastChild);
//    }
//  }
}
