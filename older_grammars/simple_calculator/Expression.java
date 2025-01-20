/**
 * An expression which, when evaluated, yields some value.
 */
abstract public class Expression {
    /** The position in the source code at which the expression begins. */
    public final int _position;

    /** Service that replicates the evaluation of expressions to provide more detailed comments. */
    public final static MockCompiler mockCompiler = new MockCompiler();

    /**
     * Create an expression.  The type is not provided because it is assumed to be unknown (pending binding).
     * @param position The location in the source code at which the expression begins.
     */
    public Expression (int position) {
	    _position = position;
    }

    @Override
    abstract public String toString();

    abstract public String toAssembly();
}

class IntegerLiteral extends Expression {
    final Token _token;
    public String _operandPosition = "one";

    public IntegerLiteral(Token token) {
        super(token._position);
        _token = token;
    }

    @Override
    public String toString() {
        return "(" + _token._text + ")";
    }

    public String toAssembly() {
        StringBuilder sb = new StringBuilder();

        sb.append("push ")
                .append(_token._text)
                .append("          ;Push operand ")
                .append(_operandPosition)
                .append(" (")
                .append(_token._text)
                .append(")")
                .append(" onto the stack ")
                .append("\n");

        // Replicate this logic on our mock compiler
        if (_token._text != null) {
            int token_int = Integer.parseInt(_token._text);
            mockCompiler.stack.push(token_int);
        }

        return sb.toString();
    }

}

class Operation extends Expression {
    final Expression _operandOne;
    final Expression _operandTwo;
    final Operator _operator;

    public Operation(Operator operator, Expression operandOne, Expression operandTwo, int position) {
        super(position);
        _operandOne = operandOne;
        _operandTwo = operandTwo;
        _operator   = operator;
    }

    @Override
    public String toString() {
        return "(" + _operator._token._text + " " + _operandOne.toString() + " " + _operandTwo.toString() + ")";
    }

    public String toAssembly() {

        StringBuilder sb = new StringBuilder();

        sb.append(_operandOne.toAssembly());
        if (_operandTwo instanceof IntegerLiteral) {
            ((IntegerLiteral) _operandTwo)._operandPosition = "two";
        }
        sb.append(_operandTwo.toAssembly());
        sb.append("\n");

        // replicate the popping logic in the mock compiler
        mockCompiler.rbx = mockCompiler.stack.pop();
        mockCompiler.rax = mockCompiler.stack.pop();

        // reproduce the popping logic in assembly
        sb.append("pop rbx         ;Pop operand two (").append(mockCompiler.rbx).append(") into rbx\n");
        sb.append("pop rax         ;Pop operand one (").append(mockCompiler.rax).append(") into rax\n");

        // reproduce the arithmetic logic in the mock compiler and in assembly
        doArithmeticOperationInMockCompiler(_operator._token);
        sb.append(getArithmeticOperation(_operator._token));

        // reproduce the pushing logic in the mock compiler and in assembly
        if (isModulo(_operator._token)) {
            sb.append("push rdx        ;Push the results (").append(mockCompiler.rdx).append(") back onto the stack\n");
            mockCompiler.stack.push(mockCompiler.rdx);
        } else {
            sb.append("push rax        ;Push the results (").append(mockCompiler.rax).append(") back onto the stack\n");
            mockCompiler.stack.push(mockCompiler.rax);
        }
        sb.append("\n");
        return sb.toString();
    }

    private boolean isModulo(Token token) {
        return token._type == Token.Type.PERCENT;
    }

    private void doArithmeticOperationInMockCompiler(Token token) {
        switch (token._type) {
            case PLUS -> mockCompiler.add();
            case DASH -> mockCompiler.subtract();
            case STAR -> mockCompiler.multiply();
            case SLASH, PERCENT -> mockCompiler.divide();
            default -> {}
        }
    }

    private String getArithmeticOperation(Token token) {
        return switch (token._type) {
            case PLUS    -> "add rax, rbx    ;Perform the addition\n";
            case DASH    -> "sub rax, rbx    ;Perform the subtraction\n";
            case STAR    -> "imul rbx        ;Perform the multiplication. Result is now in rax\n";
            case SLASH   -> "idiv rbx        ;Perform the division. Result is now in rax\n";
            case PERCENT -> "idiv rbx        ;Perform the division. Result is now in rdx\n";
            default -> {
                Utility.error("Error from Expression: " + token._text + " is not an operator", token._position);
                yield "error"; // fallback
            }
        };
    }
}



