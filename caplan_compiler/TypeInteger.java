public class TypeInteger extends Type {

    public TypeInteger(int position) {
      super(position);
    }
  
    public TypeInteger (Token token) {
	super(token._position);
    }

    public int getSize () {
      return Utility._bytesPerWord;
    }

    public String getSizedRegister (String baseName) {
	return 'r' + baseName + 'x';
    }

    public String getSizeAnnotation () {
	return "qword";
    }

    public Type duplicateType(int position){
      return new TypeInteger(position);
    }
  
    @Override
    public boolean equals(Object obj) {
      return obj instanceof TypeInteger;
    }
  
    @Override
    public String toString() {
	return "int";
    }

}  
