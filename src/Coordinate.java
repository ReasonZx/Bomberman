
public class Coordinate {
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
