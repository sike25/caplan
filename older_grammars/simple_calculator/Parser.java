import java.util.List;
import java.util.LinkedList;

/**
 * Perform lexical and syntactic analysis on an input, generating an internal representation of each element parsed.
 */
public class Parser {
    /** The list of tokens to parse. */
    private final List<Token> _tokens;
    /** The position within the token list during the parsing. */
    private int _index;

	public Parser (List<Token> tokens) {
		_tokens = tokens;
		_index  = -1;
    }

	/**
     * <program> ::= <expression list>
     * @return A <code>Program</code>, which contains lists of expressions.
     */
    public Program parse () {
		// Parse declarations, starting at the beginning of the tokens list.
		_index = 0;
		List<Expression> expressions = parseExpressionList();
		// Return the newly constructed program from this list of expressions.
		return new Program(expressions);
    }

	/**
     * <expression list> ::= <expression> <expression list> | <EMPTY>
     * @return A list of expressions.
     */
    private List<Expression> parseExpressionList () {
		if (endOfTokens()) {
			return new LinkedList<>();
		}

		// Attempt to parse one expression.
		Expression expression = parseExpression();
		// If there was none, return the empty list of expressions.
		if (expression == null) {
			return new LinkedList<>();
		}
		// There was an expression, so parse the remaining list of them, and then combine the one with the many to return.
		List<Expression> expressions = parseExpressionList();

		expressions.addFirst(expression);
		return expressions;
    }

	/**
     * <expression> ::= <integer> | <operation>
     * @return An expression is one is parsed; <code>null</code> otherwise.
     */
    private Expression parseExpression () {
		int startIndex = _index;
        consumeWhite();

        if (endOfTokens()) {
			return null;
		}

		Token token = _tokens.get(_index++);

		// <integer>
		if (token._type == Token.Type.INTEGER) {
			return new IntegerLiteral(token);
		}

		// ... | <operation>
		// Reset to starting position before trying to parse differently.
		_index = startIndex;
		return parseOperation();
    }


	private Expression parseOperation() {
		consumeWhite();

		int idx = _index++;
		Token token = _tokens.get(idx);

		if (isOperator(token)) {
			Operator operator = identifyOperator(token);
			Expression operandOne = parseExpression();
			Expression operandTwo = parseExpression();
			if (operandTwo == null) {
				Utility.error("Missing operand(s)", operator._position);
			}
			return new Operation(operator, operandOne, operandTwo, operator._position);
		} else if (token._type == Token.Type.OPENPAREN) {
			Expression expression = parseOperation();

			// Consume the close parentheses.
			if (endOfTokens()) {
				System.out.println(_index + " " + _tokens.size());
				Utility.error("File ended suddenly.", _tokens.get(_index - 1)._position);
			}
			token = _tokens.get(_index++);
			if (token._type != Token.Type.CLOSEPAREN) {
				Utility.error("Operation that started with a bracket not ending with a bracket", token._position);
			}

			return expression;
		}
		else {
			Utility.error("Operation not starting with an operator or bracket. But with ' " + token._text + "' .", token._position);
		}
		return null;
	}


	private boolean isOperator(Token token) {
		return (token._type == Token.Type.PLUS || token._type == Token.Type.DASH || token._type == Token.Type.SLASH || token._type == Token.Type.PERCENT || token._type == Token.Type.STAR);
	}

	private Operator identifyOperator(Token token) {
        return switch (token._type) {
            case PLUS    -> new Add(token);
            case DASH    -> new Subtract(token);
            case STAR    -> new Multiply(token);
            case SLASH 	 -> new Divide(token);
            case PERCENT -> new Modulo(token);
            default -> {
                Utility.error("Error from Parser: " + token._text + " is not an operator", token._position);
                yield new Add(token); // fallback
            }
        };
	}

	/**
	 * Consume whitespace tokens, leaving the position at the first non-whitespace token encountered.
	 */
	private void consumeWhite() {
		while (!endOfTokens() && (_tokens.get(_index)).isWhitespace() ) {
			_index++;
		}
	}

    /**
     * Obsolete.
     */
	private List<Token> consumeWhitespace () {
		List<Token> whitespaces = new LinkedList<>();
		Token token;
		while ( !endOfTokens() && (token = _tokens.get(_index++)).isWhitespace() ) {
			whitespaces.addLast(token);
		}
		if (!endOfTokens()) {
			_index -= 1;
		}
		return whitespaces;
	}

	private boolean endOfTokens () {
		return _index >= _tokens.size();
    }
}