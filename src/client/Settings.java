package client;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.newdawn.slick.Input;

public class Settings {
	private Path Setting;
	int[][] Settings_List;
	int Resolution;
	int Volume;
	
	public void init_Settings() {
		Setting = Paths.get("Settings.txt");
		try {
			
			if(Files.exists(Setting)==false) {
				Files.createFile(Setting);
				Write_Default_Settings();
			}	
			
			Settings_List = Read_File();
			
			if(Settings_Denied()==true)
				Write_Default_Settings();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean Settings_Denied() throws IOException {
		
		if (Settings_List == null)
			return true;
		
		for(int i=0;i<2;i++)
			for(int j=0;j<2;j++)
				if(Settings_List[i][j]>5 || Settings_List[i][j]<0)
					return true;
		//System.out.println("COLORS OKAY");
		for(int i=0;i<2;i++)
			for(int j=2;j<7;j++)
				for(int k=i;k<2;k++)
					for(int l=j+1;l<7;l++)
						if(Settings_List[i][j]==Settings_List[k][l])
							return true;
		//System.out.println("CONTROLS OKAY");
		if(Resolution>3 || Resolution<1)
			return true;
		
		if(Volume>3 || Volume<0)
			return true;
		
		return false;
	}

	private int[][] Read_File() throws IOException {
		List<String> s = Files.readAllLines(Setting);
		int[][] tmp = new int[2][7];
		
		if(s.size()!=16)
			return null;
		
		for(int i=0;i<2;i++)
			for(int j=0;j<7;j++)
				tmp[i][j]=Integer.parseInt(s.get(j+i*7));
		
		Resolution=Integer.parseInt(s.get(14));
		Volume=Integer.parseInt(s.get(15));
		
		return tmp;
	}
	
	public void Write_Default_Settings() throws IOException {
		List<String> s = Arrays.asList("0","1",
										Integer.toString(Input.KEY_W),
										Integer.toString(Input.KEY_S),
										Integer.toString(Input.KEY_A),
										Integer.toString(Input.KEY_D),
										Integer.toString(Input.KEY_SPACE),
										"4","2",
										Integer.toString(Input.KEY_UP),
										Integer.toString(Input.KEY_DOWN),
										Integer.toString(Input.KEY_LEFT),
										Integer.toString(Input.KEY_RIGHT),
										Integer.toString(Input.KEY_RSHIFT),
										"2",
										"3");
		Files.write(Setting,s,StandardCharsets.UTF_8);
		for(int i=0;i<2;i++)
			for(int j=0;j<7;j++)
				Settings_List[i][j]=Integer.parseInt(s.get(j+i*7));
		Resolution=2;
		Volume=3;
	}
	
	private void Write_Settings() throws IOException {
		List<String> s = new ArrayList<String>();
		
		for(int i=0;i<2;i++)
			for(int j=0;j<7;j++)
				s.add(Integer.toString(Settings_List[i][j]));
		
		s.add(Integer.toString(Resolution));
		s.add(Integer.toString(Volume));
		
		Files.write(Setting,s,StandardCharsets.UTF_8);
	}
	
	public int[][] Get_Settings() {
		return Settings_List;
	}
	
	public void Set_Settings(int[][] x) {
		Settings_List=x;
	}
	
	public void Add_New_Key(int key,int player,int pos) {
		
		for(int i=0;i<2;i++)
			for(int j=2;j<7;j++)
				if(key==Settings_List[i][j]) 
					Settings_List[i][j]=Settings_List[player-1][pos+2];
				
		Settings_List[player-1][pos+2]=key;			
		
		try {
			Write_Settings();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Add_New_Color(int arrow,int player,int pos) {
		if(arrow>=5) {
			Settings_List[player-1][pos]++;
			if(Settings_List[player-1][pos]>4)
				Settings_List[player-1][pos]=0;
		}
		else {
			Settings_List[player-1][pos]--;
			if(Settings_List[player-1][pos]<0)
				Settings_List[player-1][pos]=4;
		}
		
		try {
			Write_Settings();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Set_Resolution(int res) {
		Resolution=res;
		try {
			Write_Settings();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int Get_Resolution() {
		return Resolution;
	}

	public void Set_Volume(int vol) {
		Volume=vol;
		try {
			Write_Settings();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int Get_Volume() {
		return Volume;
	}
}
