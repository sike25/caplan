// =================================================================================================================================
/**
 * Return the logical AND of the Boolean inputs
 */
public class LogicalAndOperator extends Operator {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a logical-and operator.
     *
     * @param token The token that specifies this operator.
     */
    public LogicalAndOperator(Token token) {

	super(token);

    } // LogicalAndOperator ()
    // =============================================================================================================================


    
    // =================================================================================================================================
    /**
    * Verifies that both operands are booleans, and returns boolean
    */
    public Type verify (Expression... operands) {
        
        //Verifies that all operands are booleans
        Type bool_type = new TypeBoolean(_position);
        Type expr_type;
        for(Expression expression : operands){
            expr_type = expression.verify();
            if(expr_type == null) Utility.error("Operand has null type", expression._position);
            if(!bool_type.equals(expr_type)) Utility.error("Logical-And Operation's operand is not a boolean", expression._position);
        }

        //Returns a boolean
        return bool_type;

    } // verify()
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

	    // Apply AND to the registers.
	    assembly += "\tand\trax,\trbx\n";

	    // Push the result.
	    assembly += "\tpush\trax\n";

	}

	return assembly;
	
    } // toAssembly ()
    // =============================================================================================================================


    
// =================================================================================================================================
} // class LogicalAndOperator
// =================================================================================================================================
