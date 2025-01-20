// =================================================================================================================================
/**
 * Check operands for equality and return a Boolean value (the negation of what EqualityOperator returns).
 */
public class NotEqualityOperator extends Operator {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a not-equality operator.
     *
     * @param token The token that specifies this operator.
     */
    public NotEqualityOperator(Token token) {

	super(token);

    } // NotEqualityOperator ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    public Type verify (Expression... operands) {
        
        //Verifies that operands have the same type, always returns a boolean
        Type t1 = operands[0].verify();
        Type t2;
        if (t1 == null) Utility.error("Operand has null type", operands[0]._position);
        for(Expression expression : operands){
            if(expression != operands[0]){
                t2 = expression.verify();
                if(t2 == null) Utility.error("Operand has null type", expression._position);
                if(!t1.equals(t2)) Utility.error("Types of Operands are not equivalent in Equality Operation", expression._position);
            }
        }
        return new TypeBoolean(_position);

    } // verify ()
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
		      \tsetne\tal\t\t; A = ( B != C ? 1 : 0 )
		      """ );

        // Push the result.
        assembly += "\tpush\trax\n";

        return assembly;
	
    } // toAssembly ()
    // =============================================================================================================================


    
// =================================================================================================================================
} // class NotEqualityOperator
// =================================================================================================================================
