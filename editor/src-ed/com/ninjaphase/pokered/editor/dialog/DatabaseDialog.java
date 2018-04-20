package com.ninjaphase.pokered.editor.dialog;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.data.story.Story;
import com.ninjaphase.pokered.editor.dialog.database.DatabaseGeneralController;
import com.ninjaphase.pokered.editor.dialog.database.DatabaseTilesetController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>
 *     The {@code DatabaseDialog} is used to manage the database for the {@code Story}.
 * </p>
 *
 * @author NinjaPhase
 */
public class DatabaseDialog implements Initializable {

    private Story story;

    private Stage dialogStage;
    private ButtonType pressed;

    @FXML private DatabaseGeneralController generalController;
    @FXML private DatabaseTilesetController tilesetController;
    @FXML private Button btnApply;

    /**
     * <p>
     *     Constructs a new {@code DatabaseDialog}.
     * </p>
     *
     * @param story The story.
     */
    public DatabaseDialog(Story story) {
        this.story = story;
        try {
            this.dialogStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/database_form.fxml"));
            loader.setController(this);

            Parent root = loader.load();

            Scene scene = new Scene(root, 700, 600);

            dialogStage.setTitle("Database Manager");
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.initOwner(PokeredEditor.getPrimaryStage());
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            this.pressed = ButtonType.CANCEL;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generalController.initialise(this, this.story);
        tilesetController.setStory(this.story);
    }

    /**
     * <p>
     *     Waits for the dialog input and then gets the tilemap accordingly.
     * </p>
     *
     * @return The tilemap.
     */
    public Optional<ButtonType> showAndWait() {
        this.dialogStage.showAndWait();
        return Optional.of(this.pressed);
    }

    /**
     * <p>
     *     Called when the OK button is pressed.
     * </p>
     */
    @FXML public void onOK() {
        this.generalController.apply();
        this.pressed = ButtonType.OK;
        this.dialogStage.close();
    }

    /**
     * <p>
     *     Called when the Cancel button is pressed.
     * </p>
     */
    @FXML public void onCancel() {
        this.dialogStage.close();
    }

    @FXML public void onApply() {
        this.generalController.apply();
        this.btnApply.setDisable(true);
    }

    /**
     * <p>
     *     Enables the apply button.
     * </p>
     */
    public void enableApplyButton() {
        this.btnApply.setDisable(false);
    }
}
