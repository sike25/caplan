import java.util.Stack;

public class MockCompiler {
    public int rax = 0;
    public int rbx = 0;
    public int rdx = 0;
    public Stack<Integer> stack = new Stack<>();

    public MockCompiler() {}

    public void add() {
        rax = rax + rbx;
    }

    public void subtract() {
        rax = rax - rbx;
    }

    public void multiply() {
        rax = rax * rbx;
    }

    public void divide() {
        rax = rax / rbx;
        rdx = rax % rbx;
    }

}
