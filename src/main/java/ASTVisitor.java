import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.RuleNode;

public class ASTVisitor extends MiniCBaseVisitor<ASTNode> {

    private ASTNode checkNode(ParserRuleContext ctx, ASTNode current) {
        if (ctx.getChildCount() == 0){
            current = new ASTNode(ctx.getText());
        } else if (ctx.getChildCount() == 1) {
            current = visit(ctx.getChild(0));
        } else {
            for (int i = 0; i < ctx.getChildCount() - 1; i++) {
                ASTNode child = visit(ctx.getChild(i));
                if(child != null)
                current.addChild(child);
            }
        }

        return current;
    }

    @Override
    public ASTNode visitProgram(MiniCParser.ProgramContext ctx) {
        ASTNode root = new ASTNode("Program");
        for (int i = 0; i < ctx.getChildCount() - 1; i++) {
            ASTNode child = visit(ctx.getChild(i));
            root.addChild(child);
        }
        return root;
    }



    @Override
    public ASTNode visitStmt(MiniCParser.StmtContext ctx) {
        ASTNode current = new ASTNode(ctx.getText());
        return checkNode(ctx, current);
    }

    @Override
    public ASTNode visitVardecl(MiniCParser.VardeclContext ctx) {
        ASTNode current = new ASTNode(ctx.type().getText(), ctx.getText());
        if (ctx.expr() != null) {
            current.addChild(visit(ctx.expr())); // Initialisierung
        }
        return current;

    }
    @Override
    public ASTNode visitAssign(MiniCParser.AssignContext ctx) {
        ASTNode current = new ASTNode(ctx.getText());
        return checkNode(ctx, current);
    }
    @Override
    public ASTNode visitFndecl(MiniCParser.FndeclContext ctx) {
        ASTNode current = new ASTNode(ctx.type().getText(), ctx.ID().getText());
        current.addChild(visit(ctx.params()));
        current.addChild(visit(ctx.block()));
        return current;
    }
    @Override
    public ASTNode visitParams(MiniCParser.ParamsContext ctx) {
        ASTNode current = new ASTNode(ctx.getText());
        return checkNode(ctx, current);
    }
    @Override
    public ASTNode visitReturn(MiniCParser.ReturnContext ctx) {
        ASTNode current = new ASTNode(ctx.getText());
        current.addChild(visit(ctx.expr())); // Rückgabewert
        return current;
    }
    @Override
    public ASTNode visitFncall(MiniCParser.FncallContext ctx) {
        ASTNode current = new ASTNode( ctx.ID().getText());
        current.addChild(visit(ctx.args())); // Argumente
        return current;
    }
    @Override
    public ASTNode visitArgs(MiniCParser.ArgsContext ctx) {
        return checkNode(ctx, new ASTNode(ctx.getText()));
    }
    @Override
    public ASTNode visitBlock(MiniCParser.BlockContext ctx) {
        return visit(ctx.getChild(1));
    }
    @Override
    public ASTNode visitWhile(MiniCParser.WhileContext ctx) {
        return checkNode(ctx, new ASTNode(ctx.getText()));
    }
    @Override
    public ASTNode visitCond(MiniCParser.CondContext ctx) {
        ASTNode current = new ASTNode("if");
        current.addChild(visit(ctx.expr())); // Bedingung
        current.addChild(visit(ctx.block(0))); // Then-Block
        if (ctx.block(1) != null) {
            current.addChild(visit(ctx.block(1))); // Else-Block
        }
        return current;
    }
    @Override
    public ASTNode visitExpr(MiniCParser.ExprContext ctx) {
        if (ctx.getChildCount() == 3) { // Binäre Operation
            ASTNode opNode = new ASTNode(ctx.getChild(1).getText());
            opNode.addChild(visit(ctx.getChild(0))); // Linker Operand
            opNode.addChild(visit(ctx.getChild(2))); // Rechter Operand
            return opNode;
        } else if (ctx.getChildCount() == 2) { // Unary Operation
            ASTNode opNode = new ASTNode(ctx.getChild(0).getText());
            opNode.addChild(visit(ctx.getChild(1))); // Operand
            return opNode;
        } else if (ctx.fncall() != null) {
            return visit(ctx.fncall());
        } else if (ctx.getChildCount() == 1) { // Terminale
            return new ASTNode(ctx.getText());
        }
        return null;
    }
    @Override
    public ASTNode visitType(MiniCParser.TypeContext ctx) {
        return checkNode(ctx, new ASTNode(ctx.getText()));
    }

}
