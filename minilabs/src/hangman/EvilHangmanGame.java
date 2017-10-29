package hangman;

import java.io.File;
import java.util.Set;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.TreeSet;

public class EvilHangmanGame implements IEvilHangmanGame{
	
	//Domain
	private static int inputWordLength;					//inputed length of desired words
	private static File inputFile;						//file containing words to be used
	private static Partition bestPartition;				//the best partition with the given key
	private static int numberOfGuesses;					//number of guesses remaining for the user
	private static Set<Character> usedLetters;			//letters the user has already guessed
	private static Key partialWord;						//current word the user is trying to guess
	private static char currentChar;					//current letter entered by the user
	private static SetOfPartitions wordSet;				//table containing possible words associated with a given key
	private static String inputString;
	
	//Constants
	private static final int MAX_NUM_OF_GUESSES = 26;
	
	//Constructor
	public EvilHangmanGame(){
		usedLetters = new TreeSet<Character>();
		wordSet = new SetOfPartitions();
	}
	
	//Methods
	
	public static void initializeCommandLineArguments(String[] args) throws IOException{
		
		inputWordLength = Integer.parseInt(args[1]);
		
		if(inputWordLength < 1)
			throw new IOException("Invalid input: Cannot have a word length < 1");
		
		numberOfGuesses = Integer.parseInt(args[2]);
		
		if(numberOfGuesses > MAX_NUM_OF_GUESSES)
			throw new IOException("Invalid input: Cannot have more than " + MAX_NUM_OF_GUESSES + " guesses.");
		
		File dictionary = new File(args[0]);
		
		if(!dictionary.exists() || !dictionary.canRead())
			throw new IOException("Input file does not exist or cannot be read.");
		
		inputFile = dictionary;
		
	}
	
	public String printUsedLetters(){
		String out = "";
		for(char letter : usedLetters){
			out += letter + " ";
		}
		return out;
	}
	
	
	@Override
	public void startGame(File dictionary, int wordLength) {
		
		try{
			
			Scanner input = new Scanner( new BufferedReader( new FileReader(dictionary)));		//open and read the file
			
			Key startKey = new Key(wordLength);
			Partition startPartition = new Partition(startKey);
			
			wordSet.addNewPartition(startKey, startPartition);
			
			while(input.hasNext()){																//Set up the first partition
				
				String next = input.next().toLowerCase();
				if(next.length() == wordLength){
					wordSet.addToPartition(startKey, next);
				}
			}
			
			bestPartition = wordSet.getPartitionAt(startKey);
			
			partialWord = bestPartition.getKey();
			
			Scanner in = new Scanner(System.in);
			
			while(numberOfGuesses > 0 && !partialWord.isFull()){								//PLAY THE GAME!
				
				System.out.println("You have " + numberOfGuesses + " guesses left");
				
				System.out.println("Used letters: " + printUsedLetters());
				
				System.out.println("Word: " + partialWord.getValue());
				
				System.out.println("Enter Guess: ");
				
				inputString = in.nextLine().toLowerCase();										//Get the next letter from the user input
				
				while(inputString.length() > 1 || inputString.length() == 0 || inputString.charAt(0) < 'a' || inputString.charAt(0) > 'z'){
					System.out.println("Invalid entry: please type a letter");
					System.out.println("Enter guess: ");
					inputString = in.nextLine().toLowerCase();
				}
				
				currentChar = inputString.charAt(0);
				
				boolean tryAgain = true;
				while(tryAgain){
					try{
						makeGuess(currentChar);													//Guess a letter
						tryAgain = false;														//IF already guessed, guess again
					}catch(IEvilHangmanGame.GuessAlreadyMadeException e){						//ELSE proceed with evil algorithm
						System.out.println("You already used that letter");
						System.out.println("Enter Guess: ");
						inputString = in.nextLine().toLowerCase();										//Get the next letter from the user input
						while(inputString.length() > 1 || inputString.length() == 0 || inputString.charAt(0) < 'a' || inputString.charAt(0) > 'z'){
							System.out.println("Invalid entry: please type a letter");
							System.out.println("Enter guess: ");
							inputString = in.nextLine().toLowerCase();
						}
						currentChar = inputString.charAt(0);
					}
				}
				
				usedLetters.add(currentChar);													//Add letter to list of used letters
				partialWord = bestPartition.getKey().union(partialWord);						//Union partial word with key of best partition
				
			}
			
			if(numberOfGuesses == 0){															//IF the user used all the guesses
				System.out.println("You Lose!");												//tell them they lost!
				System.out.println("The word was: " + bestPartition.pickWord());
			}
			else{																				//ELSE the partial word is complete
				System.out.println("You Win!!");												//and they win!
			}
		
			input.close();																		//Close the input scanners
			in.close();
		
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public Set<String> makeGuess(char guess) throws IEvilHangmanGame.GuessAlreadyMadeException {
		
		if(usedLetters.contains(guess)){														//IF the letter was already guessed
			throw new IEvilHangmanGame.GuessAlreadyMadeException();								//throw exception
		}
		
		wordSet = bestPartition.partition(guess);												//Create a new word set from the current partition
		
		
		bestPartition = wordSet.getBestPartition(guess);										//Select a best partition from the new word set
		
		if(bestPartition.getKey().isEmpty()){													//IF the best partitions key is empty
			System.out.println("Sorry, there are no " + guess + "'s");							//They guessed wrong
			numberOfGuesses--;
		}
		else{																					//ELSE they guessed a letter right!
			int num = bestPartition.getKey().countCharacter(guess);								//let them know and don't decrease the
			String s = "";																		//number of guesses
			String suffix = "";
			if(num > 1){
				s = "are";
				suffix = "'s";
			}
			else
				s = "is";
			System.out.println("Yes, there " + s + " " + num + " " + guess + suffix);
		}
		return bestPartition.getWords();														//return the words in the best partition
	}
	
	public static void main(String[] args) throws IOException {
		
		initializeCommandLineArguments(args);
		
		EvilHangmanGame game = new EvilHangmanGame();
		
		game.startGame(inputFile, inputWordLength);
		
	}

}
