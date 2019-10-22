!start.

+!start <-
    .random(R1);
    +bankAccount(R1*10);
    .print("I'm entering the auction and I have ",R1*10);
    .send(auction,tell,bidder(R1*10)).

+bid(Value) <-
    -+bidReceived(Value);
    -bid(Value);
    !check.

+!check : bidReceived(Value) & bankAccount(Balance) & Value > Balance <-
    .send(auction,untell,bidder(Balance));
    .print("I'm leaving the auction");
    .my_name(A);
    .kill_agent(A).
+!check <-
    .print("I'm all in.").
