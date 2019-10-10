// Agent tom in project greeting-example.mas2j

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .send(bob,tell,hello).

+hello[source(A)]
  <- .print("I received a 'hello' from ",A);
     .send(A,tell,hello).
