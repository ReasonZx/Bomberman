import java.util.ArrayList;

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
	private Image Reset_Button;
	private Image Back_Button;
	private Image Player1,Player2;
	private Shape R1,R2,R3;
	private ArrayList <Shape> T;
	
	private int ConfigX1=300;
	private int ConfigX2=700;
	private int ConfigY=200;
	protected int butt;
	protected boolean Configurating=false;
	private KeyPressChange KeyInput;
	private int ControlsBoxLenght=300;
	private int ControlsBoxHeight=500;
	private Font myFont;
	private int ResetX=650,ResetY=560;
	private int BackX=30,BackY=560;
	private int[][] Settings;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		myFont=arg0.getDefaultFont();
		KeyInput = new KeyPressChange(arg0);
		sbg=(GUI_setup) arg1;
		sbg.Set_Controls_State(getID());
		Configuration= new Image("sprites/Configuration_Wheel.png");
		Configuration=Configuration.getScaledCopy((float)0.05);
		Reset_Button= new Image("sprites/Reset_Button.png");
		Reset_Button=Reset_Button.getScaledCopy(0.2f);
		Back_Button= new Image("sprites/back.png");
		Back_Button=Back_Button.getScaledCopy(0.1f);
		R1 = new Rectangle(50, 50, ControlsBoxLenght, ControlsBoxHeight); 
		R2 = new Rectangle(450, 50, ControlsBoxLenght, ControlsBoxHeight);
		R3 = new Rectangle(200, 200, 400, 200);
	}
	
	@Override
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
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
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		// TODO Auto-generated method stub
		arg2.setColor(Color.white);
		arg2.fill(R1);
		arg2.fill(R2);
		arg2.setColor(Color.black);
	
		for(int i=0;i<T.size();i++) {
			arg2.fill(T.get(i));
		}
		
		for(int i=0;i<5;i++) {
			myFont.drawString(50f + ControlsBoxLenght/2f - myFont.getWidth(Input.getKeyName(Settings[0][i+2]))/2f, ConfigY+myFont.getHeight(Input.getKeyName(Settings[0][i+2]))/2f+(i*((ControlsBoxHeight-200)/4)), Input.getKeyName(Settings[0][i+2]),Color.black);
			arg2.drawImage(Configuration,ConfigX1,ConfigY+(i*((ControlsBoxHeight-200)/4)));
		}
		
		for(int i=0;i<5;i++) {
			myFont.drawString(450f + ControlsBoxLenght/2f - myFont.getWidth(Input.getKeyName(Settings[1][i+2]))/2f, ConfigY+myFont.getHeight(Input.getKeyName(Settings[1][i+2]))/2f+(i*((ControlsBoxHeight-200)/4)), Input.getKeyName(Settings[1][i+2]),Color.black);
			arg2.drawImage(Configuration,ConfigX2,ConfigY+(i*((ControlsBoxHeight-200)/4)));
		}
		
		arg2.drawImage(Reset_Button,ResetX,ResetY);
		arg2.drawImage(Back_Button,BackX,BackY);
		arg2.drawImage(Player1,50 + ControlsBoxLenght/2f - Player1.getWidth()/2f,50 + Player1.getHeight()/2f);
		arg2.drawImage(Player2,450 + ControlsBoxLenght/2f - Player2.getWidth()/2f,50 + Player2.getHeight()/2f);
		
		if(Configurating()) {
			arg2.setColor(Color.black);
			arg2.fill(R3);
			arg2.setColor(Color.white);
			myFont.drawString(arg0.getWidth()/2f - myFont.getWidth("Press a new key")/2f,
	                    250, "Press a new key");
			myFont.drawString(arg0.getWidth()/2f - myFont.getWidth("Currently:")/2f,
                    300, "Currently:");
			if(butt<6)
				myFont.drawString(arg0.getWidth()/2f - myFont.getWidth("< "+ Input.getKeyName(Settings[0][butt+1]) +" >")/2f,
	                    350, "< "+ Input.getKeyName(Settings[0][butt+1]) +" >");
			else
				myFont.drawString(arg0.getWidth()/2f - myFont.getWidth("< "+ Input.getKeyName(Settings[1][butt-6+2]) +" >")/2f,
                    350, "< "+ Input.getKeyName(Settings[1][butt-6+2]) +" >");
		}
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
			
			if(Reset_Button_Pressed(posX,posY,arg0)) 
				sbg.Reset_Settings();
			
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
		if((posX>ResetX && posX < ResetX+Reset_Button.getWidth()) && (posY > ResetY  && posY < ResetY+Reset_Button.getHeight())) 	
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
				return true;
		
			
		return false;
	}
	
	private boolean Back_Button_Pressed(int posX,int posY,GameContainer arg0) {
		if((posX>BackX && posX < BackX+Back_Button.getWidth()) && (posY > BackY  && posY < BackY+Back_Button.getHeight())) 	
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
				return true;
		
			
		return false;
	}
	
	private int Arrow_Button_Pressed(int posX,int posY,GameContainer arg0) {
		for(int i=0;i<T.size();i++)
		if((posX>T.get(i).getX() && posX < T.get(i).getX()+ T.get(i).getWidth()) && (posY > T.get(i).getY()  && posY < T.get(i).getY()+ T.get(i).getHeight())) 	
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) 
				return i+1;
		
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
			tmp[0]=50 + ControlsBoxLenght/2f + Player1.getWidth()/2f + 30;
			tmp[1]=50 + Player1.getHeight()/2f+10;
			tmp[2]=50 + ControlsBoxLenght/2f + Player1.getWidth()/2f + 30;
			tmp[3]=50 + Player1.getHeight()/2f-10;
			tmp[4]=50 + ControlsBoxLenght/2f + Player1.getWidth()/2f + 50;
			tmp[5]=50 + Player1.getHeight()/2f;
		}
		else {
			tmp[0]=50 + ControlsBoxLenght/2f - Player1.getWidth()/2f - 30;
			tmp[1]=50 + Player1.getHeight()/2f+10;
			tmp[2]=50 + ControlsBoxLenght/2f - Player1.getWidth()/2f - 30;
			tmp[3]=50 + Player1.getHeight()/2f-10;
			tmp[4]=50 + ControlsBoxLenght/2f - Player1.getWidth()/2f - 50;
			tmp[5]=50 + Player1.getHeight()/2f;
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
		tmp.set(50 + ControlsBoxLenght/2f + Player1.getWidth()/2f + 30,50 + Player1.getHeight()/2f+60);
		T.get(2).setLocation(tmp);
		T.get(1).setLocation(tmp.set(450 + ControlsBoxLenght/2f + Player2.getWidth()/2f + 30,50 + Player2.getHeight()/2f-10));
		T.get(3).setLocation(tmp.set(450 + ControlsBoxLenght/2f + Player2.getWidth()/2f + 30,50 + Player2.getHeight()/2f+60));
		
		points = GetTrianglePoints(1);
		T.add(new Polygon(points));
		T.add(((Polygon) T.get(4)).copy());
		T.add(((Polygon) T.get(4)).copy());
		T.add(((Polygon) T.get(4)).copy());
		tmp.set(50 + ControlsBoxLenght/2f - Player1.getWidth()/2f - 50,50 + Player1.getHeight()/2f+60);
		T.get(6).setLocation(tmp);
		T.get(5).setLocation(tmp.set(450 + ControlsBoxLenght/2f - Player2.getWidth()/2f - 50,50 + Player2.getHeight()/2f-10));
		T.get(7).setLocation(tmp.set(450 + ControlsBoxLenght/2f - Player2.getWidth()/2f - 50,50 + Player2.getHeight()/2f+60));
	}
}
