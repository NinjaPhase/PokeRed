package com.ninjaphase.pokered.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.ninjaphase.pokered.util.audio.MidiPlayerInterface;

import javax.sound.midi.*;
import java.io.IOException;

/**
 * <p>
 *     The {@code DesktopMidiPlayer} is used to play MIDI tracks on the desktop application.
 * </p>
 */
public class DesktopMidiPlayer implements MidiPlayerInterface {
    private static final int MAX_SEQUENCER_COUNT = 5;

    private Sequencer[] sequencer;

    /**
     * <p>
     *     Constructs a new {@code DesktopMidiPlayer}.
     * </p>
     */
    DesktopMidiPlayer() {
        this.sequencer = new Sequencer[MAX_SEQUENCER_COUNT];
        try {
            for(int i = 0; i < MAX_SEQUENCER_COUNT; i++) {
                this.sequencer[i] = MidiSystem.getSequencer();
                this.sequencer[i].open();
            }
        } catch (MidiUnavailableException e) {
            System.err.println("Unable to open midi sequencer: " + e.getMessage());
            this.sequencer = null;
        }
    }

    @Override
    public void setMidiFile(int id, FileHandle fileHandle) {
        try {
            Sequence sequence = MidiSystem.getSequence(fileHandle.read());
            this.sequencer[id].setSequence(sequence);
        } catch (InvalidMidiDataException | IOException e) {
            throw new GdxRuntimeException(e);
        }
    }

    @Override
    public void play(int id) {
        if(this.sequencer == null || this.sequencer[id] == null)
            return;
        this.sequencer[id].start();
    }

    @Override
    public void stop(int id) {
        if(this.sequencer == null || this.sequencer[id] == null)
            return;
        this.sequencer[id].stop();
    }

    @Override
    public void setLoopPosition(int id, long position) {
        if(this.sequencer == null || this.sequencer[id] == null)
            return;
        this.sequencer[id].setLoopStartPoint(position);
    }

    @Override
    public void setLooping(int id, boolean looping) {
        if(this.sequencer == null || this.sequencer[id] == null)
            return;
        this.sequencer[id].setLoopCount(looping ? -1 : 0);
    }
}
