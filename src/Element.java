import org.newdawn.slick.Image;

public abstract class Element{
	protected Coordinate Coord;
	protected boolean Solid;
	protected boolean Destroyable=false;
<<<<<<< src/Element.java
	Image img;
=======
	GameLogic L;
>>>>>>> src/Element.java
	
	public int getX() {
		return Coord.getX();
	}
	
	public int getY() {
		return Coord.getY();
	}
	
	public String getCoord(){
		return "(" + Coord.getX() + "," + Coord.getY() + ")";
	}
	
	public boolean Is_Solid() {
		return Solid;
	}
	
	public boolean Is_Destroyable() {
		return Destroyable;
	}
	
<<<<<<< src/Element.java
	public Image Get_Image() {
		return img;
	}
	
	public String toString() {
		return Integer.toString(getX()) + Integer.toString(getY());
=======
	public void Set_GameLogic(GameLogic x) {
		L=x;
>>>>>>> src/Element.java
	}
}
