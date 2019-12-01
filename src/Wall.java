import org.newdawn.slick.SlickException;

public class Wall extends Element {
	protected String Wall = "sprites/Wall.png";
	
	Wall(int x,int y,GameLogic L) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		Destroyable = true;
		this.L=L;
		L.lib.Flag_For_Change(this, Wall);
		GUI_Scale=64;
		GUI_OffsetX=0;
		GUI_OffsetY=0;
	}
	
	
}
