// =================================================================================================================================
// IMPORTS

import java.util.List;
import java.util.Map;
// =================================================================================================================================



// =================================================================================================================================
/**
 * An operation on two operands that yields some value.
 */
public class Operation extends Expression {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS

    /** The operator to apply to evaluate this operation. */
    public       Operator     _operator;

    /** The operator token corresponding to this operation */
    public final Token        _token;

    /** The operands on which to apply the operator. */
    public final Expression[] _operands;

    /** The type of this operation's evaluation result. */
    private      Type         _type;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Create an operation.
     * 
     * @param position The location in the source code at which the expression begins.
     * @param token    The token of the operator to apply; the specific operator will be determine later.
     * @param operands The operands on which to apply the operator.
     */
    public Operation (int position, Token token, List<Expression> operands) {

	super(position);
	_token    = token;
	_operands = operands.toArray(new Expression[0]);

	// The type is determined later, during verification (see verify()).
	_type     = null; 

    } // Operation ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Whether this expression can have a reference to itself.  Whether it can be referenced (act as an l-value) depends on which
     * operator is being used.
     *
     * @return Whether this operation's operator yields a reference.
     */
    public boolean hasReference () {

	// Sanity check.
	if (_operator == null) {
	    Utility.abort("Operation.hasReference(): Unexpected null operator (token = " + _token + ")");
	}
        return _operator.hasReference();
	
    } // hasReference ()
    // =============================================================================================================================

    

    // =============================================================================================================================
    /**
     * Bind each variable used to its declaration.  Signal an error if no matching declaration is found.
     *
     * @param procedure The procedure to which this expression belongs.
     * @param symbols The symbol table of variable declarations.
     */
    public void bind (Procedure procedure, Map<String, Declaration> symbols) {

        determineOperator();
	_operator.bind(symbols);

	setProcedure(procedure);
        for (Expression operand : _operands) {
            operand.bind(procedure, symbols);
        }
	
    } // bind ()
    // =============================================================================================================================

    
    
