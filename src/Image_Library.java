import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Image_Library {
	ArrayList<Element> Flagged = new ArrayList<Element>();
	ArrayList<String>  Flagged_images = new ArrayList<String>();
	
	public void Flag_For_Change(Element x,String img){
		Flagged.add(x);
		Flagged_images.add(img);
	}
	
	public void Run_Changes() throws SlickException {
		while(Flagged.size()!=0) {
			Flagged.get(0).Set_Image(new Image(Flagged_images.get(0)));
			Flagged.remove(0);
			Flagged_images.remove(0);
		}
	}
}
