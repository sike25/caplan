// =================================================================================================================================
// IMPORTS

import java.util.Map;
// =================================================================================================================================



// =================================================================================================================================
/**
 * A word-sized integer literal.
 */
public class IntegerLiteral extends Literal {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS

    /** The value of the integer. */
    public final int _value;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * A new literal integer value.
     *
     * @param token The token representing the integer literal.
     */
    public IntegerLiteral (Token token) {

    	super(token, new TypeInteger(token._position));

	// Use a local variable to avoid confusion by the compiler about when the final _value is initialized in a try/catch.
	int value = 0;
	try {
	    value = Integer.parseUnsignedInt(token._text);
	} catch (NumberFormatException e) {
	    Utility.error("Integer literal cannot be stored in an int", token._position);
	}

	_value = value;

    } // IntegerLiteral ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * A new literal integer value.
     *
     * @param position The position of the token that yields the integer.
     * @param value    The integer value itself.
     */
    public IntegerLiteral (int position, int value) {

	super(position, new TypeInteger(position));
	_value = value;

    } // IntegerLiteral ()
    // =============================================================================================================================
    

    
    // =============================================================================================================================
    /**
     * Generate assembly code that will execute this expression.
     *
     * @return the generated assembly code.
     */
    public String toAssembly () {

	// Evaluating a literal involves pushing its value onto the stack.
	return "\tpush\t" + _value + '\n';
	
    } // toAssembly ()
    // =============================================================================================================================
    


    // =============================================================================================================================
    public String toString () {

	return "" + _value;

    } // toString ()
    // =============================================================================================================================



// =================================================================================================================================
} // class IntegerLiteral
// =================================================================================================================================
