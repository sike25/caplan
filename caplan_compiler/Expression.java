// =================================================================================================================================
// IMPORTS

import java.util.Map;
// =================================================================================================================================



// =================================================================================================================================
/**
 * An expression which, when evaluated, yields some value.
 */
abstract public class Expression extends Statement {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a expression.
     *
     * @param position The location in the source code at which the expression begins.
     */
    public Expression (int position) {

	super(position);

    } // Expression ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Verify semantic validity; determine operator based on operand list.
     *
     * @return the type to which the expression evaluates.
     */
    abstract public Type verify ();
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Whether this expression can have a reference to itself.
     *
     * @return <code>true</code> if this expression can be referenced; <code>false</code> otherwise.
     */
    abstract public boolean hasReference ();
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Return the type of produced by evaluating this expression.
     *
     * @return the type when evaluated.
     */
    abstract public Type getType ();
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly code for any statics that are part of this expression.  Most expressions do not generate statics, so the
     * default behavior is to do nothing.
     *
     * @return the generated assembly code.
     */
    public String toStatics () {

	return new String();

    } // toStatics ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Verify semantic validity as an l-value.  By default, expressions cannot be l-values.
     *
     * @return the type to which the expression evaluates.
     */
    public Type verify_l () {
	
        Utility.error("Invalid l-value", _position);
        return null;
	
    }
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly opcode that will execute this operator as an l-value.  By default, expressions cannot be l-values.
     * 
     * @return the generated assembly code.
     */
    public String toAssembly_l () {
	
        Utility.abort("Invalid l-value");
        return null;
	
    }
    // =============================================================================================================================


    
// =================================================================================================================================
} // class Expression
// =================================================================================================================================
