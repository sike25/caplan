/**
 * An operator which, when applied to operands, yields a value.
 */
abstract public class Operator {
    /** The position in the source at which this operator appears. */
    public final int   _position;
    /** The token that specified this operator. */
    public final Token _token;

    /**
     * Create an operator.
     * @param token The token that specifies this operator.
     */
    public Operator (Token token) {
        _position = token._position;
        _token    = token;
    }

    public String toString () {
	    return _token._text;
    }
}

class Add extends Operator {
    public Add(Token token) {
        super(token);
    }
}

class Subtract extends Operator {
    public Subtract(Token token) {
        super(token);
    }
}

class Multiply extends Operator {
    public Multiply(Token token) {
        super(token);
    }
}

class Divide extends Operator {
    public Divide(Token token) {
        super(token);
    }
}

class Modulo extends Operator {
    public Modulo(Token token) {
        super(token);
    }
}


