package prob.lems;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.experimental.ExtensionMethod;

/**
 * Another problem that happens only with javac, but not in Eclipse.<br>
 * 
 * I have an extension method stream() defined on Optional (I'm still on Java 8).
 * Lombok confuses this method with the completely unrelated {@link Collection#stream()}.
 * 
 * Eclipse has no problem with this, though.
 */
@ExtensionMethod(ProblemWithJavacButNotEclipse3.Extensions.class)
public class ProblemWithJavacButNotEclipse3 {

    public static class Extensions {

        /**
         * Extension method defined for Optional.
         * I don't use this anywhere, but just because it exists, I cannot use the normal {@link Collection#stream()} anymore.
         */
        public static <T> Stream<T> stream(Optional<T> that) {
            return that.isPresent() ? Stream.of(that.get()) : Stream.empty();
        }

        /**
         * I need another extension method, as the error only manifests inside of a Lambda expression that is passed to an extensions method.
         */
        public static <T> Stream<T> flattenTree(Stream<? extends T> stream, Function<? super T, ? extends Stream<? extends T>> childGetter) {
            return stream.flatMap(el -> Stream.concat(Stream.of(el), flattenTree(childGetter.apply(el), childGetter)));
        }
    }

    /** an example tree structure */
    public static interface Person {
        public List<Person> kids();
    }

    public void exampleProblem(List<Person> list) {

        // We have a lambda, which is passed to an extension method (flattenTree).
        // In that lambda, we use a perfectly normal non-extension method List.stream(), 
        // but Lombok confuses it with the completely unrelated Optional.stream() extension method defined above.

//      This fails with:
//      prob/lems/ProblemWithJavacButNotEclipse3.java:55: error: method stream in class Extensions cannot be applied to given types;
        list.stream().flattenTree(p -> p.kids().stream()).collect(Collectors.toList());
//                                                    ^
//      required: Optional<T>
//      found: List<Person>
//      reason: cannot infer type-variable(s) T
//        (argument mismatch; List<Person> cannot be converted to Optional<T>)
//      where T is a type-variable:
//        T extends Object declared in method <T>stream(Optional<T>)

        // not using the outer .stream() doesn't help:

//      prob/lems/ProblemWithJavacButNotEclipse3.java:67: error: method stream in class Extensions cannot be applied to given types;
        Stream.<Person>empty().flattenTree(p -> p.kids().stream()).collect(Collectors.toList());
//                                                             ^
//      required: Optional<T>
//      found: List<Person>
//      reason: cannot infer type-variable(s) T
//        (argument mismatch; List<Person> cannot be converted to Optional<T>)
//      where T is a type-variable:
//        T extends Object declared in method <T>stream(Optional<T>)


        // it works when our lambda is passed to a normal non-extension methodd (flatMap):
        list.stream().flatMap(p -> p.kids().stream()).collect(Collectors.toList());

        // it also works. when the inner .stream() is called an something whose type is known:
        list.stream().flattenTree(p -> Collections.<Person>emptyList().stream()).collect(Collectors.toList());

        // so specifying the type of the p parameter also fixes the problem:
        list.stream().flattenTree((Person p) -> p.kids().stream()).collect(Collectors.toList());

    }
}
