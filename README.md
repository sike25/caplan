# Caplan Compiler

### Caplan
Caplan is a Lisp-like programming language with support for a range of classical unary and binary operators and logical, comparison, and bitwise operators.    
It also supports control statements (if-then, if-then-else, while statements) and external and internal procedures.

```lisp

# This is a program in Caplan

# Global variables
[char newline]

# External procedure
extern int printf (char* format, etcetera)

# Internal procedure
proc int countA (char* str)
                [int length, char* current] # Local variables
{

    (= length 0)
    (= current str)
    while (!= (* current) '\0') { # While loop
	if (== (* current) 'a') { # If statement
		(= length (+ length 1))
	}
        (= current (+ current 1))
    }
    return length
}

# Main method
proc int main (int argc, char** argv)
              [int i, int offset, char* str] 
{

  (= newline '\n')
  (= i 0)
  while (< i argc) {
      (= offset (* sizeof(char*) i))
      (= str (* (+ argv offset)))

      # Function Call
      (printf "argv[%d] (length = %d) = %s%c" i (strlen str) str newline)

      (= i (+ i 1))
  }
  return 0
  
}

```

### Implementation

