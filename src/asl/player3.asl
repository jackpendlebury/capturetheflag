// Agent player in project capturetheflag

/* Initial beliefs */

/* Initial goals */

!get.

/* Plans */

+!get : not at(player,flag) <-	!at(player,flag); .fail_goal(get).
+!get : at(player,flag) <- !pickup; .fail_goal(get).

+!pickup : have(flag) <-  !score; .fail_goal(pickup).	
+!pickup : not at(player,flag) <- !get; .fail_goal(pickup).	
+!pickup : at(player,flag) 
	<-  pickup;
	   !score;
	   .succeed_goal(pickup).

+!score : not have(flag) <- !get; .fail_goal(score).
+!score : have(flag) & not at(player,base) <- !at(player,base); .fail_goal(score).
+!score : have(flag) & at(player,base)
<-	 score;
	!get;
	.succeed_goal(score).

+!at(player,P) : at(player,flag) & not have(flag) <- !pickup; .fail_goal(at(player,P)).
+!at(player,P) : at(player,base) & have(flag) <- !score; .fail_goal(at(player,P)).

+!at(player,P) : not at(player,P)
  <-  move_towards(P);
     !at(player,P).
     
+!tackle : flagholder(X) <- tackle(X).

+flagholder(X) <- !!tackle.
+close(A) 		<- .send(A,tell,near(blue)).
+near(red)[source(C)] : not C <- -near(red);
								 +C.
						
					  
