JC = javac

export CLASSPATH := lib/jade-4.3.jar:.

.SUFFIXES: .java .class
.java.class:
	$(JC) src/*.java

default: .java.class

clean:
	$(RM) src/*.class