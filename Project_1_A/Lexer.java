
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Perform lexical on an input, generating a sequence of <code>Token</code>s.
 */
public class Lexer {

    /** The sequence of characters. */
    private List<Character> _source;
    /** The position within the source during a scan. */
    private int _position;

	/**
	* Create a lexer for a particular input stream of characters.
	* Pre-read the full input, preparing it to be scanned.
	* @param source The input character sequence that forms the source code.
	* */
    public Lexer (List<Character> source) {
		_source   = source;
		_position = 0;
    }

	/**
     * Attempt to scan the sequence of characters into a sequence of tokens.
     * @return A list of <code>Token</code>s found.
     */
    public List<Token> scan () {

		// Prepare to move through the character stream.
		List<Token> tokens = new ArrayList<>();

		// Scan the input.  Track positioning within the input.
		_position = 0;
		while (!endOfSource()) {

			// Useful locals during scanning.
			int     startPosition = -1;
			boolean isHex         = false;

			// Grab the next character.
			char   c     = _source.get(_position);
			String text  = "";
			Token  token = null;

			switch (c) {
				// Whitespace
				case ' ':
				case '\r':
				case '\t':
				case '\n':
					token = new Token(c, _position, Token.Type.WHITESPACE);
					break;

				// Open-closes?
				case '(':
					token = new Token(_position, Token.Type.OPENPAREN);
					break;
				case ')':
					token = new Token(_position, Token.Type.CLOSEPAREN);
					break;

				// Operator?
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

				// Comment?
				case '#': {
					// Read characters until the end-of-line is reached.
					startPosition = _position;
					do {
						_position += 1;
						if (endOfSource()) {
							Utility.error("Input ended mid-comment", _position);
						}
						c = _source.get(_position);
					} while (c != '\n');
					// No token to create.
					Utility.debug(2, "Comment scanned from @" + startPosition + " to @" + _position);
					break;
				}

				// Integer?
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
					StringBuilder digit = new StringBuilder();
					while (Character.isDigit(c)) {
						digit.append(c);
						_position += 1;
						if (endOfSource()) {
							break;
						}
						c = _source.get(_position);
					}
					_position -= 1;
					token = new Token(digit.toString(), _position, Token.Type.PERCENT);
					break;
				}

				// Anything else is an error.
				default:
					Utility.error("Malformed token", _position);
			}

			// If a token was created (not a comment), add it to the list scanned.
			if (token != null) {
				tokens.add(token);
			}
			_position += 1;
		}

		//printTokens(tokens);
		return tokens;
    }

	private boolean endOfSource () {
		return _position >= _source.size();
    }

	private void printTokens(List<Token> tokens) {
		for (Token token: tokens) {
			System.out.println("->" + token._text + "<-");
		}

	}


}