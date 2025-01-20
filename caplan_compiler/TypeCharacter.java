public class TypeCharacter extends Type {

    public TypeCharacter (Token token) {
	super(token._position);
    }
    
    public TypeCharacter (int position) {
      super(position);
    }
  
    public int getSize () {
      return 1;
    }

    public String getSizedRegister (String baseName) {
	return baseName + 'l';
    }

    public String getSizeAnnotation () {
	return "byte";
    }

    public Type duplicateType(int position){
      return new TypeCharacter(position);
    }
  
    @Override
    public boolean equals(Object obj) {
	return obj instanceof TypeCharacter;
    }
  
    @Override
    public String toString() {
	return "char";
    }

}  
