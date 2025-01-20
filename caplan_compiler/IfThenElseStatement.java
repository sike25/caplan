// =================================================================================================================================
// IMPORTS

import java.util.Map;
// =================================================================================================================================



// =================================================================================================================================
/**
 * An iterative `if-then-else` statement.
 */
class IfThenElseStatement extends IfThenStatement {
// =================================================================================================================================



    
    // =============================================================================================================================
    // DATA MEMBERS

    /** The else-branch. */
    public final Statement  _elseBranch;
    // =============================================================================================================================

    

    // =============================================================================================================================
    /**
     * Create a new if-then-else statement.
     *
     * @param position   The position at which this statement begins in the source code.
     * @param condition  The controlling condition.
     * @param thenBranch The then-branch of the conditional statement.
     * @param elseBranch The else-branch of the conditional statement.
     */
    public IfThenElseStatement (int position, Expression condition, Statement thenBranch, Statement elseBranch) {

        super(position, condition, thenBranch);
	_elseBranch = elseBranch;
	
    } // IfThenElseStatement ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Bind each variable used to its declaration.  Signal an error if no matching declaration is found.
     *
     * @param procedure The procedure to which this statement belongs.
     * @param symbols The symbol table of variable declarations.
     */
    public void bind (Procedure procedure, Map<String, Declaration> symbols) {

	super.bind(procedure, symbols);
	_elseBranch.bind(procedure, symbols);
	
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

        super.verify();
        _elseBranch.verify();
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
	
        String else_label = "ELSE_START_"   + _position;
        String end_label  = "POST_IF_ELSE_" + _position;
	
        return ( ";BEGIN IF ELSE STATEMENT @ " + _position + "\n" +
		 _condition.toAssembly() +
		 "\tpop\trax\t\t; Place the result of evaluating the condition in the register\n" +
		 "\tand\trax,\trax\t; Set the zero flag based on the condition\n" +
		 "\tjz\t" + else_label + "\n" +
		 _thenBranch.toAssembly() +
		 "\tjmp\t" + end_label + "\n" +
		 else_label + ":\n" +
		 _elseBranch.toAssembly() +
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

	return super.toStatics() + _elseBranch.toStatics();
	
    } // toStatics ()
    // =============================================================================================================================

    

    // =============================================================================================================================
    public String toString () {

	return "if " + _condition + "\n\t" + _thenBranch + "\nelse\n\t" + _elseBranch;
	
    } // toString ()
    // =============================================================================================================================


    
// =================================================================================================================================
} // class IfThenElseStatement
// =================================================================================================================================
