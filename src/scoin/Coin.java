package scoin;

import java.util.ArrayList;
import java.util.UUID;

public class Coin {
	public String CoinID;
	public ArrayList<Transaction> previousTransactions;
	public Coin() {
		this.CoinID = UUID.randomUUID().toString();
		previousTransactions=new ArrayList<Transaction>();
	}
}
 