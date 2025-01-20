public class TypeVoid extends Type {
    public TypeVoid(int position) {
      super(position);
    }
  
    public int getSize () {

	// void types don't really have a size.
	Utility.error("Attempted to get size of a void type", _position);
	return -1;
	
    }

    public String getSizedRegister (String baseName) {
	Utility.abort("TypeVoid.getSizedRegister(): Should never be called");
	return null;
    }

    public String getSizeAnnotation () {
	Utility.abort("TypeVoid.getSizedRegister(): Should never be called");
	return null;
    }

    public Type duplicateType(int position){
      return new TypeVoid(position);
    }
  
    @Override
    public boolean equals(Object obj) {
      return obj instanceof TypeVoid;
    }
  
    @Override
    public String toString() {
	return "void";
    }

}  
