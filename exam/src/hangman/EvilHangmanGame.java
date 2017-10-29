package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;

public class EvilHangmanGame implements IEvilHangmanGame {
	
	//Domain
	private String inputLine;
	private char guessedChar;
	private Partition bestPartition;
	private SetOfPartitions wordSet;
	private int numberOfGuesses;
	private TreeSet<Character> usedLetters;
	private Key partialWord;
	
	//Constructor
	public EvilHangmanGame(){
		this.inputLine = "";
		this.bestPartition = new Partition();
		this.wordSet = new SetOfPartitions();
	}
	
	//Command
	public void setNumberOfGuesses(int num){
		this.numberOfGuesses = num;
	}
	
	//Methods
	public static void initializeCommandLineArguments(String[] args) throws IOException {
		
		if(args.length > 3)
			throw new IOException("Too many command line arguments. Syntax is: java hangman file.txt wordLength numberOfGuesses");
		
		File inputFile = new File(args[0]);
		
		if(!inputFile.exists() || !inputFile.canRead())
			throw new IOException("Input file does not exist or cannot be read");
		
		if(Integer.parseInt(args[1]) < 0 || Integer.parseInt(args[2]) < 0)
			throw new IOException("Cannot have negative values for word length and number of guesses");
		
	}
	
	public String printGuessedLetters() {
		String result = "";
		for(char c : usedLetters){
			result = result + c + " ";
		}
		return result;
	}
	
	public void printNewRound(){
		
		System.out.println("You have " + numberOfGuesses + " guesses left");
		
		System.out.println("Used letters: " + printGuessedLetters());
		
		System.out.println("Word: " + partialWord.getValue());
		
		System.out.println("Enter guess: ");
		
	}
	
	public void startGame(File dictionary, int wordLength){
		
		try{
			Scanner input = new Scanner( new BufferedReader( new FileReader( dictionary)));
			
			Key startKey = new Key(wordLength);
			Partition startPartition = new Partition(startKey);
			
			while(input.hasNext()){
				
				String word = input.next().toLowerCase();
				startPartition.addWord(word);
				
			}
			
			this.wordSet.addNewPartition(startPartition);
			input.close();
			
			//Time to play the game!!
			Scanner in = new Scanner(System.in);
			
			while(this.numberOfGuesses > 0 && !partialWord.isFull()){
				
				this.printNewRound();
				
				this.inputLine = in.nextLine().toLowerCase();
				
				while(this.inputLine.length() > 1 || this.inputLine.charAt(0) < 'a' || this.inputLine.charAt(0) > 'z'){
					System.out.println("Invalid input: please enter a letter");
					System.out.println("Enter guess: ");
					this.inputLine = in.nextLine().toLowerCase();
				}
				
				this.guessedChar = this.inputLine.charAt(0);
				
				boolean guessing = true;
				
				try{
					while(guessing){
						makeGuess(this.guessedChar);
						guessing = false;
					}
				}catch(GuessAlreadyMadeException e){
					System.out.println("You've already guessed a " + this.guessedChar);
					System.out.println("Enter guess: ");
					this.inputLine = in.nextLine().toLowerCase();
					while(this.inputLine.length() > 1 || this.inputLine.charAt(0) < 'a' || this.inputLine.charAt(0) > 'z'){
						System.out.println("Invalid input: pleas enter a letter");
						System.out.println("Enter guess: ");
						this.inputLine = in.nextLine().toLowerCase();
					}
					this.guessedChar = this.inputLine.charAt(0);
				}
				
				this.usedLetters.add(this.guessedChar);
				this.partialWord = this.bestPartition.getKey().union(this.partialWord);
				
			}
			
			//Check to see why the loop was exited
			
			if(this.numberOfGuesses == 0){
				System.out.println("You lose!");
				System.out.println("The word was:" + bestPartition.pickWord());
			}
			else
				System.out.println("You win!!");
			
			
			in.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public Set<String> makeGuess(char guess) throws IEvilHangmanGame.GuessAlreadyMadeException{
		
		if(this.usedLetters.contains(guess))
			throw new GuessAlreadyMadeException();
		
		this.wordSet = bestPartition.partition(guess);
		
		this.bestPartition = wordSet.getBestPartition(guess);
		
		if(bestPartition.getKey().isEmpty()){
			System.out.println("Sorry, there are no " + guess + "'s");
			this.numberOfGuesses--;
		}
		else{
			int num = bestPartition.getKey().getCharacterCount(guess);
			String s = "is";
			String suffix = "";
			if(num > 1){
				s = "are";
				suffix = "'s";
			}
			System.out.println("Yes, there " + s + " " + num + " " + guess + suffix);
		}
			
		return this.bestPartition.getWords();
	}
	
	public static void main(String[] args) throws IOException{
		
		initializeCommandLineArguments(args);
		
		EvilHangmanGame game = new EvilHangmanGame();
		
		File inputFile = new File(args[0]);
		
		int wordLength = Integer.parseInt(args[1]);
		
		game.setNumberOfGuesses(Integer.parseInt(args[2]));
		
		game.startGame(inputFile, wordLength);
		
	}
}
