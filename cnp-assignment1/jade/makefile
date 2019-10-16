JC = javac

export CLASSPATH := lib/jade-4.3.jar:.

.SUFFIXES: .java .class
.java.class:
	$(JC) test/*.java
	$(JC) src/*.java

default: .java.class

clean:
	$(RM) test/*.class
	$(RM) src/*.class