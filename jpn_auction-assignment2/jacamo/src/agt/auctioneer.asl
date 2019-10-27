{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }

+!wait_for_people_to_join <-
    .wait(4000);
    -startPrice(Price);
    startAuction(Price).

+!auction : numBidders(N) & N > 1 <-
    ?increment(I);
    ?price(Price);
    P = Price + I;
    .print("Bid at ", P);
    inc(I);
    .wait(2000);
    !auction.

+!auction : numBidders(N) & N == 1 <-
    ?winner(Winner);
    .print("Auction complete. Winner is ", Winner).

+!auction : numBidders(N) & N < 1 <-
    .print("There was a tie for the auction").