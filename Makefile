# showcase the problem that only occurs in javac
all: problem1 problem2

problem1:
	javac -cp lombok-1.18.28.jar prob/lems/ProblemWithJavacButNotEclipse.java
	java -cp lombok-1.18.28.jar:. 'prob.lems.ProblemWithJavacButNotEclipse'

problem2:
	javac -cp lombok-1.18.28.jar prob/lems/ProblemWithJavacButNotEclipse2.java
	java -cp lombok-1.18.28.jar:. 'prob.lems.ProblemWithJavacButNotEclipse2'

problem3:
	javac -cp lombok-1.18.28.jar prob/lems/ProblemWithJavacButNotEclipse3.java

jni_problem:
	javac -cp lombok-1.18.28.jar prob/lems/JNIProblem.java
	java -cp lombok-1.18.28.jar:. 'prob.lems.JNIProblem'
