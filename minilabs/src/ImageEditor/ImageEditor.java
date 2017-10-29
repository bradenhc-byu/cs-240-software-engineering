package ImageEditor;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;


public class ImageEditor {
	//Constants
	public static final String INVERT = "invert";
	public static final String GRAYSCALE = "grayscale";
	public static final String EMBOSS = "emboss";
	public static final String MOTION_BLUR = "motionblur";
	
	
	//Domain
	private static Image image;
	private static File inputFile;
	private static File outputFile;
	private static int blurLength;
	
	//Main routine
	public static void main(String[] args) throws Exception{
		try{
			
			initializeCommandLineArguments(args);
			
			Scanner scanner = new Scanner(new BufferedReader(new FileReader(inputFile)));
			PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
			
			scanner.useDelimiter("\\s*#.*(\r?\n|\r)?|\\s+");
			
			image = new Image(scanner);
			
			switch(args[2]){
			
				case INVERT:
					image.invert();
					break;
				case GRAYSCALE:
					image.grayscale();
					break;
				case EMBOSS:
					image.emboss();
					break;
				case MOTION_BLUR:
					image.motionblur(blurLength);
					break;
			
			}
			
			image.print(printer);

		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}	
	
	//Private methods
	private static void initializeCommandLineArguments(String[] args) throws Exception{
		
		if(args.length < 4 || (args.length == 4 && args[2].equals(MOTION_BLUR))){
			
			inputFile = new File(args[0]);
			outputFile = new File(args[1]);
			
			if(!inputFile.exists() || !inputFile.canRead()){
				throw new Exception("The input file does not exist or cannot be read");
			}else{
				switch(args[2]){
					case INVERT: break;
					case GRAYSCALE: break;
					case EMBOSS: break;
					case MOTION_BLUR: break;
					default: throw new Exception("USAGE: Invalid command argument: " + args[2] + "\n\njava ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
				}
				if(args[2].equals(MOTION_BLUR)){
					try{
						blurLength = Integer.parseInt(args[3]);
						if(blurLength < 0)
							throw new Exception();
					}catch(Exception e){
						throw new Exception("Cannot have negative value for motionblur.");
					}	
				}	
			}
		}else{
			throw new Exception("Too many command line arguments. Number of arguments: " + args.length);	
		}	
	}
}