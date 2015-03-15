// Agent player in project capturetheflag

/* Initial beliefs and rules */
want(flag).

/* Initial goals */

!get(flag).

/* Plans */

+!get(flag) : want(flag) & not at(player, flag) <-	!at(player,flag).
+!get(flag) : want(flag) & at(player, flag) <- !pickup(flag).

+!pickup(flag) : got(flag) <- !score(flag).	
+!pickup(flag) : not at(player, flag) <- !get(flag).	
+!pickup(flag) : want(flag) & at(player,flag) 
	<- pickup(flag);
	   -want(flag); +got(flag);
	   !score(flag).

+!score(flag) : not got(flag) <- !get(flag).
+!score(flag) : got(flag) & not at(player, base) <- !at(player, base).
+!score(flag) : got(flag) & at(player, base)
<-	score(flag);
	-got(flag);
	.print("Flag Scored!");
	+want(flag); !get(flag).
	
+!at(player,P) : at(player, flag) & not got(flag) <- !pickup(flag).
+!at(player,P) : at(player, base) & got(flag) <- !score(flag).
+!at(player,P) : not at(player,P)
  <- move_towards(P);
     !at(player,P).
