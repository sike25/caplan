// =================================================================================================================================
// IMPORTS

import java.util.Map;
// =================================================================================================================================




// =================================================================================================================================
/**
 * Variable object.
 */
public class Variable extends Expression {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS
   
    /** The name of the variable; used later for binding. */
    public final String _name;

    /** The declaration of the named variable. */
    private VariableDeclaration _declaration;
    // =============================================================================================================================

    

    // =============================================================================================================================
    /**
     * Create a Variable object from a given token (assumed to be good).
     *
     * @param token The token representing the variable used.
     */
    public Variable (Token token) {

        super(token._position);
        _name        = token._text;
	_declaration = null;
	
    } // Variable ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Evaluates whether or not a reference to this expression can be made
     * (all variables can have references)
     *
     * @return <code>true</code>
     */
    public boolean hasReference () {

        return true;
	
    } // hasReference ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Bind each variable used to its declaration.  Signal an error if no matching declaration is found.
     *
     * @param procedure The procedure to which this expression belongs.
     * @param symbols The symbol table of variable declarations.
     */
    public void bind (Procedure procedure, Map<String, Declaration> symbols) {

	setProcedure(procedure);
	Declaration declaration = symbols.get(_name);
	if (declaration != null) {
	    try {
		_declaration = (VariableDeclaration)declaration;
	    } catch (ClassCastException e) {
		Utility.error("Cannot use a procedure as a variable", _position);
	    }
	}

    } // bind ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Verify that variables have a valid type
     * Returns a Type object corresponding to the type of the Variable
     */
    public Type verify () {

	if (_declaration == null) {
	    Utility.error("Use of undeclared variable", _position);
	}
	
	Type type = _declaration._type;
	if (type == null) Utility.abort("Variable Declaration has no type: " + _declaration._position);
    
	Type void_type = new TypeVoid(_position);
	if (void_type.equals(type)) Utility.error("Cannot define variable of void type", _declaration._position);

	return type;
	
    } // verify ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Verify semantic validity as an l-value
     *
     * @return the type to which the expression evaluates.
     */
    public Type verify_l () {

	Type type = _declaration._type;
	if (type == null) Utility.abort("Variable Declaration has no type: " + _declaration._name);
	
	Type void_type = new TypeVoid(_position);
	if (void_type.equals(type)) Utility.error("Cannot define variable of void type", _declaration._position);

	return type;
    
    } // verify_l ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly to use this variable as an r-value.
     */
    public String toAssembly () {

	// Get the correct type of register for this data type.
	String register = getType().getSizedRegister("a");

	// Special case: x86-64, for half-word or byte values, does not clear upper bytes of the register, so we clear it here.
	String assembly = ( getType().getSize() < Utility._dwordSize ?
			    "\txor\trax,\trax\t; Clear register for small value\n" :
			    "" );

	// Load the value into the register, then push that value onto the stack
	assembly += ( "\tmov\t" + register + ",\t" + _declaration.toAssembly() + "\t; Load " + _name + '\n' +
		      "\tpush\trax\t\t; Push as whole register\n" );

	return assembly;
	
    } // toAssembly ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Generate assembly to use this variable as an l-value.
     */
    public String toAssembly_l () {

	return _declaration.toAssembly_l();

    } // toAssembly_l ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Return the type of produced by evaluating this expression.
     *
     * @return the type when evaluated.
     */
    public Type getType () {

	if (_declaration == null) {
	    Utility.error(_name + " is unbound, type unavailable", _position);
	}
	return _declaration._type;

    } // getType ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    public String toString () {
	
        return _name;
	
    }
    // =============================================================================================================================
    


// =================================================================================================================================    
} // class Variable
// =================================================================================================================================
