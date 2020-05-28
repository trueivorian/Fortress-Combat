package ui;

import ai.AI;
import graphics.Graphics;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import networking.MultiplayerGame;
import networking.ai.management.InstanceManager;
import networking.exceptions.ServerNotFoundException;
import sound.Background_Music;
import sound.SoundFX;
//import ai.AI;

import java.io.IOException;

public class Launcher extends Application {

    private static Stage stage;

    private static Background_Music music;

    private static SoundFX sfx;

    static boolean castlePicked = false;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        launchScreen("main_menu");
        music = new Background_Music();
        music.SetFXsoundVolume(0); // mute annoying music for sanity purposes
        sfx = new SoundFX();
        music.playBackground_Music();
    }

    static void launchScreen(String fileName) {
        fileName = "/screens/" + fileName + ".fxml";
        try {
            Parent root = FXMLLoader.load(MainMenuController.class.getResource(fileName));
            Scene scene = new Scene(root);
            stage.setTitle("Fortress Combat");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(420);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert Message");
        //alert.setHeaderText("Look, a Warning Dialog");
        alert.setContentText(message);
        alert.showAndWait();
    }

    static void adjustVolume(double volume) {
        int vol = (int) volume;
        music.SetFXsoundVolume(vol);
    }

    static void adjustSfx(double volume) {
        int vol = (int) volume;
        sfx.SetFXsoundVolume(vol);
    }

    public static void playTestSFX() {
        sfx.play_Physical();
    }

    public static void playShuffle() {
        sfx.play_Shuffing();
    }

    static void launchGame() {
        Graphics graphicsInstance = new Graphics(stage);
        Thread graphicsThread = new Thread(graphicsInstance);
        graphicsThread.start(); // multi-player connection done in Menu window before gfx starts
        stage.setScene(graphicsInstance.getGameScene()); // Add the Scene to the Stage
        stage.setMaximized(true); // Maximise the Screen
        stage.show();
    }

    static void launchAIGame() {

        boolean brokenAi = false;
        if (!brokenAi) {
            try {
                MultiplayerGame multiplayerGame = new MultiplayerGame("Single Player Guy");
                // launching a multi-player game that is hosting in the background
            } catch (ServerNotFoundException e) {
                e.printStackTrace();
            }
            launchGame();
            InstanceManager instanceManager = new InstanceManager();
            try {
                instanceManager.startAIInstance();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void pauseGame() {
        Launcher.launchScreen("pause_menu");
    }
}
