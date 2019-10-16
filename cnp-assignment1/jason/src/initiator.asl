// Professions
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
//requests(1).

all_proposals_received(CNPId) :-
        nb_participants(CNPId,NP) &                     // number of participants
        .count(propose(CNPId,_,_)[source(_)], NO) &       // number of proposes received
        .count(refuse(CNPId)[source(_)], NR) &          // number of refusals received
        NP = NO + NR.

// Registers initiator to yellow pages and asks the watcher how many requests
// will be made to hire services
!start.

+!start : requests(X) <-
        .df_register(initiator).

+!start : true <-
        .df_register(initiator);
        .send(watcher,askOne,requests(R),A);
        +A.

+requests(X) <-
        +count(X);
        +sum(X);
        !request_cnp.

// Starts N CNPs
+!request_cnp : count(N) & N > 0 <- 
        .random(R1);                // randomizes profession selection
        Cod = math.floor(10*R1);
        -count(A);
        C = A - 1;
        ?codFunc(Cod,Name);
        !startCNP(A,Name);            // creates CNP
        +count(C);
        !request_cnp.

// Alerts watcher that agent has finished negotiations
+!request_cnp <-
        .print("Finished creating CFPs.");
        .send(watcher,achieve,initiator_finished).

// Starts the CNP
+!startCNP(Id,Task) <-
        .print(Task, ":    Waiting participants...");
        .wait(2000);                                // waits for participants to register to yellow pages
        +cnp_state(Id,propose);
        .df_search("participant",LP);
        .print(Task, ":    Sending CFP to ",LP);
        +nb_participants(Id,.length(LP));
        .send(LP,tell,cfp(Id,Task));                // sends CFP
        .wait(all_proposals_received(Id), 4000, _);
        !contract(Id).

// this plan needs to be atomic so as not to accept
// proposals or refusals while contracting
@lc1[atomic]
+!contract(CNPId) : cnp_state(CNPId,propose) <-
        -cnp_state(CNPId,_);
        +cnp_state(CNPId,contract);
        .findall(offer(O,T,A),propose(CNPId,T,O)[source(A)],L);
        //.print("    Offers are ",L);
        L \== [];
        .min(L,offer(WOf,WT, WAg));
        .print(WT, ":    Winner is ", WAg," with ",WOf);
        !announce_result(CNPId,L,WAg);
        -+cnp_state(CNPId,finished).

// nothing todo, the current phase is not 'propose'
@lc2 +!contract(_).

-!contract(CNPId) <-
        .print("    CNP ",CNPId," has failed!").

+!announce_result(_,[],_).

// announce to the winner
+!announce_result(CNPId,[offer(_,_,WAg)|T],WAg) <-
        .send(WAg,tell,accept_proposal(CNPId));
        !announce_result(CNPId,T,WAg).

// announce to others
+!announce_result(CNPId,[offer(_,_,LAg)|T],WAg) <-
        .send(LAg,tell,reject_proposal(CNPId));
        !announce_result(CNPId,T,WAg).