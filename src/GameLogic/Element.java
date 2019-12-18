package GameLogic;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class Element{
	protected Coordinate Coord;
	protected boolean Solid;
	protected boolean Destroyable=false;
	protected int GUI_Scale;
	protected int GUI_OffsetX,GUI_OffsetY;

	protected Image img;
	protected GameLogic L;
	
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
	
	protected void Set_Image(Image tmp) throws SlickException{
		img = tmp.copy();
	}

	public Image Get_Image() {
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
	
	public int Get_OffsetX() {
		return GUI_OffsetX;
	}
	
	public int Get_OffsetY() {
		return GUI_OffsetY;
	}
	
	public String toString() {
		return Integer.toString(getX()) + Integer.toString(getY());
	}
	
	public void Set_GameLogic(GameLogic x) {
		L=x;
	}

	protected void Destroy() {
		
	}
}
