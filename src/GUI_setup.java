import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GUI_setup extends BasicGame{

	public GUI_setup(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	
	public void init(GameContainer container) throws SlickException {

	}

	public void update(GameContainer container, int delta) throws SlickException {
		
	}
	
	public void render(GameContainer container, Graphics g) throws SlickException {
		Image img = new Image("sprites/bomb_explode.png");
		g.drawImage(img, 400, 300, null);
	}
	
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new GUI_setup("setup test"));

		app.setDisplayMode(800, 600, false);
		
		app.start();
		
	}

}
