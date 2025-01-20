[char newline]

extern int printf (char* format, etcetera)

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

proc int main (int argc, char** argv)
              [int i, int offset, char* str] {

  (= newline '\n')
  (= i 0)
  while (< i argc) {
      (= offset (* sizeof(char*) i))
      (= str (* (+ argv offset)))
      (printf "argv[%d] (length = %d) = %s%c" i (strlen str) str newline)
      (= i (+ i 1))
  }
  return 0
  
}
