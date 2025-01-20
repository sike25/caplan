// =================================================================================================================================
/**
 * Represent a single token from the scanned input.
 */
public class Token {
// =================================================================================================================================



    // =============================================================================================================================
    public enum Type {

	// Whitespaces & separators
	SPACE,
	TAB,
	NEWLINE,
	COMMA,

	// Enclosures
	OPENPAREN,
	CLOSEPAREN,
	OPENBRACKET,
	CLOSEBRACKET,
	OPENBRACE,
	CLOSEBRACE,

	// Operators
	AT,
	BANG,
	BANGEQUALS,
	TILDE,
	CARAT,
	PLUS,
	DASH,
	STAR,
	SLASH,
	PERCENT,
	EQUALS,
	DOUBLEEQUALS,
	LEFTANGLE,
	LEFTANGLEEQUALS,
	DOUBLELEFTANGLE,
	RIGHTANGLE,
	RIGHTANGLEEQUALS,
	DOUBLERIGHTANGLE,
	BAR,
	DOUBLEBAR,
	AMPERSAND,
	DOUBLEAMPERSAND,

	// Literals (by type)
	INTEGER,
	CHAR,
	BOOLEAN,
	STRING,

	// Special operators
	SIZEOF,

	// Keywords
	IF,
	ELSE,
	WHILE,
	RETURN,
	PROC,
	EXTERN,
	ETCETERA,

	// Names
	NAME
    }
    // =============================================================================================================================
    


    // =============================================================================================================================
    // DATA MEMBERS

    /** The string of the token, as given in the input. */
    public final String _text;

    /** The position in the input at which the token begins. */
    public final int    _position;

    /** The type of token. */
    public final Type   _type;
    // =============================================================================================================================



    // =============================================================================================================================
    public Token (String text, int position, Type type) {

	_text     = text;
	_position = position;
	_type     = type;
	
    }
    // =============================================================================================================================


    
    // =============================================================================================================================
    public Token (int position, Type type) {
	
	_position = position;
	_type     = type;
	switch (_type) {
	case SPACE:            _text = " ";  break;
	case TAB:              _text = "\t"; break;
	case NEWLINE:          _text = "\n"; break;
	case COMMA:            _text = ",";  break;
	case OPENPAREN:        _text = "(";  break;
	case CLOSEPAREN:       _text = ")";  break;
	case OPENBRACKET:      _text = "[";  break;
	case CLOSEBRACKET:     _text = "]";  break;
	case OPENBRACE:        _text = "{";  break;
	case CLOSEBRACE:       _text = "}";  break;
	case AT:               _text = "@";  break;
	case BANG:             _text = "!";  break;
	case BANGEQUALS:       _text = "!="; break;
	case TILDE:            _text = "~";  break;
	case CARAT:            _text = "^";  break;
	case PLUS:             _text = "+";  break;
	case DASH:             _text = "-";  break;
	case STAR:             _text = "*";  break;
	case SLASH:            _text = "/";  break;
	case PERCENT:          _text = "%";  break;
	case EQUALS:           _text = "=";  break;
	case DOUBLEEQUALS:     _text = "=="; break;
	case LEFTANGLE:        _text = "<";  break;
	case LEFTANGLEEQUALS:  _text = "<="; break;
	case DOUBLELEFTANGLE:  _text = "<<"; break;
	case RIGHTANGLE:       _text = ">";  break;
	case RIGHTANGLEEQUALS: _text = ">="; break;
	case DOUBLERIGHTANGLE: _text = ">>"; break;
	case BAR:              _text = "|";  break;
	case DOUBLEBAR:        _text = "||"; break;
	case AMPERSAND:        _text = "&";  break;
	case DOUBLEAMPERSAND:  _text = "&&"; break;
	default:
	    _text = null;
	    Utility.error("Token type " + type + " cannot be created without text");
	}
		
    } // Token ()
    // =============================================================================================================================
		
	
	
    // =============================================================================================================================
    public boolean isWhitespace () {
	return _type == Type.SPACE || _type == Type.TAB || _type == Type.NEWLINE;
    }
    // =============================================================================================================================
	
	
	
    // =============================================================================================================================
    public boolean isOperator () {
	return ( _type == Type.AT               ||
		 _type == Type.BANG             ||
		 _type == Type.TILDE            ||
		 _type == Type.CARAT            ||
		 _type == Type.PLUS             ||
		 _type == Type.DASH             ||
		 _type == Type.STAR             ||
		 _type == Type.SLASH            ||
		 _type == Type.PERCENT          ||
		 _type == Type.EQUALS           ||
		 _type == Type.DOUBLEEQUALS     ||
		 _type == Type.BANGEQUALS       ||
		 _type == Type.LEFTANGLE        ||
		 _type == Type.LEFTANGLEEQUALS  ||
		 _type == Type.DOUBLELEFTANGLE  ||
		 _type == Type.RIGHTANGLE       ||
		 _type == Type.RIGHTANGLEEQUALS ||
		 _type == Type.DOUBLERIGHTANGLE ||
		 _type == Type.BAR              ||
		 _type == Type.DOUBLEBAR        ||
		 _type == Type.AMPERSAND        ||
		 _type == Type.DOUBLEAMPERSAND  );
	
    }
    // =============================================================================================================================
	
	
	
    // =============================================================================================================================
    public String toString () {
	
	return _text + " (" + _type + ") @" + _position;
		
    } // toString ()
    // =============================================================================================================================
		
	
	
// =================================================================================================================================
} // class Token
// =================================================================================================================================
	
