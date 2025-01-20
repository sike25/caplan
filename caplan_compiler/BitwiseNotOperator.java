// =================================================================================================================================
/**
 * A unary bitwise not operator.
 */
public class BitwiseNotOperator extends Operator {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a bitwise not operator.
     *
     * @param token The token that specifies this operator.
     */
    public BitwiseNotOperator(Token token) {

	super(token);

    } // BitwiseNotOperator ()
    // =============================================================================================================================

    // =============================================================================================================================
    public Type verify(Expression... operands) {
        
	// Sanity check on operand count.
	if (operands.length != 1) {
	    Utility.abort("BitwiseNotOperator.verify(): Invalid argument count (" + operands.length + ")");
	}

        //Verifies that operand is an integer and returns the int type
        Type int_type  = new TypeInteger(_position);
        Type expr_type = operands[0].verify();
        if (expr_type == null) Utility.abort("Operand has null type");
        if (!int_type.equals(expr_type)) Utility.error("Bitwise-Not Operation's operand is not an integer", _position);

        //If we have reached this point, t was an int type
        return int_type;

    } // verify ()
    // =============================================================================================================================
    
    // =============================================================================================================================
    @Override
    public String toAssembly (Expression... operands) {

        String assembly = new String();

        // Generate assembly for the operand.
	assembly += operands[0].toAssembly();

	// Pop the operand result into registers A.
	assembly += "\tpop\trax\n";

	// Apply bitwise-NOT to the register.
	assembly += "\tnot\trax\n";

	// Push the result.
	assembly += "\tpush\trax\n";

	return assembly;		
	
    } // toAssembly ()
    // =============================================================================================================================


    
    
// =================================================================================================================================
} // class BitwiseNotOperator
// =================================================================================================================================
