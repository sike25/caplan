// =================================================================================================================================
/**
 * Return the bitwise OR of the integer inputs
 */
public class BitwiseOrOperator extends Operator {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a bitwise-or operator.
     *
     * @param token The token that specifies this operator.
     */
    public BitwiseOrOperator(Token token) {

	super(token);

    } // BitwiseOrOperator ()
    // =============================================================================================================================

    // =============================================================================================================================
    public Type verify(Expression... operands) {
        
	// Sanity check on operand count.
	if (operands.length < 2) {
	    Utility.abort("BitwiseOrOperator.verify(): Invalid argument count (" + operands.length + ")");
	}

        //Verifies that all operands are integers and returns the int type
        Type int_type = new TypeInteger(_position);
        Type expr_type;
        for(Expression expression : operands){
            expr_type = expression.verify();
            if(expr_type == null) Utility.abort("Operand has null type");
            if(!int_type.equals(expr_type)) Utility.error("Bitwise-Or Operation's operand is not an integer", expression._position);
        }

        //If we have reached this point, t was an int type
        return int_type;

    } // verify ()
    // =============================================================================================================================
    
    // =============================================================================================================================
    @Override
    public String toAssembly (Expression... operands) {

        String assembly = new String();

        // Generate assembly for the operands in reverse order (so that evaluation is later done left-to-right).
	for (int i = operands.length - 1; i >= 0; i -= 1) {
	    assembly += operands[i].toAssembly();
	}

	// Perform each of the additions in turn.
	for (int i = 0; i < operands.length - 1; i += 1) {
	
	    // Pop the top two operand results into registers A and B.
	    // x86-64 performs push/pop only with 64-bit registers, so specify rax/rbx.
	    assembly += "\tpop\trax\n\tpop\trbx\n";

	    // Apply bitwise-OR to the registers.
	    assembly += "\tor\trax,\trbx\n";

	    // Push the result.
	    assembly += "\tpush\trax\n";

	}

	return assembly;	

    } // toAssembly ()
    // =============================================================================================================================

    
// =================================================================================================================================
} // class BitwiseOrOperator
// =================================================================================================================================
