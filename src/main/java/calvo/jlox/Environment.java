package calvo.jlox;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Environment {
  private final Environment enclosing;
  private final Map<String, Object> values = new HashMap<>();

  Environment() {
    enclosing = null;
  }

  Environment(Environment enclosing) {
    this.enclosing = enclosing;
  }


  void define(String name, Object value) {
    values.put(name, value);
  }

  Object get(Token name) {
    if (values.containsKey(name.lexeme)) {
      return values.get(name.lexeme);
    }

    if (enclosing != null) {
      return this.enclosing.get(name);
    }

    throw new RuntimeError(name,
      "Undefined variable '" + name.lexeme + "'.");
  }

  public void assign(Token name, Object value) {
    if (values.containsKey(name.lexeme)) {
      values.put(name.lexeme, value);
    } else if (enclosing != null) {
      enclosing.assign(name, value);
    } else {
      throw new RuntimeError(name,
        "Undefined variable '" + name.lexeme + "'.");
    }
  }

  public Object getAt(Integer distance, String name) {
    return ancestor(distance).values.get(name);
  }

  private Environment ancestor(Integer distance) {
    Environment environment = this;
    for (int i = 0; i < distance; i++) {
      // This is not needed but makes the linter happy.
      assert environment != null;
      environment = environment.enclosing;
    }

    return environment;
  }

  public void assignAt(Integer distance, Token name, Object value) {
    ancestor(distance).values.put(name.lexeme, value);
  }
}
