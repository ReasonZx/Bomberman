import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Wall extends Element {
	
	Wall(int x,int y) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		Destroyable = true;
		Set_Image(new Image("sprites/Wall.png"));
	}

}
