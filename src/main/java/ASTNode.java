import java.util.ArrayList;

public class ASTNode {
    ArrayList children = new ArrayList();
    String type;
    String value;

    public ASTNode(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public ASTNode(String type) {
        this.type = type;
        this.value = "";
    }

    public void addChild(ASTNode child) {
        this.children.add(child);
    }

    public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isLast) {
        // Anzeige des aktuellen Knotens
        System.out.println(prefix + (isLast ? "'__ " : "|-- ") + this.type + (this.value.isEmpty() ? "" : " (" + this.value + ")"));

        // Anzeige der Kinder
        for (int i = 0; i < children.size(); i++) {
            ASTNode child = (ASTNode) children.get(i);
            boolean lastChild = (i == children.size() - 1);
            child.print(prefix + (isLast ? "    " : "|   "), lastChild);
        }
    }
}