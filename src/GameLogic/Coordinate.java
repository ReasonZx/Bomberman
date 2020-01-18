package GameLogic;

import java.io.Serializable;

@SuppressWarnings("serial")
/**
 * @author João Gomes up201506251@fe.up.pt
 */
public class Coordinate implements Serializable{
	private int x;
	private int y;
	
	/**
	 * Constructor of the Coordinate class
	 * <p>
	 * Creates a new coordinate (x,y)
	 * <p>
	 * @param x The X Coordinate value
	 * @param y The Y Coordinate value
	 */
	public Coordinate(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	/**
	 * Decreases the Y coordinate value
	 */
	public void MoveUp(){
		y--;
	}
	
	/**
	 * Increases the Y coordinate value
	 */
	public void MoveDown(){
		y++;
	}
	
	/**
	 * Decreases the X coordinate value
	 */
	public void MoveLeft(){
		x--;
	}
	
	/**
	 * Increases the X coordinate value
	 */
	public void MoveRight(){
		x++;
	}
	
	/**
	 * Returns the X coordinate value
	 * @return The X coordinate value
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns the Y coordinate value
	 * @return The Y coordinate value
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Sets the X coordinate value
	 * @param i The X coordinate value
	 */
	public void setX(int i) {
		x=i;
	}
	
	/**
	 * Sets the Y coordinate value
	 * @param i The Y coordinate value
	 */
	public void setY(int i) {
		y=i;
	}
}