    // =============================================================================================================================
    /**
     * Additionally, determine the specific operator based on the operands.
     *
     * @return the type produced by evaluation.
     */
    public Type verify () {

	// Verify the operands by passing them to the operator, which controls how each operand is verified (as an l-value or an
	// r-value).
	return _type = _operator.verify(_operands);
	
    } // verify ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Map the operator token and the operand list to a specific operator.
     */
    private void determineOperator () {
	
        if (_operands.length == 0) {
	    Utility.error("operation has no arguments", _position);
	}

        switch(_token._type) {
	    
	case Token.Type.PLUS:
	    if (_operands.length == 1) {
		Utility.error("addition (+) operation at " + _position + " requires at least two arguments");
	    }
	    _operator = new AdditionOperator(_token);
	    break;
	case Token.Type.DASH:
	    if(_operands.length > 2) {
		Utility.error("subtraction (-) operation at "+ _position + " accepts at most two arguments");
	    } else if (_operands.length == 2) {
		_operator = new SubtractionOperator(_token);
	    } else {
		_operator = new NegationOperator(_token);
	    }
	    break;
	case Token.Type.STAR:
	    if(_operands.length == 1) {
		_operator = new DereferenceOperator(_token);
	    } else {
		_operator = new MultiplicationOperator(_token);
	    }
	    break;
	case Token.Type.SLASH:
	    if(_operands.length != 2) Utility.error("division (/) operation requires two arguments", _position);
	    _operator = new DivisionOperator(_token);
	    break;
	case Token.Type.PERCENT:
	    if(_operands.length != 2) Utility.error("modulus (%) operation requires two arguments", _position);
	    _operator = new ModulusOperator(_token);
	    break;
	case Token.Type.EQUALS:
	    if(_operands.length != 2) Utility.error("assignment (=) operation requires two arguments", _position);
	    _operator = new AssignmentOperator(_token);
	    break;
	case Token.Type.DOUBLEEQUALS:
	    if(_operands.length != 2) Utility.error("double-equals (==) operation requires two arguments", _position);
	    _operator = new EqualityOperator(_token);
	    break;
	case Token.Type.BANGEQUALS:
	    if(_operands.length != 2) Utility.error("not-equals (!=) operation requires two arguments", _position);
	    _operator = new NotEqualityOperator(_token);
	    break;
	case Token.Type.LEFTANGLE:
	    if(_operands.length != 2) Utility.error("less-than (<) operation requires two arguments", _position);
	    _operator = new LessThanOperator(_token);
	    break;
	case Token.Type.LEFTANGLEEQUALS:
	    if(_operands.length != 2) Utility.error("less-than-or-equals (<=) operation requires two arguments", _position);
	    _operator = new LessThanEqualsOperator(_token);
	    break;
	case Token.Type.RIGHTANGLE:
	    if(_operands.length != 2) Utility.error("greater-than (>) operation requires two arguments", _position);
	    _operator = new GreaterThanOperator(_token);
	    break;
	case Token.Type.RIGHTANGLEEQUALS:
	    if(_operands.length != 2) Utility.error("greater-than-or-equals (>=) operation requires two arguments", _position);
	    _operator = new GreaterThanEqualOperator(_token);
	    break;
	case Token.Type.DOUBLEBAR:
	    if(_operands.length != 2) Utility.error("logical or (||) operation requires two arguments", _position);
	    _operator = new LogicalOrOperator(_token);
	    break;
	case Token.Type.DOUBLEAMPERSAND:
	    if(_operands.length != 2) Utility.error("logical and (&&) operation requires two arguments", _position);
	    _operator = new LogicalAndOperator(_token);
	    break;
	case Token.Type.BAR:
	    if(_operands.length != 2) Utility.error("bitwise or (|) operation requires two arguments", _position);
	    _operator = new BitwiseOrOperator(_token);
	    break;
	case Token.Type.AMPERSAND:
	    if(_operands.length != 2) Utility.error("bitwise and (&) operation requires two arguments", _position);
	    _operator = new BitwiseAndOperator(_token);
	    break;
	case Token.Type.CARAT:
	    if(_operands.length != 2) Utility.error("bitwise xor (^) operation requires two arguments", _position);
	    _operator = new BitwiseXorOperator(_token);
	    break;
	case Token.Type.TILDE:
	    if(_operands.length != 1) Utility.error("bitwise not (~) operation requires one argument", _position);
	    _operator = new BitwiseNotOperator(_token);
	    break;
	case Token.Type.AT:
	    if(_operands.length != 1) Utility.error("reference (@) operation requires one argument", _position);
	    _operator = new ReferenceOperator(_token);
	    break;
	case Token.Type.BANG:
	    if(_operands.length != 1) Utility.error("logical not (!) operation requires one argument", _position);
	    _operator = new LogicalNotOperator(_token);
	    break;
	case Token.Type.NAME:
	    _operator = new CallOperator(_token);
	    break;
	default:
	    Utility.abort("Operation at " + _position + " has an invalid operator token");

        }

    } // determineOperator ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Return the type of produced by evaluating this expression.
     *
     * @return the type when evaluated.
     */
    public Type getType () {

	if (_type == null) {
	    Utility.abort("Operation.getType(): _type is null");
	}
	
	return _type;

    } // getType ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly code that will execute this expression.
     *
     * @return the generated assembly code.
     */
    public String toAssembly () {

        return _operator.toAssembly(_operands);

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

	String assembly = new String();

	for (Expression operand : _operands) {
	    assembly += operand.toStatics();
	}

	return assembly;

    } // toStatics ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Verify that the right number of operands is being provided, and then verify any needed properties about those operands.
     *
     * @return the type that this operator will yield given the operands.
     */
    @Override
    public Type verify_l () {

	// First, verify as it would be for an r-value.
	verify();

	// Now, check if the operator on its operands can be an l-value.
	_operator.verify_l(_operands);

	return _type;
	
    } // verify_l ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Generate assembly that will evaluate this operator as an l-value.
     * 
     * @return the generated assembly code.
     */
    @Override
    public String toAssembly_l () {

	// Assume that verification has validated the ability to invoke l-value assembly generation on this operator.
	return _operator.toAssembly_l(_operands);
	
    } // toAssembly_l ()
    // =============================================================================================================================


    // =============================================================================================================================
    public String toString () {

	String s = "(" + _token._text + ' ';
	for (int i = 0; i < _operands.length; i += 1) {
	    s += _operands[i].toString();
	    if (i < _operands.length - 1) {
		s += ' ';
	    }
	}
	s += ')';
	return s;
	
    } // toString ()
    // =============================================================================================================================

    

// =================================================================================================================================
} // class Operation
// =================================================================================================================================
