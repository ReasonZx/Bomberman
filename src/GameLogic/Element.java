package GameLogic;

import java.io.Serializable;

@SuppressWarnings("serial")
/**
 * @author João Gomes up201506251@fe.up.pt
 */
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
	
	/**
	 * Returns the X coordinate value
	 * @return The X coordinate value
	 */
	public int getX() {
		return Coord.getX();
	}
	
	/**
	 * Returns the Y coordinate value
	 * @return The Y coordinate value
	 */
	public int getY() {
		return Coord.getY();
	}
	
	/**
	 * Sets the X coordinate value
	 * @param i The X coordinate value
	 */
	public void setX(int i) {
		Coord.setX(i);
	}
	
	/**
	 * Sets the Y coordinate value
	 * @param i The Y coordinate value
	 */
	public void setY(int i) {
		Coord.setY(i);
	}
	
	/**
	 * Returns the Coordinate of the Element on a string format
	 * @return "(X,Y)"
	 */
	public String getCoord(){
		return "(" + Coord.getX() + "," + Coord.getY() + ")";
	}
	
	/**
	 * Returns if an element is solid or not
	 * @return boolean value of Solid
	 */
	public boolean Is_Solid() {
		return Solid;
	}
	
	/**
	 * Returns if an element is Destroyable or not
	 * @return boolean value of Destroyable
	 */
	public boolean Is_Destroyable() {
		return Destroyable;
	}
	
	/**
	 * Sets the path of the image to render
	 * @param tmp The String path of the image to render
	 */
	protected void Set_Image(String tmp){
		img = tmp;
	}
	
	/**
	 * Get the path of the image to render
	 * @return The String path of the image to render
	 */
	public String Get_Image() {
		return img;
	}
	
	/**
	 * Returns if an element currently has an image or not
	 * @return True if img string is not null
	 */
	public boolean Has_Image() {
		if(img==null)
			return false;
		else
			return true;
	}
	
	/**
	 * Gets the value of the GUI_Scale
	 * @return Value of GUI_Scale
	 */
	public int Get_Scale() {
		return GUI_Scale;
	}
	
	/**
	 * Serialises data into a string format to be sent over the object socket of the server
	 */
	public String toString() {
		return Short.toString(serialVersionUID)+","+Integer.toString(getX())+","+Integer.toString(getY())+","+img;
	}
	
	protected void Destroy() {
		
	}
}
