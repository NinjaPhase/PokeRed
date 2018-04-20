package com.ninjaphase.pokered.editor.dialog;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.data.story.ResourceType;
import com.ninjaphase.pokered.editor.data.story.Story;
import com.ninjaphase.pokered.editor.data.story.tilemap.TileMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>
 *     The {@code ResourceDialog} is used to manage the resources within a story.
 * </p>
 */
public class ResourceDialog implements Initializable {

    @FXML private ListView<ResourceType> lvResourceTypes;
    @FXML private ListView<String> lvResources;
    @FXML private Button btnPreview, btnDelete;

    private Story s;

    private Stage dialogStage;

    private ResourceType selectedResourceType;

    /**
     * <p>
     *     Constructs a new {@code ResourceDialog}.
     * </p>
     *
     * @param s The story.
     */
    public ResourceDialog(Story s) {
        this.s = s;
        try {
            this.dialogStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/resource_form.fxml"));
            loader.setController(this);

            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 441);

            dialogStage.setTitle("Database Manager");
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.initOwner(PokeredEditor.getPrimaryStage());
            dialogStage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(ResourceType resourceType : ResourceType.values()) {
            this.lvResourceTypes.getItems().add(resourceType);
        }
        this.lvResourceTypes.getSelectionModel().selectFirst();
        this.setResourceType(this.lvResourceTypes.getSelectionModel().getSelectedItem());
        this.lvResources.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.btnPreview.setDisable(newValue == null);
            this.btnDelete.setDisable(newValue == null);
        });
    }

    @FXML public void onResourceTypesListClick(MouseEvent e) {
        if(e.getClickCount() == 2
                && this.selectedResourceType != this.lvResourceTypes.getSelectionModel().getSelectedItem()) {
            this.setResourceType(this.lvResourceTypes.getSelectionModel().getSelectedItem());
        }
    }

    @FXML public void onImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Resource");
        if(this.selectedResourceType.getClassType() == Image.class) {
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Graphic Files", "*.png", "*.jpg", "*.bmp"));
        }
        File f = fileChooser.showOpenDialog(this.dialogStage);

        if(f != null) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                Image image = new Image(fis);
                if(image.isError())
                    throw image.getException();

                String name = f.getName().substring(0, f.getName().lastIndexOf("."));

                this.s.getResourceManager().addResource(this.selectedResourceType, name, image);
                this.lvResources.getItems().add(name);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @FXML public void onPreview() {
        Image img = (Image) this.s.getResourceManager().getResource(
                this.selectedResourceType, lvResources.getSelectionModel().getSelectedItem());
        Stage previewStage = new Stage();
        VBox vBox = new VBox();
        {
            vBox.setSpacing(8.0);
            vBox.setPadding(new Insets(8));

            ScrollPane scrollPane = new ScrollPane();
            {
                VBox.setVgrow(scrollPane, Priority.ALWAYS);

                scrollPane.setContent(new ImageView(img));
            }
            vBox.getChildren().add(scrollPane);
            HBox hBox = new HBox();
            {
                Pane pane = new Pane();
                HBox.setHgrow(pane, Priority.ALWAYS);

                Button btnClose = new Button("Close");
                btnClose.setOnAction((event) -> {
                    previewStage.close();
                });
                btnClose.setMinWidth(80.0);

                hBox.getChildren().add(pane);
                hBox.getChildren().add(btnClose);
            }
            vBox.getChildren().add(hBox);
        }

        previewStage.setScene(new Scene(vBox, 600, 400));
        previewStage.setResizable(false);
        previewStage.setTitle("Preview of " + lvResources.getSelectionModel().getSelectedItem());
        previewStage.initOwner(this.dialogStage);
        previewStage.initModality(Modality.APPLICATION_MODAL);
        previewStage.showAndWait();
    }

    @FXML public void onDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("You are about to delete '" + this.lvResources.getSelectionModel().getSelectedItem() + "'");
        alert.setContentText("Are you sure you would like to delete " + this.lvResources.getSelectionModel().getSelectedItem() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            String item =  this.lvResources.getSelectionModel().getSelectedItem();
            this.s.getResourceManager().removeResource(this.selectedResourceType, item);
            this.lvResources.getItems().remove(item);
        }
    }

    @FXML public void onClose() {
        this.dialogStage.close();
    }

    public void showAndWait() {
        this.dialogStage.showAndWait();
    }

    /**
     * <p>
     *     Sets the current resource type.
     * </p>
     *
     * @param resourceType The resource type.
     */
    private void setResourceType(ResourceType resourceType) {
        this.selectedResourceType = resourceType;
        this.lvResources.getItems().clear();
        for(String key : this.s.getResourceManager().getResources(resourceType)) {
            this.lvResources.getItems().add(key);
        }
    }
}
