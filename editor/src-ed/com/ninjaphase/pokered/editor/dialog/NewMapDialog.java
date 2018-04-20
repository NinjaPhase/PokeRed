package com.ninjaphase.pokered.editor.dialog;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.components.listeners.NumberChangeListener;
import com.ninjaphase.pokered.editor.data.story.Story;
import com.ninjaphase.pokered.editor.data.story.tilemap.TileMap;
import com.ninjaphase.pokered.editor.data.story.tilemap.TileSet;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>
 *     The {@code NewMapDialog} is used to create new maps on the application.
 * </p>
 */
public class NewMapDialog implements Initializable {

    private Stage dialog;

    private Story s;
    private TileMap tileMap;

    @FXML private TextField txtName, txtDisplayName;
    @FXML private TextField txtWidth, txtHeight;
    @FXML private ChoiceBox<TileSet> cbTilesets;

    private ButtonType pressed;

    /**
     * <p>
     *     Constructs a NewMapDialog.
     * </p>
     *
     * @param s The story to do this for.
     */
    public NewMapDialog(TileMap map, Story s) {
        this.s = s;
        this.tileMap = map;
        this.dialog = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/newmap_form.fxml"));
            loader.setController(this);

            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);

            dialog.setTitle(
                    map == null ? "Create a New Map" : "Map Properties - ID:" + map.getId());
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.initOwner(PokeredEditor.getPrimaryStage());
            dialog.initModality(Modality.APPLICATION_MODAL);

            this.pressed = ButtonType.CANCEL;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.txtWidth.textProperty().addListener(new NumberChangeListener(this.txtWidth, 2, 200));
        this.txtHeight.textProperty().addListener(new NumberChangeListener(this.txtHeight, 2, 200));

        this.cbTilesets.getItems().clear();
        this.cbTilesets.getItems().addAll(this.s.getDatabaseManager().getTileSets());

        if(this.tileMap != null) {
            this.txtName.setText(this.tileMap.getName());
            this.txtWidth.setText(String.valueOf(this.tileMap.getWidth()));
            this.txtHeight.setText(String.valueOf(this.tileMap.getHeight()));
            this.cbTilesets.getSelectionModel().select(
                    this.s.getDatabaseManager().getTileSet(this.tileMap.getTileSetId()));
        } else {
            this.cbTilesets.getSelectionModel().select(0);
        }

        Platform.runLater(() -> {
            this.txtName.requestFocus();
        });
    }

    /**
     * <p>
     *     Waits for the dialog input and then gets the tilemap accordingly.
     * </p>
     *
     * @return The tilemap.
     */
    public Optional<ButtonType> showAndWait() {
        dialog.showAndWait();
        return Optional.of(this.pressed);
    }

    @FXML public void onOK() {
        if(this.txtName.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Enter a Map Name");
            alert.setContentText("You must enter a map name.");

            alert.showAndWait();
            return;
        }
        this.pressed = ButtonType.OK;
        if(this.tileMap == null) {
            this.tileMap = new TileMap(this.s, this.s.getDatabaseManager().getNextMapId(), this.txtName.getText(),
                    Integer.parseInt(txtWidth.getText()), Integer.parseInt(txtHeight.getText()),
                    this.cbTilesets.getSelectionModel().getSelectedItem().getId());
        } else {
            this.tileMap.setName(this.txtName.getText());
            this.tileMap.resize( Integer.parseInt(txtWidth.getText()), Integer.parseInt(txtHeight.getText()));
        }
        this.dialog.close();
    }

    @FXML public void onCancel() {
        this.tileMap = null;
        this.dialog.close();
    }

    /**
     * @return Gets the tilemap.
     */
    public TileMap getTileMap() {
        return this.tileMap;
    }
}
