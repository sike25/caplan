// =================================================================================================================================
/**
 * A unary negation operator.
 */
public class NegationOperator extends Operator {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a negation operator.
     *
     * @param token The token that specifies this operator.
     */
    public NegationOperator(Token token) {

	super(token);

    } // NegationOperator ()
    // =============================================================================================================================

    public Type verify(Expression... operands) {
        
        Type int_type = new TypeInteger(_position);
        Type expr_type = operands[0].verify();
        if (expr_type == null) Utility.error("Operand has null type", operands[0]._position);
        if (!int_type.equals(expr_type)) Utility.error("Negation Operation's operand is not an integer", operands[0]._position);
        return int_type;

    } // verify ()
    // =============================================================================================================================
    


    // =============================================================================================================================
    @Override
    public String toAssembly (Expression... operands) {

        String assembly = new String();

        // Generate assembly for the operand, pushing it onto the stack.
        assembly = operands[0].toAssembly();

        // Pop the operand result into register A.
        assembly += "\tpop\trax\n";

        // Apply (two's complement) negation to the register.
        assembly += "\tneg\trax\t\t; Negate\n";

        // Push the result.
        assembly += "\tpush\trax\n";

        return assembly;
	
    } // toAssembly ()
    // =============================================================================================================================

    
    
// =================================================================================================================================
} // class NegationOperator
// =================================================================================================================================
