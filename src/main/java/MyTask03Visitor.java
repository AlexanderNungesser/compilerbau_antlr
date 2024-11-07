import java.util.Optional;

public class MyTask03Visitor extends Task03BaseVisitor<String> {

  private int tabLevel = 0;
  private static final String tab = "    "; // 4 Leerzeichen pro Tab

  private String indent() {
    return tab.repeat(tabLevel);
  }

  @Override
  public String visitProgram(Task03Parser.ProgramContext ctx) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < ctx.getChildCount() - 1; i++) {
      sb.append(visit(ctx.getChild(i)));
    }
    return sb.toString();
  }

  @Override
  public String visitAssign(Task03Parser.AssignContext ctx) {
    return indent()
        + ctx.ID().getText()
        + " "
        + ctx.getChild(1).getText()
        + " "
        + visit(ctx.getChild(2))
        + "\n";
  }

  @Override
  public String visitWhile(Task03Parser.WhileContext ctx) {
    StringBuilder sb = new StringBuilder();
    sb.append(indent())
        .append(ctx.getChild(0))
        .append(" ")
        .append(visit(ctx.cond()))
        .append(" ")
        .append(ctx.getChild(2))
        .append("\n");
    tabLevel++;
    for (int i = 3; i < ctx.getChildCount() - 1; i++) {
      sb.append(visit(ctx.getChild(i)));
    }
    tabLevel--;
    sb.append(indent()).append(ctx.getChild(ctx.getChildCount() - 1)).append("\n");
    return sb.toString();
  }

  @Override
  public String visitIf(Task03Parser.IfContext ctx) {
    StringBuilder sb = new StringBuilder();
    Optional<Integer> elseIndex =
        ctx.children.stream()
            .filter(child -> child.getText().equals("else do"))
            .map(child -> ctx.children.indexOf(child))
            .findFirst();
    sb.append(indent())
            .append(ctx.getChild(0))
            .append(" ")
            .append(visit(ctx.cond()))
            .append(" ")
            .append(ctx.getChild(2))
            .append("\n");
    tabLevel++;
    if (elseIndex.isPresent()) {
      for (int i = 3; i < elseIndex.get(); i++) {
        sb.append(visit(ctx.getChild(i)));
      }
      tabLevel--;
      sb.append(indent()).append(ctx.getChild(elseIndex.get())).append("\n");
      tabLevel++;
      for (int i = elseIndex.get() + 1; i < ctx.getChildCount() - 1; i++) {
        sb.append(visit(ctx.getChild(i)));
      }
    } else {
      for (int i = 3; i < ctx.getChildCount() - 2; i++) {
        sb.append(visit(ctx.getChild(i)));
      }
    }
    tabLevel--;
    sb.append(indent()).append(ctx.getChild(ctx.getChildCount() - 1)).append("\n");
    return sb.toString();
  }

  @Override
  public String visitStmt(Task03Parser.StmtContext ctx) {
    return visit(ctx.getChild(0));
  }

  @Override
  public String visitCond(Task03Parser.CondContext ctx) {
    return visit(ctx.getChild(0)) + " " + ctx.getChild(1).getText() + " " + visit(ctx.getChild(2));
  }

  @Override
  public String visitExpr(Task03Parser.ExprContext ctx) {
    if (ctx.getChildCount() == 3) { // Rechenoperation
      return visit(ctx.getChild(0))
          + " "
          + ctx.getChild(1).getText()
          + " "
          + visit(ctx.getChild(2));
    } else {
      return ctx.getText(); // ID, NUM oder STR
    }
  }
}
