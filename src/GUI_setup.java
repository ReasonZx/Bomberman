import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GUI_setup extends StateBasedGame{
	private int Menu_ID,Login_ID,Game_ID,GameOver_ID,MainMenu_ID,Controls_ID;
	private Settings Player_Settings;

	public GUI_setup(String title) {
		super(title);
	}
	
	public static void main(String args[]) throws SlickException {
		AppGameContainer app = new AppGameContainer(new GUI_setup("Setup"));
		app.setDisplayMode(800, 600, false);
		app.start();
	}

	public void initStatesList(GameContainer container) throws SlickException {
		this.addState(new Menu());
		this.addState(new LogIn());
		this.addState(new Gamestate());
		this.addState(new GameOverState());
		this.addState(new MainMenuState());
		this.addState(new ControlsState());
		Player_Settings= new Settings();
		Player_Settings.init_Settings();
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
	
	public void Set_Menu_State(int x) {
		Menu_ID=x;
	}
	
	public void Set_Login_State(int x) {
		Login_ID=x;
	}
	
	public void Set_Game_State(int x) {
		Game_ID=x;
	}
	
	public void Set_GameOver_State(int x) {
		GameOver_ID=x;
	}
	
	public void Set_MainMenu_State(int x) {
		MainMenu_ID=x;
	}
	
	public void Set_Controls_State(int x) {
		Controls_ID=x;
	}
	
	public int[][] Get_Settings() {
		return Player_Settings.Get_Settings();
	}
	
	public void Set_Settings(int[][] x) {
		Player_Settings.Set_Settings(x);
	}
}
