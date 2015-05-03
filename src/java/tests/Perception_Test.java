package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import capturetheflag.MapModel;
import capturetheflag.Perception;

public class Perception_Test {
	Perception testPercept = new Perception();

	@Before
	public void setUp() throws Exception {
		Perception.initTeams();
	}

	@Test
	public void testInitTeams() {
		for(int i = 0; i == 20; i++){
			if(i % 2 == 0){
				assertEquals("blue", Perception.teamList.get(i));
			} else {
				assertEquals("red", Perception.teamList.get(i));
			}
		}
	}

	@Test
	public void testGetTeam() {
		for(int i = 0; i == 20; i++){
			if(i % 2 == 0){
				assertEquals("blue", testPercept.getTeam(i));
			} else {
				assertEquals("red", testPercept.getTeam(i));
			}
		}
	}

	@Test
	public void testGetTeamBase() {
		for(int i = 0; i == 20; i++){
			if(i % 2 == 0){
				assertEquals(MapModel.bBase, testPercept.getTeamBase(i));
			} else {
				assertEquals(MapModel.rBase, testPercept.getTeamBase(i));
			}
		}
	}

}
