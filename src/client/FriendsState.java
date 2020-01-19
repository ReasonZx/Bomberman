package client;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class FriendsState extends BasicGameState{
	
	private GUI_setup sbg;
	private TextField NewFriend;
	private ArrayList<String> FriendList;
	private ArrayList<String> FriendListState;
	private Shape T1,T2;
	private Shape R1,R2;
	private int MaxPage,CurrentPage;
	private Font myFont;
	private String Accept_Button;
	private String Reject_Button;
	private String Remove_Button;
	private String Invite_Button;
	private Image Add_New_Button, Add_hover;
	private Image Back_Button, Back_hover;
	private Image Back_Button_2;
	private Image PopUp_Button;
	private String OK_Button="sprites/ok.png";
	private String Cancel_Button="sprites/back.png";
	private int Back_ButtonX,Back_ButtonY;
	private int Back_Button2X,Back_Button2Y;
	private int Add_New_ButtonX,Add_New_ButtonY;
	private int PopUp_ButtonX,PopUp_ButtonY;
	private ArrayList<String> Buttons;
	private boolean Adding=false;
	private String Error_Message=" ";
	private String Message=" ";
	private boolean add_h = false, hovering_a = false;
	private boolean back_h = false, hovering_b = false;
	private boolean init=true;
	private boolean creating_list=false;
	private boolean block=false;
	private boolean waiting_for_game=false;
	private String server_response;
	private File click_file = new File("music/click.wav");
	private File hover_file = new File("music/hover.wav");
	private Image background;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		sbg=(GUI_setup) arg1;
		sbg.Set_Friends_State(getID());
		myFont=arg0.getDefaultFont();
		T1 = new Polygon(GetTrianglePoints(arg0,0));
		T2 = new Polygon(GetTrianglePoints(arg0,1));
		R1 = new Rectangle(20,20,arg0.getWidth()-40,arg0.getHeight()-40);
		R2 = new Rectangle(0, arg0.getHeight()/3f, arg0.getWidth(), arg0.getHeight()/3f);
		
		Add_New_Button=new Image("sprites/add.png");
		Add_New_Button=Add_New_Button.getScaledCopy(0.3f);
		Add_hover=new Image("sprites/add_hover.png");
		Add_hover=Add_hover.getScaledCopy(0.3f);
		Add_New_ButtonX=(int) (arg0.getWidth()/3f*2-Add_New_Button.getWidth()/2f);
		Add_New_ButtonY=arg0.getHeight()-Add_New_Button.getHeight();
		
		Back_Button = new Image("sprites/back.png");
		Back_Button = Back_Button.getScaledCopy(0.3f);
		Back_hover = new Image("sprites/back_hover.png");
		Back_hover = Back_hover.getScaledCopy(0.3f);
		Back_ButtonX=(int) (arg0.getWidth()/3f-Add_New_Button.getWidth()/2f);
		Back_ButtonY=arg0.getHeight()-Add_New_Button.getHeight();

		Back_Button_2 = Back_Button.getScaledCopy(0.5f);
		Back_Button2X=50;
		Back_Button2Y=(int) (R2.getY()+R2.getHeight()-20-Back_Button_2.getHeight()/2f);
		
		Accept_Button="sprites/accept.png";
		
		Reject_Button= "sprites/decline.png";

		Remove_Button= "sprites/remove.png";
		
		Invite_Button= "sprites/invite.png";
		
		background=new Image("sprites/background.png");
		
		PopUp_Button = new Image("sprites/ok.png");
		PopUp_Button=PopUp_Button.getScaledCopy(0.3f);
		PopUp_ButtonX=(int) (R2.getX()+R2.getWidth()/2f-PopUp_Button.getWidth()/2f);
		PopUp_ButtonY=(int) (((R2.getY()+R2.getHeight())-(R2.getHeight()-(R2.getHeight()/3f + myFont.getHeight("LOOKING FOR A GAME")/2f))/2f)-PopUp_Button.getHeight()/2f);
		
		NewFriend = new TextField(arg0, myFont,(int)(arg0.getWidth()/4f),(int)(arg0.getHeight()/2f),(int)(arg0.getWidth()/4f)*2,20);
		NewFriend.setBackgroundColor(Color.white);
		NewFriend.setBorderColor(Color.white);
		NewFriend.setTextColor(Color.black);
		NewFriend.setAcceptingInput(false);
	}

	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		FriendList = new ArrayList<String>();
		FriendListState = new ArrayList<String>();
		
		arg0.getInput().clearMousePressedRecord();
	
		CurrentPage=1;
		
		NewFriend.setAcceptingInput(false);
		init=true;
		creating_list=false;
		Error_Message="";
		Message="";
		block=false;
		waiting_for_game=false;
	}
	

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		int posX = arg0.getInput().getMouseX();
		int posY = arg0.getInput().getMouseY();
		
		if(init) {
			Server_Request_FriendList();
			MaxPage=(int) Math.ceil((double)FriendList.size()/10);
			if(MaxPage == 0)
				MaxPage = 1;
			Generate_Buttons();
		}
		else{
			if(!Adding) {
				if(!block) {
					int arrow=Arrow_Button_Pressed(posX,posY,arg0);
					if(arrow!=0) {
						if(arrow==1 && CurrentPage>1)
							CurrentPage--;
						else if(arrow==2 && CurrentPage<MaxPage)
							CurrentPage++;
					}
					Action_Button_Pressed(posX,posY,arg0);
					Add_New_Button_Pressed(posX,posY,arg0,1);	
					Back_Button_Pressed(posX,posY,arg0,1);
				}
				else {
					try {
						server_response=sbg.server.poll();
						PopUp_Button_Pressed(posX,posY,arg0);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(server_response!=null){
						System.out.println(server_response);
						if(waiting_for_game) {
							if(server_response.equals("friends_invite_NotAvailable")){
								waiting_for_game=false;
								PopUp_Message("PLAYER IS UNREACHABLE",OK_Button);
							}
							else if(server_response.equals("friends_invite_decline")){
								waiting_for_game=false;
								PopUp_Message("PLAYER DECLINED",OK_Button);
							}
							else if(server_response.equals("friends_invite_accept")){
								sbg.enterState(sbg.Get_OnlineGame_State());
							}
						}
					}
				}
			}
			else {
				Add_New_Button_Pressed(posX,posY,arg0,2);
				Back_Button_Pressed(posX,posY,arg0,2);
			}
		}
		
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics arg2) throws SlickException {
		
		if(init) {
			
		}
		else {
			arg2.setColor(Color.white);
			arg2.texture(R1, background, true);
			arg2.fill(T1);
			arg2.fill(T2);
			
			myFont.drawString(20+R1.getWidth()/2f - myFont.getWidth(Integer.toString(CurrentPage) + " / " + Integer.toString(MaxPage))/2f, gc.getHeight()-80,
					"" + Integer.toString(CurrentPage) + " / " + Integer.toString(MaxPage) + "");
			myFont.drawString(20+R1.getWidth()/7f - myFont.getWidth("Username")/2f, 20+20,
					"Username");
			
			RenderFriendList();
			RenderButtons();
			
			if(add_h == false) {
				Add_New_Button.draw(Add_New_ButtonX, Add_New_ButtonY);
			}
			else Add_hover.draw(Add_New_ButtonX, Add_New_ButtonY);
			
			if(back_h == false) {
				Back_Button.draw(Back_ButtonX, Back_ButtonY);
			}
			else Back_hover.draw(Back_ButtonX, Back_ButtonY);

			
			if(block) {
				arg2.setColor(Color.black);
				arg2.fill(R2);
				arg2.setColor(Color.white);
				myFont.drawString(gc.getWidth()/2f - myFont.getWidth(Error_Message)/2f,R2.getY()+R2.getHeight()/2f-myFont.getHeight(Error_Message),Error_Message,Color.red);
				PopUp_Button.draw(PopUp_ButtonX,PopUp_ButtonY);
			}
			
			if(Adding) {
				arg2.setColor(Color.black);
				arg2.fill(R2);
				arg2.setColor(Color.white);
				myFont.drawString(gc.getWidth()/2f - myFont.getWidth("Type the name of the player you wish to add and press the button!")/2f,
		                    R2.getY()+20, "Type the name of the player you wish to add and press the button!");
				NewFriend.setAcceptingInput(true);
				NewFriend.render(gc, arg2);
				myFont.drawString(gc.getWidth()/2f - myFont.getWidth(Message)/2f, 
						(gc.getHeight()/2f+NewFriend.getHeight()+R2.getY()+R2.getHeight()-Add_New_Button.getHeight())/2f-myFont.getHeight(Message)/2f, 
						Message,Color.red);
				Add_New_Button.draw(gc.getWidth()/2f-Add_New_Button.getWidth()/2f,R2.getY()+R2.getHeight()-Add_New_Button.getHeight());
				Back_Button_2.draw(Back_Button2X,Back_Button2Y);
			}
		}
	}
	
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		NewFriend.setAcceptingInput(false);
		FriendList.removeAll(FriendList);
		FriendListState.removeAll(FriendListState);
	}

	@Override
	public int getID() {
		return 8;
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
				String tmp[]=FriendList.get(i).split("_");
				myFont.drawString(20+R1.getWidth()/7f - myFont.getWidth(tmp[0])/2f, 80+(R1.getHeight()-T2.getHeight()-60)/10f*(i-(CurrentPage-1)*10),
						tmp[0]);
			}
		}
		else
			for(int i=(CurrentPage-1)*10;i<(CurrentPage-1)*10+10;i++) {
				String tmp[]=FriendList.get(i).split("_");
				myFont.drawString(20+R1.getWidth()/7f - myFont.getWidth(tmp[0])/2f, 80+(R1.getHeight()-T2.getHeight()-60)/10f*(i-(CurrentPage-1)*10),
						tmp[0]);
			}
	}
	
	private void PopUp_Button_Pressed(int posX,int posY,GameContainer arg0) throws IOException {
		if((posX>PopUp_ButtonX && posX < PopUp_ButtonX+ PopUp_Button.getWidth()) && (posY >PopUp_ButtonY  && posY < PopUp_ButtonY+ PopUp_Button.getHeight())){ 	
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
				block=false;
				if(waiting_for_game) {
					waiting_for_game=false;
					server_response=sbg.server.request("friends_invite_cancel");
				}
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
	
	private void RenderButtons() throws SlickException {
		if(CurrentPage==MaxPage) {
			
			for(int i=(CurrentPage-1)*10;i<(CurrentPage-1)*10+FriendList.size()%10;i++) {
				Image tmp = new Image(Buttons.get(i*2));
				tmp=tmp.getScaledCopy(0.18f);
				Image tmp2= new Image(Buttons.get(i*2+1));
				tmp2=tmp2.getScaledCopy(0.18f);
				tmp.draw(20+R1.getWidth()/3f, 80+(R1.getHeight()-T2.getHeight()-60)/10f*(i-(CurrentPage-1)*10)+myFont.getHeight("Users")/2f-tmp.getHeight()/2f);	
				tmp2.draw(20+R1.getWidth()/3f*2, 80+(R1.getHeight()-T2.getHeight()-60)/10f*(i-(CurrentPage-1)*10)+myFont.getHeight("Users")/2f-tmp2.getHeight()/2f);
			}
		}
		else
			for(int i=(CurrentPage-1)*10;i<(CurrentPage-1)*10+10;i++) {
				Image tmp = new Image(Buttons.get(i*2));
				tmp=tmp.getScaledCopy(0.18f);
				Image tmp2= new Image(Buttons.get(i*2+1));
				tmp2=tmp2.getScaledCopy(0.18f);
				tmp.draw(20+R1.getWidth()/3f, 80+(R1.getHeight()-T2.getHeight()-60)/10f*(i-(CurrentPage-1)*10)+myFont.getHeight("Users")/2f-tmp.getHeight()/2f);
				tmp2.draw(20+R1.getWidth()/3f*2, 80+(R1.getHeight()-T2.getHeight()-60)/10f*(i-(CurrentPage-1)*10)+myFont.getHeight("Users")/2f-tmp2.getHeight()/2f);
			}
	}
	
	private void Action_Button_Pressed(int posX,int posY,GameContainer arg0) throws SlickException {
		int tpY,tpX,tpY2;
		Image tmp = new Image(Accept_Button);
		tmp=tmp.getScaledCopy(0.18f);
		tpY=(int) (80+myFont.getHeight("Users")/2f-tmp.getHeight()/2f);
		tpY2=(int) ((R1.getHeight()-T2.getHeight()-60)/10f);
		tpX=(int) (20+R1.getWidth()/3f);
		
		if(CurrentPage==MaxPage) {
			for(int i=(CurrentPage-1)*10;i<(CurrentPage-1)*10+FriendList.size()%10;i++) {
				if((posX>tpX && posX < tpX+ tmp.getWidth()) 
					&& (posY >tpY+tpY2*(i-(CurrentPage-1)*10)  && posY < tpY +tpY2*(i-(CurrentPage-1)*10)+tmp.getHeight())){ 	
						if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) { 
							Button_Was_Pressed(i,1);
							return;
						}
				}
				else if((posX>(tpX-20)*2+20 && posX < (tpX-20)*2+20+ tmp.getWidth()) 
					&& (posY >tpY+tpY2*(i-(CurrentPage-1)*10)  && posY < tpY +tpY2*(i-(CurrentPage-1)*10)+tmp.getHeight())){	
						if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
							Button_Was_Pressed(i,2);
							return;
						}
				}
			}
		}
		else
			for(int i=(CurrentPage-1)*10;i<(CurrentPage-1)*10+10;i++) {
				if((posX>tpX && posX < tpX+ tmp.getWidth()) 
						&& (posY >tpY+tpY2*(i-(CurrentPage-1)*10)  && posY < tpY +tpY2*(i-(CurrentPage-1)*10)+tmp.getHeight())){ 	
							if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) { 
								Button_Was_Pressed(i,1);
								return;
							}
					}
					else if((posX>(tpX-20)*2+20 && posX < (tpX-20)*2 + 20 + tmp.getWidth()) 
						&& (posY >tpY+tpY2*(i-(CurrentPage-1)*10)  && posY < tpY +tpY2*(i-(CurrentPage-1)*10)+tmp.getHeight())){	
							if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
								Button_Was_Pressed(i,2);
								return;
							}
					}
				}
	}
	
	private void Back_Button_Pressed(int posX,int posY,GameContainer arg0,int butt) {
		if(butt==1) {
			if((posX>Back_ButtonX && posX < Back_ButtonX + Back_Button.getWidth()) && (posY >Back_ButtonY  && posY < Back_ButtonY+ Back_Button.getHeight())){ 	
				back_h = true;
				if(hovering_b == false) {
					play_hover_sound();
					hovering_b = true;
				}
				if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					play_click_sound();
					sbg.enterState(sbg.Get_MainMenu_State());
				}
			}
			else {
				back_h = false;
				hovering_b = false;
			}
		}
		else
			if((posX>Back_Button2X && posX < Back_Button2X + Back_Button_2.getWidth()) && (posY > Back_Button2Y  && posY < Back_Button2Y+ Back_Button_2.getHeight())){ 	
				if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
					Adding=false;
				}
	}
	
	private void Add_New_Button_Pressed(int posX,int posY,GameContainer gc,int state) {
		if(state==1) {
			if((posX>Add_New_ButtonX && posX < Add_New_ButtonX + Add_New_Button.getWidth()) && (posY >Add_New_ButtonY  && posY < Add_New_ButtonY+ Add_New_Button.getHeight())){ 	
				add_h = true;
				if(hovering_a == false) {
					play_hover_sound();
					hovering_a = true;
				}
				if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					play_click_sound();
					Adding=true;
					Message="";
				}
			}
			else {
				add_h = false;
				hovering_a = false;
			}
		}
		else {
			if((posX>gc.getWidth()/2f-Add_New_Button.getWidth()/2f && posX < gc.getWidth()/2f-Add_New_Button.getWidth()/2f + Add_New_Button.getWidth()) && (posY > R2.getY()+R2.getHeight()-Add_New_Button.getHeight()  && posY < R2.getY()+R2.getHeight())){ 	
				if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) { 
					//TODO
					//GET TEXT FIELD VALUE AND SEND TO SERVER
					Server_Send_New_Friend_Request(NewFriend.getText());
					System.out.println(server_response);
					if(server_response.equals("friends_add_OK")) {
						Message="REQUEST SENT";
					}
					else{
						//ERROR MESSAGES GO HERE
						Message=server_response;
					}
				}
			}
			if(gc.getInput().isKeyPressed(1)) {
				Adding=false;
			}
		}
				
	}
	
	private boolean IsFriend(int pos) {
		return !(FriendListState.get(pos).equals("0"));
	}
	
	private void Server_Request_FriendList() {
		try {
			if(!creating_list) 
				server_response=sbg.server.request("friends_request");
			else	
				server_response=sbg.server.poll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		if(server_response!=null){
			String tmp[]=server_response.split("_");
		
			if(server_response.equals("friends_request_start")){
				creating_list=true;
			}
			else if(server_response.equals("friends_request_stop")) {
				creating_list=false;
				init=false;
			}
			else if(tmp[0].equals("friends") && tmp[1].equals("info")) {
				String info = new String("");
				for(int i=2;i<tmp.length;i++)
					info=info.concat(tmp[i]);
				String tmp2[]=info.split("FRIENDSTATE420=");
				if(tmp2.length==2) {
					String username[]=tmp2[0].split("USER=");
					FriendList.add(username[1]);
					FriendListState.add(tmp2[1]);
					System.out.println(username[1]+"_"+tmp2[1]);
				}
			}
		else{
			Error_Message=server_response;
		}
		}
	}
	
	private void Button_Was_Pressed(int Friend,int butt) {
		try {
		if(butt==1) {
			if(IsFriend(Friend)){
				//TODO
				//INVITE BUTTON WAS CLICKED
				server_response=sbg.server.request("friends_invite_"+FriendList.get(Friend));
				if(server_response!=null)
					if(server_response.equals("friends_invite_online")){
						PopUp_Message("WAITING FOR PLAYER "+FriendList.get(Friend),Cancel_Button);
						waiting_for_game=true;
					}
					else
						PopUp_Message("ERROR FINDING PLAYER",OK_Button);
					return;
			}
			else {
				//TODO
				//ACCEPT BUTTON WAS CLICKED
				server_response=sbg.server.request("friends_accept_"+FriendList.get(Friend));
				if(server_response.equals("friends_accept_OK")){
					FriendListState.set(Friend,"1");
					Update_Buttons(Friend);
					PopUp_Message("ADDED TO FRIENDS",OK_Button);
				}
				else {
					PopUp_Message("REQUEST COULDN'T BE ACCEPTED",OK_Button);
				}
				return;
			}
		}
		else{
			if(IsFriend(Friend)){
				//TODO
				//REMOVE BUTTON WAS CLICKED
				server_response=sbg.server.request("friends_remove_"+FriendList.get(Friend));
				if(server_response.equals("friends_remove_OK")){
					FriendList.remove(Friend);
					FriendListState.remove(Friend);
					Buttons.remove(Friend*2);
					Buttons.remove(Friend*2);
					PopUp_Message("REMOVED FROM FRIENDS",OK_Button);
				}
				else {
					PopUp_Message("ERROR REMOVING FRIEND",OK_Button);
				}
				return;
			}
			else{
				//TODO
				//REJECT BUTTON WAS CLICKED
				server_response=sbg.server.request("friends_reject_"+FriendList.get(Friend));
				if(server_response.equals("friends_reject_OK")){
					FriendList.remove(Friend);
					FriendListState.remove(Friend);
					Buttons.remove(Friend*2);
					Buttons.remove(Friend*2);
					PopUp_Message("REJECTED FRIEND REQUEST",OK_Button);
				}
				else {
					PopUp_Message("ERROR REJECTING REQUEST",OK_Button);
				}
				return;
			}
		}
		} catch (IOException | SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		}
	}
	
	private void Generate_Buttons() throws SlickException {
		Buttons = new ArrayList<String>();
		
		for(int i=0;i<FriendList.size();i++) {
			if(IsFriend(i)) {
				Buttons.add(Invite_Button);
				Buttons.add(Remove_Button);
			}
			else {
				Buttons.add(Accept_Button);
				Buttons.add(Reject_Button);
			}
		}
	}
	
	private void Update_Buttons(int Friend) throws SlickException{
		
		if(IsFriend(Friend)){
			Buttons.set(Friend*2,Invite_Button);
			Buttons.set(Friend*2+1,Remove_Button);
		}
		else {
			Buttons.set(Friend*2,Accept_Button);
			Buttons.set(Friend*2+1,Reject_Button);
		}
	}
	
	private void Server_Send_New_Friend_Request(String s){
		try {
			server_response=sbg.server.request("friends_add_"+s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void PopUp_Message(String s,String button) throws SlickException{
		Image tmp= new Image(button);
		PopUp_Button= tmp.getScaledCopy(0.3f);
		block=true;
		Error_Message=s;
	}
	
	public void play_hover_sound() {
		
		AudioInputStream hover_sound;
	
		try {
			hover_sound = AudioSystem.getAudioInputStream(hover_file);
			Clip hover_s = AudioSystem.getClip(null);
			hover_s.open(hover_sound);
			hover_s.loop(0);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	public void play_click_sound() {
		
		AudioInputStream click_sound;
		
		try {
			click_sound = AudioSystem.getAudioInputStream(click_file);
			Clip click_s = AudioSystem.getClip(null);
			click_s.open(click_sound);
			click_s.loop(0);
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
