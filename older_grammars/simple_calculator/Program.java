import java.util.List;

/**
 * A single program, which is a list expressions to evaluate.
 */
public class Program {
    /** The expressions in order in which they appear in the source. */
    private final List<Expression> _expressions;

    /**
     * Create a new program from a given list of expressions.
     *
     * @param expressions A list of expressions.
     */
    public Program (List<Expression> expressions) {
		_expressions = expressions;
    }

	/**
     * Generate assembly code that will execute this program.
     *
     * @return the generated assembly code.
     */
    public String toAssembly () {
		// Prologue stub code, setting up and starting the definition of main().
		StringBuilder assembly = new StringBuilder(("""
                \tglobal\tmain
                \textern\tprintf

                \tsection\t.text

                main:

                """
		));

		// Evaluate and print each expression.
		boolean firstpass = true;
		for (Expression expression : _expressions) {

			// Prefix the code with a textual representing of this expression as a comment.
			assembly.append("; Expression: ").append(expression).append('\n');
			assembly.append(";   Evaluate\n");

			// Generate the assembly that evaluates the expression.
			assembly.append(expression.toAssembly());

			// Call printf() to emit the result of the expression, which is on top of the stack.
			assembly.append(";   Print\n");
			if (firstpass) {
			assembly.append("""
                    ;     arg[0] (rdi) = formatting string
                    ;     arg[1] (rsi) = expression result
                    ;     arg[2] (rax) = end of varargs
                    """);
			firstpass = false;
			}
			assembly.append("\tmov\trdi,\texpstr").append(expression._position).append('\n').append("\tpop\trsi\n").append("\tmov\trax,\t0\n")
					.append("\tsub rsp, 8\n")
					.append("\tcall\tprintf\n")
					.append("\tadd rsp, 8\n\n");

	}

	// Epilogue stub code A: returning from main().
	assembly.append("\n; Return from main()\n");
	assembly.append("\tmov\trax,\t0\n");
	assembly.append("\tret\n");

	// Epilogue stub code B: Define the string constants that are used during printing, namely the expressions themselves.
	assembly.append("\n\tsection\t.data\n\n");
	for (Expression expression : _expressions) {

	    // Create a label and then define the string with an appended newline (10) and null termination.  Use the position value
	    // as a unique ID.  Each of these is a formatting string for printf that includes markers for the result (%d).
	    assembly.append("expstr").append(expression._position).append(":\tdb\t\"").append(expression).append(" = %d\", 10, 0\n");
	    
	}
	return assembly.toString();

    }

	public String toString () {
		StringBuilder text = new StringBuilder();
		for (Expression expression : _expressions) {
			text.append(expression.toString()).append('\n');
		}
	return text.toString();
	
    }
}