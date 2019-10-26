value(0).

!tick.

+!tick : value(A) & A == 0 <-
	X = A + 1;
    -+value(X);
    .wait(1000);
    !tick.

+!tick : .count(bidder(_),Bidder) & Bidder == 1 & bidder(_)[source(Name)]<-
    .print("The Winner is:", Name).

+!tick : value(X)<-
    A = X + 1;
    -+value(A);
	.print("Bid at ",A);
    .broadcast(tell,bid(A));
    .wait(1500);
    !tick.
