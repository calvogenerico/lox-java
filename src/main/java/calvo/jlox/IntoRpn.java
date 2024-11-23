package calvo.jlox;

public class IntoRpn implements Expr.Visitor<String> {
  public String print(Expr expr) {
    return expr.accept(this);
  }

  @Override
  public String visitBinaryExpr(Expr.Binary expr) {
    return String.format("%s %s %s", expr.left.accept(this), expr.right.accept(this), expr.operator.lexeme);
  }


  @Override
  public String visitGroupingExpr(Expr.Grouping expr) {
    return String.format("(%s)", expr.expression.accept(this));
  }

  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    if (expr.value == null) return "nil";
    return expr.value.toString();
  }

  @Override
  public String visitLogicalExpr(Expr.Logical expr) {
    return "";
  }

  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    return String.format("%s %s", expr.right.accept(this), expr.operator.lexeme);
  }

  @Override
  public String visitAssignExpr(Expr.Assign expr) {
    return "";
  }

  @Override
  public String visitVariableExpr(Expr.Variable expr) {
    return "";
  }
}
