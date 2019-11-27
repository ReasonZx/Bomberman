import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Bomber extends Element{
	
	Bomber(int x,int y) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		img = new Image("sprites/parado.png");
	}
	
	public void MoveUp() {
		Coord.MoveUp();
	}
	
	public void MoveRight() {
		Coord.MoveRight();
	}
	
	public void MoveLeft() {
		Coord.MoveLeft();
	}
	
	public void MoveDown() {
		Coord.MoveDown();
	}
	
	
}
