package GameLogic;

import java.io.Serializable;

public class Coordinate implements Serializable{
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
	
	public void setX(int i) {
		x=i;
	}
	
	public void setY(int i) {
		y=i;
	}
}
