package utils;

import java.util.HashMap;

import gamelogic.*;


public class Dictionary 
{
	private static HashMap<Integer, Class<?>> dictionary = new HashMap<>();
	 
	 static {
		 dictionary.put(0,CastleWindshireHold.class);
		 dictionary.put(1,CastleCalterburryFortress.class);
		 dictionary.put(2,CastleWaretonRidge.class);
		 dictionary.put(3,CastleStonehillKeep.class);
		 dictionary.put(4,CastleTortmainFortress.class);
		 dictionary.put(5,CastleChateauDuDraguizo.class);
		
		 dictionary.put(6,DecreeSacrifice.class);
		 dictionary.put(7,DecreeCallforArms.class);
		 dictionary.put(8,DecreeSpeedUp.class);
		 dictionary.put(9,DecreeDestroy.class);
		 
		 dictionary.put(10,SoldierPawnWarrior.class);
		 dictionary.put(11,SoldierSquire.class);
		 dictionary.put(12, SoldierRampantGiant.class);
		 dictionary.put(13,SoldierSupport.class);
		 dictionary.put(14,SoldierPaladin.class);
		 dictionary.put(15,SoldierSeeker.class); 
		 dictionary.put(16,SoldierArcher.class);
		 dictionary.put(17, SoldierCavalry.class);
		 dictionary.put(18,SoldierCannonmen.class);
		 dictionary.put(19,SoldierSiege.class);
		 dictionary.put(20,SoldierSacrfice.class);
		 dictionary.put(21,SoldierCastleKnight.class); 
		 dictionary.put(22,SoldierKnight.class);
		 dictionary.put(23,SoldierBarbarian.class);
		 dictionary.put(24,SoldierWarlock.class);
		 dictionary.put(25,SoldierCentaur.class);
		 dictionary.put(26,SoldierWarrior.class); 
		 dictionary.put(27,SoldierMagesPartner.class);
		 
		 dictionary.put(28,TricksterTheJestersJester.class);
		 dictionary.put(29,TricksterReinforcements.class);
		 dictionary.put(30,TricksterAntiMage.class);
		 dictionary.put(31,TricksterTheWanderer.class);
		 dictionary.put(32,TricksterOpportunist.class);
		 dictionary.put(33,TricksterShaman.class);
		 dictionary.put(34,TricksterFireHandler.class);
		 dictionary.put(35,TricksterIceHandler.class);
		 dictionary.put(36,TricksterSlayer.class);
		 dictionary.put(37,TricksterDisciple.class);
		 dictionary.put(38,TricksterFate.class);
		 dictionary.put(39,TricksterSwap.class);
		 dictionary.put(40,TricksterMirage.class);
		 dictionary.put(41,TricksterPlague.class);
		 
		 dictionary.put(42,MageSorcerer.class);
		 dictionary.put(43,MageApprentice.class);
		 dictionary.put(44,MageAttacker.class);
		 dictionary.put(45,MageDefender.class);
		 dictionary.put(46,MageDeath.class);
		 dictionary.put(47,MageSoldiersPartner.class); 
		 dictionary.put(48,MageWizard.class);
		 dictionary.put(49,MageGlory.class);
		 dictionary.put(50,MageTribute.class);
		 dictionary.put(51,MageSeductress.class);
		 dictionary.put(52,MageTheUndead.class);
		 dictionary.put(53,MageMut.class); 
		 dictionary.put(54,MageLight.class);
		 dictionary.put(55,MageStorm.class);
		 dictionary.put(56,MageOracle.class);
		 dictionary.put(57,MageDemon.class);
		 dictionary.put(58,MageProtector.class);
	 }

	 public static Class<?> getEntry(Integer entry) {
	 	return dictionary.get(entry);
	 }
}
