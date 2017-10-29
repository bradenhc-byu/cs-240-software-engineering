package hangman;

import java.util.HashMap;
import java.util.Iterator;

public class SetOfPartitions {
	
	//Domain
	private HashMap<String,Partition> partitions;
	
	//Constructor
	public SetOfPartitions(){
		partitions = new HashMap<String,Partition>();
	}
	
	//Queries
	public HashMap<String,Partition> getAllPartitions() {
		return partitions;
	}
	
	public Partition getPartition(Key k){
		return partitions.get(k.getValue());
	}
	
	public boolean contains(Key k){
		return partitions.containsKey(k.getValue());
	}
	
	//Commands
	public void addNewPartition(Partition p){
		partitions.put(p.getKey().getValue(), p);
	}
	
	public void addToPartition(Key k, String word){
		partitions.get(k.getValue()).addWord(word);
	}
	
	//Methods
	public Partition getBestPartition(char letter){
		Iterator<String> iter = partitions.keySet().iterator();
		Partition bestPartition = partitions.get(iter.next());
		Partition p;
		while(iter.hasNext()){
			p = partitions.get(iter.next());
			bestPartition = bestPartition.compareTo(p, letter);
		}
		return bestPartition;
	}

}
