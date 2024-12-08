import java.util.HashSet;
import java.util.Set;

public class Task05ASTTypeCheckVisitor extends Task05ASTScopeVisitor {
  Scope scope;
  Set<Scope> visitedScopes = new HashSet<Scope>();

  public Task05ASTTypeCheckVisitor(Scope scope) {
    this.scope = scope;
  }

  public Task05ASTNode visit(Task05ASTNode node) {
    switch (node.getType()) {
      case Task05ASTNode.Type.FCALL:
        visitFcall(node);
        break;
      case Task05ASTNode.Type.BLOCK:
        visitBlock(node);
        break;
      case Task05ASTNode.Type.LET:
        visitLet(node);
        break;
      case Task05ASTNode.Type.FN:
        visitFn(node);
        break;
      default:
        visitChildren(node);
        break;
    }
    return node;
  }

  public Task05ASTNode visitChildren(Task05ASTNode node) {
    if (node.children.isEmpty()) return node;
    for (Task05ASTNode child : node.children) {
      visit(child);
    }
    return node;
  }

  private String isID(Task05ASTNode node) {
    if (node.getType() == Task05ASTNode.Type.ID) {
      Symbol sym1 = this.scope.resolve(node.getValue());
      return sym1.type;
    } else {
      return node.getType().name();
    }
  }

  public Task05ASTNode visitFcall(Task05ASTNode node) {
    if (this.scope.resolve(node.children.getFirst().getValue()) instanceof BuiltIn) {

      switch (node.children.getFirst().getValue()) {
        case "print":
          if (node.children.size() > 2) {
            System.out.println("Error: to much parameters");
          } else if (!isID(node.children.get(1)).equals(Task05ASTNode.Type.STRING.name())) {
            System.out.println(
                "Error: parameter must be of type " + Task05ASTNode.Type.STRING.name());
          } else {
            return node;
          }
          break;
        case "str":
          return node;
        case "head", "tail":
          if (!isID(node.children.get(1)).equals(Task05ASTNode.Type.LIST.name())) {
            return node;
          } else {
            System.out.println(
                "Error: parameter must be of type " + Task05ASTNode.Type.LIST.name());
          }
          break;
        case "nth":
          if (isID(node.children.get(1)).equals(Task05ASTNode.Type.LIST.name())) {
            if (isID(node.children.get(2)).equals(Task05ASTNode.Type.NUMBER.name())) {
              return node;
            } else {
              System.out.println(
                  "Error: parameter must be of type " + Task05ASTNode.Type.NUMBER.name());
            }
          } else {
            if (!isID(node.children.get(2)).equals(Task05ASTNode.Type.NUMBER.name())) {
              System.out.println(
                  "Error: parameter must be of type " + Task05ASTNode.Type.NUMBER.name());
            }
            System.out.println(
                "Error: parameter must be of type " + Task05ASTNode.Type.LIST.name());
          }
          break;
      }

      String s1;
      String s2;

      s1 = isID(node.children.get(1));
      s2 = isID(node.children.get(2));

      if (s1 == null || s2 == null) return node;

      if (!s1.equals(s2)) {
        System.out.println("Error: cannot compare type " + s1 + " with type " + s2);
      }

    } else {
      visitChildren(node);
    }
    return node;
  }

  public Task05ASTNode visitFn(Task05ASTNode node) {
    return visitScopes(node);
  }

  public Task05ASTNode visitLet(Task05ASTNode node) {
    return visitScopes(node);
  }

  public Task05ASTNode visitBlock(Task05ASTNode node) {
    return visitScopes(node);
  }

  private Task05ASTNode visitScopes(Task05ASTNode node) {
    for (Scope scope : this.scope.innerScopes) {
      if (!visitedScopes.contains(scope)) {
        this.scope = scope;
        visitChildren(node);
        this.scope = this.scope.enclosingScope;
        visitedScopes.add(scope);
      }
    }
    return node;
  }
}
