package client;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class ControlsState extends BasicGameState{

	protected GUI_setup sbg;
	private Image Configuration;
	private Image Reset_Button, Reset_hover;
	private Image Back, Back_hover;
	private Image Player1,Player2;
	private Image high_volume, medium_volume, low_volume, no_volume;
	private Shape R1,R2,R3,R4;
	private ArrayList <Shape> T;
	private Shape ST1,ST2;
	private Shape CheckBox1,CheckBox2,CheckBox3;
	private Shape Selected;
	
	private Menu Sound;
	
	private int ConfigX1;
	private int ConfigX2;
	private int ConfigY;
	private int volume;
	protected int butt;
	protected boolean Configurating=false;
	private boolean clicked = false;
	private KeyPressChange KeyInput;
	private int ControlsBoxLenght;
	private int ControlsBoxHeight;
	private Font myFont;
	private int ResetX,ResetY;
	private int BackX,BackY;
	private boolean reset_h = false , back_h = false;
	private boolean hovering_r = false , hovering_b = false;
	private int[][] Settings;
	private Image background,background2;
	private File click_file = new File("music/click.wav");
	private File hover_file = new File("music/hover.wav");
	private int resolution;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		myFont=arg0.getDefaultFont();
		
		KeyInput = new KeyPressChange(arg0);
		sbg=(GUI_setup) arg1;
		sbg.Set_Controls_State(getID());
		
		background2 = new Image("sprites/background.png");
		background2=background2.getScaledCopy(arg0.getWidth(),arg0.getHeight());
		
		Configuration= new Image("sprites/Configuration_Wheel.png");
		Configuration=Configuration.getScaledCopy((float)0.05);
		
		high_volume= new Image("sprites/high_volume.png");
		high_volume=high_volume.getScaledCopy((float)0.25);
		
		medium_volume= new Image("sprites/medium_volume.png");
		medium_volume=medium_volume.getScaledCopy((float)0.25);
		
		low_volume= new Image("sprites/low_volume.png");
		low_volume=low_volume.getScaledCopy((float)0.25);
		
		no_volume= new Image("sprites/mute_volume.png");
		no_volume=no_volume.getScaledCopy((float)0.25);
			
		
		Reset_Button = new Image("sprites/reset.png");
		Reset_Button = Reset_Button.getScaledCopy(0.2f);
		Reset_hover = new Image("sprites/reset_hover.png");
		Reset_hover = Reset_hover.getScaledCopy(0.2f);
		
		Back = new Image("sprites/back.png");
		Back = Back.getScaledCopy(0.2f);
		Back_hover = new Image("sprites/back_hover.png");
		Back_hover = Back_hover.getScaledCopy(0.2f);
		
		BackX = (int) (sbg.Get_Display_width()/12f);
		BackY = (int) (sbg.Get_Display_height()-sbg.Get_Display_height()/20f-Back.getHeight()/2f); 
		ResetX = (int) (sbg.Get_Display_width()*11/12f-Reset_Button.getWidth());
		ResetY = (int) (sbg.Get_Display_height()-sbg.Get_Display_height()/20f-Reset_Button.getHeight()/2f);
		
		ControlsBoxLenght=(int) (sbg.Get_Display_width()*3/12f);
		ControlsBoxHeight=(int) (sbg.Get_Display_height()*8/10f);
		
		R1 = new Rectangle(sbg.Get_Display_width()/24f, sbg.Get_Display_height()/10f, ControlsBoxLenght, ControlsBoxHeight); 
		R2 = new Rectangle(sbg.Get_Display_width()*9/24f, sbg.Get_Display_height()/10f, ControlsBoxLenght, ControlsBoxHeight);
		R3 = new Rectangle(sbg.Get_Display_width()*3/8f, sbg.Get_Display_height()*3/8f, sbg.Get_Display_width()/4f, sbg.Get_Display_height()/4f);
		R4 = new Rectangle(sbg.Get_Display_width()*17/24f, sbg.Get_Display_height()/10f, ControlsBoxLenght, ControlsBoxHeight);
		
		CheckBox1 = new Rectangle(R4.getMaxX()-R4.getWidth()/4f, R4.getY()+R4.getHeight()*2/8f,myFont.getHeight("A")*3/2,myFont.getHeight("A")*3/2);
		CheckBox2 = new Rectangle(R4.getMaxX()-R4.getWidth()/4f, R4.getY()+R4.getHeight()*3/8f,myFont.getHeight("A")*3/2,myFont.getHeight("A")*3/2);
		CheckBox3 = new Rectangle(R4.getMaxX()-R4.getWidth()/4f, R4.getY()+R4.getHeight()*4/8f,myFont.getHeight("A")*3/2,myFont.getHeight("A")*3/2);
		
		background = new Image("sprites/background.png");
		resolution=GUI_setup.Get_Resolution();
	}
	
	@Override
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		Sound=(Menu) arg1.getState(sbg.Get_Menu_State());
		arg0.getInput().clearMousePressedRecord();
		arg0.getInput().addKeyListener(KeyInput);
		KeyInput.setAcceptingInput(false);
		Settings=sbg.Get_Settings();
		Player1 = new Image("sprites/D_"   + Settings[0][0] + Settings[0][1] + ".png");
		Player1=Player1.getScaledCopy(3);
		Player2 = new Image("sprites/D_"   + Settings[1][0] + Settings[1][1] + ".png");
		Player2=Player2.getScaledCopy(3);
		T= new ArrayList<Shape>();
		Set_Triangles();
		volume=(int)Sound.Get_Volume();
		
		ConfigX1=(int) R1.getMaxX()-2*Configuration.getWidth();
		ConfigX2=(int) R2.getMaxX()-2*Configuration.getWidth();
		ConfigY= (int) (R1.getY()+ Player1.getHeight()*2f);
		
		if(resolution==1)
			Selected=new Rectangle(R4.getMaxX()-R4.getWidth()/4f+myFont.getHeight("A")/4f, R4.getY()+R4.getHeight()*2/8f+myFont.getHeight("A")/4f,myFont.getHeight("A"),myFont.getHeight("A"));
		else if(resolution==2)
			Selected=new Rectangle(R4.getMaxX()-R4.getWidth()/4f+myFont.getHeight("A")/4f, R4.getY()+R4.getHeight()*3/8f+myFont.getHeight("A")/4f,myFont.getHeight("A"),myFont.getHeight("A"));
		else
			Selected=new Rectangle(R4.getMaxX()-R4.getWidth()/4f+myFont.getHeight("A")/4f, R4.getY()+R4.getHeight()*4/8f+myFont.getHeight("A")/4f,myFont.getHeight("A"),myFont.getHeight("A"));
		
		Set_Volume_Arrows();
	}
	
	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub
		int posX = arg0.getInput().getMouseX();
		int posY = arg0.getInput().getMouseY();
		
		
		
		if(!Configurating()) {
			butt=Configuration_Button_Pressed(posX,posY,arg0);
			if(butt!=0) 
				Configurating=true;
			
			
			
			
			if(Reset_Button_Pressed(posX,posY,arg0)){
				sbg.Reset_Settings();
				Sound.set_volume(Sound.Get_Volume());
				volume=Sound.Get_Volume();
			}
			
			if(Back_Button_Pressed(posX,posY,arg0))
				sbg.enterState(sbg.Get_MainMenu_State());
			
			int arrow=Arrow_Button_Pressed(posX,posY,arg0);
			if(arrow!=0) {
				if(arrow%2==1)
					if(arrow==1 || arrow==5)
						sbg.Change_Settings_Color(arrow, 1, 0);
					else
						sbg.Change_Settings_Color(arrow, 1, 1);
				else
					if(arrow==2 || arrow==6)
						sbg.Change_Settings_Color(arrow, 2, 0);
					else
						sbg.Change_Settings_Color(arrow, 2, 1);
			}
			
			arrow=Sound_Arrow_Pressed(posX,posY,arg0);
			if(arrow!=0) {
				if(arrow==1) {
					if(volume>0){
						volume--;
						Sound.set_volume(volume);
					}
				}
				else {
					if(volume<3){
						volume++;
						Sound.set_volume(volume);
					}
				}
			}
			arrow=CheckBock_Pressed(posX,posY,arg0);
			if(arrow!=0) {
				if(arrow==1) {
					sbg.Set_Resolution(1);
					resolution=1;
					Selected.setLocation(R4.getMaxX()-R4.getWidth()/4f+myFont.getHeight("A")/4f, R4.getY()+R4.getHeight()*2/8f+myFont.getHeight("A")/4f);
				}
				else if(arrow==2){
					sbg.Set_Resolution(2);
					resolution=2;
					Selected.setLocation(R4.getMaxX()-R4.getWidth()/4f+myFont.getHeight("A")/4f, R4.getY()+R4.getHeight()*3/8f+myFont.getHeight("A")/4f);
				}
				else {
					sbg.Set_Resolution(3);
					resolution=3;
					Selected.setLocation(R4.getMaxX()-R4.getWidth()/4f+myFont.getHeight("A")/4f, R4.getY()+R4.getHeight()*4/8f+myFont.getHeight("A")/4f);
				}
			}
			
			Player1 = new Image("sprites/D_"   + Settings[0][0] + Settings[0][1] + ".png");
			Player1=Player1.getScaledCopy(3);
			Player2 = new Image("sprites/D_"   + Settings[1][0] + Settings[1][1] + ".png");
			Player2=Player2.getScaledCopy(3);
			
			
			
		}
		else
		{
			KeyInput.setAcceptingInput(true);
		}
				
	}
	
	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		// TODO Auto-generated method stub
		arg2.drawImage(background2, 0, 0);
		arg2.setColor(Color.white);
		arg2.texture(R1, background, true);
		arg2.texture(R2, background, true);
		arg2.texture(R4, background, true);
	
		for(int i=0;i<T.size();i++) {
			arg2.fill(T.get(i));
		}
		
		arg2.fill(ST1);
		arg2.fill(ST2);
		
		arg2.fill(CheckBox1);
		arg2.fill(CheckBox2);
		arg2.fill(CheckBox3);
		arg2.setColor(Color.black);
		
		arg2.fill(Selected);
		
		arg2.setColor(Color.white);
		
		myFont.drawString(R4.getX()+R4.getWidth()/8f, R4.getY()+R4.getHeight()/8f, "Resolution",Color.white);
		myFont.drawString(R4.getX()+R4.getWidth()*2/8f, R4.getY()+R4.getHeight()/8f+myFont.getHeight("Resolution")*2, "requires restart",Color.white);
		
		myFont.drawString(R4.getX()+R4.getWidth()/4f, R4.getY()+R4.getHeight()*2/8f, "1920x1080",Color.white);
		myFont.drawString(R4.getX()+R4.getWidth()/4f, R4.getY()+R4.getHeight()*3/8f, "1366x768",Color.white);
		myFont.drawString(R4.getX()+R4.getWidth()/4f, R4.getY()+R4.getHeight()*4/8f, "1024x768",Color.white);
		
		myFont.drawString(R4.getX()+R4.getWidth()/8f, R4.getY()+R4.getHeight()*5/8f, "Sound",Color.white);

		
		
		if(volume == 0) {
			arg2.drawImage(no_volume,R4.getX()+R4.getWidth()/2f-no_volume.getWidth()/2f, R4.getMaxY()-R4.getHeight()/6f-no_volume.getHeight()/2f);
		}
		else if(volume == 1) {
			arg2.drawImage(low_volume,R4.getX()+R4.getWidth()/2f-no_volume.getWidth()/2f, R4.getMaxY()-R4.getHeight()/6f-no_volume.getHeight()/2f);
		}
		else if(volume == 2) {
			arg2.drawImage(medium_volume,R4.getX()+R4.getWidth()/2f-no_volume.getWidth()/2f, R4.getMaxY()-R4.getHeight()/6f-no_volume.getHeight()/2f);
		}
		else if(volume == 3) {
			arg2.drawImage(high_volume,R4.getX()+R4.getWidth()/2f-no_volume.getWidth()/2f, R4.getMaxY()-R4.getHeight()/6f-no_volume.getHeight()/2f);
		}
		
		for(int i=0;i<5;i++) {
			myFont.drawString(R1.getX() + ControlsBoxLenght/2f - myFont.getWidth(Input.getKeyName(Settings[0][i+2]))/2f, ConfigY+myFont.getHeight(Input.getKeyName(Settings[0][i+2]))/2f+(i*((ControlsBoxHeight-ConfigY)/4)), Input.getKeyName(Settings[0][i+2]),Color.white);
			arg2.drawImage(Configuration,ConfigX1,ConfigY+(i*((ControlsBoxHeight-ConfigY)/4)));
		}
		
		for(int i=0;i<5;i++) {
			myFont.drawString(R2.getX() + ControlsBoxLenght/2f - myFont.getWidth(Input.getKeyName(Settings[1][i+2]))/2f, ConfigY+myFont.getHeight(Input.getKeyName(Settings[1][i+2]))/2f+(i*((ControlsBoxHeight-ConfigY)/4)), Input.getKeyName(Settings[1][i+2]),Color.white);
			arg2.drawImage(Configuration,ConfigX2,ConfigY+(i*((ControlsBoxHeight-ConfigY)/4)));
		}
		
		if(back_h == false) {
			Back.draw(BackX, BackY);
		}
		else Back_hover.draw(BackX, BackY);

		if(reset_h == false) {
			Reset_Button.draw(ResetX, ResetY);
		}
		else Reset_hover.draw(ResetX, ResetY);
		
		
		
		
		arg2.drawImage(Player1,R1.getX() + ControlsBoxLenght/2f - Player1.getWidth()/2f,R1.getY() + Player1.getHeight()/2f);
		arg2.drawImage(Player2,R2.getX() + ControlsBoxLenght/2f - Player2.getWidth()/2f,R2.getY() + Player2.getHeight()/2f);
		
		if(Configurating()) {
			arg2.fill(R3);
			myFont.drawString(arg0.getWidth()/2f - myFont.getWidth("Press a new key")/2f,
	                    R3.getY()+20-myFont.getHeight("Press a new key")/2f, "Press a new key",Color.black);
			
			myFont.drawString(arg0.getWidth()/2f - myFont.getWidth("Currently:")/2f,
                    R3.getCenterY()-myFont.getHeight("Currently:")/2f, "Currently:",Color.black);
			
			if(butt<6)
				myFont.drawString(arg0.getWidth()/2f - myFont.getWidth("< "+ Input.getKeyName(Settings[0][butt+1]) +" >")/2f,
						 R3.getY()+R3.getHeight()-20-myFont.getHeight("< "+ Input.getKeyName(Settings[0][butt+1]) +" >")/2f, 
						 "< "+ Input.getKeyName(Settings[0][butt+1]) +" >",Color.black);
			else
				myFont.drawString(arg0.getWidth()/2f - myFont.getWidth("< "+ Input.getKeyName(Settings[1][butt-6+2]) +" >")/2f,
						 R3.getY()+R3.getHeight()-20-myFont.getHeight("< "+ Input.getKeyName(Settings[1][butt-6+2]) +" >")/2f, 
						 "< "+ Input.getKeyName(Settings[1][butt-6+2]) +" >",Color.black);
		}
		
		myFont.drawString(R1.getCenterX()-myFont.getWidth("PLAYER 1")/2f,R1.getY()+5, "PLAYER 1",Color.black);
		myFont.drawString(R2.getCenterX()-myFont.getWidth("PLAYER 2")/2f,R2.getY()+5, "PLAYER 2",Color.black);
	}
	
	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		//System.out.println("LEAVING");
		arg0.getInput().removeKeyListener(KeyInput);
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 6;
	}
	
	private boolean Configurating() {
		return Configurating;
	}
	
	private int Configuration_Button_Pressed(int posX,int posY,GameContainer arg0) {
		
		for(int i=0;i<5;i++)
			if((posX>ConfigX1 && posX<ConfigX1+20) && (posY > ConfigY+(i*((ControlsBoxHeight-200)/4)) && posY < ConfigY+(i*((ControlsBoxHeight-200)/4))+20)) {		
				if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					return i+1;
				}
			}
		
		for(int i=0;i<5;i++)
			if((posX>ConfigX2 && posX<ConfigX2+20) && (posY > ConfigY+(i*((ControlsBoxHeight-200)/4)) && posY < ConfigY+(i*((ControlsBoxHeight-200)/4))+20)) {		
				if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					return i+6;
				}
			}
		
		return 0;
	}
	
	private boolean Reset_Button_Pressed(int posX,int posY,GameContainer arg0) {
		
		if ((posX > ResetX && posX < ResetX + Reset_Button.getWidth()) && (posY > ResetY && posY < ResetY + Reset_Button.getHeight())) {
			reset_h = true;
			if(hovering_r == false) {
				play_hover_sound();
				hovering_r = true;
			}
			if (Mouse.isButtonDown(0)) {
				if(clicked == false) {
					clicked = true ;
					play_click_sound();
				}
				return true;
			}
			else clicked = false;
		}
		else {
			reset_h = false;
			hovering_r = false;
		}
			
		return false;
	}
	
	private boolean Back_Button_Pressed(int posX,int posY,GameContainer arg0) {
		
		if ((posX > BackX && posX < BackX + Back.getWidth()) && (posY > BackY && posY < BackY + Back.getHeight())) {
			back_h = true;
			if(hovering_b == false) {
				play_hover_sound();
				hovering_b = true;
			}
			if (Mouse.isButtonDown(0)) {
				play_click_sound();
				return true;
			}
		}
		else {
			back_h = false;
			hovering_b = false;
		}	
			
		return false;
	}
	
	private int Arrow_Button_Pressed(int posX,int posY,GameContainer arg0) {
		for(int i=0;i<T.size();i++)
		if((posX>T.get(i).getX() && posX < T.get(i).getX()+ T.get(i).getWidth()) && (posY > T.get(i).getY()  && posY < T.get(i).getY()+ T.get(i).getHeight())) 	
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
				return i+1;
		
		return 0;
	}
	
	private int Sound_Arrow_Pressed(int posX,int posY,GameContainer arg0) {
		if((posX>ST1.getX() && posX < ST1.getX()+ ST1.getWidth()) && (posY > ST1.getY()  && posY < ST1.getY()+ ST1.getHeight())) 	
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
				return 1;
		if((posX>ST2.getX() && posX < ST2.getX()+ ST2.getWidth()) && (posY > ST2.getY()  && posY < ST2.getY()+ ST2.getHeight())) 	
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
				return 2;
		
		return 0;
	}
	
	private int CheckBock_Pressed(int posX,int posY,GameContainer arg0) {
		if((posX>CheckBox1.getX() && posX < CheckBox1.getX()+ CheckBox1.getWidth()) && (posY > CheckBox1.getY()  && posY < CheckBox1.getY()+ CheckBox1.getHeight())) 	
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
				return 1;
		if((posX>CheckBox2.getX() && posX < CheckBox2.getX()+ CheckBox2.getWidth()) && (posY > CheckBox2.getY()  && posY < CheckBox2.getY()+ CheckBox2.getHeight())) 	
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
				return 2;
		if((posX>CheckBox3.getX() && posX < CheckBox3.getX()+ CheckBox3.getWidth()) && (posY > CheckBox3.getY()  && posY < CheckBox3.getY()+ CheckBox3.getHeight())) 	
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
				return 3;
		
		return 0;
	}
	
	private class KeyPressChange implements KeyListener{
		protected boolean used=true;
		KeyPressChange(GameContainer x){
		}
		
		@Override
		public void inputEnded() {
			// TODO Auto-generated method stub
		}

		@Override
		public void inputStarted() {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean isAcceptingInput() {
			// TODO Auto-generated method stub
			return used;
		}

		public void setAcceptingInput(boolean x) {
			// TODO Auto-generated method stub
			used=x;
		}
		
		@Override
		public void setInput(org.newdawn.slick.Input arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(int arg0, char arg1) {
			// TODO Auto-generated method stub
			if(butt<6)
				sbg.Change_Settings_Key(arg0, 1, butt-1);
			else
				sbg.Change_Settings_Key(arg0, 2, butt-6);
			Configurating=false;
			KeyInput.setAcceptingInput(false);
		}

		@Override
		public void keyReleased(int arg0, char arg1) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private float[] GetTrianglePoints(int direction){
		float tmp[] = new float[6];
		
		if(direction==0) {
			tmp[0]=R1.getX() + ControlsBoxLenght/2f + Player1.getWidth()/2f + 30;
			tmp[1]=R1.getY() + Player1.getHeight()/2f+10;
			tmp[2]=R1.getX() + ControlsBoxLenght/2f + Player1.getWidth()/2f + 30;
			tmp[3]=R1.getY() + Player1.getHeight()/2f-10;
			tmp[4]=R1.getX() + ControlsBoxLenght/2f + Player1.getWidth()/2f + 50;
			tmp[5]=R1.getY() + Player1.getHeight()/2f;
		}
		else {
			tmp[0]=R1.getX() + ControlsBoxLenght/2f - Player1.getWidth()/2f - 30;
			tmp[1]=R1.getY() + Player1.getHeight()/2f+10;
			tmp[2]=R1.getX() + ControlsBoxLenght/2f - Player1.getWidth()/2f - 30;
			tmp[3]=R1.getY() + Player1.getHeight()/2f-10;
			tmp[4]=R1.getX() + ControlsBoxLenght/2f - Player1.getWidth()/2f - 50;
			tmp[5]=R1.getY() + Player1.getHeight()/2f;
		}
		
		return tmp;
	}
	
	private void Set_Triangles() {
		float points[];
		points = GetTrianglePoints(0);
		T.add(new Polygon(points));
		T.add(((Polygon) T.get(0)).copy());
		T.add(((Polygon) T.get(0)).copy());
		T.add(((Polygon) T.get(0)).copy());
		Vector2f tmp = new Vector2f();
		tmp.set(R1.getX() + ControlsBoxLenght/2f + Player1.getWidth()/2f + 30,R1.getY() + Player1.getHeight()/2f+60);
		T.get(2).setLocation(tmp);
		T.get(1).setLocation(tmp.set(R2.getX() + ControlsBoxLenght/2f + Player2.getWidth()/2f + 30,R2.getY() + Player2.getHeight()/2f-10));
		T.get(3).setLocation(tmp.set(R2.getX() + ControlsBoxLenght/2f + Player2.getWidth()/2f + 30,R2.getY() + Player2.getHeight()/2f+60));
		
		points = GetTrianglePoints(1);
		T.add(new Polygon(points));
		T.add(((Polygon) T.get(4)).copy());
		T.add(((Polygon) T.get(4)).copy());
		T.add(((Polygon) T.get(4)).copy());
		tmp.set(R1.getX() + ControlsBoxLenght/2f - Player1.getWidth()/2f - 50,R1.getY() + Player1.getHeight()/2f+60);
		T.get(6).setLocation(tmp);
		T.get(5).setLocation(tmp.set(R2.getX() + ControlsBoxLenght/2f - Player2.getWidth()/2f - 50,R2.getY() + Player2.getHeight()/2f-10));
		T.get(7).setLocation(tmp.set(R2.getX() + ControlsBoxLenght/2f - Player2.getWidth()/2f - 50,R2.getY() + Player2.getHeight()/2f+60));
	}
	
	private void Set_Volume_Arrows() {
		float points[];
		points = GetTrianglePoints(1);
		ST1=new Polygon(points);
		ST1.setLocation(R4.getX()+(R4.getWidth()/2f-no_volume.getWidth()/2f)/2f-ST1.getWidth()/2f, R4.getMaxY()-R4.getHeight()/6f);
		
		points = GetTrianglePoints(0);
		ST2=new Polygon(points);
		ST2.setLocation(R4.getMaxX()-(R4.getWidth()/2f-no_volume.getWidth()/2f)/2f+ST1.getWidth()/2f, R4.getMaxY()-R4.getHeight()/6f);
	}
	
	public void play_hover_sound() {
		
		AudioInputStream hover_sound;
	
		try {
			hover_sound = AudioSystem.getAudioInputStream(hover_file);
			Clip hover_s = AudioSystem.getClip();
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
			Clip click_s = AudioSystem.getClip();
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
