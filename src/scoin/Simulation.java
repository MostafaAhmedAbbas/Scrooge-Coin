package scoin;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JTextField;

public  class Simulation {
	String ToBeWritten;
	boolean endSim;
	JFrame jframe;
	public Simulation() {
		endSim=true;
		  JTextField textField = new JTextField();
			 
		  textField.addKeyListener(new MKeyListener(this));
		 
		  jframe = new JFrame();
		  jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		  jframe.add(textField);
		 
		  jframe.setSize(400, 350);
		 
		  jframe.setVisible(true);
	}
	void StartSimulation() throws InvalidKeyException, NoSuchAlgorithmException {
		this.ToBeWritten="";
		
		
		
		Scrooge scrooge = new Scrooge();
		scrooge.s=this;
		
		ArrayList<User> users = new ArrayList<User>();
		for(int i=0;i<100;i++) { 
			User u = new User();
			users.add(u);
			this.ToBeWritten=this.ToBeWritten + " User's Public Key" + Base64.getEncoder().encodeToString(u.publicKey.getEncoded())  +" has " +"10 Coins"+"\n";
			this.ToBeWritten=this.ToBeWritten+ "\n";
			System.out.println("User's Public Key" +Base64.getEncoder().encodeToString(u.publicKey.getEncoded())+" has " +"10 Coins \n");
		}
		scrooge.users.addAll(users);
		
		for(int i=0; i< users.size();i++) {
			User current=users.get(i);
			for(int j=0;j<10;j++) {
				Coin coin=scrooge.CreateCoin();
				scrooge.AssignCoin(current, coin);
			}
//			this.ToBeWritten=this.ToBeWritten + " User's Public Key" +current.publicKey+" has " + current.OwnedCoins.size()+" Coins"+"\n";
//			System.out.println("User's Public Key" +current.publicKey+" has " + current.OwnedCoins.size()+" Coins");
		}
		
		Random rand = new Random();
		while(endSim) {
			int senderIndex = rand.nextInt(100);
			int receiverIndex=rand.nextInt(100);
			User sender = users.get(senderIndex);
			User receiver=users.get(receiverIndex);
			if(sender.OwnedCoins.size()>0) {
			scrooge.VerifyTransaction(sender.createTransaction(receiver.UserID));}
		}
		
		
		
	}
	void EndSimulation() {
		endSim=false;
		writeToFile();
		jframe.dispose();
	}
	
	 void writeToFile() {
		 String s=ToBeWritten;
//		try {
//			File myObj = new File("C:\\Users\\Mostafa\\outputOfScrooge.txt");
//		      if (myObj.createNewFile()) {
//		        System.out.println("File created: " + myObj.getName());
//		      } else {
//		        System.out.println("File already exists.");
//		      }
//		    } catch (IOException e) {
//		      System.out.println("An error occurred.");
//		      e.printStackTrace();
//		    }
		 
			File dir = new File("output");
			dir.mkdirs();
			File tmp = new File(dir, "outputOfScrooge.txt");
			try {
				tmp.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 
		 
		try {
		      FileWriter myWriter = new FileWriter("output\\outputOfScrooge.txt");
		      myWriter.write(s);
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
}
class MKeyListener extends KeyAdapter {
	 Simulation simulation;
    
    public MKeyListener(Simulation simulation) {
    	this.simulation=simulation;
    }
    
    public void keyPressed(KeyEvent event) {
 
  char ch = event.getKeyChar();
 
  if (ch == 32) {
 
System.out.println("Terminate");
simulation.EndSimulation();
 
  }
 
//  if (event.getKeyCode() == KeyEvent.VK_HOME) {
// 
//System.out.println("Key codes: " + event.getKeyCode());
// 
//  }
    }
}