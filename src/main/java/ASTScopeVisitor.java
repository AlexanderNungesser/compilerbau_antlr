public class ASTScopeVisitor extends ASTVisitor{

    Scope scope;

    public ASTNode visitChildren(ASTNode node) {
        for(ASTNode child : node.children){
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
        return node;
    }

    public ASTNode visitVardecl(ASTNode node) {
        Symbol t = scope.resolve(node.children.get(0).getValue());
        Symbol var = new Symbol(node.children.get(1).getValue(), t.name);
        scope.bind(var);
        return node;
    }

    
    public ASTNode visitType(ASTNode node) {
        return node;
    }

    
    public ASTNode visitExpr(ASTNode node) {
        if (node.children.size() == 1) {
            String name = node.children.get(0).getValue();
            Symbol var = scope.resolve(name);
            if (var == null) {
                System.out.println("Error: no such var: " + name);
            }
        }
        return node;
    }

    
    public ASTNode visitFncall(ASTNode node) {
        return node;
    }

    
    public ASTNode visitArgs(ASTNode node) {
        return node;
    }

    
    public ASTNode visitAssign(ASTNode node) {
        return node;
    }

    
    public ASTNode visitFndecl(ASTNode node) {
        return node;
    }

    public ASTNode visitParams(ASTNode node) {
        return node;
    }

    
    public ASTNode visitBlock(ASTNode node) {
        scope = new Scope(scope);
        visitChildren(node);
        scope = scope.enclosingScope;
        return node;
    }

    
    public ASTNode visitWhile(ASTNode node) {
        return node;
    }

    
    public ASTNode visitCond(ASTNode node) {
        return node;
    }

    
    public ASTNode visitReturn(ASTNode node) {
        return node;
    }
}
