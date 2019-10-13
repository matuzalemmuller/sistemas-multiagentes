//available professions
codFunc(0,pedreiro).
codFunc(1,padeiro).
codFunc(2,mecanico).
codFunc(3,encanador).
codFunc(4,pintor).
codFunc(5,programador).
codFunc(6,carteiro).
codFunc(7,bancario).
codFunc(8,padre).
codFunc(9,chaveiro).

!register.

+!register <- .df_register("participant");
              .df_subscribe("initiator");
              .random(R1); //First random number to get the profession
              Cod = math.floor(10*R1);
              .random(R2); //Second random number (price)
              X = (1000*R2);
              +price(Cod,X).

@c1 +cfp(CNPId,Task)[source(A)]
   :  provider(A,"initiator") &
      price(Cod,Offer) & codFunc(Cod,Task)
   <- +proposal(CNPId,Task,Offer); // remember my proposal
      .send(A,tell,propose(CNPId,Task,Offer)).

@r1 +accept_proposal(CNPId)[source(A)]
   :  proposal(CNPId,Task,Offer)
   <- .print("	My proposal '",Offer,"' won CNP ",CNPId, " for ",Task, " from ", A, "!").
      // do the task and report to initiator

@r2 +reject_proposal(CNPId)[source(A)]
   <- .print("	I lost CNP ",CNPId, " from ", A, ".");
      -proposal(CNPId,_,_). // clear memory
