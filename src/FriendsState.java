import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class FriendsState extends BasicGameState{
	
	private GUI_setup sbg;
	private ArrayList<String> FriendList;
	private Shape T1,T2;
	private Shape R1;
	private int MaxPage,CurrentPage;
	private Font myFont;
	Image Accept;
	Image Reject;
	Image Remove;
	Image Invite;
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		sbg=(GUI_setup) arg1;
		sbg.Set_Friends_State(getID());
		myFont=arg0.getDefaultFont();
		T1 = new Polygon(GetTrianglePoints(0));
		T2 = new Polygon(GetTrianglePoints(1));
		R1 = new Rectangle(20,20,800-40,600-40);
	}

	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		FriendList = new ArrayList<String>();
		Hardcode_Friends();
		arg0.getInput().clearMousePressedRecord();
		MaxPage=(int) Math.ceil((double)FriendList.size()/10);
		CurrentPage=1;
	}
	
	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		// TODO Auto-generated method stub
		arg2.setColor(Color.white);
		arg2.fill(R1);
		arg2.setColor(Color.black);
		arg2.fill(T1);
		arg2.fill(T2);
		arg2.setColor(Color.white);
		myFont.drawString(20+R1.getWidth()/2f - myFont.getWidth(Integer.toString(CurrentPage) + " / " + Integer.toString(MaxPage))/2f, 520,
				"" + Integer.toString(CurrentPage) + " / " + Integer.toString(MaxPage) + "",Color.black);
		myFont.drawString(20+R1.getWidth()/7f - myFont.getWidth("Username")/2f, 20+20,
				"Username",Color.black);
		RenderFriendList();
		
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub
		int posX = arg0.getInput().getMouseX();
		int posY = arg0.getInput().getMouseY();
		
		int arrow=Arrow_Button_Pressed(posX,posY,arg0);
		if(arrow!=0) {
			if(arrow==1 && CurrentPage>1)
				CurrentPage--;
			else if(arrow==2 && CurrentPage<MaxPage)
				CurrentPage++;
		}
	}
	
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 7;
	}
	
	private void Hardcode_Friends() {
		FriendList.add(new String("SlimShady"));
		FriendList.add(new String("DonaldDuck"));
		FriendList.add(new String("RemoveMePls"));
		FriendList.add(new String("RemoveMePls"));
		FriendList.add(new String("RemoveMePls"));
		FriendList.add(new String("RemoveMePls"));
		FriendList.add(new String("RemoveMePls"));
		FriendList.add(new String("RemoveMePls"));
		FriendList.add(new String("RemoveMePls"));
		FriendList.add(new String("RemoveMePls"));
		FriendList.add(new String("RemoveMePls"));
		FriendList.add(new String("RemoveMePls"));
	}
	
	private float[] GetTrianglePoints(int direction){
		float tmp[] = new float[6];
			if(direction==0) {
				tmp[0]=70;
				tmp[1]=510;
				tmp[2]=70;
				tmp[3]=550;
				tmp[4]=40;
				tmp[5]=530;
			}
			else {
				tmp[0]=730;
				tmp[1]=510;
				tmp[2]=730;
				tmp[3]=550;
				tmp[4]=760;
				tmp[5]=530;
			}
		
		return tmp;
	}
	
	private void RenderFriendList() {
		if(CurrentPage==MaxPage) {
			for(int i=(CurrentPage-1)*10;i<(CurrentPage-1)*10+FriendList.size()%10;i++) {
				myFont.drawString(20+R1.getWidth()/7f - myFont.getWidth(FriendList.get(i))/2f, 80+(R1.getHeight()-T2.getHeight()-40)/11f*(i-(CurrentPage-1)*10),
						FriendList.get(i),Color.black);
			}
		}
		else
			for(int i=(CurrentPage-1)*10;i<(CurrentPage-1)*10+10;i++) {
				myFont.drawString(20+R1.getWidth()/7f - myFont.getWidth(FriendList.get(i))/2f, 80+(R1.getHeight()-T2.getHeight()-40)/11f*(i-(CurrentPage-1)*10),
						FriendList.get(i),Color.black);
			}
	}
	
	private int Arrow_Button_Pressed(int posX,int posY,GameContainer arg0) {
		if((posX>T1.getX() && posX < T1.getX()+ T1.getWidth()) && (posY >T1.getY()  && posY < T1.getY()+ T1.getHeight())){ 	
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
				return 1;
			}
		else if((posX>T2.getX() && posX < T2.getX()+ T2.getWidth()) && (posY >T2.getY()  && posY < T2.getY()+ T2.getHeight())) {	
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
				return 2;
		}
		
		return 0;
	}
}
