package com.ninjaphase.pokered.editor.components.listeners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * <p>
 *     The NumberChangeListener is used to check for number changes on a text field.
 * </p>
 */
public class NumberChangeListener implements ChangeListener<String> {

    private TextField textField;
    private int minValue, maxValue;

    /**
     * <p>
     *     Constructs a new {@code NumberChangeListener}.
     * </p>
     *
     * @param textField The text field.
     */
    public NumberChangeListener(TextField textField, int minValue, int maxValue) {
        this.textField = textField;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
            textField.setText(newValue.replaceAll("[^\\d]", ""));
        }
        if(textField.getText().equalsIgnoreCase(""))
            textField.setText("0");
        int val = Integer.parseInt(textField.getText());
        val = Math.max(Math.min(val, maxValue), minValue);
        textField.setText(String.valueOf(val));
    }
}
