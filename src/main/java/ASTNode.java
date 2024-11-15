import java.util.ArrayList;

public class ASTNode {
    ArrayList children = new ArrayList();
    String value;

    public ASTNode(String value) {
        this.value = value;
    }

    public void addChild(ASTNode child) {
        this.children.add(child);
    }

    public void print() {
        System.out.println(this.value);
        System.out.print("-->");
        for (Object child : this.children) {
            ((ASTNode) child).print();
        }
        System.out.print("<--");
    }
}