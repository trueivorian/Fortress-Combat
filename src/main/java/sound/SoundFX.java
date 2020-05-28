package sound;


public class SoundFX {
	
	Audio Physical = new Audio("src/main/resources/sound/Physical_attack.wav");// The sound of physical attack
	Audio Magical  = new Audio("src/main/resources/sound/Magical_attack.wav");// The sound of Magical attack
	Audio Outcard  = new Audio("src/main/resources/sound/Out__card.wav");// The sound of drawing a card
	Audio Shuffing = new Audio("src/main/resources/sound/Shuffing.wav");// The sound of shuffing cards
	Audio Decree   = new Audio("src/main/resources/sound/Decree.wav");// The sound of use decree
	Audio Trickers = new Audio("src/main/resources/sound/Trickers.wav");// The sound of trigger the trap
	Audio Level    = new Audio("src/main/resources/sound/Level_up.wav");// The sound of mages level up
	Audio Win      = new Audio("src/main/resources/sound/Win__1.wav");// The sound of WIN the game P.S. Win__2.wav could also be used
	Audio Lose     = new Audio("src/main/resources/sound/Lose__1.wav");// The sound of LOSE the game P.S. Lose__2.wav could also be used

	
	public SoundFX()
	{}
	
	public void play_Physical()
	{
		Physical.start(false);
	}
	
	public void play_Magical()
	{
		Magical.start(false);
	}
	
	public void play_Outcard()
	{
		Outcard.start(false);
	}
	
	public void play_Shuffing()
	{
		Shuffing.start(false);
	}
	
	public void play_Mages()
	{
		Decree.start(false);
	}
	
	public void play_Trickers()
	{
		Trickers.start(false);
	}
	
	public void play_Level()
	{
		Level.start(false);
	}
	
	public void play_Win()
	{
		Win.start(false);
	}
	
	public void play_Lose()
	{
		Lose.start(false);
	}
	
	public void SetFXsoundVolume(int vol)
	{
		//System.out.println("setting sound fx in SetFXsoundVolume to " + vol);
		Physical.setVolume(vol);
		Magical.setVolume(vol);
		Outcard.setVolume(vol);
		Shuffing.setVolume(vol);
		Decree.setVolume(vol);
		Trickers.setVolume(vol);
		Level.setVolume(vol);
		Win.setVolume(vol);
		Lose.setVolume(vol);
	}
}