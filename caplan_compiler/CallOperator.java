// =================================================================================================================================
// IMPORTS

import java.util.List;
import java.util.Map;
// =================================================================================================================================



// =================================================================================================================================
/**
 * An operator that calls a procedure.
 */
public class CallOperator extends Operator {
// =================================================================================================================================


    
    // =============================================================================================================================
    // DATA MEMBERS

    /** The name of the procedure; used later for binding. */
    public final String           _name;
    
    /** The procedure whose execution is being initiated by this call operator. */
    private      Procedure        _procedure;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Basic constructor for a call operator.
     * 
     * @param position  The location in the source code at which the expression begins.
     */
    public CallOperator (Token token) {

	super(token);
	_name = token._text;

	// The procedure is determined later, during verification (see bind()).
	_procedure = null; 

    } // CallOperator ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Whether this expression can have a reference to itself.
     *
     * @return Whether this operation's operator yields a reference.
     */
    public boolean hasReference () {

        // DONE
        
        return false;

    } // hasReference ()
    // =============================================================================================================================

    

    // =============================================================================================================================
    /**
     * Binds this procedureCall to the Procedure object to which it corresponds (i.e. sets _procedure)
     *
     * @param symbols The symbol table of variable declarations.
     */
    public void bind (Map<String, Declaration> symbols) {

        Declaration declaration = symbols.get(_name);
	Utility.debug(2, "CallOperator.bind(): Lookup " + _name + " = " + declaration);
        if (declaration != null) {
	    try {
		_procedure = (Procedure)declaration;
	    } catch (ClassCastException e) {
		Utility.error("Cannot use variable as a procedure", _position);
	    }
	}
        
    } // bind ()
    // =============================================================================================================================

    
    
    // =============================================================================================================================
    /**
     * Should set the _return_type field of this expression based on the _procedure field
     * Should also force arguments to verify() as well
     *
     * @return the type produced by evaluation.
     */
    public Type verify (Expression... arguments) {

	if (_procedure == null) {
	    Utility.error("Call to undeclared procedure", _position);
	}
	
        List<VariableDeclaration> parameters = _procedure._parameters;
	boolean reachedEtcetra = false;
        for (int i = 0; i < arguments.length; i += 1) {

	    Type argType = arguments[i].verify();
	    
	    // Have we (newly) reached an etcetera?
            if (!reachedEtcetra && i < parameters.size() && parameters.get(i) instanceof EtceteraDecl) {
		reachedEtcetra = true;
	    }

	    // If we have not reached an etcetera, then the argument type must match its parameter's type.
            if (!reachedEtcetra && !argType.equals(parameters.get(i)._type)) {
                Utility.error("Argument " + arguments[i] + " is of type " + arguments[i].getType() +
			      ", expecting type " + parameters.get(i)._type,
			      arguments[i]._position);
            }

        }

        return _procedure._type;
	
    } // verify ()
    // =============================================================================================================================

    
    // =============================================================================================================================
    /**
     * Return the type of produced by evaluating this expression
     *
     * @return the type when evaluated.
     */
    public Type getType () {

        // TODO
        return null;

    } // getType ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly code that will call the specified procedure.
     *
     * @return the generated assembly code.
     */
    @Override
    public String toAssembly (Expression... operands) {

        String    assembly  = new String();
	Procedure procedure = operands[0].getProcedure();

        // According to x86-64 calling conventions, the caller should save these registers and store operands in the second array registers
        String[] operandRegisters   = {"rdi", "rsi", "rdx", "rcx", "r8", "r9"}; 

        assembly += "\t\t ; Setting up to call " + _procedure._name + "\n";

        // Generate assembly for the operands in reverse order (so that evaluation is later done left-to-right).
        for (int i = operands.length - 1; i >= 0; i -= 1) {
            assembly += operands[i].toAssembly();
        }

        // Load operands into the right registers according to x86-64 calling conventions
        for (int i = 0; i < operands.length; i++) {
            assembly += String.format("\tpop\t%s\t\t; Loading operand %d into register %s\n", operandRegisters[i], i, operandRegisters[i]);
        }

	// If variadic arguments are used by the procedure, indicate zero vector arguments.
	if (_procedure.usesVarargs()) {
	    assembly += "\tmov\trax,\t0\t; No vector arguments for varargs\n";
	}

        // Call the procedure
        assembly += "\tcall\t" + _procedure._name + "\n";

        // Push the return value onto the stack
        assembly += "\tpush\trax\t\t; Pushing the result of " + _procedure._name + " to the stack\n";

        // As is done in InternalProcedure, replace every instance of PROCEDURENAME with the right name
	// SK: But wait.  If we can do this replacement *here*, then we could have just used the proper name in the first place!
        // assembly = assembly.replaceAll("PROCEDURENAME", _procedure._name._text);

        return assembly;	

    } // toAssembly ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly code for any statics that are part of this expression.  The operands may generate statics, so recur to
     * them.
     *
     * @return the generated assembly code.
     */
    public String toStatics () {

	return new String();

    } // toStatics ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Verify that the right number of operands is being provided, and then verify any needed properties about those operands.
     *
     * @return the type that this operator will yield given the operands.
     */
    @Override
    public Type verify_l (Expression... operands) {

        // TODO
        return null;
	
    } // verify_l ()
    // =============================================================================================================================



    // =============================================================================================================================
    public String toString () {

        // TODO
        return null;
	
    } // toString ()
    // =============================================================================================================================



// =================================================================================================================================
} // class CallOperator
// =================================================================================================================================
