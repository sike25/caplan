// =================================================================================================================================
// IMPORTS

import java.util.Map;
// =================================================================================================================================



// =================================================================================================================================
/**
 * An iterative `if-then` statement.
 */
class IfThenStatement extends ControlStatement {
// =================================================================================================================================


    
    // =============================================================================================================================
    // DATA MEMBERS

    /** The then-branch. */
    public final Statement  _thenBranch;
    // =============================================================================================================================
    


    // =============================================================================================================================
    /**
     * Create a new if-then statement.
     *
     * @param position   The position at which this statement begins in the source code.
     * @param condition  The controlling condition.
     * @param thenBranch The then-branch of the conditional statement.
     */
    public IfThenStatement (int position, Expression condition, Statement thenBranch) {

        super(position, condition);
	    _thenBranch = thenBranch;
	
    } // IfThenStatement ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Bind each variable used to its declaration.  Signal an error if no matching declaration is found.
     *
     * @param procedure The procedure to which this statement belongs.
     * @param symbols   The symbol table of variable declarations.
     */
    public void bind (Procedure procedure, Map<String, Declaration> symbols) {

	setProcedure(procedure);
	_condition.bind(procedure, symbols);
	_thenBranch.bind(procedure, symbols);

    } // bind ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Verify semantic validity; determine operator based on operand list.  This method signals an error if the condition is not a
     * boolean expression.
     *
     * @return the type of data produced by executing the statement; <code>null</code> if no data is produced.
     */
    public Type verify () {
	
        Type conditionType = _condition.verify();
        if (!conditionType.equals(new TypeBoolean(-1))) {
            Utility.error("Condition must be a boolean value", _condition._position);
        }
        _thenBranch.verify();

	// Control statements don't yield a value.	
        return null;
	
    } // verify ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly code that will execute this statement.
     *
     * @return the generated assembly code.
     */
    public String toAssembly () {

        String end_label = "POST_IF_" + _position;
	
        return ( "\t; Begin if-then @" + _position + "\n" +
		 _condition.toAssembly() +
		 "\tpop\trax\t\t; Place the result of evaluating the condition in the register\n" +
		 "\tand\trax,\trax\t; Set the zero flag based on the condition\n" +
		 "\tjz\t" + end_label + "\n" +
		 _thenBranch.toAssembly() +
		 end_label + ":\n" );
		 
    } // toAssembly ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly code for any statics that are part of this expression.
     *
     * @return the generated assembly code.
     */
    public String toStatics () {

	return _condition.toStatics() + _thenBranch.toStatics();
	
    } // toStatics ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    public String toString () {

	return "if " + _condition + "\n\t" + _thenBranch;
	
    } // toString ()
    // =============================================================================================================================


    
// =================================================================================================================================
} // class IfThenStatement
// =================================================================================================================================
