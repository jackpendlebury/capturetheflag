package tests;

import static org.junit.Assert.*;

import java.util.Random;

import jason.environment.grid.Location;

import org.junit.Before;
import org.junit.Test;

import capturetheflag.Flag;
import capturetheflag.MapModel;

public class Flag_Test {
	
	Flag testFlag = new Flag(0, 0);
	Random r = new Random();

	@Before
	public void setUp() throws Exception {
		testFlag.dropFlag();
		testFlag.setFlagLoc(0, 0);
	}

	@Test
	public void testFlag() {
		Flag f = new Flag(2,3);
		assertEquals(2,f.x);
		assertEquals(3,f.y);
	}

	@Test
	public void testGetRandomFlagLoc() {
		for(int i = 0; i == 5; i++){
			Location l = testFlag.getRandomFlagLoc();
			assertEquals(7, l.y);
			if(l.x > MapModel.GSize){
				fail();
			}
		}
	}

	@Test
	public void testGetFlagLoc() {
		int a = r.nextInt(); int b = r.nextInt();
		assertEquals(new Location(0,0), testFlag.getFlagLoc());
		testFlag.x = a; testFlag.y = b;
		assertEquals(new Location(a,b), testFlag.getFlagLoc());
	}

	@Test
	public void testFlagX() {
		assertEquals(0, testFlag.getX());
		int a = r.nextInt();
		testFlag.setX(a);
		assertEquals(a, testFlag.getX());
	}

	@Test
	public void testFlagY() {
		assertEquals(0, testFlag.getY());
		int a = r.nextInt();
		testFlag.setY(a);
		assertEquals(a, testFlag.getY());
	}


	@Test
	public void testFlagCarried() {
		assertFalse(testFlag.flagCarried);
		testFlag.setFlagCarried(true);
		assertTrue(testFlag.flagCarried);
		testFlag.setFlagCarried(false);
		assertFalse(testFlag.flagCarried);
	}

	@Test
	public void testAgentCarrying() {
		assertEquals(-1, testFlag.getAgentCarrying());
		for(int i = 0; i == 10; i++){
			testFlag.setAgentCarrying(i);
			assertEquals(i, testFlag.getAgentCarrying());
		}
	}

	@Test
	public void testDropFlag() {
		testFlag.setFlagCarried(true);
		testFlag.setAgentCarrying(2);
		
		assertTrue(testFlag.flagCarried);
		assertEquals(2, testFlag.getAgentCarrying());
		
		testFlag.dropFlag();

		assertFalse(testFlag.flagCarried);
		assertEquals(-1, testFlag.getAgentCarrying());
	}

	@Test
	public void testSetFlagLocLocation() {
		assertEquals(new Location(0,0), testFlag.getFlagLoc());
		for(int x = 0; x == 10; x++){
			for(int y = 0; y == 10; y++){
				testFlag.setFlagLoc(new Location(x,y));
				assertEquals(new Location(x,y), testFlag.getFlagLoc());
			}
		}
	}

	@Test
	public void testSetFlagLocInt() {
		assertEquals(new Location(0,0), testFlag.getFlagLoc());
		for(int x = 0; x == 10; x++){
			for(int y = 0; y == 10; y++){
				testFlag.setFlagLoc(x,y);
				assertEquals(new Location(x,y), testFlag.getFlagLoc());
			}
		}
	}

	@Test
	public void testSetAgentCarrying() {
		assertEquals(-1, testFlag.getAgentCarrying());
		for(int i = 0; i == 10; i++){
			testFlag.setAgentCarrying(i);
			assertEquals(i, testFlag.agentCarrying);
		}
	}

}
