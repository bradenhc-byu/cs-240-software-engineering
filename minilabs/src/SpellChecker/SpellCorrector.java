package SpellChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;

import SpellChecker.ISpellCorrector.NoSimilarWordFoundException;
import SpellChecker.ITrie.INode;

public class SpellCorrector implements ISpellCorrector {
	
	//Instance Variables
	public Trie words;
	public int bestFrequency;
	public int sameFrequencyValue;
	public String bestWord;
	public boolean firstRun;
	
	//Constructor
	public SpellCorrector(){
		words = new Trie();
		bestFrequency = 0;
		sameFrequencyValue = 0;
		bestWord = null;
		firstRun = true;
	}
	
	//Methods
	public ArrayList<String> findWordsWithOneEDA(String word){
		
		ArrayList<String> oneEDAWords = new ArrayList<String>();
		
		//Deletion
		for(int i = 0; i < word.length(); i++){
			oneEDAWords.add(word.substring(0,i) + word.substring(i+1));
		}
		
		//Transposition
		for(int i = 0; i+1 < word.length(); i++){
			oneEDAWords.add(word.substring(0,i) + word.substring(i+1,i+2) + word.substring(i,i+1) + word.substring(i+2));
		}
		
		//Alteration
		for(int i = 0; i < word.length(); i++){
			for(char c = 'a'; c <= 'z'; c++){
				oneEDAWords.add(word.substring(0,i) + c + word.substring(i+1));
			}
		}
		
		//Insertion
		for(int i = 0; i < word.length(); i++){
			for(char c = 'a'; c <= 'z'; c++){
				oneEDAWords.add(word.substring(0,i) + c + word.substring(i));
			}
		}
		for(char c = 'a'; c <= 'z'; c++){
			oneEDAWords.add(word + c);
		}

		return oneEDAWords;
		
		
	}
	
	public String findBestWord(ArrayList<String> similarWords){
		INode result;
		//Check frequency
		ArrayList<String> sameFrequency = new ArrayList<String>();
		for(int i = 0; i < similarWords.size(); i++){
			result = words.find(similarWords.get(i));
			if(result.getValue() > bestFrequency){
				bestFrequency = result.getValue();
				bestWord = similarWords.get(i);
			}
			//One EDA words with same frequency
			else if(result.getValue() == bestFrequency){
				sameFrequencyValue = bestFrequency;
				sameFrequency.add(similarWords.get(i));
			}
		}
		if(sameFrequencyValue == bestFrequency && sameFrequencyValue != 0){
			//return the best string alphabetically
			sameFrequency.add(bestWord);
			for(int i = 0; i < sameFrequency.size(); i++){
				if(sameFrequency.get(i).compareTo(bestWord) < 0){
					bestWord = sameFrequency.get(i);
				}
			}
		}
		return bestWord;
	}
	
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException{
		
		Scanner input = new Scanner( new BufferedReader( new FileReader(dictionaryFileName)));
		
		try {
			input.useDelimiter("[^a-zA-Z]+");
			
			while(input.hasNext()){
				
				String lowerCaseWord = input.nextLine().toLowerCase();
				words.add(lowerCaseWord);
				
			}
		} finally{
			input.close();
		}
	}
	
	@Override
	public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException{
		
		try{
			inputWord = inputWord.toLowerCase();
			INode result = null;
			if(firstRun){
				result = words.find(inputWord);
				if(result != null){
					bestWord = inputWord;
					return bestWord;
				}
			}
			ArrayList<String> oneEDAWords = findWordsWithOneEDA(inputWord);
			ArrayList<String> similarWords = new ArrayList<String>();
			for(int i = 0; i < oneEDAWords.size(); i++){
				result = words.find(oneEDAWords.get(i));
				if(result != null)
					similarWords.add(oneEDAWords.get(i));
			}
			if(similarWords.size() == 1){
				bestWord = similarWords.get(0);
			}
			else{
				//Multiple One EDA words, check for best
				bestWord = findBestWord(similarWords);
				
				if(bestWord == null){
					//did not find a similar word one EDA, check for two EDA
					firstRun = false;
					ArrayList<String> TwoEDAWords = new ArrayList<String>();
					for(int i = 0; i < oneEDAWords.size(); i++){
						TwoEDAWords.addAll(findWordsWithOneEDA(oneEDAWords.get(i)));
					}
					ArrayList<String> bestTwoEDAWords = new ArrayList<String>();
					for(int i = 0; i < TwoEDAWords.size(); i++){
						result = words.find(TwoEDAWords.get(i));
						if(result != null){
							bestTwoEDAWords.add(TwoEDAWords.get(i));
						}
					}
					if(bestTwoEDAWords.size() == 1)
						bestWord = bestTwoEDAWords.get(0);
					else{
						bestWord = findBestWord(bestTwoEDAWords);
					}
					if(bestWord == null)
						throw new ISpellCorrector.NoSimilarWordFoundException();
				}
			}	
		}catch(NoSimilarWordFoundException e){
			throw e;
		}
		
		return bestWord;
	}
	
	public static void initializeCommandLineArguments(String[] args) throws IOException {
		
		if(args.length > 2)
			throw new IOException("Too many command line arguments. Syntax is: java SpellCorrector file.txt word");
		
		File inputFile = new File(args[0]);
		
		if(!inputFile.exists() || !inputFile.canRead())
			throw new IOException("Given file name does not exist or cannot be read");
		
	}
	
	public static void main(String[] args) throws IOException, NoSimilarWordFoundException {
		
		initializeCommandLineArguments(args);
		
		SpellCorrector speller = new SpellCorrector();
		
		speller.useDictionary(args[0]);
		
		String bestWord = speller.suggestSimilarWord(args[1]);
		
		System.out.println(bestWord);
		
		
	}
}
