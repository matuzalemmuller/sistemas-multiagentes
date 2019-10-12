To run JADE code:

```
export CLASSPATH=~/jason/libs/jade-XXXX.jar:.

javac jade/cnp/*.java
java jade.Boot -gui "tom:jade.cnp.Participant"
java jade.Boot -container -host localhost "bob:jade.cnp.Initiator"
```