import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GUI_setup extends StateBasedGame{
	private int Menu_ID,Login_ID,Game_ID,GameOver_ID;
	private static int display_x;
	private static int display_y;

	public GUI_setup(String title) {
		super(title);
	}
	
	public static void main(String args[]) throws SlickException {
		AppGameContainer app = new AppGameContainer(new GUI_setup("Setup"));
		display_x = 1280;
		display_y = 720;
		app.setDisplayMode(display_x, display_y, false);
		app.setShowFPS(false);
		app.start();
	}

	public void initStatesList(GameContainer container) throws SlickException {
		this.addState(new Menu());
		this.addState(new LogIn());
		this.addState(new Gamestate());
		this.addState(new GameOverState());
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
	
	public int Get_Display_height() {
		return display_y;
	}
	
	public int Get_Display_width() {
		return display_x;
	}
	
}
