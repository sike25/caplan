public class TypeBoolean extends Type {

    public TypeBoolean(int position) {
	super(position);
    }
  
    public TypeBoolean (Token token) {
	super(token._position);
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
	return new TypeBoolean(position);
    }
  
    @Override
    public boolean equals (Object obj) {
	return obj instanceof TypeBoolean;
    }
  
    @Override
    public String toString() {
	return "bool";
    }

}  
