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

The compiler is written in four layers.
1. **Lexer**    
This performs the lexical analysis converting the source code into tokens (numbers, operands, names- variables, procedures, keywords))
   
2. **Parser**    
This performs the synctatic analysis which is concerned with grammar and is the most important part of the compiler. It converts the list of tokens from the lexer into a Program object. See Grammar section for the (tree) structure of a resultant Program.

3. **Verifier**   
This performs the semantic analysis. It is responsible for things like type theory and number overflows. This layer also includes the binder which accounts for declared variables, their initialized values (or lack thereof) and changes-- as well as binding procedures to return statements.
   
4. **Generator**      
This translates the verified Program into assembly (.asm).


### Grammar

<program> ::= <multi var decl> <proc decl list>

##### VARIABLE DECLARATIONS
<multi var decl> ::= '[' <var decl list> ']'
<var decl list>  ::= <var decl> ',' <var decl list> | <var decl>
<var decl>       ::= <type> <name> | 🥺
<type>           ::= <name> '*'*

##### PROCEDURE DECLARATIONS
<proc decl list>  ::= <proc decl> <proc decl list> | 🥺
<proc decl>       ::= 'extern' <type> <name> '(' <param decl list> ')' |
                      'proc'   <type> <name> '(' <var decl list> ')' <multi var decl> <statement>
<param decl list> ::= <var decl> ',' <param decl list> | ( <var decl> | 'etcetera' )

##### STATEMENTS (we no longer parse print statements)
<stmt>       ::= <multi stmt> | <control stmt> | <return stmt> | <print stmt> | <exp>
<multi stmt> ::= '{' <stmt list> '}'                   
<stmt list>  ::= <stmt> <stmt list> | 🥺

##### CONTROL STATEMENTS
<control stmt> ::= <conditional> | <loop>
<conditional>  ::= 'if' <exp> <statement> [ 'else' <statement> ]
<loop>         ::= 'while' <exp> <statement>

##### EXPRESSIONS
<exp>       ::= <sizeof> | <literal> | <operation> | <name>
<sizeof>    ::= 'sizeof' '(' <type> ')'
<operation> ::= '(' <operator> <expression list> ')'
<operator>  ::= { @ - * ! + / % = == != < > || && | & ^ } | <name>
<name>      ::= ( <alphabetic char> | '_' ) ( <alphabetic char> | <digit char> | '_' )*

##### LITERALS
<literal>        ::= <int literal> | <char literal> | <bool literal>
<int literal>    ::= <digit>+
<char literal>   ::= ''' <character> '''
<bool literal>   ::= 'true' | 'false'
<string literal> ::= '"' <character>* '"'

##### 🥺
🥺 ::= <EMPTY>
