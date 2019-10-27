{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }

+!join <-
    +exited(false);
    .random(R1);
    C = R1 * 100;
    Amount = math.ceil(C);
    +bankAccount(Amount);
    .my_name(Name);
    join(Name);
    .wait(2000);
    .print("I'm entering the auction and I have ", Amount).

+price(Value) : bankAccount(Balance) & Balance > Value & Value > 0 & exited(E) & E == false <-
    .print("I'm still in the auction.").

+price(Value) : bankAccount(Balance) & Balance == Value & exited(E) & E == false <-
    .print("I'm all in.").

+price(Value) : bankAccount(Balance) & Balance < Value & exited(E) & E == false <-
    .print("I'm leaving the auction");
    .my_name(Name);
    exit(Name);
    -+exited(true).