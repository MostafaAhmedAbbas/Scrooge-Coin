package scoin;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.Timestamp;
import java.util.Base64;
import java.util.UUID;


public class Transaction {
	
	String TransID;
	Signature SenderSignature;
	Transaction PreviousHashPointer;
	String SenderID;
	String ReceiverID;
	Coin coin;
	String hash;
	String previoushash;
	
	public Transaction(String SenderID, String ReceiverID, Transaction previousHashPointer, Signature SenderSignature,Coin coin) {
		this.TransID = UUID.randomUUID().toString();
		this.SenderID=SenderID;
		this.ReceiverID=ReceiverID;
		this.PreviousHashPointer= previousHashPointer;
		if(!(previousHashPointer==null)) {
		previoushash=previousHashPointer.hash;
		}
		this.SenderSignature=SenderSignature;
		this.coin=coin;
		hash();
	} 
	void hash() {
		String dataToHash="";
		if(PreviousHashPointer==null) {
		 dataToHash = ""  +  SenderID + coin.CoinID + new java.util.Date();
		}else {
		 dataToHash = ""  + this.PreviousHashPointer.hash +  SenderID + coin.CoinID +new java.util.Date() ;
		}
		
		MessageDigest digest;
		String encoded = null;
		
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
			encoded = Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		this.hash = encoded;
	}
}
