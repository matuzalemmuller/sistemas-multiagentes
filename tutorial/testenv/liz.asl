// Agent liz in project testenv.mas2j

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- burn.

+fire <- run.

+run <- .print("I'm running!").
