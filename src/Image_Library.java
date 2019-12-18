
import java.io.Serializable;
import java.util.ArrayList;

import org.newdawn.slick.SlickException;

public class Image_Library implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8459759623730666317L;
	private ArrayList<Element> Flagged = new ArrayList<Element>();
	private ArrayList<String>  Flagged_images = new ArrayList<String>();
	public Map m;
	
	public void Flag_For_Change(Element x,String img){
			Flagged.add(x);
			Flagged_images.add(img);
	}
	
	public void Run_Changes() throws SlickException {
		while(Flagged.size()!=0 && Flagged_images.size()!=0) {
			
			Flagged.get(0).Set_Image(Flagged_images.get(0));
			Flagged.remove(0);
			Flagged_images.remove(0);
		}
	}
}
