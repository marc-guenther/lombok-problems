package prob.lems;

import lombok.val;
import lombok.experimental.ExtensionMethod;

/**
 * Lombok does not allow to annotate interfaces with {@code @ExtensionMethod}.
 */
public class ProblemWithInterfaces {

    public static class Extensions {
        public static String toTitleCase(String that) {
            return "Hallo"; // some irrelevant implementation
        }
    }

    // an interface - this will fail
    @ExtensionMethod(ProblemWithInterfaces.Extensions.class)
    public static interface GreeterAsInterface {
        public default void greetings() {
            System.out.println("hallo".toTitleCase());
        }
    }

    // a normal class - this is OK
    @ExtensionMethod(ProblemWithInterfaces.Extensions.class)
    public static class GreeterAsClass {
        public void greetings() {
            System.out.println("hallo".toTitleCase());
        }
    }
}
