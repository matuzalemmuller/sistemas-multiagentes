// gets the price for the product,
// a random value between 100 and 110.


price(pedreiro,X) :- .random(R) & X = (100*R).
price(padeiro,X) :- .random(R) & X = (100*R).
price(mecanico,X) :- .random(R) & X = (100*R).
price(encanador,X) :- .random(R) & X = (100*R).
price(pintor,X) :- .random(R) & X = (100*R).
price(programador,X) :- .random(R) & X = (100*R).
price(carteiro,X) :- .random(R) & X = (100*R).
price(bancario,X) :- .random(R) & X = (100*R).
price(padre,X) :- .random(R) & X = (100*R).
price(chaveiro,X) :- .random(R) & X = (100*R).


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
