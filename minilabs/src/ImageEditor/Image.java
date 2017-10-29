package ImageEditor;

import java.util.Arrays;
import java.util.Scanner;
import java.io.PrintWriter;

public class Image {
	//Instance variables
	public Pixel[][] image;
	public int width;
	public int height;
	public int maxColor;
	
	//Queries
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public Pixel[][] getImage(){
		return image;
	}
	
	public int getMaxColor(){
		return maxColor;
	}
	
	
	//Constructors
	public Image(Scanner input) throws Exception{
		input.next();
		width = input.nextInt();
		height = input.nextInt();
		maxColor = input.nextInt();
		image = new Pixel[height][width];
		
		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				image[row][col] = new Pixel(input);
			}
		}
	}
	
	//Methods
	public void invert(){
		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				image[row][col].invert();
			}
		}
	}
	
	public void grayscale() throws Exception{
		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				image[row][col].grayscale();
			}
		}
	}
	
	public void emboss() throws Exception{
		for(int row = height-1; row > 0; row--){
			for(int col = width-1; col > 0; col--){
					Pixel upperLeftPixel = image[row-1][col-1];
					image[row][col].emboss(upperLeftPixel);
			}
		}
		for(int col = 0; col < width; col++){
			image[0][col].setRedVal(128);
			image[0][col].setGreenVal(128);
			image[0][col].setBlueVal(128);
		}
		for(int row = 0; row < height; row++){
			image[row][0].setRedVal(128);
			image[row][0].setGreenVal(128);
			image[row][0].setBlueVal(128);
		}
	}
	
	public void motionblur(int blurLength) throws Exception{
		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				int blurDistance = Math.min(blurLength - 1, width - col - 1);
				image[row][col].motionBlur(Arrays.copyOfRange(image[row],col, col + blurDistance + 1));
			}
		}
	}
	
	public void print(PrintWriter writer){
		writer.println("P3\n");
		writer.println(width + " ");
		writer.println(height + "\n");
		writer.println(maxColor + "\n");
		
		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				image[row][col].print(writer);
			}
			writer.println("\n");
		}
		
	}
}
