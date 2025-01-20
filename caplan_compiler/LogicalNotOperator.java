// =================================================================================================================================
/**
 * A unary logical not operator.
 */
public class LogicalNotOperator extends Operator {
// =================================================================================================================================



    // =============================================================================================================================
    /**
     * Create a logical not operator.
     *
     * @param token The token that specifies this operator.
     */
    public LogicalNotOperator(Token token) {

	super(token);

    } // LogicalNotOperator ()
    // =============================================================================================================================


    
    // =================================================================================================================================
    /**
    * Verifies that the operand is a boolean, and returns boolean
    */
    public Type verify (Expression... operands) {
        
        Type bool_type = new TypeBoolean(_position);
        Type expr_type = operands[0].verify();

        if(expr_type == null) Utility.abort("Operand has null type");   

        if(!bool_type.equals(expr_type)) Utility.error("Logical-Not Operation's operand is not a boolean", operands[0]._position);

        //Returns a boolean type
        return bool_type;

    } // verify()
    // =============================================================================================================================


    
    // =============================================================================================================================
    @Override
    public String toAssembly (Expression... operands) {

        String assembly = new String();

        // Generate assembly for the operand.
	assembly += operands[0].toAssembly();

	// Pop the operand result into registers A.
	assembly += "\tpop\trax\n";

	// Apply NOT to the register.
	assembly += "\tnot\trax\n";

	// Push the result.
	assembly += "\tpush\trax\n";

	return assembly;		
	
    } // toAssembly ()
    // =============================================================================================================================


    
    
// =================================================================================================================================
} // class LogicalNotOperator
// =================================================================================================================================
