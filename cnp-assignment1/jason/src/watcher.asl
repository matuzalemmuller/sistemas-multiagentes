requests(20).				// number of CNPs performed by each initiator

!start.

// Registers to initiator yellow pages to know how many initiators are there
// so MAS is stopped after all negotiations are completed
+!start : true <-
        .wait(1000);
        .df_search("initiator",LP);
        X = .length(LP);
        +count(X).

// Stops simulation after all negotiations are completed
@lc1[atomic]
+!initiator_finished :
        count(N) & N > 1 <-
        -count(A);
        C = A - 1;
        +count(C).

+!initiator_finished <- .stopMAS.