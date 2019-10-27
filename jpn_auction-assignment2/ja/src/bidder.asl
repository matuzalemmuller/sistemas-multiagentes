!join.

+!join <-
    .random(R1);
    S = R1 * 10;
    Amount = math.ceil(S);
    +bankAccount(Amount);
    .print("I'm entering the auction and I have ",Amount);
    .send(auctioneer,tell,bidder(Amount)).

+bid(Value) <-
    -+bidReceived(Value);
    -bid(Value);
    !check.

+!check : bidReceived(Value) & bankAccount(Balance) & Value < Balance  <-
    .print("I'm still in the auction.").

+!check : bidReceived(Value) & bankAccount(Balance) & Value == Balance <-
    .print("I'm all in.").

+!check <-
    ?bankAccount(C);
    .print("I'm leaving the auction");
    .send(auctioneer,untell,bidder(C));
    .my_name(Name);
    .send(auctioneer,achieve,lastBidder(Name));
    .kill_agent(Name).