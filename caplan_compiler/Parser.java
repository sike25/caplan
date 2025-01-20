// =================================================================================================================================
// IMPORTS

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
// =================================================================================================================================



// =================================================================================================================================
/**
 * Perform lexical and syntactic analysis on an input, generating an internal representation of each element parsed.
 */
public class Parser {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS

    /** The list of tokens to parse. */
    private final List<Token> _tokens;

    /** The position within the token list during the parsing. */
    private int               _index;
    // =============================================================================================================================



    // =============================================================================================================================
    public Parser (List<Token> tokens) {

	_tokens = tokens;
	_index  = -1;

    } // Parser ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     *   <program> ::= <var list> <stmt list>
     *
     * @return A Program, containing a list of variable declarations followed by a list of expressions
     */
    public Program parse () {

	// Starting from the start of the tokens list...
	_index = 0;

	// <var list>
	List<VariableDeclaration> variables = parseVariableDeclarationList(false); // false = global, true = local

	// <procedure list>
	List<Procedure> procedures = parseProcedureList();

	// Return the newly constructed program from this list of expressions.
	return new Program(procedures, variables);

    } // parse ()
    // =============================================================================================================================
	


    // =============================================================================================================================
    private List<VariableDeclaration> parseParameterList () {
	
	List<VariableDeclaration> variables = new LinkedList<>();

	consumeWhitespace();
	if(endOfTokens()) return null;

	Token first = _tokens.get(_index++);
	if (first._type != Token.Type.OPENPAREN) {
	    Utility.error("Expected parameter list", first._position);
	}

	consumeWhitespace();

	// iterate over every variable declared
	while (_index < _tokens.size() && _tokens.get(_index)._type != Token.Type.CLOSEPAREN) {
	    //First check if it is an etcetera declaration:
	    Token t = _tokens.get(_index++);
	    if (t._type == Token.Type.ETCETERA) {
		variables.add(new EtceteraDecl(t));
	    } else {
		// parse this variable declaration
		_index--;
		variables.add(parseVariableDeclaration(true));
	    }




	    // break out if last variable
	    if (_tokens.get(_index)._type != Token.Type.COMMA) {
		break;
	    }

	    _index++;
	    consumeWhitespace();

	}

	if(endOfTokens()) return null;

	if (_tokens.get(_index)._type != Token.Type.CLOSEPAREN) {
	    Utility.error("Expected close bracket at " + _tokens.get(_index)._position);
	}

	_index++;
	Utility.debug(2, "Variable List: " + variables);
	return variables;

    } // parseParameterList ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     *  Parses the variable declaration at the start of the .src file
     *  [int i, char c, boolean b, int* p]
     *
     *    <var list> ::= '[' <vars> ']' | empty
     *    <vars>     ::= <var decl> ‘,' <var list> | <var decl>
     *
     * @param isLocal Whether these declarations are global (<code>false</code>) or local (<code>true</code>).
     * @return List of variable declarations
     */
    private List<VariableDeclaration> parseVariableDeclarationList (boolean isLocal) {

	List<VariableDeclaration> variables = new LinkedList<>();

	consumeWhitespace();
	if(endOfTokens()) return null;

	Token first = _tokens.get(_index++);
	if (first._type != Token.Type.OPENBRACKET) {
	    _index--;
	    return variables;
	}

	consumeWhitespace();

	// iterate over every variable declared
	while (_index < _tokens.size() && _tokens.get(_index)._type != Token.Type.CLOSEBRACKET) {
	    
	    // parse this variable declaration
	    variables.add(parseVariableDeclaration(isLocal));

	    // break out if last variable
	    if (_tokens.get(_index)._type != Token.Type.COMMA) {
		break;
	    }

	    _index++;
	    consumeWhitespace();

	}

	if(endOfTokens()) return null;

	if (_tokens.get(_index)._type != Token.Type.CLOSEBRACKET) {
	    Utility.error("Expected close bracket at " + _tokens.get(_index)._position);
	}

	_index++;
	Utility.debug(2, "Variable List: " + variables);
	return variables;

    } // parseVariableList ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Parses a single variable declaration, like int i or char c.
     * <var decl> ::= <type> <name>
     *
     * @param isLocal Whether these declarations are global (<code>false</code>) or local (<code>true</code>).
     * @return a VariableDeclaration object which stores type and name of variable.
     */
    private VariableDeclaration parseVariableDeclaration (boolean isLocal) {
	
	// parse the type of this variable
	Type type = parseType();
	consumeWhitespace();
	// parse the name of this variable
	if(endOfTokens()) return null;
	
	Token current = _tokens.get(_index++);

	if (current._type != Token.Type.NAME) {
	    Utility.error("Expected variable name at " + current._position);
	}
	consumeWhitespace();

	return isLocal ? new LocalVariableDecl(current,type) : new GlobalVariableDecl(current, type);
	
    } // parseVariableDeclaration ()
    // =============================================================================================================================



