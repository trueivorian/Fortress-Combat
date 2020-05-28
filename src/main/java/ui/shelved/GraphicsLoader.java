package ui.shelved;

import graphics.Graphics;
import javafx.application.Application;
import javafx.stage.Stage;
import networking.MultiplayerGame;
import networking.exceptions.ServerNotFoundException;

public class GraphicsLoader extends Application {
	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Graphics g = new Graphics(stage);
		Thread gThread = new Thread(g);
		
		try {
			gThread.start();
			MultiplayerGame multiplayerGame = new MultiplayerGame("Sam");
		} catch (ServerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Add the Scene to the Stage
		stage.setScene(g.getGameScene());
		
		// Maximise the Screen
		stage.setMaximized(true);
		
		// Set the Title of the Stage
		stage.setTitle("Fortress Combat");
		
		// Display the Stage
		stage.show();
	}
}
