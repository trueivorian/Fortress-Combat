package utils;

import java.util.EnumMap;

import javafx.geometry.Point3D;

/**
 * 
 * @author Osanne Gbayere
 * A type that enumerates all possible positions on the board
 * and maps this to a specific coordinate on the scene
 *
 */
public enum CardPos {
	MY_HAND_1 ("MY_HAND_1"),
	MY_HAND_2 ("MY_HAND_2"),
	MY_HAND_3 ("MY_HAND_3"),
	MY_HAND_4 ("MY_HAND_4"),
	MY_HAND_5 ("MY_HAND_5"),
	MY_HAND_6 ("MY_HAND_6"),
	MY_HAND_7 ("MY_HAND_7"),
	MY_HAND_8 ("MY_HAND_8"),
	MY_HAND_9 ("MY_HAND_9"),
	MY_HAND_10 ("MY_HAND_10"),
	MY_DECREE ("MY_DECREE"), 
	MY_MAGE_1 ("MY_MAGE_1"),
	MY_SOLDIER_1 ("MY_SOLDIER_1"),
	MY_SOLDIER_2 ("MY_SOLDIER_2"),
	MY_MAGE_2 ("MY_MAGE_2"),
	MY_TRICKSTER_1 ("MY_TRICKSTER_1"),
	MY_CASTLE ("MY_CASTLE"),
	MY_TRICKSTER_2 ("MY_TRICKSTER_2"),
	MY_DISCARD_1 ("MY_DISCARD_1"),
	MY_DISCARD_2 ("MY_DISCARD_2"),
	MY_DISCARD_3 ("MY_DISCARD_3"),
	MY_DISCARD_4 ("MY_DISCARD_4"),
	MY_DISCARD_5 ("MY_DISCARD_5"),
	MY_DISCARD_6 ("MY_DISCARD_6"),
	MY_DISCARD_7 ("MY_DISCARD_7"),
	MY_DISCARD_8 ("MY_DISCARD_8"),
	MY_DISCARD_9 ("MY_DISCARD_9"),
	MY_DISCARD_10 ("MY_DISCARD_10"),
	MY_DISCARD_11 ("MY_DISCARD_11"),
	MY_DISCARD_12 ("MY_DISCARD_12"),
	MY_DISCARD_13 ("MY_DISCARD_13"),
	MY_DISCARD_14 ("MY_DISCARD_14"),
	MY_DISCARD_15 ("MY_DISCARD_15"),
	MY_DISCARD_16 ("MY_DISCARD_16"),
	MY_DISCARD_17 ("MY_DISCARD_17"),
	MY_DISCARD_18 ("MY_DISCARD_18"),
	MY_DISCARD_19 ("MY_DISCARD_19"),
	MY_DISCARD_20 ("MY_DISCARD_20"),
	MY_DISCARD_21 ("MY_DISCARD_21"),
	MY_DISCARD_22 ("MY_DISCARD_22"),
	MY_DISCARD_23 ("MY_DISCARD_23"),
	MY_DISCARD_24 ("MY_DISCARD_24"),
	MY_DISCARD_25 ("MY_DISCARD_25"),
	MY_DISCARD_26 ("MY_DISCARD_26"),
	MY_DISCARD_27 ("MY_DISCARD_27"),
	MY_DISCARD_28 ("MY_DISCARD_28"),
	MY_DISCARD_29 ("MY_DISCARD_29"),
	MY_DISCARD_30 ("MY_DISCARD_30"),
	MY_DISCARD_31 ("MY_DISCARD_31"),
	MY_DISCARD_32 ("MY_DISCARD_32"),
	MY_DISCARD_33 ("MY_DISCARD_33"),
	MY_DISCARD_34 ("MY_DISCARD_34"),
	MY_DISCARD_35 ("MY_DISCARD_35"),
	MY_DISCARD_36 ("MY_DISCARD_36"),
	MY_DISCARD_37 ("MY_DISCARD_37"),
	MY_DISCARD_38 ("MY_DISCARD_38"),
	MY_DISCARD_39 ("MY_DISCARD_39"),
	MY_DISCARD_40 ("MY_DISCARD_40"),
	MY_DISCARD_41 ("MY_DISCARD_41"),
	MY_DISCARD_42 ("MY_DISCARD_42"),
	MY_DISCARD_43 ("MY_DISCARD_43"),
	MY_DISCARD_44 ("MY_DISCARD_44"),
	MY_DISCARD_45 ("MY_DISCARD_45"),
	MY_DISCARD_46 ("MY_DISCARD_46"),
	MY_DISCARD_47 ("MY_DISCARD_47"),
	MY_DISCARD_48 ("MY_DISCARD_48"),
	MY_DISCARD_49 ("MY_DISCARD_49"),
	MY_DISCARD_50 ("MY_DISCARD_50"),
	MY_DECK_1 ("MY_DECK_1"),
	MY_DECK_2 ("MY_DECK_2"),
	MY_DECK_3 ("MY_DECK_3"),
	MY_DECK_4 ("MY_DECK_4"),
	MY_DECK_5 ("MY_DECK_5"),
	MY_DECK_6 ("MY_DECK_6"),
	MY_DECK_7 ("MY_DECK_7"),
	MY_DECK_8 ("MY_DECK_8"),
	MY_DECK_9 ("MY_DECK_9"),
	MY_DECK_10 ("MY_DECK_10"),
	MY_DECK_11 ("MY_DECK_11"),
	MY_DECK_12 ("MY_DECK_12"),
	MY_DECK_13 ("MY_DECK_13"),
	MY_DECK_14 ("MY_DECK_14"),
	MY_DECK_15 ("MY_DECK_15"),
	MY_DECK_16 ("MY_DECK_16"),
	MY_DECK_17 ("MY_DECK_17"),
	MY_DECK_18 ("MY_DECK_18"),
	MY_DECK_19 ("MY_DECK_19"),
	MY_DECK_20 ("MY_DECK_20"),
	MY_DECK_21 ("MY_DECK_21"),
	MY_DECK_22 ("MY_DECK_22"),
	MY_DECK_23 ("MY_DECK_23"),
	MY_DECK_24 ("MY_DECK_24"),
	MY_DECK_25 ("MY_DECK_25"),
	MY_DECK_26 ("MY_DECK_26"),
	MY_DECK_27 ("MY_DECK_27"),
	MY_DECK_28 ("MY_DECK_28"),
	MY_DECK_29 ("MY_DECK_29"),
	MY_DECK_30 ("MY_DECK_30"),
	MY_DECK_31 ("MY_DECK_31"),
	MY_DECK_32 ("MY_DECK_32"),
	MY_DECK_33 ("MY_DECK_33"),
	MY_DECK_34 ("MY_DECK_34"),
	MY_DECK_35 ("MY_DECK_35"),
	MY_DECK_36 ("MY_DECK_36"),
	MY_DECK_37 ("MY_DECK_37"),
	MY_DECK_38 ("MY_DECK_38"),
	MY_DECK_39 ("MY_DECK_39"),
	MY_DECK_40 ("MY_DECK_40"),
	MY_DECK_41 ("MY_DECK_41"),
	MY_DECK_42 ("MY_DECK_42"),
	MY_DECK_43 ("MY_DECK_43"),
	MY_DECK_44 ("MY_DECK_44"),
	MY_DECK_45 ("MY_DECK_45"),
	MY_DECK_46 ("MY_DECK_46"),
	MY_DECK_47 ("MY_DECK_47"),
	MY_DECK_48 ("MY_DECK_48"),
	MY_DECK_49 ("MY_DECK_49"),
	MY_DECK_50 ("MY_DECK_50"),
	THEIR_HAND_1 ("THEIR_HAND_1"),
	THEIR_HAND_2 ("THEIR_HAND_2"),
	THEIR_HAND_3 ("THEIR_HAND_3"),
	THEIR_HAND_4 ("THEIR_HAND_4"),
	THEIR_HAND_5 ("THEIR_HAND_5"),
	THEIR_HAND_6 ("THEIR_HAND_6"),
	THEIR_HAND_7 ("THEIR_HAND_7"),
	THEIR_HAND_8 ("THEIR_HAND_8"),
	THEIR_HAND_9 ("THEIR_HAND_9"),
	THEIR_HAND_10 ("THEIR_HAND_10"),
	THEIR_DECREE ("THEIR_DECREE"),
	THEIR_MAGE_1 ("THEIR_MAGE_1"),
	THEIR_SOLDIER_1 ("THEIR_SOLDIER_1"),
	THEIR_SOLDIER_2 ("THEIR_SOLDIER_2"),
	THEIR_MAGE_2 ("THEIR_MAGE_2"),
	THEIR_TRICKSTER_1 ("THEIR_TRICKSTER_1"),
	THEIR_CASTLE ("THEIR_CASTLE"),
	THEIR_TRICKSTER_2 ("THEIR_TRICKSTER_2"),
	THEIR_DISCARD_1 ("THEIR_DISCARD_1"),
	THEIR_DISCARD_2 ("THEIR_DISCARD_2"),
	THEIR_DISCARD_3 ("THEIR_DISCARD_3"),
	THEIR_DISCARD_4 ("THEIR_DISCARD_4"),
	THEIR_DISCARD_5 ("THEIR_DISCARD_5"),
	THEIR_DISCARD_6 ("THEIR_DISCARD_6"),
	THEIR_DISCARD_7 ("THEIR_DISCARD_7"),
	THEIR_DISCARD_8 ("THEIR_DISCARD_8"),
	THEIR_DISCARD_9 ("THEIR_DISCARD_9"),
	THEIR_DISCARD_10 ("THEIR_DISCARD_10"),
	THEIR_DISCARD_11 ("THEIR_DISCARD_11"),
	THEIR_DISCARD_12 ("THEIR_DISCARD_12"),
	THEIR_DISCARD_13 ("THEIR_DISCARD_13"),
	THEIR_DISCARD_14 ("THEIR_DISCARD_14"),
	THEIR_DISCARD_15 ("THEIR_DISCARD_15"),
	THEIR_DISCARD_16 ("THEIR_DISCARD_16"),
	THEIR_DISCARD_17 ("THEIR_DISCARD_17"),
	THEIR_DISCARD_18 ("THEIR_DISCARD_18"),
	THEIR_DISCARD_19 ("THEIR_DISCARD_19"),
	THEIR_DISCARD_20 ("THEIR_DISCARD_20"),
	THEIR_DISCARD_21 ("THEIR_DISCARD_21"),
	THEIR_DISCARD_22 ("THEIR_DISCARD_22"),
	THEIR_DISCARD_23 ("THEIR_DISCARD_23"),
	THEIR_DISCARD_24 ("THEIR_DISCARD_24"),
	THEIR_DISCARD_25 ("THEIR_DISCARD_25"),
	THEIR_DISCARD_26 ("THEIR_DISCARD_26"),
	THEIR_DISCARD_27 ("THEIR_DISCARD_27"),
	THEIR_DISCARD_28 ("THEIR_DISCARD_28"),
	THEIR_DISCARD_29 ("THEIR_DISCARD_29"),
	THEIR_DISCARD_30 ("THEIR_DISCARD_30"),
	THEIR_DISCARD_31 ("THEIR_DISCARD_31"),
	THEIR_DISCARD_32 ("THEIR_DISCARD_32"),
	THEIR_DISCARD_33 ("THEIR_DISCARD_33"),
	THEIR_DISCARD_34 ("THEIR_DISCARD_34"),
	THEIR_DISCARD_35 ("THEIR_DISCARD_35"),
	THEIR_DISCARD_36 ("THEIR_DISCARD_36"),
	THEIR_DISCARD_37 ("THEIR_DISCARD_37"),
	THEIR_DISCARD_38 ("THEIR_DISCARD_38"),
	THEIR_DISCARD_39 ("THEIR_DISCARD_39"),
	THEIR_DISCARD_40 ("THEIR_DISCARD_40"),
	THEIR_DISCARD_41 ("THEIR_DISCARD_41"),
	THEIR_DISCARD_42 ("THEIR_DISCARD_42"),
	THEIR_DISCARD_43 ("THEIR_DISCARD_43"),
	THEIR_DISCARD_44 ("THEIR_DISCARD_44"),
	THEIR_DISCARD_45 ("THEIR_DISCARD_45"),
	THEIR_DISCARD_46 ("THEIR_DISCARD_46"),
	THEIR_DISCARD_47 ("THEIR_DISCARD_47"),
	THEIR_DISCARD_48 ("THEIR_DISCARD_48"),
	THEIR_DISCARD_49 ("THEIR_DISCARD_49"),
	THEIR_DISCARD_50 ("THEIR_DISCARD_50"),
	THEIR_DECK_1 ("THEIR_DECK_1"),
	THEIR_DECK_2 ("THEIR_DECK_2"),
	THEIR_DECK_3 ("THEIR_DECK_3"),
	THEIR_DECK_4 ("THEIR_DECK_4"),
	THEIR_DECK_5 ("THEIR_DECK_5"),
	THEIR_DECK_6 ("THEIR_DECK_6"),
	THEIR_DECK_7 ("THEIR_DECK_7"),
	THEIR_DECK_8 ("THEIR_DECK_8"),
	THEIR_DECK_9 ("THEIR_DECK_9"),
	THEIR_DECK_10 ("THEIR_DECK_10"),
	THEIR_DECK_11 ("THEIR_DECK_11"),
	THEIR_DECK_12 ("THEIR_DECK_12"),
	THEIR_DECK_13 ("THEIR_DECK_13"),
	THEIR_DECK_14 ("THEIR_DECK_14"),
	THEIR_DECK_15 ("THEIR_DECK_15"),
	THEIR_DECK_16 ("THEIR_DECK_16"),
	THEIR_DECK_17 ("THEIR_DECK_17"),
	THEIR_DECK_18 ("THEIR_DECK_18"),
	THEIR_DECK_19 ("THEIR_DECK_19"),
	THEIR_DECK_20 ("THEIR_DECK_20"),
	THEIR_DECK_21 ("THEIR_DECK_21"),
	THEIR_DECK_22 ("THEIR_DECK_22"),
	THEIR_DECK_23 ("THEIR_DECK_23"),
	THEIR_DECK_24 ("THEIR_DECK_24"),
	THEIR_DECK_25 ("THEIR_DECK_25"),
	THEIR_DECK_26 ("THEIR_DECK_26"),
	THEIR_DECK_27 ("THEIR_DECK_27"),
	THEIR_DECK_28 ("THEIR_DECK_28"),
	THEIR_DECK_29 ("THEIR_DECK_29"),
	THEIR_DECK_30 ("THEIR_DECK_30"),
	THEIR_DECK_31 ("THEIR_DECK_31"),
	THEIR_DECK_32 ("THEIR_DECK_32"),
	THEIR_DECK_33 ("THEIR_DECK_33"),
	THEIR_DECK_34 ("THEIR_DECK_34"),
	THEIR_DECK_35 ("THEIR_DECK_35"),
	THEIR_DECK_36 ("THEIR_DECK_36"),
	THEIR_DECK_37 ("THEIR_DECK_37"),
	THEIR_DECK_38 ("THEIR_DECK_38"),
	THEIR_DECK_39 ("THEIR_DECK_39"),
	THEIR_DECK_40 ("THEIR_DECK_40"),
	THEIR_DECK_41 ("THEIR_DECK_41"),
	THEIR_DECK_42 ("THEIR_DECK_42"),
	THEIR_DECK_43 ("THEIR_DECK_43"),
	THEIR_DECK_44 ("THEIR_DECK_44"),
	THEIR_DECK_45 ("THEIR_DECK_45"),
	THEIR_DECK_46 ("THEIR_DECK_46"),
	THEIR_DECK_47 ("THEIR_DECK_47"),
	THEIR_DECK_48 ("THEIR_DECK_48"),
	THEIR_DECK_49 ("THEIR_DECK_49"),
	THEIR_DECK_50 ("THEIR_DECK_50");

