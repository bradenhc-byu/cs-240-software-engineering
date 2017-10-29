package hangman;

public class Key {
	
	//Domain
	String value;
	
	//Constructor
	public Key(int wordLength){
		for(int i = 0; i < wordLength; i++) {
			this.value += "-";
		}
	}
	
	public Key(String word, char letter){
		for(int i = 0; i < word.length(); i++){
			if(word.charAt(i) == letter)
				this.value += letter;
			else
				this.value += "-";
		}
	}
	
	public Key(String v){
		this.value = v;
	}
	
	//Queries
	public String getValue() {
		return value;
	}
	
	public int getCharacterCount(char c) {
		int count = 0;
		for(int i = 0; i < value.length(); i++){
			if(value.charAt(i) == c)
				count++;
		}
		return count;
	}
	
	public boolean isFull(){
		for(int i = 0; i < this.value.length(); i++){
			if(this.value.charAt(i) == '-')
				return false;
		}
		return true;
	}
	
	public boolean isEmpty(){
		for(int i = 0; i < this.value.length(); i++){
			if(this.value.charAt(i) != '-')
				return false;
		}
		return true;
	}
	
	//Methods
	public Key getRightmostKey(Key k, char c){
		int index = value.length() - 1;
		while(index >= 0){
			if(this.value.charAt(index) != k.getValue().charAt(index)){
				if(this.value.charAt(index) == c)
					return this;
				else
					return k;
			}
		}
		return this;		//default return in case of error is this key
	}
	
	public Key union(Key k){
		StringBuilder builder = new StringBuilder();
		builder.append(this.value);
		for(int i = 0; i < this.value.length(); i++){
			if(this.value.charAt(i) != k.getValue().charAt(i))
				builder.setCharAt(i, k.getValue().charAt(i));
		}
		return new Key(builder.toString());
	}

}
