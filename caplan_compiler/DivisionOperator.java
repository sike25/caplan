// =================================================================================================================================
/**
 * A binary division operator.
 */
public class DivisionOperator extends Operator {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a division operator.
     *
     * @param token The token that specifies this operator.
     */
    public DivisionOperator (Token token) {

	super(token);

    } // DivisionOperator ()
    // =============================================================================================================================



    // =================================================================================================================================
    /**
     * Verifies that all operands are integers, and returns integer
     */
    @Override
    public Type verify (Expression... operands) {
        
	// Sanity check on operand count.
	if (operands.length != 2) {
	    Utility.abort("DivisionOperator.verify(): Invalid argument count (" + operands.length + ")");
	}

        // Verifies that all operands are integers and returns the int type
        Type int_type = new TypeInteger(_position);
        Type expr_type;
        for (Expression expression : operands){
            expr_type = expression.verify();
            if(expr_type == null) Utility.abort("Operand has null type");
            if(!int_type.equals(expr_type)) Utility.error("Cannot divide " + expr_type, expression._position);
        }

        //If we have reached this point, t was an int type
        return int_type;

    } // verify ()
    // =============================================================================================================================

    
    // =============================================================================================================================
    @Override
    public String toAssembly(Expression... operands) {

        String assembly = new String();

        // Generate assembly for the two operands; both get pushed to the stack
        assembly = operands[0].toAssembly() + operands[1].toAssembly();

        // Pop the operand result into registers A and B (which are in reverse order on the stack).  
        // x86-64 performs push/pop only with 64-bit registers, so specify rax/rbx.
        assembly += "\tpop\trbx\n\tpop\trax\n";

        // Apply division to the registers.
        // To use idiv:  rdx:rax / rbx => rax (R rdx)
        //   rax already holds the lower half of the dividend; cdq sign-extends into edx to complete it.
        //   rbx already holds the divisor.
        //   The result is stored into rax already, and rdx is ignored.
        assembly += "\tcdq\n\tidiv\trbx\n";

        // Push the result.
        assembly += "\tpush\trax\n";

        return assembly;
    }
    // =============================================================================================================================


// =================================================================================================================================
} // class DivisionOperator
// =================================================================================================================================
