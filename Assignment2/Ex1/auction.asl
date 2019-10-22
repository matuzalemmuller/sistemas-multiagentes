value(0).

!tick.


+!tick : value(A) & A == 0 <-
    -+value(0.1);
    .wait(1000);
    !tick.

+!tick : .count(bidder(_),Bidder) & Bidder == 1 & bidder(_)[source(Name)]<-
    .print("The Winner is:", Name).


+!tick : value(X)<-
    A = X + 0.1;
    -+value(A);
    .broadcast(tell,bid(A));
    .print("Bid at ",A);
    .wait(1000);
    !tick.