    // =============================================================================================================================
    private List<Procedure> parseProcedureList () {
	Procedure procedure = parseProcedure();
	if (procedure == null) {
	    return new LinkedList<Procedure>();
	}

	List<Procedure> procedures = parseProcedureList();
	procedures.addFirst(procedure);

	return procedures;
    }
    // =============================================================================================================================

    

    // =============================================================================================================================
    private Procedure parseProcedure () {
	
	int startIndex = _index;
	consumeWhitespace();
	if (endOfTokens()) return null;

	Token proc_token = _tokens.get(_index++);
	if (proc_token._type != Token.Type.PROC && proc_token._type != Token.Type.EXTERN) {
	    _index = startIndex;
	    return null;
	}
	
	Type type = parseType();
	if (type == null) Utility.error("Expected return type", _index);
	
	Token name = _tokens.get(_index++);
	if (name == null) Utility.error("Invalid name", _index);
	
	List<VariableDeclaration> params = parseParameterList();
	if (params == null) Utility.error("Invalid Parameter List", _index);
	
	if (proc_token._type == Token.Type.EXTERN) {
		return new ExternalProcedure(name, type, params);
	}

	List<VariableDeclaration> locals = parseVariableDeclarationList(true);
	if (locals == null) Utility.error("Invalid Locals List", _index);
	
	Statement body = parseStatement();
	if (body == null) Utility.error("Invalid Procedure Body",_index);

	return new InternalProcedure(name, type, params, locals, body);
    
    }
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Parses a list of statements.
     *   <stmt list> ::= <statement> <stmt list> | <EMPTY>
     *
     * @return A list of statements.
     */
    private List<Statement> parseStatementList () {

	// <statement>
	Statement statement = parseStatement();

	// If there was no statement, return the empty list of expressions.
	if (statement == null) {
	    return new LinkedList<Statement>();
	}

	// <stmt list>
	List<Statement> statements = parseStatementList();
	statements.addFirst(statement);

	return statements;

    } // parseStatementList ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Parse a statement.
     *   <statement> ::= <multi-stmt> | <print stmt> | <control stmt> | <expression>
     *
     * @return A statement if one is parsed; <code>null</code> otherwise.
     */
    private Statement parseStatement () {

	int startIndex = _index;
	Statement statement = null;

	// <multi-stmt>
	statement = parseMultiStatement();


	// ... | <control stmt>
	if (statement == null) {
	    statement = parseControlStatement();
	}

	// ... | return statement
		if (statement == null) {
			statement = parseReturnStatement();
		}

	// ... | <expression>
	if (statement == null) {
	    statement = parseExpression();
	}

	if (statement == null) {
	    _index = startIndex;
	}

	return statement;
	
    } // parseStatement ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Parses a multi-statement
     *   <multi stmt> ::= '{' | <stmt list> | '}'
     *
     * @return An multi-statement if one is parsed; <code>null</code> otherwise.
     */
    private MultiStatement parseMultiStatement () {

	// Mark the beginning.
	int startIndex = _index;


	// '{'
	consumeWhitespace();
	Token open = null;
	if ( endOfTokens() || (open = _tokens.get(_index++))._type != Token.Type.OPENBRACE ) {
	    _index = startIndex;
	    return null;
	}

	// <stmt list>
	List<Statement> statements = parseStatementList();

	// '}'
	consumeWhitespace();
	Token close = null;
	if (endOfTokens()) {
	    Utility.error("Input ends before close of multi-statement", open._position);
	}
	if ((close = _tokens.get(_index++))._type != Token.Type.CLOSEBRACE) {
	    Utility.error("Expected closing brace for multi-statement started at @" + open._position, close._position);
	}

	return new MultiStatement(open._position, statements);
	
    } // parseMultiStatement ()
    // =============================================================================================================================



