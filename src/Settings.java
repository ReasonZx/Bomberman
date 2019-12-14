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
			for(int j=0;j<4;j++)
				if(Settings_List[i][j]>6 || Settings_List[i][j]<0)
					return true;
		//System.out.println("COLORS OKAY");
		for(int i=0;i<2;i++)
			for(int j=4;j<9;j++)
				for(int k=i;k<2;k++)
					for(int l=j+1;l<9;l++)
						if(Settings_List[i][j]==Settings_List[k][l])
							return true;
		//System.out.println("CONTROLS OKAY");
		return false;
	}

	private int[][] Read_File() throws IOException {
		List<String> s = Files.readAllLines(Setting);
		int[][] tmp = new int[2][9];
		
		if(s.size()!=18)
			return null;
		
		for(int i=0;i<2;i++)
			for(int j=0;j<9;j++)
				tmp[i][j]=Integer.parseInt(s.get(j+i*9));	
		
		return tmp;
	}
	
	public void Write_Default_Settings() throws IOException {
		List<String> s = Arrays.asList("0","1","2","3",
										Integer.toString(Input.KEY_W),
										Integer.toString(Input.KEY_S),
										Integer.toString(Input.KEY_A),
										Integer.toString(Input.KEY_D),
										Integer.toString(Input.KEY_SPACE),
										"4","2","1","3",
										Integer.toString(Input.KEY_UP),
										Integer.toString(Input.KEY_DOWN),
										Integer.toString(Input.KEY_LEFT),
										Integer.toString(Input.KEY_RIGHT),
										Integer.toString(Input.KEY_RSHIFT));
		Files.write(Setting,s,StandardCharsets.UTF_8);
		for(int i=0;i<2;i++)
			for(int j=0;j<9;j++)
				Settings_List[i][j]=Integer.parseInt(s.get(j+i*9));
	}
	
	private void Write_Settings() throws IOException {
		List<String> s = new ArrayList<String>();
		
		for(int i=0;i<2;i++)
			for(int j=0;j<9;j++)
				s.add(Integer.toString(Settings_List[i][j]));
		
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
			for(int j=4;j<9;j++)
				if(key==Settings_List[i][j]) 
					Settings_List[i][j]=Settings_List[player-1][pos+4];
				
		Settings_List[player-1][pos+4]=key;			
		
		try {
			Write_Settings();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
