import org.newdawn.slick.Image;

public abstract class Element{
	protected Coordinate Coord;
	protected boolean Solid;
	protected boolean Destroyable=false;
	Image img;
	
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
	
	public Image Get_Image() {
		return img;
	}
	
	public String toString() {
		return Integer.toString(getX()) + Integer.toString(getY());
	}
}
