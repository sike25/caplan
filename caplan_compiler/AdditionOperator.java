// =================================================================================================================================
/**
 * A binary addition operator.
 */
public class AdditionOperator extends Operator {
    // =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create an addition operator.
     *
     * @param token The token that specifies this operator.
     */
    public AdditionOperator (Token token) {

	super(token);

    } // AdditionOperator ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Verifies that the correct types of operands are used.
     * At most one operand may be a pointer (zero is acceptable)
     * If there is a pointer, returns a pointer; otherwise, returns an integer.
     *
     * @return the Type which results after the operation is performed.
     */
    public Type verify (Expression... operands) {
        
        // Default return type is an integer, unless a pointer is identified.
        Type int_type = new TypeInteger(_position);
        Type pointer  = null;

		// Sanity check on operand count.
		if (operands.length < 2) {
			Utility.abort("AdditionOperator.verify(): Invalid argument count (" + operands.length + ")");
		}
	
		// Verify each of the operands as r-values; check that there are 0 or 1 pointers.
		for (Expression expression : operands) {

			Type expr_type = expression.verify();
				if (expr_type == null) Utility.abort("Operand has null type");

			// Is this a pointer?
				if (expr_type.getPointedType() != null) {
		
					// Yes.  Make sure we have not already seen a pointer.
					if (pointer != null) Utility.error("More than one pointer as operand in addition operation", expression._position);
					pointer = expr_type;

				} else if (!int_type.equals(expr_type)) {

			// Non-pointer argument is not an integer.
			Utility.error("Invalid type of operand for addition", expression._position);

			}
	    
	}

        // If one operand is a pointer, then the result of addition is a pointer; otherwise, it remains an integer.
        if (pointer != null) {
	    return pointer.duplicateType(_position);
	} else {
	    return int_type;
	}
        
    } // AdditionOperator ()
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

	    // Apply addition to the registers.
	    assembly += "\tadd\trax,\trbx\n";

	    // Push the result.
	    assembly += "\tpush\trax\n";

	}

	return assembly;
	
    } // toAssembly ()
    // =============================================================================================================================

    

    
    
// =================================================================================================================================
} // class AdditionOperator
// =================================================================================================================================
