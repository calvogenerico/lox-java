import calvo.jlox.Expr;
import calvo.jlox.IntoRpn;
import calvo.jlox.Token;
import calvo.jlox.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestZeroOne {
  @Test
  void addition() {
    // 1 + 2 * 3 -> 1 2 3 * +

    Expr expression = new Expr.Binary(
      new Expr.Literal(1),
      new Token(TokenType.STAR, "+", null, 1),
      new Expr.Binary(
        new Expr.Literal(2),
        new Token(TokenType.STAR, "*", null, 1),
        new Expr.Literal(3)
      ));

    String res = new IntoRpn().print(expression);
    assertEquals("1 2 3 * +", res);
  }
}
