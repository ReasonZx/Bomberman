package GameLogic;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author João Gomes up201506251@fe.up.pt
 */
public class Image_Library implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8459759623730666317L;
	private ArrayList<Element> Flagged = new ArrayList<Element>();
	private ArrayList<String>  Flagged_images = new ArrayList<String>();
	public Map m;
	private int cnt=0;
	
	/**
	 * Flags an Image for change
	 * <p>
	 * This method is required because slick2D needs a slick instance to render or change graphic assets
	 * The serialisation of objects also requires it because you can't change an object during it's serialisation process, 
	 * something that would happen because of the timers on this library
	 * Therefore, this method flags an object to change it's image path for when the run changes method is called.
	 * It adds both parameters to array lists.
	 * <p>
	 * @param x - The Element to be flagged
	 * @param img - The new string path to be changed
	 */
	public void Flag_For_Change(Element x,String img){
			Flagged.add(x);
			Flagged_images.add(img);
	}
	
	/**
	 * Runs the changes stored on the Array Lists
	 * @return The number of changes made in this cycle (used by server to minimise update messages)
	 */
	public int Run_Changes(){
		while(Flagged.size()!=0 && Flagged_images.size()!=0) {
			
			Flagged.get(0).Set_Image(Flagged_images.get(0));
			Flagged.remove(0);
			Flagged_images.remove(0);
			
			cnt++;
		}
		int tmp=cnt;
		cnt=0;
		return tmp;
	}
	
	/**
	 * Increments the change count in case the change was a removal of an object
	 */
	public void Removal_Change() {
		cnt++;
	}
}
