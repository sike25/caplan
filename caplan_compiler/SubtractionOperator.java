// =================================================================================================================================

/**
 * A binary subtraction operator.
 */
public class SubtractionOperator extends Operator {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a subtraction operator.
     *
     * @param token The token that specifies this operator.
     */
    public SubtractionOperator (Token token) {

	super(token);

    } // SubtractionOperator ()
    // =============================================================================================================================



    // =============================================================================================================================
    public Type verify (Expression... operands) {
 
	// Sanity check on operand count.
	if (operands.length != 2) {
	    Utility.abort("SubtractionOperator.verify(): Invalid argument count (" + operands.length + ")");
	}

	// Verifies that first operand is either a pointer or integer and second operand is an integer
        Type int_type = new TypeInteger(_position);
        Type t1       = operands[0].verify();
        Type t2       = operands[1].verify();
        if (t1 == null || t2 == null) Utility.abort("Operand has null type");

        Type pointed_type = t1.getPointedType();
        if (!int_type.equals(t1) && pointed_type == null) {
	    Utility.error("First subtraction operand cannot be " + t1, operands[0]._position);
	}
        if (!int_type.equals(t2)) {
	    Utility.error("Second subtraction operand cannot be " + t2, operands[1]._position);
	}
	
        // Returns copy of type of first operand with new position corresponding to the operator
        return t1.duplicateType(_position);

    } // verify ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    @Override
    public String toAssembly (Expression... operands) {

        String assembly = new String();

        // Generate assembly for the two operands; both get pushed to the stack
        assembly = operands[0].toAssembly() + operands[1].toAssembly();

        // Pop the operand result into registers A and B (which are in reverse order on the stack).  
        // x86-64 performs push/pop only with 64-bit registers, so specify rax/rbx.
        assembly += "\tpop\trbx\n\tpop\trax\n";

        // Apply subtraction to the registers.
        assembly += "\tsub\trax,\trbx\n";

        // Push the result.
        assembly += "\tpush\trax\n";

        return assembly;

    }
    // =============================================================================================================================


    
// =================================================================================================================================
} // class SubtractionOperator
// =================================================================================================================================
