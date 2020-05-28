package sound;
import java.util.concurrent.TimeUnit;

public class Audiotest 
{	
	public static void main(String[] args) throws InterruptedException
	{
		/*TEST FOR THE BACKGROUND_MUSIC CLASS AND THE SOUNDFX CLASS
		 * Test whether the background music and lot of soundFX resources could play together
		 * ATTENTION:
		 * The volume of sound is between 0 and 200.
		 */
//---------------------------------------------------------------------------------------------------------------------------------------------------		
		/*TEST FOR THE BACKGROUND_MUSIC CLASS	
		 * 1. Test whether the background music resource can be played
		 * 2. Test whether the background music resource can be stopped
		 * 3. Test whether the background music resource can be contiuned
		 * 4. Test whether we can control the volume of	background music
		 */
		/*Background_Music Test = new Background_Music();
		Test.playBackground_Music();
		TimeUnit.SECONDS.sleep(7);
		Test.Stop();
		TimeUnit.SECONDS.sleep(4);
		Test.Continues();
		TimeUnit.SECONDS.sleep(4);
		Test.SetFXsoundVolume(0);
		TimeUnit.SECONDS.sleep(4);
		Test.SetFXsoundVolume(200);
		TimeUnit.SECONDS.sleep(4);
		Test.SetFXsoundVolume(100);
		TimeUnit.SECONDS.sleep(4);
		Test.SetFXsoundVolume(50);
		TimeUnit.SECONDS.sleep(4);
		Test.SetFXsoundVolume(30);*/
			
//---------------------------------------------------------------------------------------------------------------------------------------------------		
		/*TEST FOR THE SOUNDFX CLASS
		 * 1. Test whether all sound resource can be played
		 * 2. Test whether we can control the volume of Sound FX
		 * ATTENTION:
		 * 1.The volume of soundFX is between 0 and 200.
		 * 2.All audio files cannot be played in a loop.(If you want to loop it, you can change false to true in function start())
		 */
		
		SoundFX test = new SoundFX();
		//test.play_Physical();
        //TimeUnit.SECONDS.sleep(3);
		//test.play_Magical();
		//test.SetFXsoundVolume(40);
		//TimeUnit.SECONDS.sleep(3);
		test.play_Outcard();
		//test.SetFXsoundVolume(200);
		TimeUnit.SECONDS.sleep(3);
		test.play_Shuffing();
		//test.SetFXsoundVolume(100);
		TimeUnit.SECONDS.sleep(3);
		test.play_Trickers();
		//test.SetFXsoundVolume(50);
		TimeUnit.SECONDS.sleep(3);
		//test.play_Level();
		//test.SetFXsoundVolume(180);
		TimeUnit.SECONDS.sleep(3);
		test.play_Win();
		//test.SetFXsoundVolume(70);
		TimeUnit.SECONDS.sleep(3);
		test.play_Lose();
		//test.SetFXsoundVolume(90);
		TimeUnit.SECONDS.sleep(3);
		test.play_Mages();
	}
}
