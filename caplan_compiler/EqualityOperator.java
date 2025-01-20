// =================================================================================================================================
/**
 * Check operands for equality and return a Boolean value.
 */
public class EqualityOperator extends Operator {
// =================================================================================================================================


    
    // =============================================================================================================================
    /**
     * Create an equality operator.
     *
     * @param token The token that specifies this operator.
     */
    public EqualityOperator(Token token) {

	super(token);

    } // EqualityOperator ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    public Type verify (Expression... operands) {

	// Sanity check on operand count.
	if (operands.length != 2) {
	    Utility.abort("EqualityOperator.verify(): Invalid argument count (" + operands.length + ")");
	}
	
        //Verifies that operands have the same type, always returns a boolean
        Type t1 = operands[0].verify();
        Type t2 = operands[1].verify();
        if (t1 == null || t2 == null) Utility.abort("Operand has null type");
	if (!t1.equals(t2)) Utility.error("Types of Operands are not equivalent in Equality Operation", _position);
        return new TypeBoolean(_position);
        
    }
    // =============================================================================================================================


    
    // =============================================================================================================================
    @Override
    public String toAssembly (Expression... operands) {

        String assembly = new String();

        // Generate assembly for the two operands; both get pushed to the stack
        assembly = operands[0].toAssembly() + operands[1].toAssembly();

        // Pop the operand result into registers B and C (which are in reverse order on the stack).  
        // x86-64 performs push/pop only with 64-bit registers, so specify rax/rbx.
        assembly += "\tpop\trcx\n\tpop\trbx\n";

	// Compare B & C; clear A, then set its lowest byte based on the result of the comparison.
	assembly += ( """
		      \txor\trax,\trax\t; Clear A
		      \tcmp\trbx,\trcx\t; Compare operands
		      \tsete\tal\t\t; A = ( B == C ? 1 : 0 )
		      """ );

        // Push the result.
        assembly += "\tpush\trax\n";

        return assembly;
	
    } // toAssembly ()
    // =============================================================================================================================


// =================================================================================================================================
} // class EqualityOperator
// =================================================================================================================================
