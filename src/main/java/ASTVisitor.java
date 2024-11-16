public class ASTVisitor extends MiniCBaseVisitor<ASTNode> {

  @Override
  public ASTNode visitProgram(MiniCParser.ProgramContext ctx) {
    ASTNode root = new ASTNode("program");
    for (int i = 0; i < ctx.getChildCount() - 1; i++) {
      ASTNode child = visit(ctx.getChild(i));
      root.addChild(child);
    }
    return root;
  }

  @Override
  public ASTNode visitStmt(MiniCParser.StmtContext ctx) {
    return visit(ctx.getChild(0));
  }

  @Override
  public ASTNode visitVardecl(MiniCParser.VardeclContext ctx) {
    ASTNode node = new ASTNode("vardecl");
    // TODO: int i; und int i = 0;
    node.addChild(visit(ctx.getChild(0)));
    node.addChild(new ASTNode(ctx.getChild(1).getText()));
    node.addChild(visit(ctx.getChild(3)));
    return node;
  }

  @Override
  public ASTNode visitType(MiniCParser.TypeContext ctx) {
    return new ASTNode(ctx.getChild(0).getText());
  }

  @Override
  public ASTNode visitExpr(MiniCParser.ExprContext ctx) {
    if (ctx.getChildCount() == 1) {
      return new ASTNode(ctx.getChild(0).getText());
    } else {
      ASTNode node = new ASTNode(ctx.getChild(1).getText());
      if (ctx.getChild(0).getText().equals("(") && ctx.getChild(2).getText().equals(")")) {
        return node;
      }
      ASTNode child1 = visit(ctx.getChild(0));
      ASTNode child2 = visit(ctx.getChild(2));
      node.addChild(child1);
      node.addChild(child2);
      return node;
    }
  }

  @Override
  public ASTNode visitFncall(MiniCParser.FncallContext ctx) {
    ASTNode node = new ASTNode("fncall");
    node.addChild(new ASTNode(ctx.getChild(0).getText()));
    node.addChild(visit(ctx.getChild(2)));
    return node;
  }

  @Override
  public ASTNode visitArgs(MiniCParser.ArgsContext ctx) {
    ASTNode node = new ASTNode("args");
    for (int i = 0; i < ctx.getChildCount() - 1; i++) {
      if (i % 2 != 0) {
        continue;
      }
      node.addChild(visit(ctx.getChild(i)));
    }
    return node;
  }

  @Override
  public ASTNode visitAssign(MiniCParser.AssignContext ctx) {
    ASTNode node = new ASTNode("assign");
    node.addChild(new ASTNode(ctx.getChild(0).getText()));
    node.addChild(visit(ctx.getChild(2)));
    return node;
  }

  @Override
  public ASTNode visitFndecl(MiniCParser.FndeclContext ctx) {
    ASTNode node = new ASTNode("fndecl");
    node.addChild(visit(ctx.getChild(0)));
    node.addChild(new ASTNode(ctx.getChild(1).getText()));
    node.addChild(visit(ctx.getChild(3)));
    node.addChild(visit(ctx.getChild(5)));
    return node;
  }

  public ASTNode visitParams(MiniCParser.ParamsContext ctx) {
    ASTNode node = new ASTNode("params");
    int id = 1;
    for (int i = 0; i < ctx.getChildCount() - 2; i++) {
      if (ctx.getChild(i + 1).getText().equals(",")) {
        node.addChild(new ASTNode(ctx.getChild(i).getText()));
        continue;
      }
      if (ctx.getChild(i).getText().equals(",")) {
        continue;
      }
      node.addChild(visit(ctx.getChild(i)));
    }
    return node;
  }

  @Override
  public ASTNode visitBlock(MiniCParser.BlockContext ctx) {
    ASTNode node = new ASTNode("block");
    for (int i = 0; i < ctx.getChildCount() - 1; i++) {
      if (i == 0 || i == ctx.getChildCount() - 1) {
        continue;
      }
      node.addChild(visit(ctx.getChild(i)));
    }
    return node;
  }

  @Override
  public ASTNode visitWhile(MiniCParser.WhileContext ctx) {
    ASTNode node = new ASTNode("while");
    node.addChild(visit(ctx.getChild(2)));
    node.addChild(visit(ctx.getChild(4)));
    return node;
  }

  @Override
  public ASTNode visitCond(MiniCParser.CondContext ctx) {
    ASTNode node = new ASTNode("cond");
    node.addChild(visit(ctx.getChild(2)));
    node.addChild(visit(ctx.getChild(4)));
    if (ctx.getChildCount() == 7) {
      node.addChild(visit(ctx.getChild(6)));
    }
    return node;
  }

  @Override
  public ASTNode visitReturn(MiniCParser.ReturnContext ctx) {
    return visit(ctx.getChild(1));
  }
}
