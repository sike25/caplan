// =================================================================================================================================
/**
 * A unary reference operator.
 */
public class ReferenceOperator extends Operator {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a reference operator.
     *
     * @param token The token that specifies this operator.
     */
    public ReferenceOperator(Token token) {

	super(token);

    } // ReferenceOperator ()
    // =============================================================================================================================



    // =============================================================================================================================
    public Type verify (Expression... operands) {
        
	// Sanity check on operand count.
	if (operands.length != 1) {
	    Utility.abort("ReferenceOperator.verify(): Invalid argument count (" + operands.length + ")");
	}

        // The only thing to verify here is that the operand can be referenced.
        Type expr_type = operands[0].verify_l();
        if (expr_type == null) Utility.abort("Operand has null type");
        if (!operands[0].hasReference()) {
	    Utility.error("Reference Operation's operand not an l-value, cannot be referenced", operands[0]._position);
	}

        // Type of this expression is a ptr to whatever the type of its operand is
        return new TypePointer(_position, expr_type);

    } // verify ()
    // =============================================================================================================================

    

    // =============================================================================================================================
    @Override
    public String toAssembly (Expression... operands) {

	// To evaluate a reference of the operand, simply evaluate it as an l-value, which are the only operands that can be
	// referenced.
	return operands[0].toAssembly_l();
	
    } // toAssembly ()
    // =============================================================================================================================

    
    
// =================================================================================================================================
} // class ReferenceOperator
// =================================================================================================================================
