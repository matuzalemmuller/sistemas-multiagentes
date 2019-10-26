!start.

+!start <-
    .random(R1);
	A = R1 * 100;
	C = math.ceil(A);
    +bankAccount(C);
    .print("I'm entering the auction and I have ",C);
    .send(auction,tell,bidder(C)).

+bid(Value) <-
    -+bidReceived(Value);
    -bid(Value);
    !check.

+!check : bidReceived(Value) & bankAccount(Balance) & Value < Balance <-
	.print("I'm still in the auction.").

+!check : bidReceived(Value) & bankAccount(Balance) & Value == Balance <-
    .print("I'm all in.").

+!check <-
    .send(auction,untell,bidder(Balance));
    .print("I'm leaving the auction");
    .my_name(A);
    .kill_agent(A).
