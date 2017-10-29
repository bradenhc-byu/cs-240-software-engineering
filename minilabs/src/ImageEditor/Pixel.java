package ImageEditor;

import java.util.Scanner;
import java.io.PrintWriter;
import java.lang.Math;

public class Pixel {
	//Domain
	public Color red;
	public Color blue;
	public Color green;
	
	//Constructor
	public Pixel(Scanner inputImage){
		red = new Color(inputImage.nextInt());
		green = new Color(inputImage.nextInt());
		blue = new Color(inputImage.nextInt());
	}
	
	//Queries
	public int getRedVal(){
		return red.getValue();
	}
	
	public int getBlueVal(){
		return blue.getValue();
	}
	
	public int getGreenVal(){
		return green.getValue();
	}
	
	//Commands
	public void setRedVal(int v) throws Exception{
		red.setValue(v);
	}
	
	public void setGreenVal(int v) throws Exception {
		green.setValue(v);
	}
	
	public void setBlueVal(int v) throws Exception{
		blue.setValue(v);
	}
	
	
	//Private and Public Methods
	public void invert(){
		red.invert();
		green.invert();
		blue.invert();
	}
			
	public void grayscale() throws Exception{
		int newVal = (red.getValue() + green.getValue() + blue.getValue()) / 3;
		try{
			red.setValue(newVal);
			green.setValue(newVal);
			blue.setValue(newVal);
		}catch(Exception e){
			throw new Exception("Invalid color value during grayscale calculation.");
		}
	}

	public void emboss(Pixel upperLeft) throws Exception{
		int redDiff = red.getValue() - upperLeft.getRedVal();
		int greenDiff = green.getValue() - upperLeft.getGreenVal();
		int blueDiff = blue.getValue() - upperLeft.getBlueVal();
		int absRed = Math.abs(redDiff);
		int absGreen = Math.abs(greenDiff);
		int absBlue = Math.abs(blueDiff);
		
		int maxDiff = Math.max(absRed,Math.max(absGreen, absBlue));
		
		if(maxDiff == absRed)
			maxDiff = redDiff;
		else if(maxDiff == absGreen)
			maxDiff = greenDiff;
		else
			maxDiff = blueDiff;
		
		maxDiff += 128;
		
		if(maxDiff < 0)
			maxDiff = 0;
		else if(maxDiff > Color.MAX_COLOR_VALUE)
			maxDiff = Color.MAX_COLOR_VALUE;
		try{
			red.setValue(maxDiff);
			green.setValue(maxDiff);
			blue.setValue(maxDiff);
		}catch(Exception e){
			throw new Exception("Invalid color value during emboss calculation.");
		}
	}

	public void motionBlur(Pixel[] pixels) throws Exception{
			int redSum = 0;
			int greenSum = 0;
			int blueSum = 0;
			int size = pixels.length;
			if(size == 0)
				return;
			for(int x = 0; x < size; x++){
				redSum += pixels[x].getRedVal();
				greenSum += pixels[x].getGreenVal();
				blueSum += pixels[x].getBlueVal();
			}
			int redAvg = redSum / size;
			int greenAvg = greenSum / size;
			int blueAvg = blueSum / size;
			try {
				red.setValue(redAvg);
				green.setValue(greenAvg);
				blue.setValue(blueAvg);
			} catch (Exception e){
				throw new Exception("Invalid color value during motionblur calculation");
			}
	}
	
	public void print(PrintWriter writer){
		
		red.print(writer);
		green.print(writer);
		blue.print(writer);
		writer.write("  ");
		
	}
}
