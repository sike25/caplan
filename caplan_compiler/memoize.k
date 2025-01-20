# ===================================================================================================================================
# memoize.k
#
# Compute a Fibonacci sequence, but keep a list of results to avoid recomputation of established values.
# ===================================================================================================================================



# ===================================================================================================================================
# GLOBALS

[

    # The array of precomputed Fibonacci numbers.
    int* memo,

    # The highest Fibonacci number memoized so far.
    int highest_fib,

    # The newline character (for printing).
    char newline,

    # Whether to emit debugging messages.
    bool debug
    
]
# ===================================================================================================================================



# ===================================================================================================================================
# EXTERNAL PROCEDURES

extern int printf (char* format, etcetera)
extern void* malloc (int size)
extern int atoll (char* str)
extern void exit (int status)
# ===================================================================================================================================



# ===================================================================================================================================
proc int main (int argc, char** argv)
              [int n]
{

    # Initialize constants.
    (= newline '\n')
    (= debug false)

    # Check usage.  Note that lack of short-circuit boolean logic
    # prevents us from combining these two cases with boolean-OR.
    if (!= argc 2) (show_usage_and_exit (* argv))
    if (== (= n (atoll (* (+ argv sizeof(char*))))) 0) (show_usage_and_exit (* argv))

    # Create memo array, then compute.
    (init n)
    (fib n)

    # Print the resulting array.
    (print n)

    return 0
    
} # main ()
# ===================================================================================================================================



# ===================================================================================================================================
proc void show_usage_and_exit (char* invocation)
                              []
{

    (printf "USAGE: %s <n>%c" invocation newline)
    (exit 1)

} # show_usage_and_exit ()
# ===================================================================================================================================



# ===================================================================================================================================
proc void init (int n)
               [int i]
{

    # Allocate the array.  (Entries for 0 to n imply length n+1.)
    (= memo (malloc (* (+ n 1) sizeof(int))))

    # Clear the array.
    (= i 0)
    while (< i n) {
        (= (* (+ memo (* i sizeof(int)))) 0)
	(= i (+ i 1))
    }

    # Initialize the first two (base case) entries.  [0] = 0 already; set [1] = 1.
    (= (* (+ memo sizeof(int))) 1)
    (= highest_fib 1)

} # init ()
# ===================================================================================================================================



# ===================================================================================================================================
proc int fib (int n)
             [int result]
{

    # Check memoized results, returning that if it has already been computed.  Doing so also serves as the base case.
    if (<= n highest_fib) return (* (+ memo (* n sizeof(int))))

    # Compute the new value, memoizing its result.
    (= result (+ (fib (- n 1)) (fib (- n 2))))
    (= (* (+ memo (* n sizeof(int)))) result)
    (= highest_fib n)
    if debug (printf "DEBUG: Calculated [%d] = %d%c" n result newline)
    return result

} # fib ()
# ===================================================================================================================================



# ===================================================================================================================================
proc void print (int n)
                [int i]
{

    (= i 0)
    while (<= i n) {
        (printf "[%d] = %d%c" i (* (+ memo (* i sizeof(int)))) newline)
	(= i (+ i 1))
    }

} # print ()
# ===================================================================================================================================
