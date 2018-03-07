package com.ninjaphase.pokered.editor.dialogs;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.data.TileMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>
 *     The {@code NewMapDialog} is used to display a prompt to create new maps.
 * </p>
 */
public class NewMapDialog extends JDialog implements ActionListener {

    private JPanel pnlGeneral, pnlButtons;
    private JTextField txtName;
    private JSpinner spWidth, spHeight;
    private JButton btnCreate, btnCancel;

    private TileMap tileMap;

    /**
     * <p>
     *     Constructs a new {@code NewMapDialog}
     * </p>
     */
    public NewMapDialog() {
        super(PokeredEditor.getEditor(), "Create a New Map", true);
        this.addComponents();
        this.pack();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(PokeredEditor.getEditor());
    }

    /**
     * <p>
     *     Adds the components to the new map dialog.
     * </p>
     */
    private void addComponents() {
        getContentPane().setLayout(new BorderLayout());
        pnlGeneral = new JPanel(new GridLayout(3,2));
        {
            txtName = new JTextField(15);
            pnlGeneral.add(new JLabel("Map Name: "), "0, 0");
            pnlGeneral.add(txtName, "1, 0");
            spWidth = new JSpinner(new SpinnerNumberModel(20, 1, 200, 1));
            spHeight = new JSpinner(new SpinnerNumberModel(15, 1, 200, 1));
            pnlGeneral.add(new JLabel("Dimensions"), "0, 1");
            JPanel pnlDim = new JPanel(new GridLayout(1, 2));
            pnlDim.add(spWidth);
            pnlDim.add(spHeight);
            pnlGeneral.add(pnlDim, "1, 2");
        }
        JPanel tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tmp.add(pnlGeneral);
        getContentPane().add(tmp);
        pnlButtons = new JPanel();
        {
            btnCreate = new JButton("Create");
            btnCreate.addActionListener(this);
            btnCancel = new JButton("Cancel");
            btnCancel.addActionListener(this);
            pnlButtons.add(btnCreate);
            pnlButtons.add(btnCancel);
        }
        tmp = new JPanel(new BorderLayout());
        tmp.add(pnlButtons, BorderLayout.EAST);
        getContentPane().add(pnlButtons, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.btnCreate) {
            this.tileMap = new TileMap(this.txtName.getText(),
                    (int)this.spWidth.getValue(), (int)this.spHeight.getValue());
            this.dispose();
        } else if(e.getSource() == this.btnCancel) {
            this.dispose();
        }
    }

    /**
     * @return The newly created tilemap, or {@code null} if cancelled.
     */
    public TileMap getTileMap() {
        return this.tileMap;
    }
}
