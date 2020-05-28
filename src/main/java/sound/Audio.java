package sound;
/**
* @see
* @author TheLastOfUs PeihengLi
* @date 16/02/2019
* @version V2.0  
* Description: 
* Audio Part for "Fortress Combat"			   
*/
import javax.sound.sampled.*;
import java.io.*;


public class Audio {
	//audio file 
	private String musicPath;
	//Record whether the audio is played or not
	private volatile boolean run = true;
	//The task thread that plays the audio
	private Thread mainThread;
	private AudioInputStream audioStream;
	private AudioFormat audioFormat;
	private SourceDataLine sourceDataLine;

	public Audio(String musicPath) 
	{
		this.musicPath = musicPath;
		prefetch();
	}
	
	//Data preparation
	private void prefetch(){
		try{
		//Gets the audio input stream
	    audioStream = AudioSystem.getAudioInputStream(new File(musicPath));
		//Gets the audio encoding object
		audioFormat = audioStream.getFormat();
		//Package audio information
		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
				audioFormat,AudioSystem.NOT_SPECIFIED);
		//Use the Info class after wrapping the audio information to create the source data row that ACTS as the source for the mixer
		sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);
		sourceDataLine.open(audioFormat);
		sourceDataLine.start();
		setVolume(50);
		}catch(UnsupportedAudioFileException ex){
			ex.printStackTrace();
		}catch(LineUnavailableException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	//turns off audio read streams and data rows
	protected void finalize() throws Throwable
	{
		super.finalize();
		sourceDataLine.drain();
		sourceDataLine.close();
		audioStream.close();
	}
	
	//Play audio: whether to play in a loop is set by the loop parameter
	private void playMusic(boolean loop)throws InterruptedException {
		try{
				if(loop){
					while(true){
						playMusic();
					}
				}else{
					playMusic();
					//Clear the data row and close it
					sourceDataLine.drain();
					//sourceDataLine.close();
                    // hey owen, it's george. closing this data line will stop sfx playing more than once, so i've removed it
					audioStream.close();
				}
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		
	}
	
	private void playMusic(){
		try{
			synchronized(this){
				run = true;
			}
			//Read the audio data stream through the data row and send it to the mixer;;
			//Data stream transmission processAudioInputStream -> SourceDataLine;
				audioStream = AudioSystem.getAudioInputStream(new File(musicPath));
				int count;
				byte tempBuff[] = new byte[1024];
				while((count = audioStream.read(tempBuff,0,tempBuff.length)) != -1){
					synchronized(this){
					while(!run)
						wait();
					}
					sourceDataLine.write(tempBuff,0,count);
					}
			}catch(UnsupportedAudioFileException ex){
			ex.printStackTrace();
			}catch(IOException ex){
			ex.printStackTrace();
			}catch(InterruptedException ex){
			ex.printStackTrace();
			}
		
	}
	
	//Pause audio
	private void stopMusic(){
		synchronized(this){
			run = false;
			notifyAll();
		}
	}
	
	//Let's go ahead and play music
	private void continueMusic(){
		synchronized(this){
			 run = true;
			 notifyAll();
		}
	}
	
	
	//External call control method: generate the main audio thread;
	public void start(boolean loop)
	{
		mainThread = new Thread(new Runnable(){
			public void run(){
				try {
					playMusic(loop);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		mainThread.start();
		//Default volume
		//setVolume(50);
        // setting volume to 50 here would
	}
	
	//External call control method: pause the audio thread
	public void stop()
	{
		new Thread(new Runnable(){
			public void run(){
				stopMusic();
			}
		}).start();
	}
	
	//External call control method: continue the audio thread
	public void continues()
	{
		new Thread(new Runnable(){
			public void run(){
				continueMusic();
			}
		}).start();
	}
	
	public void setVolume(int volume)
	{
        //System.out.println(volume);
		  try {
		    FloatControl gainControl=(FloatControl)sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
		    BooleanControl muteControl=(BooleanControl)sourceDataLine.getControl(BooleanControl.Type.MUTE);
		    if (volume == 0) {
		      muteControl.setValue(true);
		    }
		 else {
		      muteControl.setValue(false);
		      gainControl.setValue((float)(Math.log(volume / 100d) / Math.log(10.0) * 20.0));
		    }
		  }
		 catch (  Exception e) {
		    //LOGGER.log(Level.WARNING,"unable to set the volume to the provided source",e);
		  }
	}
	
}