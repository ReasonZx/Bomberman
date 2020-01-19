 package client;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GUI_setup extends StateBasedGame{
	private int Menu_ID,Login_ID,Game_ID,GameOver_ID,MainMenu_ID,Controls_ID,Signup_ID,OnlineGame_ID,OnlineGameOver_ID,Friends_ID,LockedMenu_ID, LockedSettingsState_ID, StatisticsState_ID;
	private boolean LockedStates = false;
	private static Settings Player_Settings;
	private static int display_x;
	private static int display_y;
	private static int Resolution;
	private static int GUI_Scale;

	public ServerHandler server;

	public GUI_setup(String title) {
		super(title);
	}

	public static void main(String args[]) throws SlickException {
		if (System.getProperty("os.name").contains("Windows")) {
		    // Windows
		    System.setProperty("org.lwjgl.librarypath", new File("libraries/natives-windows").getAbsolutePath());
		} else if (System.getProperty("os.name").contains("Mac")) {
		    // Mac OS X
		    System.setProperty("org.lwjgl.librarypath", new File("libraries/natives-mac").getAbsolutePath());
		} else if (System.getProperty("os.name").contains("Linux")) {
		    // Linux
		    System.setProperty("org.lwjgl.librarypath", new File("libraries/natives-linux").getAbsolutePath());
		} else {
		    throw new RuntimeException("Your OS is not supported");
		}
		
		AppGameContainer app = new AppGameContainer(new GUI_setup("Setup"));
		Player_Settings= new Settings();
		Player_Settings.init_Settings();
		
		Resolution=Get_Resolution();
		if(Resolution==1) {
			display_x = 1600;
			display_y = 900;
			GUI_Scale=78;
		}
		else if(Resolution==2){
			display_x = 1366;
			display_y = 768;
			GUI_Scale=64;
		}
		else{
			display_x = 1024;
			display_y = 768;
			GUI_Scale=50;
		}
		
		app.setDisplayMode(display_x, display_y, false);
		app.setShowFPS(false);
		app.start();
	}

	public void initStatesList(GameContainer container) throws SlickException {
		this.addState(new Menu());
		this.addState(new LogIn());
		this.addState(new Gamestate());
		this.addState(new GameOverState());
		this.addState(new MainMenuState());
		this.addState(new ControlsState());
		this.addState(new SignupState());
		this.addState(new OnlineGameState());
		this.addState(new OnlineGameOverState());
        this.addState(new FriendsState());
        this.addState(new LockedMenuState());
        this.addState(new LockedSettingsState());
        this.addState(new StatisticsState());

		try {
			ServerHandler server = new ServerHandler();
			this.server = server;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cant' connect to server, restart the game and try again later.\nServer might be down.");
			e.printStackTrace();
		}

	}

	public int Get_Menu_State() {
		return Menu_ID;
	}

	public int Get_Login_State() {
		return Login_ID;
	}

	public int Get_Game_State() {
		return Game_ID;
	}

	public int Get_GameOver_State() {
		return GameOver_ID;
	}

	public int Get_MainMenu_State() {
		return MainMenu_ID;
	}

	public int Get_Controls_State() {
		return Controls_ID;
	}
    
    public int Get_Friends_State() {
		return Friends_ID;
    }
    
    public int Get_LockedMenu_State() {
		return LockedMenu_ID;
    }
    
    public int Get_LockedSettings_State() {
		return LockedSettingsState_ID;
    }
    
    public void Set_Friends_State(int x) {
		Friends_ID=x;
	}
    
    public void Set_LockedSettings_State(int x) {
		LockedSettingsState_ID=x;
	}
	
	public int Get_Signup_State() {
		return Signup_ID;
	}
	
	public int Get_OnlineGame_State() {
		return OnlineGame_ID;
	}
	
	public int Get_OnlineGameOver_State() {
		return OnlineGameOver_ID;
	}
	
	public int Get_Statistics_State() {
		return StatisticsState_ID;
	}
	
	public void Set_Menu_State(int x) {
		Menu_ID = x;
	}

	public void Set_Login_State(int x) {
		Login_ID = x;
	}

	public void Set_Game_State(int x) {
		Game_ID = x;
	}

	public void Set_GameOver_State(int x) {
		GameOver_ID = x;
	}
	
	public void Set_OnlineGameOver_State(int x) {
		OnlineGameOver_ID = x;
	}
	
	public void Set_LockedMenu_State(int x) {
		LockedMenu_ID=x;
	}
	
	public int Get_Display_height() {
		return display_y;
	}
	
	public int Get_Display_width() {
		return display_x;
	}
	
	
	public void Set_MainMenu_State(int x) {
		MainMenu_ID = x;
	}

	public void Set_Controls_State(int x) {
		Controls_ID = x;
	}
	
	public void Set_Signup_State(int x) {
		Signup_ID=x;
	}
	
	public void Set_OnlineGame_State(int x) {
		OnlineGame_ID=x;
	}
	
	public void Set_Statistics_State(int x) {
		StatisticsState_ID = x;
	}
	
	public int[][] Get_Settings() {
		return Player_Settings.Get_Settings();
	}

	public void Set_Settings(int[][] x) {
		Player_Settings.Set_Settings(x);
	}

	public void Change_Settings_Key(int key, int player, int pos) {
		Player_Settings.Add_New_Key(key, player, pos);
	}
	
	public void Change_Settings_Color(int arrow,int player,int pos) {
		Player_Settings.Add_New_Color(arrow, player, pos);
	}
	
	public void Reset_Settings() {
		try {
			Player_Settings.Write_Default_Settings();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Set_locked_State(boolean x) {
		LockedStates=x;
	}
	
	public boolean Get_locked_State() {
		return LockedStates;
	}
	
	public static int Get_Resolution() {
		return Player_Settings.Get_Resolution();
	}
	
	public void Set_Resolution(int res){
		Player_Settings.Set_Resolution(res);
	}
	
	public void Set_Volume(int vol){
		Player_Settings.Set_Volume(vol);
	}
	
	public int Get_Volume() {
		return Player_Settings.Get_Volume();
	}
	
	public int Get_GUI_Scale() {
		return GUI_Scale;
	}
}
