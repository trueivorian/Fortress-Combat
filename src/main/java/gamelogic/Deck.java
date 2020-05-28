package gamelogic;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
public class Deck {
	ArrayList<String> deck;
	
	public void createDeck(String Name, ArrayList deck) throws IOException {
		FileWriter fw = new FileWriter(new File("\\Deck\\", Name + ".txt"));
		BufferedWriter writer = new BufferedWriter(fw);

		for(int i = 0;i<deck.size()-1;i++) {
			writer.append(deck.get(i).toString());
		}

		writer.close();
		
	}
	//This takes in an arraylist of strings and writes them to a text file. This arraylist should be filled with the id's of the cards.
}
