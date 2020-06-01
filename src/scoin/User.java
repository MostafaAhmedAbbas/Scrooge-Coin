package scoin;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.UUID;

public class User {
	public PublicKey publicKey;
	private PrivateKey privateKey;
	String UserID;
	ArrayList<Coin> OwnedCoins;
	public Signature sign;
	
	public User() throws InvalidKeyException, NoSuchAlgorithmException {
		
		this.UserID=UUID.randomUUID().toString();
		OwnedCoins=new ArrayList<Coin>();
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
	Transaction createTransaction(String receiverID) {
		Coin coin = OwnedCoins.get(0);
		Transaction trans = new Transaction(this.UserID,receiverID,coin.previousTransactions.get(coin.previousTransactions.size()-1),this.sign,coin);
		return trans;
	}
}
