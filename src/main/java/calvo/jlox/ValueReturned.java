package calvo.jlox;

public class ValueReturned extends RuntimeException {
  public final Object value;

  public ValueReturned(Object value) {
    super(null, null, false, false);
    this.value = value;
  }
}
