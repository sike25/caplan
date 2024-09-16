import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
/** Direct the stages of compilation.*/
public class Compiler {

    /** The suffix used for source code files. */
    public static final String _sourceSuffix = ".src";

    /** The entry point.  Parse command-line arguments/options and carry out the compilation process.
	 * @param args The command-line arguments. */
    public static void main (String[] args) {

		// Get the input path from the command line, and construct the output path from it.
		String inPath  = parseCommandLine(args);
		String outPath = inPath.substring(0, inPath.length() - _sourceSuffix.length()) + ".txt";

		// Open the files.
		DataInputStream in  = openInput(inPath);
		PrintWriter     out = openOutput(outPath);

		// 0. Read the entire input and close that input.  Give the Utility the source for error message use.
		List<Character> source = readInput(in);
		try {
			in.close();
		} catch (IOException e) {
			Utility.warn("Failure closing " + inPath);
		}
		in = null;
		Utility._source = source;

		// 1. Scan
		Lexer lexer  = new Lexer(source);
		List<Token> tokens = lexer.scan();
		Utility.debug(1, "Tokens scanned:");
		int i = 0;
		for (Token token : tokens) {
			Utility.debug(2, "\t" + (i++) + ": " + token);
		}
    }

    /**
     * Parse the command-line arguments. Extract the assembly stub path (if provided), the standard library path (if provided), and
     * the source code to compile.  Assemble the paths into an array, using defaults for the paths not provided.
     *
     * @param args The command-line arguments.
     * @return The code path.
     */
    private static String parseCommandLine (String[] args) {

		// Check the number of arguments.
		if (args.length != 1) {
			printUsageAndExit();
		}

		// Check that the code path ends with the correct suffix.
		String inPath = args[0];
		if ( (!inPath.endsWith(_sourceSuffix)) ) {
			printUsageAndExit();
		}
		return inPath;
    }

    /**
     * Print the usage message and exit.
     */
    private static void printUsageAndExit () {
		System.err.println("USAGE: java Compiler <source code pathname (" + _sourceSuffix + ")>");
		System.exit(1);
    }


    /**
     * Open the output file.
     *
     * @param path The pathname of the output file.
     * @return A writer into which output can be written.
     */
    private static PrintWriter openOutput (String path) {
		FileWriter outputWriter = null;
		try {
			outputWriter = new FileWriter(path);
		} catch (IOException e) {
			Utility.abort("Unable to open output file " + path);
		}
		return new PrintWriter(outputWriter);

    }

    /**
     * Open an input file.
     *
     * @param path The pathname of the input file.
     * @return An input stream for the input file.
     */
    private static DataInputStream openInput (String path) {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			Utility.abort("Unable to open input file " + path);
		}
		return new DataInputStream(inputStream);

    }

    private static List<Character> readInput (DataInputStream in) {
		// Create an empty list of input characters.
		List<Character> source = new ArrayList<Character>();

		// Repeatedly read characters until none remain.
		while (true) {
			// Read the next character.
			int readResult = 0;
			try {
				readResult = in.read();
			} catch (IOException e) {
				System.err.println("Failed read.\n");
				System.exit(1);
			}

			// End processing if there are no more characters to process.
			if (readResult == -1) break;

			// Add the current character to the sequence.
			source.add((char)readResult);
		}
		return source;
    }
}
