// Agent player in project capturetheflag

/* Initial beliefs and rules */
want(flag).

/* Initial goals */

!get(flag).

/* Plans */

+!get(flag) : want(flag) & at(player, flag) <- !pickup(flag).
+!get(flag) : want(flag) & not at(player, flag) <-	!at(player,flag).
	
+!pickup(flag) : not at(player, flag) <- !get(flag).	
+!pickup(flag) : want(flag) & at(player,flag) 
	<- -want(flag); +got(flag);
	   pickup(flag);
	   !at(player, base).

+!score : got(flag) & not at(player, base) <- !at(player, base).
+!score : got(flag) & at(player, base)
<-	score(flag);
	-got(flag);
	.print("Flag Scored").

+!at(player,P) : at(player,P) <- true.
+!at(player,P) : not at(player,P)
  <- move_towards(P);
     !at(player,P).
