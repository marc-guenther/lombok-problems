package prob.lems;

import lombok.val;
import lombok.experimental.ExtensionMethod;

/**
 * Another problem that happens only with javac, but not Eclipse.<br>
 * 
 * One cannot call an extension method in {@code this}.
 * It works perfectly fine when assigning {@code this} to a local variable first, though.
 * Run "make problem2" to see the error.
 * 
 * Again Eclipse has no problem with any of this.
 */
@ExtensionMethod(ProblemWithJavacButNotEclipse2.Extensions.class)
public class ProblemWithJavacButNotEclipse2 {

    public static class Extensions {
        public static void hello(ProblemWithJavacButNotEclipse2 that) {
            System.out.println("Hallo");
        }
    }

    public void doSomething() {

        // This fails with: error: cannot find symbol
        this.hello();
        //  ^
        //  symbol: method hello()
        // 1 error

        // but works perfectly fine, when I assign "this" to a local variable first
        ProblemWithJavacButNotEclipse2 a = this;
        a.hello();

        // even "val" is no problem
        val b = this;
        b.hello();
    }

    public static void main(String[] args) {
        new ProblemWithJavacButNotEclipse2().doSomething();
    }
}
