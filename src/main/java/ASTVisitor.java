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
    node.addChild(visit(ctx.getChild(0)));
    node.addChild(new ASTNode(ctx.getChild(1).getText(), "ID"));

    if (ctx.getChildCount() == 5) {
      node.addChild(visit(ctx.getChild(3)));
    }
    return node;
  }

  @Override
  public ASTNode visitType(MiniCParser.TypeContext ctx) {
    return new ASTNode(ctx.getChild(0).getText());
  }

  @Override
  public ASTNode visitExpr(MiniCParser.ExprContext ctx) {
    if (ctx.getChildCount() == 1) {
      if (ctx.getChild(0) == ctx.ID()) {
        return new ASTNode(ctx.getChild(0).getText(), "ID");
      }else if (ctx.getChild(0) == ctx.NUMBER()) {
        return new ASTNode(ctx.getChild(0).getText(), "NUMBER");
      }else if (ctx.getChild(0) == ctx.STRING()) {
        return new ASTNode(ctx.getChild(0).getText(), "STRING");
      }else {
        return visit(ctx.getChild(0));
      }
    } else {
      if (ctx.getChild(0).getText().equals("(") && ctx.getChild(2).getText().equals(")")) {
        return visit(ctx.getChild(1));
      }
      ASTNode node = new ASTNode(ctx.getChild(1).getText());
      ASTNode child1 = visit(ctx.getChild(0));
      node.addChild(child1);
      ASTNode child2 = visit(ctx.getChild(2));
      node.addChild(child2);
      return node;
    }
  }

  @Override
  public ASTNode visitFncall(MiniCParser.FncallContext ctx) {
    ASTNode node = new ASTNode(ctx.ID().getText(), "ID");
    node.addChildren(visit(ctx.getChild(2)).children);
    return node;
  }

  @Override
  public ASTNode visitArgs(MiniCParser.ArgsContext ctx) {
    ASTNode node = new ASTNode("");
    for (int i = 0; i < ctx.getChildCount(); i+=2) {
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
    ASTNode node = new ASTNode(ctx.ID().getText(), ctx.getChild(0).getText());
    if (ctx.getChildCount() == 5) {
      node.addChild(visit(ctx.getChild(4)));
    }else{
      node.addChild(visit(ctx.getChild(3)));
      node.addChild(visit(ctx.getChild(5)));
    }
    return node;
  }

  public ASTNode visitParams(MiniCParser.ParamsContext ctx) {
    ASTNode node = new ASTNode("params");
    for (int i = 0; i < ctx.getChildCount(); i+=3) {
      ASTNode child1 = visit(ctx.getChild(i));
      ASTNode child2 = new ASTNode(ctx.getChild(i+1).getText(), child1.getValue());
      node.addChild(child2);
    }
    return node;
  }

  @Override
  public ASTNode visitBlock(MiniCParser.BlockContext ctx) {
    ASTNode node = new ASTNode("block");
    for (int i = 1; i < ctx.getChildCount() - 1; i++) {
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
