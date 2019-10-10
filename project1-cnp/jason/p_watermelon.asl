// gets the price for the product,
// a random value between 100 and 110.
price(watermelon,X) :- .random(R) & X = (10*R)+3.

!register.

+!register <- .df_register("participant");
              .df_subscribe("initiator").

// answer to Call For Proposal
@c1 +cfp(CNPId,Task)[source(A)]
   :  provider(A,"initiator") & 
      price(Task,Offer)
   <- +proposal(CNPId,Task,Offer); // remember my proposal
      .send(A,tell,propose(CNPId,Task,Offer)).

@r1 +accept_proposal(CNPId)[source(A)]
   :  proposal(CNPId,Task,Offer)
   <- .print("	My proposal '",Offer,"' won CNP ",CNPId, " for ",Task, " from ", A, "!").
      // do the task and report to initiator

@r2 +reject_proposal(CNPId)[source(A)]
   <- .print("	I lost CNP ",CNPId, " from ", A, ".");
      -proposal(CNPId,_,_). // clear memory
