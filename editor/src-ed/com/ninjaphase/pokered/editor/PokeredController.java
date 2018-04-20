package com.ninjaphase.pokered.editor;

import com.ninjaphase.pokered.editor.components.MapNode;
import com.ninjaphase.pokered.editor.components.TileMapEditor;
import com.ninjaphase.pokered.editor.components.TileSetViewer;
import com.ninjaphase.pokered.editor.data.DataManager;
import com.ninjaphase.pokered.editor.data.exception.StoryLoadException;
import com.ninjaphase.pokered.editor.data.exception.StoryWriteException;
import com.ninjaphase.pokered.editor.data.story.Story;
import com.ninjaphase.pokered.editor.data.story.tilemap.TileMap;
import com.ninjaphase.pokered.editor.data.listeners.MapChangeListener;
import com.ninjaphase.pokered.editor.data.listeners.StoryChangeListener;
import com.ninjaphase.pokered.editor.dialog.DatabaseDialog;
import com.ninjaphase.pokered.editor.dialog.NewMapDialog;
import com.ninjaphase.pokered.editor.dialog.NewStoryDialog;
import com.ninjaphase.pokered.editor.dialog.ResourceDialog;
import com.ninjaphase.pokered.editor.util.Constants;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>
 *     The {@code PokeredController} is the controller for the application.
 * </p>
 */
public class PokeredController implements Initializable, MapChangeListener, StoryChangeListener {

    @FXML private MenuItem miClose, miSave, miSaveAs;
    @FXML private TreeView<MapNode> trMaps;
    @FXML private TileMapEditor cvsTilemap;
    @FXML private TileSetViewer cvsTileset;
    @FXML private Button btnDatabase, btnResource;
    @FXML private ToggleButton btnPaint, btnEvents, btnLayerOne, btnLayerTwo, btnLayerThree;
    @FXML private ToggleGroup grpLayers;

