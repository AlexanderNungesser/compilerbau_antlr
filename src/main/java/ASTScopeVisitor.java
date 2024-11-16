public class ASTScopeVisitor extends ASTVisitor {

  Scope scope;

  public ASTNode visitChildren(ASTNode node) {
    for (ASTNode child : node.children) {
      visitChildren(child);
    }
    return node;
  }

  public ASTNode visitProgram(ASTNode node) {
    Scope globals = new Scope();
    globals.bind(new BuiltIn("int"));
    globals.bind(new BuiltIn("bool"));
    globals.bind(new BuiltIn("string"));
    scope = globals;
    visitChildren(node);
    return node;
  }

  public ASTNode visitVardecl(ASTNode node) {
    Symbol t = scope.resolve(node.children.get(0).getValue());
    Symbol var = new Symbol(node.children.get(1).getValue(), t.name);
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
      if (var == null){
        System.out.println("Error: no such variable: " + name);
      }
    }
    return node;
  }

  public ASTNode visitFncall(ASTNode node) {
    String name = node.getValue();
    Symbol func = scope.resolve(name);
    if (func == null){
      System.out.println("Error: no such function: " + name);
    }
//    if (func.type == variable){
//      System.out.println("Error: " + name + " is not a function");
//    }
    return node;
  }

//  public ASTNode visitArgs(ASTNode node) {
//    return node;
//  }

//  public ASTNode visitAssign(ASTNode node) {
//    return node;
//  }

  public ASTNode visitFndecl(ASTNode node) {
    String name = node.getValue();
    Symbol type = scope.resolve(node.getType());
    Symbol func = new Symbol(name, type.name);
    scope.bind(func);
    scope = new Scope(scope);
    visitChildren(node);
    return node;
  }

  public ASTNode visitParams(ASTNode node) {
    for (ASTNode child : node.children) {
      String name = child.getValue();
      Symbol type = scope.resolve(child.getType());
      Symbol param = new Symbol(name, type.name);
      scope.bind(param);
    }
    return node;
  }

  public ASTNode visitBlock(ASTNode node) {
    scope = new Scope(scope);
    visitChildren(node);
    scope = scope.enclosingScope;
    return node;
  }

//  public ASTNode visitWhile(ASTNode node) {
//    return node;
//  }
//
//  public ASTNode visitCond(ASTNode node) {
//    return node;
//  }
//
//  public ASTNode visitReturn(ASTNode node) {
//    return node;
//  }
}
