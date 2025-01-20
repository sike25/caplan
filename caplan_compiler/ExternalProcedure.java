// =================================================================================================================================
// IMPORTS

import java.util.List;
import java.util.Map;
// =================================================================================================================================



// =================================================================================================================================
/**
 * The declaration of an externally defined procedure.
 */
public class ExternalProcedure extends Procedure {
// =================================================================================================================================


    
    // =============================================================================================================================
    // DATA MEMBERS

    /** Whether this procedure uses variadic arguments. */
    private boolean _usesVarargs;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Basic constructor for an ExternalProcedure.
     *
     * @param name       The procedure's name.
     * @param type       The proecdure's return type.
     * @param parameters The parameters.
     * @param locals     The local variables.
     */
    public ExternalProcedure (Token name, Type type, List<VariableDeclaration> parameters) {

        super(name, type, parameters);

	// Assume varargs aren't used by default; this can change during verify().
	_usesVarargs = false;

    } // ExternalProcedure
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Provide whether the procedure uses variadic arguments.
     *
     * @return <code>true</code> if the procedure uses variadic arguments; <code>false</code> otherwise.
     */
    public boolean usesVarargs () {

	return _usesVarargs;

    } // usesVarargs ()
    // =============================================================================================================================    


    
    // =============================================================================================================================
    /**
     * Bind the local variables of a procedure to the global variables declarations and arguments passed in.  We cannot bind on
     * external procedures.
     *
     * @param globals    The global variable symbol table.
     * @param procedures The procedure symbol table.
     */
    public void bind (Map<String, Declaration> globals, Map<String, Declaration> procedures) {}
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Verify correctness properties of this procedure.  For external procedures, check that any use of <code>etcetera</code> is at
     * the end of the parameter list.
     *
     * @return the type produced by evaluation.
     */
    public Type verify () {

	// Is etcetera present?
	for (int i = 0; i < _parameters.size(); i += 1) {
	    VariableDeclaration parameter = _parameters.get(i); 
	    if (parameter instanceof EtceteraDecl) {

		// It is, and it must come at the end.
		if (i != _parameters.size() - 1) {
		    Utility.error("Parameter list may only use etcetera at the end", parameter._position);
		}

		_usesVarargs = true;
		
	    }
	}
	
	return _type;
	
    }
    // =============================================================================================================================
    


    // =============================================================================================================================
    /**
     * Generate assembly for this procedure.  Externally defined procedures yield no assembly.
     *
     * @return the code-segment assembly for this procedure.
     */
    public String toAssembly () {

	return "\textern\t" + _name + "\n";

    } // toAssembly ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Generate assembly for any statically allocated elements in this procedure.  Externally defined procedures have no static
     * elements.
     *
     * @return the statics-segment assembly for this procedure.
     */
    public String toStatics () {

	return new String();

    } // toStatics ();
    // =============================================================================================================================
    


    // =============================================================================================================================
    public String toString() {

	String text = "extern " + super.toString();
	return text;
	
    }
    // =============================================================================================================================



// =================================================================================================================================
} // ExternalProcedure
// =================================================================================================================================
