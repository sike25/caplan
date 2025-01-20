// =================================================================================================================================
/**
 * A binary modulues operator.
 */
public class ModulusOperator extends Operator {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a modulus operator.
     *
     * @param token The token that specifies this operator.
     */
    public ModulusOperator (Token token) {

	super(token);

    } // ModulusOperator ()
    // =============================================================================================================================


    
    // =================================================================================================================================
    /**
    * Verifies that both operands are integers, and returns integer
    */
    public Type verify(Expression... operands){

	// Sanity check on operand count.
	if (operands.length != 2) {
	    Utility.abort("ModulusOperator.verify(): Invalid argument count (" + operands.length + ")");
	}

        //Verifies that all operands are integers and returns the int type
        Type int_type = new TypeInteger(_position);
        Type expr_type;
        for(Expression expression : operands){
            expr_type = expression.verify();
            if(expr_type == null) Utility.abort("Operand has null type");
            if(!int_type.equals(expr_type)) Utility.error("Cannot modulus " + expr_type, expression._position);
        }

        //If we have reached this point, t was an int type
        return int_type;

    } // verify ()

    // =============================================================================================================================
    @Override
    public String toAssembly(Expression... operands) {

        String assembly = new String();

        // Generate assembly for the two operands; both get pushed to the stack
        assembly = operands[0].toAssembly() + operands[1].toAssembly();

        // Pop the operand result into registers A and B (which are in reverse order on the stack).  
        // x86-64 performs push/pop only with 64-bit registers, so specify rax/rbx.
        assembly += "\tpop\trbx\n\tpop\trax\n";

        // Apply modulus to the registers.
        // To use idiv:  rdx:rax / rbx => rax (R edx)
        //   rax already holds the lower half of the dividend; cdq sign-extends into rdx to complete it.
        //   rbx already holds the divisor.
        //   The result must be moved from rdx to rax.
        assembly += "\tcdq\n\tidiv\trbx\n\tmov\trax,\trdx\n";

        // Push the result.
        assembly += "\tpush\trax\n";

        return assembly;

    }
    // =============================================================================================================================



    
// =================================================================================================================================
} // class ModulusOperator
// =================================================================================================================================
