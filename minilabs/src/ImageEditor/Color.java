package ImageEditor;

import java.io.PrintWriter;

public class Color {
	//Constants
	public static final int MAX_COLOR_VALUE = 255;
	
	//Domain
	public int value;
	
	//Constructor
	public Color(int val){
		value = val;
	}
	
	//Queries
	public int getValue(){
		return value;
	}
	
	//Commands
	public void setValue(int val) throws Exception{
		try{
			if(val > MAX_COLOR_VALUE || val < 0){
				throw new NumberFormatException();
			}
			else
				value = val;
		}catch(NumberFormatException e){
			throw new Exception("Invalid color value. (Negative values or values exceeding 255 are not allowed)");
		}
	}
	
	//Methods
	public void invert(){
		value = MAX_COLOR_VALUE - value;
	}
	
	public void print(PrintWriter out){
		
		out.println(value + " ");
		
	}
}
