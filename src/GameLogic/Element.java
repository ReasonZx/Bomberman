package GameLogic;
import java.io.Serializable;

public abstract class Element implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7264664472479783418L;
	protected Coordinate Coord;
	protected boolean Solid;
	protected boolean Destroyable=false;
	protected int GUI_Scale;
	protected int GUI_OffsetX,GUI_OffsetY;

	protected String img;
	protected Map m;
	protected Image_Library lib;
	
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
	
	protected void Set_Image(String tmp){
		img = tmp;
	}

	public String Get_Image() {
		return img;
	}
	
	public boolean Has_Image() {
		if(img==null)
			return false;
		else
			return true;
	}
	
	public int Get_Scale() {
		return GUI_Scale;
	}
	
	public float Get_OffsetX() {
		return GUI_OffsetX;
	}
	
	public float Get_OffsetY() {
		return GUI_OffsetY;
	}
	
	public String toString() {
		return Integer.toString(getX()) + Integer.toString(getY());
	}

	protected void Destroy() {
		
	}
}
