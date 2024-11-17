import java.util.ArrayList;

public class ASTScopeVisitor extends ASTVisitor {

  Scope scope;
  ArrayList<Scope> scopeList = new ArrayList<>();

  public ASTNode visit(ASTNode node) {
    switch (node.getValue()) {
      case "program":
        visitProgram(node);
        break;
      case "vardecl":
        visitVardecl(node);
        break;
      case "fndecl":
        visitFndecl(node.children.getFirst());
        break;
      case "fncall":
        visitFncall(node.children.getFirst());
        break;
      case "while":
        visitWhile(node);
        break;
      case "cond":
        visitCond(node);
        break;
      case "block":
        visitBlock(node);
        break;
      case "assign":
        visitAssign(node);
        break;
      case "params":
        visitParams(node);
        break;
      case "return":
        visitReturn(node);
        break;
      default:
        visitExpr(node);
        break;
    }
    return node;
  }

  public ASTNode visitChildren(ASTNode node) {
    for (ASTNode child : node.children) {
      visit(child);
    }
    return node;
  }

  public ASTNode visitProgram(ASTNode node) {
    Scope globals = new Scope();
    scopeList.add(globals);
    globals.bind(new BuiltIn("int"));
    globals.bind(new BuiltIn("bool"));
    globals.bind(new BuiltIn("string"));
    scope = globals;
    visitChildren(node);
    return node;
  }

  public ASTNode visitVardecl(ASTNode node) {
    Symbol t = scope.resolve(node.children.get(0).getValue());
    Symbol var = new Variable(node.children.get(1).getValue(), t.name);
    Symbol exists = scope.resolve(var.name);
    if (exists != null) {
      System.out.println("Error: such variable " + exists.name + " already exists");
      return node;
    }
    scope.bind(var);
    return node;
  }

  public ASTNode visitExpr(ASTNode node) {
    if (node.children.isEmpty() && node.getType().equals("ID")) {
      String name = node.getValue();
      Symbol var = scope.resolve(name);
      if (var == null) {
        System.out.println("Error: no such variable: " + name);
      }
    } else {
      visitChildren(node);
    }
    return node;
  }

  public ASTNode visitFncall(ASTNode node) {
    String name = node.getValue();
    Symbol func = scope.resolve(name);
    if (func == null) {
      System.out.println("Error: no such function: " + name);
    }
    if (func instanceof Variable) {
      System.out.println("Error: " + name + " is not a function");
    }
    visitChildren(node);
    return node;
  }

  //  public ASTNode visitArgs(ASTNode node) {
  //    return node;
  //  }

  public ASTNode visitAssign(ASTNode node) {
    for (ASTNode child : node.children) {
      visitExpr(child);
    }
    return node;
  }

  public ASTNode visitFndecl(ASTNode node) {
    String name = node.getValue();
    Symbol type = scope.resolve(node.getType());
    Symbol func = new Function(name, type.name);
    scope.bind(func);
    scope = new Scope(scope);
    scopeList.add(scope);
    visitChildren(node);
    scope = scope.enclosingScope;
    return node;
  }

  public ASTNode visitParams(ASTNode node) {
    for (ASTNode child : node.children) {
      String name = child.getValue();
      Symbol type = scope.resolve(child.getType());
      Symbol param = new Variable(name, type.name);
      scope.bind(param);
    }
    return node;
  }

  public ASTNode visitBlock(ASTNode node) {
    scope = new Scope(scope);
    scopeList.add(scope);
    visitChildren(node);
    scope = scope.enclosingScope;
    return node;
  }

  public ASTNode visitWhile(ASTNode node) {
    visitChildren(node);
    return node;
  }

  public ASTNode visitCond(ASTNode node) {
    visitChildren(node);
    return node;
  }

  public ASTNode visitReturn(ASTNode node) {
    visitChildren(node);
    return node;
  }
}
