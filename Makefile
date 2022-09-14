# showcase the problem that only occurs in javac
all:
	javac -cp lombok-1.18.24.jar prob/lems/ProblemWithJavacButNotEclipse.java
	java -cp lombok-1.18.24.jar:. 'prob.lems.ProblemWithJavacButNotEclipse'
