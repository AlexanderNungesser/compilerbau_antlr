import java.util.HashMap;
import java.util.Map;

public class Scope {

  public Scope enclosingScope;
  public Map<String, Symbol> symbols = new HashMap<String, Symbol>();

  public Scope() {}

  public Scope(Scope scope) {
    this.enclosingScope = scope;
  }

  public void bind(Symbol symbol) {
    this.symbols.put(symbol.name, symbol);
    symbol.scope = this;
  }

  public Symbol resolve(String name) {
    if (symbols.containsKey(name)) return symbols.get(name);
    try {
      return enclosingScope.resolve(name);
    } catch (Exception e) {
      return null;
    }
  }
}
