package sound;

public class Background_Music {

	Audio Background_Music = new Audio("src/main/resources/sound/testmusic2.wav");

	public Background_Music()
	{}
	
	public void playBackground_Music()
	{
		Background_Music.start(true);
	}
	
	public void Stop()
	{
		Background_Music.stop();
	}
	
	public void Continues()
	{
		Background_Music.continues();
	}
	
	public void SetFXsoundVolume(int vol)
	{
		Background_Music.setVolume(vol);
	}
}
