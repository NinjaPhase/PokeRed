package com.ninjaphase.pokered.editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * <p>
 *     The {@code PokeredEditor} is used to create maps on the PokeRed application.
 * </p>
 */
public class PokeredEditor extends Application {

    private static Stage PRIMARY_STAGE;

    @Override
    public void start(Stage primaryStage) throws Exception {
        PokeredEditor.PRIMARY_STAGE = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("/forms/main_form.fxml"));

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(root, primaryScreenBounds.getWidth() * 0.75, primaryScreenBounds.getHeight() * 0.75);

        primaryStage.setTitle("Pokered Editor");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/icon_main.png")));
        primaryStage.show();
    }

    /**
     * @return The primary JavaFX Stage.
     */
    public static Stage getPrimaryStage() {
        return PokeredEditor.PRIMARY_STAGE;
    }

    /**
     * <p>
     *     Application Entry.
     * </p>
     * @param args The application arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
