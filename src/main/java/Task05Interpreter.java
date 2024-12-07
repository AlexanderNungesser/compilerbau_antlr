import java.util.ArrayList;

public class Task05Interpreter extends Task05ASTTypeCheckVisitor {
  Scope scope;

  public Task05Interpreter(Scope scope) {
    super(scope);
    this.scope = scope;
  }

  public Object eval(Task05ASTNode node) {
    return switch (node.getType()) {
      case Task05ASTNode.Type.ID -> evalID(node);
      case Task05ASTNode.Type.NUMBER -> Double.parseDouble(node.getValue());
      case Task05ASTNode.Type.BOOLEAN -> Boolean.parseBoolean(node.getValue());
      case Task05ASTNode.Type.STRING -> node.getValue();
      case Task05ASTNode.Type.LIST -> evalList(node);
      case Task05ASTNode.Type.DEF -> evalDef(node);
      case Task05ASTNode.Type.FN -> evalFn(node);
      case Task05ASTNode.Type.FCALL -> evalFcall(node);
      case Task05ASTNode.Type.LET -> evalLet(node);
      case Task05ASTNode.Type.IF -> evalIf(node);
      case Task05ASTNode.Type.BLOCK -> evalBlock(node);
      case Task05ASTNode.Type.OP -> evalOp(node);
      default -> evalChildren(node);
    };
  }

  public Object evalChildren(Task05ASTNode node) {
    for (Task05ASTNode child : node.children) {
      eval(child);
    }
    return node;
  }

  public Object evalID(Task05ASTNode node) {

    return node.getValue();
  }

  public ArrayList<Object> evalList(Task05ASTNode node) {
    ArrayList<Object> list = new ArrayList<>();
    for (Task05ASTNode child : node.children) {
      eval(child);
      list.add(child.getValue());
    }
    return list;
  }

  public Object evalDef(Task05ASTNode node) {
    return eval(node.children.getLast());
  }

  public Object evalFn(Task05ASTNode node) {
    this.scope = this.scope.innerScope;
    evalChildren(node);
    this.scope = this.scope.enclosingScope;
    return node;
  }

  public Object evalFcall(Task05ASTNode node) {
    Task05ASTNode child = node.children.getFirst();
    if (this.scope.resolve(child.getValue()) != null) {

      switch (child.getType()) {
        case Task05ASTNode.Type.OP:
          evalOp(node);
          break;

        case Task05ASTNode.Type.ID:
          switch (child.getValue()) {
            case "print":
              return (String) eval(node.children.get(1));

            case "str":
              String a = (String) eval(node.children.get(1));
              String b = (String) eval(node.children.get(2));
              return a.concat(b);

            case "head":
              return eval(node.children.get(1).children.getFirst());

            case "tail":
              return eval(node.children.get(1).children.getLast());

            case "nth":
              return eval(
                  node.children
                      .get(1)
                      .children
                      .get(Integer.parseInt(node.children.getLast().getValue())));
          }
          break;
      }
    }
    return node;
  }

  public Object evalOp(Task05ASTNode node) {
    switch (node.children.getFirst().getValue()) {
      case "+":
        if (node.children.get(1).getType() == Task05ASTNode.Type.STRING) {
          String a = (String) eval(node.children.get(1));
          String b = (String) eval(node.children.get(2));
          return a.concat(b);
        }
        double a = (Double) eval(node.children.get(1));
        double b = (Double) eval(node.children.get(2));
        return a + b;

      case "-":
        double c = (Double) eval(node.children.get(1));
        double d = (Double) eval(node.children.get(2));
        return c - d;

      case "*":
        double e = (Double) eval(node.children.get(1));
        double f = (Double) eval(node.children.get(2));
        return e * f;

      case "/":
        double g = (Double) eval(node.children.get(1));
        double h = (Double) eval(node.children.get(2));
        return g / h;

      case "=":
        if (node.children.get(1).getType() == Task05ASTNode.Type.NUMBER) {
          double i = (Double) eval(node.children.get(1));
          double j = (Double) eval(node.children.get(2));
          return i == j;
        } else if (node.children.get(1).getType() == Task05ASTNode.Type.STRING) {
          String i = (String) eval(node.children.get(1));
          String j = (String) eval(node.children.get(2));
          return i.equals(j);
        } else if (node.children.get(1).getType() == Task05ASTNode.Type.BOOLEAN) {
          boolean i = (Boolean) eval(node.children.get(1));
          boolean j = (Boolean) eval(node.children.get(2));
          return i == j;
        }

      case "<":
        double i = (Double) eval(node.children.get(1));
        double j = (Double) eval(node.children.get(2));
        return i < j;

      case ">":
        double k = (Double) eval(node.children.get(1));
        double l = (Double) eval(node.children.get(2));
        return k > l;
    }
    return node;
  }

  public Object evalLet(Task05ASTNode node) {
    this.scope = this.scope.innerScope;
    evalChildren(node);
    this.scope = this.scope.enclosingScope;
    return node;
  }

  public Object evalIf(Task05ASTNode node) {
    evalChildren(node);
    return node;
  }

  public Object evalBlock(Task05ASTNode node) {
    this.scope = this.scope.innerScope;
    evalChildren(node);
    this.scope = this.scope.enclosingScope;
    return node;
  }
}
