{ include("$jacamoJar/templates/common-cartago.asl") }

!wait_for_people_to_join.

+!wait_for_people_to_join : startPrice(Price) <-
	.wait(4000);
	startAuction(Price);
 	!auction.

+!auction : numBidders(N) & N > 1 & price(Price) & increment(I) <-
	P = Price + I;
	.print("Bid at ", P);
	inc(I);
	.wait(2000);
	!auction.

+!auction : numBidders(N) & N == 1 & winner(Winner) <-
	.print("Auction complete. Winner is ", Winner).

+!auction : numBidders(N) & N < 1 <-
	.print("There was a tie for the auction").