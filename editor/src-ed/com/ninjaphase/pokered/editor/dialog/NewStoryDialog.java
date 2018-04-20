package com.ninjaphase.pokered.editor.dialog;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.data.story.Story;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>
 *     The {@code NewStoryDialog} is used to create a new story with initial data.
 * </p>
 *
 * @author NinjaPhase
 */
public class NewStoryDialog implements Initializable {

    private Story story;

    private Stage stage;

    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;

    private ButtonType pressed;

    /**
     * <p>
     *     Constructs a new {@code NewStoryDialog}.
     * </p>
     */
    public NewStoryDialog() {
        try {
            this.stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/newstory_form.fxml"));
            loader.setController(this);

            Parent root = loader.load();

            Scene scene = new Scene(root, 480, 270);

            stage.setTitle("Create a New Story");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initOwner(PokeredEditor.getPrimaryStage());
            stage.initModality(Modality.APPLICATION_MODAL);
            this.pressed = ButtonType.CANCEL;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            this.txtName.requestFocus();
        });
    }

    /**
     * <p>
     *     Called when of is pressed.
     * </p>
     */
    @FXML public void onOK() {
        if(this.txtName.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Enter a Story Name");
            alert.setContentText("You must enter a story name.");

            alert.showAndWait();
            return;
        }
        this.story = new Story(this.txtName.getText(), this.txtDescription.getText());
        this.pressed = ButtonType.OK;
        this.stage.close();
    }

    /**
     * <p>
     *     Called when cancel is pressed.
     * </p>
     */
    @FXML public void onCancel() {
        this.pressed = ButtonType.CANCEL;
        this.stage.close();
    }

    /**
     * <p>
     *     Shows this dialog and waits for the result.
     * </p>
     */
    public Optional<ButtonType> showAndWait() {
        this.stage.showAndWait();
        return Optional.of(pressed);
    }

    /**
     * @return The newly created story if ok was pressed, otherwise {@code null}.
     */
    public Story getStory() {
        return this.story;
    }
}
