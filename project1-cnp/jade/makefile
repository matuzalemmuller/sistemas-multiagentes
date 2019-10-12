JC = javac

export CLASSPATH := cnp/jade-4.3.jar:.

.SUFFIXES: .java .class
.java.class:
	$(JC) cnp/*.java

default: .java.class

clean:
	$(RM) cnp/*.class