package spell;

import java.lang.StringBuilder;

public class Trie implements ITrie {
	
	public class TrieNode implements INode {
		
		//Domain
		private char value;
		private TrieNode[] children;
		private int frequency;
		
		//Constructor
		public TrieNode(char c) {
			value = c;
			children = new TrieNode[26];
			frequency = 0;
			nodeCount++;
		}
		
		//Queries
		public char getLetter() {
			return value;
		}
		
		public int getValue() {
			return frequency;
		}
		
		public TrieNode[] getChildren() {
			return children;
		}
		
		public TrieNode getChildAt(int index){
			return children[index];
		}
		
		//Commands
		public void setValue(char c) {
			value = c;
		}
		
		public void increaseFrequency(){
			frequency++;
			wordCount++;
		}
		
		public void setChildValueAt(int index, char c){
			children[index].setValue(c);
		}
		
		public void setChildAt(int index, TrieNode n){
			children[index] = n;
		}
		
	}
	
	//Trie Domain
	private static int code = 0;
	private int wordCount;
	private int nodeCount;
	private TrieNode[] rootChildren;
	
	//Constructor
	public Trie() {
		code = code++;
		wordCount = 0;
		nodeCount = 1;
		rootChildren = new TrieNode[26];
	}
	
	//Queries
	@Override
	public int getWordCount() {
		return wordCount;
	}
	
	@Override
	public int getNodeCount() {
		return nodeCount;
	}
	
	public TrieNode[] getRootChildren() {
		return rootChildren;
	}
	
	public TrieNode getRootChildAt(int index) {
		return rootChildren[index];
	}
	
	@Override
	public int hashCode() {
		return code;
	}
	
	//Commands
	public void setRootChildAt(int index, char c) {
		rootChildren[index].setValue(c);
	}
	
	//Methods
	@Override
	public void add(String word) {
		char c = word.charAt(0);
		int index = c - 'a';
		if(rootChildren[index] == null)
			rootChildren[index] = new TrieNode(c);
		recursiveAdd(rootChildren[index],word.substring(1));
	}
	
	public void recursiveAdd(TrieNode node, String suffix){
		if(suffix.length() == 0){
			node.increaseFrequency();
			return;
		}
		else{
			char c = suffix.charAt(0);
			int index = c - 'a';
			if(node.getChildAt(index) == null)
				node.setChildAt(index, new TrieNode(c));
			recursiveAdd(node.getChildAt(index),suffix.substring(1));
		}
	}
	
	@Override
	public INode find(String word) {
		char c = word.charAt(0);
		int index = c - 'a';
		if(rootChildren[index] == null)
			return null;
		else
			return recursiveFind(rootChildren[index],word.substring(1));
	}
	
	public TrieNode recursiveFind(TrieNode node, String suffix){
		if(suffix.length() == 0)
			return node;
		char c = suffix.charAt(0);
		int index = c - 'a';
		if(node.getChildAt(index) == null)
			return null;
		else
			return recursiveFind(node.getChildAt(index),suffix.substring(1));
		
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < this.rootChildren.length; i++){
			String prefix = "";
			getStrings(builder,prefix,this.rootChildren[i]);
		}
		return builder.toString();
	}
	
	public void getStrings(StringBuilder builder, String prefix, TrieNode node) {
		if(node == null)
			return;
		else{
			String word = prefix + node.getLetter();
			if(node.getValue() > 0)
				builder.append(word + "\n");
			for(int i = 0; i < node.getChildren().length; i++){
				getStrings(builder,word,node.getChildAt(i));
			}
		}
		
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Trie == false)
			return false;
		else if(((Trie) o).getNodeCount() != this.getNodeCount())
			return false;
		else if(((Trie) o).getWordCount() != this.getWordCount())
			return false;
		else{
			for(int i = 0; i < this.rootChildren.length; i++)
				if(equalsCheck(this.rootChildren[i],((Trie) o).getRootChildAt(i)) == false)
					return false;
		}
		return true;
	}
	
	public boolean equalsCheck(TrieNode thisNode, TrieNode objectNode) {
		if(thisNode == null && objectNode == null)
			return true;
		else if(thisNode.getLetter() == objectNode.getLetter() && thisNode.getValue() == objectNode.getValue()){
			for(int i = 0; i < thisNode.getChildren().length; i++){
				if(equalsCheck(thisNode.getChildAt(i),objectNode.getChildAt(i)) == false)
					return false;
			}
			return true;
		}
		else
			return false;
	}
}