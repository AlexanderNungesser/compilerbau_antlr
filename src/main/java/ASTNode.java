import java.util.ArrayList;

public class ASTNode {
  ArrayList<ASTNode> children = new ArrayList<>();
  private String value = null;
  private String type = null;

  public String getValue() {
    return value;
  }

  public String getType() {
    return type;
  }

  public ASTNode(String value) {
    this.value = value;
    this.type = "";
  }

  public ASTNode(String value, String type) {
    this.value = value;
    this.type = type;
  }

  public void addChild(ASTNode child) {
    this.children.add(child);
  }

  public void addChildren(ArrayList<ASTNode> children) {
    this.children.addAll(children);
  }

  public void print() {
    print("", true);
  }

  private void print(String prefix, boolean isLast) {
    // Anzeige des aktuellen Knotens
    System.out.println(
        prefix
            + (isLast ? "'__ " : "|-- ")
            + this.value
            + (this.type.isEmpty() ? "" : " (" + this.type + ")"));

    // Anzeige der Kinder
    for (int i = 0; i < children.size(); i++) {
      ASTNode child = children.get(i);
      boolean lastChild = (i == children.size() - 1);
      child.print(prefix + (isLast ? "    " : "|   "), lastChild);
    }
  }
}
