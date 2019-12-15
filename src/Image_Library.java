import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Image_Library {
	ArrayList<Element> Flagged = new ArrayList<Element>();
	ArrayList<String>  Flagged_images = new ArrayList<String>();
	ArrayList<String>  Sprites = new ArrayList<String>();
	ArrayList<ArrayList<Image>> PlayerSprites;
	
	public void Flag_For_Change(Element x,String img){
		Flagged.add(x);
		Flagged_images.add(img);
	}
	
	public void Run_Changes() throws SlickException {
		while(Flagged.size()!=0 && Flagged_images.size()!=0) {
			switch(Flagged_images.get(0)) {
			case "StopDown":
				Flagged.get(0).Set_Image(PlayerSprites.get(((Bomber) Flagged.get(0)).Get_Player()).get(0));
				Flagged.remove(0);
				Flagged_images.remove(0);
				break;
			case "StopLeft":
				Flagged.get(0).Set_Image(PlayerSprites.get(((Bomber) Flagged.get(0)).Get_Player()).get(0));
				Flagged.get(0).Get_Image().setRotation(90);
				Flagged.remove(0);
				Flagged_images.remove(0);
				break;
			case "StopUp":
				Flagged.get(0).Set_Image(PlayerSprites.get(((Bomber) Flagged.get(0)).Get_Player()).get(0));
				Flagged.get(0).Get_Image().setRotation(180);
				Flagged.remove(0);
				Flagged_images.remove(0);
				break;
			case "StopRight":
				Flagged.get(0).Set_Image(PlayerSprites.get(((Bomber) Flagged.get(0)).Get_Player()).get(0));
				Flagged.get(0).Get_Image().setRotation(270);
				Flagged.remove(0);
				Flagged_images.remove(0);
				break;
			case "Down1":
				Flagged.get(0).Set_Image(PlayerSprites.get(((Bomber) Flagged.get(0)).Get_Player()).get(1));
				Flagged.remove(0);
				Flagged_images.remove(0);
				break;
			case "Down2":
				Flagged.get(0).Set_Image(PlayerSprites.get(((Bomber) Flagged.get(0)).Get_Player()).get(1).getFlippedCopy(true, false));
				Flagged.remove(0);
				Flagged_images.remove(0);
				break;
			case "Left1":
				Flagged.get(0).Set_Image(PlayerSprites.get(((Bomber) Flagged.get(0)).Get_Player()).get(1));
				Flagged.get(0).Get_Image().setRotation(90);
				Flagged.remove(0);
				Flagged_images.remove(0);
				break;
			case "Left2":
				Flagged.get(0).Set_Image(PlayerSprites.get(((Bomber) Flagged.get(0)).Get_Player()).get(1).getFlippedCopy(true, false));
				Flagged.get(0).Get_Image().setRotation(90);
				Flagged.remove(0);
				Flagged_images.remove(0);
				break;
			case "Up1":
				Flagged.get(0).Set_Image(PlayerSprites.get(((Bomber) Flagged.get(0)).Get_Player()).get(1));
				Flagged.get(0).Get_Image().setRotation(180);
				Flagged.remove(0);
				Flagged_images.remove(0);
				break;
			case "Up2":
				Flagged.get(0).Set_Image(PlayerSprites.get(((Bomber) Flagged.get(0)).Get_Player()).get(1).getFlippedCopy(true, false));
				Flagged.get(0).Get_Image().setRotation(180);
				Flagged.remove(0);
				Flagged_images.remove(0);
				break;
			case "Right1":
				Flagged.get(0).Set_Image(PlayerSprites.get(((Bomber) Flagged.get(0)).Get_Player()).get(1));
				Flagged.get(0).Get_Image().setRotation(270);
				Flagged.remove(0);
				Flagged_images.remove(0);
				break;
			case "Right2":
				Flagged.get(0).Set_Image(PlayerSprites.get(((Bomber) Flagged.get(0)).Get_Player()).get(1).getFlippedCopy(true, false));
				Flagged.get(0).Get_Image().setRotation(270);
				Flagged.remove(0);
				Flagged_images.remove(0);
				break;
			default:
				Flagged.get(0).Set_Image(new Image(Flagged_images.get(0)));
				Flagged.remove(0);
				Flagged_images.remove(0);
			}
		}
	}
	
	public void Initialize_Image_Library() throws SlickException {
		PlayerSprites = new ArrayList<ArrayList<Image>>();
		while(Sprites.size()!=0){
			ArrayList<Image> listtmp = new ArrayList<Image>();
			
			Image tmp1 = new Image(Sprites.get(0));
			listtmp.add(tmp1);
			Sprites.remove(0);
			
			Image tmp2 = new Image(Sprites.get(0));
			listtmp.add(tmp2);
			Sprites.remove(0);
			
			PlayerSprites.add(listtmp);
		}
	}
	
	public void Player_Sprites_Initialize(String s1,String s2){
		Sprites.add(s1);
		Sprites.add(s2);
	}
}
