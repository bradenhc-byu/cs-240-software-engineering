package hangman;

import java.util.Set;
import java.util.TreeSet;

public class Partition {
	
	//Instance Variables
	private Key key;
	private Set<String> words;
	
	//Constructor
	public Partition(Key k){
		key = k;
		words = new TreeSet<String>();
	}
	
	//Queries
	public Key getKey(){
		return this.key;
	}
	
	public Set<String> getWords(){
		return this.words;
	}
	
	public int getSize(){
		return words.size();
	}
	
	public String pickWord(){
		String word = "";
		for(String s : words){
			word = s;
			break;
		}
		return word;
	}
	
	//Commands
	public void addWord(String word){
		words.add(word);
	}
	
	//Methods
	
	public Partition compareTo(Partition p, char c) {
		if(this == p)
			return this;
		if(this.getSize() != p.getSize())							//compare sizes
			return (this.getSize() > p.getSize()) ? this : p;
		
		int thisCharCount = this.key.countCharacter(c);
		int pCharCount = p.getKey().countCharacter(c);
		
		if(thisCharCount != pCharCount)								//find one with fewest characters c
			return (thisCharCount < pCharCount) ? this : p;
		else{														//find one with rightmost character c
			return (this.key.compareRightmost(p.getKey(), c) == this.key) ? this : p;
		}
	}
	
	public SetOfPartitions partition(char guess){
		SetOfPartitions newWordSet = new SetOfPartitions();
		for(String word : this.words){
			Key k = new Key(word,guess);
			if(newWordSet.contains(k)){
				newWordSet.addToPartition(k, word);
			}
			else{
				Partition p = new Partition(k);
				p.addWord(word);
				newWordSet.addNewPartition(k,p);
			}
		}
		return newWordSet;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(this.key.getValue());
		builder.append("\n\n");
		
		for(String word : this.words){
			builder.append(word + "\n");
		}
		
		return builder.toString();
	}
}
