package com.ninjaphase.pokered.editor.dialogs;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.data.entity.Entity;
import com.ninjaphase.pokered.editor.data.entity.event.EntityEvent;
import com.ninjaphase.pokered.editor.data.entity.event.node.EventNode;

import javax.swing.*;
import java.awt.*;

/**
 * <p>
 *     The {@code EventEditDialog} is used to edit events for a given entity.
 * </p>
 */
public class EventEditDialog extends JDialog {

    private Entity e;

    private JList<EventNode> events;
    private DefaultListModel<EventNode> eventModel;

    /**
     * <p>
     *     Constructs a new {@code NewMapDialog}
     * </p>
     */
    public EventEditDialog(Entity e) {
        super(PokeredEditor.getEditor(), "Edit Event", true);
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        this.setContentPane(contentPane);
        this.eventModel = new DefaultListModel<>();
        this.e = e;
        for(EntityEvent ev : this.e.getEvents()) {
            this.eventModel.addElement(ev.getNode());
        }
        this.eventModel.addElement(new EventNode(null));
        this.addComponents();
        this.pack();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(PokeredEditor.getEditor());
        this.setVisible(true);
    }

    /**
     * <p>Adds a component.</p>
     */
    private void addComponents() {
        this.events = new JList<>(this.eventModel);
        this.events.setBorder(BorderFactory.createLoweredBevelBorder());
        this.add(events);
    }
}
