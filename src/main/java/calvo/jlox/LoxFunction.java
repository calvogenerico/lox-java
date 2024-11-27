package calvo.jlox;

import java.util.List;

public class LoxFunction implements LoxCallable {
  private final Stmt.Function declaration;

  public LoxFunction(Stmt.Function stmt) {
    this.declaration = stmt;
  }

  @Override
  public int arity() {
    return this.declaration.params.size();
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    Environment env = new Environment(interpreter.globals);

    for (int i = 0; i < declaration.params.size(); i++) {
      env.define(declaration.params.get(i).lexeme,
        arguments.get(i));
    }


    interpreter.executeBlock(declaration.body, env);

    return null;
  }

  @Override
  public String toString() {
    return "<function " + this.declaration.name.lexeme + ">";
  }
}
