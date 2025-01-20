// =================================================================================================================================
// IMPORTS

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
// =================================================================================================================================



// =================================================================================================================================
/**
 * A single program, which is a list statements to evaluate.
 */
public class Program {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS

    /** The expressions in order in which they appear in the source. */
    public List<Procedure>           _procedures;

    /** The procedure table, mapping procedure names to their declarations. */
    public Map<String, Declaration>  _procedureTable;

    /** The global variables in the order in which they are declared. */
    public List<VariableDeclaration> _variables;

    /** The global variable table, mapping variable names to their declarations. */
    public Map<String, Declaration>  _variableTable;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Create a new program from a given list of statements.
     *
     * @param statements A list of statments.
     */
    public Program (List<Procedure> procedures, List<VariableDeclaration> variables) {

	_procedures = procedures;
        _variables  = variables;

	// Create the procedure and variable symbol tables.
	_procedureTable = new HashMap<String, Declaration>();
	for (Declaration procedure : _procedures) {
	    Declaration oldProcedure = _procedureTable.put(procedure._name, procedure);
	    if (oldProcedure != null) {
		Utility.error("Duplicate procedure declaration\nPrevious declaration " +
			       Utility.positionToLineColumn(oldProcedure._position),
			      procedure._position);
	    }
	}
	_variableTable = new HashMap<String, Declaration>();
	for (Declaration variable : _variables) {
	    Declaration oldVariable = _variableTable.put(variable._name, variable);
	    if (oldVariable != null) {
		Utility.error("Duplicate variable declaration\nPrevious declaration " +
			       Utility.positionToLineColumn(oldVariable._position) + oldVariable._position + ")",
			      variable._position);
	    }
	}

    } // Program ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Find all uses of names and bind them to their declarations.
     */
    public void bind () {
	
        for (Procedure procedure : _procedures) {
            procedure.bind(_variableTable, _procedureTable);
        }
	
    } // bind ()
    // =============================================================================================================================

    

    // =============================================================================================================================
    /**
     * Verify the semantic properties of each statement (e.g., type matching).
     */
    public void verify () {

        for (Procedure procedure : _procedures) {
            procedure.verify();
        }
	
    } // verify ()
    // =============================================================================================================================

    

    // =============================================================================================================================
    /**
     * Generate assembly code that will execute this program.
     *
     * @return the generated assembly code.
     */
    public String toAssembly() {

        // Prologue: stub code, setting up and starting the code portion of the
        // assembly.
        String assembly = "\tglobal\tmain\n";

	// Declare all extern procedures.
	for (Procedure procedure : _procedures) {
	    if (procedure instanceof ExternalProcedure) {
		assembly += procedure.toAssembly();
	    }
	}

	assembly += "\tsection\t.text\n\n";

        // Generate the code of each defined (internal) procedure.
        for (Procedure procedure : _procedures) {
	    if (procedure instanceof InternalProcedure) {
		assembly += procedure.toAssembly();
	    }
        }
			    
        // Epilogue stub code: Add any statically allocated string literals.
        assembly += "\n\tsection\t.data\n\n";
        for (VariableDeclaration variable : _variables) {
            assembly += variable.toStatics();
        }
        for (Procedure procedure : _procedures) {
            assembly += procedure.toStatics();
        }

        return assembly;

    } // toAssembly ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    public String toString () {
        String text = "";

        text += "[";
        for (VariableDeclaration variable : _variables) {
            text += variable._type.toString() + ' ' + variable.toString();
            if (variable != _variables.getLast()) text += ", ";
        }
        text += "]\n";
        
        for (Procedure procedure : _procedures) {
            text += procedure.toString() + '\n';
        }

        return text;
	
    } // toString ()
    // =============================================================================================================================
    
    
    
// =================================================================================================================================
} // class Program
// =================================================================================================================================