    private Story story;
    private TileMap map;
    private DataManager dataManager;
    private FileChooser fileChooser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fileChooser = new FileChooser();
        this.cvsTilemap.init(this.cvsTileset);
        this.dataManager = new DataManager();
        this.dataManager.addMapChangeListener(this);
        this.dataManager.addStoryChangeListener(this);
        this.onStoryChange(null);
        this.grpLayers.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if((newValue == null)) {
                Platform.runLater(() -> this.grpLayers.selectToggle(oldValue));
            }
        }));
    }

    /**
     * <p>
     *     Sets up the tilemap tree.
     * </p>
     *
     * @param s The story to setup the tilemap tree for.
     */
    private void setupTileMapTree(Story s) {
        TreeItem<MapNode> root = s == null ? null : new TreeItem<>(s);
        if(s == null) {
            this.trMaps.setRoot(null);
        } else {
            root.setExpanded(true);
            root.setGraphic(s.getIcon());

            for(TileMap t : s.getDatabaseManager().getRootMaps()) {
                addTileMap(root, t);
            }
        }

        this.trMaps.setRoot(root);
        this.trMaps.getSelectionModel().selectFirst();
    }

    /**
     * <p>
     *     Adds the tilemap to the game.
     * </p>
     *
     * @param parent The parent.
     * @param t The tilemap.
     */
    private void addTileMap(TreeItem<MapNode> parent, TileMap t) {
        TreeItem<MapNode> tileMapNode = new TreeItem<>(t);
        tileMapNode.setGraphic(t.getIcon());
        for(TileMap child : t.getChildren()) {
            addTileMap(tileMapNode, child);
        }

        parent.getChildren().add(tileMapNode);
    }

    @FXML public void onLayerClick(ActionEvent e) {
        if(e.getSource() == this.btnLayerOne) {
            this.cvsTilemap.setLayer(0);
        } else if(e.getSource() == this.btnLayerTwo) {
            this.cvsTilemap.setLayer(1);
        } else if(e.getSource() == this.btnLayerThree) {
            this.cvsTilemap.setLayer(2);
        }
     }

    @FXML public void onDatabaseOpen() {
        new DatabaseDialog(this.story).showAndWait();
        this.trMaps.refresh();
    }

    @FXML public void onResourceOpen() {
        new ResourceDialog(this.story).showAndWait();
    }

    @FXML public void onNewItem() {
        NewStoryDialog dialog = new NewStoryDialog();

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            this.dataManager.setStory(dialog.getStory());
        }
    }

    @FXML public void onMapProperties() {
        if(!(this.trMaps.getSelectionModel().getSelectedItem().getValue() instanceof TileMap))
            return;
        NewMapDialog newMapDialog = new NewMapDialog(
                (TileMap) this.trMaps.getSelectionModel().getSelectedItem().getValue(), this.story);

        newMapDialog.showAndWait();
        this.trMaps.refresh();

        this.cvsTilemap.updateTileMap();
    }

    @FXML public void onNewMap() {
        NewMapDialog newMapDialog = new NewMapDialog(null, this.story);

        Optional<ButtonType> result = newMapDialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            TileMap newMap = newMapDialog.getTileMap();
            TreeItem<MapNode> node = this.trMaps.getSelectionModel().getSelectedItem();

            if (node.getValue() instanceof Story) {
                ((Story) node.getValue()).getDatabaseManager().getRootMaps().add(newMap);
            } else if (node.getValue() instanceof TileMap) {
                ((TileMap) node.getValue()).getChildren().add(newMap);
            }
            addTileMap(node, newMap);
            this.dataManager.setTileMap(newMap);
        }
    }

    @FXML public void onDeleteMap() {
        TreeItem<MapNode> node = this.trMaps.getSelectionModel().getSelectedItem();

        if(node.getValue() instanceof TileMap) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are you sure?");
            alert.setHeaderText("You are about to delete '" + ((TileMap) node.getValue()).getName() + "'");
            alert.setContentText("Are you sure you would like to delete " + ((TileMap) node.getValue()).getName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                if(((TileMap) node.getValue()).getParent() != null) {
                    ((TileMap) node.getValue()).getParent().getChildren().remove(node.getValue());
                } else {
                    this.story.getDatabaseManager().getRootMaps().remove(node.getValue());
                }
                node.getParent().getChildren().remove(node);
                this.dataManager.setTileMap(null);
            }
        }
    }

    @FXML public void onChangeMap(MouseEvent me) {
        if(me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2) {
            TreeItem<MapNode> tileMapNode = this.trMaps.getSelectionModel().getSelectedItem();

            if(tileMapNode.getValue() instanceof TileMap) {
                this.dataManager.setTileMap((TileMap) tileMapNode.getValue());
            }
        }
    }

    @FXML public void onSaveStory(ActionEvent e) {
        File f = this.dataManager.getStory().getFile();
        if(e.getSource() == this.miSaveAs || f == null) {
            this.fileChooser.setTitle("Save Story");
            this.fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("ZIP File", "*.zip"));
            f = this.fileChooser.showSaveDialog(PokeredEditor.getPrimaryStage());
            if(f == null)
                return;
            if(!f.getName().toLowerCase().endsWith(".zip"))
                f = new File(f.getAbsolutePath() + ".zip");
        }
        try {
            this.dataManager.getStory().save(f);
        } catch (StoryWriteException exc) {
            exc.printStackTrace();
        }
    }

    @FXML public void onLoadStory(ActionEvent e) {
        this.fileChooser.setTitle("Load Story");
        this.fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("ZIP File", "*.zip"));
        File f = this.fileChooser.showOpenDialog(PokeredEditor.getPrimaryStage());
        if(f == null)
            return;
        try {
            this.dataManager.setStory(new Story(f));
        } catch (StoryLoadException exc) {
            exc.printStackTrace();
        }
    }

    @FXML public void onCloseStory() {
        this.dataManager.setStory(null);
    }

    @Override
    public void onMapChange(TileMap newMap) {
        this.map = newMap;
        this.btnPaint.setDisable(newMap == null);
        this.btnEvents.setDisable(newMap == null);
        this.btnLayerOne.setDisable(newMap == null);
        this.btnLayerTwo.setDisable(newMap == null);
        this.btnLayerThree.setDisable(newMap == null);
        this.cvsTileset.setTileMap(newMap);
        this.cvsTilemap.setTileMap(newMap);
    }

    @Override
    public void onStoryChange(Story s) {
        this.onMapChange(null);
        this.story = s;
        this.btnDatabase.setDisable(s == null);
        this.btnResource.setDisable(s == null);
        this.trMaps.setDisable(s == null);
        this.miClose.setDisable(s == null);
        this.miSave.setDisable(s == null);
        this.miSaveAs.setDisable(s == null);

        this.setupTileMapTree(s);
    }
}
