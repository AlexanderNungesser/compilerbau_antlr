import java.util.ArrayList;

public class Task05ASTScopeVisitor extends Task05ParseTreeVisitor {

  Scope scope;
  ArrayList<Scope> scopeList = new ArrayList<>(); // for printing

  public Task05ASTNode visit(Task05ASTNode node) {
    switch (node.getType()) {
      case Task05ASTNode.Type.PROGRAM:
        visitProgram(node);
        break;
      case Task05ASTNode.Type.ID:
        visitID(node);
        break;
      case Task05ASTNode.Type.NUMBER:
        visitNumber(node);
        break;
      case Task05ASTNode.Type.BOOLEAN:
        visitBoolean(node);
        break;
      case Task05ASTNode.Type.STRING:
        visitString(node);
        break;
      case Task05ASTNode.Type.LIST:
        visitList(node);
        break;
      case Task05ASTNode.Type.DEF:
        visitDef(node);
        break;
      case Task05ASTNode.Type.FN:
        visitFn(node);
        break;
      case Task05ASTNode.Type.FCALL:
        visitFcall(node);
        break;
      case Task05ASTNode.Type.LET:
        visitLet(node);
        break;
      case Task05ASTNode.Type.IF:
        visitIf(node);
        break;
      case Task05ASTNode.Type.BLOCK:
        visitBlock(node);
        break;
      default:
        System.out.println("Error: Node type " + node.getType().name() + " not recognized");
        break;
    }
    return node;
  }

  public Task05ASTNode visitChildren(Task05ASTNode node) {
    for (Task05ASTNode child : node.children) {
      visit(child);
    }
    return node;
  }

  public Task05ASTNode visitProgram(Task05ASTNode node) {
    Scope globals = new Scope();
    scopeList.add(globals); // for printing
    globals.bind(new BuiltIn(Task05ASTNode.Type.NUMBER.name()));
    globals.bind(new BuiltIn(Task05ASTNode.Type.STRING.name()));
    globals.bind(new BuiltIn(Task05ASTNode.Type.BOOLEAN.name()));
    globals.bind(new BuiltIn(Task05ASTNode.Type.LIST.name()));
    globals.bind(new BuiltIn("print", Task05ASTNode.Type.STRING.name()));
    globals.bind(new BuiltIn("str", Task05ASTNode.Type.STRING.name()));
    globals.bind(new BuiltIn("head"));
    globals.bind(new BuiltIn("tail"));
    globals.bind(new BuiltIn("nth"));
    globals.bind(new BuiltIn("+"));
    globals.bind(new BuiltIn("-"));
    globals.bind(new BuiltIn("*"));
    globals.bind(new BuiltIn("/"));
    globals.bind(new BuiltIn("<", Task05ASTNode.Type.BOOLEAN.name()));
    globals.bind(new BuiltIn(">", Task05ASTNode.Type.BOOLEAN.name()));
    globals.bind(new BuiltIn("=", Task05ASTNode.Type.BOOLEAN.name()));
    scope = globals;
    visitChildren(node);
    return node;
  }

  public Task05ASTNode visitID(Task05ASTNode node) {
    Symbol exists = scope.resolve(node.getValue());
    if (exists == null) {
      Symbol var = new Variable(node.getValue(), null);
      scope.bind(var);
    }
    return node;
  }

  public Task05ASTNode visitList(Task05ASTNode node) {
    visitChildren(node);
    return node;
  }

  public Task05ASTNode visitDef(Task05ASTNode node) {
    Symbol t = scope.resolve(node.children.get(1).getType().name());
    Symbol var = new Variable(node.children.get(0).getValue(), t.name);
    Symbol exists = scope.resolve(var.name);
    if (exists != null) {
      System.out.println("Error: such variable " + exists.name + " already exists");
      return node;
    }
    scope.bind(var);
    return node;
  }

  public Task05ASTNode visitFn(Task05ASTNode node) {
    String name = node.children.getFirst().getValue();
    Symbol func = new Function(name, null);
    scope.bind(func);
    Scope iScope = new Scope(scope);
    scope.innerScope = iScope;
    scope = iScope;
    scopeList.add(scope); // for printing
    for (int i = 1; i < node.children.size(); i++) {
      visit(node.children.get(i));
    }
    scope = scope.enclosingScope;
    return node;
  }

  public Task05ASTNode visitNumber(Task05ASTNode node) {
    return node;
  }

  public Task05ASTNode visitBoolean(Task05ASTNode node) {
    return node;
  }

  public Task05ASTNode visitString(Task05ASTNode node) {
    return node;
  }

  public Task05ASTNode visitFcall(Task05ASTNode node) {
    String name = node.children.getFirst().getValue();
    Symbol func = scope.resolve(name);
    if (func == null) {
      System.out.println("Error: no such function: " + name);
    }
    if (func instanceof Variable) {
      System.out.println("Error: " + name + " is not a function");
    }
    for (int i = 1; i < node.children.size(); i++) {
      visit(node.children.get(i));
    }
    return node;
  }

  public Task05ASTNode visitLet(Task05ASTNode node) {
    scope.innerScope = new Scope(scope);
    scope = scope.innerScope;
    scopeList.add(scope); // for printing
    for (int i = 0; i <= node.children.size() - 1; i += 2) {
      if (node.children.get(i).getType() == Task05ASTNode.Type.ID) {
        Symbol t = scope.resolve(node.children.get(i + 1).getType().name());
        Symbol var = new Variable(node.children.get(i).getValue(), t.name);
        scope.bind(var);
      } else {
        visit(node.children.get(i));
      }
    }
    scope = scope.enclosingScope;
    return node;
  }

  public Task05ASTNode visitIf(Task05ASTNode node) {
    visitChildren(node);
    return node;
  }

  public Task05ASTNode visitBlock(Task05ASTNode node) {
    scope.innerScope = new Scope(scope);
    scope = scope.innerScope;
    scopeList.add(scope); // for printing
    visitChildren(node);
    scope = scope.enclosingScope;
    return node;
  }
}
