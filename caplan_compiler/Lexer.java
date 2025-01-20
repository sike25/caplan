// =================================================================================================================================
// IMPORTS

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
// =================================================================================================================================



// =================================================================================================================================
/**
 * Perform lexical on an input, generating a sequence of <code>Token</code>s.
 */
public class Lexer {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS

    /** The sequence of characters. */
    private List<Character> _source;

    /** The position within the source during a scan. */
    private int             _position;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Create a lexer for a particular input stream of characters.  Pre-read the full input, preparing it to be scanned.
     *
     * @param source The input character sequence that forms the source code.
     */
    public Lexer (List<Character> source) {

	_source   = source;
	_position = 0;

    } // Lexer ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Attempt to scan the sequence of characters into a sequence of tokens.
     *
     * @return A list of <code>Token</code>s found.
     */
    public List<Token> scan () {

	// Prepare to move through the character stream.
	List<Token> tokens = new ArrayList<Token>();

	// Scan the input.  Track positioning within the input.
	_position = 0;
	while (!endOfSource()) {

	    // Useful locals during scanning.
	    int     startPosition = -1;

	    // Grab the next character.
	    char   c     = _source.get(_position);
	    String text  = "";
	    Token  token = null;

	    switch (c) {

	    // Separator?
	    case ',':
		token = new Token(_position, Token.Type.COMMA);
		break;
		
	    // Whitespace?
	    case ' ':
		token = new Token(_position, Token.Type.SPACE);
		break;
	    case '\t':
		token = new Token(_position, Token.Type.TAB);
		break;
	    case '\n':
	    case '\r':
		token = new Token(_position, Token.Type.NEWLINE);
		break;
	    
	    // Open-closes?
	    case '(':
		token = new Token(_position, Token.Type.OPENPAREN);
		break;
	    case ')':
		token = new Token(_position, Token.Type.CLOSEPAREN);
		break;
	    case '[':
		token = new Token(_position, Token.Type.OPENBRACKET);
		break;
	    case ']':
		token = new Token(_position, Token.Type.CLOSEBRACKET);
		break;
	    case '{':
		token = new Token(_position, Token.Type.OPENBRACE);
		break;
	    case '}':
		token = new Token(_position, Token.Type.CLOSEBRACE);
		break;

	    // Operator?
	    case '@':
		token = new Token(_position, Token.Type.AT);
		break;
	    case '!': {
		_position += 1;
		if ( !endOfSource() && (c = _source.get(_position)) == '=' ) {
		    token = new Token(_position - 1, Token.Type.BANGEQUALS);
		} else {
		    _position -= 1;
		    token = new Token(_position, Token.Type.AT);
		}
		break;
	    }
	    case '~':
		token = new Token(_position, Token.Type.TILDE);
		break;
	    case '^':
		token = new Token(_position, Token.Type.CARAT);
		break;
	    case '+':
		token = new Token(_position, Token.Type.PLUS);
		break;
	    case '-':
		token = new Token(_position, Token.Type.DASH);
		break;
	    case '*':
		token = new Token(_position, Token.Type.STAR);
		break;
	    case '/':
		token = new Token(_position, Token.Type.SLASH);
		break;
	    case '%':
		token = new Token(_position, Token.Type.PERCENT);
		break;
	    case '=': {
		_position += 1;
		if ( !endOfSource() && (c = _source.get(_position)) == '=' ) {
		    token = new Token(_position - 1, Token.Type.DOUBLEEQUALS);
		} else {
		    _position -= 1;
		    token = new Token(_position, Token.Type.EQUALS);
		}
		break;
	    }
	    case '<': {
		_position += 1;
		if ( !endOfSource() && (c = _source.get(_position)) == '=' ) {
		    token = new Token(_position - 1, Token.Type.LEFTANGLEEQUALS);
		} else if (c == '<') {
		    token = new Token(_position - 1, Token.Type.DOUBLELEFTANGLE);
		} else {
		    _position -= 1;
		    token = new Token(_position, Token.Type.LEFTANGLE);
		}
		break;
	    }
	    case '>': {
		_position += 1;
		if ( !endOfSource() && (c = _source.get(_position)) == '=' ) {
		    token = new Token(_position - 1, Token.Type.RIGHTANGLEEQUALS);
		} else if (c == '>') {
		    token = new Token(_position - 1, Token.Type.DOUBLERIGHTANGLE);
		} else {
		    _position -= 1;
		    token = new Token(_position, Token.Type.RIGHTANGLE);
		}
		break;
	    }
	    case '|': {
		_position += 1;
		if ( !endOfSource() && (c = _source.get(_position)) == '|' ) {
		    token = new Token(_position - 1, Token.Type.DOUBLEBAR);
		} else {
		    _position -= 1;
		    token = new Token(_position - 1, Token.Type.BAR);
		}
		break;
	    }
	    case '&': {
		_position += 1;
		if ( !endOfSource() && (c = _source.get(_position)) == '&' ) {
		    token = new Token(_position - 1, Token.Type.DOUBLEAMPERSAND);
		} else {
		    _position -= 1;
		    token = new Token(_position - 1, Token.Type.AMPERSAND);
		}
		break;
	    }

	    // Comment?
	    case '#': {
		    
		// Read characters until the end-of-line is reached.
		startPosition = _position;
		while (true) {

		    _position += 1;
		    if (endOfSource()) {
			Utility.error("Input ended mid-comment", _position);
		    }
		    c = _source.get(_position);
		    if (c == '\n') {
			break;
		    }
		    
		}

		// No token to create.
		Utility.debug(3, "Comment scanned from @" + startPosition + " to @" + _position);
		break;
		
	    }

	    // Integer literal?
	    case '0':
	    case '1':
	    case '2':
	    case '3':
	    case '4':
	    case '5':
	    case '6':
	    case '7':
	    case '8':
	    case '9': {

		startPosition = _position;

		// Add the leading digit to the token.
		text += c;
		
		// Read any additional digits, ending the token with the source's end or any valid integer terminator (whitespace,
		// symbol).
		while (true) {
		    _position += 1;
		    if ( endOfSource() || !isAlphanumeric(c = _source.get(_position)) ) {
			_position -=1;
			token = new Token(text, startPosition, Token.Type.INTEGER);
			break;
		    }
		    if (isDigit(c)) {
			text += c;
		    } else {
			Utility.error("Malformed integer (invalid digit)", _position);
		    }
		}

		break;
		
	    }
		
	    // Character literal?
	    case '\'': {

		// Yes.  Scan the next character.
		startPosition = _position;
		text = "" + scanChar();

		// Verify that the closing single-quote is there.
		_position += 1;
		if ( endOfSource() || _source.get(_position) != '\'' ) {
		    Utility.error("Quoted character incomplete", startPosition);
		}
		
		token = new Token(text, startPosition + 1, Token.Type.CHAR);
		break;
		
	    }

	    // String literal?
	    case '"': {

		// Read any and all characters until an (unescaped) quote is encountered.  The delimiting quotes themselves are not
		// part of the token.
		startPosition = _position;
		while (true) {

		    // Grab the next character...
		    c = scanChar();

		    // Terminate the string if this was the closing quote.
		    if (c == '"') break;

		    // Add the character to the string.
		    text += c;

		}

		token = new Token(text, startPosition, Token.Type.STRING);
		break;
		
	    }

            // Handle anything that is a keyword/identifier (for which the first character could be a range of options), leaving
            // everything else to be a scanning failure.
 	    default:

		// Start of a name?
                if ( isAlphanumeric(c) || c == '_' ) {

                    // Adopt the first character as part of the token.
                    startPosition = _position;
                    text += c;

                    // Read any additional characters, ending the token with the source's end or any non-alphanumeric character
                    // (whitespace or symbol).
                    while (true) {
			
                        _position += 1;
                        if ( endOfSource() || !isNameSymbol(c = _source.get(_position)) ) {

			    // We reached the end of whatever this name is.
                            _position -= 1;

                            // If this identifier is a keyword, create that specialized token; otherwise create it as a generic
                            // identifier.
                            switch (text) {
			    case "false":
			    case "true":
				token = new Token(text, startPosition, Token.Type.BOOLEAN);
				break;
			    case "sizeof":
				token = new Token(text, startPosition, Token.Type.SIZEOF);
				break;
			    case "if":
				token = new Token(text, startPosition, Token.Type.IF);
				break;
			    case "else":
				token = new Token(text, startPosition, Token.Type.ELSE);
				break;
			    case "while":
				token = new Token(text, startPosition, Token.Type.WHILE);
				break;
			    case "return":
				token = new Token(text, startPosition, Token.Type.RETURN);
				break;
			    case "proc":
				token = new Token(text, startPosition, Token.Type.PROC);
				break;
			    case "extern":
				token = new Token(text, startPosition, Token.Type.EXTERN);
				break;
			    case "etcetera":
				token = new Token(text, startPosition, Token.Type.ETCETERA);
				break;
			    default:
				token = new Token(text, startPosition, Token.Type.NAME);
			    }
			    break;

                        }

                        text += c;
			
                    }

                } else {

                    // Doesn't conform to any token pattern.
                    Utility.error("Malformed token", _position);

                }

	    } // switch (c)
	    
	    // If a token was created (not a comment), add it to the list scanned.
	    if (token != null) {
		tokens.add(token);
	    }
	    _position += 1;


	} // while (!endOfSource())

	return tokens;

    } // scan ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Scan the next character as a literal.  If it is escaped, identify the correct underlying character value.
     *
     * @return the scanned character.
     */
    private char scanChar () {

	// Get the next character.
	_position += 1;
        char c = _source.get(_position);

	// Is it an escape?
        if (c != '\\') {

	    // No, so this one character is all we need.
	    return c;

	}
	
        // It is an escape, so get the character that is escaped.
        _position += 1;
        if (endOfSource()) {
            Utility.error("Incomplete escaped-character sequence at end of input", _position);
        }
        c = _source.get(_position);

        // Is this a special escape sequence?
        switch (c) {
	case '0':
	    c = 0x00; // Null character
	    break;
        case 'a':
            c = 0x07; // Alert
            break;
        case 'b':
            c = 0x08; // Backspace
            break;
        case 'e':
            c = 0x1b; // Escape
            break;
        case 'f':
            c = 0x0c; // Formfeed page-break
            break;
        case 'n':
            c = 0x0a; // Newline
            break;
        case 'r':
            c = 0x0d; // Carriage return
            break;
        case 't':
            c = 0x09; // Horizontal tab
            break;
        case 'v':
            c = 0x0b; // Vertical tab
            break;
	case '\\':
	case '\'':
	    break;    // Itself -- nothing to do.
        default:
	    Utility.error("Invalid escape character: " + c, _position);
        }

        return c;

    } // scanChar ()
    // =============================================================================================================================



    // =============================================================================================================================
    private boolean endOfSource () {

	return _position >= _source.size();

    } // endOfSource ()
    // =============================================================================================================================



    // =============================================================================================================================
    private static boolean isDigit (char c) {

	return '0' <= c && c <= '9';

    } // isDigit ()
    // =============================================================================================================================
    


    // =============================================================================================================================
    private static boolean isAlphabetic (char c) {

	return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');

    } // isAlphabetic ()
    // =============================================================================================================================
    


    // =============================================================================================================================
    private static boolean isAlphanumeric (char c) {

	return isAlphabetic(c) || isDigit(c);

    } // isAlphanumeric ()
    // =============================================================================================================================
    


    // =============================================================================================================================
    private static boolean isNameSymbol (char c) {

        return c == '_' || isAlphanumeric(c);

    } // isAlphanumeric ()
    // =============================================================================================================================


    
// =================================================================================================================================
} // class Lexer
// =================================================================================================================================

