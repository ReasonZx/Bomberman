package GameLogic;

import java.io.Serializable;

public abstract class Element implements Serializable{
	/**
	 * 
	 */
	public short serialVersionUID;
	protected Coordinate Coord;
	protected boolean Solid;
	protected boolean Destroyable=false;
	static protected int GUI_Scale=64;

	protected String img;
	protected Map m;
	public Image_Library lib;
	
	public int getX() {
		return Coord.getX();
	}
	
	public int getY() {
		return Coord.getY();
	}
	
	public void setX(int i) {
		Coord.setX(i);
	}
	
	public void setY(int i) {
		Coord.setY(i);
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
	
	public String toString() {
		return Short.toString(serialVersionUID)+","+Integer.toString(getX())+","+Integer.toString(getY())+","+img;
	}

	protected void Destroy() {
		
	}
}
