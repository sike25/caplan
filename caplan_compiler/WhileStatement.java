// =================================================================================================================================
// IMPORTS

import java.util.Map;
// =================================================================================================================================



// =================================================================================================================================
/**
 * An iterative `while` statement.
 */
class WhileStatement extends ControlStatement {
// =================================================================================================================================


    
    // =============================================================================================================================
    // DATA MEMBERS

    /** The body to execute with each iteration. */
    public final Statement  _body;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Create a new while statement.
     *
     * @param position  The position at which this statement begins in the source code.
     * @param condition The continuing condition.
     * @param body      The loop body.
     */
    public WhileStatement (int position, Expression condition, Statement body) {

        super(position, condition);
	    _body = body;
	
    } // WhileStatement ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Bind each variable used to its declaration.  Signal an error if no matching declaration is found.
     *
     * @param procedure The procedure to which this statement belongs.
     * @param symbols The symbol table of variable declarations.
     */
    public void bind (Procedure procedure, Map<String, Declaration> symbols) {

	setProcedure(procedure);
        _condition.bind(procedure, symbols);
        _body.bind(procedure, symbols);
	
    } // bind ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Verify semantic validity; determine operator based on operand list.  This method signals an
     * error and ends compilation upon an error.
     *
     * @return the type of data produced by executing the statement; <code>null</code> if no data is produced.
     */
    public Type verify () {
        Type conditionType = _condition.verify();
        if (!conditionType.equals(new TypeBoolean(-1))) {
            Utility.error("Condition must be a boolean value", _condition._position);
        }
        _body.verify();

        return null; // While loops don't produce any data
    } // verify ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly code that will execute this statement.
     *
     * @return the generated assembly code.
     */
    public String toAssembly () {

        String start_label = "WHILE_START_" + _position;
        String end_label   = "POST_WHILE_"  + _position;

        return ( start_label + ":\n" +
		 _condition.toAssembly() +
		 "\tpop\trax\t\t; Place the result of evaluating the condition in the register\n" +
		 "\tand\trax,\trax\t; Set the zero flag based on the condition\n" +
		 "\tjz\t" + end_label + "\n" +
		 _body.toAssembly() +
		 "\tjmp\t" + start_label + "\n" +
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

	return _condition.toStatics() + _body.toStatics();
	
    } // toStatics ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    public String toString () {

	return "while " + _condition + "\n\t" + _body;
	
    } // toString ()
    // =============================================================================================================================


	
// =================================================================================================================================
} // class WhileStatement
// =================================================================================================================================
