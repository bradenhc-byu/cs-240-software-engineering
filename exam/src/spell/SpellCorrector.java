package spell;

import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.ArrayList;

import spell.ITrie.INode;

public class SpellCorrector implements ISpellCorrector {
	
	//Domain
	private Trie words;
	private int bestFrequency;
	private int similarFrequency;
	private String bestWord;
	
	
	//Constructor
	public SpellCorrector(){
		words = new Trie();
	}
	
	//Methods
	public static void initializeCommandLineArguments(String[] args) throws IOException {
		
		if(args.length > 2)
			throw new IOException("Too many command line arguments. Format is: java spell filename.txt word");
		
		File inputFile = new File(args[0]);
		
		if(inputFile.exists() || !inputFile.canRead())
			throw new IOException("Input file does not exist or cannot be read");
		
	}
	
	public ArrayList<String> findWordsWithOneEDA(String word) {
		
		ArrayList<String> oneEDAWords = new ArrayList<String>();
		
		//Deletion
		for(int i = 0; i < word.length(); i++){
			oneEDAWords.add(word.substring(0,i) + word.substring(i+1));
		}
		
		//Transposition
		for(int i = 0; i + 1 < word.length(); i++){
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
		for(char c = 'a'; c <='z'; c++){
			oneEDAWords.add(word + c);
		}
		
		return oneEDAWords;
		
	}
	
	public String findBestWord(ArrayList<String> similarWords) {
		
		INode result;
		ArrayList<String> similarFrequencyWords = new ArrayList<String>();
		//Check the frequency of each word in the trie
		for(int i = 0; i < similarWords.size(); i++){
			result = words.find(similarWords.get(i));
			if(result.getValue() > bestFrequency){
				bestWord = similarWords.get(i);
				bestFrequency = result.getValue();
			}
			else if(result.getValue() == bestFrequency){
				similarFrequency = bestFrequency;
				similarFrequencyWords.add(similarWords.get(i));
				
			}
		}
		if(bestFrequency == similarFrequency && similarFrequency != 0){
			//Return the string that is alphabetically first
			similarFrequencyWords.add(bestWord);
			for(int i = 0; i < similarFrequencyWords.size(); i++){
				if(similarFrequencyWords.get(i).compareTo(bestWord)< 0)
					bestWord = similarFrequencyWords.get(i);
			}
		}
		return bestWord;
		
	}
	
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		
		Scanner inputFile = new Scanner( new BufferedReader( new FileReader( new File(dictionaryFileName))));;
		try{
			while(inputFile.hasNext()){
				String word = inputFile.next().toLowerCase();
				words.add(word);
			}
		}finally {
			inputFile.close();
		}
	}
	
	@Override
	public String suggestSimilarWord(String inputWord) throws ISpellCorrector.NoSimilarWordFoundException {
		
		try{
			
		INode result = words.find(inputWord);
		if(result != null){
			bestWord = inputWord;
		}
		else{
			ArrayList<String> oneEDAWords = findWordsWithOneEDA(inputWord);
			ArrayList<String> similarOneEDAWords = new ArrayList<String>();
			for(int i = 0; i < oneEDAWords.size(); i++){
				result = words.find(oneEDAWords.get(i));
				if(result != null)
					similarOneEDAWords.add(oneEDAWords.get(i));
			}
			if(similarOneEDAWords.size() == 1){
				bestWord = similarOneEDAWords.get(0);
			}
			else{
				bestWord = findBestWord(similarOneEDAWords);
				if(bestWord == null){
					//Do it all for Two EDA Words
					ArrayList<String> twoEDAWords = new ArrayList<String>();
					for(int i = 0; i < oneEDAWords.size(); i++){
						twoEDAWords.addAll(findWordsWithOneEDA(oneEDAWords.get(i)));
					}
					ArrayList<String> similarTwoEDAWords = new ArrayList<String>();
					for(int i = 0; i < twoEDAWords.size(); i++){
						result = words.find(twoEDAWords.get(i));
						if(result != null)
							similarTwoEDAWords.add(twoEDAWords.get(i));
					}
					if(similarTwoEDAWords.size() == 1){
						bestWord = similarTwoEDAWords.get(0);
					}
					else{
						bestWord = findBestWord(similarTwoEDAWords);
						if(bestWord == null)
							throw new NoSimilarWordFoundException();
					}
				}
			}
		}
		return bestWord;
		
		}catch(NoSimilarWordFoundException e){
			throw e;
		}
		
	}
	
	public static void main(String[] args) throws IOException,ISpellCorrector.NoSimilarWordFoundException {
		
		initializeCommandLineArguments(args);
		
		SpellCorrector checker = new SpellCorrector();
		
		checker.useDictionary(args[0]);
		
		String similarWord = checker.suggestSimilarWord(args[1]);
		
		System.out.println(similarWord);
		
	}

}
