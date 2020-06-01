package scoin;

import java.util.ArrayList;

public class BlockChain {
	
	ArrayList<Block> chain;
	
	public BlockChain() {
		chain = new ArrayList<Block>();
		chain.add(generateGenesis()); 
	}
	
	private Block generateGenesis() {
		Block genesis = new Block(new java.util.Date(), "<transactions>");
		genesis.previousHash=null;
		genesis.computeHash();
		return genesis;
	}
	public void addBlock(Block blk) {
		Block newBlock = blk;
		newBlock.previousHash=(chain.get(chain.size()-1).hash);
		newBlock.computeHash();
		this.chain.add(newBlock);
	}
public String displayChain() {
		String res="";
		for(int i=0; i<chain.size(); i++) {
			System.out.println("Block: " + i+" ");
			res = res+ "Block: " + i +" ";
//			System.out.println("Version: " + chain.get(i).version);
			System.out.println("Timestamp: " + chain.get(i).Timestamp +" ");
			res = res + "Timestamp: " + chain.get(i).Timestamp +" ";
			System.out.println("PreviousHash: " + chain.get(i).previousHash +" ");
			res = res + "PreviousHash: " + chain.get(i).previousHash +" ";
			System.out.println("Hash: " + chain.get(i).hash +" ");
			res = res + "Hash: " + chain.get(i).hash +" ";
			System.out.println();
			res =res +"\n";
		}
		return res;
	}
	
	public Block getLatestBlock() {
		return this.chain.get(chain.size()-1);
	}
	
	public void isValid() {
		
		for(int i=chain.size()-1; i>0; i--) {
			if(   !(chain.get(i).hash.equals(chain.get(i).computeHash()))   ) {
				System.out.println("Chain is not valid");
				return;
			}
			
			if(  !(chain.get(i).previousHash.equals(chain.get(i-1).computeHash()))  ) {
				System.out.println("Chain is not valid");
				return;
			}
		}
		
		System.out.println("Chain is valid.");
		
	}
}