	private static CardPos[] positions = values();

	private static EnumMap<CardPos, Point3D> cardPosMap = new EnumMap<CardPos, Point3D>(CardPos.class);
	private String stringValue;
	CardPos(String toString) {
		stringValue = toString;
	}

	static {

		// Map card positions
		Point3D myHandPos = new Point3D(355,-80,0);
		CardPos p = CardPos.MY_HAND_1;
		for(int i=0; i<10; i++) {
			cardPosMap.put(p, new Point3D(myHandPos.getX() - 50 +(75*i),
					myHandPos.getY() - 50,
					myHandPos.getZ()+(10-i)-100));
			p = p.next();

		}

		cardPosMap.put(MY_DECREE, new Point3D(160,1080-1,-2+1080+270));
		cardPosMap.put(MY_MAGE_1, new Point3D(466,1080-1,162+1080+270));
		cardPosMap.put(MY_SOLDIER_1, new Point3D(760,1080-1,162+1080+270));
		cardPosMap.put(MY_SOLDIER_2, new Point3D(1084,1080-1,162+1080+270));
		cardPosMap.put(MY_MAGE_2, new Point3D(1371,1080-1,162+1080+270));
		
		Point3D myDiscardPos = new Point3D(1665,1080-1,162+1080+270);
		p = CardPos.MY_DISCARD_1;
		for(int i=0; i<50; i++) {
			cardPosMap.put(p, new Point3D(myDiscardPos.getX(),
					myDiscardPos.getY()-i,
					myDiscardPos.getZ()));
			p = p.next();
		}
		
		cardPosMap.put(MY_TRICKSTER_1, new Point3D(628,1080-1,-162+1080+270));
		cardPosMap.put(MY_CASTLE, new Point3D(912,1080-1,-162+1080+270));
		cardPosMap.put(MY_TRICKSTER_2, new Point3D(1208,1080-1,-162+1080+270));

		Point3D myDeckPos = new Point3D(1665,1080-1,-162+1080+270);
		p = CardPos.MY_DECK_1;
		for(int i=0; i<50; i++) {
			cardPosMap.put(p, new Point3D(myDeckPos.getX(),
					myDeckPos.getY()-i,
					myDeckPos.getZ()));
			p = p.next();
		}

		Point3D theirHandPos = new Point3D(355,0,1080);
		p = CardPos.THEIR_HAND_1;
		for(int i=0; i<10; i++) {
			cardPosMap.put(p, new Point3D(theirHandPos.getX()+(75*i),
					theirHandPos.getY() + 100,
					theirHandPos.getZ()+(10-i)+150));
			p = p.next();

		}

		cardPosMap.put(THEIR_DECREE, new Point3D(1681,1080-1,936+1080+270));
		cardPosMap.put(THEIR_MAGE_1, new Point3D(1375,1080-1,772+1080+270));
		cardPosMap.put(THEIR_SOLDIER_1, new Point3D(1081,1080-1,772+1080+270));
		cardPosMap.put(THEIR_SOLDIER_2, new Point3D(757,1080-1,772+1080+270));
		cardPosMap.put(THEIR_MAGE_2, new Point3D(469,1080-1,772+1080+270));
		
		Point3D theirDiscardPos = new Point3D(175,1080-1,772+1080+270);
		p = CardPos.THEIR_DISCARD_1;
		for(int i=0; i<50; i++) {
			cardPosMap.put(p, new Point3D(theirDiscardPos.getX(),
					theirDiscardPos.getY()-i,
					theirDiscardPos.getZ()));
			p = p.next();
		}
		
		cardPosMap.put(THEIR_TRICKSTER_1, new Point3D(1210,1080-1,1098+1080+270));
		cardPosMap.put(THEIR_CASTLE, new Point3D(926,1080-1,1098+1080+270));
		cardPosMap.put(THEIR_TRICKSTER_2, new Point3D(630,1080,1098+1080+270));

		Point3D theirDeckPos = new Point3D(175,1080-1,1098+1080+270);
		p = CardPos.THEIR_DECK_1;
		for(int i=0; i<50; i++) {
			cardPosMap.put(p, new Point3D(theirDeckPos.getX(),
					theirDeckPos.getY()-i,
					theirDeckPos.getZ()));
			p = p.next();
		}
	
	}
	
