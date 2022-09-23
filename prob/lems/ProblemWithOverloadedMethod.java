package prob.lems;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.experimental.ExtensionMethod;

/**
 * Here we declare an extension method {@code Stream.filter(BiPredicate)}.
 * That name already exists, but with a different parameter type.<br>
 * This causes Lombok to fail on all calls to the normal {@code Stream.filter(Predicate)} method.<br>
 * Declaring that existing method as well in the Extensions class seemed to work at first,
 * but then caused several other strange problems... 
 *
 * @author Marc Günther
 */
public class ProblemWithOverloadedMethod {

    // method with different signature
    public static class Extensions1 {
        public static <T> Stream<T> filter(Stream<T> that, BiPredicate<? super T, Integer> predicate) {
            return that; // some irrelevant implementation
        }
    }

    // method with identical signature to Stream.filter()
    public static class Extensions2 {
        public static <T> Stream<T> filter(Stream<T> that, Predicate<? super T> predicate) {
            return that.filter(predicate);
        }
    }

    // --- the following three classes are all identical, except they include different extension methods

    // no extension methods at all, everything is fine
    //@ExtensionMethod(ProblemWithOverloadedMethod.Extensions1.class)
    //@ExtensionMethod(ProblemWithOverloadedMethod.Extensions2.class)
    public static class NormalCase {
        public static void doit() {
            List<String> list = Stream.of("a", "b", "c").filter(s -> {
                String a = "";
                return a.length() == 0;
            }).collect(Collectors.toList());
            System.out.println(list);
        }
    }

    // overload example: extension method, which is also called "filter", but has different parameters
    // Eclipse complains with:
    // The method filter(Stream<T>, BiPredicate<? super T,Integer>) in the type Main2.Extensions1 is not applicable for the arguments (Stream<String>, Predicate<? super String>)
    @ExtensionMethod(ProblemWithOverloadedMethod.Extensions1.class)
    public static class FailCase1 {
        public static void doit() {
            List<String> list = Stream.of("a", "b", "c").filter(s -> {
                //                                       ^^^^^^
                // fails, because we declared filter with a different parameter type
                String a = "";
                return a.length() == 0;
            }).collect(Collectors.toList());
            System.out.println(list);
        }
    }

    // identical-method example: extension method, whose signature is identical to the original "Stream.filter()" method (same name and parameter list)
    // Eclipse complains in the "return a.length()..." line:
    // The local variable a may not have been initialized
    @ExtensionMethod(ProblemWithOverloadedMethod.Extensions2.class)
    public static class FailCase2 {
        public static void doit() {
            List<String> list = Stream.of("a", "b", "c").filter(s -> {
                String a = "";
                return a.length() == 0;
                //     ^
                // "The local variable a may not have been initialized"
                // This doesn't make any sense. We declared the filter method exactly as it is already defined in Stream.
            }).collect(Collectors.toList());
            System.out.println(list);
        }
    }

    // another of these weird examples, this time with the lambda parameter
    @ExtensionMethod(ProblemWithOverloadedMethod.Extensions2.class)
    public static class FailCase3 {
        public static void doit() {
            List<String> list = Stream.of("a", "b", "c").filter(s -> s instanceof String).collect(Collectors.toList());
            //                                                       ^
            // s cannot be resolved or is not a field
            System.out.println(list);
        }
    }

    // and another one, this one includes a method parameter as well
    @ExtensionMethod(ProblemWithOverloadedMethod.Extensions2.class)
    public static class FailCase4 {
        public static void doit(String param) {
            List<String> list = Stream.of("a", "b", "c").filter(s -> param == s).collect(Collectors.toList());
            //                                                       ^^^^^
            // param cannot be resolved or is not a field
            //                                                                ^
            // s cannot be resolved or is not a field
            System.out.println(list);
        }
    }

    // this one works, though
    @ExtensionMethod(ProblemWithOverloadedMethod.Extensions2.class)
    public static class SucessCase5 {
        public static void doit(String param) {
            // no problem here
            List<String> list = Stream.of("a", "b", "c").filter(s -> param.equals(s)).collect(Collectors.toList());
            System.out.println(list);
        }
    }


    // and another one, this one involves an enum
    @ExtensionMethod(ProblemWithOverloadedMethod.Extensions2.class)
    public static class FailCase6 {
        public static void doit(String param) {
            List<String> list = Stream.of("a", "b", "c").filter(s -> {
                switch (someAnimal()) {
                    case DOG:
                //       ^^^
                //     DOG cannot be resolved to a variable
                        return true;

                    default:
                //  ^^^^^^^
                //  The default case is already defined
                        return false;
                }
            }).collect(Collectors.toList());
            System.out.println(list);
        }
    }

    // enum and a helper function for above example
    public static enum Animal {DOG, CAT, MOUSE;}

    public static Animal someAnimal() {
        return Animal.DOG;
    }

}
