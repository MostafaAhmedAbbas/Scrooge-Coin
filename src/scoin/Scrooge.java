package scoin;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;

public class Scrooge {
	BlockChain ledger;
	String HashPointer;
	public PublicKey publicKey;
	private PrivateKey privateKey;
	Signature sign;
	ArrayList<Transaction> transactions;
	Block currentBlock;
	ArrayList<User> users;
	ArrayList<String> AllHashValues;
	Simulation s;
	public Scrooge() throws InvalidKeyException, NoSuchAlgorithmException {
		users=new ArrayList<User>();
		AllHashValues= new ArrayList<String>();
		transactions=new ArrayList<Transaction>();
		ledger=new BlockChain();
		createSignature();
		
	}
	void createSignature() throws NoSuchAlgorithmException, InvalidKeyException{
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
		keyPairGen.initialize(2048);
		KeyPair pair = keyPairGen.generateKeyPair();
		PrivateKey privKey = pair.getPrivate();
		PublicKey pubKey=pair.getPublic();
		this.privateKey=privKey;
		this.publicKey=pubKey;
		sign = Signature.getInstance("SHA256withDSA");
		sign.initSign(privKey); 
		
		
	}
	public void PublishBlock(Block block) {

		ledger.addBlock(block);
		String ss=ledger.displayChain();
		s.ToBeWritten=s.ToBeWritten+ss;
		s.ToBeWritten=s.ToBeWritten+"\n";
		for(int i=0;i<10;i++) {
			Transaction tran = transactions.get(i);
			User receiver = getUserByID(tran.ReceiverID);
			User sender = getUserByID(tran.SenderID);
			Coin coin = sender.OwnedCoins.get(sender.OwnedCoins.size()-1);
			receiver.OwnedCoins.add(coin);
			sender.OwnedCoins.remove(sender.OwnedCoins.size()-1);
		}
		
	}
	public Coin CreateCoin() {
		Coin coin = new Coin();
		Transaction creationTransaction=new Transaction("Scrooge","",null,sign,coin);
		AllHashValues.add(creationTransaction.hash);
		transactions.add(creationTransaction);
		publishInitialTransactions();
		coin.previousTransactions.add(creationTransaction);
		return coin;
	}
	public void AssignCoin(User receiver,Coin coin) {
		Transaction assigment=new Transaction("Scrooge",receiver.UserID,coin.previousTransactions.get(coin.previousTransactions.size()-1),sign,coin);
		AllHashValues.add(assigment.hash);
		receiver.OwnedCoins.add(coin);
		transactions.add(assigment);
		publishInitialTransactions();
	}
	void publishInitialTransactions() {
		if(transactions.size()==10) {
			System.out.println("New Block Created! \n");
			s.ToBeWritten=s.ToBeWritten + "New Block Created!"+"\n";
			s.ToBeWritten=s.ToBeWritten+"\n";
			String data="";
			for(int i=0;i<10;i++) {
				data= data + transactions.get(i).toString();
			}
			Block block = new Block( new java.util.Date(), data);
			ledger.addBlock(block);
			String ss=ledger.displayChain();
			s.ToBeWritten=s.ToBeWritten+ss;
			s.ToBeWritten=s.ToBeWritten+"\n";
			transactions.clear();
		}
	}
	public void VerifyTransaction(Transaction trans) {
		boolean flag=true;
		User receiver = getUserByID(trans.ReceiverID);
		User sender = getUserByID(trans.SenderID);
		Coin coin = trans.coin; 
		//make sure user owns coin
		if(sender.OwnedCoins.size()==0) {
			//System.out.println("Invalid Transaction 1 \n");
			return;
		}
		if(!(sender.OwnedCoins.contains(trans.coin))) {
			//System.out.println("Invalid Transaction 2 \n");
			return;
		}
		
		//make sure unique hash
		if(AllHashValues.contains(trans.hash)) {
			//System.out.println("Invalid Transaction 3 \n");
			return;
		}else {
			AllHashValues.add(trans.hash);
		}
		
		//trace back transactions of this coin
		Transaction previoustransaction = trans.PreviousHashPointer;
		if(!(previoustransaction.hash.equals(trans.previoushash))) {
			//System.out.println("Invalid Transaction 4 \n");
			return;
		}
		
		//double spending coin appears twice in the same block from transactions arraylist
		for(int i=0;i<transactions.size()-1;i++) {
			Transaction prevTran= transactions.get(i);
			if(prevTran.coin.CoinID.equals(coin.CoinID)) {
				//System.out.println("Invalid Transaction 5 \n");
				return;
			}
		}
		
		//verify signature 
		try { 
			User us = sender;
			byte[] signature = us.sign.sign();
			us.sign.initVerify(us.publicKey);
			flag = us.sign.verify(signature);
			
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//return;
		}
		
		
		if(flag) {
			String addToFile=("Succesfl Transaction!" +"\n"+"\n"+"Transaction Details: Sender: "+trans.SenderID+" Receiver: "+ trans.ReceiverID+ " Signature: "+trans.SenderSignature+ " Hash: " + trans.hash+"\n"+"\n");
			System.out.println(addToFile);
			s.ToBeWritten=s.ToBeWritten+addToFile;
			transactions.add(trans);
		}else {
			//System.out.println("Invalid Transaction 6 \n");
			return;
		}
		if(transactions.size()==10) {
			System.out.println("New Block Created! \n");
			s.ToBeWritten=s.ToBeWritten+"\n";
			s.ToBeWritten=s.ToBeWritten+"New Block Created!"+"\n";
			s.ToBeWritten=s.ToBeWritten+"\n";
			String data="";
			for(int i=0;i<10;i++) {
				data= data + transactions.get(i).toString();
			}
			Block block = new Block( new java.util.Date(), data);
			PublishBlock(block);
			transactions.clear();
		}
		
	}
	public User getUserByID(String ID) {
		for(int i=0;i<users.size();i++) {
			if(users.get(i).UserID.equals(ID)) {
				return users.get(i);
			}
		}
		return null;
	}
}