    // =============================================================================================================================
	// =============================================================================================================================



    // =============================================================================================================================
    /**
     * Parses a control statement.
     *   <control stmt> ::= <conditional> | <loop>
     *
     * @return An control statement if one is parsed; <code>null</code> otherwise.
     */
    private ControlStatement parseControlStatement () {

	int startIndex = _index;
	consumeWhitespace();
	if (endOfTokens()) {
		return null;
	}

	// <conditional> 
	ControlStatement statement = parseConditional();

	// ... | <loop>
	if (statement == null) {
	    statement = parseLoop();
	}

	if (statement == null) {
	    _index = startIndex;
	}

	return statement;

    } // ControlStatement ()
    // =============================================================================================================================

	// =============================================================================================================================
	/**
	 * Parses a return statement.
	 *   <return stmt> ::= return <expression>
	 *
	 * @return A return statement if one is parsed; <code>null</code> otherwise.
	 */
	private ReturnStatement parseReturnStatement () {

		int startIndex = _index;
		consumeWhitespace();
		if (endOfTokens()) {
			return null;
		}
		Token t = _tokens.get(_index++);
		if (t._type != Token.Type.RETURN) {
			_index--;
			return null;
		}

		ReturnStatement rs =  new ReturnStatement(t, parseExpression());
		consumeWhitespace();
		//Currently parsing without assuming semicolon termination (currently whitespace terminated)
		return rs;


	} // ReturnStatement ()
	// =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Parses a conditional.
     *   <conditional> ::= 'if' <expression> <statement> [ 'else' <statement> ]
     *
     * @return An expression if one is parsed; <code>null</code> otherwise.
     */
    private IfThenStatement parseConditional () {

	// Mark the beginning.
	int startIndex = _index;

	// 'if'
	consumeWhitespace();
	if (endOfTokens()) {
	    _index = startIndex;
	    return null;
	}
	Token ifToken = _tokens.get(_index++);
	if (ifToken._type != Token.Type.IF) {
	    _index = startIndex;
	    return null;
	}

	// <expression>
	Expression condition = parseExpression();
	if (condition == null) {
	    Utility.error("Conditional expression expected", ifToken._position);
	}

	// <statement>
	Statement thenBranch = parseStatement();
	if (thenBranch == null) {
	    Utility.error("Conditional then-branch expected", ifToken._position);
	}

	// [ 'else'
	int   endOfThenIndex = _index;
	Token elseToken      = null;
	consumeWhitespace();
	if ( endOfTokens() || (elseToken = _tokens.get(_index++))._type != Token.Type.ELSE ) {

	    // No 'else', so reset to the end of the then-branch and return the complete if-then.
	    _index = endOfThenIndex;
	    return new IfThenStatement(ifToken._position, condition, thenBranch);
	    
	} else {

	    // There is an 'else', so complete it and return the resulting if-then-else.
	    // <statement> ]
	    Statement elseBranch = parseStatement();
	    if (elseBranch == null) {
		Utility.error("Incomplete else-branch", elseToken._position);
	    }
	    return new IfThenElseStatement(ifToken._position, condition, thenBranch, elseBranch);

	}

    } // parseConditional ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Parses a while-loop.
     *   <loop> ::= 'while' <expression> <statement>
     *
     * @return A while-loop if one is parsed; <code>null</code> otherwise.
     */
    private WhileStatement parseLoop () {

	// Mark the beginning.
	int startIndex = _index;

	// 'while'
	consumeWhitespace();
	if (endOfTokens()) {
	    _index = startIndex;
	    return null;
	}
	Token whileToken = _tokens.get(_index++);
	if (whileToken._type != Token.Type.WHILE) {
	    _index = startIndex;
	    return null;
	}

	// <expression>
	Expression condition = parseExpression();
	if (condition == null) {
	    Utility.error("Loop condition expected", whileToken._position);
	}	    

	// <statement>
	Statement body = parseStatement();
	if (body == null) {
	    Utility.error("Loop body expected", whileToken._position);
	}

	return new WhileStatement(whileToken._position, condition, body);

    } // parseLoop ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Parses an expression,
     *   <exp> ::= <literal> | <memory> | <sizeof> | <operation> | <name>
     *
     * @return An expression if one is parsed; <code>null</code> otherwise.
     */
    private Expression parseExpression () {

	int startIndex = _index;
	consumeWhitespace();
	if (endOfTokens()) {
	    return null;
	}
	Expression expression = null;

	// ... | <literal>
	if (expression == null) {
	    _index = startIndex;
	    expression = parseLiteral();
	}

	// ... | <sizeof>
	if (expression == null) {
	    _index = startIndex;
	    expression = parseSizeOf();
	}

	// ... | <operation>
	if (expression == null) {
	    _index = startIndex;
	    expression = parseOperation();
	}

	// ... | <name>
	if (expression == null) {
	    consumeWhitespace();
	    Token token = _tokens.get(_index++);
	    if (token._type == Token.Type.NAME) {
		expression = new Variable(token);
	    }
	}

	if (expression == null) {
		_index = startIndex;
	}

	return expression;

    } // parseExpression ()
    // =============================================================================================================================


