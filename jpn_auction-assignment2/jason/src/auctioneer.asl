startPrice(0).
increment(1).

!wait_for_people_to_join.

+!wait_for_people_to_join <-
	.wait(4000);
 	!auction.

+!auction : startPrice(Price) <-
	-startPrice(Price);
    +value(Price);
    !auction.

+!auction : .count(bidder(_),Bidder) & Bidder == 1 & bidder(_)[source(Name)]<-
    .print("The Winner is:", Name).

+!auction : .count(bidder(_),Bidder) & Bidder == 0 <-
	?lastBidder(L);
    .print("Increment was too high. Latest one to leave and winner is: ", L).

+!auction : value(X) & increment(I) <-
    Amount = X + I;
    -+value(Amount);
	.print("Bid at ",Amount);
    .broadcast(tell,bid(Amount));
    .wait(2000);
    !auction.

+!lastBidder(L) <-
	-+lastBidder(L).
