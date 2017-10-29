package hangman;

import java.util.TreeSet;
import java.util.Set;

public class Partition {
	
	//Domain
	private Set<String> words;
	private Key key;
	
	//Constructors
	public Partition(){
		this.words = new TreeSet<String>();
		this.key = null;
	}
	
	public Partition(Key k){
		this.words = new TreeSet<String>();
		this.key = k;
	}
	
	//Queries
	public Key getKey() {
		return this.key;
	}
	
	public int getSize() {
		return this.words.size();
	}
	
	public Set<String> getWords() {
		return words;
	}
	
	public String pickWord(){
		for(String word : words)
			return word;
		return "";
	}
	
	//Commands
	public void addWord(String word){
		this.words.add(word);
	}
	
	public void setKey(Key k){
		this.key = k;
	}
	
	//Methods
	public SetOfPartitions partition(char letter) {
		SetOfPartitions newWordSet = new SetOfPartitions();
		for(String word : this.words){
			Key k = new Key(word,letter);
			if(newWordSet.contains(k))
				newWordSet.addToPartition(k, word);
			else{
				Partition p = new Partition(k);
				newWordSet.addNewPartition(p);
			}
		}
		return newWordSet;
	}
	
	public Partition compareTo(Partition other, char letter){
		if(this.getSize() != other.getSize())
			return (this.getSize() > other.getSize()) ? this : other;
		int thisCharCount = this.key.getCharacterCount(letter);
		int otherCharCount = other.getKey().getCharacterCount(letter);
		if(thisCharCount != otherCharCount)
			return (thisCharCount > otherCharCount) ? this : other;
		return (this.key.getRightmostKey(other.key, letter) == this.key) ? this : other;
	}

}
