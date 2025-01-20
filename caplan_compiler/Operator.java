// =================================================================================================================================
// IMPORTS

import java.util.Map;
// =================================================================================================================================



// =================================================================================================================================
/**
 * An operator which, when applied to operands, yeilds a value.
 */
abstract public class Operator {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS

    /** The position in the source at which this operator appears. */
    public final int   _position;

    /** The token that specified this operator. */
    public final Token _token;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Create an operator.
     *
     * @param token The token that specifies this operator.
     */
    public Operator (Token token) {

	_position = token._position;
	_token    = token;

    } // Operator ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Whether an operator generates a reference (and thus can be an l-value).  By default, it does not.
     *
     * @return whether this operator yields a reference.
     */
    public boolean hasReference () {

	return false;

    } // hasReference ()
    // =============================================================================================================================
    


    // =============================================================================================================================
    /**
     * Generate assembly opcode that will execute this operator. Temporary fix to specify arity of 2 in this project.
     * 
     * @param operands list of operands to pass into the operator
     *
     * @return the generated assembly code.
     */
    abstract public String toAssembly (Expression... operands);
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Bind the operator against a symbol table's entries.  By default, operators have nothing to bind.
     */
    public void bind (Map<String, Declaration> symbols) {}
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Verify that the right number of operands is being provided, and then verify any needed properties about those operands.
     *
     * @param operands The operands on which this operator will be applied.
     * @return the type that this operator will yield given the operands.
     */
    abstract public Type verify (Expression... operands);
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly opcode that will execute this operator. Temporary fix to specify arity of 2 in this project.
     * 
     * @param operands list of operands to pass into the operator
     *
     * @return the generated assembly code.
     */
    public Type verify_l (Expression... operands) {
	
        Utility.abort("This operator cannot be an l-value");
        return null;
	
    }
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly opcode that will execute this operator as an l-value.
     * 
     * @param operands list of operands to pass into the operator
     * @return the generated assembly code.
     */
    public String toAssembly_l (Expression... operands) {

	Utility.abort("This operator cannot be an l-value");
        return null;
	
    }
    // =============================================================================================================================


    // =============================================================================================================================
    public String toString () {

	return _token._text;

    } // toString ()
    // =============================================================================================================================


    
// =================================================================================================================================
} // class Operator
// =================================================================================================================================
