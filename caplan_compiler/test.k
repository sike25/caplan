[char newline]

extern int printf (char* format, etcetera)
extern void* malloc (int size)

proc int strlen (char* str)
                [int length, char* current]
{

    (= length 0)
    (= current str)
    while (!= (* current) '\0') {
        (= length (+ length 1))
        (= current (+ current 1))
    }

    return length

}

proc int fib (int n)
             []
{
	if (== n 0) return 0
	if (== n 1) return 1
	return (+ (fib (- n 1)) (fib (- n 2)))
}

proc int main (int argc, char** argv)
              [int* x]
{
	(= newline '\n')

	(= x (malloc sizeof(int)))
	(= (* x) 12345)
	(printf "x: %p, *x: %d %c" x (* x) newline)
	(printf "fib(12) = %d%c" (fib 12) newline)
	return 0
}
