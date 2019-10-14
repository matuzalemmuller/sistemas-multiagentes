/* Initial beliefs and rules */
codFunc(0,builder).
codFunc(1,baker).
codFunc(2,mechanic).
codFunc(3,plumber).
codFunc(4,painter).
codFunc(5,priest).
codFunc(6,locksmith).
codFunc(7,programmer).
codFunc(8,postman).
codFunc(9,banker).


all_proposals_received(CNPId)
  :- nb_participants(CNPId,NP) &                 // number of participants
     .count(propose(CNPId,_,_)[source(_)], NO) &   // number of proposes received
     .count(refuse(CNPId)[source(_)], NR) &      // number of refusals received
     NP = NO + NR.

/* Initial goals */

+requisitions(X) <- +count(X);
                    +sum(X).

+!start : count(N) & N > 0 <-  .random(R1); //First random number to get the profession
            Cod = math.floor(10*R1);
            -count(A);
            C = A - 1;
            ?codFunc(Cod,Nome);
            !startCNP(A,Nome);
            +count(C);
            !start.

+!start <- .print("Finished creating CFPs.").

+!register <- .df_register(initiator).

/* Plans */

// start the CNP
+!startCNP(Id,Task)
   <- .print(Task, ":	Waiting participants...");
      .wait(2000);  // wait participants introduction
      +cnp_state(Id,propose);   // remember the state of the CNP
      .df_search("participant",LP);
      .print(Task, ":	Sending CFP to ",LP);
      +nb_participants(Id,.length(LP));
      .send(LP,tell,cfp(Id,Task));
      // the deadline of the CNP is now + 4 seconds (or all proposals were received)
      .wait(all_proposals_received(Id), 4000, _);
      !contract(Id).

+cnp_state(_,finished) :
         .count(cnp_state(_,finished),Total) &
         sum(X) &
         Total == X
         <- .send(mainInitiator,achieve,count).

// this plan needs to be atomic so as not to accept
// proposals or refusals while contracting
@lc1[atomic]
+!contract(CNPId)
   :  cnp_state(CNPId,propose)
   <- -cnp_state(CNPId,_);
      +cnp_state(CNPId,contract);
      .findall(offer(O,T,A),propose(CNPId,T,O)[source(A)],L);
      //.print("	Offers are ",L);
      L \== []; // constraint the plan execution to at least one offer
      .min(L,offer(WOf,WT, WAg)); // sort offers, the first is the best
      .print(WT, ":	Winner is ", WAg," with ",WOf);
      !announce_result(CNPId,L,WAg);
      -+cnp_state(CNPId,finished).

// nothing todo, the current phase is not 'propose'
@lc2 +!contract(_).

-!contract(CNPId)
   <- .print("	CNP ",CNPId," has failed!").

+!announce_result(_,[],_).
// announce to the winner
+!announce_result(CNPId,[offer(_,_,WAg)|T],WAg)
   <- .send(WAg,tell,accept_proposal(CNPId));
      !announce_result(CNPId,T,WAg).
// announce to others
+!announce_result(CNPId,[offer(_,_,LAg)|T],WAg)
   <- .send(LAg,tell,reject_proposal(CNPId));
      !announce_result(CNPId,T,WAg).
