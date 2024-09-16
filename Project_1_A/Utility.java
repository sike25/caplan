import java.util.List;

/**
 * A collection of useful values and methods that can be used throughout the code.
 */
public class Utility {

    // DATA MEMBERS
    // Useful constants.
    public static final int _bitsPerByte          = 8;
    public static final int _bytesPerWord         = 4;
    public static final int _bitsPerWord          = _bitsPerByte * _bytesPerWord;
    public static final int _minSignedWordValue   = (int)Math.pow(2, _bitsPerWord - 1) * -1;
    public static final int _maxSignedWordValue   = (int)Math.pow(2, _bitsPerWord - 1) - 1;
    public static final int _minUnsignedWordValue = 0;
    public static final int _maxUnsignedWordValue = (int)Math.pow(2, _bitsPerWord);
    public static final int _debuggingLevel = 2;

    /** The source code for error message generation. */
    public static List<Character> _source = null;

    /**
     * Emit information about a compilation error (not an internal error) and halt processing.
     *
     * @param message The error message to emit.
     */
    public static void error (String message) {
		System.err.println("ERROR:" + message);
		System.exit(1);
    }

	/**
     * Emit information about a compilation error (not an internal error), highlighting the point in the source text at which the
     * error occurred, and then halt processing.
     *
     * @param message  The error message to emit.
     * @param position Where in the source code the error occurred.
     */
    public static void error (String message, int position) {

		// Sanity check.
		if (_source == null) {
			abort("Attempt to emit compilation error without having set source");
		}

		// Traverse the text, keeping track of the textual position.
		int line           = 1;
		int column         = 0;
		int lineStartIndex = 0;
		for (int i = 0; i < position && i < _source.size(); i += 1) {
			char c = _source.get(i);
			if (c == '\n') {
				line          += 1;
				column         = 0;
				lineStartIndex = i + 1;
			} else {
				column += 1;
			}
		}

		// Print the error message:
		//   1. Print the error message...
		System.err.println("ERROR: " + message + " @" + position + " (" + line + "," + column + ")");

		//   2. Print the line of code.
		int i = lineStartIndex;
		while (i < _source.size()) {
			char c = _source.get(i);
			if (c == '\n') break;
			System.err.print(c);
			i += 1;
		}
		System.err.println();

		//   3. Print a marker at the given position beneath the line of code.
		for (i = lineStartIndex; i < position; i += 1) {
			if (_source.get(i) == '\t') {
				System.err.print('\t');
			} else {
				System.err.print(' ');
			}
		}
		System.err.println('^');
		System.exit(1);
	
    }


	/**
	* Emit information about an internal processing error (not a compilation error) and halt processing.
	* @param message The error message to emit.
	*/
    public static void abort (String message) {
		System.err.println("INTERNAL ERROR: " + message);
		System.exit(1);
    }

	public static void warn (String message) {
		System.err.println("WARNING: " + message);
    }

	/**
	* Emit a debugging message if the current debugging level is sufficiently high.
	* @param level The level of the debugging message.  The higher, the more detailed.
	* @param message The debugging message to print.
	*/
    public static void debug (int level, String message) {
		if (level <= _debuggingLevel) {
			String indent = new String();
			for (int i = 0; i < level; i++) {
				indent += "  ";
			}
			System.err.println("DEBUG [" + level + "]: " + indent + message);
		}
    }
}