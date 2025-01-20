// =================================================================================================================================
/**
 * Compare operands and return a Boolean: true if the first is less than or equal to the second, false otherwise
 */
public class LessThanEqualsOperator extends Operator {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a less-than-or-equal-to operator.
     *
     * @param token The token that specifies this operator.
     */
    public LessThanEqualsOperator(Token token) {

	super(token);

    } // LessThanEqualsOperator ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    public Type verify(Expression... operands) {

        //Verifies that all operands are integers
        Type int_type = new TypeInteger(_position);
        Type expr_type;
        for(Expression expression : operands){
            expr_type = expression.verify();
            if(expr_type == null) Utility.error("Operand has null type", expression._position);
            if(!int_type.equals(expr_type)) Utility.error("Less-than-or-Equal Operation's operand is not an integer", expression._position);
        }

        //Returns boolean type
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
		      \tsetle\tal\t\t; A = ( B <= C ? 1 : 0 )
		      """ );

        // Push the result.
        assembly += "\tpush\trax\n";

        return assembly;
	
    } // toAssembly ()
    // =============================================================================================================================


    
    
// =================================================================================================================================
} // class LessThanEqualsOperator
// =================================================================================================================================
