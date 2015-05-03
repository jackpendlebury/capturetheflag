package tests;

import static org.junit.Assert.*;
import jason.environment.grid.Location;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import capturetheflag.Flag;
import capturetheflag.MapModel;

public class Model_Test {
	
	MapModel testModel = new MapModel();
	Flag testFlag = testModel.flag;

	@Before
	public void setUp() throws Exception {
		MapModel.GSize = 15;
		MapModel.TotAgt = 2;
		
		testModel.bscore = 0;
		testModel.rscore = 0;
		
		testFlag.dropFlag();
	}


	@Ignore
	public void testInitAgents() {
		MapModel.GSize = 11;
		MapModel.TotAgt = 12;
		assertFalse("InitAgts catches TotAgt > GSize Error", testModel.initAgents());
		
		MapModel.GSize = 10;
		MapModel.TotAgt = 0;
		assertFalse("InitAgts catches TotAgt = 0 Error", testModel.initAgents());
		
		MapModel.GSize = 11;
		MapModel.TotAgt = 2;
		assertTrue("InitAgts functions with correct data", testModel.initAgents());
	}

	@Test
	public void testRound() {
		double i = 15;
		assertEquals(7, MapModel.round(i/2));
		i = 20;
		assertEquals(6, MapModel.round(i/3));
		i = 7;
		assertEquals(3, MapModel.round(i/2));
	}

	@Test
	public void testInitFlag() {
		assertEquals(new Location(7,7), testFlag.getFlagLoc());
	}
	
	@Test
	public void testPickUpFlag(){
		testFlag.flagCarried = true;
		testFlag.agentCarrying = 2;
		assertFalse(testModel.pickupFlag("player1"));
		
		testFlag.flagCarried = false;
		testFlag.agentCarrying = -1;
		assertTrue(testModel.pickupFlag("player1"));
	}

	@Test
	public void testScoreFlag() {
		testFlag.flagCarried = true;
		testFlag.agentCarrying = 2;
		assertFalse(testModel.scoreFlag("player1"));
		
		testFlag.agentCarrying = 0;
		assertTrue(testModel.scoreFlag("player1"));
	}
	
	@Test
	public void testScoring(){
		testFlag.flagCarried = true;
		testFlag.agentCarrying = 0;
		assertTrue(testModel.scoreFlag("player1"));
		assertEquals(1, testModel.bscore);
		assertEquals(0, testModel.rscore);
		
		testModel.bscore = 0;
		testFlag.agentCarrying = 1;
		assertTrue(testModel.scoreFlag("player2"));
		assertEquals(0, testModel.bscore);
		assertEquals(1, testModel.rscore);
	}

	@Test
	public void testTakeFlag() {
		testFlag.flagCarried = true;
		testFlag.agentCarrying = 1;
		
		assertTrue(testModel.takeFlag("player1", "player2"));
		assertEquals(-1, testFlag.agentCarrying);
		
		testFlag.flagCarried = false;
		assertFalse(testModel.takeFlag("player1", "player2"));
	}

	@Test
	public void testGetAgentID() {
		for(int i = 1; i >= 20; i++){
			assertEquals(i-1,testModel.getAgentID("player" + i));
		}
	}

	@Test
	public void testGetAgName() {
		for(int i = 1; i >= 20; i++){
			assertEquals("player"+ i,testModel.getAgName(i-1));
		}
	}

}
