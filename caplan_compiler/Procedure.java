// =================================================================================================================================
// IMPORTS

import java.util.HashMap;
import java.util.List;
import java.util.Map;
// =================================================================================================================================



// =================================================================================================================================
/**
 * A declared procedure.
 */
abstract public class Procedure extends Declaration {
// =================================================================================================================================


    
    // =============================================================================================================================
    // DATA MEMBERS

    // Parameters for this procedure, in the order declared.
    public final List<VariableDeclaration> _parameters;

    // The local variable table, mapping names to declarations (for both parameters and locals).
    public final Map<String, Declaration>  _localsTable;
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Basic constructor for a Procedure.
     *
     * @param name       The procedure's name token.
     * @param type       The proecdure's return type.
     * @param parameters The parameters.
     */
    public Procedure (Token name, Type type, List<VariableDeclaration> parameters) {
	
	super(name, type);
        _parameters = parameters;

	// Create the locals symbol table.
	_localsTable = new HashMap<String, Declaration>();
	for (Declaration variable : _parameters) {
	    Declaration oldVariable = _localsTable.put(variable._name, variable);
	    if (oldVariable != null) {
		Utility.error("Duplicate variable declaration\nPrevious declaration " +
			       Utility.positionToLineColumn(oldVariable._position) + oldVariable._position + ")",
			      variable._position);
	    }
	}	
	
    } // Procedure ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Bind the local variables of a procedure to the global variables declarations and arguments passed in
     *
     * @param globals    The global variable symbol table.
     * @param procedures The procedure symbol table.
     */
    abstract public void bind (Map<String, Declaration> globals, Map<String, Declaration> procedures);
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Verify correctness properties of this procedure (e.g., all symbol names are bound, type uses in statements match).
     *
     * @return the type produced by evaluation.
     */
    abstract public Type verify ();
    // =============================================================================================================================
    


    // =============================================================================================================================
    /**
     * Provide whether the procedure uses variadic arguments.
     *
     * @return <code>true</code> if the procedure uses variadic arguments; <code>false</code> otherwise.
     */
    abstract public boolean usesVarargs ();
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Generate assembly for this procedure.
     *
     * @return the code-segment assembly for this procedure.
     */
    abstract public String toAssembly ();
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Generate assembly for any statically allocated elements in this procedure.
     *
     * @return the statics-segment assembly for this procedure.
     */
    abstract public String toStatics ();
    // =============================================================================================================================


    
    // =============================================================================================================================
    public String toString() {

	String text = _type.toString() + " " + _name + " ";

	text += "( ";
	for(VariableDeclaration var : _parameters){
	    text += var._name + " ";
	}
	text += ")\n";

	return text;
	
    } // toString ()
    // =============================================================================================================================

    

// =================================================================================================================================
} // Procedure ()
// =================================================================================================================================
