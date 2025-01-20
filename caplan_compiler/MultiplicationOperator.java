// =================================================================================================================================
/**
 * A binary multiplication operator.
 */
public class MultiplicationOperator extends Operator {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a multiplication operator.
     *
     * @param token The token that specifies this operator.
     */
    public MultiplicationOperator (Token token) {

	super(token);

    } // MultiplicationOperator ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Verifies that all given operands are compatible with this operator
     * In particular, makes sure every operand is an "int"
     *
     * @param operands Array of expressions to be verified
     * @return The type resulting from this operation, an "int"
     */
    public Type verify (Expression... operands) {

	// Sanity check on operand count.
	if (operands.length < 2) {
	    Utility.abort("MultiplicationOperator.verify(): Invalid argument count (" + operands.length + ")");
	}

        // Verifies that all operands are integers and returns the int type
        Type int_type = new TypeInteger(_position);
        Type expr_type;
        for (Expression expression : operands){
            expr_type = expression.verify();
            if (expr_type == null) Utility.abort("Operand has null type");
            if (!int_type.equals(expr_type)) Utility.error("Cannot multiply " + expr_type, expression._position);
        }

        // Returns int type
        return int_type;
        
    } // verify ()
    // =============================================================================================================================


    // =============================================================================================================================
    @Override
    public String toAssembly(Expression... operands) {

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

	    // Apply the multiplication to the registers.
	    assembly += "\timul\trax,\trbx\n";

	    // Push the result.
	    assembly += "\tpush\trax\n";

	}

        return assembly;

    }
    // =============================================================================================================================


    
// =================================================================================================================================
} // class MultiplicationOperator
// =================================================================================================================================