    // =============================================================================================================================
    /**
     * Parses a literal,
     * <literal> ::= <int literal> | <char literal> | <bool literal>
     * @return A literal if one is parsed, null otherwise
     */
    private Literal parseLiteral () {

	int startIndex = _index;
	consumeWhitespace();
		
	Token token = _tokens.get(_index++);
	Literal literal = switch (token._type) {
	case Token.Type.INTEGER -> new IntegerLiteral(token);
	case Token.Type.CHAR    -> new CharacterLiteral(token);
	case Token.Type.BOOLEAN -> new BooleanLiteral(token);
	case Token.Type.STRING  -> new StringLiteral(token);
	default                 -> null;
	};

	if (literal == null) _index = startIndex;
	
	return literal;

    } // parseLiteral ()
    // =============================================================================================================================


    // =============================================================================================================================
    /**
     * Parse an operation.
     * <operation> ::= '(' <operator> <expression list> ')'
     *
     * @return An operation, if parsed; <code>null</code> otherwise.
     */
    private Operation parseOperation () {

	// Mark beginning.
	int startIndex = _index;

        // '('
        consumeWhitespace();
        Token openToken;
        if ( endOfTokens() || (openToken = _tokens.get(_index++))._type != Token.Type.OPENPAREN ) {
            _index = startIndex;
            return null;
        }

        // <operator>
	// Get only the token since the specific operator will depend later on the operands.
        Token operatorToken = _tokens.get(_index++);

	// <expression list>
        List<Expression> operands = parseExpressionList();

        // ')'
        consumeWhitespace();
        if ( endOfTokens() || _tokens.get(_index++)._type != Token.Type.CLOSEPAREN ) {
            Utility.error("Close parentheses expected at end of operation", _tokens.get(_index-1)._position);
        }

        return new Operation(openToken._position, operatorToken, operands);

    } // parseOperation ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Parses a list of expressions
     * <expression list> ::= <expression> <expression list> | <EMPTY>
     * @return A list of expressions.
     */
    private List<Expression> parseExpressionList () {

	// Attempt to parse one expression.
	Expression expression = parseExpression();
	System.out.println(_index + " : " + expression);

	// If there was none, return the empty list of expressions
	if (expression == null) {
	    return new LinkedList<Expression>();
	}

	// There was an expression, so parse the remaining list of them, and then combine the one with the many to return.
	List<Expression> expressions = parseExpressionList();
	expressions.addFirst(expression);

	return expressions;

    } // parseExpressionList ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Parse a sizeof() expression.
     *   <sizeof> ::= 'sizeof(' <type> ')'
     * @return The integer literal yielded by a correctly parsed sizeof(); <code>null</code> if parsing fails.
     */
    private IntegerLiteral parseSizeOf() {

	// Mark beginning.
	int startIndex = _index;

	// 'sizeof'
	consumeWhitespace();
	Token sizeofToken = null;
	if ( endOfTokens() || (sizeofToken = _tokens.get(_index++))._type != Token.Type.SIZEOF ) {
	    _index = startIndex;
            return null;
	}

	// '('
	consumeWhitespace();
	Token openToken = null;
	if ( endOfTokens() || (openToken = _tokens.get(_index++))._type != Token.Type.OPENPAREN ) {
	    Utility.error("Open parenthesis expected after sizeof", sizeofToken._position);
	}

	// <type>
	consumeWhitespace();
	Type type = parseType();
	if (type == null) {
	    Utility.error("sizeof requires type", openToken._position);
	}

	// ')'
        consumeWhitespace();
	Token closeToken = null;
	if ( endOfTokens() || (closeToken = _tokens.get(_index++))._type != Token.Type.CLOSEPAREN ) {
	    Utility.error("Close parenthesis expected at end of sizeof", type._position);
	}

	return new IntegerLiteral(sizeofToken._position, type.getSize());
	
    } // parseSizeOf ()
    // =============================================================================================================================


    
    // =============================================================================================================================
	// =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Parse a type declaration. It is always known unambiguously when a type is expected (before a variable declaration or inside a
     * sizeof()), so this method does not fail silently; it signals an error directly.
     *parseType () {
	
	int startIndex = _index;
	consumeWhitespace();

	Token nameToken = _tokens.get(_index++);
	if (nameToken._type != Token.Type.NAME) {
	    Utility.error("Expected type name at " + nameToken._position );
	}
	
	Type type = switch(nameToken._text) {
	case "char" -> new TypeCharacter(nameToken);
	case "int"  -> new TypeInteger(nameToken);
	case "bool" -> new TypeBoolean(nameToken);
	case "void" -> new TypeVoid(nameToken._position);
	default     -> null;
	};

	if (type == null) {
	    Utility.error("Invalid type name " + nameToken._text + " at " + nameToken._position );
	}

	consumeWhitespace();
	Token nextToken = _tokens.get(_index++);

	// Determine the number of indirections to the base type.
	while (nextToken._type == Token.Type.STAR) {
	    type = new TypePointer(nextToken, type);
	    consumeWhitespace();
	    if (endOfTokens()) break;
	    nextToken = _tokens.get(_index++);
	}
		
	_index--;
	return type;
    }
    // =============================================================================================================================


    
    // =============================================================================================================================
    /**
     * Consume whitespace tokens, leaving the position at the first non-whitespace token encountered.
     * @return The sequence of consecutive whitespace tokens consumed.
     */
    private List<Token> consumeWhitespace () {

	// Read whitespace tokens until there are no more, or we encounter a non-whitespace token.
	List<Token> whitespaces = new LinkedList<Token>();
	Token       token       = null;
	while ( !endOfTokens() && (token = _tokens.get(_index++)).isWhitespace() ) {
	    whitespaces.add(token);
	}

	// Put the final token back into play if we didn't hit the end of the input
	// or if the final token of the input was not whitespace (and might be meaningful).
	if ( !endOfTokens() || (token != null && !token.isWhitespace()) ) {
	    _index -= 1;
	}

	return whitespaces;

    } // consumeWhitespace ()
    // =============================================================================================================================

    

    // =============================================================================================================================
    private boolean endOfTokens () {

	return _index >= _tokens.size();

    } // endOfTokens ()
    // =============================================================================================================================



    // =============================================================================================================================
    // Testing methods
    public static void main(String[] args) {
	testVariableDeclarationList();
	System.out.println();
	testType();
	System.out.println();
	testMemory();
	System.out.println();
	testSizeOf();
    }

    public static void testVariableDeclarationList() {
	ArrayList<Token> tokens = new ArrayList<>();
	tokens.add(new Token("[", 0, Token.Type.OPENBRACKET));
	tokens.add(new Token("one", 1, Token.Type.NAME));
	tokens.add(new Token(2, Token.Type.STAR));
	tokens.add(new Token(3, Token.Type.STAR));
	tokens.add(new Token(4, Token.Type.STAR));
	tokens.add(new Token("two", 5, Token.Type.NAME));
	tokens.add(new Token(",", 6, Token.Type.COMMA));
	tokens.add(new Token("char", 7, Token.Type.NAME));
	tokens.add(new Token("three", 8, Token.Type.NAME));
	tokens.add(new Token("]", 9, Token.Type.CLOSEBRACKET));

	System.out.println("Testing variable declaration list parsing...");
	System.out.println("Scanned variable list should be: [one*** two, char three]");
	Parser parser = new Parser(tokens);
	parser._index = 0;
	System.out.println(parser.parseVariableDeclarationList(false));
    }

    public static void testType() {
	// int***
	ArrayList<Token> tokens = new ArrayList<>();
	tokens.add(new Token("int", 0, Token.Type.NAME));
	tokens.add(new Token(1, Token.Type.STAR));
	tokens.add(new Token(2, Token.Type.STAR));
	tokens.add(new Token(3, Token.Type.STAR));
	tokens.add(new Token(4, Token.Type.NEWLINE)); // All token lists must end with a newline!

	System.out.println("Testing type parsing...");

	Parser parser = new Parser(tokens);
	parser._index = 0; // Since we aren't calling parse() directly, _index is -1. We need to tell the parser that this is not a mistake, and we really do want to call a submethod directly
	Type t = parser.parseType();

	System.out.println("Scanned type (should be int***): " + t);
	System.out.println("Pointed type (should be int**): " + t.getPointedType());
    }


    public static void testMemory() {
	// memory(5)
	ArrayList<Token> tokens = new ArrayList<>();
	tokens.add(new Token("memory", 0, Token.Type.NAME));
	tokens.add(new Token(3, Token.Type.OPENPAREN));
	tokens.add(new Token(4, Token.Type.SPACE));
	tokens.add(new Token(5, Token.Type.SPACE));

	tokens.add(new Token("5", 6, Token.Type.INTEGER));

	tokens.add(new Token(7, Token.Type.NEWLINE));
	tokens.add(new Token(8, Token.Type.CLOSEPAREN));
	tokens.add(new Token(2, Token.Type.NEWLINE)); // All token lists must end with a newline!


	System.out.println("Testing memory parsing...");
	Parser parser = new Parser(tokens);
	parser._index = 0; // Since we aren't calling parse() directly, _index is -1. We need to tell the parser that this is not a mistake, and we really do want to call a submethod directly
	Expression memory = parser.parseExpression();
	System.out.println("Scanned memory token (should be (memory 5)): " + memory);
    }

    public static void testSizeOf() {
	// sizeof(int)
	ArrayList<Token> tokens = new ArrayList<>();
	tokens.add(new Token("sizeof", 0, Token.Type.NAME));
	tokens.add(new Token(1, Token.Type.OPENPAREN));
	tokens.add(new Token("int", 2, Token.Type.NAME));
	tokens.add(new Token(4, Token.Type.CLOSEPAREN));

	System.out.println("Testing sizeOf...");
	System.out.println("Scanned sizeOf token (should be (sizeof int))");
	Parser parser = new Parser(tokens);
	parser._index = 0;
	System.out.println(parser.parseExpression());
    }

// =================================================================================================================================
} // class Parser
// =================================================================================================================================
