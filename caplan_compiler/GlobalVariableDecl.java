// =================================================================================================================================
public class GlobalVariableDecl extends VariableDeclaration {
// =================================================================================================================================



    // =================================================================================================================================
    /**
     * Create a new global variable declaration.
     *
     * @param token The token use declare the variable name.
     * @param type  The data type of the variable.
     */
    public GlobalVariableDecl (Token token, Type type) {

	super(token, type);
	
    } // GlobalVariableDecl ()
    // =================================================================================================================================



    // =================================================================================================================================
    /**
     * Provide assembly for access to the global variable.  It is accessed via a label into the statics.
     *
     * @return assmebly for accessing this variable.
     */
    public String toAssembly () {

	return "[" + getLabel() + "]";

    } // toAssembly ()
    // =================================================================================================================================
    

    
    // =================================================================================================================================
    /**
     * Generate assembly to use this variable as an l-value.
     */
    public String toAssembly_l () {

	String assembly = "\tpush\t" + getLabel() + "\t; Push address of " + _name + "\n";
    
	return assembly;
	
    } // toAssembly_l ()
    // =================================================================================================================================



    // =================================================================================================================================
    /**
     * Generate static label for this variable
     */
    public String toStatics() {

	String assembly = getLabel() + ":\t";

        // Reserve the right amount of bytes given the size of the type
        switch (_type.getSize()) {
            case 1:
                assembly += "db";
                break;
            case 2:
                assembly += "dw";
                break;
            case 4:
                assembly += "dd";
                break;
            case 8:
                assembly += "dq";
                break;
            default:
                Utility.error("Unexpected type in the statics", _position);
        }

        assembly += "\t0\t; Global variable declaration for " + _name + '\n';

        return assembly;
    }
    // =================================================================================================================================


    public String getLabel() {
        return _name + "_global";
    }

    
    
// =================================================================================================================================
} // class GlobalVariable
// =================================================================================================================================
