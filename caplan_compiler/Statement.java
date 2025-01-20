// =================================================================================================================================
// IMPORTS

import java.util.Map;
// =================================================================================================================================



// =================================================================================================================================
/**
 * A statement --- the core unit of code.
 */
abstract public class Statement {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS

    /** The position in the source code at which the statement begins. */
    public final int  _position;

    /** The procedure to which this statement belongs. */
    private Procedure _procedure;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Create a statement.
     *
     * @param position The location in the source code at which the statement begins.
     */
    public Statement (int position) {

	_position  = position;
	_procedure = null;     // Set during bind().

    } // Statement ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Provide the procedure to which this statement belongs.
     *
     * @return the containing procedure.
     */
    public Procedure getProcedure () {

	return _procedure;

    } // getProcedure ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Set the procedure to which this statement belongs.
     */
    public void setProcedure (Procedure procedure) {

	_procedure = procedure;

    } // getProcedure ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Bind each variable used to its declaration.  Signal an error if no matching declaration is found.
     *
     * @param procedure The procedure to which this statement belongs.
     * @param symbols   A symbol table of declarations.
     */
    abstract public void bind (Procedure procedure, Map<String, Declaration> symbols);
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Verify semantic validity; determine operator based on operand list.  This method signals an
     * error and ends compilation upon an error.
     *
     * @return the type of data produced by executing the statement; <code>null</code> if no data is produced.
     */
    abstract public Type verify ();
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly code that will execute this statement.
     *
     * @return the generated assembly code.
     */
    abstract public String toAssembly ();
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Generate assembly code for any statics that are part of this expression.
     *
     * @return the generated assembly code.
     */
    abstract public String toStatics ();
    // =============================================================================================================================



// =================================================================================================================================
} // class Statement
// =================================================================================================================================
