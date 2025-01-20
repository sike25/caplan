// =================================================================================================================================
// IMPORTS

import java.util.List;
// =================================================================================================================================



// =================================================================================================================================
/**
 * A collection of useful values and methods that can be used throughout the code.
 */
public class Utility {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS

    // Useful constants.
    public static final int _bitsPerByte          = 8;
    public static final int _bytesPerWord         = 8;
    public static final int _bitsPerWord          = _bitsPerByte * _bytesPerWord;
    public static final int _minSignedWordValue   = (int)Math.pow(2, _bitsPerWord - 1) * -1;
    public static final int _maxSignedWordValue   = (int)Math.pow(2, _bitsPerWord - 1) - 1;
    public static final int _minUnsignedWordValue = 0;
    public static final int _maxUnsignedWordValue = (int)Math.pow(2, _bitsPerWord);

    // Control error & abort behavior.
    public static final int     _debuggingLevel   = 0;
    public static final boolean _endWithException = false;

    // x86-specific constants.
    public static final String[] _argumentRegisters = { "rdi", "rsi", "rdx", "rcx", "r8", "%r9" };
    public static final int      _byteSize          = 1;
    public static final int      _wordSize          = 2;
    public static final int      _dwordSize         = 4;
    public static final int      _qwordSize         = 8;

    /** The source code for error message generation. */
    public static List<Character> _source = null;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Emit information about a compilation error (not an internal error) and halt processing.
     *
     * @param message The error message to emit.
     */
    public static void error (String message) {

	System.err.println("ERROR:" + message + '\n');

	if (_endWithException) {
	    throw new RuntimeException();
	}
	System.exit(1);

    } // error (String)
    // =============================================================================================================================



    // =============================================================================================================================
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

	// Show the message and the location in the code.
	System.err.println("ERROR: " + message + ' ' + positionToLineColumn(position));

	if (_endWithException) {
	    throw new RuntimeException();
	}
	System.exit(1);
	
    } // error (String, List<Character>, int)
    // =============================================================================================================================
    


    // =============================================================================================================================
    /**
     * Emit information about an internal processing error (not a compilation error) and halt processing.
     *
     * @param message The error message to emit.
     */
    public static void abort (String message) {

	System.err.println("INTERNAL ERROR: " + message);

	if (_endWithException) {
	    throw new RuntimeException();
	}
	System.exit(1);

    } // abort ()
    // =============================================================================================================================



    // =============================================================================================================================
    public static void warn (String message) {

	System.err.println("WARNING: " + message);
	
    } // warn ()
    // =============================================================================================================================
    


    // =============================================================================================================================
    /**
     * Emit a debugging message if the current debugging level is sufficiently high.
     *
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

    } // debug ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Given a position in the source, return a string that shows the line and the column within that line.
     *
     * @param position The position within the code.
     * @return a string that, when printed, shows the line of code and highlights the column in that line.
     */
    public static String positionToLineColumn (int position) {

	// Sanity check.
	if (_source == null) {
	    abort("Utility.positionToLineColumn(): source not set");
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

	// Construct the error message:
	//   1. The location information itself...
	String message = "@" + position + " (" + line + "," + column + "):\n";

	//   2. The line of code itself.
	int i = lineStartIndex;
	while (i < _source.size()) {
	    char c = _source.get(i);
	    if (c == '\n') break;
	    message += c;
	    i += 1;
	}
	message += '\n';

	//   3. A marker at the given column, beneath the line of code.
	for (i = lineStartIndex; i < position; i += 1) {
	    message += (_source.get(i) == '\t' ? '\t' : ' ');
	}
	message += "^\n";

	return message;
	
    } // positionToLineColumn ()
    // =============================================================================================================================
    

    
// =================================================================================================================================
} // class Utility
// =================================================================================================================================
