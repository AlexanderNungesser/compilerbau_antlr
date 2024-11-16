public class BuiltIn extends Symbol {
  enum Type {
    INT,
    BOOL,
    STRING
  }

  public BuiltIn(String name) {
    //        if(!name.equals(Type.INT.toString()) || !name.equals(Type.BOOL.toString()) ||
    // !name.equals(Type.STRING.toString())){
    //            System.out.println("Error: no such type: " + name);
    //            return;
    //        }
    super(name, null);
  }
}
