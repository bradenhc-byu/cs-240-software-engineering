package SpellChecker;


public class Trie implements ITrie {
	
	public class TrieNode implements INode {
		//Instance Variables
		private char root;
		private int frequency;
		private TrieNode[] children;
		
		//Constructor
		public TrieNode(char c){
			nodeCount++;
			root = c;
			frequency = 0;
			children = new TrieNode[26];
		}
		
		//Queries
		@Override
		public int getValue(){
			return frequency;
		}
		
		public char getRoot(){
			return root;
		}
		
		public TrieNode[] getChildren(){
			return children;
		}
		
		//Commands
		public void increaseFrequency(){
			frequency++;
		}
		
		public void setChildAt(int index, char c){
			children[index] = new TrieNode(c);
		}
		
	}
	
	
	//Instance Variables-------------------------------------------
	private static int code = 0;		//hash code value
	public TrieNode[] children;			//first letters of words in the trie
	public int wordCount;				//number of words in the trie
	private int nodeCount;				//number of nodes in the trie
	
	//Constructor--------------------------------------------------
	
	public Trie(){
		code = code++;
		children = new TrieNode[26];
		wordCount = 0;
		nodeCount = 1;
	}
	
	//Queries------------------------------------------------------
	
	@Override
	public int getNodeCount(){
		return nodeCount;
	}
	
	@Override
	public int getWordCount(){
		return wordCount;
	}
	
	@Override
	public int hashCode(){
		return code;
	}
	
	public TrieNode[] getChildren(){
		return children;
	}
	
	//Methods------------------------------------------------------
	
	@Override
	public void add(String word){
		int index = word.charAt(0) - 'a';
		if(children[index] == null){
			children[index] = new TrieNode(word.charAt(0));
			recursiveAdd(children[index],word.substring(1));
		}
		else{
			if(word.length() == 1){
				children[index].increaseFrequency();
				wordCount++;
				return;
			}
			else{
				recursiveAdd(children[index],word.substring(1));
			}
		}
	}
	
	public void recursiveAdd(TrieNode node, String suffix){
		if(suffix.length() == 0){
			node.increaseFrequency();
			wordCount++;
			return;
		}
		else{
			int index = suffix.charAt(0) - 'a';
			if(node.getChildren()[index] == null){
				node.setChildAt(index,suffix.charAt(0));
				recursiveAdd(node.getChildren()[index],suffix.substring(1));
			}
			else
				recursiveAdd(node.getChildren()[index],suffix.substring(1));
		}
	}
	
	@Override
	public INode find(String word){
		if(word.length() > 0){
			int index = word.charAt(0) - 'a';
			return recursiveFind(children[index],word.substring(1));
		}
		return null;
	}
	
	public INode recursiveFind(TrieNode n, String suffix){
		if(n == null || (suffix.length() == 0 && n.getValue() == 0))
			return null;
		else if(suffix.length() == 0 && n.getValue() > 0)
			return n;
		else{
			int index = suffix.charAt(0)-'a';
			return recursiveFind(n.getChildren()[index],suffix.substring(1));
		}
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Trie == false)
			return false;
		else if((((Trie)o)).getNodeCount() != nodeCount || ((Trie)o).getWordCount() != wordCount)
			return false;
		else{
			for(int x = 0; x < children.length; x++){
				if(recursiveEqualsCheck(((Trie)o).getChildren()[x],children[x]) == false)
					return false;
			}
			return true;
		}
		
		
	}
	
	public boolean recursiveEqualsCheck(TrieNode objectNode, TrieNode thisNode){
		if(objectNode == null && thisNode == null)
			return true;
		else if(objectNode.getRoot() == thisNode.getRoot() && objectNode.getValue() == thisNode.getValue()){
			for(int x = 0; x < thisNode.getChildren().length; x++){
				if(recursiveEqualsCheck(objectNode.getChildren()[x],thisNode.getChildren()[x]) == false)
					return false;
			}
			return true;
		}
		else
			return false;
	}
	
	@Override
	public String toString(){
		StringBuilder builder =  new StringBuilder();
		for(int x = 0; x < 26; x++){
			if(children[x] != null){
				String prefix = "";
				getStrings(builder,prefix,children[x]);		
			}
			
		}
		return builder.toString();
		
	}
	
	public void getStrings(StringBuilder builder, String word, TrieNode n){
		if(n == null)
			return;
		else if(n.getValue() > 0){
			String nextWord = word + n.getRoot();
			builder.append(nextWord + "\n");
			for(int x = 0; x < n.getChildren().length; x++){
				getStrings(builder,nextWord,n.getChildren()[x]);
			}
		}
		else{
			String nextWord = word + n.getRoot();
			for(int x = 0; x < n.getChildren().length; x++){
				getStrings(builder,nextWord,n.getChildren()[x]);
			}
		}
	}
}
