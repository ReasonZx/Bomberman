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
	private Shape R1,R2;
	private int MaxPage,CurrentPage;
	private Font myFont;
	private Image Accept_Button;
	private Image Reject_Button;
	private Image Remove_Button;
	private Image Invite_Button;
	private Image Add_New_Button;
	private Image Back_Button;
	private int Back_ButtonX,Back_ButtonY;
	private int Add_New_ButtonX,Add_New_ButtonY;
	private ArrayList<Image> Buttons;
	private boolean Adding=false;
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		sbg=(GUI_setup) arg1;
		sbg.Set_Friends_State(getID());
		myFont=arg0.getDefaultFont();
		T1 = new Polygon(GetTrianglePoints(arg0,0));
		T2 = new Polygon(GetTrianglePoints(arg0,1));
		R1 = new Rectangle(20,20,arg0.getWidth()-40,arg0.getHeight()-40);
		R2 = new Rectangle(0, arg0.getHeight()/3f, arg0.getWidth(), arg0.getHeight()/3f);
		Add_New_Button=new Image("sprites/Reset_Button.png");
		Add_New_Button=Add_New_Button.getScaledCopy(0.3f);
		Add_New_ButtonX=(int) (arg0.getWidth()/3f*2-Add_New_Button.getWidth()/2f);
		Add_New_ButtonY=arg0.getHeight()-Add_New_Button.getHeight();
		Back_Button = new Image("sprites/back.png");
		Back_Button = Back_Button.getScaledCopy(0.3f);
		Back_ButtonX=(int) (arg0.getWidth()/3f-Add_New_Button.getWidth()/2f);
		Back_ButtonY=arg0.getHeight()-Add_New_Button.getHeight();
	}

	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		FriendList = new ArrayList<String>();
		Hardcode_Friends();
		Server_Request_FriendList();
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
		myFont.drawString(20+R1.getWidth()/2f - myFont.getWidth(Integer.toString(CurrentPage) + " / " + Integer.toString(MaxPage))/2f, arg0.getHeight()-80,
				"" + Integer.toString(CurrentPage) + " / " + Integer.toString(MaxPage) + "",Color.black);
		myFont.drawString(20+R1.getWidth()/7f - myFont.getWidth("Username")/2f, 20+20,
				"Username",Color.black);
		RenderFriendList();
		RenderButtons();
		Add_New_Button.draw(Add_New_ButtonX, Add_New_ButtonY);
		Back_Button.draw(Back_ButtonX,Back_ButtonY);
		
		if(Adding) {
			arg2.setColor(Color.black);
			arg2.fill(R2);
			arg2.setColor(Color.white);
			myFont.drawString(arg0.getWidth()/2f - myFont.getWidth("Type the name of the player you wish to add and press the button!")/2f,
	                    R2.getY()+20, "Type the name of the player you wish to add and press the button!");
			//TODO
			//TEXTFIELD
			Add_New_Button.draw(arg0.getWidth()/2f-Add_New_Button.getWidth()/2f,R2.getY()+R2.getHeight()-Add_New_Button.getHeight());
		}
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub
		int posX = arg0.getInput().getMouseX();
		int posY = arg0.getInput().getMouseY();
		
		if(!Adding) {
			int arrow=Arrow_Button_Pressed(posX,posY,arg0);
			if(arrow!=0) {
				if(arrow==1 && CurrentPage>1)
					CurrentPage--;
				else if(arrow==2 && CurrentPage<MaxPage)
					CurrentPage++;
			}
			Action_Button_Pressed(posX,posY,arg0);
			Add_New_Button_Pressed(posX,posY,arg0,1);	
			Back_Button_Pressed(posX,posY,arg0);
		}
		else {
			Add_New_Button_Pressed(posX,posY,arg0,2);
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
	
	private float[] GetTrianglePoints(GameContainer arg0,int direction){
		float tmp[] = new float[6];
			if(direction==0) {
				tmp[0]=70;
				tmp[1]=arg0.getHeight()-80;
				tmp[2]=70;
				tmp[3]=arg0.getHeight()-30;
				tmp[4]=40;
				tmp[5]=arg0.getHeight()-55;
			}
			else {
				tmp[0]=arg0.getWidth()-70;
				tmp[1]=arg0.getHeight()-80;
				tmp[2]=arg0.getWidth()-70;
				tmp[3]=arg0.getHeight()-30;
				tmp[4]=arg0.getWidth()-40;
				tmp[5]=arg0.getHeight()-55;
			}
		
		return tmp;
	}
	
	private void RenderFriendList() {
		if(CurrentPage==MaxPage) {
			for(int i=(CurrentPage-1)*10;i<(CurrentPage-1)*10+FriendList.size()%10;i++) {
				myFont.drawString(20+R1.getWidth()/7f - myFont.getWidth(FriendList.get(i))/2f, 80+(R1.getHeight()-T2.getHeight()-40)/10f*(i-(CurrentPage-1)*10),
						FriendList.get(i),Color.black);
			}
		}
		else
			for(int i=(CurrentPage-1)*10;i<(CurrentPage-1)*10+10;i++) {
				myFont.drawString(20+R1.getWidth()/7f - myFont.getWidth(FriendList.get(i))/2f, 80+(R1.getHeight()-T2.getHeight()-60)/10f*(i-(CurrentPage-1)*10),
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
	
	private void RenderButtons() {
		if(CurrentPage==MaxPage) {
			for(int i=(CurrentPage-1)*10;i<(CurrentPage-1)*10+FriendList.size()%10;i++) {
				if(IsFriend(FriendList.get(i))){
					//RENDER INVITE AND REMOVE BUTTON
					//TODO
					}
				else {
					//RENDER ACCEPT AND REJECT BUTTON
					//TODO
				}
					
			}
		}
		else
			for(int i=(CurrentPage-1)*10;i<(CurrentPage-1)*10+10;i++) {
				
			}
	}
	
	private void Action_Button_Pressed(int posX,int posY,GameContainer arg0) {
		if(CurrentPage==MaxPage) {
			for(int i=(CurrentPage-1)*10;i<(CurrentPage-1)*10+FriendList.size()%10;i++) {
				if((posX>T1.getX() && posX < T1.getX()+ T1.getWidth()) && (posY >T1.getY()  && posY < T1.getY()+ T1.getHeight())){ 	
					if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
						//TODO
						//BUTTON 1 OF FRIEND i WAS PRESSED
						return;
					}
				else if((posX>T2.getX() && posX < T2.getX()+ T2.getWidth()) && (posY >T2.getY()  && posY < T2.getY()+ T2.getHeight())) {	
					if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
						//TODO
						//BUTTON 2 OF FRIEND i WAS PRESSED
						return;
				}
			}
		}
		else
			for(int i=(CurrentPage-1)*10;i<(CurrentPage-1)*10+10;i++) {
				if((posX>T1.getX() && posX < T1.getX()+ T1.getWidth()) && (posY >T1.getY()  && posY < T1.getY()+ T1.getHeight())){ 	
					if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
						//TODO
						//BUTTON 1 OF FRIEND i WAS PRESSED
						return;
					}
				else if((posX>T2.getX() && posX < T2.getX()+ T2.getWidth()) && (posY >T2.getY()  && posY < T2.getY()+ T2.getHeight())) {	
					if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
						//TODO
						//BUTTON 2 OF FRIEND i WAS PRESSED
						return;
				}
			}
	}
	
	private void Back_Button_Pressed(int posX,int posY,GameContainer arg0) {
		if((posX>Back_ButtonX && posX < Back_ButtonX + Back_Button.getWidth()) && (posY >Back_ButtonY  && posY < Back_ButtonY+ Back_Button.getHeight())){ 	
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
				sbg.enterState(sbg.Get_MainMenu_State());
			}
	}
	
	private void Add_New_Button_Pressed(int posX,int posY,GameContainer arg0,int state) {
		if(state==1) {
			if((posX>Add_New_ButtonX && posX < Add_New_ButtonX + Add_New_Button.getWidth()) && (posY >Add_New_ButtonY  && posY < Add_New_ButtonY+ Add_New_Button.getHeight())){ 	
				if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
					Adding=true;
				}
		}
		else {
			if((posX>arg0.getWidth()/2f-Add_New_Button.getWidth()/2f && posX < arg0.getWidth()/2f-Add_New_Button.getWidth()/2f + Add_New_Button.getWidth()) && (posY > R2.getY()+R2.getHeight()-Add_New_Button.getHeight()  && posY < R2.getY()+R2.getHeight())){ 	
				if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) { 
					//TODO
					//GET TEXT FIELD VALUE AND SEND TO SERVER
					System.out.println("ADDING FRIEND");
					if(Server_Send_New_Friend_Request("")==0) {
						Adding=false;
					}
					else{
						//ERROR MESSAGES GO HERE
					}
				}
			}
		}
				
	}
	
	private boolean IsFriend(String s) {
		//VERIFY IF FRIEND WITH NAME s IS ALREADY A FRIEND OR IF A REQUEST WAS SENT
		//TODO
		return false;
	}
	
	private void Server_Request_FriendList() {
		//REQUESTS FRIENDLIST TO SERVER AND SAVE IT ON VARIABLE FriendList (might need to change type of arraylist)
		//TODO
	}
	
	private int Server_Send_New_Friend_Request(String s){
		//TODO
		//REQUESTS SERVER TO SEND A NEW FRIEND REQUEST, RETURNS 0 IF SUCCESS, NEGATIVE IF ERROR
		return 0;
	}
}
