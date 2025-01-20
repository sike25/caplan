/**
 * Represent a single token from the scanned input.
 */
public class Token {

	public enum Type {
		WHITESPACE,
		OPENPAREN,
		CLOSEPAREN,
		PLUS,
		DASH,
		STAR,
		SLASH,
		PERCENT,
		INTEGER
    }

	// DATA MEMBERS
    /** The string of the token, as given in the input. */
    public final String _text;
    /** The position in the input at which the token begins. */
    public final int _position;
    /** The type of token. */
    public final Token.Type _type;

	public Token (String text, int position, Token.Type type) {
		_text     = text;
		_position = position;
		_type     = type;
    }

	public Token (char c, int position, Token.Type type) {
		_text     = "" + c;
		_position = position;
		_type     = type;
	
    }

	public Token (int position, Token.Type type) {
		_position = position;
		_type     = type;

		switch (_type) {
			case OPENPAREN:
				_text = "(";
				break;
			case CLOSEPAREN:
				_text = ")";
				break;
			case PLUS:
				_text = "+";
				break;
			case DASH:
				_text = "-";
				break;
			case STAR:
				_text = "*";
				break;
			case SLASH:
				_text = "/";
				break;
			case PERCENT:
				_text = "%";
				break;
			default:
				_text = null;
				Utility.error("Token type " + type + " cannot be created without text");
		}
    }

    public String toString () {
		return _text + " @" + _position;
    }

	public boolean isWhitespace() {
		return _type == Type.WHITESPACE;
	}
}