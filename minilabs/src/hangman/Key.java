package hangman;

import java.lang.StringBuilder;

public class Key {
	//Instance Variables
	private String value;
	
	//Constructor
	public Key(int wordLength){
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < wordLength; i++){
			builder.append("-");
		}
		this.value = builder.toString();
	}
	
	public Key(String v){
		this.value = v;
	}
	
	public Key(String v, char c){
		String val = "";
		for(int i = 0; i < v.length(); i++){
			if(v.charAt(i) == c)
				val += c;
			else
				val += "-";
		}
		this.value = val;
	}
	
	//Queries
	public String getValue(){
		return this.value;
	}
	
	public int hashCode(){
		return value.hashCode();
	}
	
	//Methods
	
	public int countCharacter(char c){
		int count = 0;
		for(int i = 0; i < value.length(); i++){
			if(value.charAt(i) == c)
				count++;
		}
		return count;
	}
	
	public Key compareRightmost(Key k, char c){
		int index = this.value.length() - 1;
		while(index >= 0){
			if(this.value.charAt(index) != k.getValue().charAt(index)){
				if(this.value.charAt(index) == c)
					return this;
				else
					return k;
			}
			index--;
		}
		return this;
	}
	
	public boolean isFull(){
		return value.indexOf("-") < 0;
	}
	
	public boolean isEmpty(){
		for(int i = 0; i < value.length(); i++){
			if(value.charAt(i) != '-')
				return false;
		}
		return true;
	}
	
	public Key union(Key k){
		StringBuilder builder = new StringBuilder();
		builder.append(this.value);
		for(int i = 0; i < this.value.length(); i++){
			if(builder.charAt(i) != k.getValue().charAt(i) && builder.charAt(i) == '-'){
				builder.setCharAt(i,k.getValue().charAt(i));
			}
		}
		return new Key(builder.toString());
	}
}
