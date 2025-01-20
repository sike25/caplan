// =================================================================================================================================
// IMPORTS
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// =================================================================================================================================



// =================================================================================================================================
/**
 * An object storing data about procedures (name, return type, parameter list, local declaration list)
 */
public class InternalProcedure extends Procedure {
// =================================================================================================================================

    
    
    // =============================================================================================================================
    // DATA MEMBERS

    // Local variables declared in this procedure, in the order declared.
    public final List<VariableDeclaration> _locals;

    /** The body statement. */
    public final Statement _body;

    /** The size of a single variable on the stack, in bytes. */
    private final int VARIABLE_SIZE_BYTES = Utility._bytesPerWord;

    /** Per C calling conventions, the first 6 arguments go in these registers, in order. */
    private final String[] ARGUMENT_REGISTERS = {
        "rdi", "rsi", "rdx", "rcx", "r8", "r9"
    };    
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Basic constructor for an InternalProcedure.
     *
     * @param name       The procedure's name.
     * @param type       The proecdure's return type.
     * @param parameters The parameters.
     * @param locals     The local variables.
     * @param body       The code body.
     */
    public InternalProcedure (Token name,
			      Type type,
			      List<VariableDeclaration> parameters,
			      List<VariableDeclaration> locals,
			      Statement body){

        super(name, type, parameters);

	_locals = locals;
	for (Declaration variable : _locals) {
	    Declaration oldVariable = _localsTable.put(variable._name, variable);
	    if (oldVariable != null) {
		Utility.error("Duplicate variable declaration\nPrevious declaration " +
			       Utility.positionToLineColumn(oldVariable._position) + oldVariable._position + ")",
			      variable._position);
	    }
	}
	
	_body = body;

    } // InternalProcedure ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Provide whether the procedure uses variadic arguments.  Internal procedures do not have this capability.
     *
     * @return <code>true</code> if the procedure uses variadic arguments; <code>false</code> otherwise.
     */
    public boolean usesVarargs () {

	return false;

    } // usesVarargs ()
    // =============================================================================================================================    


    
    // =============================================================================================================================
    /**
     * Bind the local variables of a procedure to the global variables declarations and arguments passed in
     *
     * @return the statics-segment assembly for this procedure.
     */
    public void bind (Map<String, Declaration> globals, Map<String, Declaration> procedures) {

	// Implementing scoping rules: locals first, then globals.
        _body.bind(this, _localsTable);
        _body.bind(this, globals);
        _body.bind(this, procedures);
	
    } // bind ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Verify 
     *
     * @return the type produced by evaluation.
     */
    public Type verify () {

	// Is etcetera present?
	for (int i = 0; i < _parameters.size(); i += 1) {
	    VariableDeclaration parameter = _parameters.get(i); 
	    if (parameter instanceof EtceteraDecl) {

		// It is, and that's invalid for an internal procedure.
		Utility.error("Internal procedures may not use etcetera", parameter._position);
		
	    }
	}	
	
	return _body.verify();
	
    } // verify ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly for this procedure.
     *
     * @return the code-segment assembly for this procedure.
     */
    public String toAssembly () {

        // Create function entry label.
        String assembly = _name + ":\n";

	// Preserve the base pointer.
	assembly += "\n\t; Callee prologue (" + _name + ")\n";
	assembly += "\tpush\trbp\t\t; Preserve rbp\n";

	// Push the locals and parameters.
        int stackVariableTotalSize = (_locals.size() + _parameters.size()) * VARIABLE_SIZE_BYTES;
        int currentOffset = stackVariableTotalSize;
        for (int i = _locals.size() - 1; i >= 0; i--) { // Reverse order because stack is LIFO
            LocalVariableDecl var = (LocalVariableDecl)_locals.get(i);
            currentOffset -= VARIABLE_SIZE_BYTES;
            var.setOffset(currentOffset);
            Utility.debug(2, "Program.toAssembly(): Pushing " + var + " at +" + currentOffset);
            assembly += "\tpush\tqword 0\t\t; Initialize variable " + var + " to 0\n";
        }

        for (int i = _parameters.size() - 1; i >= 0; i--) { // Reverse order because stack is LIFO
            LocalVariableDecl var = (LocalVariableDecl)_parameters.get(i);
            currentOffset -= VARIABLE_SIZE_BYTES;
            var.setOffset(currentOffset);
            Utility.debug(2, "Program.toAssembly(): Pushing " + var + " at +" + currentOffset);

            // Get which register contains this argument's initial value
            String register = ARGUMENT_REGISTERS[i];

            assembly += "\tpush\t" + register + "\t\t; Initialize variable " + var + " to arg #" + i + " from register " + register + "\n";
        }

	// Move the base pointer into this fram.
        assembly += "\tmov\trbp,\trsp\t; Update base pointer\n\n\t ; Callee body (" + _name + ")";

        // Generate assembly code for the procedure body
        assembly += _body.toAssembly();

        // Execution falls through to return label if there is no explicit return statement.
        // It is assumed that the verifier has confirmed that this does not occur if a value must be returned.
        assembly += "return_" + _name + ":\n";

        assembly += ( "\n\t; Callee epilogue (" + _name + ")\n" +
                      "\tmov\trsp,\trbp\t; Pop all temp results\n" +
                      "\tadd\trsp,\t" + stackVariableTotalSize + "\t; Pop variables\n" +
                      "\tpop\trbp\t\t; Restore base pointer\n" +
                      "\tret\n"
        );

        return assembly;

    } // toAssembly ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Generate assembly for any statically allocated elements in this procedure.
     *
     * @return the statics-segment assembly for this procedure.
     */
    public String toStatics () {

	// Define any statics contained in the procedure body.
	return _body.toStatics();

    } // toStatics ();
    // =============================================================================================================================
    


    // =============================================================================================================================
    public String toString() {

	String text = "proc " + super.toString();

	text += "[ ";
	for(VariableDeclaration var : _locals){
	    text += var + " ";
	}
	text += "]\n";

	text += _body + "\n";

	return text;
	
    } // toString ()
    // =============================================================================================================================

    

// =================================================================================================================================
} // class InternalProcedure
// =================================================================================================================================
