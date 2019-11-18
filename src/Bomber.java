
public class Bomber extends Element{
	
	Bomber(int x,int y){
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
	}
	
	public void MoveUp() {
		Coord.MoveUp();
	}
	
	public void MoveRight() {
		Coord.MoveRight();
	}
	
	public void MoveLeft() {
		Coord.MoveLeft();
	}
	
	public void MoveDown() {
		Coord.MoveDown();
	}
}
