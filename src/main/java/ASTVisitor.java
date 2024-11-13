import org.antlr.v4.runtime.ParserRuleContext;

public class ASTVisitor extends MiniCBaseVisitor<ASTNode> {

    private ASTNode checkNode(ParserRuleContext ctx) {
        ASTNode current;
        if (ctx.getChildCount() == 1) {
            current = visit(ctx.getChild(0));
        } else {
            current = new ASTNode(ctx.getText());
            for (int i = 0; i < ctx.getChildCount() - 1; i++) {
                ASTNode child = visit(ctx.getChild(i));
                current.addChild(child);
            }
        }

        return current;
    }

    public ASTNode visit(ParserRuleContext ctx) {
        return checkNode(ctx);
    }
}