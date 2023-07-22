package prob.lems;

import lombok.experimental.ExtensionMethod;

// This works fine when compiled with javac and run with java.
//
// With Eclipse, it compiles, but when run, it produces:
//
//    Error: A JNI error has occurred, please check your installation and try again
//    Exception in thread "main" java.lang.VerifyError: Bad type on operand stack
//    Exception Details:
//      Location:
//        prob/lems/JNIProblem.someMethod()V @3: invokestatic
//      Reason:
//        Type integer (current frame, stack[1]) is not assignable to long_2nd
//      Current Frame:
//        bci: @3
//        flags: { }
//        locals: { 'prob/lems/JNIProblem' }
//        stack: { 'java/lang/String', integer }
//      Bytecode:
//        0x0000000: 120f 08b8 0011 120f 1400 17b8 0011 b1  
//    
//        at java.lang.Class.getDeclaredMethods0(Native Method)
//        at java.lang.Class.privateGetDeclaredMethods(Class.java:2701)
//        at java.lang.Class.privateGetMethodRecursive(Class.java:3048)
//        at java.lang.Class.getMethod0(Class.java:3018)
//        at java.lang.Class.getMethod(Class.java:1784)
//        at sun.launcher.LauncherHelper.validateMainClass(LauncherHelper.java:650)
//        at sun.launcher.LauncherHelper.checkAndLoadMain(LauncherHelper.java:632)

@ExtensionMethod(JNIProblem.Extensions.class)
public class JNIProblem {

    public static class Extensions {
        public static <T> void hello(String that, long n) {
            // nothing here
        }
    }

    public void someMethod() {
        // here we call the method with an int, but it expects a long
        "".hello(5);
        // changing this to a literal long value fixes the problem
        "".hello(5l);
    }

    public static void main(String[] args) {
        System.out.println("works in javac but not with Eclipse");
    }
}
