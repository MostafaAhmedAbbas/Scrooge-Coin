package scoin;

// https://github.com/TrojanCookieProtocol/BlockchainSeries1
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

public class Block {
	
//	 String version;
	 Date Timestamp;
	 String hash;
	 String previousHash;
	 String data;
	
	public Block( Date timestamp, String data) {
//		this.version = version;
		this.Timestamp = timestamp;
		this.data = data;
		this.hash = computeHash();
	}
	public String computeHash() { 
		
		String dataToHash = ""  + this.Timestamp + this.previousHash + this.data;
		
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
		return encoded;
		
	}
	
}
