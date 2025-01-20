// =================================================================================================================================
/**
 * A unary dereference operator.
 */
public class DereferenceOperator extends Operator {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a dereference operator.
     *
     * @param token The token that specifies this operator.
     */
    public DereferenceOperator (Token token) {

	super(token);

    } // DereferenceOperator ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Whether an operator generates a reference (and thus can be an l-value).  Dereferences does.
     *
     * @return whether this operator yields a reference.
     */
    public boolean hasReference () {

	return true;

    } // hasReference ()
    // =============================================================================================================================
    


    // =============================================================================================================================
    public Type verify (Expression... operands) {
        
	// Sanity check on operand count.
	if (operands.length != 1) {
	    Utility.abort("DereferenceOperator.verify(): Invalid argument count (" + operands.length + ")");
	}

        Type expr_type = operands[0].verify();
        if (expr_type == null) Utility.abort("Operand has null type");

        Type pointedType = expr_type.getPointedType();
        if (pointedType == null) Utility.error("Dereference Operation's operand is not a pointer", operands[0]._position);

        // Currently denies the ability to dereference a pointer to a void type
        Type void_type = new TypeVoid(_position);
        if (void_type.equals(pointedType)) {
	    Utility.error("Expressions pointing to Expressions of Type Void cannot be dereferenced", operands[0]._position);
	}

        return pointedType.duplicateType(_position);

    } // verify ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    @Override
    public String toAssembly (Expression... operands) {

        String assembly = new String();

        // Generate assembly for the operand.
	Expression reference   = operands[0];
        Type       pointedType = reference.getType().getPointedType();
	assembly += reference.toAssembly();

	// Pop the operand result into register B.
	assembly += "\tpop\trbx\n";

	// Special case: x86-64, for half-word or byte values, does not clear upper bytes of the register, so we clear it here.
	String register = pointedType.getSizedRegister("a");
	assembly += ( pointedType.getSize() < Utility._dwordSize ?
		      "\txor\trax,\trax\t; Clear register for small value\n" :
		      "" );

	// Move the value at the end of the pointer into the register.
	assembly += "\tmov\t" + register + ",\t[rbx]\t; Derefence\n";

	// Push the result.
	assembly += "\tpush\trax\n";

	return assembly;
	
    } // toAssembly ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    public Type verify_l (Expression... operands) {

	// Verifying as an l-type is identical to verifying as an r-value.
	return verify(operands);
	
    }
    // =============================================================================================================================


    // =============================================================================================================================
    public String toAssembly_l (Expression... operands) {

	// Evaluating the operand yields the address to be dereferenced.  Using it as an l-value means simply pushing that
	// reference, where its use will later dereference it.
        return operands[0].toAssembly();

    }
    // =============================================================================================================================
    
    
// =================================================================================================================================
} // class DereferenceOperator
// =================================================================================================================================
