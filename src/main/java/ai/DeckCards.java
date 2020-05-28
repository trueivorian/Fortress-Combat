package ai;
import java.util.ArrayList;
import graphics.UIController;

import java.util.Arrays;
import java.util.HashMap;
import gamelogic.*;
import networking.exceptions.ServerNotFoundException;
public class DeckCards
{

	static ArrayList<Integer> deck=new  ArrayList<Integer>();

	static
	{
		deck.add(6);
		deck.add(8);
		deck.add(10);
		deck.add(10);
		deck.add(11);
		deck.add(12);
		deck.add(13);
		deck.add(13);
		deck.add(14);
		deck.add(14);
		deck.add(15);
		deck.add(16);
		deck.add(17);
		deck.add(18);
		deck.add(19);
		deck.add(19);
		deck.add(9);
		deck.add(20);
		deck.add(20);
		deck.add(21);
		deck.add(21);
		deck.add(22);
		deck.add(22);
		deck.add(23);
		deck.add(24);
		deck.add(25);
		deck.add(25);
		deck.add(26);
		deck.add(27);
		deck.add(34);
		deck.add(45);
		deck.add(53);
		deck.add(44);
		deck.add(45);
		deck.add(53);
		deck.add(57);
		deck.add(28);
		deck.add(29);
		deck.add(30);
		deck.add(31);
		deck.add(32);
		deck.add(33);
		deck.add(34);
		deck.add(35);
		deck.add(36);
		deck.add(37);
		deck.add(39);
		deck.add(38);
		deck.add(40);
		deck.add(41);
		deck.add(1);
		try{
			System.out.println(Arrays.asList(deck));
			UIController.joinGame("Oona", deck) ;
		}
		catch (ServerNotFoundException e)
		{
			e.printStackTrace();
		}

	}


	 public static int get_castle()
	 {
		 int returnCastle;
		 int r=(int)(Math.random()*3);
		 {
			 if(r==0)
			 {
				 returnCastle= 1;
			 }
			 else if(r==1)
			 {
				 returnCastle=2;

			 }
			 else
			 {
				 returnCastle=4;
			 }
			return returnCastle;
	     }
	 }
}


