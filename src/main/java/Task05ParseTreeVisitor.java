public class Task05ParseTreeVisitor extends MiniLispBBaseVisitor<Task05ASTNode> {

  @Override
  public Task05ASTNode visitProgram(MiniLispBParser.ProgramContext ctx) {
    Task05ASTNode root = new Task05ASTNode(Task05ASTNode.Type.PROGRAM);
    for (int i = 0; i < ctx.getChildCount() - 1; i++) {
      Task05ASTNode child = visit(ctx.getChild(i));
      root.addChild(child);
    }
    return root;
  }

  @Override
  public Task05ASTNode visitExpr(MiniLispBParser.ExprContext ctx) {
    return visitChildren(ctx);
  }

  @Override
  public Task05ASTNode visitLiteral(MiniLispBParser.LiteralContext ctx) {
    Task05ASTNode node = null;
    if (ctx.getChild(0) == ctx.NUMBER()) {
      node = new Task05ASTNode(ctx.getChild(0).getText(), Task05ASTNode.Type.NUMBER);
    } else if (ctx.getChild(0) == ctx.STRING()) {
      node = new Task05ASTNode(ctx.getChild(0).getText(), Task05ASTNode.Type.STRING);
    } else if (ctx.getChild(0) == ctx.TRUE() || ctx.getChild(0) == ctx.FALSE()) {
      node = new Task05ASTNode(ctx.getChild(0).getText().toLowerCase(), Task05ASTNode.Type.BOOLEAN);
    }
    return node;
  }

  @Override
  public Task05ASTNode visitSymbol(MiniLispBParser.SymbolContext ctx) {
    return new Task05ASTNode(ctx.getChild(0).getText(), Task05ASTNode.Type.ID);
  }

  @Override
  public Task05ASTNode visitList(MiniLispBParser.ListContext ctx) {
    Task05ASTNode node = new Task05ASTNode(Task05ASTNode.Type.LIST);
    ;
    for (int i = 2; i < ctx.getChildCount() - 1; i++) {
      Task05ASTNode child = visit(ctx.getChild(i));
      node.addChild(child);
    }
    return node;
  }

  @Override
  public Task05ASTNode visitDef(MiniLispBParser.DefContext ctx) {
    Task05ASTNode node = new Task05ASTNode(Task05ASTNode.Type.DEF);
    node.addChild(visit(ctx.getChild(2)));
    node.addChild(visit(ctx.getChild(3)));
    return node;
  }

  @Override
  public Task05ASTNode visitFn(MiniLispBParser.FnContext ctx) {
    Task05ASTNode node = new Task05ASTNode(Task05ASTNode.Type.FN);
    for (int i = 2; i < ctx.getChildCount() - 1; i++) {
      if (ctx.symbol().contains(ctx.getChild(i)) || ctx.expr().contains(ctx.getChild(i))) {
        node.addChild(visit(ctx.getChild(i)));
      }
    }
    return node;
  }

  @Override
  public Task05ASTNode visitFcall(MiniLispBParser.FcallContext ctx) {
    Task05ASTNode node = new Task05ASTNode(Task05ASTNode.Type.FCALL);
    if (ctx.getChild(1) == ctx.ID()) {
      node.addChild(new Task05ASTNode(ctx.getChild(1).getText(), Task05ASTNode.Type.ID));
    } else if (ctx.getChild(1) == ctx.OP()) {
      node.addChild(new Task05ASTNode(ctx.getChild(1).getText(), Task05ASTNode.Type.OP));
    }
    for (int i = 2; i < ctx.getChildCount() - 1; i++) {
      if (ctx.expr().contains(ctx.getChild(i))) {
        node.addChild(visit(ctx.getChild(i)));
      }
    }
    return node;
  }

  @Override
  public Task05ASTNode visitLet(MiniLispBParser.LetContext ctx) {
    Task05ASTNode node = new Task05ASTNode(Task05ASTNode.Type.LET);
    for (int i = 2; i < ctx.getChildCount() - 1; i++) {
      if (ctx.binding().contains(ctx.getChild(i))) {
        node.addChildren(visit(ctx.getChild(i)).children);
      } else if (ctx.getChild(i) == ctx.expr()) {
        node.addChild(visit(ctx.getChild(i)));
      }
    }
    return node;
  }

  @Override
  public Task05ASTNode visitBinding(MiniLispBParser.BindingContext ctx) {
    Task05ASTNode node = new Task05ASTNode(null, null);
    for (int i = 0; i < ctx.getChildCount(); i++) {
      node.addChild(visit(ctx.getChild(i)));
    }
    return node;
  }

  @Override
  public Task05ASTNode visitIf(MiniLispBParser.IfContext ctx) {
    Task05ASTNode node = new Task05ASTNode(Task05ASTNode.Type.IF);
    for (int i = 2; i < ctx.getChildCount() - 1; i++) {
      node.addChild(visit(ctx.getChild(i)));
    }
    return node;
  }

  @Override
  public Task05ASTNode visitBlock(MiniLispBParser.BlockContext ctx) {
    Task05ASTNode node = new Task05ASTNode(Task05ASTNode.Type.BLOCK);
    if (ctx.getChildCount() == 1) {
      return visit(ctx.getChild(0));
    }
    for (int i = 2; i < ctx.getChildCount() - 1; i++) {
      node.addChild(visit(ctx.getChild(i)));
    }
    return node;
  }
}
