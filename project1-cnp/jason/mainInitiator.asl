// Agent asd in project ContractNetProtocol.mas2j

/* Initial beliefs and rules */
start(initiator1,3).
start(initiator2,2).
start(initiator3,2).
start(initiator4,2).
start(initiator5,2).
start(initiator6,2).
start(initiator7,2).
start(initiator8,2).
start(initiator9,2).
start(initiator10,2).
start(initiator11,2).
start(initiator12,2).
start(initiator13,2).
start(initiator14,2).
start(initiator15,2).
start(initiator16,2).
start(initiator17,2).
start(initiator18,2).
start(initiator19,2).
start(initiator20,2).
start(initiator21,2).
start(initiator22,2).
start(initiator23,2).
start(initiator24,2).
start(initiator25,2).
start(initiator26,2).
start(initiator27,2).
start(initiator28,2).
start(initiator29,2).
start(initiator30,2).

/* Initial goals */

!start.

/* Plans */

+!start : start(Name,Requisitions) <- .send(Name,tell,requisitions(Requisitions)); //number of requisitions for each initiator
                  .send(Name,achieve,start);
                  .send(Name,achieve,register);
                  -start(Name,Requisitions);
                  !start.

+!start <- .print("Finished Sending Requisitions.").
