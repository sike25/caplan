// =================================================================================================================================
abstract public class VariableDeclaration extends Declaration {
// =================================================================================================================================



    // =================================================================================================================================
    /**
     * Create a new variable declaration.
     *
     * @param token The token use declare the variable name.
     * @param type  The data type of the variable.
     */
    public VariableDeclaration (Token token, Type type) {
	
	super(token, type);
	
    } // VariableDeclaration ()
    // =================================================================================================================================



    // =================================================================================================================================
    /**
     * Provide assembly for access to the variable.  This code depends on the memory region in which the variable is allocated.
     *
     * @return assmebly for accessing this variable.
     */
    abstract public String toAssembly();
    // =================================================================================================================================



    // =================================================================================================================================
    /**
     * Provide assembly for access to the variable's address.  This code depends on the memory region in which the variable is
     * allocated.
     *
     * @return assmebly for accessing this variable's address.
     */
    abstract public String toAssembly_l();
    // =================================================================================================================================



    // =================================================================================================================================
    /**
     * Provide assembly for the statics section as needed for each variable declaration
     * @return
     */
    abstract public String toStatics();
    // =================================================================================================================================

    

// =====================================================================================================================================
} // class VariableDeclaration
// =====================================================================================================================================
