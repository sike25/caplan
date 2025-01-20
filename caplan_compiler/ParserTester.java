import java.util.ArrayList;
import java.util.List;

public class ParserTester {
	public static void main(String[] args) {
	//Anthonys test code

	    String sourceCode = """ 
	    extern int printf(char* format, etcetera)
            extern void* malloc(int size)
            proc int main(int argc, char** argv)
            [int* x]
            {
                (= newline '\\n')
                (= x (malloc 4))
                (= (* x) 1)
                (printf "x: %p, *x: %d, %c" x (* x) newline)
                return 0
            }
            """;


	    List<Character> sourceCharacters = new ArrayList<Character>();
	    
	// read source into list of characters to pass
	for (char c : sourceCode.toCharArray()) {
		sourceCharacters.add(c);
	}

	// Pass to Lexer
	Lexer lexer = new Lexer(sourceCharacters);
	List<Token> tokens = lexer.scan();

	System.out.println("Tokens:");
	for (Token token : tokens) {
		System.out.println(token);
	}

	Parser parser = new Parser(tokens);
	try {
		Program program = parser.parse();
		System.out.println("\nParsed Program:");
		System.out.println(program);
	} catch (Exception e) {
		System.err.println("Error parsing/not our fault: " + e.getMessage());
	}
	}
 }  
