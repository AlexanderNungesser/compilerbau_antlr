import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.RuleNode;

public class ASTVisitor extends MiniCBaseVisitor<ASTNode> {

    private ASTNode checkNode(ParserRuleContext ctx) {
        ASTNode current;
        if (ctx.getChildCount() == 0){
            current = new ASTNode(ctx.getText());
        } else if (ctx.getChildCount() == 1) {
            current = visit(ctx.getChild(0));
        } else {
            current = new ASTNode(ctx.getText());
            for (int i = 0; i < ctx.getChildCount() - 1; i++) {
                ASTNode child = visit(ctx.getChild(i));
                if(child != null) {
                    current.addChild(child);
                }
            }
        }

        return current;
    }

    @Override
    public ASTNode visitProgram(MiniCParser.ProgramContext ctx) {
        ASTNode root = new ASTNode(ctx.getText());
        for (int i = 0; i < ctx.getChildCount() - 1; i++) {
            ASTNode child = visit(ctx.getChild(i));
            root.addChild(child);
        }
        return root;
    }



    @Override
    public ASTNode visitStmt(MiniCParser.StmtContext ctx) {
        return checkNode(ctx); // Standard-Visitor-Verhalten
    }

    @Override
    public ASTNode visitVardecl(MiniCParser.VardeclContext ctx) {
        return checkNode(ctx);
    }
    @Override
    public ASTNode visitAssign(MiniCParser.AssignContext ctx) {
        return checkNode(ctx);
    }
    @Override
    public ASTNode visitFndecl(MiniCParser.FndeclContext ctx) {
        return checkNode(ctx);
    }
    @Override
    public ASTNode visitParams(MiniCParser.ParamsContext ctx) {
        return checkNode(ctx);
    }
    @Override
    public ASTNode visitReturn(MiniCParser.ReturnContext ctx) {
        return checkNode(ctx);
    }
    @Override
    public ASTNode visitFncall(MiniCParser.FncallContext ctx) {
        return checkNode(ctx);
    }
    @Override
    public ASTNode visitArgs(MiniCParser.ArgsContext ctx) {
        return checkNode(ctx);
    }
    @Override
    public ASTNode visitBlock(MiniCParser.BlockContext ctx) {
        return checkNode(ctx);
    }
    @Override
    public ASTNode visitWhile(MiniCParser.WhileContext ctx) {
        return checkNode(ctx);
    }
    @Override
    public ASTNode visitCond(MiniCParser.CondContext ctx) {
        return checkNode(ctx);
    }
    @Override
    public ASTNode visitExpr(MiniCParser.ExprContext ctx) {
        return checkNode(ctx);
    }
    @Override
    public ASTNode visitType(MiniCParser.TypeContext ctx) {
        return checkNode(ctx);
    }

}
