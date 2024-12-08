import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Task05Interpreter extends Task05ASTTypeCheckVisitor {
  Scope scope;
  Set<Scope> visitedScopes = new HashSet<Scope>();

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
    Symbol symbol = scope.resolve(node.getValue());
    // keine Ahnung was passieren soll, wenn symbol oder symbol.type null ist
    if (symbol == null) {}
    if (symbol.type == null){}
    if (symbol.type.equals(Task05ASTNode.Type.NUMBER.name())) {
      return Double.parseDouble(symbol.value);
    } else if (symbol.type.equals(Task05ASTNode.Type.BOOLEAN.name())) {
      return Boolean.parseBoolean(symbol.value);
    } else if (symbol.type.equals(Task05ASTNode.Type.STRING.name())) {
      return symbol.value;
    } else {
      return evalList(node);
    }
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
    return evalScopes(node);
  }

  public Object evalFcall(Task05ASTNode node) {
    Task05ASTNode child = node.children.getFirst();
    if (this.scope.resolve(child.getValue()) != null) {

      switch (child.getType()) {
        case Task05ASTNode.Type.OP:
          node = evalOp(node);
          break;

        case Task05ASTNode.Type.ID:
          switch (child.getValue()) {
            case "print":
              return eval(node.children.get(1));

            case "str":
              String a = (String) eval(node.children.get(1));
              String b = (String) eval(node.children.get(2));
              return new Task05ASTNode(a.concat(b));

            case "head":
              return new Task05ASTNode((String) eval(node.children.get(1).children.getFirst()));

            case "tail":
              return new Task05ASTNode((String) eval(node.children.get(1).children.getLast()));

            case "nth":
              return new Task05ASTNode((String) eval(
                  node.children
                      .get(1)
                      .children
                      .get(Integer.parseInt(node.children.getLast().getValue()))));
          }
          break;
      }
    }
    return node;
  }

  public Task05ASTNode evalOp(Task05ASTNode node) {
    switch (node.children.getFirst().getValue()) {
      case "+":
        if (node.children.get(1).getType() == Task05ASTNode.Type.STRING) {
          String a = (String) eval(node.children.get(1));
          String b = (String) eval(node.children.get(2));
          return new Task05ASTNode(a.concat(b));
        }
        double a = Double.parseDouble((String) eval(node.children.get(1)));
        double b = Double.parseDouble((String) eval(node.children.get(2)));
        return new Task05ASTNode(String.valueOf(a+b));

      case "-":
        double c = Double.parseDouble((String) eval(node.children.get(1)));
        double d = Double.parseDouble((String) eval(node.children.get(2)));
        return new Task05ASTNode(String.valueOf(c-d));

      case "*":
        double e = Double.parseDouble((String) eval(node.children.get(1)));
        double f = Double.parseDouble((String) eval(node.children.get(2)));
        return new Task05ASTNode(String.valueOf(e*f));

      case "/":
        double g = Double.parseDouble((String) eval(node.children.get(1)));
        double h = Double.parseDouble((String) eval(node.children.get(2)));
        return new Task05ASTNode(String.valueOf(g/h));

      case "=":
        if (node.children.get(1).getType() == Task05ASTNode.Type.NUMBER) {
          double i = Double.parseDouble((String) eval(node.children.get(1)));
          double j = Double.parseDouble((String) eval(node.children.get(2)));
          return new Task05ASTNode(String.valueOf(i==j));
        } else if (node.children.get(1).getType() == Task05ASTNode.Type.STRING) {
          String i = (String) eval(node.children.get(1));
          String j = (String) eval(node.children.get(2));
          return new Task05ASTNode(String.valueOf(i.equals(j)));
        } else if (node.children.get(1).getType() == Task05ASTNode.Type.BOOLEAN) {
          boolean i = (Boolean) eval(node.children.get(1));
          boolean j = (Boolean) eval(node.children.get(2));
          return new Task05ASTNode(String.valueOf(i==j));
        }

      case "<":
        double i = Double.parseDouble((String) eval(node.children.get(1)));
        double j = Double.parseDouble((String) eval(node.children.get(2)));
        return new Task05ASTNode(String.valueOf(i<j));

      case ">":
        double k = Double.parseDouble((String) eval(node.children.get(1)));
        double l = Double.parseDouble((String) eval(node.children.get(2)));
        return new Task05ASTNode(String.valueOf(k>l));
    }
    return node;
  }

  public Object evalLet(Task05ASTNode node) {
    return evalScopes(node);
  }

  public Object evalIf(Task05ASTNode node) {
    evalChildren(node);
    return node;
  }

  public Object evalBlock(Task05ASTNode node) {
    return evalScopes(node);
  }

  private Object evalScopes(Task05ASTNode node) {
    for (Scope scope : this.scope.innerScopes) {
      if (!visitedScopes.contains(scope)) {
        this.scope = scope;
        evalChildren(node);
        this.scope = this.scope.enclosingScope;
        visitedScopes.add(scope);
      }
    }
    return node;
  }
}
