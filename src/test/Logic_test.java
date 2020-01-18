package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.newdawn.slick.SlickException;

import GameLogic.Bomber;
import GameLogic.GameLogic;
import GameLogic.Image_Library;
import GameLogic.Map;
import GameLogic.Wall;

public class Logic_test {

	@Test
	public void Free_Movement_Test() throws SlickException {
		Image_Library lib = new Image_Library();	//Needed because of image changing methods
		Bomber Player = new Bomber(0, 0, lib, null, 0, 0, 0, 0, 0, 0, 0, 0);
		
		Player.MoveUp();
		assertEquals("(0,-1)",Player.getCoord());
		Player.MoveLeft();
		assertEquals("(-1,-1)",Player.getCoord());
		Player.MoveDown();
		assertEquals("(-1,0)",Player.getCoord());
		Player.MoveRight();
		assertEquals("(0,0)",Player.getCoord());
	}
	
	@Test //Takes in consideration map limits and key settings
	public void Logic_Movement_Test() throws SlickException, InterruptedException {	
		Image_Library lib = new Image_Library();
		Map m = new Map(0,10,0,10);
		Bomber Player = new Bomber(1, 1, lib, m, 0, 0, 0, 1, 2, 3, 4, 0);
		GameLogic GL = new GameLogic(lib, m);
		ArrayList<Bomber> players = new ArrayList<Bomber>();
		players.add(Player);
		GL.Place_Characters(players);
		
		GL.Action(Player.Get_MoveUp_Key(), Player.Get_Player()); //Doesn't move, Top Limit
		assertEquals("(1,1)",players.get(0).getCoord());
		
		GL.Action(Player.Get_MoveDown_Key(), Player.Get_Player());
		assertEquals("(1,2)",players.get(0).getCoord());
		GL.Action(Player.Get_MoveDown_Key(), Player.Get_Player()); //Doesn't move, walking is on cooldown
		assertEquals("(1,2)",players.get(0).getCoord());
		
		TimeUnit.MILLISECONDS.sleep(500);
		GL.Action(Player.Get_MoveLeft_Key(), Player.Get_Player()); //Doesn't move, Left Limit
		assertEquals("(1,2)",players.get(0).getCoord());
		
		GL.Action(Player.Get_MoveRight_Key(), Player.Get_Player());
		assertEquals("(2,2)",Player.getCoord());
		
		TimeUnit.MILLISECONDS.sleep(500);
		GL.Action(Player.Get_MoveUp_Key(), Player.Get_Player());
		assertEquals("(2,1)",Player.getCoord());
		
		TimeUnit.MILLISECONDS.sleep(500);
		GL.Action(Player.Get_MoveLeft_Key(), Player.Get_Player());
		assertEquals("(1,1)",Player.getCoord());
	}
	
	@Test
	public void Bomb_Placing_Test() throws SlickException, InterruptedException{
		Image_Library lib = new Image_Library();
		Map m = new Map(0,10,0,10);
		Bomber Player = new Bomber(1, 1, lib, m, 0, 0, 0, 1, 2, 3, 4, 0);
		GameLogic GL = new GameLogic(lib, m);
		ArrayList<Bomber> players = new ArrayList<Bomber>();
		players.add(Player);
		GL.Place_Characters(players);
		
		GL.Action(Player.Get_Action_Key(), Player.Get_Player()); //Places bomb
		
		GL.Action(Player.Get_MoveRight_Key(), Player.Get_Player());
		assertEquals("(2,1)",players.get(0).getCoord());
		
		TimeUnit.MILLISECONDS.sleep(500);
		GL.Action(Player.Get_MoveLeft_Key(), Player.Get_Player()); //Doesn't move, Bomb Blocking
		assertEquals("(2,1)",players.get(0).getCoord());
		
		TimeUnit.MILLISECONDS.sleep(2700);
		GL.Action(Player.Get_MoveLeft_Key(), Player.Get_Player());
		assertEquals("(1,1)",players.get(0).getCoord());
	}

	@Test
	public void Death_Test() throws SlickException, InterruptedException{
		Image_Library lib = new Image_Library();
		Map m = new Map(0,10,0,10);
		Bomber Player = new Bomber(1, 1, lib, m, 0, 0, 0, 1, 2, 3, 4, 0);
		GameLogic GL = new GameLogic(lib, m);
		ArrayList<Bomber> players = new ArrayList<Bomber>();
		players.add(Player);
		GL.Place_Characters(players);
		
		GL.Action(Player.Get_Action_Key(), Player.Get_Player()); //Places bomb
		
		TimeUnit.MILLISECONDS.sleep(3200);
		
		assertEquals(1,GL.Death_Check());
	}
	
	@Test
	public void Wall_Logic_Test() throws SlickException, InterruptedException{
		Image_Library lib = new Image_Library();
		Map m = new Map(0,10,0,10);
		Bomber Player = new Bomber(1, 1, lib, m, 0, 0, 0, 1, 2, 3, 4, 0);
		GameLogic GL = new GameLogic(lib, m);
		ArrayList<Bomber> players = new ArrayList<Bomber>();
		players.add(Player);
		GL.Place_Characters(players);
		m.Add_Element(new Wall(2,1,lib,m,2));	//Added destroyable wall
		m.Add_Element(new Wall(1,2,lib,m,1));	//Added non destroyable wall
		
		GL.Action(Player.Get_Action_Key(), Player.Get_Player()); //Places bomb
		
		GL.Action(Player.Get_MoveRight_Key(), Player.Get_Player()); //Doesn't move, Wall blocking
		assertEquals("(1,1)",players.get(0).getCoord());
		GL.Action(Player.Get_MoveDown_Key(), Player.Get_Player()); //Doesn't move, Wall blocking
		assertEquals("(1,1)",players.get(0).getCoord());
		
		TimeUnit.MILLISECONDS.sleep(3300);
		
		GL.Action(Player.Get_MoveDown_Key(), Player.Get_Player()); //Doesn't move, Wall still blocking
		assertEquals("(1,1)",players.get(0).getCoord());
		GL.Action(Player.Get_MoveRight_Key(), Player.Get_Player()); //Can move, wall destroyed
		assertEquals("(2,1)",players.get(0).getCoord());
	}
}
