public class TypePointer extends Type {
    
    private Type _pointedType;
  
    public TypePointer(int position, Type pointedType) {
      super(position);
      _pointedType = pointedType;
    }
  
    public TypePointer (Token token, Type pointedType) {
	super(token._position);
	_pointedType = pointedType;
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
      return new TypePointer(position, _pointedType);
    }
  
    @Override
    public Type getPointedType() {
      return _pointedType;
    }
  
    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof TypePointer))
          return false;
      TypePointer p = (TypePointer) obj;
      return getPointedType().equals(p.getPointedType());
    }
  
    @Override
    public String toString() {
	return getPointedType().toString() + "*";
    }

}  
