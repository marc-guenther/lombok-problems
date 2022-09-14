package prob.lems;

import lombok.val;
import lombok.experimental.ExtensionMethod;

/**
 * A very peculiar problem, that happens only with javac, but not Eclipse.<br>
 * 
 * When declaring a local variable using {@code val}, and calling an extension method
 * in the expression on the right hand side, then {@code javac} cannot figure out the type.
 * Eclipse has no problem with this, though.
 */
@ExtensionMethod(ProblemWithJavacButNotEclipse.Extensions.class)
public class ProblemWithJavacButNotEclipse {

    public static class Extensions {
        public static String toTitleCase(String that) {
            return "Hallo"; // some irrelevant implementation
        }
    }

    public static void main(String[] args) {
        // this is fine
        String s1 = "hallo".toTitleCase();
        System.out.println(s1);

        // this fails with:
        // prob/lems/ProblemWithJavacButNotEclipse.java:30: error: Cannot use 'val' here because initializer expression does not have a representable type: Type cannot be resolved
        // when compiling with javac (run "make" to see the error)
        val s2 = "hallo".toTitleCase();
        System.out.println(s2);
    }
}
