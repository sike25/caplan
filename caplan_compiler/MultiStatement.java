// =================================================================================================================================
// IMPORTS

import java.util.List;
import java.util.Map;
// =================================================================================================================================



// =================================================================================================================================
/**
 * A block of statements.
 */
class MultiStatement extends Statement {
// =================================================================================================================================


    
    // =============================================================================================================================
    // DATA MEMBERS

    /** The contained list of statements. */
    public final List<Statement> _statements;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Create a new multi-statement.
     *
     * @param position   The position at which this statement begins in the source code.
     * @param statements The contained list of statements that compose this multi-statement.
     */
    public MultiStatement (int position, List<Statement> statements) {

        super(position);
	_statements = statements;
	
    } // MultiStatement ()
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
	for (Statement statement : _statements) {
	    statement.bind(procedure, symbols);
	}
	
    } // bind ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Verify semantic validity; determine operator based on operand list.  This method signals an error if an invalidity is found.
     *
     * @return the type of data produced by executing the statement; <code>null</code> if no data is produced.
     */
    public Type verify () {

	for (Statement statement : _statements) {
	    statement.verify();
	}

        return null; // Multi-statements don't yield a value.
	
    } // verify ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly code that will execute this statement.
     *
     * @return the generated assembly code.
     */
    public String toAssembly () {

	String assembly = "\t; Begin multi-statement @" + _position + '\n';
	for (Statement statement : _statements) {
	    assembly += statement.toAssembly();
	}
	assembly += "\t; End multi-statement @" + _position + '\n';

	return assembly;

    } // toAssembly ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly code for any statics that are part of this expression.
     *
     * @return the generated assembly code.
     */
    public String toStatics () {

	String assembly = new String();
	for (Statement statement : _statements) {
	    assembly += statement.toStatics();
	}

	return assembly;
	
    } // toStatics ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    public String toString () {

	String string = "{\n";
	for (Statement statement : _statements) {
	    string += "\t" + statement + '\n';
	}
	string += "}\n";

	return string;
	
    } // toString ()
    // =============================================================================================================================


	
// =================================================================================================================================
} // class MultiStatement
// =================================================================================================================================
