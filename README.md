# lombok-problems
Eclipse project showing some problems I found with extension-methods in lombok-1.8.24:

- interfaces cannot be annotated with `@ExtensionMethod`
- `val` doesn't like _extension methods_
- various problems with methods of the same name, that already exist on some classes

## Interfaces cannot be annotated with @ExtensionMethod

_reported as [projectlombok/lombok/issues#3259](https://github.com/projectlombok/lombok/issues/3259)_

Since Java 8 (2014) interfaces have `default` methods, and it makes perfect sense to use extension methods there.

```java
    // an interface - this will fail
    @ExtensionMethod(ProblemWithInterfaces.Extensions.class)
    public static interface GreeterAsInterface {
        // some code which would like to use extension methods
        ...
    }
```

https://github.com/marc-guenther/lombok-problems/blob/df013050404c29c0ce3b6b133e3182dc32643133/prob/lems/ProblemWithInterfaces.java#L17-L19

## Existing methods with identical name clash with extension methods

_reported as [projectlombok/lombok/issues#3261](https://github.com/projectlombok/lombok/issues/3261)_

When I define an extension method, eg. `or(...)` on `Object`, and some other class in my project already happens to implement a different method also called `or(...)`, then compilation fails. This makes extension methods on `Object` rather dangerous, unless you can come up with names which are guaranteed to be unused in all of your classes.

Here we have a `Qualifier` class, which defines its own `or(Predicate)` method, and compilation fails, just because we have enabled extension methods:

```java
    Qualifier<Qualifier> qual = new Qualifier<>(); 
    Object result = qual.or(q -> true); 
    //                   ^^ 
    // fails, because we also declared or() as an extension method  
```

https://github.com/marc-guenther/lombok-problems/blob/1ce6627b06586eed7435a018008d4d1e53ba31d1/prob/lems/ProblemWithExistingMethodInSubclass.java#L54-L57

Something more peculiar happened when I implemented my own version `filter(BiPredicate)` method on `Stream`. This of course clashed in the same way with the existing `filter(Predicate)` method:

```java
List<String> list = Stream.of("a", "b", "c").filter(s -> { 
    //                                       ^^^^^^ 
    // fails, because we declared filter with a different parameter type 
```

https://github.com/marc-guenther/lombok-problems/blob/c88e625565ca6ffe564b54595a73b3424cc26350/prob/lems/ProblemWithOverloadedMethod.java#L57-L59

So I thought, I simply add the existing method as an extension method as well. This seemed to work at first, but then very strange failures occured in unexpected places:

```java
    List<String> list = Stream.of("a", "b", "c").filter(s -> { 
    String a = ""; 
    return a.length() == 0; 
    //     ^ 
    // "The local variable a may not have been initialized" 
```

https://github.com/marc-guenther/lombok-problems/blob/c88e625565ca6ffe564b54595a73b3424cc26350/prob/lems/ProblemWithOverloadedMethod.java#L73-L77
Oviously `a` has just been initialized, so this doesn't really make sense.

The above is just one example of such a strange error, I have lots of them in my code, all in the vicinity of such overloaded methods, but not directly on the method itself.

## `val` and extension-methods work in Eclipse but not in javac

_reported as [projectlombok/lombok/issues#3260](https://github.com/projectlombok/lombok/issues/3260)_

Assigning an expression which involves an extension method to a `val` local variable works in Eclipse, but javac gives this error:

```java
 // this fails with: 
 // prob/lems/ProblemWithJavacButNotEclipse.java:30: error: Cannot use 'val' here because initializer expression does not have a representable type: Type cannot be resolved 
 // when compiling with javac (run "make" to see the error) 
 val s2 = "hallo".toTitleCase(); 
```

https://github.com/marc-guenther/lombok-problems/blob/c88e625565ca6ffe564b54595a73b3424cc26350/prob/lems/ProblemWithJavacButNotEclipse.java#L27-L30

To reproduce, simply run `make`.
