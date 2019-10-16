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

// Registers participant to yellow pages and defines price for service
!register.

+!register <-
		.df_register("participant");
        .df_subscribe("initiator");
        .random(C);						// randomizes profession selection
        Cod = math.floor(10*C);
        .random(P);						// randomizes price
        X = (100*P);
		?codFunc(Cod,Name);
        +price(Name,P).					// saves price for service

		
@c1 +cfp(CNPId,Task)[source(A)] : provider(A,"initiator") & price(Task,Offer) <-
		+proposal(CNPId,Task,Offer); // remember my proposal
		.send(A,tell,propose(CNPId,Task,Offer)).

@r1 +accept_proposal(CNPId)[source(A)] :  proposal(CNPId,Task,Offer) <-
		.print("	My proposal '",Offer,"' won CNP ",CNPId, " for ",Task, " from ", A, "!").

@r2 +reject_proposal(CNPId)[source(A)] <-
		.print("	I lost CNP ",CNPId, " from ", A, ".");
		-proposal(CNPId,_,_). // clear memory
