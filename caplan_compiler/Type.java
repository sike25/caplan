abstract public class Type {

    /**
     * Represents an expression's type
     */

    //If type is that of a literal/variable, corresponds to position of the literal/variable in the code
    //If type is result of an operation, corresponds to position of the operator used to create it
    public final int _position;

    /**
     * Creates a Type object to represent the type of variable which is NOT a pointer.
     * 
     * @param name The type of the variable, a <name>.
     */
    public Type (int position) {
        _position = position;
    }

    /**
     * Provide the size of data of this type (in bytes).
     *
     * @return the number of bytes that this type of data consumes.
     */
    abstract public int getSize ();

    /**
     * Construct the register name appropriate for this type's data size.
     *
     * @param baseName The foundational name of the register (e.g., "a", "b", "c", "d").
     * @return the full name of the register for this type's data size.
     */
    abstract public String getSizedRegister (String baseName);

    /**
     * Provide the size annotation used in assembly for this type (byte, word, dword, qword).
     *
     * @return the size annotation to be used in assembly.
     */
    abstract public String getSizeAnnotation ();

    /**
     * Should return an exact copy of this type, except the _position field contains position parameter
     * 
     * @param position The value to be stored in the duplicate's _position field
     */
    abstract public Type duplicateType (int position);

    /**
     * Return the type of variable to which this variable points.
     * Type.getPointedType() is a stub which always returns null.
     * TypePointer.getPointedType() overrides this and returns the type of the variable it points to.
     * @return a Type object if this variable is a pointer, otherwise null
     */
    public Type getPointedType () { return null; }

}
