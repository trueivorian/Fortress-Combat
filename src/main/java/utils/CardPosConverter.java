package utils;

import java.util.ArrayList;
import java.util.HashMap;


public class CardPosConverter extends Dictionary
{
	
	
	
	protected static HashMap<Integer, Class> cardPosConverter4hand1(ArrayList<String> hand1)
	{
		HashMap<Integer, Class> hand1Converted=new HashMap<Integer, Class>();
	
		for(int i=0;i<=6;i++)
		{
	
			hand1Converted.put(i+1,getEntry(i));
			
		}
		
		return hand1Converted;	
		
	}
	protected static HashMap<Integer, Class> cardPosConverter4hand2(ArrayList<String> hand2)
	{
		HashMap<Integer, Class> hand2Converted=new HashMap<Integer, Class>();
		
		for(int i=0;i<=6;i++)
		{
	
			hand2Converted.put(i+1,getEntry(i));
			
		}
		
		
		return hand2Converted;
		
		
	}
	protected static HashMap<Integer, Class> cardPosConverter4Deck1(ArrayList<String> deck1)
	{
		HashMap<Integer, Class> Deck1Converted=new HashMap<Integer, Class>();
		
		for(int i=0;i<=50;i++)
		{
	
			Deck1Converted.put(i+1,getEntry(i));
			
		}
		
		
		return Deck1Converted;
		
		
	}
	protected static HashMap<Integer, Class> cardPosConverter4Deck2(ArrayList<String> deck2)
	{
		HashMap<Integer, Class> Deck2Converted=new HashMap<Integer, Class>();
		
		for(int i=0;i<=50;i++)
		{
	
			Deck2Converted.put(i+1,getEntry(i));
			
		}
		
		
		return Deck2Converted;
		
		
	}
	
	
	
	
	

}
