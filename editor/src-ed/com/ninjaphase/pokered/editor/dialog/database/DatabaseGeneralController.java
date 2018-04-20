package com.ninjaphase.pokered.editor.dialog.database;

import com.ninjaphase.pokered.editor.data.story.Story;
import com.ninjaphase.pokered.editor.dialog.DatabaseDialog;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * <p>
 *     The {@code DatabaseGeneralController} is used to control the general settings of the story.
 * </p>
 */
public class DatabaseGeneralController {

    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;

    private Story s;
    private DatabaseDialog databaseDialog;

    /**
     * <p>
     *     Sets the current {@code Story} of the DatabaseGeneralController.
     * </p>
     *
     * @param s The story.
     */
    public void initialise(DatabaseDialog databaseDialog, Story s) {
        this.s = s;
        this.databaseDialog = databaseDialog;
        this.txtName.setText(s.getDatabaseManager().getStoryName());
        this.txtName.textProperty().addListener((change) -> databaseDialog.enableApplyButton());
        this.txtDescription.setText(s.getDatabaseManager().getStoryDescription());
        this.txtDescription.textProperty().addListener((change) -> databaseDialog.enableApplyButton());
    }

    /**
     * <p>
     *     Applies the changes to the story.
     * </p>
     */
    public void apply() {
        this.s.getDatabaseManager().setStoryName(this.txtName.getText());
        this.s.getDatabaseManager().setStoryDescription(this.txtDescription.getText());
    }

}
