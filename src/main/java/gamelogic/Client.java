package gamelogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
	String name;
	int id;
	ArrayList<Card> hand = new ArrayList<Card>();
	ArrayList<String> deck = new ArrayList<String>();
	ArrayList<String> board = new ArrayList<String>();
	ArrayList<String> grave = new ArrayList<String>();
	Socket s;
	boolean isTurn  = false;

	public Client(String Name, Socket socket){
		this.name = Name;
		s = socket;
	}

	public void initialiseDeck(String deckName) throws IOException {
		String fileName = deckName + ".txt";
		File file = new File(fileName);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		while((line = br.readLine()) != null){
		    deck.add(line);
		}
	}


	public int getId() {
		return id;
	}

	public void setId(int i) {
		id = i;
	}
	public void sendHand() {

	}
	public void sendGrave() {

	}
	public void sendDeck() {

	}
	public void sendBoard() {

	}

}
