package GameLogic;

import java.io.Serializable;

public class Coordinate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6846431225379720004L;
	private int x;
	private int y;
	
	Coordinate(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public void MoveUp(){
		y--;
	}
	
	public void MoveDown(){
		y++;
	}
	
	public void MoveLeft(){
		x--;
	}
	
	public void MoveRight(){
		x++;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
