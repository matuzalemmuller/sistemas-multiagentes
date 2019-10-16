// Available professions
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