	/**
	 * 
	 * @return Returns the next card position
	 */
	public CardPos next() {
		return positions[(this.ordinal()+1)%positions.length];
	}
	public CardPos previous() {
		return positions[(this.ordinal()-1)%positions.length];
	}
	
	/**
	 * 
	 * @param p The CardPos key
	 * @return Returns the specific point on the scene of a card at CardPos p
	 */
	public static Point3D calcPosition(CardPos p) {
		return cardPosMap.get(p);
	}
	
	public static boolean isAttackable(CardPos p) {
		return (p.compareTo(THEIR_TRICKSTER_2)<=0) && (p.compareTo(THEIR_DECREE)>=0);
	}

	public static boolean isInMyHand(CardPos p){
		return p.compareTo(MY_HAND_10)<=0;
	}

	public static boolean isInTheirHand(CardPos p){
		return (p.compareTo(THEIR_HAND_10)<=0) && (p.compareTo(THEIR_HAND_1)>=0);
	}

	public static boolean isOnTheBoard(CardPos p){
		return ((p.compareTo(THEIR_TRICKSTER_2)<=0) && (p.compareTo(THEIR_DECREE)>=0))
				||((p.compareTo(MY_TRICKSTER_2)<=0) && (p.compareTo(MY_DECREE)>=0));
	}

	public static boolean isInTheDeck(CardPos p){
		return ((p.compareTo(THEIR_DECK_50)<=0) && (p.compareTo(THEIR_DECK_1)>=0))
				||((p.compareTo(MY_DECK_50)<=0) && (p.compareTo(MY_DECK_1)>=0));
	}

	public static boolean isDiscarded(CardPos p){
		return ((p.compareTo(THEIR_DISCARD_50)<=0) && (p.compareTo(THEIR_DISCARD_1)>=0))
				||((p.compareTo(MY_DISCARD_50)<=0) && (p.compareTo(MY_DISCARD_1)>=0));
	}

	public static boolean isTheirTrickster(CardPos p){
		return ((p.compareTo(THEIR_TRICKSTER_1)==0) || (p.compareTo(THEIR_TRICKSTER_2)==0));
	}

	public String toString() {
		return stringValue;
	}
}
