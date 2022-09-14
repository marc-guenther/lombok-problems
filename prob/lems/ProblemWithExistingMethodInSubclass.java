package prob.lems;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.experimental.ExtensionMethod;

/**
 * Showcase a problem with lombok with defining extension methods, which might already exist in some subclasses.<br>
 * (this is probably related/identical to the first part of {@link ProblemWithOverloadedMethod.FailCase1})<br>
 * <br>
 * Here we define the {@code Object.or()} method from the Lombok documentation,
 * but unfortunately a class {@code Qualifier} already has its own implementation of {@code or()}.
 * This causes existing code to fail, just by declaring the extension method.
 *
 * @author Marc Günther
 */
public class ProblemWithExistingMethodInSubclass {

    /** taken from Lombok documentation */
    public static class ObjectExtensions {
        public static <T> T or(T object, T ifNull) {
            return object != null ? object : ifNull;
        }
    }

    /** some class that existed already in our code base */
    public static class Qualifier<T> {
        public Object or(Predicate<T> p) {
            return this; // some irrelevant implementation
        }
    }


    // -- the following two methods are identical, except they include extension methods or don't

    // no extension method, all is fine
    //@ExtensionMethod(ProblemWithExistingMethodInSubclass.ObjectExtensions.class)
    public static class NormalCase {
        public static void doit() {
            Qualifier<Qualifier> qual = new Qualifier<>();
            Object result = qual.or(q -> true);
            System.out.println(result);
        }
    }

    // just by enabling extension method, existing code does not compile anymore
    @ExtensionMethod(ProblemWithExistingMethodInSubclass.ObjectExtensions.class)
    public static class FailureCase {
        public static void doit() {
            Qualifier<Qualifier> qual = new Qualifier<>();
            Object result = qual.or(q -> true);
            //                   ^^
            // fails, because we also declared or() as an extension method 
            System.out.println(result);
        }
    }


    // just call all of them...
    public static void main(String[] args) {
        ProblemWithExistingMethodInSubclass.NormalCase.doit();
        ProblemWithExistingMethodInSubclass.FailureCase.doit();
    }
}
