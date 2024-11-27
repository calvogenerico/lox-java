package calvo.jlox;

import java.util.List;

public class LoxFunction implements LoxCallable {
  private final Stmt.Function declaration;
  private final Environment closure;

  public LoxFunction(Stmt.Function stmt, Environment closure) {
    this.declaration = stmt;
    this.closure = closure;
  }

  @Override
  public int arity() {
    return this.declaration.params.size();
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    Environment env = new Environment(this.closure);

    for (int i = 0; i < declaration.params.size(); i++) {
      env.define(declaration.params.get(i).lexeme,
        arguments.get(i));
    }


    try {
      interpreter.executeBlock(declaration.body, env);
    } catch (ValueReturned returned) {
      return returned.value;
    }

    return null;
  }

  @Override
  public String toString() {
    return "<function " + this.declaration.name.lexeme + ">";
  }
}
